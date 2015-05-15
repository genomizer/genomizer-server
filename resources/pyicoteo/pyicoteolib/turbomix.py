"""
Pyicoteo is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
"""
from __future__ import with_statement
import os.path
import sys
import os
import logging
import math
import random
from collections import defaultdict
from datetime import datetime
from tempfile import gettempdir

from core import Cluster, Region, InvalidLine, InsufficientData, ConversionNotSupported
from defaults import *
import utils
import bam
import enrichment
from regions import RegionWriter

try:
    from shutil import move
except:
    print "WARNING: Couldn't import shutil, using os.rename instead"
    from os import rename as move 


class NeedFragmentSize(Exception):
    pass


class OperationFailed(Exception):
    pass                 

#FIXME Turbomix became a blob antipattern. Sorry about that, I was trying to do something too ambitious with it. 
# Feel free to extract as much functionality as possible from it, break it down, kill it, etc. Juan GV. 
class Turbomix:
    """
    This class is the pipeline that makes possible the different combination of operations. 
    It has different switches that are activated by the list 'self.operations'.

    """
    def __init__(self, experiment_path, output_path, experiment_format=BED, output_format=PK, label=LABEL, 
                 open_experiment=OPEN_EXPERIMENT, open_output=OPEN_OUTPUT, debug = DEBUG, rounding=ROUNDING, tag_length = TAG_LENGTH, discarded_names = REMLABELS,
                 control_path = CONTROL, control_format = PK, open_control = OPEN_CONTROL, region_path = REGION, region_format = PK, 
                 open_region=OPEN_REGION, span = SPAN, frag_size = FRAG_SIZE, p_value = P_VALUE, height_limit = HEIGHT_LIMIT, correction_factor = CORRECTION,
                 trim_percentage=TRIM_PROPORTION, no_sort=NO_SORT, duplicates=DUPLICATES, threshold=THRESHOLD, trim_absolute=TRIM_ABSOLUTE, max_delta=MAX_DELTA, 
                 min_delta=MIN_DELTA, correlation_height=HEIGHT_FILTER, delta_step=DELTA_STEP, verbose=VERBOSE, species=SPECIES, cached=CACHED, split_proportion=SPLIT_PROPORTION, 
                 split_absolute=SPLIT_ABSOLUTE, repeats=REPEATS, masker_file=MASKER_FILE, max_correlations=MAX_CORRELATIONS, keep_temp=KEEP_TEMP, experiment_b_path=EXPERIMENT,
                 replica_path=EXPERIMENT, replica_b_path=EXPERIMENT, poisson_test=POISSONTEST, stranded_analysis=STRANDED_ANALYSIS, proximity=PROXIMITY, 
                 postscript=POSTSCRIPT, showplots=SHOWPLOTS, plot_path=PLOT_PATH, pseudocount=PSEUDOCOUNT, len_norm=LEN_NORM, label1=LABEL1, 
                 label2=LABEL2, binsize=BINSIZE, zscore=ZSCORE, blacklist=BLACKLIST, sdfold=SDFOLD, recalculate=RECALCULATE, counts_file=COUNTS_FILE, 
                 region_mintags=REGION_MINTAGS, bin_step=WINDOW_STEP, tmm_norm=TMM_NORM, n_norm=N_NORM, skip_header=SKIP_HEADER, total_reads_a=TOTAL_READS_A, 
                 total_reads_b=TOTAL_READS_B, total_reads_replica=TOTAL_READS_REPLICA, a_trim=A_TRIM, m_trim=M_TRIM, use_replica_flag=USE_REPLICA, tempdir=TEMPDIR,
                 use_samtools=USESAMTOOLS, access_sequential = SEQUENTIAL, experiment_label = EXPERIMENT_LABEL, replica_label = REPLICA_LABEL, title_label = TITLE_LABEL, 
                 count_filter = COUNT_FILTER, force_sort=FORCE_SORT, push_distance=PUSH_DIST, quant_norm=QUANT_NORM, parser_name="pyicoteo",
                 region_magic=REGION_MAGIC, gff_file=GFF_FILE, interesting_regions=INTERESTING_REGIONS, disable_significant_color=DISABLE_SIGNIFICANT,
                 f_custom_in=F_CUSTOM, custom_in_sep=CUSTOM_SEP, f_custom_out=F_CUSTOM, custom_out_sep=CUSTOM_SEP, 
                 galaxy_workarounds=GALAXY_WORKAROUNDS, html_output=HTML_OUTPUT, overlap=OVERLAP):
        self.__dict__.update(locals())
        if type(self.tempdir) is not list: #
            self.tempdir = [self.tempdir] 

        self.normalize_factor = 1
        self.logger = utils.get_logger("%s.log"%parser_name, verbose, debug)

        if self.debug:
            self.logger.info("ARGV: %s", ' '.join(sys.argv))

        self.is_sorted = False
        self.temp_experiment = False # Indicates if temp files were created for the experiment
        self.temp_control = False # Indicates if temporary files were created for the control
        self.temp_replica = False
        self.operations = []
        self.previous_chr = None
        self.open_region = False
        self.safe_reader = utils.SafeReader(self.logger)        
        if not self.discarded_names:
            self.discarded_names = []
     
        #Reusable cluster objects 
        try:
            self.cluster = Cluster(read=self.experiment_format, write=self.output_format, rounding=self.rounding, read_half_open = self.open_experiment, write_half_open = self.open_output, tag_length=self.tag_length, span = self.span, logger=self.logger, cached=cached)
            self.cluster_aux = Cluster(read=self.experiment_format, write=self.output_format, rounding=self.rounding, read_half_open = self.open_experiment, write_half_open = self.open_output, tag_length=self.tag_length, span = self.span, logger=self.logger, cached=cached)
            self.cluster_aux2 = Cluster(read=self.experiment_format, write=self.output_format, rounding=self.rounding, read_half_open = self.open_experiment, write_half_open = self.open_output, tag_length=self.tag_length, span = self.span, logger=self.logger, cached=cached)
            self.control_cluster = Cluster(read=experiment_format, read_half_open = open_experiment, logger=self.logger, cached=cached)
        except ConversionNotSupported:
            self.logger.error('Reading "%s" and writing "%s" is not supported.\n'%(self.experiment_format, self.output_format))
            utils.list_available_formats()

        #duplicates flags
        self.previous_start = 0
        self.previous_end = 0
        self.previous_chr = ''
        self.duplicates_found = 0
        #poisson stuff
        self.first_chr = True
        self._init_poisson()
        self.poisson_results = {}
        for test in POISSON_OPTIONS:
            self.poisson_results[test] = defaultdict()
        
        #self.poisson_results = {'length': defaultdict(), 'height': defaultdict(), 'numtags': defaultdict()}
        self.maxheight_to_pvalue = {}
        self.numtags_to_pvalue = {}
        #Operation flags
        self.do_poisson = False
        self.do_subtract = False
        self.do_heuremove = False
        self.do_split = False
        self.do_trim = False
        self.do_cut = False
        self.do_dupremove = False
        self.sorted_by_pyicos = False
        self.sorted_region_path = ''

    def __icant(self, message, print_formats=False):
        self.logger.error(message)
        if print_formats: print CLUSTER_FORMATS
        sys.exit(0)

    def i_cant_do(self):
        """Quits and exists if exits if the combination of non possible operations"""
        if FILTER in self.operations and POISSON not in self.operations and not self.threshold:
            self.__icant("Can't do Filter without Poisson or a fixed threshold\n")
        elif (SUBTRACT or SPLIT or POISSON) in self.operations and (self.output_format not in CLUSTER_FORMATS):
            self.__icant("Can't get the output as tag format (eland, bed) for Subtract, Split, Poisson filtering please use a clustered format", True)
        elif (EXTEND or PUSH) in self.operations and self.experiment_format in CLUSTER_FORMATS:
            self.__icant("Can't extend or push if the experiment is a clustered format ", True)
        elif STRAND_CORRELATION in self.operations and self.experiment_format in CLUSTER_FORMATS:
            self.__icant("Can't perform strand correlation operation if the experiment is a clustered format ", True)
        elif ENRICHMENT in self.operations and MODFDR in self.operations:
            self.__icant("\nEnrichment and ModFDR operations are not compatible")
        elif ENRICHMENT in self.operations and POISSON in self.operations:
            self.__icant("\nEnrichment and Poisson operations are not compatible")           
    
    def _add_slash_to_path(self, path):
        return utils.add_slash_to_path(path)

    def success_message(self, output_path):
        self.logger.info('Operations complete.')
        self.logger.info('')
        self.logger.info('Summary of operations:')
        self.logger.info('----------------------')
        if self.experiment_format != self.output_format and not NOWRITE in self.operations:
            self.logger.info('Convert from %s to %s.'%(self.experiment_format, self.output_format))

        if STRAND_CORRELATION in self.operations:
            self.logger.info('Strand correlation')

        if EXTEND in self.operations:
            self.logger.info('Extend to %s'%self.frag_size)

        if PUSH in self.operations:
            self.logger.info('Push %s'%self.push_distance)

        if self.do_subtract:
            self.logger.info('Subtract')

        if self.discarded_names:
            self.logger.info('Discard tags: %s'%self.discarded_names)

        if self.do_heuremove:
            self.logger.info('Heuristic Remove from %s'%self.region_path)

        if self.do_split:
            self.logger.info('Split')

        if DISCARD_ARTIFACTS in self.operations:
            self.logger.info('Discard Artifacts')

        if self.do_poisson:
            self.logger.info('Poisson')

        if self.do_cut:
            self.logger.info('Filter')

        if self.do_dupremove:
            self.logger.info('Removed %s duplicates, allowing up to %s'%(self.duplicates_found, self.duplicates))

        if not NOWRITE in self.operations:
            self.logger.info('Output at: %s'%(output_path))

    def start_operation_message(self):
        if self.experiment_format != self.output_format and not NOWRITE in self.operations:
            self.logger.info('Converting file %s from %s to %s...'%(self.current_experiment_path, self.experiment_format, self.output_format))
        else:
            self.logger.info('Reading file %s as a %s file...'%(self.current_experiment_path, self.experiment_format))

        if self.current_control_path:
            self.logger.info('Control file:%s'%self.current_control_path)
            if self.do_normalize:
                self.logger.info('The file %s will be normalized to match %s'%(self.current_experiment_path, self.current_control_path))

            if self.do_subtract:
                self.logger.info('The file %s will be subtracted from %s'%(self.current_control_path, self.current_experiment_path))

    def get_normalize_factor(self, experiment, control):
        ret = self.numcells(experiment, self.experiment_format)/self.numcells(control, self.control_format)
        self.logger.info('Normalization factor: %s\n'%(ret))

        return ret
    
    def numcells(self, file_path, file_format):
        """Returns the total number of cells in a file"""
        num = 0.
        cluster = Cluster(read=file_format, logger=self.logger)
        for line in utils.open_file(file_path, file_format, logger=self.logger):
            self.safe_read_line(cluster, line)
            for level in cluster: 
                num += level[0]*level[1]

            cluster.clear()

        return num  
    
    def safe_read_line(self, cluster, line):
        self.safe_reader.safe_read_line(cluster, line)

    def run(self):
        self.do_subtract = (SUBTRACT in self.operations and self.control_path)
        self.do_normalize = (NORMALIZE in self.operations and self.control_path)
        self.do_heuremove = (REMOVE_REGION in self.operations and self.blacklist)
        self.do_poisson = POISSON in self.operations
        self.do_split = SPLIT in self.operations
        self.do_trim = TRIM in self.operations
        self.do_cut = FILTER in self.operations
        self.do_discard = DISCARD_ARTIFACTS in self.operations
        self.do_dupremove = REMOVE_DUPLICATES in self.operations
        self.tag_to_cluster = (not self.experiment_format in CLUSTER_FORMATS and self.output_format in CLUSTER_FORMATS)
        self.use_MA = USE_MA in self.operations
        self.logger.info("")
        self.logger.info('***********************************************')
        self.logger.info('Pyicoteo running... (PID: %s)'%(os.getpid()))
        self.logger.info('This log will also be saved at: %s'%(os.path.abspath(self.logger.name)))

        if REGIONS in self.operations and len(self.operations) == 1: # FIXME: bad workaround?
            outfile = open(self.output_path, 'w')
            regwriter = RegionWriter(self.gff_file, outfile, self.region_magic, no_sort=self.no_sort, logger=self.logger, write_as=self.output_format, galaxy_workarounds=self.galaxy_workarounds)
            regwriter.write_regions()
            return

        if self.control_path:
            self.process_all_files_paired(self.experiment_path, self.control_path)
        elif self.experiment_b_path:
            self.process_all_files_paired(self.experiment_path, self.experiment_b_path)
        elif self.plot_path:
            self.process_all_files_recursive(self.plot_path, self.output_path)
        elif self.counts_file:
            self.process_all_files_recursive(self.counts_file, self.output_path)
        else:
            self.process_all_files_recursive(self.experiment_path, self.output_path)

    def process_all_files_paired(self, path_a, path_b):
        if os.path.isdir(path_a):
            self.logger.warning('Operating with directories will only give appropriate results if the files and the control are paired in alphabetical order.')
            controls = os.listdir(path_b)
            controls.sort()
            experiments = os.listdir(path_a)
            experiments.sort()
            for i in xrange(0, len(experiments)):
                try:
                    operate(experiments[i], control[i], '%s%s'%(self.output_path, experiments[i]))
                except IndexError:
                    pass
        else:
            output = self.output_path
            if os.path.isdir(self.output_path):
                output = self._add_slash_to_path(self.output_path)
                output = '%s%s_minus_%s'%(self.output_path, os.path.basename(path_a), os.path.basename(path_b))
            
            self.operate(path_a, path_b, output)
    
    def process_all_files_recursive(self, dirorfile, output, firstIteration=True):
        paths = []
        """Goes through all the directories and creates files recursively. Returns the paths of the resulting files"""
        if os.path.isdir(dirorfile):
            dirorfile = self._add_slash_to_path(dirorfile)
            if not firstIteration:
                output = '%s/%s/'%(os.path.abspath(output), dirorfile.split('/')[-2])
                if not os.path.exists(output):
                    os.makedirs(output)  
            
            for filename in os.listdir(dirorfile):
                self.process_all_files_recursive('%s%s'%(dirorfile,filename), output)
                
        elif os.path.isfile(os.path.abspath(dirorfile)):
            if os.path.isdir(output):
                output = '%s%s.%s'%(self._add_slash_to_path(output), (os.path.basename(dirorfile)).split('.')[0], self.output_format)
            try:
                self.operate(experiment_path=os.path.abspath(dirorfile), output_path=os.path.abspath(output))
                paths.append(output)
            except OperationFailed:
                self.logger.warning('%s couldn\'t be read. Skipping to next file.'%os.path.abspath(dirorfile))
                os.remove(os.path.abspath(output))
            except StopIteration:
                self.logger.warning('%s End of file reached.'%os.path.abspath(dirorfile))
        else:
            self.logger.error('Input "%s" doesn\'t exist?'%os.path.abspath(dirorfile))

        return paths

    def normalize(self):
        if self.control_path:
            self.logger.info('Calculating normalization factor...')
            self.normalize_factor = self.get_normalize_factor(self.current_experiment_path, self.current_control_path)

    def _manage_temp_file(self, path):
        """A temporary file that is no longer needed is given, and depending on the value of self.keep_temp its removed or kept"""
        if self.keep_temp:
            self.logger.info("Temp file kept at: %s (remember cleaning them!)"%path)
        else:
            os.remove(path)
            self.logger.info('Temporary file %s removed'%path)

    def _filter_file(self, file_path, temp_name, remove_temp, file_format, file_open):
        """Assumes sorted file. Extend, pushing and removal of duplicates go here"""
        if self.logger: self.logger.debug("ENTER filter_file")
        if self.tempdir:
            td = self.tempdir[0]
        else:
            td = gettempdir()

        new_file_path = "%s/tempfilter%s_%s"%(td, os.getpid(), temp_name)
        new_file = open(new_file_path, 'w')
        self.logger.info("Filtering %s file..."%temp_name)
        previous_line = ''
        equal_lines = 0
        self.cluster.clear()
        cluster_same = Cluster(read=file_format, write=file_format, rounding=self.rounding, read_half_open = file_open, write_half_open = file_open, tag_length=self.tag_length, span = self.span, logger=self.logger, cached=self.cached)
        for line in utils.open_file(file_path, file_format, logger=self.logger):
            self.cluster.clear()
            self.safe_read_line(self.cluster, line)
            if self.cluster.start == cluster_same.start and self.cluster.end == cluster_same.end and self.cluster.name == cluster_same.name and self.cluster.strand == cluster_same.strand:
                equal_lines+=1
            else:
                equal_lines=0

            cluster_same.clear()
            self.safe_read_line(cluster_same, line)
     
            if self.do_heuremove:
                cluster_same = self.remove_regions(cluster_same)

            if self.duplicates >= equal_lines:
                if EXTEND in self.operations:
                    cluster_same.extend(self.frag_size)

                if PUSH in self.operations:
                    cluster_same.push(self.push_distance)

                new_file.write(cluster_same.write_line())
            else:
                self.duplicates_found += 1

            cluster_same.clear() #Need to re-read to allow combination of extend and remove duplicates
            self.safe_read_line(cluster_same, line)

        new_file.flush()
        new_file.close()
        if (EXTEND or PUSH) in self.operations:
            self.logger.info("Resorting %s after extension/push..."%temp_name)
            sorter = utils.BigSort(file_format, file_open, 0, 'fisort%s'%temp_name, logger=self.logger)
            old_path = new_file_path
            sorted_new = sorter.sort(old_path, None, utils.sorting_lambda(file_format), tempdirs=self.tempdir)
            new_file_path = sorted_new.name
            self._manage_temp_file(old_path)

        if remove_temp:
            self._manage_temp_file(file_path) 

        self.cluster.clear()
        self.cluster_aux.clear()   
        return new_file_path
    
    def filter_files(self):
        """Filter the files removing the duplicates."""
        self.current_experiment_path = self._filter_file(self.current_experiment_path, "experiment", self.temp_experiment, self.experiment_format, self.open_experiment)
        if self.experiment_format == BAM: 
            self.experiment_format = SAM

        self.temp_experiment = True
        if self.current_control_path:
            self.current_control_path = self._filter_file(self.current_control_path, "control", self.temp_control, self.control_format, self.open_control)
            self.temp_control = True
            if self.control_format == BAM: 
                self.control_format = SAM

    def decide_sort(self, experiment_path, control_path=None):
        """Decide if the files need to be sorted or not."""
        #FIXME refractor this, copy pasted code (warning, its not as easy as it seems)
        if self.force_sort or self.tag_to_cluster or self.do_subtract or self.do_heuremove or self.do_dupremove or MODFDR in self.operations or ENRICHMENT in self.operations or REMOVE_REGION in self.operations or STRAND_CORRELATION in self.operations or self.frag_size or PUSH in self.operations:
            self.experiment_preprocessor = utils.BigSort(self.experiment_format, self.open_experiment, self.frag_size, 'experiment', logger=self.logger, push_distance=self.push_distance)
            self.experiment_b_preprocessor = utils.BigSort(self.experiment_format, self.open_experiment, self.frag_size, 'experiment_b', logger=self.logger, push_distance=self.push_distance)
            self.replica_preprocessor = utils.BigSort(self.experiment_format, self.open_experiment, self.frag_size, 'replica', logger=self.logger, push_distance=self.push_distance)
            self.control_preprocessor = utils.BigSort(self.control_format, self.open_control, self.frag_size, 'control', logger=self.logger, push_distance=self.push_distance)        
            if self.no_sort or self.experiment_format == BAM:
                if self.no_sort: self.logger.warning('Input sort skipped. Results might be wrong.')
                self.current_experiment_path = experiment_path
            elif not self.counts_file:
                self.logger.info('Sorting experiment file...')
                self.is_sorted = True
                sorted_experiment_file = self.experiment_preprocessor.sort(experiment_path, None, utils.sorting_lambda(self.experiment_format), self.tempdir)
                self.current_experiment_path = sorted_experiment_file.name
                self.temp_experiment = True

            if self.do_subtract:
                if self.no_sort or self.experiment_format == BAM:
                    if self.no_sort: self.logger.warning('Control sort skipped. Results might be wrong.')
                    self.current_control_path = control_path
                else:
                    self.logger.info('Sorting control file...')
                    sorted_control_file = self.control_preprocessor.sort(control_path, None, utils.sorting_lambda(self.control_format), self.tempdir)
                    self.current_control_path = sorted_control_file.name
                    self.temp_control = True
            
            if ENRICHMENT in self.operations:  
                if self.no_sort or self.experiment_format == BAM:
                    if self.no_sort:  self.logger.warning('Experiment_b sort skipped. Results might be wrong.')
                    self.current_control_path = control_path
                elif not self.counts_file:
                    self.logger.info('Sorting experiment_b file...')
                    sorted_control_file = self.experiment_b_preprocessor.sort(control_path, None, utils.sorting_lambda(self.experiment_format), self.tempdir)
                    self.current_control_path = sorted_control_file.name
                    self.temp_control = True
            
            if self.replica_path:  
                if self.no_sort or self.experiment_format == BAM:
                    if self.no_sort: self.logger.warning('replica sort skipped')
                    self.current_replica_path = self.replica_path
                else:
                    self.logger.info('Sorting replica file...')
                    sorted_replica_file = self.replica_preprocessor.sort(self.replica_path, None, utils.sorting_lambda(self.experiment_format), self.tempdir)
                    self.current_replica_path = sorted_replica_file.name
                    self.temp_replica = True

        if self.region_path:
            if self.no_sort or self.experiment_format == BAM:
                if self.no_sort:
                    self.logger.warning('region sort skipped')
                self.sorted_region_path = self.region_path
            else:
                self.logger.info("Sorting region file...")
                self.region_preprocessor = utils.BigSort(self.region_format, self.open_region, None, 'region', logger=self.logger)
                self.sorted_region_file = self.region_preprocessor.sort(self.region_path, None, utils.sorting_lambda(BED), self.tempdir)
                self.sorted_region_path = self.sorted_region_file.name

    def operate(self, experiment_path, control_path=None, output_path=None):
        """Operate expects single paths, not directories. It's called from run() several times if the experiment for pyicoteo is a directory"""
        try:
            self.i_cant_do()
            #per operation variables
            self.previous_chr = None
            self.real_experiment_path = experiment_path #for refering to the files after all the intermediate files
            self.real_control_path = control_path #for refering to the files after all the intermediate files
            self.current_experiment_path = experiment_path
            self.current_control_path = control_path
            self.current_output_path = output_path
            self.cluster.clear()
            self.cluster_aux.clear()
            self.start_operation_message()
            self.decide_sort(experiment_path, control_path)
            self.estimate_frag_size = self.do_poisson and not self.frag_size
            if self.control_path:
                self.control_reader_simple = utils.SortedFileReader(self.current_control_path, self.control_format, rounding=self.rounding, logger=self.logger)
                self.control_reader = utils.read_fetcher(self.current_control_path, self.control_format, rounding=self.rounding, cached=self.cached, logger=self.logger)

            if self.blacklist:
                self.blacklist_reader = utils.read_fetcher(self.blacklist, BED, rounding=self.rounding, cached=self.cached, logger=self.logger)

            if STRAND_CORRELATION in self.operations:
                self.strand_correlation()          

            if self.do_dupremove or (EXTEND in self.operations and (STRAND_CORRELATION in self.operations or self.experiment_format == BAM or self.control_format == BAM)) or self.do_heuremove:
                self.filter_files()

            if NORMALIZE in self.operations:
                self.normalize()

            if self.do_cut: #if we cut, we will round later
                self.cluster.rounding = False
                self.cluster_aux.rounding = False

            if ((not NOWRITE in self.operations) or (NOWRITE in self.operations and POISSON in self.operations)) and not ENRICHMENT in self.operations: 
                self.process_file()
            
            if self.do_poisson: #extract info from the last name and print the thresholds
                self.poisson_analysis(self.previous_chr)
                self.logger.info('Cluster thresholds for p-value %s:'%self.p_value)
                for name, k in self.poisson_results[self.poisson_test].items():
                    self.logger.info('%s: %s'%(name, k))

            if CHECK_REPLICAS in self.operations: 
                self.experiment_values = []
                self.replica_values = []
                #enrichment.check_replica(self)

            #Mutually exclusive final operations
            if ENRICHMENT in self.operations:
                self.plot_path = enrichment.enrichment(self)
                if CALCZSCORE in self.operations:
                    self.plot_path = enrichment.calculate_zscore(self, self.plot_path)
            elif self.do_cut: 
                self.cut()
            elif MODFDR in self.operations:
                self.modfdr()

            #Plot and summary operations
            if PLOT in self.operations:
                  self.logger.info("Plotting...")
                  enrichment.plot_enrichment(self, self.plot_path)            

            self.success_message(output_path)

        finally: #Finally try deleting all temporary files, quit silently if they dont exist
            try:
                if self.temp_experiment:
                    self._manage_temp_file(self.current_experiment_path)
            except AttributeError, OSError:
                pass

            try:
                if self.temp_control:
                    self._manage_temp_file(self.current_control_path)
            except AttributeError, OSError:
                pass
 
            try:
                if self.sorted_region_path:
                    self._manage_temp_file(self.sorted_region_file.name)
            except AttributeError, OSError:
                pass

            try:
                if self.temp_replica:
                    self._manage_temp_file(self.current_replica_path)
            except AttributeError, OSError:
                pass

    def _to_read_conversion(self, experiment, output):
        for line in experiment:
            try:
                self.safe_read_line(self.cluster, line)
                if not self.cluster.is_empty():
                    self.process_cluster(self.cluster, output)
                self.cluster.clear()
            except InsufficientData:
                self.logger.warning('For this type of conversion (%s to %s) you need to specify the tag length with the --tag-length flag'%(self.experiment_format, self.output_format))
                sys.exit(0)

    def _to_cluster_conversion(self, experiment, output):
        #load the first read
        self.logger.debug("_to_cluster_conversion: running clustering...")
        while self.cluster.is_empty():
            self.safe_read_line(self.cluster, experiment.next())

        for line in experiment:
            self.cluster_aux.clear()
            self.safe_read_line(self.cluster_aux, line)
            if not self.cluster_aux.is_empty():
                if self.cluster_aux.touches(self.cluster):
                    self.cluster += self.cluster_aux
                else:
                    if not self.cluster.is_empty():
                        self.process_cluster(self.cluster, output)
                        self.cluster.clear()
                        
                    self.safe_read_line(self.cluster, line)

        if not self.cluster.is_empty(): #Process the last cluster
            self.process_cluster(self.cluster, output)
            self.cluster.clear()

        self.logger.debug("_to_cluster_conversion: Done clustering.")

    def process_file(self):
        self.cluster.clear()
        self.cluster_aux.clear()
        if NOWRITE in self.operations:
            output = None
        else:
            output = open(self.current_output_path, 'wb')

        experiment = utils.open_file(self.current_experiment_path, self.experiment_format, logger=self.logger)
        if (self.output_format == WIG or self.output_format == VARIABLE_WIG) and not self.skip_header:
            output.write('track type=wiggle_0\tname="%s"\tvisibility=full\n'%self.label)

        if self.output_format in CLUSTER_FORMATS and not MODFDR in self.operations and not ENRICHMENT in self.operations:
            if not self.experiment_format in CLUSTER_FORMATS:
                self.logger.info('Clustering reads...')
            self._to_cluster_conversion(experiment, output)
        else:
            self._to_read_conversion(experiment, output)

        if not NOWRITE in self.operations:
            output.flush()
            output.close()

    def subtract(self, cluster):
        region = Region(cluster.name, cluster.start, cluster.end, logger=self.logger)
        self.logger.debug("Getting overlapping clusters...")
        self.cluster_aux.clear()
        for c in self.control_reader_simple.overlapping_clusters(region, overlap=EPSILON):
            self.cluster_aux.normalize_factor = self.normalize_factor #add the normalization factor
            c.normalize_factor = self.normalize_factor #add the normalization factor
            if self.cluster_aux.is_empty(): #first one
               c.copy_cluster_data(self.cluster_aux)           
            elif self.cluster_aux.touches(c):
                self.cluster_aux += c
            else:
                region.clusters.append(self.cluster_aux.copy_cluster())
                self.logger.debug("Region clusters length: %s "%len(region.clusters))
                c.copy_cluster_data(self.cluster_aux)

        if self.cluster_aux: #last one
            region.clusters.append(self.cluster_aux.copy_cluster())
            self.logger.debug("Region clusters length: %s Last cluster cache: %s"%(len(region.clusters), region.clusters[-1]._tag_cache))
            self.cluster_aux.clear()

        self.logger.debug("Done adding tags!")
        self.logger.debug("Clustering control (metacluster)...")
        meta = region.get_metacluster()
        del region
        cluster -= meta
        self.logger.debug("OPERATION: Done subtracting (for real)")       
        return cluster

    def remove_regions(self, cluster):
        region = Region(cluster.name, cluster.start, cluster.end)
        overlapping_blacklist = list(self.blacklist_reader.overlapping_clusters(region, overlap=EPSILON))
        if overlapping_blacklist: 
            cluster.clear() 

        return cluster

    def _correct_bias(self, p_value):
        if p_value < 0:
            return 0
        else:
            return p_value

    def _init_poisson(self):
        self.total_bp_with_reads = 0.
        self.total_clusters = 0.
        self.total_reads = 0.
        self.acum_height = 0.
        self.absolute_max_height = 0
        self.absolute_max_numtags = 0
        self.chr_length = 0

    def poisson_analysis(self, name=''):
        """
        We do 3 different poisson statistical tests per name for each experiment file:
 
        Cluster analysis:
        This analysis takes as a basic unit the "cluster" profile and performs a poisson taking into account the height
        of the profile. This will help us to better know which clusters are statistically significant and which are product of chromatin noise

        We do this by calculating the average height of clusters in a given chromosome. Given this mean, we calculate the p-value for each height k the poisson
        function gives us the probability p of one cluster having a height k by chance. 
        With this if we want, for example, to know what is the probability of getting a cluster higher than k = 7, we accumulate the p-values 
        for poisson(k"0..6", mean), and calculate 1 - sum(poisson(k, mean))
        
        Nucleotide analysis:
        This analysis takes the nucleotide as the unit to analize. We give a p-value for each "height"
        of read per nucleotides using an accumulated poisson. With this test we can infer more accurately 
        what nucleotides in the cluster are part of the DNA binding site.

        Number of reads analysis:
        We analize the number of reads of the cluster. Number of reads = sum(xi * yi) / read_length

        """
        if os.path.isdir(self.output_path):
           out = self.output_path
        else:
           out = os.path.dirname(os.path.abspath(self.output_path))

        #search for the name length in the chr len files
        found = False
        try:
            chrlenpath = "%s/chromlen/"%os.path.dirname(os.path.abspath(__file__))
            self.logger.debug("Chromosome length path: %s"%chrlenpath)
            for line in open('%s%s'%(chrlenpath, self.species)):
                chrom, length = line.split()
                if chrom == name:
                    found = True
                    self.chr_length = int(length)
        except IOError:
            pass #file not found, the warning will be printed
        
        if not found: 
            try:
                from chromlen import getChromosomeDescription
                self.logger.info("Couldn't find %s length file, trying to download from UCSC..."%self.species)
                getChromosomeDescription.retrieve_chrinfo(self.species, chrlenpath)
            except IOError:
                self.logger.warning("Couldn't download lengths from UCSC for assembly %s. An approximation will be used. To download this file, run pyicoteo with admin privileges"%(self.species))
        
        self.logger.info('Correction factor: %s'%(self.correction_factor))
        self.reads_per_bp =  self.total_bp_with_reads / self.chr_length*self.correction_factor
        p_nucleotide = 1.
        p_cluster = 1.
        p_numtags = 1.
        k = 0
        while ((self.absolute_max_numtags > k) or (self.absolute_max_height > k)) and k < self.height_limit:
            p_nucleotide -= utils.poisson(k, self.reads_per_bp) #analisis nucleotide
            p_cluster -= utils.poisson(k, self.acum_height/self.total_clusters) #analysis cluster
            p_numtags -= utils.poisson(k, self.total_reads/self.total_clusters) #analysis numtags
            p_nucleotide = self._correct_bias(p_nucleotide)
            p_cluster = self._correct_bias(p_cluster)
            p_numtags = self._correct_bias(p_numtags)
            if name not in self.poisson_results['length'].keys() and p_nucleotide < self.p_value: #if we don't have a height k that is over the p_value yet, write it.
                self.poisson_results["length"][name] = k

            if name not in self.poisson_results['height'].keys() and p_cluster < self.p_value:
                self.poisson_results["height"][name] = k
            
            if name not in self.poisson_results['numtags'].keys() and p_numtags < self.p_value:
                self.poisson_results["numtags"][name] = k

            if k not in self.maxheight_to_pvalue:
                self.maxheight_to_pvalue[k] = {}
            self.maxheight_to_pvalue[k][name] = p_cluster
            if k not in self.numtags_to_pvalue:
                self.numtags_to_pvalue[k] = {}
            self.numtags_to_pvalue[k][name] = p_numtags
            k+=1

    def poisson_retrieve_data(self, cluster):
        if self.estimate_frag_size: 
            if cluster.tag_length:
                self.frag_size = cluster.tag_length
            else:
                raise NeedFragmentSize
                
        acum_numtags = 0.
        self.total_clusters+=1
        for length, height in cluster:
            self.total_bp_with_reads+=length
            acum_numtags += length*height

        self.chr_length = max(self.chr_length, cluster.end)
        max_height = cluster.max_height()
        #numtags per cluster
        numtags_in_cluster = acum_numtags/self.frag_size
        self.total_reads += numtags_in_cluster
        self.absolute_max_numtags = max(numtags_in_cluster, self.absolute_max_numtags)
        #maxheight per cluster
        self.acum_height += max_height
        self.absolute_max_height = max(max_height, self.absolute_max_height)

    def process_cluster(self, cluster, output):
        if self.cluster._tag_cache:
            self.cluster._flush_tag_cache()

        if cluster.name not in self.discarded_names and not cluster.is_empty():
            if self.previous_chr != cluster.name: #A new name has been detected
                if self.is_sorted:
                    self.logger.info('Processing %s...'%cluster.name)
                    sys.stdout.flush()
                if self.do_poisson and not self.first_chr:
                    self.poisson_analysis(self.previous_chr)
                    self._init_poisson()
                self.previous_chr = cluster.name
                if self.output_format == VARIABLE_WIG:
                    output.write('variableStep\tchrom=%s\tspan=%s\n'%(self.previous_chr, self.span))
                self.first_chr = False

            if self.do_subtract:
                cluster = self.subtract(cluster)
                self.logger.debug("Absolute split and post subtract...")
                for cluster in cluster.absolute_split(threshold=0):
                    self._post_subtract_process_cluster(cluster, output)
            else:
                self._post_subtract_process_cluster(cluster, output)

            #self.logger.debug("Finished process_cluster")

    def _post_subtract_process_cluster(self, cluster, output):
        if self.do_poisson:
            self.poisson_retrieve_data(cluster)

        if not (cluster.is_artifact() and DISCARD_ARTIFACTS in self.operations) and not NOWRITE in self.operations:
            if self.do_trim:
                cluster.trim(self.trim_percentage, self.trim_absolute)

            if self.do_split:
                for subcluster in cluster.split(self.split_proportion, self.split_absolute): 
                    self.extract_and_write(subcluster, output)
            else:
                self.extract_and_write(cluster, output)

    def extract_and_write(self, cluster, output):
        """The line will be written to the file if the last conditions are met"""
        if not cluster.is_empty() and cluster.start > -1:
            output.write(cluster.write_line())

    def _current_directory(self):
        return os.path.abspath(os.path.dirname(self.current_output_path))

    def cut(self):
        """Discards the clusters that don't go past the threshold calculated by the poisson analysis"""
        current_directory = self._current_directory()
        old_output = '%s/before_cut_%s'%(current_directory, os.path.basename(self.current_output_path))
        move(os.path.abspath(self.current_output_path), old_output)
        filtered_output = open(self.current_output_path, 'w+')
        if self.poisson_test == 'height':
            msg = "Filtering using cluster height..."
        else:
            msg = "number of reads per cluster..."

        self.logger.info(msg)
        unfiltered_output = open('%s/unfiltered_%s'%(current_directory, os.path.basename(self.current_output_path)), 'w+')
        if self.output_format == WIG or self.output_format == VARIABLE_WIG:
            wig_header = 'track type=wiggle_0\tname="%s"\tvisibility=full\n'%self.label
            filtered_output.write(wig_header)
            unfiltered_output.write(wig_header)
   
        cut_cluster = Cluster(read=self.output_format, write=self.output_format, rounding=self.rounding, read_half_open = self.open_output, write_half_open = self.open_output, tag_length=self.tag_length, span = self.span, logger=self.logger)
        self.logger.info('Writing filtered and unfiltered file...')
        for line in open(old_output):
            cut_cluster.clear()
            self.safe_read_line(cut_cluster, line)
            try:
                if self.poisson_test == 'height':
                    cut_cluster.p_value = self.maxheight_to_pvalue[int(round(cut_cluster.max_height()))][cut_cluster.name]  
                else:
                    cut_cluster.p_value = self.numtags_to_pvalue[int(round(cut_cluster.area()/self.frag_size))][cut_cluster.name]      
            except KeyError:
                cut_cluster.p_value = 0 #If the cluster is not in the dictionary, it means its too big, so the p_value will be 0

            try:
                if self.threshold:
                    thres = self.threshold
                else:
                    thres = self.poisson_results[self.poisson_test][cut_cluster.name]
                    
                if cut_cluster.is_significant(thres, self.poisson_test, self.frag_size):
                    filtered_output.write(cut_cluster.write_line())                    
            except KeyError:
                pass

            if not cut_cluster.is_empty():
                unfiltered_output.write(cut_cluster.write_line()) 

        self._manage_temp_file(old_output)

    def _output_dir(self):
        """Returns the output directory"""
        path = os.path.dirname(os.path.realpath(self.current_output_path))
        if not os.path.exists(path):
            os.makedirs(path)
        return path

    def _region_from_sline(self, sline):
        if sline:
            try:
                strand = None
                exome_size = 0
                if self.stranded_analysis:
                    strand = sline[5]
                elif self.region_format == BED12:
                    for size in sline[10].split(","):
                        if size: #to discard last empty value when there is an extra comma (1,2,3, instead of 1,2,3)
                            exome_size += int(size)
                
                return Region(sline[0], int(sline[1]), int(sline[2]), name2=sline[3], strand=strand, exome_size=exome_size)
            except ValueError:
                self.logger.debug("Discarding _region_from_sline %s"%'\t'.join(sline))

    def _save_figure(self, figure_name, width = None, height= None):
        #FIXME move to utils.py, maybe a separate plotting module
        if self.postscript:
            exten = 'ps'
        else:
            exten = 'png'
        from matplotlib.pyplot import savefig, clf, show
        if self.showplots:
            show()

        else:
            figure_path = '%s/%s_%s.%s'%(self._output_dir(), figure_name, os.path.basename(self.current_output_path), exten) 
            import warnings 
            with warnings.catch_warnings(): #To make the RuntimeWarning go away
                warnings.simplefilter("ignore")

            if width and height:
                 savefig(figure_path, bbox_inches="tight", width=width, height=height,  pad_inches=0.7)
            else:
                savefig(figure_path, scatterpoints = 1)

            self.logger.info("%s figure saved to %s"%(figure_name, figure_path))
        clf()
        
    def modfdr(self):
        if self.logger: self.logger.info("Running modfdr filter with %s p-value threshold and %s repeats..."%(self.p_value, self.repeats))
        old_output = '%s/before_modfdr_%s'%(self._current_directory(), os.path.basename(self.current_output_path))
        move(os.path.abspath(self.current_output_path), old_output)
        cluster_reader = utils.read_fetcher(old_output, self.output_format, cached=self.cached, logger=self.logger)
        filtered_output = open(self.current_output_path, 'w+')
        unfiltered_output = open('%s/unfiltered_%s'%(self._current_directory(), os.path.basename(self.current_output_path)), 'w+')

        if self.gff_file and self.region_magic:
            calc_region_file = open('%s/regionclip_%s'%(self._current_directory(), os.path.basename(self.current_output_path)), 'w+')
            regwriter = RegionWriter(self.gff_file, calc_region_file, self.region_magic, no_sort=self.no_sort, logger=self.logger, write_as=BED, galaxy_workarounds=self.galaxy_workarounds)
            regwriter.write_regions()
            self.sorted_region_path = calc_region_file.name
            calc_region_file.flush()
            calc_region_file.close()

        for region_line in open(self.sorted_region_path):
            region = self._region_from_sline(region_line.split())
            region.logger = self.logger
            region.add_tags(list(cluster_reader.overlapping_clusters(region, overlap=EPSILON)), True)
            classified_clusters = region.get_FDR_clusters(self.repeats)
            for cluster in classified_clusters:
                unfiltered_output.write(cluster.write_line())
                if cluster.p_value < self.p_value:
                    filtered_output.write(cluster.write_line())

        self._manage_temp_file(old_output)

    def strand_correlation(self):
        self.logger.info("Strand correlation analysis...")
        self.delta_results = dict()
        self.best_delta = -1
        positive_cluster = Cluster(cached=True)
        negative_cluster = Cluster(cached=True)
        positive_cluster_cache = [] # we are trying to hold to the previous cluster
        self.analyzed_pairs = 0.
        acum_length = 0.
        num_analyzed = 0
        for line in utils.open_file(self.current_experiment_path, self.experiment_format, logger=self.logger):
            line_read = Cluster(read=self.experiment_format)
            self.safe_read_line(line_read, line)
            if line_read.strand == PLUS_STRAND:
                if  positive_cluster.intersects(line_read):
                     positive_cluster += line_read
                     acum_length += len(line_read)
                     num_analyzed += 1
                elif positive_cluster.is_empty() or positive_cluster.is_artifact():
                    positive_cluster = line_read.copy_cluster()
                elif not positive_cluster_cache:
                    positive_cluster_cache.append(line_read.copy_cluster())
                elif line_read.intersects(positive_cluster_cache[0]):
                    positive_cluster_cache[0] += line_read
                else:
                    positive_cluster_cache.append(line_read.copy_cluster())
            else:
                if negative_cluster.is_empty() or not negative_cluster.intersects(line_read):
                    if positive_cluster.max_height() > self.correlation_height and negative_cluster.max_height() > self.correlation_height: # if we have big clusters, correlate them
                        self._correlate_clusters(positive_cluster, negative_cluster)
                        if self.max_correlations < self.analyzed_pairs: # if we analyzed enough clusters, stop
                            break                     
                    negative_cluster = line_read.copy_cluster() # after correlating, select the next cluster
                else:
                    negative_cluster += line_read
                    acum_length += len(line_read)
                    num_analyzed += 1
                # advance in the positive cluster cache if it's too far behind
                distance = negative_cluster.start-positive_cluster.start
                while distance > self.max_delta or positive_cluster.name < negative_cluster.name: # if the negative clusters are too far behind, empty the positive cluster
                    positive_cluster.clear()
                    if positive_cluster_cache:
                        positive_cluster = positive_cluster_cache.pop() 
                    else:
                        break
        # Use the results
        data = []
        max_delta = 0
        max_corr = -1
        average_len = 0
        if num_analyzed:
            average_len = acum_length/num_analyzed
            self.logger.info("Average analyzed length %s"%average_len)
            for delta in range(self.min_delta, self.max_delta, self.delta_step):
                if delta in self.delta_results:
                    self.delta_results[delta]=self.delta_results[delta]/self.analyzed_pairs
                    data.append(self.delta_results[delta])
                    if self.delta_results[delta] > max_corr:
                        max_delta = delta
                        max_corr = self.delta_results[delta]
                    self.logger.debug('Delta %s:%s'%(delta, self.delta_results[delta]))

        if average_len: 
            self.logger.info('Correlation test RESULT: You should extend this dataset to %s nucleotides'%(max_delta+average_len))
            self.frag_size = int(round(max_delta+average_len))
        else:
            self.logger.warning("NOT ENOUGH DATA to calculate strand correlation. Assuming fragment size is 100nt. Please check your experiment file format. Are you sure this is a Punctuated ChIP-Seq experiment? This is probably not good.")
            self.frag_size = 100

        if not data:
            self.logger.warning('NOT ENOUGH DATA to plot the correlation graph. Lower the threshold of the --correlation-filter flag. In general, this means that your experiment has little coverage, or that there was some mistake. This is probably not good.')
        else: 
            try:
                if self.postscript:
                    import matplotlib
                    matplotlib.use("PS")

                import matplotlib.pyplot as plt
                plt.plot(range(self.min_delta, self.max_delta), data)
                plt.plot()
                plt.xlabel("Shift length between + and - clusters")
                plt.ylabel("Pearson correlation coefficient")
                self._save_figure("correlation")
            except ImportError:
                if self.debug:
                    raise
                self.logger.warning('Pyicoteo can not find an installation of matplotlib, so no plot will be drawn for the strand correlation. If you want to get a plot with the correlation values, install the matplotlib library (version >1.0.1).')

    def _correlate_clusters(self, positive_cluster, negative_cluster):
        distance = negative_cluster.end-positive_cluster.start
        if (distance < self.max_delta and distance > self.min_delta) and (positive_cluster.name == negative_cluster.name):
            self.analyzed_pairs+=1
            for delta in range(self.min_delta, self.max_delta+1, self.delta_step):
                r_squared = self._analize_paired_clusters(positive_cluster, negative_cluster, delta)**2
                if delta not in self.delta_results:
                    self.delta_results[delta] = r_squared
                else:
                    self.delta_results[delta] += r_squared

    def _analize_paired_clusters(self, positive_cluster, negative_cluster, delta):
        # from scipy.stats.stats import pearsonr Abandoned scipy
        positive_array = []
        negative_array = [] 
        # delta correction
        corrected_positive_start = positive_cluster.start + delta
        # add zeros at the start of the earliest cluster
        if corrected_positive_start > negative_cluster.start:
            self.__add_zeros(positive_array, corrected_positive_start - negative_cluster.start)
        elif negative_cluster.start > corrected_positive_start:  
            self.__add_zeros(negative_array, negative_cluster.start - corrected_positive_start)
        # add the values of the clusters
        positive_array.extend(positive_cluster.get_heights())
        negative_array.extend(negative_cluster.get_heights())
        # add the zeros at the end of the shortest array
        if len(positive_array) > len(negative_array):
            self.__add_zeros(negative_array, len(positive_array) - len(negative_array))
        elif len(positive_array) < len(negative_array):
            self.__add_zeros(positive_array, len(negative_array) - len(positive_array))
        return utils.pearson(negative_array, positive_array)

    
    def __add_zeros(self, array, num_zeros):
        for i in range(0, num_zeros):
            array.append(0)



