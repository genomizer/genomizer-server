.. _Pyicoller:

Pyicoller
=========

This peak caller is a combination of some of Pyicos commands (extend, normalize, subtract, remove, poisson and filter) for the task of calling peaks from a ChIP-Seq experiment (with narrow peaks). A control file is optional but recommended.


Example::

    pyicoller my_experiment.bed significant_peaks.bedpk -f bed -o --control control.bed --control-format bed --open-control --region regions_to_be_removed.bed --remlabels chrY --correction 0.8 --k-limit 20 --p-value 0.001 -x 130


Important flags
-----------------

This is a description of the most important flags. To see the complete list of flags, type ``pyicoller -h`` to get the full list.

.. option:: --species SPECIES

    The species assembly that you are analyzing. This will read the length of the chromosomes of this species from the files inside the folder "chrdesc". If the species information is not known, pyicoller will try to connect to the UCSC database and download it. [Default hg19]


.. option:: --duplicates DUPLICATES

    The number of duplicated reads allowed by the algorithm. Any duplicated read after this threshold will be discarded. By default, no duplicated reads are allowed. 

.. option:: -x FRAG_SIZE, --frag-size FRAG_SIZE

    The estimated inmmunoprecipitated sequence fragment size. This is used by Pyicoller to reconstruct the original signal. If not specified, a :ref:`strcorr` test will be performed in order to estimate the fragment length. 

.. option:: --poisson-test POISSON_TEST

    Decide what property of the read clusters will be used for the poisson analysis. ``--poisson-test height`` will use the summit of the peaks, ``--poisson-test numtags`` the total number of reads and ``--poisson-test length`` the length occupied by the peaks. Default is ``height``



Credit
------

* Developer: Juan González-Vallinas
* Beta testing: Sonja Althammer, Eneritz Agirre, Nuria Conde Pueyo
* Optimization of the pipeline: Sonja Althammer
* Benchmarking against other peak callers: Sonja Althammer
* Performance benchmarking: Juan González-Vallinas


