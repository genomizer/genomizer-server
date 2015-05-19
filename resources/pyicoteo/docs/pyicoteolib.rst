.. _libdocs:

Pyicoteolib.core
================

Pyicoteolib is the library and the building blocks of the Pyicoteo suite. The pyicoteolib.core library contains the main holders of data in two main objects: ReadCluster and ReadRegion.

ReadCluster
-------------

A ReadCluster object may contain one read or a group of **overlapping** reads. It can read both tag like (bed, sam, bam..) and histogram like (wig, bed_pk...) formats. Instances of the ReadCluster object can be added, compared, subtracted to other readCluster objects with standard python syntax.

The ReadCluster object is optimized in order to deal with millions of overlaps, and has been tested with multiple different HTS datasets. The optimization consists in 2 main principles: 

Common python operators
^^^^^^^^^^^^^^^^^^^^^^^^^^

All the following standard operators are supported::

Adding
""""""""

Adding combines the signal of 2 different ReadClusters, with nucleotide precision::

    from pyicoteolib.core import ReadCluster, PK

    cluster1 = ReadCluster(read=PK)
    cluster2 = ReadCluster(read=PK)
    cluster1.read_line('chr1 1 45 9:2.00|41:3.00|50:2.00|45:1.00')
    cluster2.read_line('chr1 1 125 9:4.00|41:3.00|30:2.00|45:1.00')
    result = cluster1 + cluster2

    result.write_line()

    "chr1    1   145 50:6.00|30:4.00|20:3.00|25:2.00|20:1.00 6.0 .   25  550.0"


Subtracting
""""""""""""""

Substracts the signal of 2 different ReadClusters, with nucleotide precision::

    from pyicoteolib.core import ReadCluster, SAM, PK

    cluster1 = ReadCluster(read=SAM)
    cluster2 = ReadCluster(read=PK)
    cluster1.read_line('SL-XAJ_1_FC305HJAAXX:2:21:872:1402  0   chr1    1   50  36M *   0   0   AAAAGGGGGAATAAAAAGTAACCCAAAACTAACTAT    <<<,7<<<<<7<1:71)<+51<+<5(75()1344+2    PG:Z:FC_305HJAAXX_ln_2.dat')
    cluster2.read_line('chr1 1 125 9:4.00|41:3.00|30:2.00|45:1.00')
    result = cluster2 - cluster1

    result.write_line()

Length
"""""""""

Returns the length of the read cluster::

    c = Cluster(name="chrX", start=1, end=10000)
    len(c)

    10000


Comparison operators (< > == !=)
"""""""""""""""""""""""""""""""""""""

This indicates which read cluster is before another in a coordinate system::

    c1 = Cluster(name="chr1", start=100, end=1000)
    c1_copy = Cluster(name="chr1", start=100, end=1000)
    c2 = Cluster(name="chr1", start=50000, end=100000)
    c1 > c2 
    False
    c1 == c1_copy
    True


Lets see some usage examples.

Read a .bed file, print the length andd the area of each cluster of overlapping reads
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
::

    from pyicoteolib.core import ReadCluster, BED

    bed_file = open("/path/to/myfile.bed")

    for line in bed_file:
        rc = ReadCluster(read_as=BED)
        rc.read_line(l)
        print len(rc), rc.area()


Read some .bed lines, cluster them, output a wiggle file
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
::

    from pyicoteolib.core import ReadCluster

    cluster =  ReadCluster(read="bed", write="bed_wig")
    cluster.read_line('chr1 1 20000 id1 0 +')
    cluster.read_line('chr1 1 20000 id2 0 +')
    cluster.read_line('chr1 1 20000 id3 0 +')
    cluster.read_line('chr1 1001 20000 id4 0 +')
    cluster.write_line()

    "chr1    1       1000    3.00"
    "chr1    1001    20000   4.00"

Useful functions
^^^^^^^^^^^^^^^^^^^

``extend(self, extension)``
"""""""""""""""""""""""""""""

Extends the coordinates covered by a read in the strand direction::

    from pyicoteolib.core import ReadCluster

    minus_read = ReadCluster(read="bed", write="bed")
    minus_read.read_line('chr1 1000 2000 id1 0 -')
    minus_read.extend(30)
    minus_read.write_line()

    "chr1 970 2000 id1 0 -"

``push(push_distance)``
"""""""""""""""""""""""""""""

Push the read coordinates in the strand direction::

    from pyicoteolib.core import ReadCluster

    minus_read = ReadCluster(read="bed", write="bed")
    minus_read.read_line('chr1 1000 2000 id1 0 -')
    minus_read.push(30)
    minus_read.write_line()

    "chr1 970 1970 id1 0 -"    

``trim(ratio=0.3, absolute=0)``
""""""""""""""""""""""""""""""""""""""""""

Trim the borders of an histogram given a theshold. The ratio parameter indicates the ratio to trim counting from the top height. The absolute parameter overrides the ratio, giving a fixed height were to trim::
 
    from pyicoteolib.core import ReadCluster

    gaussian = ReadCluster(read="bed", write="bed_wig")
    gaussian.read_line("chr1 1  100 id1 0 +")
    gaussian.read_line("chr1 20 120 id2 0 +")
    gaussian.read_line("chr1 40 140 id3 0 +")
    gaussian.read_line("chr1 60 160 id4 0 +")
    gaussian.read_line("chr1 80 180 id5 0 +")
    gaussian.read_line("chr1 80 180 id6 0 +")

    #the peak of the cluster is 6
    gaussian.max_height()

    #actual profile
    gaussian.write_line()

    """
    chr1    1   19  1.00
    chr1    20  39  2.00
    chr1    40  59  3.00
    chr1    60  79  4.00
    chr1    80  100 6.00
    chr1    101 120 5.00
    chr1    121 140 4.00
    chr1    141 160 3.00
    chr1    161 180 2.00
    """

    #using a ratio parameter of 0.5, 0.5*6=3 Every level below 3 will be trimmed
    gaussian.trim(0.5)
    gaussian.write_line()

    """
    chr1    40  59  3.00
    chr1    60  79  4.00
    chr1    80  100 6.00
    chr1    101 120 5.00
    chr1    121 140 4.00
    chr1    141 160 3.00
    """

    #using an absolute parameter to trim everything under height of 4
    gaussian.trim(absolute=4)
    """
    chr1    60  79  4.00
    chr1    80  100 6.00
    chr1    101 120 5.00
    chr1    121 140 4.00
    """        

``split(ratio=0.9, absolute=0)``
""""""""""""""""""""""""""""""""""""""

This function scans each histogram position from start to end and looks for local maxima x and local minima y. Given two consecutive local maxima :math:`x^i` :math:`x^(i+1)` we define the smallest of them as :math:`x^min`. For every :math:`y^j` between two local maxima, the :math:`y` minima that will define a split point is calculated as:

.. math:: \frac{y^j} {x^{min}*(1-t)} 

Where t is a ratio between 0 and 1. By default :math:`t=0.05`. The cluster will divide at the local minimum::

    from pyicoteolib.core import ReadCluster

    double_cluster = ReadCluster(write="bed_wig")
    double_cluster.name = "chrX" #add chromosome

    #Fill the ReadCluster with a histogram with 2 maxima

    double_cluster.add_level(0, 10, 1)
    double_cluster.add_level(1, 10, 2)
    double_cluster.add_level(2, 10, 3)
    double_cluster.add_level(3, 10, 4)
    double_cluster.add_level(4, 10, 5)
    double_cluster.add_level(5, 10, 6)
    double_cluster.add_level(6, 10, 4)
    double_cluster.add_level(7, 10, 3)
    double_cluster.add_level(8, 10, 2)
    double_cluster.add_level(9, 10, 4)
    double_cluster.add_level(10, 10, 5)
    double_cluster.add_level(11, 10, 6)  
    double_cluster.add_level(12, 10, 7)     
    double_cluster.add_level(13, 10, 5) 
    double_cluster.add_level(14, 10, 3) 
    double_cluster.add_level(15, 10, 1)

    #The cluster is composed of 2 distint signals that are overlapping
    double_cluster.write_line()

    """
    chrX    0   9   1.00
    chrX    10  19  2.00
    chrX    20  29  3.00
    chrX    30  39  4.00
    chrX    40  49  5.00
    chrX    50  59  6.00
    chrX    60  69  4.00
    chrX    70  79  3.00
    chrX    80  89  2.00
    chrX    90  99  4.00
    chrX    100 109 5.00
    chrX    110 119 6.00
    chrX    120 129 7.00
    chrX    130 139 5.00
    chrX    140 149 3.00
    chrX    150 159 1.00
    """

    #split using an absolute value of 3  
    for splitted in double_cluster.split(absolute=3):
        splitted.write_line()

    #First peak
    """
    chrX    0   9   1.00
    chrX    10  19  2.00
    chrX    20  29  3.00
    chrX    30  39  4.00
    chrX    40  49  5.00
    chrX    50  59  6.00
    chrX    60  69  4.00
    chrX    70  79  3.00
    chrX    80  83  2.00
    """

    #Second peak. Note how the extremes of the peak are conserved.

    """
    chrX    85  89  2.00
    chrX    90  99  4.00
    chrX    100 109 5.00
    chrX    110 119 6.00
    chrX    120 129 7.00
    chrX    130 139 5.00
    chrX    140 149 3.00
    chrX    150 159 1.00
    """
    
is_artifact()
""""""""""""""

Returns True if the read histogram looks like a punctuated ChIP-Seq artifact, returns False otherwise.
A ReadCluster is considered artifactual if it is shorter than 100 nucleotides or the maximum height takes more than is more than 30% of the cluster::

    from pyicoteolib.core import ReadCluster

    art = ReadCluster(read="bed")
    art.read_line("chr1 1 200 repeat 0 +")
    art.read_line("chr1 1 200 repeat 0 +")
    art.read_line("chr1 1 200 repeat 0 +")
    art.read_line("chr1 1 200 repeat 0 +")

    art.is_artifact()

    True

is_empty()
"""""""""""

Returns True if the ReadCluster contains no reads, returns False otherwise::


    from pyicoteolib.core import ReadCluster

    c = ReadCluster(read="bed")
    c.is_empty()

    True

    c.read_line("gene1  10000 120000 ")

    c.is_empty()

    False

ReadRegion
-------------

A ReadRegion object holds a genomic region that may contain ReadClusters. 


Pyicoteolib.utils
=====================

The utils modules contains several file manipulation classes optimized for minimal working memory usage while working with huge sequential text files.  

SortedFileReader
------------------

Holds a cursor and a file path. Given a start and an end, it iterates through the file starting on the cursor position, and yields the clusters that overlap with the region specified. The cursor will be left behind the position of the last region fed to the SortedFileReader.

Important functions of the instances::

    def rewind(self):
        """Move back to initial cursor of the file"""
    
    def _read_line(self):
        """Reads the next line of the file. If advance, the cursor will get the position of the file"""

    def overlapping_clusters(self, region, overlap=1):
        """Returns clusters of reads overlapping with the given region. Sufficient overlap between reads and regions is defined by the overlap parameter."""

SortedFileCountReader
------------------------

Holds a cursor and a file path. Given a start and an end, it iterates through the file starting on the cursor position, and retrieves the *counts* (number of reads) that overlap with the region specified. Because this class doesn't store the reads, but only counts them, it doesn't have memory problems when encountering huge clusters of reads.


BigSort
-----------

This class can sort huge files without loading them fully into memory. It divides the files smaller files, sorts them and then merges them. 

Important functions::

    def __init__(self, file_format, read_half_open=False, frag_size=0, id=0, logger=True, filter_chunks=True, push_distance=0, buffer_size = 320000, temp_file_size = 8000000):
        """
        Constructor. Sorting buffer and file size is configurable through the ``buffer_size`` and ``temp_file_size`` parameters respectively. For optimization reason, this class can also preprocess the reads, applying extend or displacement (push) of reads while sorting. The sorting format defines how the sorting will be performed.
        """

    def sort(self, input, output=None, key=None, tempdirs=[]):    
        """
        Key parameter defines the lambda function for sorting. A list of temporary directories can be provided for the sorting algorithm to use through the tempdirs parameter.
        """


Example of sorting a huge SAM file::

        from pyicoteolib.utils import BigSort
    
        unsorted_file = open("/path/to/file/unsorted.sam")
        #we want to sort and extend the reads 1000 bp in strand direction at the same time
        sorter = utils.BigSort("sam", False, frag_size=0, 'fisort%s'%temp_name, logger=self.logger)
        sorted_file = sorter.sort(old_path, None, utils.sorting_lambda(file_format))


Credit
-------

* Developers: Juan González-Vallinas
* Unit and beta Testing: Juan González-Vallinas, Ferran Lloret
* Supervision: Eduardo Eyras


