.. _Pyicoclip:

Pyicoclip
=========

Introduction
-------------

Pyicoclip is an implementation of the modified False Discovery Rate algorithm proposed_ by Yeo et al. to determine which clusters are significant in a list of genomic regions (like genes or transcripts). This method is typically used in CLIP-Seq data that doesn't have a valid control experiment to compare against. 

.. _proposed: http://www.nature.com/nsmb/journal/v16/n2/full/nsmb.1545.html

This method could in principle be used for any other kind of experiment that involves short reads and doesn't have a valid control. You can provide your own region file, or otherwise you can provide a ``--region-magic`` description with a GTF file (see below )

Basic usage
-----------

``pyicoclip`` usage requires the experiment CLIP-Seq file (eland, SAM, BAM, BED formats) and a region file in BED format. Default output format is :ref:`The bedpk format`, our extended but compatible with BED format.

Example::

    pyicoclip my_experiment.bed output.pk -f bed --region my_regions.bed 

This command will output 2 files: ``output.pk`` and ``unfiltered_output.pk``. In ``output.pk`` you will get the significant peaks after applying the method an the significant p-value, in the unfiltered one you will get the unfiltered peaks with 2 or more overlapping reads.  Single reads are omitted from the output.

Important flags
-----------------

This is a description of the most important flags. To see the complete list of flags, type ``pyicoclip -h`` to get the full list.

``--stranded``
"""""""""""""""""

You will probably want to use the --stranded flag, in order to take into consideration reads only overlapping with the strand of the regions of interest (6th column of your BED6 region file)::

    pyicoclip my_experiment.bed output.pk -f bed --region my_regions.bed **``--stranded``**


``--p-value``
"""""""""""""""

The threshold to make it to the significant peaks list. Default is 0.01 


``--region-magic`` and ``--gtf-file``
"""""""""""""""""""""""""""""""""""""""

You can automatically generate exploratory region files using the ``--region-magic`` and ``--gtf-file`` flags.

For example, explore the regions 500 bases upstream and 1000 downstream of TSS::

    pyicoclip my_experiment.bed output.pk -f bed --region my_regions.bed --gtf-file myref.gtf --region-magic tss -500 1000

Check all genes::

    pyicoclip my_experiment.bed output.pk -f bed --region my_regions.bed --gtf-file myref.gtf --region-magic genebody

See :ref:`Pyicoregion` documentation for more details on how to use ``--region-magic`` flag.


Credit
------

* Developer: Juan González-Vallinas
* Beta Testing: Mireya Plass, Juan González-Vallinas
* Supervision: Eduardo Eyras
