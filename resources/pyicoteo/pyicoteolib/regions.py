from core import ReadCluster, ReadRegion, InvalidLine
from utils import BigSort, get_logger
from defaults import *
import re
import os
import sys


class RegionException(Exception):
    pass

class AnnotationRegion(object):
    """
    Abstract class to represent an annotated region.
    Contains all basic attributes and methods common to all regions.
    """
    def __init__(self, region_id, parent_id, seqname, source, feature, start, end, score, strand, frame, attrs):
        self.region_id = region_id
        self.parent_id = parent_id
        self.seqname = seqname
        self.source = source
        self.feature = feature
        self.start = int(start)
        self.end = int(end)
        self.score = score
        self.strand = strand
        self.frame = frame
        self.attrs = attrs
        self.children = []
        self.parent = None

    def set_parent(self, parent):
        self.parent = parent

    def get_parent(self):
        return self.parent

    def append_child(self, child):
        self.children.append(child)

    def add_child(self, child): # unused in the new version because they are already sorted, TODO: remove?
        if child.start < self.start or child.end > self.end:
            raise RegionException('Child out of parent\'s bounds')

        pos = len(self.children)
        if pos > 0:
            for reg in reversed(self.children):
                if reg.start > child.start:
                    pos -= 1
        self.children.insert(pos, child)

    def get_child(self, child_id):
        for child in self.children:
            if child.region_id == child_id:
                return child
        return None

    def get_children(self):
        return self.children

    @staticmethod
    def find_region_in_list(region_id, region_list, strand=None):
        """ 
        Static method. Finds the region with id 'region_id' in the list 'region_list'.
        Can also search by strand.
        """
        try:
            for reg in reversed(region_list):
                #print "find_region_in_list: %s" % (region_id)
                if reg.region_id == region_id:
                    if (strand is not None and reg.strand == strand) or (strand is None):
                        return reg
        except Exception:
            pass
        return None

    # for testing purposes
    def print_region(self, level = 0):
        print ' '*4*level + self.__str__()
        for child in self.children:
            child.print_region(level + 1)

    # custom representation as string of the region, for testing purposes
    def __str__(self):
        return "%s\tSeq: %s\tID: %s\tParent: %s\tStart: %d\tEnd: %d\tStrand: %s" % (self.__class__.__name__, self.seqname, self.region_id, self.parent_id, self.start, self.end, self.strand)

    def __eq__(self, other): # TODO: compare children and parent?
        return (self.region_id == other.region_id
                and self.parent_id == other.parent_id
                and self.seqname == other.seqname
                and self.source == other.source
                and self.feature == other.feature
                and self.start == other.start
                and self.end == other.end
                and self.score == other.score
                and self.strand == other.strand
                and self.frame == other.frame
                and self.attrs == other.attrs  # compare attrs too (?)
                )

    def to_read_region(self):
        reg = Region(name=self.seqname, start=self.start, end=self.end, strand=self.strand, name2=self.region_id)
        return reg



class AnnotationGene(AnnotationRegion):
    pass


class AnnotationTranscript(AnnotationRegion):
    pass


class AnnotationExon(AnnotationRegion):
    pass



GENE = 'gene'
TRANSCRIPT = 'transcript'
EXON = 'exon'


class LineWarning(Exception):
    pass

# Fields: <seqname> <source> <feature> <start> <end> <score> <strand> <frame> [attributes] [comments]
def parse_gff_line(line):
    fields = line.rstrip().split('\t')
    if len(fields) < GFF_MANDATORY_FIELDS:
        raise InvalidLine('Missing some fields')

    (seqname, source, feature, start, end, score, strand, frame) = fields[0:8]

    if len(seqname) == 0:
        raise InvalidLine('Bad seqname')
    if len(source) == 0:
        raise InvalidLine('Bad source')
    if feature not in [GENE, TRANSCRIPT, EXON]:
        raise LineWarning('Feature %s not accepted' % (feature))
    try:
        start = int(start)
    except ValueError:
        raise InvalidLine('Start is not an integer')
    try:
        end = int(end)
    except ValueError:
        raise InvalidLine('End is not an integer')
    if start > end:
        raise InvalidLine('Start > End')
    #if score != '.':
    #    try:
    #        score = float(score)
    #    except ValueError:
    #        raise InvalidLine('Score is not a float')
    if strand not in ['+', '-', '.']:
        raise InvalidLine('Bad strand')
    #if frame not in ['0', '1', '2', '.']:
    #    raise InvalidLine('Bad frame')

    return fields



def parse_attributes(attributes):
    attrs = re.split(';\s*', attributes)
    attr_dict = {'gene_id': '', 'transcript_id': ''} # default values: empty string (workaround for files without attributes field)
    for attr in attrs:
        att = re.split('\s+', attr, maxsplit=1)
        if len(att) == 2:
            attr_dict[att[0]] = att[1].strip('"')
    return attr_dict


def feature_cmp(s):
    if s == GENE:
        return 0
    elif s == TRANSCRIPT:
        return 1
    elif s == EXON:
        return 2
    else:
        return sys.maxint


# attr_filter parameter: ignore all the lines that contain any attribute matching any {"key": "value"} in the dictionary (default = {}: process all lines)
def read_gff_file(gff_path, transcript_type=["protein_coding"], attr_checks=None, attr_filter={}, no_sort=False, do_filter=False, logger=None):
    if no_sort:
        if logger: logger.warning('GTF file sort skipped. Results might be wrong.')
        sorted_file = open(gff_path, 'r')
    else:
        if logger: logger.info('Sorting GTF file...')
        output_path = None
        sorter = BigSort(file_format=None, id='mergeexonid', logger=False, filter_chunks=False)
        sorted_file = sorter.sort(gff_path,
                                  output_path,
                                  lambda x:(x.split()[0], int(x.split()[3]), -int(x.split()[4]), feature_cmp(x.split()[2])),
                                  tempdirs=['/tmp']) 
        # sorted by seqname, start, -end, feature
        if logger: logger.info('GTF file sorted')

    current_genes = []
    #current_genes = deque() # use queue instead of list?
    for num, line in enumerate(sorted_file, 1):
        if line[0] == '#': # skip comment lines
            continue

        if num % 100000 == 0:
            if logger: logger.info('Lines read: ' + str(num))
            
        try:
            parsed_line = parse_gff_line(line)
        except LineWarning as e:
            #if logger: logger.info("Warning. Line %s: %s" % (num, e))
            continue
        except InvalidLine as e:
            if logger: logger.error("Invalid line %s: %s" % (num, e))
            continue
        except Exception as e:
            if logger: logger.error("Unexpected error. Line %s: %s" % (num, e))
            continue

        if len(parsed_line) < GFF_MANDATORY_FIELDS + 1:
            parsed_line.append("") # add fake "attributes" field if it doesn't exist

        (seqname, source, feature, start, end, score, strand, frame, attrs) = parsed_line
        parsed_attrs = parse_attributes(attrs)


        filter_line = False
        for filter_key in attr_filter.keys():
            if filter_key in parsed_attrs:
                if parsed_attrs[filter_key] == attr_filter[filter_key]:
                    filter_line = True # ignore lines that contain any of the attributes in attr_filter
                    break

        if "transcript_type" in parsed_attrs:
            if parsed_attrs["transcript_type"] not in transcript_type and do_filter:
               filter_line = True
#        if attr_checks is not None:
#            filter_line = attr_checks(parsed_attrs)

        if filter_line:
            continue

        if feature == GENE:
            gene_id = parsed_attrs['gene_id']
            gene = AnnotationGene(gene_id, None, seqname, source, feature, start, end, score, strand, frame, parsed_attrs)

            to_remove = []
            for (i, g) in enumerate(current_genes): # search for (complete) genes to process
                if (g.end < gene.start) or gene.seqname != g.seqname:
                    #process_gene(g)
                    yield g
                    to_remove.append(i)
                else: # stop returning genes (otherwise, the order is incorrect)
                    break
            for i in reversed(to_remove): # remove all processed genes
                current_genes.pop(i)

            #current_genes = current_genes[to_remove:]
            current_genes.append(gene)

        elif feature == TRANSCRIPT:
            gene_id = parsed_attrs['gene_id']
            transcript_id = parsed_attrs['transcript_id']
            transcript = AnnotationTranscript(transcript_id, gene_id, seqname, source, feature, start, end, score, strand, frame, parsed_attrs)
            parent = AnnotationRegion.find_region_in_list(transcript.parent_id, current_genes, transcript.strand)
            if parent is None:
                if logger: logger.warning(str(num) + ': Gene not found, skipping transcript')
                continue
            else:
                transcript.set_parent(parent)
                parent.append_child(transcript)

        elif feature == EXON:
            gene_id = parsed_attrs['gene_id']
            transcript_id = parsed_attrs['transcript_id']
            exon_id = gene_id + ':' + transcript_id + ':' + str(start) + ':' + str(end)
            exon = AnnotationExon(exon_id, transcript_id, seqname, source, feature, start, end, score, strand, frame, parsed_attrs)
            try:
                #parent = current_gene.get_children()[-1]
                #sys.stderr.write(exon_id + '\n')
                gene = AnnotationRegion.find_region_in_list(exon.attrs['gene_id'], current_genes, exon.strand)
                if gene is None:
                    if logger: logger.warning(str(num) + ': Gene ' + gene_id + ' not found, skipping exon')
                    continue
                parent = AnnotationRegion.find_region_in_list(exon.parent_id, gene.get_children(), exon.strand)
                if parent is None:
                    if logger: logger.warning(str(num) + ': Transcript ' + transcript_id + ' not found, skipping exon')
                    continue
                #if parent.attrs['gene_id'] != exon.attrs['gene_id']:
                #    raise Exception('Bad parent id: ' + parent.attrs['gene_id'] + '\t' + exon.attrs['gene_id'])
            except Exception as ex:
                if logger: logger.warning(str(num) + ": EXCEPTION: " + str(ex))
            exon.set_parent(parent)
            parent.append_child(exon)

    for gene in current_genes: # process last gene (repeated code)
        yield gene

    # delete temporary (sorted) file
    #print "SORTED_FILE: " + sorted_file.name
    os.remove(os.path.abspath(sorted_file.name))


def get_exons(gff_path, remove_duplicates=True, min_length=0, no_sort=False,  position=None, logger=None):
    tmp_genes = []
    #max_genes = 0

    #def chk(attrs):
    #    if "transcript_type" in attrs:
    #        if attrs["transcript_type"] not in ["protein_coding"]:
    #            return True
    #    return False

    for gene in read_gff_file(gff_path, no_sort=no_sort, logger=logger):#, attr_checks=chk):
        if len(tmp_genes) > 0:
            if (gene.start > tmp_genes[-1].end) or (tmp_genes[-1].seqname != gene.seqname): # new gene not overlapping
                if position is None: # return all exons
                    for ex in _get_exons_from_gene_list(tmp_genes, remove_duplicates):
                        if ex.end - ex.start >= min_length: # check if it's longer than min_length
                            yield ex
                else:
                    for g in tmp_genes:
                        if (position == 'first' and g.strand in ['+', '.']) or (position == 'last' and g.strand == '-'):
                            pos = 0
                        else:
                            pos = -1
                        exon = g.get_children()[pos].get_children()[pos] # first/last exon of first/last transcript
                        yield exon

                tmp_genes = [] # empty list
        tmp_genes.append(gene)
        tmp_genes.sort(key = lambda gn: (int(gn.end)))
    if position is None:
        for ex in _get_exons_from_gene_list(tmp_genes, remove_duplicates): # remaining exons
            if ex.end - ex.start >= min_length: # check if it's longer than min_length
                yield ex
    else:
        for g in tmp_genes:
            if (position == 'first' and g.strand in ['+', '.']) or (position == 'last' and g.strand == '-'):
                pos = 0
            else:
                pos = -1
            exon = g.get_children()[pos].get_children()[pos] # first/last exon of first/last transcript
            yield exon


def _get_exons_from_gene_list(tmp_genes, remove_duplicates=True):
    exons = []
    for g in tmp_genes:
        for tr in g.get_children():
            exons.extend(tr.get_children())
    exons.sort(key = lambda ex: (int(ex.start), int(ex.end)))

    # Remove duplicated exons (only checks e1.start == e2.start && e1.end == e2.end)
    # (Is there a better way to do it?)
    if remove_duplicates:
        to_remove = []
        for num, ex in enumerate(exons[1:]): # we start with "[1:]" because we compare with the previous exon in the list
            if ex.start == exons[num].start and ex.end == exons[num].end and ex.strand == exons[num].strand:
                to_remove.append(num+1) # join exon_ids?
        for n in reversed(to_remove):
            exons.pop(n)

    for ex in exons:
        yield ex


def get_introns(gff_path, min_length=0, no_sort=False, position=None, logger=None):
    tmp_genes = []
    for gene in read_gff_file(gff_path, no_sort=no_sort, logger=logger):
        if len(tmp_genes) > 0:
            if (gene.start > tmp_genes[-1].end) or (tmp_genes[-1].seqname != gene.seqname): # new gene not overlapping
                for intron in _get_introns_from_gene_list(tmp_genes, min_length, position):
                    yield intron
                tmp_genes = [] # empty list
        tmp_genes.append(gene)
        tmp_genes.sort(key = lambda gn: (int(gn.end)))

    for intron in _get_introns_from_gene_list(tmp_genes, min_length, position):
        yield intron


def _get_introns_from_gene_list(tmp_genes, min_length, position=None):
    if position is None: # return all introns
        exons = [exon for exon in _get_exons_from_gene_list(tmp_genes, remove_duplicates=True)]
        if len(exons) >= 2:
            last_exon = exons[0]
            for ex in exons[1:]:
                if ex.start > last_exon.end and ((ex.start - last_exon.end) >= min_length):
                    yield (ex.seqname, last_exon.end, ex.start, ex.region_id, ex.strand) # intron returned as tuple (seqname, start, end, id, strand)
                last_exon = ex
    else:
        for g in tmp_genes:
            g_exons  = [exon for exon in _get_exons_from_gene_list([g], remove_duplicates=True)]
            if len(g_exons) >= 2:
                if (position == 'first' and g.strand in ['+', '.']) or (position == 'last' and g.strand == '-'):
                    pos = slice(0, 2) # first and second exons
                else:
                    pos = slice(len(g_exons) - 2, len(g_exons)) # second-to-last and last exons
                ex1 = g_exons[pos][0]
                ex2 = g_exons[pos][1]
                yield (ex1.seqname, ex1.end, ex2.start, ex1.region_id, ex1.strand)


def get_tss(gff_path, add_start=0, add_end=0, no_sort=False, logger=None):
    tmp_genes = []
    for gene in read_gff_file(gff_path, no_sort=no_sort, do_filter=True, logger=logger):
        if len(tmp_genes) > 0:
            # Had some order problems in cases like:
            #       X-->|>------------X
            #   X------------<|<--X
            # (hopefully solved now)
            if (gene.start - max(add_start, add_end) > tmp_genes[-1].end + max(add_start, add_end)) or (tmp_genes[-1].seqname != gene.seqname): # new gene not overlapping
            #if (gene.start > tmp_genes[-2].end) or (tmp_genes[-1].seqname != gene.seqname): # new gene not overlapping
                for tss in _get_tss_from_gene_list(tmp_genes, add_start, add_end):
                    yield tss

                tmp_genes = [] # empty list
        tmp_genes.append(gene)
        tmp_genes.sort(key = lambda gn: (int(gn.end)))

    for tss in _get_tss_from_gene_list(tmp_genes, add_start, add_end):
        yield tss


def _get_tss_from_gene_list(tmp_genes, add_start, add_end, remove_duplicates=True):
    tmp_tss = []
    for g in tmp_genes:
        #g.children.sort(key = lambda t: (t.start))
        for tr in g.get_children():
            if tr.strand in ['+', '.']: # positive (or not specified) strand
                start = tr.start - add_start
                end = tr.start + add_end
            else: # negative strand
                start = tr.end - add_end
                end = tr.end + add_start

            tmp_tss.append((tr.seqname, start, end, tr.strand, tr.region_id)) # tss: tuple (seqname, start, end, strand, region_id)
    tmp_tss.sort(key = lambda t: (int(t[1]))) # sort transcripts by start position

    # adapted from _get_exons_from_gene_list
    if remove_duplicates:
        to_remove = []
        for num, tss in enumerate(tmp_tss[1:]): # we start with "[1:]" because we compare with the previous tss in the list
            if tss[1] == tmp_tss[num][1] and tss[2] == tmp_tss[num][2] and tss[3] == tmp_tss[num][3]: # comparing start, end, and strand
                to_remove.append(num+1) # join transcript_ids?
        for n in reversed(to_remove):
            tmp_tss.pop(n)

    for tss in tmp_tss:
        yield tss



# generates all windows inside the interval [start, end]
# consider cases where len(interval) < win_size ?
def generate_windows(start, end, win_size, win_step, fit_last=True):
    win_start = start
    win_end = win_start + win_size
    if (end - start) >= win_size:
        while win_end <= end:
            yield (win_start, win_start + win_size)
            win_start += win_step
            win_end += win_step

        if fit_last and win_end > end:
            yield (end - win_size, end)

def get_genebody(gff_path, add_start, add_end, no_sort, logger):
    for gene in read_gff_file(gff_path, no_sort=no_sort, logger=logger):
        yield gene.seqname, add_start+gene.start, gene.end+add_end, gene.strand, gene.region_id

# sliding windows
# parameter chr_length must be a dictionary with seqname as keys and integers as values (lengths) (implemented in pyicoenrich, with "chromlen" files)
def gene_slide(gff_path, win_size, win_step, win_type, chr_lengths={}, no_sort=False, logger=None):
    tmp_genes = [] # for temporarily storing overlapping genes
    for gene in read_gff_file(gff_path, no_sort=no_sort, logger=logger):
        if len(tmp_genes) > 0:
            if (tmp_genes[-1].seqname != gene.seqname): # new sequence, return last zone in tmp_genes
                if win_type == REGION_SLIDE_INTER:
                    if tmp_genes[-1].seqname in chr_lengths.keys(): # check sequence length
                        g = tmp_genes[-1]
                        for (start, end) in generate_windows(g.end, chr_lengths[g.seqname], win_size, win_step):
                            yield (g.seqname, start, end, "%s:%s"%(start, end)) # return last windows

                elif win_type == REGION_SLIDE_INTRA:
                    for (start, end) in generate_windows(tmp_genes[0].start, tmp_genes[-1].end, win_size, win_step):
                        yield (tmp_genes[-1].seqname, start, end, "%s:%s"%(start, end))

                tmp_genes = []

            else:
                if (gene.start > tmp_genes[-1].end) or (tmp_genes[-1].seqname != gene.seqname): # new gene not overlapping
                    if win_type == REGION_SLIDE_INTER:
                        sname = gene.seqname
                        start_pos = tmp_genes[-1].end
                        end_pos = gene.start

                    elif win_type == REGION_SLIDE_INTRA:
                        sname = tmp_genes[-1].seqname
                        start_pos = tmp_genes[0].start
                        end_pos = tmp_genes[-1].end

                    for (start, end) in generate_windows(start_pos, end_pos, win_size, win_step):
                        yield (sname, start, end, "%s:%s"%(start, end)) # create id?

                    tmp_genes = []

        tmp_genes.append(gene)
        tmp_genes.sort(key = lambda gn: (int(gn.end)))

    if win_type == REGION_SLIDE_INTER and len(tmp_genes) > 0 and tmp_genes[-1].seqname in chr_lengths.keys(): # last chromosome
        g = tmp_genes[-1]
        for (start, end) in generate_windows(g.end, chr_lengths[g.seqname], win_size, win_step):
            yield (g.seqname, start, end, "%s:%s"%(start, end))

    if win_type == REGION_SLIDE_INTRA and len(tmp_genes) > 0:
        for (start, end) in generate_windows(tmp_genes[0].start, tmp_genes[-1].end, win_size, win_step):
            yield (tmp_genes[-1].seqname, start, end, "%s:%s"%(start, end))
        



class RegionWriter():
    def __init__(self, gff_path, region_file, params, write_as=BED, no_sort=False, logger=None, galaxy_workarounds=False, debug=False):
        self.gff_path = gff_path
        self.region_file = region_file
        self.params = params
        self.write_as = write_as
        self.no_sort = no_sort
        self.logger = logger
        self.debug = debug
        self.galaxy_workarounds = galaxy_workarounds
        #self.write_regions()

    # (writes output to self.region_file)
    def write_regions(self):
        try:
            if self.params[0] == REGION_EXONS:
                pos = None
                if len(self.params) > 1:
                    pos = self.params[1]

                for exon in get_exons(self.gff_path, remove_duplicates=True, no_sort=self.no_sort, position=pos, logger=self.logger):
                    cl = ReadCluster(name=exon.seqname, start=exon.start, end=exon.end, strand=exon.strand, name2=exon.region_id, write=self.write_as)
                    self.region_file.write(cl.write_line())

            elif self.params[0] == REGION_INTRONS:
                pos = None
                if len(self.params) > 1:
                    pos = self.params[1]

                for (seqname, start, end, region_id, strand) in get_introns(self.gff_path, no_sort=self.no_sort, position=pos, logger=self.logger):
                    cl = ReadCluster(name=seqname, start=start, end=end, write=self.write_as, name2=region_id, strand=strand)
                    self.region_file.write(cl.write_line())

            elif self.params[0] == REGION_SLIDE:
                win_size = int(self.params[1])
                win_step = int(self.params[2])
                if win_size < win_step:
                    self.logger.error("Window size < Step!")
                    sys.exit(1)

                win_type = self.params[3]
                if len(self.params) > 4:
                    chrlen_path = self.params[4]
                else:
                    chrlen_path = None
                    if win_type == REGION_SLIDE_INTER:
                        self.logger.warning("Chromlen file not specified")

                for (seqname, start, end, name2) in gene_slide(self.gff_path, win_size, win_step, win_type, chr_lengths=self.read_chromlen(chrlen_path), no_sort=self.no_sort, logger=self.logger):
                    cl = ReadCluster(name=seqname, start=start, end=end, write=self.write_as,    name2=name2)
                    self.region_file.write(cl.write_line())

            elif self.params[0] == REGION_TSS:
                add_start = int(self.params[1])
                add_end = int(self.params[2])
                for (seqname, start, end, strand, name2) in get_tss(self.gff_path, add_start, add_end, no_sort=self.no_sort, logger=self.logger):
                    cl = ReadCluster(name=seqname, start=start, end=end, strand=strand, write=self.write_as,    name2=name2)
                    self.region_file.write(cl.write_line())

            elif self.params[0] == REGION_GENEBODY:
                add_start = add_end = 0
                if len(self.params) > 1:
                    add_start = int(self.params[1])
                if len(self.params) > 2:
                    add_end = int(self.params[2])                    

                for (seqname, start, end, strand, name2) in get_genebody(self.gff_path, add_start, add_end, no_sort=self.no_sort, logger=self.logger):                
                    cl = ReadCluster(name=seqname, start=start, end=end, strand=strand, write=self.write_as, name2=name2)
                    self.region_file.write(cl.write_line())

            else:
                self.logger.error("Incorrect --region-magic parameter: %s" % (self.params[0]))
                sys.exit(1)

            self.region_file.flush()

        except Exception as exc:
            if self.debug:
                raise
            if self.logger:
                self.logger.error(type(exc))
                self.logger.error(exc.__str__())
                self.logger.error('Unexpected or incorrect --region-magic parameters')

    def read_chromlen(self, path=None):
        length_dict = {}
        if path is None:
            if self.galaxy_workarounds:
                self.logger.info("Using default chromlen file (hg19)")
                chrlenpath = "%s/chromlen/"%os.path.dirname(os.path.abspath(__file__))
                path = chrlenpath + "/hg19"
            else:
                return length_dict

        if not os.path.isfile(path):
            self.logger.warning("Chromlen file not found")
            return length_dict

        try:
            f = open(path, 'r')
            for line in f.readlines():
                l = line.strip().split('\t')
                length_dict[l[0]] = int(l[1])
            f.close()
            #self.logger.info("Chromlen: " + str(length_dict))

        except IOError as ioerror:
            self.logger.warning("Chromlen file %s not found" % (path))

        return length_dict


