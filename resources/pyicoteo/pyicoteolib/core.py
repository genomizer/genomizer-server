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

import re
import sys
import random
import math
from collections import defaultdict
from defaults import *
import heapq
import string


debug = False


class Empty:
    def __init__(self):
        x = 1

##################################
#  EXCEPTIONS                    #
##################################
class InvalidLine(Exception):
    pass

class NotClusteredException(Exception):
    pass

class ConversionNotSupported(Exception):
    pass

class InsufficientData(Exception):
    pass

class DifferentChromosome(Exception):
    pass


class AbstractCore:
    """
    Do not cast, abstract class
    """
    def intersects(self, other):
        """Returns true if a Cluster/Region intersects with another Cluster/Region"""
        return ((other.start >= self.start and other.start <= self.end)
                 or (other.end >= self.start and other.end < self.end)
                 or (other.start <= self.start and other.end >= self.end)) and (self.name == other.name)

        
    def __cmp__(self, other):
        if self.name < other.name:
            return -1
        elif self.name > other.name:
            return 1
        elif self.start < other.start:
            return -1
        elif self.start > other.start:
            return 1
        elif self.end < other.end:
            return -1
        elif self.end > other.end:
            return 1
        else:
            return 0     

    def overlap(self, other):
        """Returns the percentage of overlap of the self cluster with another cluster, from 0 to 1"""
        if self.name != other.name or not self.intersects(other): #different name or no intersection, no overlap
            return 0
        if other.start > self.start and other.end >= self.end:
            #|--------------------| self
            #           |--------------------| other
            #OR
            #|--------------------| self
            #           |---------| other
            return float(self.end-other.start+1)/len(self)

        elif other.end < self.end and other.start <= self.start:
            #      |-------------------| self
            #|---------------| other
            #OR
            #|---------------------| self
            #|---------------| other
            return float(other.end-self.start+1)/len(self)
        elif self.start < other.start and self.end > other.end:
            #|------------------------| self
            #    |------------| other
            return float(len(other))/len(self)
        else:
            #    |------------| self
            #|------------------------| other
            #OR
            #|------------------------| self
            #|------------------------| other
            #OR
            return 1

    def is_contiguous(self, other):
        """Returns true if a Cluster read is contiguous to another one. """
        return (other.start == self.end+1 or other.end+1 == self.start) and self.name == other.name

    

###################################
#   READERS                       #
###################################
class ReaderFactory:
    def create_reader(self, format, half_open=False, cached=True):
        if format == BED or format == BED12:
            return BedReader(format, half_open, cached)
        elif format == PK:
            return PkReader(format, half_open, cached)
        elif format == WIG:
            return WigReader(format, half_open, cached)
        elif format == ELAND:
            return ElandReader(format, half_open, cached)
        elif format == SAM or format == BAM:
            return SamReader(format, half_open, cached)
        elif format == COUNTS:
            return None
        elif format == CUSTOM_FORMAT:
            return CustomReader(format, half_open, cached)

        else:
            raise ConversionNotSupported

class Reader:

    def __init__(self, format, half_open, cached):
        if half_open:
            self.correction = 1
        else:
            self.correction = 0

        self.format = format
        self.half_open = half_open
        self.cached = cached


    def read_line(self):
        raise NotImplementedError("You're using the abstract base class 'Reader', use a specific class instead")

    def _add_line_to_cluster(self, line, cluster):
        cluster_aux = Cluster()
        cluster_aux.reader = cluster.reader
        cluster_aux.normalize_factor = cluster.normalize_factor
        cluster_aux.read_line(line)
        result = cluster + cluster_aux
        cluster.start = result.start
        cluster.end = result.end
        cluster._levels = result._levels
        #if the strands are different, the cluster becomes strandless
        if cluster.strand != cluster_aux.strand:
            cluster.strand == NO_STRAND


    def quality_filter(self, line):
        """checks if the line passes the quality conditions"""
        return True


class CustomReader(Reader):
    def read_line(self, cluster, line):
        line = re.split(CustomReader.custom_in_sep, line.strip())
        try:
            fields = [int(x) for x in CustomReader.f_custom_in]
        except ValueError as err:
            print "CustomReader error: all f-custom-in values must be integers!"
            sys.exit(1) # FIXME: different action?
        try:
            seqname = line[fields[0]]
            start = int(line[fields[1]])
            end = int(line[fields[2]])
            if len(fields) > 3:
                strand = line[fields[3]]
            else:
                strand = '.'
        except ValueError as err:
            if 'start' not in locals():
                print "CustomReader error: incorrect field type: start"
            elif 'end' not in locals():
                print "CustomReader error: incorrect field type: end"
            sys.exit(1) # FIXME: different action?

        if cluster.is_empty():
            cluster.add_tag_cached(start, end)
            cluster.start = start
            cluster.end = end
            cluster.name = seqname
            cluster.name2 = str(start) + ":" + str(end)
            cluster.strand = strand
        else:
            cluster.add_tag_cached(start, end)
            cluster.start = min(cluster.start, start)
            cluster.end = max(cluster.end, end)
            cluster.name2 = str(cluster.start) + ":" + str(cluster.end)
        cluster.sequence = ''
        cluster.tag_length = len(cluster)


class BedReader(Reader):

    def _add_extra_fields(self, cluster, line):
        cluster.extra_fields = []
        for field in range(6, len(line)):
            cluster.extra_fields.append(line[field])

    def _add_name(self, cluster, line):
        if len(line) > 3:
            cluster.name2 = line[3]
        else:
            cluster.name2 = 'pyicos'

    def _add_score(self, cluster, line):
        if len(line) > 4:
            cluster.score = line[4] #this should be casted to int, but since we dont use it better that its not, lot of people mix this label and the 'name' one
        else:
            cluster.score = 300

    def _add_strand(self, cluster, line):
        if len(line) > 5:
            cluster.strand = line[5]
        else:
            cluster.strand = '.'

    def read_line(self, cluster, line):
        cluster.read_count += 1
        try:
            if self.cached:
                line = line.split()
                new_start = int(line[1])+self.correction
                cluster.name = line[0]
                if cluster.is_empty():
                    cluster.add_tag_cached(new_start, line[2])
                    cluster.start = new_start
                    cluster.end = int(line[2])
                    cluster.tag_length = len(cluster) #if the cluster is empty, its the perfect moment to read the tag length of the first read. We assume that all reads are the same, inside the cluster, so in the case that they vary (not that common), the randomness of choosing this should cancel the bias.  
                    self._add_strand(cluster, line) 
                else:
                    cluster.add_tag_cached(new_start, line[2])
                    cluster.start = min(cluster.start, new_start)
                    cluster.end = max(cluster.end, int(line[2]))
                    if len(line) > 5:
                        if line[5] != cluster.strand:
                            cluster.strand = '.'
                    else:
                        cluster.strand = '.'
                
                self._add_name(cluster, line)
                self._add_score(cluster, line)
            else:
                if not cluster.is_empty():
                    self._add_line_to_cluster(line, cluster)
                else:
                    line = line.split()
                    cluster.name = line[0]
                    cluster.start = int(line[1])+self.correction
                    cluster.end = max(cluster.end, int(line[2]))
                    self._add_name(cluster, line)
                    self._add_score(cluster, line)
                    self._add_strand(cluster, line)
                    cluster.tag_length = len(cluster)
                    cluster.append_level(cluster.end-cluster.start+1, cluster.normalize_factor)

            self._add_extra_fields(cluster, line)
        except (ValueError, IndexError):
            raise InvalidLine

class SamReader(Reader):

    def _get_strand(self):
        strand = "+"
        if (self.sam_flag & (0x10)):	# minus strand if true.
            strand = "-"
        return strand

    def read_line(self, cluster, line):
        try:
            line = line.split()
            self.sam_flag = int(line[1])
            if (not (self.sam_flag & 0x0004)):
                new_start = int(line[3])+self.correction
                new_end = new_start+len(line[9])-1
                cluster.name = line[2]
                new_strand = self._get_strand()
                if cluster.is_empty():
                    cluster.add_tag_cached(new_start, new_end)
                    cluster.start = new_start
                    cluster.end = new_end
                    cluster.name2 = line[0]
                    cluster.sequence = line[9]
                    cluster.strand = new_strand
                    cluster.tag_length = len(cluster)
                    
                else:
                    cluster.add_tag_cached(new_start, new_end)
                    cluster.start = min(cluster.start, new_start)
                    cluster.end = max(cluster.end, new_end)
                    cluster.sequence = ''
                    cluster.name2 = ''
                    if cluster.strand != new_strand:
                        cluster.strand == NO_STRAND

        except (ValueError, IndexError, AttributeError):
            raise InvalidLine

    def quality_filter(self, line):
        sline = line.split()
        try:
            mapped = (int(sline[1]))
        except:
            return False

        return not (mapped & 0x0004)


class WigReader(Reader):
    def read_line(self, cluster, line):
        cluster.read_count = None
        try:
            line = line.split()
            if cluster.is_empty():
                cluster.name = line[0]
                cluster.start = int(line[1])+self.correction

            cluster.append_level(int(line[2])-int(line[1])-self.correction+1, float(line[3])*cluster.normalize_factor)
            cluster._recalculate_end()
            #print "Buenas", cluster.name, cluster.start, cluster._levels
        except (ValueError, IndexError):
            raise InvalidLine

class ElandReader(Reader):
    """Convertor for the eland export format"""
    #eland_filter = re.compile(r'\w+.fsa')

    def read_line(self, cluster, line):     
        if line is not None and line != '\n':
            sline = line.split()
            if len(line) >= 7: #TODO check quality
                try:
                    if cluster.is_empty():
                        cluster.name2 = sline[0]
                        length = len(sline[1])
                        cluster.sequence = sline[1]
                        cluster.name = sline[6].split('.')[0]
                        cluster.start = int(sline[7])+self.correction
                        cluster.end = length+cluster.start-1
                        cluster.append_level(length+self.correction, cluster.normalize_factor)
                        cluster.tag_length = len(cluster)
                        if sline[8] is 'F':
                            cluster.strand = '+'
                        else:
                            cluster.strand = '-'
                    else:
                        self._add_line_to_cluster(line, cluster)

                    cluster.read_count += 1
                except (ValueError, IndexError):
                    raise InvalidLine

    #def quality_filter(self, line):
    #    return self.eland_filter.search(line)

class PkReader(Reader):
    def read_line(self, cluster, line):
        try:
            cluster.read_count = None
            if line is not None and line != '\n':
                if not cluster.is_empty():
                    self._add_line_to_cluster(line, cluster)
                else:
                    line = line.split()
                    if line[0] == 'track':
                        raise InvalidLine
                    cluster.name = line[0]
                    cluster.start = int(line[1])+self.correction
                    for item in line[3].split('|'):
                        temp = item.split(':')
                        cluster.append_level(int(temp[0]), float(temp[1])*cluster.normalize_factor) #normalizing in execution time

                    if len(line) > 5:
                        cluster.strand = line[5]
                    else:
                        cluster.strand = '.'

                    if len(line) > 8:
                        cluster.p_value = float(line[8])

                    cluster._recalculate_end()

        except (ValueError, IndexError):
            raise InvalidLine



##################################
#       WRITERS                  #
##################################
class WriterFactory:
    def create_writer(self, format, half_open, span=0):
        if format == ELAND:
            return ElandWriter(format, half_open, span)
        if format == BED or format == BED12:
            return BedWriter(format, half_open, span)
        elif format == WIG:
            return WigWriter(format, half_open, span)
        elif format == VARIABLE_WIG:
            return VariableWigWriter(format, half_open, span)
        elif format == PK:
            return PkWriter(format, half_open, span)
        elif format == SAM or format == BAM:
            return SamWriter(format, half_open, span)
        elif format == COUNTS:
            None

        elif format == CUSTOM_FORMAT:
            return CustomWriter(format, half_open, span)

        else:
            raise ConversionNotSupported

class Writer:
    def __init__(self, format, half_open, span=0):
        if half_open:
            self.correction = -1
        else:
            self.correction = 0
        self.half_open = half_open
        self.span = span
        self.format = format

    def write_line(self):
        raise NotImplementedError("You're using the abstract base class 'Writer', use a specific class instead")


class CustomWriter(Writer):
    def write_line(self, cluster):
        # ex. f-custom: 2 0 1 3 => chr1  start  end  strand
        if cluster.is_empty():
            return ''
        else:
            try:
                fields = [int(x) for x in CustomWriter.f_custom_out]
            except ValueError:
                print "CustomWriter error: all f-custom-out values must be integers!"

            field_list = [str(cluster.name), str(cluster.start), str(cluster.end), str(cluster.strand)]
            rseq = [None]*len(fields) # allocate array
            for (num, pos) in enumerate(fields):
                rseq[num] = field_list[pos]
 
            #if len(fields) > 3:
            #    strand_pos = fields[3]
            #else:
            #    strand_pos = len(fields)
            return string.join(rseq, CustomWriter.custom_out_sep.decode("string-escape")) + '\n' # string-escape: for tabs and other "special" characters


class ElandWriter(Writer):
    def write_line(self, cluster):
        if cluster.is_empty():
            return ''
        else:
            if cluster.strand is '-':
                cluster.strand = 'R'
            else:
                cluster.strand = 'F'
            if cluster.sequence is None:
                seq = 'A'*(cluster.end-cluster.start+1+self.correction)
            else:
                seq = cluster.sequence
            return '%s\t%s\tU0\t1\t0\t0\t%s.fa\t%s\t%s\t..\t26A\n'%(cluster.name2, seq, cluster.name, cluster.start+self.correction, cluster.strand)

class BedWriter(Writer):
    def write_line(self, cluster):
        bed_blueprint = '%s\t%s\t%s\t%s\t%s\t%s\n'

        if cluster.is_empty():
            return ''
        else:
            if not cluster.is_singleton():
                lines = ''
                split_tags = cluster.absolute_split(0)
                for tag in split_tags:
                    tags = cluster.decompose()
                    for tag in tags:
                        lines = '%s%s'%(lines, bed_blueprint%(tag.name, tag.start+self.correction, tag.end, tag.name2, tag.score, tag.strand))
                return lines
            else:
                if cluster.extra_fields:
                    fields = [cluster.name, str(cluster.start+self.correction), str(cluster.end), cluster.name2, str(cluster.score),  cluster.strand]
                    for extra in cluster.extra_fields:
                        fields.append(extra)

                    return "%s\n"%("\t".join(fields))
                else:    
                    return bed_blueprint%(cluster.name, cluster.start+self.correction, cluster.end, cluster.name2, cluster.score,  cluster.strand)

class SamWriter(Writer):
    def write_line(self, cluster):
        sam_blueprint = '%s\t%s\t%s\t%s\t255\t%sM\t*\t0\t0\t%s\t%s\n'
        if cluster.is_empty():
            return ''
        else:
            if cluster.strand == '-':
                samflag = 16
            else:
                samflag = 0
            if not cluster.sequence:
                cluster.sequence = (len(cluster)+self.correction)*'A'
            if not cluster.is_singleton():
                lines = ''
                split_tags = cluster.absolute_split(0)
                for tag in split_tags:
                    tags = cluster.decompose()
                    for tag in tags:
                        lines = '%s%s'%(lines, sam_blueprint%(tag.name2, samflag, tag.name, tag.start+self.correction, len(tag)+self.correction, tag.sequence, (len(tag)+self.correction)*'I'))
                return lines
            else:
                return sam_blueprint%(cluster.name2, samflag, cluster.name, cluster.start+self.correction, len(cluster)+self.correction, cluster.sequence, (len(cluster)+self.correction)*'I')


class WigWriter(Writer):
    def write_line(self, cluster):
        lines = ''
        start = cluster.start
        for length, height in cluster:
            end = start+length-1
            if cluster.rounding:
                height = round(height)

            if height > 0:
                if cluster.rounding:
                    lines = '%s%s\t%s\t%s\t%.0f\n'%(lines,cluster.name,start+self.correction, end, height)
                else:
                    lines = '%s%s\t%s\t%s\t%.2f\n'%(lines,cluster.name,start+self.correction, end, height)
            start = end+1
        return lines

class FixedWigWriter(Writer):
    def write_line(self, cluster):
        lines = ''
        for length, height in cluster:
            if cluster.rounding:
                height = int(round(height))
            if height > 0:
                new_lines = ('%.2f\n'%height)*length
                lines = '%s%s'%(lines, new_lines)
        return lines

class VariableWigWriter(Writer):
    def write_line(self, cluster):
        """With this format we lose precision, but it loads faster in UCSC"""
        lines = ''
        height_array = []
        start = cluster.start
        for length, height in cluster:
            for i in range(length):
                height_array.append(height)

            while len(height_array) > self.span:
                mean_height = sum(height_array[0:self.span])/self.span
                for i in range(0, self.span):
                    height_array.pop(0)
                new_line = '%s\t%s\n'%(start, mean_height)
                lines = '%s%s'%(lines, new_line)
                start += self.span

        return lines

class PkWriter(Writer):
    def _format_line(self, cluster, start, acum_length, profile, max_height, max_height_pos, area):
        format_str = '%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s'%(cluster.name, start+self.correction, start+acum_length-1, profile, max_height, cluster.strand, max_height_pos, area)
        if cluster.p_value != None: #it can be 0 and we want to print this, so DONT change this to "if cluster.p_value":
            format_str = '%s\t%s\n'%(format_str, cluster.p_value)
        else:
            format_str = '%s\n'%format_str
        return format_str


    def write_line(self, cluster):
        """prints a pk line out of the data in the object
        Also calculates the max height, max height position and area for all subclusters, replicating the functionality of max_height(), max_height_pos() and area()"""
        profile = ''
        lines = ''
        first = True
        acum_length = 0
        start = cluster.start
        max_height = -sys.maxint
        area = 0
        first_max_pos = 0
        last_max_pos = 0
        for length, height in cluster:
            if height > 0:   
                area += length*height           
                if first:
                    if cluster.rounding:
                        profile = '%s%s:%.0f'%(profile, length, height)
                    else:
                        profile = '%s%s:%.2f'%(profile, length, height)
                else:
                    if cluster.rounding:
                        profile = '%s|%s:%.0f'%(profile, length, height)
                    else:
                        profile = '%s|%s:%.2f'%(profile, length, height)

                acum_length += length
                if height > max_height:
                    max_height = height
                    first_max_pos = start + acum_length - length/2 - 1
                
                if height == max_height:
                    last_max_pos = start + acum_length - length/2 - 1

                first = False

            elif not first:
                lines = '%s%s'%(lines, self._format_line(cluster, start, acum_length, profile, max_height, (first_max_pos + last_max_pos)/2, area))
                start = start+acum_length+length
                acum_length = 0
                area = 0
                max_height = -sys.maxint
                first_max_pos = 0
                last_max_pos = 0
                first = True
                profile = ""

        if not first:
            lines = '%s%s'%(lines, self._format_line(cluster, start, acum_length, profile, max_height, (first_max_pos + last_max_pos)/2, area))

        return lines

######################################
#    CLUSTER OBJECT                  #
######################################

class ReadCluster(AbstractCore):
    """
    Represents one cluster of overlapping tags or a single Tag. This object can read and write in every format provided.
    It can be added, compared, subtracted to other cluster objects.
    """
    def __init__(self, name='', start=0, end=-1, strand='.', name2='noname', score=0, rounding = False,
                 read=PK, write=PK, read_half_open=False, write_half_open=False, normalize_factor=1., tag_length=0, sequence=None, span=20, cached=False, logger=None):
        """If you want the object to operate with integers instead
        of floats, set the rounding flag to True."""
        self._fast_init()
        self.logger = logger
        self.name = name
        self.start = int(start)
        self.end = int(end)
        self.name2 = name2
        self.restart_levels()
        length = self.end-self.start+1
        if length > 0:
            self.append_level(length, 1)

        self.strand = strand
        self.rounding = rounding
        self.normalize_factor = normalize_factor
        self.score = score
        self.cached = cached
        self.read_as(read, read_half_open, cached)
        self.write_as(write, write_half_open, span)
        self.tag_length = tag_length
        self.sequence = sequence

        self.strand = strand


    def _fast_init(self):
        """Stuff that all cluster instances should have. To generate and copy cluster objects faster"""
        self._tag_cache = []
        self.extra_fields = []       
        self.read_count = 0
        self.p_value = None


    def is_singleton(self):
        if self._tag_cache:
            self._flush_tag_cache()

        if self.is_empty():
            return False

        elif self.num_levels() > 1:
            return False

        elif self.get_level_height(0) > 1:
            return False


        else:
            return True

    def read_as(self, format, half_open=False, cached=False):
        f = ReaderFactory()
        self.reader = f.create_reader(format, half_open, cached)

    def write_as(self, format, half_open=False, span=0):
        f = WriterFactory()
        self.writer = f.create_writer(format, half_open, span)

    def clear(self):
        """Empties the cluster"""
        self.name = ''
        self.start = 0
        self.end = -1
        self.name2 = 'noname'
        del self._levels
        self.restart_levels()
        del self._tag_cache
        self._tag_cache = []
        self.strand = '.'
        self.score = 0
        self.read_count = 0

    def restart_levels(self):
        self._levels = []

    def __len__(self):
        return max(0, self.end-self.start+1)    
    
    def num_levels(self):
        return len(self._levels)

    def get_level_length(self, i):
        return self._levels[i][0]
 
    def get_level_height(self, i):
        return self._levels[i][1]

    def set_level_length(self, i, newval):
        self._levels[i][0] = newval

    def set_level_height(self, i, newval):
        self._levels[i][1] = newval

    def append_level(self, length, height):
        self._levels.append([int(length), float(height)])

    def delete_level(self, i):
        self._levels.pop(i)

    def add_level(self, i, length, height):
        self._levels.insert(i, [int(length), float(height)])


    def add_tag_cached(self, start, end):
        new_start = int(start)
        new_end = int(end)
        if not self._tag_cache:
            self._tag_cache.append([new_start, new_end, 1]) #first one
        else: 
            if new_start == self._tag_cache[-1][0] and new_end == self._tag_cache[-1][1]:
                self._tag_cache[-1][2] += 1   
                #print self._tag_cache[-1][2]
                #self._tag_cache.append([int(start), int(end), 1]) 
            else:        
                self._tag_cache.append([new_start, new_end, 1])

    def __eq__(self, other):
        #for cluster_a == cluster_b
        if  (self.name != other.name) or (self.start != other.start) or (self.strand != other.strand) or (self.end != other.end) or (self.name2 !=other.name2) or (self.num_levels() != other.num_levels()):
            return False
        for i in xrange (0, self.num_levels()):
                if self.get_level_length(i) != other.get_level_length(i) or self.get_level_height(i) != other.get_level_height(i):
                    return False
        return True

    def copy_cluster_data(self, other):
        """Copies the info in the other cluster, keeping the readers. Faster than copy_cluster"""
        other.start = self.start
        other.end = self.end
        other.restart_levels()
        other._tag_cache = []
        if self._tag_cache: 
            for tag in self._tag_cache:
                other._tag_cache.append(tag)
        else:
            for length, height in self:
                other.append_level(length, height)

        other.cached = self.cached
        other.name = self.name 
        other.score = self.score
        other.name2 = self.name2
        other.strand = self.strand
        other.rounding = self.rounding
        other.normalize_factor = self.normalize_factor
        other.tag_length = self.tag_length
        other.sequence = self.sequence
        other.read_count = self.read_count
        other.logger = self.logger
        other.p_value = self.p_value
        return other


    def copy_cluster(self):
        """Returns a copy of the self cluster. Faster than the standard python command copy.deepcopy()"""
        ret_cluster = Empty()
        ret_cluster.__class__ = self.__class__
        ret_cluster.start = self.start
        ret_cluster.end = self.end
        ret_cluster.restart_levels()
        ret_cluster._fast_init()

        if self._tag_cache: 
            for tag in self._tag_cache:
                ret_cluster._tag_cache.append(tag)
        else:
            for length, height in self:
                ret_cluster.append_level(length, height)

        ret_cluster.cached = self.cached
        ret_cluster.name = self.name
        ret_cluster.read_as(self.reader.format, self.reader.half_open, self.cached)
        ret_cluster.write_as(self.writer.format, self.writer.half_open, self.writer.span)

        ret_cluster.score = self.score
        ret_cluster.name2 = self.name2
        ret_cluster.strand = self.strand
        ret_cluster.rounding = self.rounding
        ret_cluster.normalize_factor = self.normalize_factor
        ret_cluster.tag_length = self.tag_length
        ret_cluster.sequence = self.sequence
        ret_cluster.read_count = self.read_count
        ret_cluster.logger = self.logger
        ret_cluster.p_value = self.p_value
        return ret_cluster

    #TODO raise 'there is no strand' error
    #TODO here I assume that the reads are shorter than the extension number.
    def extend(self, extension):
        if extension > 0:
            try:
                self._flush_tag_cache()
                if self.num_levels() > 1:
                    self.logger.error('Extension of clustered reads is not supported.')
                    raise InvalidLine

                self.sequence = None #deleting the sequence because the extension makes the sequence invalid (and incidentally screws up the counting of the length in the SAM format too)
                if self.strand is MINUS_STRAND:
                    previous_start = self.start
                    self.start = self.end - extension + 1
                    self.set_level_length(0, self.get_level_length(0) + (previous_start - self.start))

                    if self.start < 1:
                        if self.logger: self.logger.debug('Extending invalidates the read (Fell into negative). Discarding %s %s %s'%(self.name, self.start, self.end))
                        self.clear()
        
                else:
                    previous_end = self.end
                    self.end = self.start + extension - 1
                    self.set_level_length(-1, self.get_level_length(-1) + self.end - previous_end)

            except IndexError:
                #print 'Estoy atascado', self.start, self.end, self._levels
                pass

    def push(self, push_distance):
        if push_distance > 0:
            try:
                self._flush_tag_cache()
                if self.num_levels() > 1:
                    self.logger.error('Operation Push is only available for tag like entries. Convert your files to a "tag" format (bed or sam).')
                    raise InvalidLine

                self.sequence = None #deleting the sequence because the push_distance makes the sequence invalid (and incidentally screws up the counting of the length in the SAM format too)
                if self.strand is MINUS_STRAND:
                    self.start -= push_distance
                    self.end -= push_distance

                    if self.start < 1:
                        if self.logger: self.logger.debug('Pushing invalidates the read. (Fell into negative position) Discarding %s %s %s'%(self.name, self.start, self.end))
                        self.clear()
        
                else:
                    self.start += push_distance
                    self.end += push_distance

            except IndexError:
                #print 'Estoy atascado', self.start, self.end, self._levels
                pass

    def _subtrim(self, threshold, end, left):
        while self.num_levels() > 0:
            if self.get_level_height(end) < threshold:
                if left:
                    self.start += self.get_level_length(end)
                else:
                    self.end -= self.get_level_length(end)
                self.delete_level(end)
            else:
                break
    
    def trim(self, ratio=0.3, absolute=0):
        """Trims the cluster to a given threshold"""
        if absolute > 0:
            threshold = absolute
        else:
            threshold = int(round(self.max_height()*ratio))
        #print "THRESHOLD:", threshold
        self._subtrim(threshold, 0, True) #trim the left side of the cluster
        self._subtrim(threshold, -1, False) #trim the right side of the cluster

    def split(self, ratio=0.9, absolute=0):
        """
        Scans each cluster position from start to end and looks for local maxima x and local minima y.
        Given two consecutive local maxima x_{i} and x_{i+1} we define the smallest of them as x_{min}.
        For every y_{j} between two local maxima, the condition for splitting the cluster at y is defined as follows:

        y_{j}\leq x_{min}(1-t)

        Where t is a proportion threshold between 0 and 1. By default t=0.05. The cluster will divide at the local minimum. 
        """
        prev_height = -sys.maxint
        prev_local_maxima = 0
        new_cluster = self.copy_cluster()
        new_cluster.clear()
        new_cluster.start = self.start
        new_cluster.name = self.name
        clusters = []
        split_points = []
        going_up = True
        minimum_height = sys.maxint
        # get the local maxima
        level_number = 1
        for length, height in self:
            if height < prev_height: # we are going down
                if going_up: # if we were going up previously, we found a local maximum
                    if prev_local_maxima:
                        if absolute:
                            local_threshold = absolute
                        else:
                            local_threshold = min(prev_height, prev_local_maxima)*(1-ratio)
                        if minimum_height < local_threshold: # split point found
                            split_points.append(minimum_pos)               

                    prev_local_maxima = prev_height
                going_up = False
            else:
                if not going_up: # if we were going down previously, we reached a local minimum
                    minimum_height = prev_height
                    minimum_pos = level_number-1
                going_up = True

            prev_height = height
            prev_length = length
            level_number+=1
    
        if going_up: # if the cluster ended going up, it ended in a local maximum that was not detected previously
            if absolute:
                local_threshold = absolute
            else:
                local_threshold = min(prev_height, prev_local_maxima)*(1-ratio)
            if minimum_height < local_threshold: # split point found
                split_points.append(minimum_pos)    

        nucleotides = self.start
        if split_points:
            level_number = 1
            for length, height in self:
                if level_number in split_points:
                    right_len = length/2
                    left_len = right_len
                    if length%2 == 0: # it's even
                        left_len-=1

                    if left_len:
                        new_cluster.append_level(left_len, height)

                    self.__subsplit(new_cluster, clusters, nucleotides, left_len+1)

                    if right_len:
                        new_cluster.append_level(right_len, height)
                else:
                    new_cluster.append_level(length, height) # add significant parts to the profile

                nucleotides+=length
                level_number+=1

            if not new_cluster.is_empty():
               self.__subsplit(new_cluster, clusters, nucleotides, length)

            return clusters
        else:
            return [self]

        return clusters

    def __subsplit(self, new_cluster, clusters, nucleotides, length):
        """sub method of split"""
        new_cluster.name=self.name
        new_cluster._recalculate_end()
        clusters.append(new_cluster.copy_cluster())
        new_cluster.clear()
        new_cluster.start=nucleotides+length

    def absolute_split(self, threshold):
        """Returns the original cluster or several clusters if we find subclusters"""
        new_cluster = self.copy_cluster()
        new_cluster.clear()
        new_cluster.start = self.start
        nucleotides = self.start
        clusters = []
        for length, height in self:
            if height<=threshold:
                if new_cluster.is_empty():
                    new_cluster.start=nucleotides+length # trim insignificant beginning of a cluster
                else:
                    self.__subsplit(new_cluster, clusters, nucleotides, length)
            else:
                new_cluster.append_level(length, height) # add significant parts to the profile
            nucleotides+=length

        if not new_cluster.is_empty():
           self.__subsplit(new_cluster, clusters, nucleotides, length)

        return clusters

    def is_artifact(self, condition = 0.3):
        """Returns True if the cluster is considered an artifact.
           It is considered an artifact if it's shorter than 100 nucleotides,
           or the maximum height length is more than 30% of the cluster (block cluster)"""
        self._flush_tag_cache()
        if len(self) < 100:
            return True
            
        h = self.max_height()
        max_len = 0.
        for length, height in self:
            if h == height:
                max_len = max(float(length), max_len)

        return max_len/len(self) > condition

    def is_significant(self, threshold, poisson_type="height", frag_size=1):
        """Returns True if the cluster is significant provided a threshold, otherwise False"""
        if poisson_type=="height":
            return threshold <= int(round(self.max_height()))
        else:
            return threshold <= int(round(self.area()/frag_size))

    def read_line(self, line):
        self.reader.read_line(self, line)

    def write_line(self):
        if self._tag_cache:
            self._flush_tag_cache()
        return self.writer.write_line(self)

    def set_tag_length(self, tag_length):
        self.tag_length = tag_length

    def decompose(self):
        """Returns a list of Singletons that form the cluster given a tag length. If the tag length is not provided the cluster was formed by tags with
        different lengths, it's impossible to reconstruct the tags due to ambiguity (multiple different combination of tags can be used to obtain the same 
        cluster)"""
        if self.tag_length is not None and self.tag_length > 0:
            tags = []
            cluster_copy = self.copy_cluster()
            dummy_cluster = self.copy_cluster()
            dummy_cluster.restart_levels()
            dummy_cluster.append_level(self.tag_length, 1)
            dummy_cluster.start = cluster_copy.start
            dummy_cluster.end = cluster_copy.start+self.tag_length-1
            while cluster_copy.max_height() > 0:
                cluster_copy -= dummy_cluster
                tags.append(dummy_cluster.copy_cluster())
                dummy_cluster.start = cluster_copy.start
                dummy_cluster.end = cluster_copy.start+self.tag_length-1

        else:
            raise InsufficientData

        return tags


    def __sub__(self, other):
        if self.logger: self.logger.debug("SUBTRACT: Flushing experiment...")
        if self._tag_cache:
            self._flush_tag_cache()
        if self.logger: self.logger.debug("SUBTRACT: Flushing control...")
        if other._tag_cache:
            other._flush_tag_cache()
        if self.logger: self.logger.debug("SUBTRACT: Copying...")
        result = self.copy_cluster()
        if self.logger: self.logger.debug("SUBTRACT: Subtracting... len1:%s len2:%s"%(len(self._levels), len(other._levels)))
        if result.name == other.name and self.num_levels() > 0:
            break_up = False
            other_acum_levels = 0
            other_count = 0
            self_slow_count = 0
            slow_start = result.start
            slow_end = result.start + result.get_level_length(0)
            other_numlevels = other.num_levels() # for speed
            while (other_numlevels > other_count):
                other_level_start = other.start + other_acum_levels
                other_acum_levels += other.get_level_length(other_count) # for speed
                other_level_end = other.start + other_acum_levels
                other_height = other.get_level_height(other_count) # for speed
                other_length = other.get_level_length(other_count) # for speed
                while other_level_start > slow_end:
                    slow_start += result.get_level_length(self_slow_count)
                    self_slow_count += 1
                    try:
                        slow_end += result.get_level_length(self_slow_count)
                    except IndexError: # this means we got to the end of the level list and that there is not more subtracting to do
                        break_up = True
                        break
               
                if break_up:
                    break

                self_count = self_slow_count
                level_start = slow_start
                level_end = slow_start
                while result.num_levels() > self_count and other_level_end >= level_start: 
                    level_end += result.get_level_length(self_count)
                    height = result.get_level_height(self_count)
                    if other_level_start <= level_start and other_level_end >= level_end:
                        #       |------------| self
                        #   |--------------------| other
                        result.set_level_height(self_count, result.get_level_height(self_count)-other_height)

                    elif other_level_start <= level_start and other_level_end < level_end and other_level_end > level_start:
                        #     |---------------------| self
                        #  |--------------------| other
                        result.set_level_length(self_count, other_level_end - level_start)
                        result.set_level_height(self_count, result.get_level_height(self_count)-other_height)
                        result.add_level(self_count+1, level_end - other_level_end, height)
                        level_end = other_level_end

                    elif other_level_start > level_start and other_level_start < level_end and other_level_end >= level_end:
                        # |-------------------| self
                        #         |-------------------| other
                        result.set_level_length(self_count, result.get_level_length(self_count)-(level_end-other_level_start))
                        result.add_level(self_count+1, level_end-other_level_start,result.get_level_height(self_count)-other_height)
                        self_count+=1

                    elif other_level_start > level_start and other_level_end < level_end:

                        # |-------------------| self
                        #         |-----| other
                        result.set_level_length(self_count, other_length)
                        result.set_level_height(self_count, result.get_level_height(self_count)-other_height)
                        result.add_level(self_count+1, level_end - other_level_end, height)
                        result.add_level(self_count, other_level_start - level_start, height)
                
                    level_start = level_end
                    self_count+=1

                other_count+=1

            if self.logger: self.logger.debug("SUBTRACT: Done Subtracting. Cleaning...")
            result._clean_levels()
        return result

    def __iter__(self):
        if self._tag_cache:
            self._flush_tag_cache()
        return self._levels.__iter__()


    def max_height(self):
        """Returns the maximum height in the cluster"""
        max_height = 0.
        for length, height in self:
            max_height = max(max_height, height)
        return max_height


    def max_height_pos(self):
        """
        Returns the position where the maximum height is located.
        The central positions of the first and the last maximum are calculated.
        The max_height_pos will be in the middle of these two positions.
        """
        max_height = 0
        acum_length = 0
        first_max = 0
        last_max = 0
        for length, height in self:
            acum_length += length
            if round(height) > round(max_height):
                max_height = height
                first_max = self.start + acum_length - length/2 - 1

            if round(height) == round(max_height):	
                last_max = self.start + acum_length - length/2 - 1

        pos = (first_max + last_max)/2
        return pos 
  

    def area(self):
        """
        Returns the area of the peak
        """
        sum_area = 0
        for length, height in self:
            sum_area += length*height

        return sum_area


    def __add__(self, other):
        """
        Adds the levels of 2 selfs, activating the + operator for this type of object results = self + self2
        """
        if other._tag_cache:
            # for start, end, dup in other._tag_cache: 
            self.add_tag_cached(other._tag_cache[0][0], other._tag_cache[0][1]) #TODO only works when other._tag_cache == 1. For performance. Change if needed.
            self.start = min(self.start, other.start)
            self.end = max(self.end, other.end)
            if self.strand != other.strand:
                self.strand = "."    
            return self
        else: 
            result = self.copy_cluster()
            if result.read_count and other.read_count:
                result.read_count += other.read_count # if both clusters have a read count, add it
            other_acum_levels = 0
            # add zeros so both selfs have equal length and every level is added
            if other.end > result.end:
                result.append_level(other.end-result.end, 0)

            if other.start < result.start:
                result._levels.insert(0, [result.start - other.start, 0])
                result.start = other.start

            for other_count in xrange(0, other.num_levels()):
                other_level_start = other.start + other_acum_levels
                other_acum_levels += other.get_level_length(other_count)
                other_level_end = other.start + other_acum_levels
                other_height = other.get_level_height(other_count)
                other_length = other.get_level_length(other_count)
                i = 0
                acum_levels = 0
                while (len(result._levels) > i):
                    level_start = result.start + acum_levels
                    acum_levels += result.get_level_length(i)
                    level_end = result.start + acum_levels
                    height = result.get_level_height(i)
                    if (other_level_start >= level_start and other_level_start < level_end)  or (other_level_end > level_start and other_level_end < level_end) or (other_level_start <= level_start and other_level_end >= level_end):
                        if other_level_start <= level_start and other_level_end >= level_end:
                            result.set_level_height(i, result.get_level_height(i) + other_height)

                        elif other_level_start <= level_start and other_level_end < level_end:
                            result.set_level_length(i, other_level_end - level_start)
                            result.set_level_height(i, result.get_level_height(i) + other_height)
                            result._levels.insert(i+1, [level_end - other_level_end, height])
                            level_end = other_level_end

                        elif other_level_start > level_start and other_level_start < level_end and other_level_end >= level_end:
                            result.set_level_length(i, result.get_level_length(i)-level_end+other_level_start)
                            result._levels.insert(i+1, [level_end-other_level_start, result.get_level_height(i)+other_height])
                            i+=1

                        elif other_level_start > level_start and other_level_end < level_end:
                            result.set_level_length(i, other_length)
                            result.set_level_height(i, result.get_level_height(i) + other_height)
                            result._levels.insert(i+1, [level_end - other_level_end, height])
                            result._levels.insert(i, [other_level_start - level_start, height])
                    i+=1

            result._clean_levels()
        return result

    def _get_smallest_end(self, array_ends):
        return heapq.nsmallest(1, array_ends)[0][0]

    def _pop_smallest_end(self, array_ends):
        self._numends -= 1
        if heapq.nsmallest(1, array_ends)[0][1] > 1:
            heapq.nsmallest(1, array_ends)[0][1] -= 1
            return heapq.nsmallest(1, array_ends)[0][0]
            
        return heapq.heappop(array_ends)[0]

    def _push_end(self, array_ends, end):
        found = False
        self._numends += 1
        for i in range(0, len(array_ends)): # We go backwards because it's a heapq. We will probably find the value in one of the last ones of the queue
            if end == array_ends[i][0]:
                found = True
                array_ends[i][1] += 1
                break

        if not found:
            heapq.heappush(array_ends, [end, 1])

    def _flush_tag_cache(self):
        """
        Joins all reads in levels. Assumes that the tags were sorted 
        """
        #TODO: Is there a FAST way of detecting if they are sorted, so we could sort them if necessary?
        array_ends = []
        self._numends = 0 # The number of ends is not equivalent to len(array_ends) since it's compressed
        previous_start = -1
        smallest_end = sys.maxint

        while self._tag_cache:
            current_start, current_end, numduplicates = self._tag_cache.pop(0) # delete from tag cache while we transfer to _levels
            for i in range(0, numduplicates):     
                if self.logger: 
                    if len(self._tag_cache) > 1000: 
                        self.logger.debug("FLUSH TAG CACHE: smallest_end length: %s levels length: %s tag cache length: %s duplicate number: %s"%(len(array_ends), self.num_levels(), len(self._tag_cache), i+1))
                while current_start > smallest_end and self._numends > 0:
                    self.append_level(smallest_end-previous_start+1, self._numends*self.normalize_factor)
                    previous_start = self._pop_smallest_end(array_ends)
                    try:
                        while previous_start == self._get_smallest_end(array_ends):
                            previous_start = self._pop_smallest_end(array_ends)
                    except IndexError:
                        pass # if array_ends is empty, go on
                    previous_start = previous_start + 1
                    if self._numends > 0:
                        smallest_end = self._get_smallest_end(array_ends)

                if previous_start != current_start and self._numends > 0:
                    self.append_level(current_start-previous_start, self._numends*self.normalize_factor)
                previous_start = current_start
                self._push_end(array_ends, current_end)
                smallest_end = self._get_smallest_end(array_ends)

        if self._numends > 0:
            previous_end = -1
            while self._numends > 0:
                if previous_end != smallest_end and self._numends > 0:
                    self.append_level(smallest_end-previous_start+1, self._numends*self.normalize_factor)
                previous_start = self._pop_smallest_end(array_ends)+1
                if self._numends > 0:
                    previous_end = smallest_end
                    smallest_end = self._get_smallest_end(array_ends)

        self._tag_cache = []
        self._clean_levels()
        

    def get_heights(self):
        """Returns all the heights in an array"""
        ret = []
        for length, height in self:
            for i in xrange(0, length):
                ret.append(height)

        return ret

    def __nonzero__(self):
        return not self.is_empty()

    def is_empty(self):
        """Returns True if the Cluster object is empty, returns False otherwise."""
        return self.num_levels() == 0 and len(self._tag_cache) == 0

    def _clean_levels(self):
        """
        Cleans the levels array joining the values that have the same height and discarding the values
        that have height < 0 at the initial positions.
        """
        previous_height = -1
        previous_length = 0
        i = 0
        if self.num_levels() > 0:
            while self.get_level_length(0) <= 0:
                self.delete_level(0)
                if self.is_empty():
                    break

        if self.num_levels() > 0:
            while self.get_level_height(0) <= 0: #delete the 0 to the left of the Cluster
                self.start += self.get_level_length(0)
                self.delete_level(0)
                if self.is_empty():
                    break
        

        if self.num_levels() > 0:
            while self.get_level_height(-1) <= 0: #delete the 0 to the right of the Cluster
                self.delete_level(self.num_levels()-1)
                if self.num_levels() == 0:
                    break

        while (self.num_levels() > i): #join all identical levels
            if self.get_level_height(i) == previous_height:
                self.set_level_length(i, self.get_level_length(i)+previous_length)
                previous_length = self.get_level_length(i)
                self.delete_level(i-1)

            else:
                previous_length = self.get_level_length(i)
                previous_height = self.get_level_height(i)
                i+=1


        self._recalculate_end()


    def touches(self, other):
        """Returns if the clusters are contiguous in any way (first one and then the other) or intersecting"""
        return (self.overlap(other) > 0 or self.is_contiguous(other) or other.is_contiguous(self))

    def _recalculate_end(self):
        """Recalculates the end value to avoid possible inconsistency in the data"""
        self.end = self.start-1
        for length, height in self:
            self.end += length

    def has_duplicates(self, limit=20):
        """Returns true if the cluster has any ocurrence that exceeds the number of duplicated reads
        specified in the limit variable"""
        if limit < 1:
            return False
        else:
            previous_height = 0
            for length, height in self:
                if height > previous_height + limit:
                    return True
                previous_height = height
            return False

    def __str__(self):
        return "<Cluster object> name: %s start: %s end: %s name2: %s "%(self.name, self.start, self.end, self.name2)


# For backwards compatibility, also export ReadCluster as Cluster
Cluster = ReadCluster



#######################
#   REGION  OBJECT    #
#######################
class ReadRegion(AbstractCore):
    def __init__(self, name='undefined', start=0, end=-1, name2='noname', strand=None, logger=None, cached=True, exome_size=0):
        self.start = int(start)
        self.end = int(end)
        self.name = name
        self.name2 = name2
        self.strand = strand
        self.tags = []
        self.clusters = []
        self.exome_size = exome_size
        self.logger = logger
        self.cached = cached

    def __nonzero__(self):
        return (self.start is not 0 and self.end is not 0 and self.name is not None)

    def rpkm(self, total_reads, total_regions_analyzed=0, pseudocount=False):
        # DEPRECATED Doesn't take into consideration BED12 starts
        """Original definition: Reads per kilobase of exon model per million mapped reads. We generalize to: Reads per kilobase of region per million mapped reads. Added 1 pseudocount per region to avoid 0s"""
        if not pseudocount:
            total_regions_analyzed=0
            pseudo = 0
        else:
            pseudo = 1
        return (1e9*float(len(self.tags)+pseudo))/(len(self)*(total_regions_analyzed+total_reads))

    def normalized_counts(self, region_norm=False, total_n_norm=False, regions_analyzed=0, pseudocount=False, tmm_factor = 1, total_reads = 1, counts = None):
        if not counts:
            counts = float(len(self.tags))      

        if pseudocount:
            counts += 1
        else:
            regions_analyzed = 0 # because we add 1 pseudocount per region, so we have to increase N by num-regions. If pseudocounts are not used, there is no need for this count      
        if region_norm: # read per kilobase in region
            if self.exome_size:  # If an exome size is provided, use this to normalize the reads
                length = self.exome_size
            else:
                length = len(self)               

            counts = 1e3*(float(counts)/length)

        if total_n_norm: # per million reads in the sample
            counts = 1e6*(counts/(regions_analyzed+total_reads))
    
        return counts*tmm_factor


    def numtags(self, pseudocount=False):
        """Returns the number of reads in the region, gives the possibility of returning +1 reads if pseudocounts are being used"""
        if pseudocount:
            return len(self.tags)+1
        else:
            return len(self.tags)


    def __sub_swap(self, region, swap1, swap2, ratio): # ratio of reads in file A per read in file B
        for tag in region.tags:
            if random.uniform(0,2)*ratio > 1:
                swap1.add_tags(tag)
            else:
                swap2.add_tags(tag)


    def swap(self, region_b, ratio=1):
        """Given 2 regions, returns 2 new regions with the reads of both regions mixed randomly"""
        swap1 = Region(self.name, self.start, self.end)
        swap2 = Region(self.name, self.start, self.end)
        self.__sub_swap(self, swap1, swap2, ratio)
        self.__sub_swap(region_b, swap1, swap2, ratio)
        return (swap1, swap2)

    def __str__(self):
        return "chr: %s start: %s end: %s name: %s number_of_tags: %s"%(self.name, self.start, self.end, self.name2, len(self.tags))

    def _numpos_higher_than(self, h, nis):
        ret = 0
        for key, value in nis.items():
            if key >= h:
              ret += value
        return float(ret)

    def _get_probability(self, nis, h):
        return self._numpos_higher_than(h, nis)/self._numpos_higher_than(1, nis)

    def __len__(self):
        return max(0, self.end-self.start+1)

    def copy(self):
        """Copies a Region object into another one"""
        new_region = Empty()
        new_region.__class__ = self.__class__
        new_region.start = self.start
        new_region.end = self.end
        new_region.name = self.name
        new_region.name2 = self.name2
        new_region.logger = self.logger
        new_region.cached = self.cached
        new_region.tags = []
        for tag in self.tags:   
            new_region.tags.append(tag)

        new_region.clusters = []
        new_region.strand = self.strand

        for cluster in self.clusters:
            new_region.clusters.append(cluster)

        new_region.exome_size = self.exome_size

        return new_region

    def max_height(self):
        max_height = 0
        for cluster in self.clusters:
            max_height = max(max_height, cluster.max_height())
        return max_height

    def tag_len_average(self):
        ret = 0.
        for tag in self.tags:
            ret += len(tag)
        return ret/len(self.tags)            

    def binomial(self, n, p, k): 
        return (math.factorial(n)*p**k*(1-p)**(n-k))/(math.factorial(k)*math.factorial(n-k))

         
    def p0(self, l, N):
        return (N-l)/N


    def p1(self, l, N):
        return l/N

    
    def join(self, other):
        """Joins two regions. Works with a cluster object too (if flushed properly). If the regions don't overlap, the space in between will be included too."""
        self.start = min(self.start, other.start)
        self.end = max(self.end, other.end)
   

    def get_FDR_clusters(self, repeats=100, masker_tags=[]):
        if self.logger: self.logger.debug("get_FDR_clusters: Started. Getting max height...")
        max_height = int(self.max_height())
        # Get the N values and the probabilities for the real and for the simulated random instances
        if self.logger: self.logger.debug("get_FDR_clusters: Done with max height. Getting heights...")
        real_heights = self.get_heights()
        if self.logger: self.logger.debug("get_FDR_clusters: Done getting heights.")
        # we do the same calculation but shuffling the tags randomly. We do it as many times as the variable repeats asks for
        Pr_of_h = defaultdict(int)
        random_variances = defaultdict(int)
        random_region = Region(None, self.start, self.end, logger=self.logger, cached=self.cached)
        if self.logger: self.logger.debug("get_FDR_clusters: Adding tags to random...")
        random_region.add_tags(self.tags)
        if self.logger: self.logger.debug("get_FDR_clusters: Done adding")
        # Get the repeat regions that overlap with the region
        """
        masker_tags = repeat_reader.get_overlaping_clusters(region, overlap=0.000001) # get all overlapping masker tags
        masker_region = Region(None, region.start, region.end, region.name)
        masker_region.add_tags(masker_tags)
        """
        if self.logger: self.logger.debug("Calculating modfdr for %s tags in %s region"%(len(self.tags), self.end-self.start+1))
        for r in xrange(0, repeats):
            if self.logger: self.logger.debug("Suffling %s"%r)
            random_region.shuffle_tags(masker_tags)
            nis = random_region.get_heights()
            for h in xrange(1, max_height+1):
                if not random_variances[h]:
                    random_variances[h] = []
                prob = self._get_probability(nis, h)
                Pr_of_h[h] += prob
                random_variances[h].append(prob)

        # Get the mean and the variance for the random iterations and calculate the FDR
        found = False
        significant_height = sys.maxint
        p_values_per_height = defaultdict(int) # if it doesnt have the values, will return a 0

        """
        number_of_reads = len(self.tags)
        average_tag_length = self.tag_len_average()
        region_length = len(self)
         
        est_pr_of_h = defaultdict(int)
        acum = 1
        for h in xrange(1, max_height+1):
            est_pr_of_h[h] = acum
            acum -= self.binomial(number_of_reads, (average_tag_length/region_length), h)
        """

        for h in xrange(1, max_height+1):
            random_mean = Pr_of_h[h]/repeats
            #print "random_prob:", h, random_mean
            #print "Binomial:", h, est_pr_of_h[h]
            #print 

            random_variance = 0
            h_prob = self._get_probability(real_heights, h)
            for i in xrange(0, repeats):
                random_variance += (random_variances[h][i]-random_mean)**2
            random_variance = math.sqrt(random_variance/repeats)
            p_value = random_mean+random_variance/h_prob
            if p_value < 0.00000001: # A smaller p-value than 10e-8 is irrelevant. For speed.
                break
            p_values_per_height[h] = random_mean+random_variance/h_prob
        # add the p_values to the clusters and return them
        ret_clusters = self.get_clusters()
        for i in range(0 , len(ret_clusters)):
            ret_clusters[i].p_value = p_values_per_height[int(ret_clusters[i].max_height())]

        return ret_clusters

    def get_heights(self):
        """Get the number of nucleotides that have a certain height. Returns a defaultdict"""
        heights_dict = defaultdict(int)
        for cluster in self.clusters:
            cluster._flush_tag_cache()
            for length, height in cluster:
                heights_dict[int(height)] += length
        return heights_dict


    def shuffle_tags(self, masker_tags=[]):
        """Shuffle the present tags in the region randomly"""
        for tag in self.tags:
            repeat = True
            repeat_count = 0
            #length = self.end-self.start #self._recalculate_end() is too slow
            while (repeat):
                # Try to find a random spot that doesn't fall inside a repeat region.
                # If a repeat region is hit more than 2000 times, surrender and leave the tag where it last was randomized
                # (almost impossible with normal genomes, but it may be possible if a gene with more than
                # 99.9% of repeats regions is analyzed) 0.999**2000=0.13519992539749945  0.99**2000=1.8637566029922332e-09
                repeat = False
                tag.start = random.randint(self.start, self.end)
                tag._recalculate_end()
                for repeat_tag in masker_tags:
                    if tag.overlap(repeat_tag) > 0:
                        repeat = True
                        repeat_count+=1
                        break

                if repeat_count > 2000:
                    if self.logger: self.logger.warning("Couldn't find a suitable randomization spot after 2000 tries, surrendering")
                    break

        # recreate the clusters
        self.clusterize()

    def _sub_add_tag(self, tag):
        if not self.strand or tag.strand == self.strand:
            self.tags.append(tag)
        

    def add_tags(self, tags, clusterize=False):
        """This method reads a list of tags or a single tag (ReadCluster objects, not unprocessed lines). If strand is set, then only the tags with the selected strand are added"""
        if self.logger: self.logger.debug("ADDING: Starting...")
        if type(tags) == list:
            if self.logger: self.logger.debug("ADDING: Checking strand...")
            for tag in tags:
                self._sub_add_tag(tag)
            if self.logger: self.logger.debug("ADDING: Done checking.")
        elif type(tags) == type(Cluster()):
            self._sub_add_tag(tags)
        else:
            if self.logger: self.logger.error('Invalid tag. Tags need to be either Cluster or List objects')

        if self.logger: self.logger.debug("ADDING: Clustering...")
        if clusterize:
            self.clusterize()
        if self.logger: self.logger.debug("ADDING: Done Clustering")


    def clusterize(self):
        """Creates the Cluster objects of the tags in the Region object"""
        self.clusters = []
        if self.logger: self.logger.debug("CLUSTERIZE: Sorting...")
        self.tags.sort(key=lambda x: (x.start, x.end))
        if self.logger: self.logger.debug("CLUSTERIZE: Sorting done. ")
        if self.logger: self.logger.debug("CLUSTERIZE: self.cached is %s"%self.cached)
        if self.tags:
            #Insert first cluster object
            self.clusters.append(Cluster(read=BED, cached=self.cached))
            
            if self.logger: self.logger.debug("CLUSTERIZE: len:%s ..."%len(self.tags))
            for i in xrange(0, len(self.tags)):
                if not self.tags[i].is_empty():
                    if not self.clusters[-1].touches(self.tags[i]) > 0 and not self.clusters[-1].is_empty():
                        self.clusters.append(Cluster(read=BED, cached=self.cached)) #create a new cluster object
                    try:
                        prev_format = self.tags[i].writer.format
                        self.tags[i].write_as(BED)
                        self.clusters[-1].read_line(self.tags[i].write_line())
                        self.tags[i].write_as(prev_format)

                    except InvalidLine:
                        if self.logger: self.logger.error("\t".join(["Error in clustering of Region Object:", self.tags[i].write_line(), self.tags[i].reader.format, self.tags[i].writer.format, self.clusters[-1].reader.format]))
                        raise


    def percentage_covered(self):
        """Returns the percentage of the region covered by tags"""
        covered = 0.
        total = len(self)
        for cluster in self.clusters:
            for length, height in cluster:
                covered+=length
        if total > 0:
            return covered/total
        else:
            return 0
  

    def get_metacluster(self):
        """Returns a Cluster object that contains the levels of all previous clusters combined, with gaps (zeros) between them"""
        if self.clusters:
            if self.logger: self.logger.debug("METACLUSTER: Get metacluster...")
            if self.clusters[0]._tag_cache:
                self.clusters[0]._flush_tag_cache()
            metacluster = self.clusters[0].copy_cluster()
            previous_end = metacluster.end
            for i in range(1, len(self.clusters)):
                if self.logger and len(metacluster)>1000: self.logger.debug("METACLUSTER: Length %s"%(len(metacluster)))
                self.clusters[i]._flush_tag_cache() # Need the end in its proper place
                if self.clusters[i].start > previous_end: # add zeros between levels
                    metacluster.append_level(self.clusters[i].start-previous_end-1, 0)
            
                for length, height in self.clusters[i]:
                    metacluster.append_level(length, height)
                previous_end = self.clusters[i].end

            metacluster._recalculate_end()
            if self.logger: self.logger.debug("METACLUSTER: Done.")
            return metacluster
        else:
            return Cluster()
        

    def get_clusters(self, height=1):
        """Gets the clusters inside the region higher than the marked height. By default returns all clusters with at least 2 reads"""
        significant_clusters = []
        for cluster in self.clusters:
            if cluster.max_height() > height:
                significant_clusters.append(cluster)
        return significant_clusters

    def write(self):
        """Returns a line in bed format"""
        strand = self.strand
        if not strand:
            strand = '.'
        
        return "%s\t%s\t%s\t%s\t0\t%s\n"%(self.name, self.start, self.end, self.name2, strand)


    def get_array(self):
        """Returns the heights of the whole region, including 0s between clusters"""
        ret = []
        prev_end = self.start # with this we get the 0s between the start of the region and the first cluster
        for cluster in self.clusters:
            cluster._flush_tag_cache()
            # get the 0s between clusters
            for i in range(prev_end, cluster.start):
                ret.append(0)
            ret.extend(cluster.get_heights())
            prev_end = cluster.end+1
            
        # the 0s after the last cluster
        for i in range(prev_end, self.end+1):
            ret.append(0)        

        return ret


# For backwards compatibility, also export ReadRegion as Region
Region = ReadRegion

