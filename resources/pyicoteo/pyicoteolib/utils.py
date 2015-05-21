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

import math
import os
import sys
from tempfile import gettempdir
from heapq import heappop, heappush
from itertools import islice, cycle, chain
import logging

from core import Cluster, Region, InvalidLine, InsufficientData, ConversionNotSupported

from defaults import *
from bam import BamReader, BamFetcher, BamFetcherSamtools

class OperationFailed(Exception):
    pass                 

def manage_temp_file(path, keep_temp, logger):
    """A temporary file that is no longer needed is given, and depending on the value of self.keep_temp its removed or kept"""
    if keep_temp:
        logger.info("Temp file kept at: %s (remember cleaning them!)"%path)
    else:
        os.remove(path)
        logger.info('Temporary file %s removed'%path)


def sorting_lambda(format):
    if format == ELAND:
        return lambda x:(x.split()[6], int(x.split()[7]), len(x.split()[1]))
    elif format == SAM or format == BAM:
        return lambda x:(x.split()[2], int(x.split()[3]), len(x.split()[9]))
    else: #anything else, we fall to BED like format for sorting
        return lambda x:(x.split()[0],int(x.split()[1]),int(x.split()[2]))


def get_logger(logger_name, verbose=True, debug=False): 
    """
    Getting a logger in the pyicos format. 
    This should substitute the 'passing down' logging capabilities of the Turbomix class. 
    """
    logging_format= "%(asctime)s (PID:%(process)s) - %(levelname)s - %(message)s"
    logging.basicConfig(filename=logger_name, format=logging_format)
    logger = logging.getLogger(logger_name)
    if debug:
        logger.setLevel(logging.DEBUG)
    else:
        logger.setLevel(logging.INFO)

    ch = logging.StreamHandler()
    if debug: 
        ch.setLevel(logging.DEBUG)
    elif verbose:
        ch.setLevel(logging.INFO)
    else:
        ch.setLevel(logging.WARNING)

    formatter = logging.Formatter("%(asctime)s (PID:%(process)s) - %(levelname)s - %(message)s")
    ch.setFormatter(formatter)
    logger.addHandler(ch)
    return logger


def open_file(path, format=None, gzipped=False, logger=None):
    """To open files that are not binary format, like BAM and gzip files"""
    if format == BAM:
        if logger: logger.debug("read_fetcher: Returning BAM reader")
        return BamReader(path, logger)
    elif gzipped:
        print "Open Gzipped! (not implemented)"
        sys.exit(1)
    else:
        return open(path, 'rb')

def read_fetcher(file_path, experiment_format, read_half_open=False, rounding=True, cached=True, logger=None, use_samtools=False, access_sequential=True, only_counts=False):
    if experiment_format == BAM: #access_sequential
        if use_samtools:
            return BamFetcherSamtools(file_path, read_half_open, rounding, cached, logger)
        else:
            return SortedFileReader(file_path, experiment_format, read_half_open, rounding, cached, logger)
    #elif experiment_format == BAM:
        #return BamFetcher(file_path, read_half_open, rounding, cached, logger) #doesnt work yet
    elif only_counts:
        return SortedFileCountReader(file_path, experiment_format, read_half_open, rounding, cached, logger)
    else:
        return SortedFileReader(file_path, experiment_format, read_half_open, rounding, cached, logger)

def add_slash_to_path(path):
    if path[-1] != '/':
        path = '%s/'%path
    return path
    
def poisson(actual, mean):
    """From StackOverflow: This algorithm is iterative,
       to keep the components from getting too large or small"""
    try:
        p = math.exp(-mean)
        for i in xrange(actual):
            p *= mean
            p /= i+1
        return p
    
    except OverflowError:
        return 0

def pearson(list_one, list_two):
    """
    Accepts paired lists and returns a number between -1 and 1,
    known as Pearson's r, that indicates how closely correlated
    the two datasets are.
    A score close to one indicates a high positive correlation.
    That means that X tends to be big when Y is big.
    A score close to negative one indicates a high negative correlation.
    That means X tends to be small when Y is big.
    A score close to zero indicates little correlation between the two
    datasets.

    h3. Sources
    http://en.wikipedia.org/wiki/Pearson_product-moment_correlation_coefficient
    http://davidmlane.com/hyperstat/A56626.html
    http://www.cmh.edu/stats/definitions/correlation.htm
    http://www.amazon.com/Programming-Collective-Intelligence-Building-Applications/dp/0596529325
    """
    if len(list_one) != len(list_two):
        raise ValueError('The two lists you provided do not have the name number \
        of entries. Pearson\'s r can only be calculated with paired data.')

    n = len(list_one)

    # Convert all of the data to floats
    list_one = map(float, list_one)
    list_two = map(float, list_two)

    # Add up the total for each
    sum_one = sum(list_one)
    sum_two = sum(list_two)

    # Sum the squares of each
    sum_of_squares_one = sum([pow(i, 2) for i in list_one])
    sum_of_squares_two = sum([pow(i, 2) for i in list_two])

    # Sum up the product of each element multiplied against its pair
    product_sum = sum([item_one * item_two for item_one, item_two in zip(list_one, list_two)])

    # Use the raw materials above to assemble the equation
    pearson_numerator = product_sum - (sum_one * sum_two / n)
    pearson_denominator = math.sqrt((sum_of_squares_one - pow(sum_one,2) / n) * (sum_of_squares_two - pow(sum_two,2) / n))

    # To avoid dividing by zero,
    # catch it early on and drop out
    if pearson_denominator == 0:
        return 0

    # Divide the equation to return the r value
    r = pearson_numerator / pearson_denominator
    return r


def list_available_formats():
    #TODO this is very bad, this function is a trap. Fix
    print 'Formats Pyicoteo can read:'
    for format in READ_FORMATS:
        print format
    print '\nFormats Pyicoteo can write:'
    for format in WRITE_FORMATS:
        print format
    sys.exit(0)

class SafeReader:
    def __init__(self, logger=None):
        self.logger = logger
        self.invalid_count = 0
        self.invalid_limit = 2000

    def safe_read_line(self, cluster, line):
        """Reads a line in a file safely catching the error for headers.
        Triggers OperationFailed exception if too many invalid lines are fed to the method"""
        try:
            cluster.read_line(line)
        except InvalidLine:
            if self.invalid_count > self.invalid_limit:

                if self.logger:
                    self.logger.error('Limit of invalid lines: Check the experiment, control, and region file formats, probably the error is in there. Pyicoteo by default expects bedpk files, except for region files, which are bed files')

                raise OperationFailed
            else:
                if self.logger: self.logger.debug("Skipping invalid (%s) line (or header): %s"%(cluster.reader.format, line))
                self.invalid_count += 1
    
class BigSort:
    """
    This class can sort huge files without loading them fully into memory.
    Based on a recipe by Tomasz Bieruta.

    """
    def __init__(self, file_format, read_half_open=False, frag_size=0, id=0, logger=True, filter_chunks=True, push_distance=0, buffer_size = 320000, temp_file_size = 8000000):
        self.logger = logger
        self.file_format = file_format
        self.frag_size = frag_size
        self.push_distance = push_distance
        self.buffer_size = buffer_size
        self.temp_file_size = temp_file_size
        self.filter_chunks = filter_chunks
        try:
            if self.file_format:
                self.cluster = Cluster(read=self.file_format, write=self.file_format, read_half_open=read_half_open, write_half_open=read_half_open, logger=self.logger)
        except ConversionNotSupported:
            self.logger.error('')
            self.logger.error('Reading "%s" is not supported (unknown format).\n'%self.file_format)
            list_available_formats()

        self.id = id
        
    def skipHeaderLines(self, key, experiment_file):
        validLine = False
        count = 0
        while not validLine and count < 400: #file formats with more than 400 lines of header should die anyway 
            try:
                currentPos = experiment_file.tell()
                line = [experiment_file.readline()]
                line.sort(key=key)
                experiment_file.seek(currentPos)
                validLine = True
            except:
                count += 1

    def remove_chunks(self, chunks):
        for chunk in chunks:
            try:
                os.remove(chunk)
            except:
                pass
    
    def filter_chunk(self, chunk):
        filtered_chunk = []
        for line in chunk:
            if self.cluster.reader.quality_filter(line):    
                self.cluster.clear()
                try:           
                    self.cluster.read_line(line)
                    if self.frag_size:
                        self.cluster.extend(self.frag_size)

                    if self.push_distance:
                        self.cluster.push(self.push_distance)

                except InvalidLine:
                    if self.logger: self.logger.debug('Discarding middle invalid line: %s'%line)
                                   
                if not self.cluster.is_empty():
                    filtered_chunk.append(self.cluster.write_line())

        return filtered_chunk

    def sort(self, input, output=None, key=None, tempdirs=[]):
        if key is None: # unless explicitly specified, sort with the default lambda
            key = sorting_lambda(self.file_format)

        if not tempdirs:
            tempdirs.append(gettempdir())

        input_file = open(input,'rb',self.temp_file_size)
        self.skipHeaderLines(key, input_file)
        try:
            input_iterator = iter(input_file)
            chunks = []
            for tempdir in cycle(tempdirs):
                current_chunk = list(islice(input_iterator, self.buffer_size))
                if self.filter_chunks:
                    current_chunk = self.filter_chunk(current_chunk) 
                if current_chunk:
                    if self.logger: self.logger.debug("Chunk: len current_chunk: %s chunks: %s temp_file_size %s buffer_size %s"%(len(current_chunk), len(chunks), self.temp_file_size, self.buffer_size))
                    current_chunk.sort(key=key)
                    output_chunk = open(os.path.join(tempdir,'%06i_%s_%s'%(len(chunks), os.getpid(), self.id)),'w+b',self.temp_file_size)
                    output_chunk.writelines(current_chunk)
                    output_chunk.flush()
                    output_chunk.seek(0)
                    chunks.append(output_chunk.name)
                else:
                    break

        except KeyboardInterrupt: # If there is an interruption, delete all temporary files and raise the exception for further processing.
            print 'Removing temporary files...'
            self.remove_chunks(chunks)
            raise

        finally:
            input_file.close()
        
        if output is None:       
            output = "%s/tempsort%s_%s"%(tempdirs[0], os.getpid(), self.id)
        
        output_file = open(output,'wb',self.temp_file_size)
        
        try:
            output_file.writelines(self.merge(chunks,key))
        finally:
            self.remove_chunks(chunks)

        output_file.close()
        return open(output)

    def merge(self, chunks, key=None):
        if self.logger: self.logger.info("... Merging chunks...")
        if key is None:
            key = lambda x : x

        values = []
        for index, chunk in enumerate(chunks):
            try:
                chunk_file = open(chunk)
                iterator = iter(chunk_file)
                value = iterator.next()
            except StopIteration:
                self.remove_chunks(chunks)
                #try: chunks.remove(chunk) except: pass # igual hay algo magico aqui que se me ha pasado, pero creo que no vale para nada 
            else:
                heappush(values,((key(value), index, value, iterator, chunk_file)))

        while values:
            k, index, value, iterator, chunk = heappop(values)
            yield value
            try:
                value = iterator.next()
            except StopIteration:
                self.remove_chunks(chunks)
                #aqui tambien habia magia remove chunks
            else:
                heappush(values,(key(value),index,value,iterator,chunk))
 
class DualSortedReader:
    """Given two sorted files of tags in a format supported by Pyicoteo, iterates through them returning them in order"""
    def __init__(self, file_a_path, file_b_path, format, read_half_open=False, logger=None):
        self.logger = logger
        self.file_a = open(file_a_path)
        self.file_b = open(file_b_path)
        self.current_a = Cluster(cached=False, read=format, read_half_open=read_half_open, logger=self.logger)
        self.current_b = Cluster(cached=False, read=format, read_half_open=read_half_open, logger=self.logger)
        
    def __iter__(self):
        stop_a = True # indicates if the exception StopIteration is raised by file a (True) or file b (False)
        safe_reader = SafeReader(self.logger)
        try:
            while 1:
                if not self.current_a:
                    stop_a = True
                    line_a = self.file_a.next()
                    safe_reader.safe_read_line(self.current_a, line_a)
                
                if not self.current_b:
                    stop_a = False
                    line_b = self.file_b.next()
                    safe_reader.safe_read_line(self.current_b, line_b)
                
                if self.current_a < self.current_b:
                    self.current_a.clear()
                    yield line_a
                else:
                    self.current_b.clear()
                    yield line_b
        except StopIteration: # we still need to print the reminder of the sorter file
            if stop_a:
                while self.file_b:
                    yield line_b
                    line_b = self.file_b.next()
            else:
                while self.file_a:
                    yield line_a
                    line_a = self.file_a.next()


class SortedFileReader:
    """
    Holds a cursor and a file path. Given a start and an end, it iterates through the file starting on the cursor position,
    and yields the clusters that overlap with the region specified. The cursor will be left behind the position of the last region fed 
    to the SortedFileReader.
    """
    def __init__(self, file_path, experiment_format, read_half_open=False, cached=True, rounding=True, logger=None):
        self.__dict__.update(locals())
        self.file_iterator = open(file_path, 'rb')
        self.safe_reader = SafeReader()
        self.cursor = self.file_iterator.tell()
        self.initial_tell =  self.file_iterator.tell() 
    
    def restart(self):
        """Start again reading the file from the start"""
        self.logger.debug("Rewinding to %s"%self.cursor)
        self.file_iterator.seek(self.initial_tell)
        self.cursor = self.initial_tell

    def rewind(self):
        """Move back to cursor position"""
        #print "REWIND"
        self.file_iterator.seek(self.cursor)
    
    def _read_line(self):
        """Reads the next line of the file. If advance, the cursor will get the position of the file"""
        self.line = self.file_iterator.readline()
        if self.line == '':
            return True

        return False

    def _advance(self):
        #print "ADVANCE"
        self.cursor = self.file_iterator.tell()

    def _get_cluster(self):
        """Returns line in a cluster ignoring the count of lines. Assumes that the cursor position exists"""
        c = Cluster(read=self.experiment_format, read_half_open=self.read_half_open, rounding=self.rounding, cached=self.cached, logger=self.logger) 
        if self.line:
            self.safe_read_line(c, self.line)
        return c

    def overlapping_clusters(self, region, overlap=1):
        if self.logger: self.logger.debug("YIELD OVER: Started...")
        self.rewind()
        if self._read_line(): return
        cached_cluster = self._get_cluster()
        while (cached_cluster.name < region.name) or (cached_cluster.name == region.name and region.start > cached_cluster.end) or cached_cluster.is_empty():
            #print "old:",cached_cluster.name, cached_cluster.start, cached_cluster.end
            self._advance()
            if self._read_line(): return
            cached_cluster = self._get_cluster()
            #print "NEW:", cached_cluster.name, cached_cluster.start, cached_cluster.end
 
        # get intersections
        while cached_cluster.start <= region.end and cached_cluster.name == region.name:
            if cached_cluster.overlap(region) >= overlap:
                if not region.strand or region.strand == cached_cluster.strand: # don't include the clusters with different strand from the region  
                    yield cached_cluster

            if self._read_line(): return
            cached_cluster = self._get_cluster()
 
    def safe_read_line(self, cluster, line):
        self.safe_reader.safe_read_line(cluster, line)

class SortedFileCountReader:
    """
    Holds a cursor and a file path. Given a start and an end, it iterates through the file starting on the cursor position,
    and retrieves the *counts* (number of reads) that overlap with the region specified. Because this class doesn't store the reads, but only counts them, 
    it doesn't have memory problems when encountering huge clusters of reads.  
    """
    def __init__(self, file_path, experiment_format, read_half_open=False, rounding=True, cached=True, logger=None):
        self.__dict__.update(locals())
        self.file_iterator = open_file(file_path, format=experiment_format, logger=logger)
        if logger:
            self.logger.debug('Fetcher used for %s: Sequential Sorted Counts Reader'%file_path)
        self.safe_reader = SafeReader(logger=logger)
        self.__initvalues()        
    
    def rewind(self):
        """Start again reading the file from the start"""
        self.file_iterator.seek(0)
        self.__initvalues()
        
    def __initvalues(self):
        self.slow_seek = 0
        self.current_tag = Cluster()

    def _read_next_tag(self):
        """Loads the cache if the line read by the cursor is not there yet. If the line is empty, it means that the end of file was reached,
        so this function sends a signal for the parent function to halt. If the region is stranded, the only tags returned will be the ones of that strand"""
        try:
            line = self.file_iterator.readline()
        except StopIteration:
            return True

        if line == '':
            return True

        self.current_tag = Cluster(read=self.experiment_format, read_half_open=self.read_half_open, rounding=self.rounding, cached=False, logger=self.logger)
        self.safe_read_line(self.current_tag, line)        
        return False

    def get_overlaping_counts(self, region, overlap=1):
        counts = 0
        # load last seek
        self.file_iterator.seek(self.slow_seek)
        self.current_tag = Cluster()
        # advance slow seek 
        while (self.current_tag.name < region.name) or (self.current_tag.name == region.name and region.start > self.current_tag.end):     
            self.slow_seek = self.file_iterator.tell()
            if self._read_next_tag():
                return counts  

        # get intersections
        while self.current_tag.start <= region.end and self.current_tag.name == region.name:
            if self.current_tag.overlap(region) >= overlap:
            
                if not region.strand or region.strand == self.current_tag.strand:
                    counts += 1

            if self._read_next_tag():
                return counts

        return counts

    def safe_read_line(self, cluster, line):
        self.safe_reader.safe_read_line(cluster, line)






