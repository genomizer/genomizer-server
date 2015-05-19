from pyicoteolib.defaults import *
from common import *
import argparse

def create_parser():

    #enrichment flags
    enrichment_flags = new_subparser()
    enrichment_flags.add_argument('--proximity', default=PROXIMITY, type=int, help="Determines if two regions calculated automatically are close enough to be clustered. Default %(default)s nt")
    enrichment_flags.add_argument('--binsize', type=float, default=BINSIZE, help="The size of the bins to calculate the local sd and mean for the background model, as a ratio of total number or regions. Regardless of the ratio selected, the minimum window size is 50 regions, since below that threshold the results will no longer be statistically meaningful. [Default %(default)s]")        
    enrichment_flags.add_argument('--sdfold', type=float, default=SDFOLD, help="The standard deviation fold used to generate the background model. [Default %(default)s]")  
    #enrichment_flags.add_argument('--recalculate', action='store_true', default=RECALCULATE, help="Recalculate the z-score when plotting. Useful for doing different plots with 'Pyicoteo plot' [Default %(default)s]")    
    enrichment_flags.add_argument('--mintags', type=float, default=REGION_MINTAGS, help="Number of tags (of the union of the experiment and experiment_b datasets) for a region to qualify to be analyzed. [Default %(default)s]") 
    enrichment_flags.add_argument('--binstep', type=float, default=WINDOW_STEP, help="Step of the sliding window for the calculation of the z-score, as a ratio of the window size selected. If you want max precision, in the zscore calculation. You can set this value to 0 in order to use a sliding window that slides only 1 region at a time, but if you have many regions the calculation can get very slow. [Default %(default)s]")  
    enrichment_flags.add_argument('--skip-plot', action='store_true', default=SKIP_PLOT, help="Skip the plotting step. [Default %(default)s]")
    enrichment_flags.add_argument('--n-norm', action='store_true', default=N_NORM, help="Divide the read counts by the total number of reads (units of million reads)")
    enrichment_flags.add_argument('--len-norm', action='store_true', default=LEN_NORM, help="Divide the read counts by region (gene, transcript...) length (reads per kilobase units)")
    
    #enrichment_flags.add_argument('--sequential', default=SEQUENTIAL, action='store_true', help="Iterate through the files in sequential order, instead of random access (for BAM reading). This is faster than random if you are using a lot of regions that overlap with each other") #TODO This flag doesn't work because if we order chr1 chr2 every file, instead of alphabetical, the SortedClusterReader classes will fail when changing chromosome, since the ALGORITHM depends on a sorted file

    enrichment_flags.add_argument('--region-magic', nargs='+', help="Region magic")
    enrichment_flags.add_argument('--gff-file', help="GFF file")
    enrichment_flags.add_argument('--interesting-regions', help="Path to file containing interesting regions (to be marked in the plot).")
    enrichment_flags.add_argument('--disable-significant-color', action='store_true', help="Do not plot significant regions in a different color")
    enrichment_flags.add_argument('--html-output', help="Output the enrichment results as HTML (requires the jinja2 library)")


    tmm_flag = new_subparser()
    tmm_flag.add_argument('--tmm-norm', action='store_true', default=TMM_NORM, help="Trimming the extreme A and M to correct the dataset for the differences in read density between samples. [Default %(default)s]")
    quant_flag = new_subparser()
    quant_flag.add_argument('--quant-norm', action='store_true', default=QUANT_NORM, help="Full quantile normalization of the counts. This normalization method could be considered the most conservative of them all. [Default %(default)s]")

    exp_or_count = new_subparser()
    mutexc = exp_or_count.add_mutually_exclusive_group(required=True)
    output = new_subparser()
    output.add_argument('-output', help='The output file or directory')

    mutexc.add_argument('-reads', nargs=2, dest='experiments', help='Compare two packages.', metavar=("experiment_a","experiment_b"))
    mutexc.add_argument('-counts', dest='counts_file', help='Verify Content of package.')
    parser = argparse.ArgumentParser(version=VERSION, 
                                     description='An enrichment test based on the MA plots using mapped reads files. Pyicoenrich output will consist in a results table and a MA plot (optional, but matplotlib required >=0.9.7). The fields of this table are as follows: %s'%(" | ".join(enrichment_keys)), 
                                     parents=[exp_or_count, experiment_flags, basic_parser, output_flags, optional_replica, optional_region, 
                                              region_format, output, enrichment_flags, tmm_flag, quant_flag, total_reads_flags, 
                                              pseudocount, zscore, optional_push, optional_frag_size, stranded_flag, overlap_flag]
                                     )
    #-output
    return parser


def define_operations(turbomix, args):
    turbomix.operations = [ENRICHMENT, CALCZSCORE]
    if args.push_distance:
        turbomix.operations.append(PUSH)

    if args.frag_size:
        turbomix.operations.append(EXTEND)

    if not args.skip_plot:
       turbomix.operations.append(PLOT)

def run_parser(parser, test_args=None): #test_args for unit testing
    args = parse_validate_args(parser, test_args)
    turbomix = init_turbomix(args, parser_name="pyicoenrich")
    define_operations(turbomix, args)
    turbomix.run()


