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
VERSION = "2.0.7"

#Formats
ELAND = 'eland'
ELAND_EXPORT = 'eland_export'
BED = 'bed'
BED12 = 'bed12'
WIG = 'bed_wig'
VARIABLE_WIG = 'variable_wig'
FIXED_WIG = 'fixed_wig'
PK = 'bed_pk'
SAM = 'sam'
BAM = 'bam'
COUNTS = 'counts'

CUSTOM_FORMAT = 'custom'

CLUSTER_FORMATS = (WIG, VARIABLE_WIG, FIXED_WIG, PK)
WIG_FORMATS = (WIG, VARIABLE_WIG, FIXED_WIG)
READ_FORMATS = (ELAND, BED, WIG, PK, SAM, BAM, COUNTS,  CUSTOM_FORMAT) #formats that we actually can read as
WRITE_FORMATS = (ELAND, BED, WIG, SAM, VARIABLE_WIG, PK) #formats we can actually write as

REGION_FORMATS = (BED, BED12)
#Enrichment header
enrichment_keys  = ['name', 'start', 'end', 'name2', 'score', 'strand', 'signal_a', 'signal_b', 'signal_prime_1', 'signal_prime_2',
                    'A','M','total_reads_a','total_reads_b','num_tags_a','num_tags_b','A_prime','M_prime',
                    'total_reads_a','total_reads_b','total_reads_background_1','total_reads_background_2', 'A_median', 'mean', 'sd', 'zscore']

#Default values for parsers
PARSER_NAME = "pyicoteo"

#Defaults for operations
EXPERIMENT = OUTPUT = CONTROL = COUNTS_FILE = REGION = MASKER_FILE = '' #files
EXPERIMENT_FORMAT=PK
OPEN_EXPERIMENT=False
DEBUG=False
DISCARD=0
LABEL = 'pyicoteo'
OUTPUT_FORMAT=PK
OPEN_OUTPUT=False
ROUNDING=False
CONTROL_FORMAT=None
REGION_FORMAT=BED
OPEN_REGION= False
FRAG_SIZE = 0
PUSH_DIST = 0
TAG_LENGTH = 0
SPAN=40
P_VALUE=0.01
HEIGHT_LIMIT=400
CORRECTION=1.
NO_SUBTRACT = False
DO_NORMALIZE = False
SPLIT_PROPORTION=0.1
SPLIT_ABSOLUTE=0
TRIM_PROPORTION=0.3
OPEN_CONTROL=False
NO_SORT=False
DUPLICATES=0
THRESHOLD=0
TRIM_ABSOLUTE=0
MAX_DELTA=250
MIN_DELTA=20
HEIGHT_FILTER=8
DELTA_STEP=1
VERBOSE=True
SPECIES='hg19'
CACHED=True
REPEATS=100
MAX_CORRELATIONS=200
KEEP_TEMP = False
POISSONTEST = 'height'
STRANDED_ANALYSIS = False
REMLABELS = ''
PROXIMITY=50
POSTSCRIPT=False
SHOWPLOTS=False
PLOT_PATH=None
LABEL1=""
LABEL2=""
BINSIZE=0.3
ZSCORE = 2
SDFOLD = 1
BLACKLIST=None
RECALCULATE=False
REGION_MINTAGS = 0
WINDOW_STEP = 0.1
POISSON_OPTIONS=("height", "numtags", "length")
TEMPDIR=[]
USESAMTOOLS=False
FORCE_SORT=False
#Enrichment
PSEUDOCOUNT=False
LEN_NORM=False
TMM_NORM=False
QUANT_NORM=False
N_NORM=False
SKIP_HEADER=False
TOTAL_READS_A=None
TOTAL_READS_B=None
TOTAL_READS_REPLICA=None
A_TRIM=0.05
M_TRIM=0.25
SKIP_PLOT=False
USE_REPLICA=False
SEQUENTIAL=False
EXPERIMENT_LABEL = 'Experiment'
REPLICA_LABEL = 'Replica'
TITLE_LABEL = ''
COUNT_FILTER=0
OVERLAP=0.5

#CONSTANTS
PLUS_STRAND = "+"
MINUS_STRAND = "-"
NO_STRAND = "."
EPSILON=1.0842021724855044e-19 #The smallest number above 0. Got from running 1./sys.maxint It could be smaller (it can always be), but hey, I guess this one will do. Also, computers and decimal precision, hopefully this will not be rounded to 0 in some architectures. 

NORMALIZE = 'normalize'
EXTEND = 'extend'
PUSH = 'push'
SUBTRACT = 'subtract'
SPLIT = 'split'
TRIM = 'trim'
FILTER = 'filter'
POISSON = 'poisson'
NOWRITE = 'nowrite'
DISCARD_ARTIFACTS = 'discard'
REMOVE_REGION = 'remove_regions'
REMOVE_DUPLICATES = 'remove_duplicates'
MODFDR = 'modfdr'
STRAND_CORRELATION = 'strand_correlation'

USE_MA = 'use_ma'
ENRICHMENT = 'enrichment'
CALCZSCORE = 'zscore'
PLOT='plot'

CHECK_REPLICAS = 'checkrep'

# Region-related constants
REGION_EXONS = 'exons'
REGION_INTRONS = 'introns'
REGION_SLIDE = 'slide'
REGION_SLIDE_INTER = 'inter'
REGION_SLIDE_INTRA = 'intra'
REGION_TSS = 'tss'
REGION_GENEBODY = 'genebody'

REGION_MAGIC = []
GFF_FILE = ''
INTERESTING_REGIONS = ''
DISABLE_SIGNIFICANT = False
GFF_MANDATORY_FIELDS = 8

REGIONS = 'regions'

F_CUSTOM = []
CUSTOM_SEP = '\s+'

GALAXY_WORKAROUNDS = False
HTML_OUTPUT = None
