
Protocol files
==============

Protocol files are very useful for keeping track of the applied operations and especially to repeat a workflow several times.

For example, if you want to proceed for several data sets in the exact same way or you want to see the effect of minor changes without typing all the commands again you can use a protocol file.

Using protocol files is as easy as running single commands and you also do it through the command line::

    pyicos protocol narrow_peaks.pcl

 
**What is inside the protocol file?**

 
Content of narrow_peaks.pcl (To determine significant peaks; recommended for transcription factors) ::

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


.. Content of broad_peaks.pcl::     ; To determine significant peaks; recommended for histone modifications, RNA PolymeraseII and RNA-Seq peaks?


.. Content of CLIP-Seq.pcl::       ; To determine significant peaks; recommended for CLIP-Seq data



.. Content of Enrichment.pcl::      ; To determine regions that are enriched in one of the samples; recommended for histone modifications, RNA PolymeraseII and RNA-Seq





