Optional arguments
==================


trim
----

.. cmdoption:: --help, -h

    Show this help message and exit


.. cmdoption:: -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption:: -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption:: --debug


.. cmdoption:: --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption:: --force-sort

    Force the sorting step


.. cmdoption:: --silent

    Run without printing in screen


.. cmdoption:: --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption:: --keep-temp

    Keep the temporary files


.. cmdoption:: --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption:: --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption:: --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption:: --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption:: --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption:: --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption:: --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption:: -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption:: -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption:: --round

    Round the final results to an integer


.. cmdoption:: --trim-absolute TRIM_ABSOLUTE

    The height threshold to trim the clusters. Overrides the trim proportion. [Default 0]


.. cmdoption:: --trim-proportion TRIM_PROPORTION

    Fraction of the cluster height below which the peak is trimmed. Example: For a cluster of height 40, if the flag is 0.05, 40*0.05=2. Every cluster will be trimmed to that height. A position of height 1 is always considered insignificant, no matter what the cluster height is. [Default 0.3]


.. cmdoption:: --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption:: --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN


.. cmdoption:: --span SPAN

    The span of the variable and fixed wig formats [Default 40]





plot
----

.. cmdoption:: -h, --help

    show this help message and exit


.. cmdoption:: --debug


.. cmdoption:: --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption:: --force-sort

    Force the sorting step


.. cmdoption:: --silent

    Run without printing in screen


.. cmdoption:: --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease


.. cmdoption:: --keep-temp

    Keep the temporary files


.. cmdoption:: --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption:: --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption:: --label1 label1

    Manually define the first label of the graphs.


.. cmdoption:: --label2 label2

    Manually define the second label of the graphs.


.. cmdoption:: --tempdir tempdir

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption:: --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption:: --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption:: --zscore zscore

    Significant Z-score value. [Default 2]




convert
-------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption::  --tag-length TAG_LENGTH

    The tag length, or the extended one. Needed when converting from a Clustered format (wig, pk) to a non clustered format (bed, eland) [Default 0]


.. cmdoption::  --span SPAN

    The span of the variable and fixed wig formats [Default 40]


.. cmdoption::  -x FRAG_SIZE, --frag-size FRAG_SIZE

    The estimated inmmunoprecipitated fragment size. This is used by Pyicos to reconstruct the original signal in the original wet lab experiment.


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN





remduplicates
-------------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --duplicates DUPLICATES

    The number of duplicated reads accept will be counted.  Any duplicated read after this threshold will be discarded. [Default 0]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --span SPAN

    The span of the variable and fixed wig formats [Default 40]


.. cmdoption::  --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN





enrichma
--------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --replica REPLICA

    Experiment A replica file


.. cmdoption:: --region-format REGION_FORMAT

    The format the region file is written as. [default bed]


.. cmdoption::  --open-region

    Define if the region file is half-open or closed notation. [Default closed]


.. cmdoption::  --stranded

    Decide if the strand is taken into consideration for the analysis. This requires a region file in bed format with the strand information in its 6th column.


.. cmdoption::  --proximity PROXIMITY

    Determines if two regions calculated automatically are close enough to be clustered. Default 50 nt


.. cmdoption::  --binsize BINSIZE

    The size of the bins to calculate the local sd and mean for the background model, as a ratio of total number or regions. Regardless of the ratio selected, the minimum window size is 50 regions, since below that threshold the results will no longer be statistically meaningful. [Default 0.3]


.. cmdoption::  --sdfold SDFOLD

    The standard deviation fold used to generate the background model. [Default 1]


.. cmdoption::  --recalculate

    Recalculate the z-score when plotting. Useful for doing different plots with 'pyicos plot' [Default False]


.. cmdoption::  --mintags MINTAGS

    Number of tags (of the union of the experiment and experiment_b datasets) for a region to qualify to be analyzed. [Default 6]


.. cmdoption::  --binstep BINSTEP

    Step of the sliding window for the calculation of the z-score, as a ratio of the window size selected. If you want max precision, in the zscore calculation. You can set this value to 0 in order to use a sliding window that slides only 1 region at a time, but if you have many regions the calculation can get very slow.  [Default 0.1]


.. cmdoption::  --skip-plot

    Skip the plotting step. [Default False]


.. cmdoption::  --n-norm

    Divide the read counts by the total number of reads (units of million reads)


.. cmdoption::  --len-norm

    Divide the read counts by region (gene, transcript...) length (reads per kilobase units)


.. cmdoption::  --zscore ZSCORE

    Significant Z-score value. [Default 2]




extend
------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption::  --span SPAN

    The span of the variable and fixed wig formats [Default 40]


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN




remregions
----------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption:: --region-format REGION_FORMAT

    The format the region file is written as. [default bed]


.. cmdoption::  --open-region

    Define if the region file is half-open or closed notation. [Default closed]


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN





modfdr
------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --p-value P_VALUE

    The threshold p-value that will make a cluster significant. [Default 0.01]


.. cmdoption::  --repeats REPEATS

    Number of random repeats when generating the "background" for the modfdr operation[Default 100]


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN





filter
------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --p-value P_VALUE

    The threshold p-value that will make a cluster significant. [Default 0.01]


.. cmdoption::  --k-limit K_LIMIT

    The k limit Pyicos will analize to when performing a poisson test. Every cluster that goes over the threshold will have a p-value of 0, therefore considered significant. For performance purposes, raising it will give more precision when defining low p-values, but will take longer to execute. [Default 400]


.. cmdoption::  --correction CORRECTION

    This value will correct the size of the genome you are analyzing. This way you can take into consideration the real mappable genome [Default 1.0]


.. cmdoption::  --threshold THRESHOLD

    The height threshold used to cut


.. cmdoption::  -p SPECIES, --species SPECIES

    The species that you are analyzing. This will read the length of the chromosomes of this species from the files inside the folder "chrdesc". If the species information is not known, the filtering step will assume that the chromosomes are as long as the position of the furthest read. [Default hg19]


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN


.. cmdoption::  --poisson-test POISSON_TEST

    Decide what property of the cluster will be used for the poisson analysis. Choices are ('height', 'numtags', 'length') [Default height]





strcorr
-------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --max-delta MAX_DELTA

     Maximum distance to consider when correlating the positive and the negative groups of reads [Default 250]


.. cmdoption::  --min-delta MIN_DELTA

    Minimum distance to consider when correlating the positive and the negative groups of reads [Default 20]


.. cmdoption::  --height-filter HEIGHT_FILTER

    The minimum number of overlapping reads in a cluster to include it in the test [Default 8]


.. cmdoption::  --delta-step DELTA_STEP

    The step of the delta values to test [Default 1] --max-correlations MAX_CORRELATIONS The maximum pairs of clusters to analyze before considering the test complete. Lower this parameter to increase time performance [Default 200]


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN





split
-----

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --split-proportion SPLIT_PROPORTION

    Fraction of the cluster height below which the cluster is splitted. [Default 0.1]


.. cmdoption::  --split-absolute SPLIT_ABSOLUTE

    The height threshold to split the clusters. [Default 0]


.. cmdoption::  --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN



poisson
-------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  -x FRAG_SIZE, --frag-size FRAG_SIZE

    The estimated inmmunoprecipitated fragment size. This is used by Pyicos to reconstruct the original signal in the original wet lab experiment.


.. cmdoption::  --p-value P_VALUE

    The threshold p-value that will make a cluster significant. [Default 0.01]


.. cmdoption::  --k-limit K_LIMIT

    The k limit Pyicos will analize to when performing a poisson test. Every cluster that goes over the threshold will have a p-value of 0, therefore considered significant. For performance purposes, raising it will give more precision when defining low p-values, but will take longer to execute. [Default 400]


.. cmdoption::  --correction CORRECTION

    This value will correct the size of the genome you are analyzing. This way you can take into consideration the real mappable genome [Default 1.0]


.. cmdoption::  -p SPECIES, --species SPECIES
    The species that you are analyzing. This will read the length of the chromosomes of this species from the files inside the folder "chrdesc". If the species information is not known, the filtering step will assume that the chromosomes are as long as the position of the furthest read. [Default hg19]


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN


.. cmdoption::  --poisson-test POISSON_TEST

    Decide what property of the cluster will be used for the poisson analysis. Choices are ('height', 'numtags', 'length') [Default height]




discard
-------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --span SPAN

    The span of the variable and fixed wig formats [Default 40]


.. cmdoption::  --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN





push
----

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption::  --span SPAN

    The span of the variable and fixed wig formats [Default 40]


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN




callpeaks
---------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  --control CONTROL

    The control file or directory


.. cmdoption::  --control-format CONTROL_FORMAT

    The format the control file is written as. [default: The same as experiment format]


.. cmdoption::  --open-control

    Define if the region file is half-open or closed notation. [Default closed]


.. cmdoption::  --blacklist BLACKLIST

    Reads a bed file with coordinates that you want to exclude from the analysis. Useful for discarding "noisy" probable artifactual regions like centromeres and repeat regions. [Default None]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  -x FRAG_SIZE, --frag-size FRAG_SIZE

    The estimated inmmunoprecipitated fragment size. This is used by Pyicos to reconstruct the original signal in the original wet lab experiment.


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption::  --span SPAN

    The span of the variable and fixed wig formats [Default 40]


.. cmdoption::  --no-subtract

    Don't subtract the control to the output, only normalize.


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN


.. cmdoption::  --p-value P_VALUE

    The threshold p-value that will make a cluster significant. [Default 0.01]


.. cmdoption::  --k-limit K_LIMIT

    The k limit Pyicos will analize to when performing a poisson test. Every cluster that goes over the threshold will have a p-value of 0, therefore considered significant. For performance purposes, raising it will give more precision when defining low p-values, but will take longer to execute. [Default 400]


.. cmdoption::  --correction CORRECTION

    This value will correct the size of the genome you are analyzing. This way you can take into consideration the real mappable genome [Default 1.0]



.. cmdoption::  --trim-proportion TRIM_PROPORTION

    Fraction of the cluster height below which the peak is trimmed. Example: For a cluster of height 40, if the flag is 0.05, 40*0.05=2. Every cluster will be trimmed to that height. A position of height 1 is always considered insignificant, no matter what the cluster height is. [Default 0.3]


.. cmdoption::  -p SPECIES, --species SPECIES

    The species that you are analyzing. This will read the length of the chromosomes of this species from the files inside the folder "chrdesc". If the species information is not known, the filtering step will assume that the chromosomes are as long as the position of the furthest read. [Default hg19]


.. cmdoption::  --duplicates DUPLICATES

    Decide what property of the cluster will be used for the poisson analysis. Choices are ('height', 'numtags', 'length') [Default height]


.. cmdoption::  --poisson-test POISSON_TEST

    Decide what property of the cluster will be used for the poisson analysis. Choices are ('height', 'numtags', 'length') [Default height]




checkrepcount
-------------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  --stranded

    Decide if the strand is taken into consideration for the analysis. This requires a region file in bed format with the strand information in its 6th column.


.. cmdoption::  --proximity PROXIMITY

    Determines if two regions calculated automatically are close enough to be clustered. Default 50 nt


.. cmdoption::  --binsize BINSIZE

    The size of the bins to calculate the local sd and mean for the background model, as a ratio of total number or regions. Regardless of the ratio selected, the minimum window size is 50 regions, since below that threshold the results will no longer be statistically meaningful. [Default 0.3]


.. cmdoption::  --sdfold SDFOLD

    The standard deviation fold used to generate the background model. [Default 1]


.. cmdoption::  --recalculate

    Recalculate the z-score when plotting. Useful for doing different plots with 'pyicos plot' [Default False]


.. cmdoption::  --mintags MINTAGS

    Number of tags (of the union of the experiment and experiment_b datasets) for a region to qualify to be analyzed. [Default 6]


.. cmdoption::  --binstep BINSTEP

    Step of the sliding window for the calculation of the z-score, as a ratio of the window size selected. If you want max precision, in the zscore calculation. You can set this value to 0 in order to use a sliding window that slides only 1 region at a time, but if you have many regions the calculation can get very slow.  [Default 0.1]


.. cmdoption::  --skip-plot

    Skip the plotting step. [Default False]


.. cmdoption::  --n-norm

    Divide the read counts by the total number of reads (units of million reads)


.. cmdoption::  --len-norm

    Divide the read counts by region (gene, transcript...) length (reads per kilobase units)


.. cmdoption::  --total-reads-a TOTAL_READS_A

    To manually set how many reads the dataset in 'experiment' has. If not used, it will be counted from the read or counts file. Default (automatically calculated from reads or counts files)


.. cmdoption::  --total-reads-b TOTAL_READS_B

    To manually set how many reads the dataset in 'experiment_b' has. If not used, it will be counted from the read or counts file. Default (automatically calculated from reads or counts files)


.. cmdoption::  --total-reads-replica TOTAL_READS_REPLICA

    To manually set how many reads the dataset in 'experiment_replica' has. If not used, it will be calculated from the read or the counts file. Default 0 (not used)


.. cmdoption::  --a-trim A_TRIM

    Proportion of A values to be discarded when doing the TMM normalization. Only applied when combined with --tmm-norm. [Default 0.05]


.. cmdoption::  --m-trim M_TRIM

    Proportion of M values to be discarded when doing the TMM normalization. Only applied when combined with --tmm-norm. [Default 0.25]


.. cmdoption::  --experiment-label EXPERIMENT_LABEL

    The label that will identify the experiment file in the "check replicas" plot


.. cmdoption::  --replica-label REPLICA_LABEL

    The label that will identify the experiment file in the "check replicas" plot


.. cmdoption::  --title-label TITLE_LABEL

    The label that will identify the experiment file in the "check replicas" plot


.. cmdoption::  --count-filter COUNT_FILTER

    Filter the points that go below a threshold to better visualize the correlation between the replicas




subtract
--------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  --control-format CONTROL_FORMAT

    The format the control file is written as. [default: The same as experiment format]

.. cmdoption::  --open-control

    Define if the region file is half-open or closed notation. [Default closed]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --round

    Round the final results to an integer


.. cmdoption::  --normalize

    Normalize to the control before subtracting


.. cmdoption::  --tag-length TAG_LENGTH

    The tag length, or the extended one. Needed when converting from a Clustered format (wig, pk) to a non clustered format (bed, eland) [Default 0]


.. cmdoption::  --span SPAN

    The span of the variable and fixed wig formats [Default 40]


.. cmdoption::  --wig-label WIG_LABEL

    The label that will identify the experiment in the WIG tracks.


.. cmdoption::  --remlabels REMLABELS

    Discard the reads that have this particular label.  Example: --discard chr1 will discard all reads with chr1 as tag. You can specify multiple tags to discard using the following notation --discard chr1 chr2 tagN





enrichment
----------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  -o, --open-experiment

    Defines if the experiment is half-open or closed notation. [Default False]


.. cmdoption::  -f EXPERIMENT_FORMAT, --experiment-format EXPERIMENT_FORMAT

    The format the experiment file is written as. The options are ('eland', 'bed', 'bed_wig', 'bed_pk', 'bed_spk', 'sam', 'bam', 'counts'). [Default pk]


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --replica REPLICA

    Experiment A replica file


.. cmdoption::   --region REGION

    The region file or directory. In the enrichment analysis, if its not specified it will be calculated automatically from the tags in both files and the distance of clustering specified in the --proximity flag


.. cmdoption:: --region-format REGION_FORMAT

    The format the region file is written as. [default bed]


.. cmdoption::  --open-region

    Define if the region file is half-open or closed notation. [Default closed]


.. cmdoption::  --stranded

    Decide if the strand is taken into consideration for the analysis. This requires a region file in bed format with the strand information in its 6th column.


.. cmdoption::  --proximity PROXIMITY

    Determines if two regions calculated automatically are close enough to be clustered. Default 50 nt


.. cmdoption::  --binsize BINSIZE

    The size of the bins to calculate the local sd and mean for the background model, as a ratio of total number or regions. Regardless of the ratio selected, the minimum window size is 50 regions, since below that threshold the results will no longer be statistically meaningful. [Default 0.3]


.. cmdoption::  --sdfold SDFOLD

    The standard deviation fold used to generate the background model. [Default 1]


.. cmdoption::  --recalculate

    Recalculate the z-score when plotting. Useful for doing different plots with 'pyicos plot' [Default False]


.. cmdoption::  --mintags MINTAGS

    Number of tags (of the union of the experiment and experiment_b datasets) for a region to qualify to be analyzed. [Default 6]


.. cmdoption::  --binstep BINSTEP

    Step of the sliding window for the calculation of the z-score, as a ratio of the window size selected. If you want max precision, in the zscore calculation. You can set this value to 0 in order to use a sliding window that slides only 1 region at a time, but if you have many regions the calculation can get very slow.  [Default 0.1]


.. cmdoption::  --skip-plot

    Skip the plotting step. [Default False]


.. cmdoption::  --n-norm

    Divide the read counts by the total number of reads (units of million reads)


.. cmdoption::  --len-norm

    Divide the read counts by region (gene, transcript...) length (reads per kilobase units)


.. cmdoption::  --tmm-norm

    Trimming the extreme A and M to correct the dataset for the differences in read density between samples.  [Default False]


.. cmdoption::  --total-reads-a TOTAL_READS_A

    To manually set how many reads the dataset in 'experiment' has. If not used, it will be counted from the read or counts file. Default (automatically calculated from reads or counts files)


.. cmdoption::  --total-reads-b TOTAL_READS_B

    To manually set how many reads the dataset in 'experiment_b' has. If not used, it will be counted from the read or counts file. Default (automatically calculated from reads or counts files)


.. cmdoption::  --total-reads-replica TOTAL_READS_REPLICA

    To manually set how many reads the dataset in 'experiment_replica' has. If not used, it will be calculated from the read or the counts file. Default 0 (not used)


.. cmdoption::  --a-trim A_TRIM

    Proportion of A values to be discarded when doing the TMM normalization. Only applied when combined with --tmm-norm. [Default 0.05]


.. cmdoption::  --m-trim M_TRIM

    Proportion of M values to be discarded when doing the TMM normalization. Only applied when combined with --tmm-norm. [Default 0.25]


.. cmdoption::  --pseudocount

    The usage of pseudocounts in the enrichment calculation allows the inclusion of regions that have n reads in one dataset and 0 reads in the other.  [Default False]


.. cmdoption::  --zscore ZSCORE

    Significant Z-score value. [Default 2]




enrichcount
-----------

.. cmdoption::  -h, --help

    show this help message and exit


.. cmdoption::  --debug


.. cmdoption::  --no-sort

    Force skip the sorting step. WARNING: Use only if you know what you are doing. Processing unsorted files assuming they are will outcome in erroneous results


.. cmdoption::  --force-sort

    Force the sorting step


.. cmdoption::  --silent

    Run without printing in screen


.. cmdoption::  --disable-cache

    Disable internal reading cache. When Clustering low coverage files, it will increase speed and improve memory usage. With very read dense files, the speed will decrease.


.. cmdoption::  --keep-temp

    Keep the temporary files


.. cmdoption::  --postscript

    Get the output graphs in postscript format instead of .png


.. cmdoption::  --showplots

    Show the plots as they are being calculated by matplotlib. Note that the execution will be stopped until you close the window pop up that will arise


.. cmdoption::  --label1 LABEL1

    Manually define the first label of the graphs.


.. cmdoption::  --label2 LABEL2

    Manually define the second label of the graphs.


.. cmdoption::  --tempdir TEMPDIR

    Manually define the temporary directory where Pyicos will write. By default Pyicos will use the temporary directory the system provides (For example, /tmp in unix systems)


.. cmdoption::  --samtools

    Use samtools for reading BAM files [Default: Pyicos uses its own library] (reading BAM works without samtools for convert, extend, and other operations, but not for enrichment yet)]


.. cmdoption::  --skip-header

    Skip writing the header for the output file. [Default False]


.. cmdoption::  -O, --open-output

    Define if the output is half-open or closed notation.  [Default closed]


.. cmdoption::  -F OUTPUT_FORMAT, --output-format OUTPUT_FORMAT

    Format desired for the output. You can choose between ('eland', 'bed', 'bed_wig', 'variable_wig', 'bed_pk', 'bed_spk'). WARNING, for some operations, some outputs are not valid. See operation help for more info.  [default pk]


.. cmdoption::  --replica REPLICA

    Experiment A replica file


.. cmdoption::  --region-format REGION_FORMAT

    The format the region file is written as. [default bed]


.. cmdoption::  --open-region

    Define if the region file is half-open or closed notation. [Default closed]


.. cmdoption::  --stranded

    Decide if the strand is taken into consideration for the analysis. This requires a region file in bed format with the strand information in its 6th column.


.. cmdoption::  --proximity PROXIMITY

    Determines if two regions calculated automatically are close enough to be clustered. Default 50 nt


.. cmdoption::  --binsize BINSIZE

    The size of the bins to calculate the local sd and mean for the background model, as a ratio of total number or regions. Regardless of the ratio selected, the minimum window size is 50 regions, since below that threshold the results will no longer be statistically meaningful. [Default 0.3]


.. cmdoption::  --sdfold SDFOLD

    The standard deviation fold used to generate the background model. [Default 1]


.. cmdoption::  --recalculate

    Recalculate the z-score when plotting. Useful for doing different plots with 'pyicos plot' [Default False]


.. cmdoption::  --mintags MINTAGS

    Number of tags (of the union of the experiment and experiment_b datasets) for a region to qualify to be analyzed. [Default 6]


.. cmdoption::  --binstep BINSTEP

    Step of the sliding window for the calculation of the z-score, as a ratio of the window size selected. If you want max precision, in the zscore calculation. You can set this value to 0 in order to use a sliding window that slides only 1 region at a time, but if you have many regions the calculation can get very slow.  [Default 0.1]


.. cmdoption::  --skip-plot

    Skip the plotting step. [Default False]


.. cmdoption::  --n-norm

    Divide the read counts by the total number of reads (units of million reads)


.. cmdoption::  --len-norm

    Divide the read counts by region (gene, transcript...) length (reads per kilobase units)


.. cmdoption::  --tmm-norm

    Trimming the extreme A and M to correct the dataset for the differences in read density between samples.  [Default False]


.. cmdoption::  --total-reads-a TOTAL_READS_A

    To manually set how many reads the dataset in 'experiment' has. If not used, it will be counted from the read or counts file. Default (automatically calculated from reads or counts files)


.. cmdoption::  --total-reads-b TOTAL_READS_B

    To manually set how many reads the dataset in 'experiment_b' has. If not used, it will be counted from the read or counts file. Default (automatically calculated from reads or counts files)


.. cmdoption::  --total-reads-replica TOTAL_READS_REPLICA

    To manually set how many reads the dataset in 'experiment_replica' has. If not used, it will be calculated from the read or the counts file. Default 0 (not used)


.. cmdoption::  --a-trim A_TRIM

    Proportion of A values to be discarded when doing the TMM normalization. Only applied when combined with --tmm-norm. [Default 0.05]


.. cmdoption::  --m-trim M_TRIM

    Proportion of M values to be discarded when doing the TMM normalization. Only applied when combined with --tmm-norm. [Default 0.25]

.. cmdoption::  --zscore ZSCORE

    Significant Z-score value. [Default 2]


.. cmdoption::  --use-replica

    Indicates that for the calculation of the counts tables, a replica was used. [Default False]



