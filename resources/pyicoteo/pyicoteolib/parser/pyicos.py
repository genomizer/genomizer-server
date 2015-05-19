from pyicoteolib.defaults import *
from common import *
import argparse

def create_parser():
    parser = argparse.ArgumentParser(version=VERSION, 
                                     description="Pyicos, part of Pyicoteo suite, is a collection of tools for mapped reads processing and basic analysis.")
    subparsers = parser.add_subparsers(help='The operation you want to perform. Note that some operations imply previous automatic operations.')
    subparsers.add_parser('convert', help='Convert a file to another file type.',
                          parents=[experiment, experiment_flags, basic_parser, output, output_flags, round, label, tag_length, span, optional_frag_size, remlabels])

    subparsers.add_parser('subtract', help='Subtract two clustered files. Operating with directories will only give apropiate results if the files and the control are paired in alphabetical order.', parents=[experiment,experiment_flags, basic_parser, control, control_format, open_control, output, output_flags, round, normalize, tag_length, span, label, remlabels])
    #split operation
    subparsers.add_parser('split', help='Split the peaks in subpeaks. Only accepts pk or wig as output.', parents=[experiment, experiment_flags, basic_parser, output, output_flags, round, split_proportion, split_absolute, label, remlabels])
    #trim operation
    subparsers.add_parser('trim', help='Trim the clusters to a given threshold.', parents=[experiment, experiment_flags, basic_parser, output, output_flags, round, trim_absolute, trim_proportion, label, remlabels, span])
    #discard operation
    subparsers.add_parser('discard', help='Discards artifacts from a file. Only accepts pk or wig as output.', parents=[experiment, experiment_flags, basic_parser, output, output_flags, round, span, label, remlabels])
    #remove duplicates operation
    subparsers.add_parser('remduplicates', help='Removes the duplicated reads in a file. Only accepts tag-like files (bed, eland, sam)', parents=[experiment, experiment_flags, basic_parser, output, output_flags, tolerated_duplicates, round, span, label, remlabels])
    #extend operation
    subparsers.add_parser('extend', help='Extends the reads of a file to the desired length. This operation requires tag-like files (bed, eland, sam)', parents=[experiment,experiment_flags,  basic_parser,  output, output_flags, frag_size, round, label, span, remlabels])
    #push operation
    subparsers.add_parser('push', help='Push the reads in the corresponding strand. If a read doesn\'t have a strand, will be pushed from left to right. This operation requires tag-like files (bed, eland, sam)', parents=[experiment,experiment_flags, basic_parser, output, output_flags, push_distance, round, label, span, remlabels])
    #DEPRECATED doesnt really make sense, will incorporate it again if needed
    #poisson analysis
    #subparsers.add_parser('poisson', help='Analyze the significance of accumulated reads in the file using the poisson distribution. With these tests you will be able to decide what is the significant threshold for your reads.',
    #                      parents=[experiment,experiment_flags,  basic_parser, output_flags, optional_frag_size, pvalue, height, correction, species, remlabels, poisson_test])
    #cut operations
    subparsers.add_parser('filter', help="""Analyze the significance of accumulated reads in the file using the poisson distribution and generate the resulting profiles, in wig or pk formats""",
                          parents=[experiment,experiment_flags,  basic_parser, output, optional_frag_size, output_flags, round, pvalue, height, correction, threshold, species, remlabels, poisson_test])
    #remove operation
    subparsers.add_parser('remregions', help='Removes regions that overlap with another the coordinates in the "black list" file.',
                          parents=[experiment, experiment_flags, basic_parser, output_flags, blacklist, region_format, output, remlabels])
    #strcorr operation
    subparsers.add_parser('strcorr', help='A cross-correlation test between forward and reverse strand clusters in order to find the optimal extension length.',
                          parents=[experiment, experiment_flags, basic_parser, output, output_flags, correlation_flags, remlabels])

    return parser


def run_parser(parser, test_args=None):
    parser = create_parser()
    args = parse_validate_args(parser, test_args)
    turbomix = init_turbomix(args, parser_name="pyicos")

    if sys.argv[1] == 'convert': #FIXME workaround, ideally pyicos parser should not use the sys library. Break turbomix.
            if args.frag_size:
                turbomix.operations = [EXTEND]
    elif sys.argv[1] == 'subtract':
        turbomix.operations = [SUBTRACT]
        if args.normalize:
            turbomix.operations.append(NORMALIZE)
    elif sys.argv[1] == 'normalize':
        turbomix.operations = [NORMALIZE]
    elif sys.argv[1] == 'extend':
        turbomix.operations = [EXTEND]
    elif sys.argv[1] == 'push':
        turbomix.operations = [PUSH]
    elif sys.argv[1] == 'strcorr':
        turbomix.operations = [STRAND_CORRELATION, NOWRITE]
    elif sys.argv[1] == 'poisson':
        turbomix.operations = [POISSON, NOWRITE]
    elif sys.argv[1] == 'filter':
        turbomix.operations = [POISSON, FILTER]
    elif sys.argv[1] == 'remove':
        turbomix.operations = [REMOVE_REGION]
    elif sys.argv[1] == 'enrichma':
        turbomix.operations = [USE_MA, ENRICHMENT, CALCZSCORE]
        if not args.skip_plot:
           turbomix.operations.append(PLOT)
    elif sys.argv[1] == 'enrichment' or sys.argv[1] == 'enrichcount':
        turbomix.operations = [ENRICHMENT, CALCZSCORE]
        if not args.skip_plot:
           turbomix.operations.append(PLOT)
    elif sys.argv[1] == 'checkrep' or sys.argv[1] == 'checkrepcount':
        turbomix.operations = [ENRICHMENT, CHECK_REPLICAS, NOWRITE]
    elif sys.argv[1] == 'split':
        turbomix.operations = [SPLIT]
    elif sys.argv[1] == 'trim':
        turbomix.operations = [TRIM]
    elif sys.argv[1] == 'discard':
        turbomix.operations = [DISCARD_ARTIFACTS]
    elif sys.argv[1] == 'remduplicates':
        turbomix.operations = [REMOVE_DUPLICATES]
    elif sys.argv[1] == 'remregions':
        turbomix.operations = [REMOVE_REGION]

    turbomix.run()


