.. pyicos documentation master file, created by
   sphinx-quickstart on Sat Apr  3 10:47:10 2010.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome
=======

Pyicos is a toolkit for the analysis of high-throughput sequencing data. It is designed to be flexible. It can be used in several ways:
 
* Command line unix tool
* Protocol files
* Integrated as part of a bigger pipeline
* Auxiliary libraries inside your python scripts
* Galaxy_

.. _Galaxy: http://regulatorygenomics.upf.edu/galaxy

Pyicos can be used for the analysis of different kinds of data. The analysis starts with the genomic position of the reads, so after the sequences have been mapped to the reference genome. Pyicos offers basic operations (extending reads, subtracting a control, etc.) as well as protocol files (combination of the operations in typical workflows) to analyse for example ChIP-Seq, RNA-Seq or CLIP-Seq data.  



If you have any problems or suggestions please join the `Pyicos Google Group`_ and ask! 

.. _`Pyicos Google Group`: http://groups.google.com/group/pyicos


Contents:

.. toctree::
   :maxdepth: 2
   
   Getting Started <introduction>
   Operations <operations>
   Basics <basics>
   Command-line operations <command>
   Command-line optional arguments <arguments>
   Command-line optional arguments 2 <arguments2>
   Classes <classes>
   Protocol files for workflows <protocol_files>
   Answers to case study <answers>

.. Indices and tables
.. ==================

.. * :ref:`genindex`
.. * :ref:`modindex`
* :ref:`search`

.. Python libraries usage <libraries>



