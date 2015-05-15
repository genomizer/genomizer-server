Hands-on seminar for UB Master
=================================

Introduction
-------------------

We are going to analyze mapped files in order to find binding sites for a transcription factor. 

Download the `exercise files <http://regulatorygenomics.upf.edu/group/media/cebpa_example.tar.gz>`_. Extract the file, and open a console and get inside the file using the *cd* command. The file *cebpa.bed* is the result of mapping to the last human genome (hg19) to the interaction of the `CEBPA transcription factor <http://en.wikipedia.org/wiki/CEBPA>`_ to liver human cells, while the file *control.bed* is the result of immunoprecipitating "naked" DNA without a protein. This a subset (chromosome 22) of real published data from `Schmidt et Al <http://www.ncbi.nlm.nih.gov/pubmed/20378774>`_


Strand Correlation
---------------------
This is a cross-correlation test between forward and reverse strand clusters in order to find the optimal extension length. To do this, we will analyze the experiment file, cebpa.bed::

    pyicos strcorr cebpa.bed corr.png --experiment-format bed --showplots

Extend the reads
---------------------
We will use the extension value obtained with the strand correlation as the length of our reads to reconstruct the signal.
To do this, use pyicos *extend* command.

We will extend the bed reads to 114 and get the output as a bed file::

    pyicos extend cebpa.bed cebpa_ext114.bed 114 --experiment-format bed --output-format bed --open-output --wig-label cebpa_ext114
    pyicos extend control.bed control_ext114.bed 114 --experiment-format bed --output-format bed --open-output --wig-label control_ext114
    
In order to see the reads, we will "cluster" them, to better observe the enriched regions. To do this, convert the reads to half-open bed_wig with the *convert* command. We will also add the --open-output flag, since the "half-open" notation is required for UCSC visualization.

Cluster the reads
-----------------------
We will cluster the reads with the *convert* command::

    pyicos convert cebpa_ext114.bed cebpa_ext114.wig --experiment-format bed --output-format bed_wig  --open-output --wig-label 1.cebpa_ext114
    pyicos convert control.bed control_ext114.wig --experiment-format bed --output-format bed_wig  --open-output --wig-label 2.control_ext114


Upload the resulting files again in the genome browser. Go to chromosome 22::

    chr22:36,723,361-36,795,286

Subtract the control
----------------------

Now we will subtract the control reads from the cebpa ones. This operation will result in a single file that will not contain the reads duplicated in the control and the experiment file,
removing reads that are not product of cebpa interaction with the genome::

    pyicos subtract cebpa_ext114.bed control_ext114.bed subtracted.wig --experiment-format bed --open-experiment --output-format bed_wig --open-output --wig-label 3.subtracted

Upload the resulting file to the browser. Observe how the subtraction is performed with nucleotide precision, for example here::

    chr22:41,720,727-41,722,712

Discard Artifacts
---------------------

Finally, we will discard artifacts. The discard artifact algorithm will consider as an artifact any cluster that has a "block" similar shape and singletons (clusters with only one read)::

     pyicos discard subtracted.wig result.wig --experiment-format bed_wig --open-experiment --output-format bed_wig --open-output --wig-label 4.extended_subtracted_noartifacts

Upload these files. Observe how some of the noise is gone. An interesting region would be, for example::

    chr22:41,639,712-41,643,167



Note this is a simplified analysis, not a complete one. Feel free to try the other commands available.


