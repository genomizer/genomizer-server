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
import struct
import zlib
import sys
from math import ceil, floor
import subprocess

from defaults import *
from pyicoteolib.core import Cluster, InvalidLine



SEQUENCE_TRANS = list('=ACMGRSVTWYHKDBN')
CIGAR_TRANS = list('MIDNSHP=X')
LINEAR_SIZE = 16384.
s_uint8 = struct.Struct("<B").unpack
s_int8 = struct.Struct("<b").unpack
s_uint16 = struct.Struct("<H").unpack
s_int16 = struct.Struct("<h").unpack
s_uint32 = struct.Struct("<I").unpack
s_int32 = struct.Struct("<i").unpack
s_uint64 = struct.Struct("<Q").unpack

def size(path):
    """Reads the BAM file just enough to count the number of lines """ 
    f = open(path, "rb")
    dec = ""   
    dec_cursor = 0
    block_size = 0
    num_reads = 0
    bam_header = True
    try:
        extra_len = read_gzip_header(f) #get the field extra_len from the gzip header
        while extra_len:
            cdata = read_bgzf(f, extra_len) #get the compressed bam data from the CDATA extra field
            dec = "%s%s"%(dec, zlib.decompress(cdata, -15)) #decompress the CDATA field
            dec_cursor = 0
            if bam_header:
                bam_header = False
                read_bam_header(dec, dec_cursor)

            while dec:
                if block_size == 0: 
                    if len(dec[dec_cursor:]) < 4:
                        dec, extra_len = get_new_dec(dec, dec_cursor, f)
                        dec_cursor = 0
                        if not dec: #its over
                            break

                    new_cursor = dec_cursor+4                
                    block_size = s_uint32(dec[dec_cursor:new_cursor])[0]

                    dec_cursor = new_cursor            
                    if block_size > len(dec[dec_cursor:]):
                        dec, extra_len = get_new_dec(dec, dec_cursor, f)
                        dec_cursor = 0
                        if not dec: #its over
                            break

                num_reads += 1 #every block line corresponds to a read
                #print num_reads, block_size                
                dec_cursor += block_size
                block_size = 0

            gzip_tail = f.read(8)
            extra_len = read_gzip_header(f) #get the field extra_len from the gzip header

        #TODO check that everything is 0 for truncate
    finally:
        f.close()    

    return num_reads


def read_gzip_header(f):
    try:
        #gzip_header = f.read(10) #Gzip header
        #TODO probar si merece la pena hacer checks de los headers (tiempo vs mirar si el BAM esta bien formado)
        id1 = s_uint8(f.read(1))[0]
        id2 = s_uint8(f.read(1))[0]
        #print "signature:", id1, id2
        method = s_uint8(f.read(1))[0]
        flags = s_uint8(f.read(1))[0]
        mtime = s_uint32(f.read(4))[0]
        xfl = s_uint8(f.read(1))[0]
        gz_os = s_uint8(f.read(1))[0]
        extra_len = s_uint16(f.read(2))[0] # extra-header length. Siempre 4, supongo
    except struct.error: #Reached the end of the file
        return None

    return extra_len

def read_bgzf(f, extra_len):
    bgzf_header = f.read(4) #bgzf_header
    """id1 = s_uint8(f.read(1))[0] 
    id2 = s_uint8(f.read(1))[0]  
    slen = s_uint16(f.read(2))[0]"""
    bsize = s_uint16(f.read(2))[0] 
    cdata = f.read(bsize-extra_len-19)  
    return cdata


def read_int32(string, dec_cursor):
    new_cursor = dec_cursor + 4   
    return s_int32(string[dec_cursor:new_cursor])[0], new_cursor

def get_bytes(string, dec_cursor, num_bytes):
    "Reads bytes from the top of the string, and pops them returning the remainder"
    new_cursor = dec_cursor+num_bytes
    return string[dec_cursor:new_cursor], new_cursor

def get_new_dec(dec, dec_cursor, f):
    #gzip_tail = f.read(8)
    try:
        f.read(18) #Gzip header and tail
        extra_len = s_uint16(f.read(2))[0] # extra-header length. Siempre 4, supongo
    except struct.error: #Reached the end of the file
        return None, None
 
    dec = dec[dec_cursor:] #remove the already read dec
    cdata = read_bgzf(f, extra_len) #get the compressed bam data from the CDATA extra field
    try: 
        new_dec = zlib.decompress(cdata, -15)
    except zlib.error:
        print "ZLIB error reading error while decompressing bam file (truncated file?)"            
        return None, None

    return "%s%s"%(dec, new_dec), extra_len #decompress the CDATA field



def read_bam_header(dec, dec_cursor):
    chr_dict = {}
    magic, dec_cursor = get_bytes(dec, dec_cursor, 4) #Magic BAM
    l_text, dec_cursor = read_int32(dec, dec_cursor) # Length of the header text
    header, dec_cursor = get_bytes(dec, dec_cursor, l_text) #The header
    num_chrom, dec_cursor = read_int32(dec, dec_cursor) #The number of sequences (chromosomes)
    for i in range(0, num_chrom):
        l_name, dec_cursor = read_int32(dec, dec_cursor) #length of the reference name
        seq_name, dec_cursor = get_bytes(dec, dec_cursor, l_name) #The name of the reference sequence               
        l_ref, dec_cursor = read_int32(dec, dec_cursor)
        chr_dict[i] = seq_name.split("\0", 1)[0]

    return dec, dec_cursor, header.split("\0", 1)[0], chr_dict


def read_alignment(dec, dec_cursor, block_size, chr_dict):
    start_dec = len(dec[dec_cursor:]) #store the current size of dec, so when we arrive to the madness part of the tag-val_type, we know how much block we have left
    new_cursor = dec_cursor+4
    ref_id = s_uint32(dec[dec_cursor:new_cursor])[0] #The reference id (chromosome id)
    #print ref_id
    dec_cursor = new_cursor
    new_cursor = dec_cursor+4

    pos = s_uint32(dec[dec_cursor:new_cursor])[0]
    #print pos

    dec_cursor = new_cursor        
    #decompose bit_mq_nl into bin, mapq, l_read_name
    new_cursor = dec_cursor+4
    bit_mq_nl = s_uint32(dec[dec_cursor:new_cursor])[0]
    my_bin = int(bit_mq_nl >> 16)
    mapq = int((bit_mq_nl >> 8)&255)
    l_read_name = int(bit_mq_nl&255)
    dec_cursor = new_cursor
    
    #decompose flag_nc
    new_cursor = dec_cursor+4
    flag_nc = s_uint32(dec[dec_cursor:new_cursor])[0]
    dec_cursor = new_cursor      
    flag = int(flag_nc >> 16)  
    n_cigar_op = int(flag_nc&65535)    

    new_cursor = dec_cursor+4        
    l_seq = s_uint32(dec[dec_cursor:new_cursor])[0] #Length of the nucl. sequence
    dec_cursor = new_cursor  
    new_cursor = dec_cursor+4 
    next_ref_id = s_uint32(dec[dec_cursor:new_cursor])[0]
    dec_cursor = new_cursor  
    new_cursor = dec_cursor+4    
    next_pos = s_uint32(dec[dec_cursor:new_cursor])[0]
    dec_cursor = new_cursor  
    new_cursor = dec_cursor+4            
    t_len = s_uint32(dec[dec_cursor:new_cursor])[0]
    dec_cursor = new_cursor  
    new_cursor = dec_cursor+l_read_name
    read_name = dec[dec_cursor:new_cursor]
    dec_cursor = new_cursor
    cigar_str = []
    cigar_ap = cigar_str.append
    for i in range(0, n_cigar_op): 
        new_cursor = dec_cursor+4        
        cigar = s_uint32(dec[dec_cursor:new_cursor])[0]
        dec_cursor = new_cursor              
        cigar_ap(str(int(cigar >> 4)))           
        cigar_ap(CIGAR_TRANS[int(cigar&15)])
        
    cigar_str = ''.join(cigar_str) #for performance

    sequence = []
    sequence_ap = sequence.append
    #TODO control when sequence length is not odd (par, impar)?
    for i in range(0, ((l_seq+1)/2)):
        new_cursor = dec_cursor+1  
        two_nucl = ord(dec[dec_cursor:new_cursor])
        sequence_ap(SEQUENCE_TRANS[int(two_nucl >> 4)]) 
        sequence_ap(SEQUENCE_TRANS[int(two_nucl&15)]) 
        dec_cursor = new_cursor
        
    sequence = ''.join(sequence) #for performance

    #Quality madness
    new_cursor = dec_cursor+l_seq
    qual = dec[dec_cursor:new_cursor]
    dec_cursor = new_cursor   
    qual_str = []
    qual_ap = qual_str.append
    for byte in qual:
        qual_ap(unichr(s_uint8(byte)[0]+33)) #ascii base plus 33 (same as fastq)
    qual_str = ''.join(qual_str) #for performance       
    
    consumed = start_dec - len(dec[dec_cursor:])
    bytes_left = block_size - consumed 
    new_cursor = dec_cursor+bytes_left    
    block_end = dec[dec_cursor:new_cursor]
    dec_cursor = new_cursor     

    tag = block_end[0:2]
    tag_cursor = 2
    op_fields = []
    while tag_cursor < len(block_end):
        new_cursor = tag_cursor + 1
        value_type = block_end[tag_cursor:new_cursor]
        tag_cursor = new_cursor
        if value_type == "A":
            new_cursor = tag_cursor + 1
            value = block_end[tag_cursor:new_cursor]
            tag_cursor = new_cursor

        elif value_type == "c":
            value_type = "i"
            new_cursor = tag_cursor + 1
            value = s_int8(block_end[tag_cursor:new_cursor])[0]
            tag_cursor = new_cursor
            
        elif value_type == "C":
            value_type = "i"    
            new_cursor = tag_cursor + 1
            value = s_uint8(block_end[tag_cursor:new_cursor])[0]
            tag_cursor = new_cursor
  
        elif value_type == "s":
            value_type = "i"    
            new_cursor = tag_cursor + 2
            value = s_int16(block_end[tag_cursor:new_cursor])[0]
            tag_cursor = new_cursor

        elif value_type == "S":
            value_type = "i"
            new_cursor = tag_cursor + 2
            value = s_uint16(block_end[tag_cursor:new_cursor])[0]
            tag_cursor = new_cursor

        elif value_type == "i":
            new_cursor = tag_cursor + 4
            value = s_int32(block_end[tag_cursor:new_cursor])[0]
            tag_cursor = new_cursor

        elif value_type == "I":
            value_type = "i"
            new_cursor = tag_cursor + 4
            value = s_uint32(block_end[tag_cursor:new_cursor])[0]
            tag_cursor = new_cursor

        elif value_type == "f":           
            new_cursor = tag_cursor + 4
            value = s_float.unpack(block_end[tag_cursor:new_cursor])[0]
            tag_cursor = new_cursor

        elif value_type == "Z" or value_type == "H" or value_type == "B":   
            #string
            value = []
            new_cursor = tag_cursor + 1
            value_chr = block_end[tag_cursor:new_cursor]
            tag_cursor = new_cursor
            while value_chr:
                value.append(value_chr)
                new_cursor = tag_cursor + 1
                value_chr = block_end[tag_cursor:new_cursor]
                tag_cursor = new_cursor              
            value = ''.join(value).split("\0", 1)[0]

        optional_field = "%s:%s:%s"%(tag, value_type, value)
        op_fields.append(optional_field)
        new_cursor = tag_cursor + 2
        tag = block_end[tag_cursor:new_cursor]
        tag_cursor = new_cursor
   
    return "\t".join([read_name.split("\0", 1)[0], str(flag), chr_dict[ref_id], str(pos+1), str(mapq), cigar_str, "*", "0", "0", sequence, qual_str] + op_fields), dec, dec_cursor


def get_names(path):
    """
    Returns the list of names in the BAM. Used for knowing how to sort the region file.
    """ 
    f = open(path, "rb")
    extra_len = read_gzip_header(f) #get the field extra_len from the gzip header
    cdata = read_bgzf(f, extra_len) #get the compressed bam data from the CDATA extra field
    dec = "%s%s"%(dec, zlib.decompress(cdata, -15)) #decompress the CDATA field
    magic, dec_cursor = get_bytes(dec, dec_cursor, 4) #Magic BAM
    l_text, dec_cursor = read_int32(dec, dec_cursor) # Length of the header text
    header, dec_cursor = get_bytes(dec, dec_cursor, l_text) #The header
    num_chrom, dec_cursor = read_int32(dec, dec_cursor) #The number of sequences (chromosomes)
    for i in range(0, num_chrom):
        l_name, dec_cursor = read_int32(dec, dec_cursor) #length of the reference name
        seq_name, dec_cursor = get_bytes(dec, dec_cursor, l_name) #The name of the reference sequence               
        l_ref, dec_cursor = read_int32(dec, dec_cursor)
        chr_list.append(seq_name.split("\0", 1)[0])

    return chr_list

    





def BamReader(path, logger=None, tell=None, read_start=0, chr_dict = None): 
    """
    A generator, to read the complete BAM file from start to end
    """
    logger.debug("_BAM READER: Start.") 
    f = open(path, "rb")
    logger = logger
    dec = ""   
    dec_cursor = read_start
    block_size = 0
    bam_header = not tell 
    first = True
    if tell:
        f.seek(tell)

    extra_len = read_gzip_header(f) #get the field extra_len from the gzip header
    while extra_len:
        cdata = read_bgzf(f, extra_len) #get the compressed bam data from the CDATA extra field
        dec = "%s%s"%(dec, zlib.decompress(cdata, -15)) #decompress the CDATA field
        if first:
            first = False
        else:                        
            dec_cursor = 0

        if bam_header:
            bam_header = False
            dec, dec_cursor, header, chr_dict = read_bam_header(dec, dec_cursor)
            if header: yield header

        
        while dec:
            if block_size == 0: 
                if len(dec[dec_cursor:]) < 4:
                    dec, extra_len = get_new_dec(dec, dec_cursor, f)
                    if first:
                        first = False
                    else:                        
                        dec_cursor = 0
                    if not dec: #its over
                        break

                new_cursor = dec_cursor+4                
                block_size = s_uint32(dec[dec_cursor:new_cursor])[0]
                dec_cursor = new_cursor            
                if block_size > len(dec[dec_cursor:]):
                    dec, extra_len = get_new_dec(dec, dec_cursor, f)
                    if first:
                        first = False
                    else:                        
                        dec_cursor = 0
                    if not dec: #its over
                        break

            yield_line, dec, dec_cursor = read_alignment(dec, dec_cursor, block_size, chr_dict)
            yield yield_line
            block_size = 0

        gzip_tail = f.read(8)
        extra_len = read_gzip_header(f) #get the field extra_len from the gzip header

    #TODO check that everything is 0 for truncate


class BamFetcher:

    def __init__(self, bam_path, read_half_open=False, rounding=True, cached=True, logger=None):

        self.logger.info('Fetcher used for %s: Bam Fetcher'%file_path)
        self.__dict__.update(locals())
        self.bai_path = "%s.bai"%bam_path
        self.bam_file = open(bam_path)
        self.check_bai()
        self.num_references = s_int32(self.bai_file.read(4))[0]
        self.get_chrdict()
        self.get_reference_tells()


    def check_bai(self):
        try:
            self.bai_file = open(self.bai_path)

        except:
            print "The .bai file is missing", self.bai_path #TODO return exception
            sys.exit(1)

        magic = self.bai_file.read(4)
        if magic[0:3] != "BAI":
            print "Invalid .bai file", self.bai_path
            sys.exit(1) #return Exception



    def get_reference_tells(self):
        "Gets the tells for the start of each reference (chromosome, normally) for the index"
        self.reference_tells = []
        for i in range(0, self.num_references):
            self.reference_tells.append(self.bai_file.tell())
            n_bin = s_int32(self.bai_file.read(4))[0]
            for x in range(0, n_bin):
                my_bin = s_uint32(self.bai_file.read(4))[0]
                n_chunk = s_int32(self.bai_file.read(4))[0]
                self.bai_file.read(16*n_chunk)

            n_intv = s_int32(self.bai_file.read(4))[0]
            self.bai_file.read(8*n_intv)


    def get_chrdict(self):
        self.chr_dict = {}
        dec_cursor = 0
        extra_len = read_gzip_header(self.bam_file) #get the field extra_len from the gzip header
        cdata = read_bgzf(self.bam_file, extra_len) #get the compressed bam data from the CDATA extra field
        dec = zlib.decompress(cdata, -15)
        magic, dec_cursor = get_bytes(dec, dec_cursor, 4) #Magic BAM
        l_text, dec_cursor = read_int32(dec, dec_cursor) # Length of the header text
        header, dec_cursor = get_bytes(dec, dec_cursor, l_text) #The header
        num_chrom, dec_cursor = read_int32(dec, dec_cursor) #The number of sequences (chromosomes)
        for i in range(0, num_chrom):
            l_name, dec_cursor = read_int32(dec, dec_cursor) #length of the reference name
            seq_name, dec_cursor = get_bytes(dec, dec_cursor, l_name) #The name of the reference sequence                
            l_ref, dec_cursor = read_int32(dec, dec_cursor)
            self.chr_dict[seq_name.split("\0", 1)[0]] = i

        self.chr_dict_inv = dict((v,k) for k, v in self.chr_dict.iteritems())



    def get_bam_tell(self, region):
        current_ref = self.chr_dict[region.name]
        self.bai_file.seek(self.reference_tells[current_ref]) # go to the current reference         
        n_bin = s_int32(self.bai_file.read(4))[0]
        for i in range(0, n_bin):
            my_bin = s_uint32(self.bai_file.read(4))[0]
            n_chunk = s_int32(self.bai_file.read(4))[0]
            #print "    Num Chunks: ", n_chunk
            self.bai_file.read(16*n_chunk) #not using because I dont get it 
            #for j in range(0, n_chunk):
            #    chunk_beg = s_uint64(self.bai_file.read(8))[0]
            #    chunk_end = s_uint64(self.bai_file.read(8))[0]

        n_intv = s_int32(self.bai_file.read(4))[0]

        if region.start < LINEAR_SIZE*4:
            interval_start = 0 
        else:
            interval_start = int(floor((region.start-LINEAR_SIZE*4)/LINEAR_SIZE))
            
        if region.end < LINEAR_SIZE*4:
            interval_end = 0
        else:
            interval_end = int(floor((region.end-LINEAR_SIZE*4)/LINEAR_SIZE))

        #so we dont fall off the reference, in case an overflow region appears
        interval_start = min(n_intv-1, interval_start)
        interval_end = min(n_intv-1, interval_end)
        #skip to the start interval
        self.bai_file.read(8*(interval_start))
        num_intervals = interval_end-interval_start+1
        print n_intv, interval_start, interval_end
        print "Num_intervals", num_intervals, 
        ioffset = None 
        for n in range(0, num_intervals):
            ioffset = s_uint64(self.bai_file.read(8))[0]
            if ioffset:
                break
            else:
                print "discarded:", ioffset,

        print
        if ioffset:
            coffset = int(ioffset >> 16)
            uoffset = int(ioffset&65535) #El acceso al seek del fichero descomprimido (el dec)
            return coffset, uoffset
        else:
            return 0,0 #start of the reference file

  
    def get_overlaping_clusters(self, region, overlap=1):
        clusters = []
        bam_tell, read_start = self.get_bam_tell(region)
        print "TELL", bam_tell, read_start
        if bam_tell or region.start < LINEAR_SIZE*4:
            r = BamReader(self.bam_path, self.logger, bam_tell, read_start, self.chr_dict_inv)
            for line in r:
                c = Cluster(read=SAM, cached=False, read_half_open=self.read_half_open, rounding=self.rounding)
                try:
                    c.read_line(line)
                except InvalidLine:
                    print "Invalid line, .bam or .bai corrupt"
                    break

                if c.overlap(region) >= overlap:                
                    clusters.append(c)
                elif c.start > region.end or c.name != region.name:
                    break

        if len(clusters) > 0:
            print "Num clusters", len(clusters)
            print "first:", clusters[0].start, clusters[0].end
            if len(clusters) > 1: print "end:", clusters[-1].start, clusters[-1].end
        else:
            print "No clusters found!"
        print

        return clusters
        

class BamFetcherSamtools(BamFetcher):

    def __init__(self, bam_path, read_half_open=False, rounding=True, cached=True, logger=None):
        self.__dict__.update(locals())
        self.logger.info('Fetcher used for %s: Bam Fetcher (using Samtools)'%bam_path)
        self.bai_path = "%s.bai"%bam_path
        self.bam_file = open(bam_path)
        self.check_bai()


    def get_overlaping_clusters(self, region, overlap=1):    
        clusters = []
        self.logger.debug('Launching Samtools for %s...'%region)
        proc = subprocess.Popen("samtools view %s %s:%s-%s"%(self.bam_path, region.name, region.start, region.end), stdout=subprocess.PIPE, shell=True)
        out, err = proc.communicate()
        self.logger.debug('... done')
        lines = filter(None, out.split("\n"))
        self.logger.debug('Numlines in %s: %s'%(region, len(lines)))
        for line in lines:
            c = Cluster(read=SAM, cached=False, read_half_open=self.read_half_open, rounding=self.rounding)
            try:
                c.read_line(line)
            except InvalidLine:
                print "Invalid line, .bam or .bai corrupt"
                break

            if c.overlap(region) >= overlap:                
                clusters.append(c)
            elif c.start > region.end or c.name != region.name:
                break

        return clusters
        







