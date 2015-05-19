#Pyicoteo#

Pyicoteo is a suite of tools for the analysis of high-throughput sequencing data. It works with genomic coordinates, it was mainly developed for Illumina mapped reads, but it's platform-agnostic, since in many cases, it simply works with genomic coordinates. There are currently 6 different command-line based tools:
 
- [**pyicoregion**](#markdown-header-pyicoregion): for generating exploratory regions automatically
- [**pyicoenrich**](#markdown-header-pyicoenrich): for differential enrichment between two conditions, 
- [**pyicoclip**](#markdown-header-pyicoclip): for calling CLIP-Seq peaks without a control, 
- [**pyicos**](#markdown-header-pyicos): for genomic coordinates manipulation, 
- [**pyicoller**](#markdown-header-pyicoller): for peak calling on punctuated ChIP-Seq,
- [**pyicount**](#markdown-header-pyicount): to count how many reads from N experiment files overlap in a region file.
- [**pyicotrocol**](#markdown-header-pyicotrocol): to combine operations from **pyicoteo**

Additionally, *pyicoteo* can be accessed programmatically using two libraries:

- [**pyicoteolib.core**](#markdown-header-pyicoteolib-core): it contains the main objects that hold the data
- [**pyicoteolib.utils**](#markdown-header-pyicoteolib-utils):  it contains file manipulation classes optimized for minimal memory usage

An earlier version of this software was described here
[(Pyicos. Bioinf 2011)](http://bioinformatics.oxfordjournals.org/content/27/24/3333)

______________________
#Table of Contents#

- [Getting Started](#markdown-header-getting-started)
	- Download & Install
	- Dependencies
	- The bedpk format
	- General and important flags
- [**pyicoregion**](#markdown-header-pyicoregion)
	- pyicoregion arguments
- [**pyicoenrich**](#markdown-header-pyicoenrich)	
	- Basic usage
	- Region exploration
	- Important flags
	- Normalization Options
	- Input Counts file
	- Output Counts file
- [**pyicoclip**](#markdown-header-pyicoclip)
	- Introduction
	- Basic usage
	- Important flags 
	- Full CLIP analysis
- [**pyicos**](#markdown-header-pyicos)
	- convert
	- remregions
	- remduplicates
	- strcorr (Strand Correlation)
   	- extend
   	- subtract
   	- split  
   	- discard
   	- filter
   	- push
- [**pyicoller**](#markdown-header-pyicoller)
   	- Important flags
- [**pyicount**](#markdown-header-pyicount)
- [**pyicotrocol**](#markdown-header-pyicotrocol)
   	- Syntax
   	- Examples
- [**pyicoteolib.core**](#markdown-header-pyicoteolib-core)
   	- ReadCluster
   	- Useful functions
   	- ReadRegion
- [**pyicoteolib.utils**](#markdown-header-pyicoteolib-utils)
   	- SortedFileReader
   	- SortedFileCountReader
   	- BigSort
- [**Credits & Support**](#markdown-header-credits-and-support)

---------------------------
#Getting Started#

##Download##

Download Pyicoteo Latest version from our repository. The command line tools can be used directly without installation. However, installation is recommended, and necessary if you intend to use the Pyicoteolib. To do so decompress the folder and run the setup.py script with administrator privileges:

```
python setup.py install
```

To test that the software was installed correctly, start a python console and try importing it by typing:

```
python
>>> import pyicoteolib
>>> import pyicoteolib.core
```
Also, you should find the tools in your command line.

Tip: If you are in a Unix-like (GNU/Linux, FreeBSD, Mac) system, open a console, type “pyico” and tap TAB twice. You should see something like this:

```
pyicoclip  pyicoller  pyicos   pyicoenrich   pyicoregion  pyicotrocol
```

##Dependencies [depends]##

Pyicoteo has basically no dependencies other than Python 2.6 or higher. However, there are two optional libraries you could install to obtain full functionality.

- Matplotlib: For plotting capabilities, it is necessary to install Matplotlib (> 1.0).
- Samtools: for BAM reading. Pyicoteo includes a BAM reader, but you can ask Pyicoteo to read BAM using samtools with the flag --samtools.

Finally, Pyicoteo is not compatible with Python 3.

## The bedpk format ##

Some Pyicoteo tools (Pyicos, Pyicoller and Pyicoclip) default experiment and output formats is a derivative of UCSC Bed format called bedpk. It follows the same starting fields “chromosome/tag start end” but it uses some of the original optional fields to include extra information. It is a cluster oriented format that aims to recollect information of a cluster of reads in a single comprehensive line.

![bedpk_format.svg.png](https://bitbucket.org/repo/zMEkqB/images/785385912-bedpk_format.svg.png)

The bedpk format is like a BED6 format, but using the 4th column to store extra information about how the cluster was built.

The bedpk column definition:

1. Chromosome
2. Start coordinate
3. End coordinate
4. Profile: This field summarizes the accumulation of reads per nucleotide of the cluster. The first number is the number of bases covered, while the second will be the number of reads in those bases. See the example above
5. Height: The maximum height of the cluster. In this case, 3.
6. Strand: if all reads are on the positive strand +, negative strand -. If there are reads from both strands, the symbol will be .
7. Summit: The position where the maximum height is found. The binding site is expected to be close to the summit.
8. Area: The area covered by the cluster.
9. p-value: The significance of the cluster. Calculated by the poisson operation if you use the pypyicoller, or from the modified FDR calculation if you are using pyicoclip.
 
## General and important flags to use with Pyicoteo commands [flags]##

```-f or –input-format```

Format desired for the input. You can choose between eland, bed, bed_wig, bed_pk, sam, bam, counts and custom text file. Depending on the tool used, some formats will not be available.

```-F or –output-format```

Format desired for the output. You can choose between eland, bed, bed_wig, bed_pk, sam, counts and custom text file. Depending on the tool used, some formats will not be available.

### Disk usage flags

The tools in Pyicoteo follow the principle of minimal memory usage. Because of this, Pyicoteo makes heavy use of disk and temporary files. You should make sure that you have at least double the space of each file in your hard drive before using Pyicoteo. Also, please take into consideration the following flags:

```--tempdir```
Pyicoteo uses the default temporary directory in the system (in many Unix based system, the content of the variable $TEMPDIR, normally /tmp). You can change the temporary directory with this flag.

```--keep-temp```
Pyicoteo tries to delete all unnecessary temporary files. If you want to keep the files in order to check them reuse them (for example, the sorted files could be useful in combination of the --no-sort flag) or delete them, use this flag

```--postscript```
If you want to get a postscript output on your plots instead of png.

```--showplots```
To launch matplotlib interactive mode instead of saving the resulting plot.


(go to [Table of Contents](#markdown-header-#Table of Contents#))

-----------------------------
# Pyicoregion #

It generates intragenic or intergenic windows, exons, introns, TSS... from an annotation. Pyicoregion is a Pyicoteo module for processing region files. Its input is GTF files as defined by the [Gencode Annotation](http://www.gencodegenes.org/data.html). **Note**:  it might not work with other GTF formats (e.g. Ensembl) as the *gene** feature is required . The main function of this tool is to generate exploratory region files like sliding windows of arbitrary length in the intergenic and intragenic spaces, exons, introns, TSS of any genome in existence. The following drawing illustrates the different region files that can be generated by the tool:

![Pyicoregion.png](https://bitbucket.org/repo/zMEkqB/images/1350837729-Pyicoregion.png)

**Examples:**
Using the last GENCODE annotation file, output exons as a SAM file:
```
pyicoregion --region-magic tss 1000 2000 -F sam --gff-file gencode.v11.bed
```
Using the last GENCODE annotation file, output all regions around TSS 1000 bases upstream and 2000 bases downstream as a BED file:
```
pyicoregion --region-magic tss 1000 2000 -F bed --gff-file gencode.v11.bed
```

## Pyicoregion arguments

####--region-magic

- **exons [position]** Returns all the exons in the region file. If the optional argument [position] is specified (possible values: *first*, *last*), it will only return the first or last exon of every gene.

- **introns [position]**  Returns all the introns in the region file. If the optional argument [position] is specified (possible values:   first, last), it will only return the first or last intron of every gene.

- **tss <add_start> <add_end>**: it returns the TSS for every transcript in the region file. Due to a TSS being a single point, the arguments *<add_start>* and *<add_end>* specify the values added to the start and end of every TSS (taking into consideration the strand). For **pyicoregion** to work correctly, they must be non-negative integers. Also, if the strand is not specified, the regions will be treated as if they were positive.

- **slide <window_size> <window_step> <region_type> [chromlen_file_path]** Searches for intergenic and intragenic regions using sliding windows. Mandatory arguments are:  

	- <window_size> (the size of the sliding window)
	- <window_step> (the distance between the start position of every consecutive window. It must be  lower than or equal to the window size) and 
	- <region_type> (must be inter, for intergenic, or intra, for intragenic regions)
	- [chromlen_file_path] is an optional argument  used to specify the path to the file containing the   chromosome lengths (Pyicoteo’s own chromlen files can be found in pyicoteolib/chromlen/). If it is not specified for intergenic regions, the results for the last regions of the chromosomes might be wrong.
	- **Note:** if the last segment of a region is shorter than the window size, the step distance is decreased by the difference (the window size stays the same). Also, regions shorter than the window size are ignored.


####--gff-file <gff_file_path>
Used to specify the path of the GFF file containing the regions. This argument is mandatory for all operations involving regions.

**Examples**
Generate a BED file with all introns:
```
pyicoregion my_reference.gff output_introns.bed --region-magic introns
```


(go to [Table of Contents](#markdown-header-table-of-contents))

---------------------------
# Pyicoenrich #

This command applies enrichment analysis on any type of sequencing data from two conditions. You can find out how significant the difference of these two conditions are in terms of the number of reads overlapping regions of interest.

## Basic usage

Pyicoenrich usage requires the experiment files in reads format (BED, SAM/BAM, Eland...).
```
pyicoenrich -reads condition_a.bed condition_b.bed -output output.pk -f bed --region my_regions.bed
```
Alternatively, you can provide a counts file with the already calculated read counts for the interesting regions (see Input Counts file):
```
pyicoenrich -counts a_and_b.counts -output result.out
```
Pyicoenrich scoring system is based on an MA-Plot. Every point in the MA-plot represents a region, the y axis is the log2 ratio of the normalized reads in the region (M value), while the mean number of reads in a region is the average of the normalized log2 counts (A value).Regions with less reads (smaller A, left side of the x axis) are less reliable to detect differential enrichment. 

![ma_basic.png](https://bitbucket.org/repo/zMEkqB/images/2898524929-ma_basic.png)

Each point represents a region. The top plot is the background distribution obtained from the comparison between replicas. The bottom plot is the comparison between the 2 conditions. The distribution of the regions in the background (up) is used to calculate the Z-scores on the comparison of the 2 conditions (down). In both cases, the result file will be in Input Counts file

## Region exploration

If a region file is provided, Pyicoenrich returns for each region a Z-Score (See counts file description), which indicates the enrichment/depletion of condition A over condition B. If no region file is provided, Pyicoenrich provides the options to take the union of reads from both conditions to define the regions and provides the Z-Scores for the generated regions. 
In order to decide what regions are to be explored, you have 3 main options:

**Generate a file with the** ```--region-magic``` flag and GTF file. See the Pyicoregion documentation below for examples on how to use --region-magic flag to automatically explore exons, introns and the whole genome using sliding windows automatically generating your region files from standard GENCODE GTF files.
          
**Provide a regions file**. If a region file is provided, Pyicoenrich returns for each region a z-score which indicates the enrichment/depletion of condition A over condition B. The region file should be in BED format. Also, you may consider only discontinuous regions by using the BED12 format:
```
pyicoenrich -reads kidney1.bed liver1.bed -output
```
```
pyicoenrich_Kidney_Liver_result_Counts -f bed --region genes.bed
```

**Do nothing**. If no region file is provided, Pyicoenrich will automatically generate one by taking the union of reads from both conditions as a region and gives back Z-Scores for the generated regions. The flag  --proximity  controls the distance with which the regions are considered “joined”. Default is 50nt:
```
pyicoenrich -reads kidney1.bed liver1.bed -output Pyicoenrich_Kidney_Liver_result -f bed --proximity 50nt
```
![region_definition.png](https://bitbucket.org/repo/zMEkqB/images/1139191902-region_definition.png)

*In the left image, no regions were pre-defined, so they are calculated from the read data. In the right image, two regions were defined and the read count is determined from the overlap of the regions with the reads*

## Important flags

This is a description of the most important flags. To see the complete list of flags, type pyicoenrich -h to get the full list.

**--interesting-regions**

Providing a list of interesting regions matching the 4th column of the regions file (using -reads) or count file (using -counts) will highlight them in the MA plot.

![Enrich_Interesting.png](https://bitbucket.org/repo/zMEkqB/images/3794575360-Enrich_Interesting.png)

**Example** of an enrichment output plot using --interesting-regions, assuming that we are using reads, a region file named regions.bed and a list of interesting regions interreg.txt, this is how the files will look like.

```
Region file (regions.bed):

chr1    1     100     region1    0    .
chr1    1000  1100    region2    0    .
chr10   1     100     region3    0    .
...
chrN    x     y       regionN    0    .
```

Interesting regions file (interreg.txt):

```
region2
region10
...
regionZ
```

Example command:
```
pyicoenrich -reads kidney1.bed liver1.bed -output rpkm_norm.enrich -f bed --region genes.bed --interesting-regions interreg.txt
```

**pseudocounts**

As regions with 0 reads in one condition might be especially interesting, Pyicoenrich can use
```--pseudocounts```
in order to avoid a division by 0: Pyicoenrich calculates the ratio of number of reads in both conditions. As there might not be any reads in a region, Pyicoenrich assumes that there is already 1 read in each region in each condition.

**strandness**

To take into consideration reads that coincide with the strand direction of the regions (6th column in your BED6 file), you can use the option 
```--stranded```

## Normalization Options ##

Pyicoenrich included several standard normalization methods for the counts. IMPORTANT: There is no silver bullet with normalization methods. You need to understand your data and then apply the method that is appropriate for it. If you are in doubt, please consult your local statistician.

#### MA calculation adjustment

The different z-scores values are calculated using a sliding through the region points left to right in the y axis (smaller to greater saturation of reads). You can tweak this calculation using the --binsize and --binstep

```--binsize```
The size of the bins to calculate the local sd and mean for the background model, as a ratio of total number or regions. Regardless of the ratio selected, the minimum window size is 50 regions, since below that threshold the results will no longer be statistically meaningful. The default bin size is 0.3 (30% of the region points.)

```--binstep```
Step of the sliding window for the calculation of the z-score, as a ratio of the window size selected. Closer to 0 values will provide better precision, but slower performance, closer to 1 less precision but faster calculation. Default slide is 0.1 (10% of the total regions)

#### Total reads normalization (--n-norm)

This normalization will calculate the number of reads per million reads in each region and sample. This is a very simple normalization that tries to correct the bias of comparing different samples by total number of reads. You can activate it with the ```--n-norm flag```.

Example. Using 2 reads files, calculate the enrichment normalizing by N:
```
pyicoenrich -reads kidney1.bed liver1.bed -output n_norm.enrich -f bed --region genes.bed --n-norm
```
If you want to skip the total reads calculation step, you can provide the total number of reads with the following flags.

```--total-reads-a```

```--total-reads-b```

```--total-reads-replica```

Example:
```
pyicoenrich -reads kidney1.bed liver1.bed -output n_norm.enrich -f bed --region genes.bed --n-norm --total-reads-a 120000 --total-reads-b 110000

```

#### Region length normalization (--len-norm)

Calculates the number of reads per region kilobase. It aims to correct for regions with different lengths.
NOTE: If possible, try not to mix regions with different lengths.

```
pyicoenrich -reads kidney1.bed liver1.bed -output n_norm.enrich -f bed --region genes.bed --n-norm
```

#### RPKM (--len-norm and --n-norm)

RPKM normalization is the combination of both ```--n-norm``` and ```--len-norm```:

```
pyicoenrich -reads kidney1.bed liver1.bed -output rpkm_norm.enrich -f bed --region genes.bed --n-norm --len-norm

```

#### Trimmed Means of M values normalization (--tmm-norm)

As proposed in EdgeR (Robinson et al. 2010), this calculates the weighted trimmed mean of the log expression ratios (trimmed mean of M values (TMM)). It is based on the hypothesis that most of your regions do not change, and calculates a normalization factor by excluding the total amount of data.

Important flags for the TMM:

``` --a-trim```
Proportion of A values to be discarded when doing the TMM normalization. [Default 0.05]

```--m-trim```
Proportion of M values to be discarded when doing the TMM normalization. [Default 0.25]

Example: TMM normalization calculated discarding the 20% smaller A (less read coverage) and 5% of the regions with the biggest differences (up and down):
```
pyicoenrich -reads kidney1.bed liver1.bed -output rpkm_norm.enrich -f bed --region genes.bed --tmm-norm --a-trim 0.2 --m-trim 0.05
```

#### Full quantile normalization 

```--quant-norm``` This method is suitable when your samples have too much variability

## Input Counts file ##

You can provide pyicoenrich a count file instead of read files. The required file is a text file with the following columns:

```
1.  name           =  region label (chromosome, gene name, transcript...)
2.  start           =  region start coordinates
3.  end             =  region end coordinates
4.  name2          =  alternative label
5.  score          =  Reserved by a "." as it is used by the UCSC browser for coloring.
6.  strand         =  region strand. Options are "+" for positive strand, "-" for negative strand and "." for none
7.  signal_a       =  Counts in experiment A (normalized)
8.  signal_b       =  Counts in experiment B (normalized)
9.  signal_prime_1 =  Counts in experiment A (exactly the same as signal_a) or random background 1 (normalized)
10. signal_prime_2 =  Counts in experiment replica A or random background 2 (normalized if used)
```

## Output Counts file ## 

The output of pyicoenrich will look as follows (Column description of enrichment result where each line describes a region):

```
Columns 1-10 are as above.
11. A                      =  (log2(signal_a)+log2(signal_b))/2
12. M                      =  log2(signal_a/signal_b)
13. total_reads_a          =  total number of reads in sample a
14. total_reads_b          =  total number of reads in sample b
15. num_tags_a             =  number of reads in sample a overlapping the region
16. num_tags_b             =  number of reads in sample b overlapping the region
17. A_prime                =  (log2(signal_prime_1)+log2(signal_prime_2))/2
18. M_prime                =  log2(signal_prime_1/signal_prime_2)
19. total_reads_a          =  total number of reads in sample a
20. total_reads_b          =  total number of reads in sample b
21. total_reads_prime_1    =  total number of reads in sample prime 1
22. total_reads_prime_2    =  total number of reads in sample prime 2
23. A_median               =   median of A values in window
24. mean                   =   mean of M_prime values in window
25. sd                     =   standard deviation of M_prime values in window
26. zscore                 =  score for the significance of the difference of enrichment between condition a and b compared to prime 1  and prime 2
```

(go to [Table of Contents](#markdown-header-table-of-contents))

--------------------------------
# Pyicoclip#

## Introduction ##

Pyicoclip is an implementation of the modified False Discovery Rate algorithm proposed in (Yeo et al.2009) to determine which clusters are significant in a list of genomic regions (like genes or transcripts). This method is typically used in CLIP-Seq data and doesn’t need a control experiment to compare against. This method could in principle be used for any other kind of short-read data that doesn’t have a valid control. You can provide your own region file, or otherwise you can provide a --region-magic description with a GTF file (see below )

## Basic usage ##

pyicoclip usage requires the experiment CLIP-Seq file (eland, SAM, BAM, BED formats) and a region file in BED format. Default output format is the **bedpk** format, our extended but compatible with BED format (see description above)

**Example:**
```
pyicoclip my_experiment.bed output.pk -f bed --region my_regions.bed
```
This command will output 2 files: *output.pk* and *unfiltered_output.pk*. In *output.pk* you will get the significant peaks after applying the method an the significant p-value, in the unfiltered one you will get the unfiltered peaks with 2 or more overlapping reads. Single reads are omitted from the output.

## Important flags

This is a description of the most important flags. To see the complete list of flags, type 
```
pyicoclip -h
```
to get the full list.

```--stranded```: You will probably want to use the –stranded flag, in order to take into consideration reads only overlapping with the strand of the regions of interest (6th column of your BED6 region file):
```
pyicoclip my_experiment.bed output.pk -f bed --region my_regions.bed **``--stranded``**
--p-value
```
The threshold to make it to the significant peaks list. Default is 0.01

**--region-magic and --gtf-file**

You can automatically generate exploratory region files using the ```--region-magic``` and ```--gtf-file``` flags. For example, explore the regions 500 bases upstream and 1000 downstream of TSS:
```
pyicoclip my_experiment.bed output.pk -f bed --region my_regions.bed --gtf-file myref.gtf --region-magic tss -500 1000
```
Check all genes:
```
pyicoclip my_experiment.bed output.pk -f bed --region my_regions.bed --gtf-file myref.gtf --region-magic genebody
```
See [**pyicoregion**](#markdown-header-pyicoregion) documentation above for more details on how to use ```--region-magic``` flag.

## Full CLIP analysis ##

Starting from a SAM file with mapped reads from a CLIP experiment, we can perform CLIP analysis with pycoclip. However, before that, it is convenient to do some filters to the data (see  [pyicos](#markdown-header-pyicos) description below)

We convert to bed format:
```
pyicos convert clip_sorted.sam clip.bed -f sam -F bed
```
Extend reads (5'to 3') all to have same length: 
```
pyicos extend  clip.bed clip_ext.bed 35 -f bed -F bed
```
Remove reads falling in repeats and/or small non-coding RNAs (in regions.bed):
```
pyicos remregions clip_ext.bed regions.bed clip_ext_norepeats_nosRNAs.bed --experiment-format bed --region-format bed --output-format bed
```
Perform the pyicoclip analysis using gene regions (genes.bed):
```
pyicoclip clip_ext_norepeats_nosRNAs.bed clip_clusters.bedpk -f bed --region genes.bed --stranded
```
That's it. The bedpk file contains the clusters, the profile information, and the significance p-value. You can convert it to wig or bed for visualization using [pyicos](#markdown-header-pyicos).

(go to [Table of Contents](#markdown-header-table-of-contents))

------------------------------
# Pyicos #

Pyicos is a command line utility for the conversion and manipulation of genomic coordinates files. It follows a command/sub-command structure. In the interactive help you can visualize the available commands list:
```
pyicos -h
```
If you are interested in the usage of a particular command (for example, ‘extend’) and the meaning of its flags type:
```
pyicos extend -h
```
Here we explain briefly what each subcommand does and we give some examples:

## convert

Converting a file from one format to another format. Currently supported formats are:

**experiment:** Bed, Wiggle files (bed_wiggle), SAM, BAM, Eland, bedpk (Pyicos default compressed format), bedspk (Pyicos stranded compressed format)

**output:** Bed, Wiggle files (bed_wiggle, variable_wiggle), SAM, BAM, Eland, bedpk (Pyicos default compressed format)

This operation is useful if you only want to convert your data to another format. Other operations already include a conversion if you specify different experiment and output formats. Examples: 
Convert a bed file to a half-open variable wig file:
```
pyicos convert my_experiment.bed my_experiment.wig -f bed -F variable_wig -O
```
Convert all pk files in a folder to bed wig files:
```
pyicos convert my_experiment_folder/ outputfolder/ -F bed_wig
```

## remregions
Remove regions that overlap with the regions in the “black list” file. Example:
```
pyicos remregions my_experiment.bed regions.bed my_result.bed --experiment-format bed --open-experiment --region-format bed --open-region --output-format bed --open-output
```

## remduplicates
Remove the duplicated reads in a file. A duplicate is a read with the same start position as a read that has already been seen. You can choose how many duplicates you want to tolerate. If you want to keep only one read for a start position, set the duplicates to 0. Example (Here we tolerate 1 duplicate so a read can not occur more often than twice):
```
pyicos remduplicates my_experiment.bed my_experiment_1dupl.bed --duplicates 1 -f bed -o -F bed
```

## strcorr (Strand Correlation)
Finds the optimal extension value by finding the “gap” between groups of positive and negative cluster of reads by performing a pearson correlation test.

## extend
Extend the reads to the estimated fragment length, taking into consideration if they map to the forward or reverse strand of the reference genome.

![Extend.png](https://bitbucket.org/repo/zMEkqB/images/1168858242-Extend.png)

Example:We have a bed file (half open) with reads between 30 and 50 nucleotides long. We want to extend them all to 150 nucleotides and write the output in bedpk-format to accelerate the successive operations:
```
pyicos extend my_experiment.bed my_experiment_ext.bedpk 150 -f bed -o
```
We do the same with the control:
```
pyicos extend control.bed control_ext.bedpk 150 -f bed -o
```
To visualize the data in a genome browser we set the output to be half-open bed_wig:
```
pyicos extend my_experiment.bed my_experiment_ext.bed_wig 150 -f bed -o -F bed_wig -O
```

## subtract
Subtract the reads in one file from the reads in another file. Using background data (control) improves the results because the background distribution is not supposed to be normal, and statistical approaches to obtain this have a limited reach. The most straightforward approach is to subtract the control from the sample. **IMPORTANT:** Make sure the sample has been normalized to the control beforehand. *Pyicos* maintains a 1bp resolution by subtracting the reads nucleotide by nucleotide, rather than doing a statistical approximation. Operating with directories will only give appropriate results if the files and the control are paired in alphabetical order.

Example: Subtract the control from the experiment (both have already been extended, converted to bedpk and normalized):
```
pyicos subtract my_experiment_ext_norm.bedpk control_ext.bedpk my_experiment_ext_norm_subtr.bedpk
```

## split
Split peaks into subpeaks in case they fulfill the criteria. Criteria: peak has at least two neighboring maxima between which the coverage of reads falls below the threshold. The threshold can be set by the user and it reflects a proportion of the lower maximum. Output: bedpk or Wiggle files

![Split.png](https://bitbucket.org/repo/zMEkqB/images/81884610-Split.png)


Example:
```
pyicos split peaks.bedpk peaks_split.bedpk --split-proportion 0.9
```

## discard
Discards peaks that look like artifacts due to the sequencing bias. Here we refer to peaks that look like blocks that result from duplicates (reads with the same start position). Output: bedpk or Wiggle files

![Artifact.png](https://bitbucket.org/repo/zMEkqB/images/383546505-Artifact.png)
_images/Artifact.png

Example:
```
pyicos discard peaks.bedpk peaks_discA.bedpk
```

## filter
Detect and select significant clusters in the file. There are two steps in this operation: Through the poisson operation the thresholds are determined. Next the peaks are filtered according to these thresholds. Output: bedpk or Wiggle files

![Filter.png](https://bitbucket.org/repo/zMEkqB/images/3697806603-Filter.png)
_images/Filter.png

Example:
```
pyicos filter peaks.bedpk significant_peaks.bedpk 150
```

## push
Push the reads in the corresponding strand (rather than extending them). This is useful for nucleosome analysis. If a read doesn’t have a strand, it will be pushed from left to right. This operation requires tag-like files (bed, eland, sam). Example:
```
pyicos push my_experiment.bed my_experiment_pushed100.bed 100 -f bed -F bed
```

(go to [Table of Contents](#markdown-header-table-of-contents))

------------------------------
#Pyicoller#

This function is a peak caller, which works as a combination of some of Pyicos commands (extend, normalize, subtract, remove, poisson and filter) for the task of calling peaks from a ChIP-Seq experiment (with narrow peaks). A control file is optional but recommended. Example:
```
pyicoller my_experiment.bed significant_peaks.bedpk -f bed -o --control control.bed --control-format bed --open-control --region regions_to_be_removed.bed --remlabels chrY --correction 0.8 --k-limit 20 --p-value 0.001 -x 130
```
## Important flags ##
This is a description of the most important flags. To see the complete list of flags, type pyicoller -h to get the full list.

####--species SPECIES
The species assembly that you are analyzing. This will read the length of the chromosomes of this species from the files inside the folder *chrdesc*. If the species information is not known, **pyicoller** will try to connect to the UCSC database and download it. [Default hg19]

####--duplicates DUPLICATES
The number of duplicated reads allowed by the algorithm. Any duplicated read after this threshold will be discarded. By default, no duplicated reads are allowed.
```
-x FRAG_SIZE, --frag-size FRAG_SIZE
```
The estimated inmunoprecipitated sequence fragment size. This is used by **Pyicoller** to reconstruct the original signal. If not specified, a *strcorr* (Strand Correlation) test will be performed in order to estimate the fragment length.

####--poisson-test POISSON_TEST
Decide what property of the read clusters will be used for the poisson analysis. 
- *--poisson-test height*  will use the summit of the peaks, 
- *--poisson-test numtags* the total number of reads and 
- *--poisson-test length* the length occupied by the peaks. Default is height

(go to [Table of Contents](#markdown-header-table-of-contents))

-------------------------------
#Pyicount #

Read from 1 to N read files and output the counts of the files.

###Examples

Count all reads in files 1 to 5 inside the last introns of every gene. Reads are considered overlapping only if they are half in the intronic region (0.5)
```
pyicount file1.sam file2.sam file3.sam file4.sam file5.sam -f sam --region-magic intron last --gff-file genodeV11.bed --overlap 0.5
```
Count all reads in files 1 to 2 inside the custom region file. Reads have to be fully inside the region.
```
pyicount file1.bed file2.bed file3.bed  -f bed --region my_regions.bed --overlap 1
```

(go to [Table of Contents](#markdown-header-table-of-contents))

--------------------------------------------------
#Pyicotrocol#

Pyicotrocol offers access to the whole functionality of **Pyicoteo**, allowing for combination of operations in the different Pyicoteo suite tools. It also has the nice advantage in terms of experiment reproducibility, since you don’t need to save the command you used, what you did stays in the text file. 

To run a protocol file type:
```
pyicotrocol my_protocol.ini
```

##Syntax

Pyicotrocols files are based on the [.ini format](http://en.wikipedia.org/wiki/INI_file)

##Examples

This example is equivalent of using pyicoller (To determine significant peaks; recommended for transcription factors)

```
[Pyicotrocol]

; your files:
experiment          = my_experiment.bed
control             = control.bed
region              = regions_to_be_removed.bed

; format of all files:
experiment_format   = bed
control_format      = bed
region_format       = bed

; half-open?
open_experiment     = true
open_control        = true
open_region         = true

; your result:
output              = significant_peaks.bedpk

; operations that will be applied:
operations          = remove_duplicates, remove, extend, normalize, subtract, trim, poisson, filter, split

; flags to specify what the operations should do:
duplicates=0            ; Number of duplicates that are tolerated
frag_size=150           ; Estimated fragment size
correction=0.8          ; Fraction of the genome that is mappable
trim_proportion=0.1     ; Fraction of the cluster height below which the peak is trimmed
split_proportion=0.9    ; Fraction of the lower maximum; if the read coverage between two maxima falls below it the peak will be split
height_limit=100        ; After this value the poisson calculation will not assign lower p-values to the peaks anymore
```

(go to [Table of Contents](#markdown-header-table-of-contents))


-------------------------------
#pyicoteolib core#

Pyicoteolib is the library and the building blocks of the Pyicoteo suite. The pyicoteolib.core library contains the main holders of data in two main objects: ReadCluster and ReadRegion.

##ReadCluster

A ReadCluster object may contain one read or a group of overlapping reads. It can read both tag like (bed, sam, bam..) and histogram like (wig, bed_pk...) formats. Instances of the ReadCluster object can be added, compared, subtracted to other readCluster objects with standard python syntax.

The ReadCluster object is optimized in order to deal with millions of overlaps, and has been tested with multiple different HTS datasets. The optimization consists in 2 main principles:

All the following standard python operators are supported:

####Adding

Adding combines the signal of 2 different ReadClusters, with nucleotide precision:
```
#!python
from pyicoteolib.core import ReadCluster, PK

cluster1 = ReadCluster(read=PK)
cluster2 = ReadCluster(read=PK)
cluster1.read_line('chr1 1 45 9:2.00|41:3.00|50:2.00|45:1.00')
cluster2.read_line('chr1 1 125 9:4.00|41:3.00|30:2.00|45:1.00')
result = cluster1 + cluster2

result.write_line()

"chr1    1   145 50:6.00|30:4.00|20:3.00|25:2.00|20:1.00 6.0 .   25  550.0"
```

####Subtracting

Substracts the signal of 2 different ReadClusters, with nucleotide precision:
```
#!python

from pyicoteolib.core import ReadCluster, SAM, PK

cluster1 = ReadCluster(read=SAM)
cluster2 = ReadCluster(read=PK)
cluster1.read_line('SL-XAJ_1_FC305HJAAXX:2:21:872:1402  0   chr1    1   50  36M *   0   0   AAAAGGGGGAATAAAAAGTAACCCAAAACTAACTAT    <<<,7<<<<<7<1:71)<+51<+<5(75()1344+2    PG:Z:FC_305HJAAXX_ln_2.dat')
cluster2.read_line('chr1 1 125 9:4.00|41:3.00|30:2.00|45:1.00')
result = cluster2 - cluster1

result.write_line()
```

####Length

Returns the length of the read cluster:
```
#!python

c = Cluster(name="chrX", start=1, end=10000)
len(c)

10000
```

####Comparison operators (< > == !=)

This indicates which read cluster is before another in a coordinate system:
```
#!python

c1 = Cluster(name="chr1", start=100, end=1000)
c1_copy = Cluster(name="chr1", start=100, end=1000)
c2 = Cluster(name="chr1", start=50000, end=100000)
c1 > c2
False
c1 == c1_copy
True

```

###Examples

Read a .bed file, print the length andd the area of each cluster of overlapping reads:
```
#!python

from pyicoteolib.core import ReadCluster, BED

bed_file = open("/path/to/myfile.bed")

for line in bed_file:
    rc = ReadCluster(read_as=BED)
    rc.read_line(l)
    print len(rc), rc.area()

```

Read some .bed lines, cluster them, output a wiggle file:
```
#!python

from pyicoteolib.core import ReadCluster

cluster =  ReadCluster(read="bed", write="bed_wig")
cluster.read_line('chr1 1 20000 id1 0 +')
cluster.read_line('chr1 1 20000 id2 0 +')
cluster.read_line('chr1 1 20000 id3 0 +')
cluster.read_line('chr1 1001 20000 id4 0 +')
cluster.write_line()

"chr1    1       1000    3.00"
"chr1    1001    20000   4.00"

```

##Useful functions

####extend(self, extension)

Extends the coordinates covered by a read in the strand direction:

```
#!python

from pyicoteolib.core import ReadCluster

minus_read = ReadCluster(read="bed", write="bed")
minus_read.read_line('chr1 1000 2000 id1 0 -')
minus_read.extend(30)
minus_read.write_line()

"chr1 970 2000 id1 0 -"
```

####push(push_distance)

Push the read coordinates in the strand direction:
```
#!python

from pyicoteolib.core import ReadCluster

minus_read = ReadCluster(read="bed", write="bed")
minus_read.read_line('chr1 1000 2000 id1 0 -')
minus_read.push(30)
minus_read.write_line()

"chr1 970 1970 id1 0 -"

```

####trim(ratio=0.3, absolute=0)

Trim the borders of an histogram given a theshold. The ratio parameter indicates the ratio to trim counting from the top height. The absolute parameter overrides the ratio, giving a fixed height were to trim:
```
#!python

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
```

####split(ratio=0.9, absolute=0)

This function scans each histogram position from start to end and looks for local maxima x and local minima y. Given two consecutive local maxima *x(i)* and *x(i+1)*, let's call the smallest of them as *x(min)*. The we calculate the minimum among the points *y(j)* between two local maxima. The minima *y* that define a possible split point is calculated as (see pyicos function [split](#markdown-header-split) above):

**y(j)/(x(min)*(1-t))**

Where t is a ratio between 0 and 1. By default t=0.05. The cluster will divide at the local minimum:

```
#!python

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
```

####is_artifact()

Returns True if the read histogram looks like a punctuated ChIP-Seq artifact, returns False otherwise. A ReadCluster is considered artifactual if it is shorter than 100 nucleotides or the maximum height takes more than is more than 30% of the cluster:


```
#!python

from pyicoteolib.core import ReadCluster

art = ReadCluster(read="bed")
art.read_line("chr1 1 200 repeat 0 +")
art.read_line("chr1 1 200 repeat 0 +")
art.read_line("chr1 1 200 repeat 0 +")
art.read_line("chr1 1 200 repeat 0 +")

art.is_artifact()

True

```

####is_empty()

Returns True if the ReadCluster contains no reads, returns False otherwise:


```
#!python

from pyicoteolib.core import ReadCluster

c = ReadCluster(read="bed")
c.is_empty()

True

c.read_line("gene1  10000 120000 ")

c.is_empty()

False

```

##ReadRegion

A ReadRegion object holds a genomic region that may contain ReadClusters.


(go to [Table of Contents](#markdown-header-table-of-contents))

-------------------------------------
#pyicoteolib utils#

The *pyicoteolib.utils* modules contains several file manipulation classes optimized for minimal working memory usage while working with huge sequential text files.

##SortedFileReader

Holds a cursor and a file path. Given a start and an end, it iterates through the file starting on the cursor position, and yields the clusters that overlap with the region specified. The cursor will be left behind the position of the last region fed to the SortedFileReader.

Important functions of the instances:
```
#!python

def rewind(self):
    """Move back to initial cursor of the file"""

def _read_line(self):
    """Reads the next line of the file. If advance, the cursor will get the position of the file"""

def overlapping_clusters(self, region, overlap=1):
    """Returns clusters of reads overlapping with the given region. Sufficient overlap between reads and regions is defined by the overlap parameter."""

```

##SortedFileCountReader

Holds a cursor and a file path. Given a start and an end, it iterates through the file starting on the cursor position, and retrieves the counts (number of reads) that overlap with the region specified. Because this class doesn’t store the reads, but only counts them, it doesn’t have memory problems when encountering huge clusters of reads.

##BigSort
This class can sort huge files without loading them fully into memory. It divides the files smaller files, sorts them and then merges them.

Important functions:


```
#!python

def __init__(self, file_format, read_half_open=False, frag_size=0, id=0, logger=True, filter_chunks=True, push_distance=0, buffer_size = 320000, temp_file_size = 8000000):
    """
    Constructor. Sorting buffer and file size is configurable through the ``buffer_size`` and ``temp_file_size`` parameters respectively. For optimization reason, this class can also preprocess the reads, applying extend or displacement (push) of reads while sorting. The sorting format defines how the sorting will be performed.
    """

def sort(self, input, output=None, key=None, tempdirs=[]):
    """
    Key parameter defines the lambda function for sorting. A list of temporary directories can be provided for the sorting algorithm to use through the tempdirs parameter.
    """

```

Example of sorting a huge SAM file:


```
#!python

from pyicoteolib.utils import BigSort

unsorted_file = open("/path/to/file/unsorted.sam")
#we want to sort and extend the reads 1000 bp in strand direction at the same time
sorter = utils.BigSort("sam", False, frag_size=0, 'fisort%s'%temp_name, logger=self.logger)
sorted_file = sorter.sort(old_path, None, utils.sorting_lambda(file_format))

```

(go to [Table of Contents](#markdown-header-table-of-contents))

-------------------------------
#Credits and Support#

**Developers:** Juan González-Vallinas, with some contributions from Ferran Lloret
**Testing and benchmarking:** Sonja Althammer, Eneritz Agirre, Nuria Conde Pueyo, Juan González-Vallinas, Eduardo Eyras
**Questions and Support:** eduardo.eyras@upf.edu