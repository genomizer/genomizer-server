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
import sys, os
import math
import random

from core import Cluster, Region, InvalidLine, InsufficientData, ConversionNotSupported
from defaults import *
import utils
import bam

from regions import AnnotationGene, AnnotationTranscript, AnnotationExon, RegionWriter, read_gff_file, get_exons, get_introns, gene_slide

import warnings

try:
    from shutil import move
except:
    from os import rename as move 

"""
Differential expression and MA plot visualization module.
"""
def _region_from_dual(self, line):
    try:
        self.cluster_aux.clear()
        self.cluster_aux.read_line(line)
        strand = None
        if self.stranded_analysis:
            strand = self.cluster_aux.strand
        ret = Region(self.cluster_aux.name, self.cluster_aux.start, self.cluster_aux.end, name2=self.cluster_aux.name2, strand=strand)
        self.cluster_aux.clear()
        return ret     

    except ValueError:
        pass #discarding header

def __calc_reg_write(self, region_file, count, calculated_region):
    if count > self.region_mintags:
        region_file.write(calculated_region.write()) 

def calculate_region(self):
    """
    Calculate a region file using the reads present in the both main files to analyze. 
    """
    self.logger.info('Generating regions...')
    self.sorted_region_path = '%s/calcregion_%s.bed'%(self._output_dir(), os.path.basename(self.current_output_path))
    region_file = open(self.sorted_region_path, 'wb')

    if self.region_magic:
        regwriter = RegionWriter(self.gff_file, region_file, self.region_magic, no_sort=self.no_sort, logger=self.logger, write_as=BED, galaxy_workarounds=self.galaxy_workarounds)
        regwriter.write_regions()

    dual_reader = utils.DualSortedReader(self.current_experiment_path, self.current_control_path, self.experiment_format, self.logger) 
    if self.stranded_analysis:
        calculate_region_stranded(self, dual_reader, region_file)    
    else:
        calculate_region_notstranded(self, dual_reader, region_file)    

    region_file.flush()

def __cr_append(self, regions, region):
    regions.append(region)

def calculate_region_notstranded(self, dual_reader, region_file):
    calculated_region = Region()        
    readcount = 1
    for line in dual_reader:
        if not calculated_region: #first region only
            calculated_region = _region_from_dual(self, line)
            calculated_region.end += self.proximity
        else:
            new_region = _region_from_dual(self, line)
            new_region.end += self.proximity
            if calculated_region.overlap(new_region):
                calculated_region.join(new_region)
                readcount += 1
            else:
                calculated_region.end -= self.proximity
                __calc_reg_write(self, region_file, readcount, calculated_region)                 
                calculated_region = new_region.copy()
                readcount = 1

    if calculated_region:
        calculated_region.end -= self.proximity
        __calc_reg_write(self, region_file, readcount, calculated_region)                         

def calculate_region_stranded(self, dual_reader, region_file):
    temp_region_file = open(self.sorted_region_path, 'wb')
    region_plus = Region()
    region_minus = Region()
    regions = []
    numreads_plus = 1
    numreads_minus = 1
    dual_reader = utils.DualSortedReader(self.current_experiment_path, self.current_control_path, self.experiment_format, self.logger)
    for line in dual_reader:
        new_region = _region_from_dual(self, line)
        new_region.end += self.proximity
        if not (region_plus and new_region.strand == PLUS_STRAND):
            region_plus = _region_from_dual(self, line)
           
        elif not (region_plus and new_region.strand == PLUS_STRAND):
            region_minus = _region_from_dual(self, line)

        else:
            if region_plus.overlap(new_region) and region_plus.strand == new_region.strand:
                region_plus.join(new_region)
                numreads_plus += 1
            elif region_minus.overlap(new_region) and region_minus.strand == new_region.strand:
                region_minus.join(new_region)
                numreads_minus += 1
            else:
                if new_region.strand == region_plus.strand:
                    region_plus.end -= self.proximity
                    self.__calc_reg_write(region_file, numreads_plus, region_plus)                      
                    region_plus = new_region.copy()  
                    numreads_plus = 1    
                else:
                    region_minus.end -= self.proximity
                    self.__calc_reg_write(region_file, numreads_minus, region_minus)         
                    region_minus = new_region.copy()  
                    numreads_minus = 1    

    if region_plus:
        region_plus.end -= self.proximity
        regions.append(region_plus)

    if region_minus:
        region_minus.end -= self.proximity
        regions.append(region_minus)

    regions.sort(key=lambda x:(x.name, x.start, x.end, x.strand))
    for region in regions:
        region_file.write(region.write())  
   
def get_zscore(x, mean, sd):    
    if sd > 0:
        return float(x-mean)/sd
    else:
        return 0 #This points are weird anyway 

def read_interesting_regions(self, file_path):
    regs = []
    try:
        regs_file = open(file_path, 'r')
        for line in regs_file:
            regs.append(line.strip())
    except IOError as ioerror:
        self.logger.warning("Interesting regions file not found")
    return regs # memory inefficient if there's a large number of interesting regions

def plot_enrichment(self, file_path):
    with warnings.catch_warnings():
        warnings.simplefilter("ignore")
        try:
            if self.postscript:
                import matplotlib
                matplotlib.use("PS")

            from matplotlib.pyplot import *
            from matplotlib import rcParams
            rcParams.update({'font.size': 22})
            rcParams['legend.fontsize'] = 14
            #decide labels
            if self.label1:
                label_main = self.label1
            else:
                if self.real_control_path and self.real_experiment_path:
                    label_main = '%s VS %s'%(os.path.basename(self.real_experiment_path), os.path.basename(self.real_control_path))
                else:
                    label_main = "A VS B"

            if self.label2:
                label_control = self.label2
            else:         
                if self.replica_path:
                    label_control = '%s(A) VS %s(A)'%(os.path.basename(self.real_experiment_path), os.path.basename(self.replica_path))
                else:
                    label_control = 'Background distribution'

            #self.logger.info("Interesting regions path: %s" % (self.interesting_regions))
            interesting_regs = []
            if self.interesting_regions:
                self.logger.info("Reading interesting regions...")
                interesting_regs = read_interesting_regions(self, self.interesting_regions)
            #self.logger.info("Interesting regions: %s" % (interesting_regs))
            #self.logger.info("Plot path: %s" % (file_path))
            interesting_A = []
            interesting_M = []
            #self.logger.info("disable_significant: %s" % (self.disable_significant_color))

            A = []
            A_prime = []
            M = []
            M_significant = []
            A_significant = []
            M_prime = []
            A_medians = []      
            points = []
            minus_points = []
            all_points = []
            figure(figsize=(14,22))
            biggest_A  = -sys.maxint #for drawing
            smallest_A = sys.maxint #for drawing
            biggest_M = 0 #for drawing
            self.logger.info("Loading table...")
            for line in open(file_path):
                sline = line.split()
                try:
                    enrich = dict(zip(enrichment_keys, sline)) 
                    # WARNING: for slide inter and slide intra: name2 = 'start:end' (no gene_id, FIXME?)
                    name2 = enrich['name2'].split(':')
                    gene_id = name2[0]
                    if len(name2) >= 2:
                        transcript_id = name2[1] # consider transcript_id? (exons)
                    else:
                        transcript_id = None

                    if gene_id in interesting_regs or transcript_id in interesting_regs:
                        interesting_M.append(float(enrich["M"]))
                        interesting_A.append(float(enrich["A"]))

                    biggest_A = max(biggest_A, float(enrich["A"]))         
                    smallest_A = min(smallest_A, float(enrich["A"]))   
                    biggest_M = max(biggest_M, abs(float(enrich["M"])))          
                    biggest_A = max(biggest_A, float(enrich["A_prime"]))         
                    smallest_A = min(smallest_A, float(enrich["A_prime"]))
                    biggest_M = max(biggest_M, abs(float(enrich["M_prime"])))
                    positive_point = self.zscore*float(enrich["sd"])+float(enrich["mean"])
                    negative_point = -self.zscore*float(enrich["sd"])+float(enrich["mean"])
                    A_median = float(enrich["A_median"])
                    all_points.append((A_median, positive_point, negative_point))
                    if abs(float(enrich["zscore"])) < self.zscore:
                        M.append(float(enrich["M"]))
                        A.append(float(enrich["A"]))
                    else:
                        M_significant.append(float(enrich["M"]))
                        A_significant.append(float(enrich["A"]))
       
                    M_prime.append(float(enrich["M_prime"]))
                    A_prime.append(float(enrich["A_prime"]))
                except ValueError:
                    pass #to skip the header
            all_points.sort(key= lambda x:x[0])
            
            for t in all_points:
                (A_medians.append(t[0]), points.append(t[1]), minus_points.append(t[2])) 
                    
            if points:
                margin = 1.1
                A_medians.append(biggest_A*margin)
                points.append(points[-1])
                minus_points.append(minus_points[-1])
                A_medians.insert(0, smallest_A)
                points.insert(0, points[0])
                minus_points.insert(0, minus_points[0])
                self.logger.info("Plotting points...")
                #Background plot
                subplot(211, axisbg="lightyellow")
                xlabel('Average', fontsize=30)
                ylabel('Log2 ratio', fontsize=30)
                axis([smallest_A*margin, biggest_A*margin, -biggest_M*margin, biggest_M*margin])
                plot(A_prime, M_prime, '.', label=label_control, color = '#666666')
                plot(A_medians, points, 'r--', label="Z-score (%s)"%self.zscore)            
                plot(A_medians, minus_points,  'r--')            
                axhline(0, linestyle='--', color="grey", alpha=0.75)  
                leg = legend(fancybox=True, scatterpoints=1, numpoints=1, loc=2, ncol=4, mode="expand")
                leg.get_frame().set_alpha(0.5)
                #Experiment plot
                subplot(212, axisbg="lightyellow")
                axis([smallest_A*margin, biggest_A*margin, -biggest_M*margin, biggest_M*margin])
                plot(A, M, 'k.', label=label_main)
                if self.disable_significant_color:
                    significant_marker = 'ko'
                else:
                    significant_marker = 'ro'

                plot(A_significant, M_significant, significant_marker, label="%s (significant)"%label_main)
                plot(A_medians, points, 'r--', label="Z-score (%s)"%self.zscore)            
                plot(A_medians, minus_points, 'r--')
                if self.interesting_regions:
                    interesting_label = label_main + ' (interesting)'
                    plot(interesting_A, interesting_M, 'H', label=interesting_label, color='#00EE00') # plotting "interesting" regions

                axhline(0, linestyle='--', color="grey", alpha=0.75)
                xlabel('Average', fontsize=30)
                ylabel('Log2 ratio', fontsize=30)
                leg2 = legend(fancybox=True, scatterpoints=1, numpoints=1, loc=2, ncol=4)
                leg2.get_frame().set_alpha(0.7)
                self._save_figure("enrichment_MA", width=500, height=2800)
            else:
                self.logger.warning("Nothing to plot.")

        except ImportError:
            if self.debug:
                raise
            __matplotlibwarn(self)

def __matplotlibwarn(self):
    #FIXME move to utils.py or plotting module
    self.logger.warning('Pyicos can not find an installation of matplotlib, so no plot will be drawn. If you want to get a plot with the correlation values, install the matplotlib library.')    

def __calc_M(signal_a, signal_b):
    return math.log(float(signal_a)/float(signal_b), 2)

def __calc_A(signal_a, signal_b):
    return (math.log(float(signal_a), 2)+math.log(float(signal_b), 2))/2    
   
def _calculate_MA(self, region_path, read_counts, factor = 1, replica_factor = 1, file_a_reader=None, file_b_reader=None, replica_reader=None):
    tags_a = []
    tags_b = []
    numreads_background_1 = 0
    numreads_background_2 = 0
    total_reads_background_1 = 0
    total_reads_background_2 = 0
    self.logger.debug("Inside _calculate_MA")
    self.regions_analyzed_count = 0
    enrichment_result = [] #This will hold the name, start and end of the region, plus the A, M, 'A and 'M
    if NOWRITE not in self.operations:
        out_file = open(self.current_output_path, 'wb')

    for region_line in open(region_path):
        sline = region_line.split()
        region_of_interest = self._region_from_sline(sline)            
        if region_of_interest:
            region_a = None
            replica = None
            replica_tags = None
            signal_a = -1
            signal_b = -1
            signal_background_1 = -1
            signal_background_2 = -1
            swap1 = Region()
            swap2 = Region()
            if read_counts:
                signal_a = float(sline[6])
                signal_b = float(sline[7])*factor
                signal_background_1 = float(sline[8])
                signal_background_2 = float(sline[9])*replica_factor
                if CHECK_REPLICAS in self.operations: 
                    self.experiment_values.append(signal_background_1)
                    self.replica_values.append(signal_background_2)

            else:
                self.logger.debug("Reading tags for %s ..."%region_of_interest)
                if self.experiment_format == BAM:

                    tags_a = len(file_a_reader.get_overlaping_clusters(region_of_interest, overlap=self.overlap))
                    tags_b = len(file_b_reader.get_overlaping_clusters(region_of_interest, overlap=self.overlap))
                else:
                    tags_a = file_a_reader.get_overlaping_counts(region_of_interest, overlap=self.overlap)
                    tags_b = file_b_reader.get_overlaping_counts(region_of_interest, overlap=self.overlap)

                if self.use_replica:            
                    if self.experiment_format == BAM:
                        replica_tags = len(replica_reader.get_overlaping_clusters(region_of_interest, overlap=self.overlap))
                    else:
                        replica_tags = replica_reader.get_overlaping_counts(region_of_interest, overlap=self.overlap)

                self.logger.debug("... done. tags_a: %s tags_b: %s"%(tags_a, tags_b))

                #if we are using pseudocounts, use the union, use the intersection otherwise
                if (self.pseudocount and (tags_a or tags_b)) or (not self.pseudocount and tags_a and tags_b): 
                    signal_a = region_of_interest.normalized_counts(self.len_norm, self.n_norm, self.total_regions, self.pseudocount, factor, self.total_reads_a, tags_a)
                    signal_b = region_of_interest.normalized_counts(self.len_norm, self.n_norm, self.total_regions, self.pseudocount, factor, self.total_reads_b, tags_b)
                    self.already_norm = True

            if not self.counts_file:
                if (self.pseudocount and (tags_a or tags_b)) or (not self.pseudocount and tags_a and tags_b): 
                    if self.use_replica:
                        replica = region_of_interest.copy()
                        #replica.add_tags(replica_tags)
                        numreads_background_1 = tags_a
                        numreads_background_2 = replica_tags
                        total_reads_background_1 = self.total_reads_a
                        total_reads_background_2 = self.total_reads_replica
                        signal_background_1 = signal_a
                        signal_background_2 = region_of_interest.normalized_counts(self.len_norm, self.n_norm, self.total_regions, self.pseudocount, 
                                                                        replica_factor, self.total_reads_replica, replica_tags)

                    else:
                        numreads_background_1 = 0
                        numreads_background_2 = 0
                        for i in range(0, tags_a+tags_b):
                            if random.uniform(0,2) > 1:
                                numreads_background_1 += 1
                            else:
                                numreads_background_2 += 1

                        total_reads_background_1 = total_reads_background_2 = self.average_total_reads
                        signal_background_1 = region_of_interest.normalized_counts(self.len_norm, self.n_norm, self.total_regions, self.pseudocount, 
                                                                                   replica_factor, self.average_total_reads, numreads_background_1)
                        signal_background_2 = region_of_interest.normalized_counts(self.len_norm, self.n_norm, self.total_regions, self.pseudocount, 
                                                                                   replica_factor, self.average_total_reads, numreads_background_2)

            #if there is no data in the replica or in the swap and we are not using pseudocounts, dont write the data 
            if signal_a > 0 and signal_b > 0 and signal_background_1 > 0 and signal_background_2 > 0 or self.use_MA:
                if self.use_MA and not self.already_norm:
                    A = float(sline[10])
                    M = float(sline[11])
                    A_prime = float(sline[16])
                    M_prime = float(sline[17])
                else:
                    if not self.already_norm: #TODO refractor
                        if self.len_norm: #read per kilobase in region
                            signal_a = 1e3*(float(signal_a)/len(region_of_interest))
                            signal_b = 1e3*(float(signal_b)/len(region_of_interest))
                            signal_background_1 = 1e3*(float(signal_background_1)/len(region_of_interest))
                            signal_background_2 = 1e3*(float(signal_background_2)/len(region_of_interest))

                        if self.n_norm: #per million reads in the sample
                            signal_a = 1e6*(float(signal_a)/self.total_reads_a)
                            signal_b = 1e6*(float(signal_b)/self.total_reads_b)
                            if self.use_replica:
                                signal_background_1 = signal_a
                                signal_background_2 = 1e6*(float(signal_background_2)/self.total_reads_replica)
                            else:
                                signal_background_1 = 1e6*(float(signal_background_1)/self.average_total_reads)
                                signal_background_2 = 1e6*(float(signal_background_2)/self.average_total_reads)
                            
                    A = __calc_A(signal_a, signal_b)
                    M = __calc_M(signal_a, signal_b)
                    A_prime = __calc_A(signal_background_1, signal_background_2)
                    M_prime = __calc_M(signal_background_1, signal_background_2)
                    if CHECK_REPLICAS in self.operations: 
                        self.experiment_values.append(signal_background_1)
                        self.replica_values.append(signal_background_2)

                if NOWRITE not in self.operations:
                    out_file.write("%s\n"%("\t".join([region_of_interest.write().rstrip("\n"), str(signal_a), str(signal_b), str(signal_background_1), str(signal_background_2), str(A), str(M), str(self.total_reads_a), str(self.total_reads_b), str(tags_a), str(tags_b),  str(A_prime), str(M_prime), str(total_reads_background_1), str(total_reads_background_2), str(numreads_background_1), str(numreads_background_2)])))
                self.regions_analyzed_count += 1

    self.logger.debug("LEAVING _calculate_MA")
    if NOWRITE in self.operations:
        return ""
    else:
        out_file.flush()
        out_file.close()
        # Outputting to HTML (if specified)
        if self.html_output is not None:
            self.logger.info("Generating HTML")
            try:
                from jinja2 import Environment, PackageLoader, Markup
            except:
                self.logger.error("Could not find the jinja2 library")
                return out_file.name

            loadr = PackageLoader('pyicoteolib', 'templates')
            env = Environment(loader=loadr)
            template = env.get_template('enrich_html.html')

            def jinja_read_file(filename):
                f = open(filename, 'r')
                #for line in f:
                #    print line
                txt = ''.join(f.readlines())
                f.close()
                return txt
            env.globals['jinja_read_file'] = jinja_read_file

            if self.galaxy_workarounds: # Galaxy changes the working directory when outputting multiple files
                parent_dir = "./"
            else:
                parent_dir = os.sep.join(out_file.name.split(os.sep)[0:-1]) + "/"
            plot_path = parent_dir + "enrichment_MA_" + out_file.name.split(os.sep)[-1] + ".png"
            bed_path = parent_dir + out_file.name.split(os.sep)[-1]

            html_file = open(self.html_output, 'w')
            html_file.write(template.render({'page_title': 'Enrichment results', 'results_output': jinja_read_file(out_file.name), 'plot_path': plot_path, 'bed_path': bed_path}))
            html_file.flush()
            html_file.close()

        return out_file.name

def _calculate_total_lengths(self):
    msg = "Calculating enrichment in regions"
    if self.counts_file: 
        self.sorted_region_path = self.counts_file
        if (not self.total_reads_a or not self.total_reads_b or (not self.total_reads_replica and self.use_replica)) and not self.use_MA:
            self.logger.info("... counting from counts file...")
            self.total_reads_a = 0
            self.total_reads_b = 0
            if self.total_reads_replica:
                self.total_reads_replica = 0
            else:
                self.total_reads_replica = 1
            for line in open(self.counts_file):
                try:
                    enrich = dict(zip(enrichment_keys, line.split()))
                    self.total_reads_a += float(enrich["signal_a"])
                    self.total_reads_b += float(enrich["signal_b"])
                    if self.use_replica:
                        self.total_reads_replica += float(enrich["signal_prime_2"])
                except ValueError:
                    self.logger.debug("(Counting) skip header...")
    else:
        self.logger.info("... counting number of lines in files...")
        if not self.total_reads_a:
            if self.experiment_format == BAM:
                self.total_reads_a = bam.size(self.current_experiment_path)
            else:
                self.total_reads_a = sum(1 for line in utils.open_file(self.current_experiment_path, self.experiment_format, logger=self.logger))
        if not self.total_reads_b:
            if self.experiment_format == BAM:
                self.total_reads_b = bam.size(self.current_control_path)
            else:
                self.total_reads_b = sum(1 for line in utils.open_file(self.current_control_path, self.control_format, logger=self.logger))
        if self.use_replica and not self.total_reads_replica:
            if self.experiment_format  == BAM:
                self.total_reads_replica = bam.size(self.replica_path)
            else:
                self.total_reads_replica = sum(1 for line in utils.open_file(self.replica_path, self.experiment_format, logger=self.logger))

        self.logger.debug("Number lines in experiment A: %s Experiment B: %s"%(self.total_reads_a, self.total_reads_b))
        if self.use_replica:
            msg = "%s using replicas..."%msg
        else:
            msg = "%s using swap..."%msg

        self.logger.info(msg)

    self.average_total_reads = (self.total_reads_a+self.total_reads_b)/2        

def enrichment(self):
    file_a_reader = file_b_reader = replica_reader = None
    self.use_replica = (bool(self.replica_path) or (bool(self.counts_file) and self.use_replica_flag))
    self.logger.debug("Use replica: %s"%self.use_replica)
    if not USE_MA in self.operations:
        _calculate_total_lengths(self)
    if not self.counts_file:
        file_a_reader = utils.read_fetcher(self.current_experiment_path, self.experiment_format, cached=self.cached, logger=self.logger, use_samtools=self.use_samtools, access_sequential=self.access_sequential, only_counts=True)
        file_b_reader = utils.read_fetcher(self.current_control_path, self.experiment_format, cached=self.cached, logger=self.logger, use_samtools=self.use_samtools, access_sequential=self.access_sequential, only_counts=True)
        if self.use_replica:
            replica_reader = utils.read_fetcher(self.current_replica_path, self.experiment_format, cached=self.cached, logger=self.logger, use_samtools=self.use_samtools, access_sequential=self.access_sequential, only_counts=True)

        if self.sorted_region_path:
            self.logger.info('Using region file %s (%s)'%(self.region_path, self.region_format))
        else:
            calculate_region(self) #create region file semi automatically

        self.total_regions = sum(1 for line in open(self.sorted_region_path))

    self.logger.info("... analyzing regions, calculating normalized counts, A / M and replica or swap...")
    self.already_norm = False
    if self.use_MA:
        ma_path = self.counts_file
    else:
        ma_path = self.sorted_region_path

    out_path = _calculate_MA(self, ma_path, bool(self.counts_file), 1, 1, file_a_reader, file_b_reader, replica_reader)
    self.already_norm = True
    self.logger.debug("Already normalized: %s"%self.already_norm)
    if self.tmm_norm:
        if CHECK_REPLICAS in self.operations: 
            self.experiment_values = []
            self.replica_values = []

        self.logger.info("TMM Normalizing...")
        tmm_factor = calc_tmm_factor(self, out_path, self.regions_analyzed_count, False)
        replica_tmm_factor = 1
        if self.use_replica:
            replica_tmm_factor = calc_tmm_factor(self, out_path, self.regions_analyzed_count, True)
        #move output file to old output
        #use as input
        old_output = '%s/notnormalized_%s'%(self._current_directory(), os.path.basename(self.current_output_path))
        move(os.path.abspath(self.current_output_path), old_output)
        out_path = _calculate_MA(self, old_output, True, tmm_factor, replica_tmm_factor, True) #recalculate with the new factor, using the counts again

    if self.quant_norm:
        self.logger.info("Full quantile normalization...")
        signal_a = []
        signal_prime_1 = []
        enrich = []
        for line in open(out_path):
            sline = line.split()
            enrich_line = dict(zip(enrichment_keys, sline))
            enrich.append(enrich_line)
            signal_a.append(float(enrich_line['signal_a']))
            signal_prime_1.append(float(enrich_line['signal_prime_1']))
        
        #full quantile normalization
        signal_a.sort()
        enrich.sort(key=lambda x:float(x['signal_b'])) 
        quant_counts = open('%s/quantcounts_%s'%(self._current_directory(), os.path.basename(self.current_output_path)), 'w')
        for i in range(len(enrich)):
            enrich[i]['signal_b'] = signal_a[i] 

        self.logger.info("Full quantile normalization replica...")
        #full quantile normalization of the replica
        signal_prime_1.sort()
        enrich.sort(key=lambda x:float(x['signal_prime_2']))
        for i in range(len(enrich)):
            enrich[i]['signal_prime_2'] = signal_prime_1[i]         
            quant_counts.write("%s\n"%"\t".join(str(enrich[i][key]) for key in enrichment_keys[:20])) #write the lines

        quant_counts.flush()
        out_path = _calculate_MA(self, quant_counts.name, True, 1, 1, True) #recalculate with the new factor, using the counts again
        self._manage_temp_file(quant_counts.name)

    self.logger.info("%s regions analyzed."%self.regions_analyzed_count)
    if not NOWRITE in self.operations:
        self.logger.info("Enrichment result saved to %s"%self.current_output_path)

    if CHECK_REPLICAS in self.operations:
        check_replica(self)

    return out_path

def _sub_tmm(counts_a, counts_b, reads_a, reads_b):
    return (counts_a-reads_a)/(counts_a*reads_a) + (counts_b-reads_b)/(counts_b*reads_b)            

def calc_tmm_factor(self, file_counts, total_regions, replica):
    if replica:
        signal_1 = "signal_prime_1"
        signal_2 = "signal_prime_2"
        M = "M_prime"
        reads_2 = self.total_reads_replica            
    else:
        signal_1 = "signal_a"
        signal_2 = "signal_b"
        M = "M"
        reads_2 = self.total_reads_b

    values_list = []
    #read the file inside the values_list
    for line in open(file_counts):
        sline = line.split()
        values_list.append(dict(zip(enrichment_keys, sline)))

    a_trim_number = int(round(total_regions*self.a_trim))
    #discard the bad A
    self.logger.debug("Removing the worst A (%s regions, %s percent)"%(a_trim_number, self.a_trim*100))
    values_list.sort(key=lambda x:float(x["A"])) #sort by A
    for i in range (0, a_trim_number):
        values_list.pop(0)

    values_list.sort(key=lambda x:float(x[M])) #sort by M              
    m_trim_number = int(round(total_regions*(self.m_trim/2))) #this number is half the value of the flag, because we will trim half below, and half over 
    #remove on the left
    for i in range(0, m_trim_number):
        values_list.pop(0)
    #remove on the right
    for i in range(0, m_trim_number):
        values_list.pop(-1)

    #now calculate the normalization factor
    arriba = 0
    abajo = 0
    for value in values_list:
        w = _sub_tmm(float(value[signal_1]), float(value[signal_2]), self.total_reads_a, reads_2)                  
        arriba += w*float(value[M])
        abajo += w

    try:
        factor = 2**(arriba/abajo)
    except ZeroDivisionError:
        self.logger.warning("Division by zero, TMM factor could not be calculated.")
        factor = 1

    if replica:    
        self.logger.info("Replica TMM Normalization Factor: %s"%factor)
    else:
        self.logger.info("TMM Normalization Factor: %s"%factor)  
      
    return factor

def __load_enrichment_result(values_path):
    ret = []
    for line in open(values_path):
        sline = line.split()
        try:
            float(sline[1])
            ret.append(dict(zip(enrichment_keys, sline)))
        except ValueError:
            pass

    return ret        

def calculate_zscore(self, values_path): 
    num_regions = sum(1 for line in open(values_path))
    bin_size = int(self.binsize*num_regions)
    if bin_size < 50:
        self.logger.warning("The bin size results in a sliding window smaller than 50, adjusting window to 50 in order to get statistically meaningful results.")
        bin_size = 50

    bin_step = max(1, int(round(self.bin_step*bin_size)))
    self.logger.info("Enrichment window calculation using a sliding window size of %s, sliding with a step of %s"%(bin_size, bin_step))
    self.logger.info("... calculating zscore...")
    enrichment_result = __load_enrichment_result(values_path)
    enrichment_result.sort(key= lambda x:(float(x["A_prime"])))
    self.logger.debug("Number of loaded counts: %s"%len(enrichment_result))        
    self.points = []
    #get the standard deviations
    for i in range(0, num_regions-bin_size+bin_step, bin_step):
        #get the slice
        if i+bin_size < num_regions:
            result_chunk = enrichment_result[i:i+bin_size] 
        else:
            result_chunk = enrichment_result[i:]  #last chunk

        #retrieve the values
        mean_acum = 0
        a_acum = 0
        Ms_replica = []
        for entry in result_chunk:
            mean_acum += float(entry["M_prime"])
            a_acum += float(entry["A_prime"])
            Ms_replica.append(float(entry["M_prime"]))

        #add them to the points of mean and sd
        mean = mean_acum/len(result_chunk)
        sd = math.sqrt((sum((x - mean)**2 for x in Ms_replica))/len(Ms_replica))
        #the A median
        A_median = a_acum / len(result_chunk)
        self.points.append([A_median, mean, sd]) #The A asigned to the window, the mean and the standard deviation  
        #self.logger.debug("Window of %s length, with A median: %s mean: %s sd: %s"%(len(result_chunk), self.points[-1][0], self.points[-1][1], self.points[-1][2], len(self.points)))  

    #update z scores
    for entry in enrichment_result:
        entry["A_median"] = 0
        entry["mean"] = 0
        entry["sd"] = 0
        entry["zscore"] = 0
        closest_A = sys.maxint
        sd_position = 0
        for i in range(0, len(self.points)):
            new_A = self.points[i][0]
            if new_A != closest_A: #skip repeated points
                if abs(closest_A - float(entry["A"])) >= abs(new_A - float(entry["A"])):
                    closest_A = new_A
                    sd_position = i
                else:
                    break #already found, no need to go further since the points are ordered
                    
        entry["A_median"] = closest_A
        if self.points: #only calculate if there where windows...
            __sub_zscore(self.sdfold, entry, self.points[sd_position]) 
    
    if not self.points: # ... otherwise give a warning
        self.logger.warning("Insufficient number of regions analyzed (%s), z-score values could not be calculated"%num_regions)

    enrichment_result.sort(key=lambda x:(x["name"], int(x["start"]), int(x["end"])))
    old_file_path = '%s/before_zscore_%s'%(self._current_directory(), os.path.basename(values_path)) #create path for the outdated file
    move(os.path.abspath(values_path), old_file_path) #move the file
    new_file = file(values_path, 'w') #open a new file in the now empty file space  
    if not self.skip_header:
        new_file.write('\t'.join(enrichment_keys))
        new_file.write('\n') 

    for entry in enrichment_result:    
        new_file.write("\t".join(str(entry[key]) for key in enrichment_keys)+"\n")

    self._manage_temp_file(old_file_path)
    return values_path

def __sub_zscore(sdfold, entry, point):
    entry["mean"] = str(point[1])
    entry["sd"] = str(point[2])
    entry["zscore"] = str(get_zscore(float(entry["M"]), float(entry["mean"]), sdfold*float(entry["sd"])))

def check_replica(self):
    #discard everything below the flag
    new_experiment = []
    new_replica = []
    min_value = sys.maxint
    max_value = -sys.maxint
    for i in range(len(self.replica_values)):
        if self.experiment_values[i] > self.count_filter and self.replica_values[i] > self.count_filter:
            new_experiment.append(math.log(self.experiment_values[i], 2))
            new_replica.append(math.log(self.replica_values[i], 2))
            min_value = min(min_value, math.log(self.experiment_values[i], 2), math.log(self.replica_values[i], 2))
            max_value = max(max_value, math.log(self.experiment_values[i], 2), math.log(self.replica_values[i], 2))
    #print self.replica_values
    self.experiment_values = new_experiment
    self.replica_values = new_replica
    try:
        if self.postscript:
            import matplotlib
            matplotlib.use("PS")
        from matplotlib.pyplot import plot, show, xlabel, ylabel, axhline, axis, clf, text, title, xlim, ylim
    except:
        __matplotlibwarn(self)
        return 0
    clf()
    r_squared = utils.pearson(self.experiment_values, self.replica_values)**2
    text(min_value+abs(max_value)*0.1, max_value-abs(max_value)*0.2, r'Pearson $R^2$= %s'%round(r_squared, 3), fontsize=18, bbox={'facecolor':'yellow', 'alpha':0.5, 'pad':10})
    xlabel("log2(%s)"%self.experiment_label, fontsize=18)
    ylabel("log2(%s)"%self.replica_label, fontsize=18)
    xlim(min_value, max_value)
    ylim(min_value, max_value)
    title(self.title_label, fontsize=24)
    plot(self.experiment_values, self.replica_values, '.')

    self._save_figure("check_replica")   

def check_replica_correlation(self):
    "No usado, de momento" 
    min_tags = 20
    experiment_reader = utils.read_fetcher(self.current_experiment_path, self.experiment_format, cached=self.cached, logger=self.logger, use_samtools=self.use_samtools, access_sequential=self.access_sequential)
    replica_reader = utils.read_fetcher(self.current_replica_path, self.experiment_format, cached=self.cached, logger=self.logger, use_samtools=self.use_samtools, access_sequential=self.access_sequential)
    correlations_acum = 0
    num_correlations = 0
    for region_line in open(self.region_path):
        sline = region_line.split()
        region_experiment = self._region_from_sline(sline)       
        region_replica = region_experiment.copy()  
        tags_experiment = experiment_reader.get_overlaping_clusters(region_experiment, overlap=1)
        tags_replica = replica_reader.get_overlaping_clusters(region_experiment, overlap=1)
        count_experiment = len(tags_experiment)
        count_replica = len(tags_replica)
        correlations = []
        if count_experiment+count_replica > min_tags:
            region_experiment.add_tags(tags_experiment, clusterize=True)
            region_replica.add_tags(tags_replica, clusterize=True)     
            num_correlations += 1
            correlation = utils.pearson(region_experiment.get_array(), region_replica.get_array())
            correlations_acum += max(0, correlation)
            correlations.append(correlation)

    print correlations_acum/num_correlations
    try:
        if self.postscript:
            import matplotlib
            matplotlib.use("PS")
        from matplotlib.pyplot import plot, boxplot, show, legend, figure, xlabel, ylabel, subplot, axhline, axis
    except:
        __matplotlibwarn(self)
        return 0

    print correlations
    boxplot(correlations)
    self._save_figure("check_replica")    