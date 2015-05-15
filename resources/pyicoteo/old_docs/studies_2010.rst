Practical seminar for UB Master
=================================

Download Pyicos `lastest version <http://sourceforge.net/projects/pyicos>`_.
You don't need to install it, just open a terminal, extract the file and get inside the pyicos  folder. The entry script is pyicos, a command that contains several subcommands. You can execute it by typing::

        python pyicos <command> <parameters>

Examples::

    python pyicos convert file1 resultfile --output-format variable_wig
    python pyicos extend file1 resultfile 100

**Optional** If you have administrator rights to your machine, you can install it by typing::

    python setup.py install

This way the program will be available as a command line tool.

To check the command line help and get a taste on how this tool works type::

    python pyicos --help

To get the specific help for a subcommand, type::

    python pyicos <subcommand> --help

If you don't have time to finish the session you can still check the results at test_files/results.

You will find two bed files inside the test_files/ folder, p300.bed and control.bed. The file "p300.bed" is the result of mapping to the last human genome (hg19) to the interaction of
the `p300 coactivator <http://en.wikipedia.org/wiki/EP300>`_ to T CD4+ human cells. "control.bed" is the control experiment. This a subset (chromosome 22) of real published data from
`Zhibin Wang et Al <http://www.cell.com/abstract/S0092-8674%2809%2900841-1>`_


Extend the reads
---------------------
Suppose the experimentalists told us that the original immunoprecipitated fragments were 100 nucleotides long on average after sonication and that we want to use this as the length of our reads to reconstruct the signal.
To do this, use pyicos *extend* command.

We will extend the bed reads to 100 and get the output as a bed file::

    pyicos extend test_files/p300.bed test_files/p300_ext100.bed 100 --input-format bed --output-format bed --open-output --wig-label p300_ext100
    pyicos extend test_files/control.bed test_files/control_ext100.bed 100 --input-format bed --output-format bed --open-output --wig-label control_ext100
    
In order to see the reads, we will "cluster" them, to better observe the enriched regions. To do this, convert the reads to half-open bed_wig with the *convert* command.
We will also add the --open-output flag, since the "half-open" notation is required for UCSC visualization.

Cluster the reads
-----------------------
We will cluster the reads with the *convert* command::

    pyicos convert test_files/p300_ext100.bed test_files/p300_ext100.wig --input-format bed --output-format bed_wig  --open-output --wig-label 1.p300_ext100
    pyicos convert test_files/control.bed test_files/control_ext100.wig --input-format bed --output-format bed_wig  --open-output --wig-label 2.control_ext100


Upload the resulting files again in the genome browser. Go to chromosome 22::

    chr22:36,723,361-36,795,286

Subtract the control
----------------------

Now we will subtract the control reads from the p300 ones. This operation will result in a single file that will not contain the reads duplicated in the control and the experiment file,
removing reads that are not product of p300 interaction with the genome::

    pyicos subtract test_files/p300_ext100.bed test_files/control.bed test_files/subtracted.wig --input-format bed --open-input --output-format bed_wig --open-output --wig-label 3.subtracted

Upload the resulting file to the browser. Observe how the subtraction is performed with nucleotide precision, for example here::

    chr22:42,297,911-42,298,499

Discard Artifacts
---------------------

Finally, we will discard artifacts. The discard artifact algorithm will consider as an artifact any cluster that has a "block" similar shape and singletons (clusters with only one read)::

     pyicos discard test_files/subtracted.wig test_files/result.wig --input-format bed_wig --open-input --output-format bed_wig --open-output --wig-label 4.extended_subtracted_noartifacts

Upload this files. Observe how some of the noise is gone. An interesting region would be, for example::

    chr22:37,077,154-37,155,398

This is are just some common operations performed by bioinformaticians and included in several published "Peak Callers".
Note this is a simplified analysis, not a complete one. Feel free to try the other operations available.
