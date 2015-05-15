.. _protocoldocs:

Pyicotrocol
===========

Pyicotrocol offers access to the whole functionality of Pyicoteo, allowing for combination of operations in the different Pyicoteo suite tools.

It also has the nice advantage in terms of experiment reproducibility, since you don't need to save the command you used, what you did stays in the text file.
Execution
---------

To run a protocol file type::

    pyicotrocol my_protocol.ini

Syntax
------

Pyicotrocols files are based on the `.ini format`_

.. _`.ini format`: http://en.wikipedia.org/wiki/INI_file

Examples
--------
 
This example is equivalent of using pyicoller (To determine significant peaks; recommended for transcription factors) ::

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


Credit
------

* Developer: Juan González-Vallinas
* Testing: Sonja Althammer, Juan González-Vallinas
* Supervision: Eduardo Eyras


