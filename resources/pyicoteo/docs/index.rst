.. pyicoteo documentation master file, created by
   sphinx-quickstart on Mon Apr  8 15:27:37 2013.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to Pyicoteo
*******************

Pyicoteo* is a suite of tools for the analysis of high-throughput sequencing data. It works with genomic coordinates, it was mainly developed using Solexa/Illumina mapped reads, but in its core it is platform-agnostic. There are currently 7 different tools for the analysis of HTS data: 6 command-line based tools, which are: :ref:`Pyicos` for genomic coordinates manipulation, :ref:`Pyicoller` for peak calling on punctuated ChIP-Seq, :ref:`Pyicoenrich` for differential enrichment between two conditions, :ref:`Pyicoclip` for calling CLIP-Seq peaks without a control, :ref:`pyicoregion` for generating exploratory regions automatically and :ref:`Pyicount`, to count how many reads from N experiment files overlap in a region file.

Pyicoteo suite also includes one :ref:`configuration file based <protocoldocs>` and a :ref:`python library <libdocs>` for scripting using HTS data::

Before start using it, it is highly recommended that you have a look at the :ref:`intro` document::

	* Pronounced as in Spanish  "picoteo"_ /pɪkɒtɛɒ/: 
          (n) Appetizer-type foods that accompany drinks before or instead of a meal.


Table of Contents
======================

.. toctree::
  :maxdepth: 2

  intro
  pyicoenrich
  pyicoclip
  pyicos
  pyicoregion
  pyicoller
  pyicotrocol
  pyicount
  pyicoteolib


