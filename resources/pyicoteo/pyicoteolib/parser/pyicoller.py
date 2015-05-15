from pyicoteolib.defaults import *
from common import *
import argparse

def create_parser():
    parser = argparse.ArgumentParser(version=VERSION, 
                                     description="Pyicoller, part of the Pyicoteo suite, is a peak caller for the analysis of punctuated ChIP-Seq data.",
                                     parents=[experiment, experiment_flags, basic_parser, optional_control, control_format, open_control, 
                                              optional_blacklist, output, output_flags, optional_frag_size, round, label, span, no_subtract, 
                                              remlabels, pvalue, height, correction, trim_proportion, species, tolerated_duplicates, poisson_test, 
                                              correlation_flags])
    return parser


def run_parser(parser, test_args=None):
    args = parse_validate_args(parser, test_args)
    turbomix = init_turbomix(args, parser_name="pyicoller")
    turbomix.operations = [SPLIT, EXTEND, POISSON, FILTER, REMOVE_DUPLICATES, STRAND_CORRELATION] 
    if args.duplicates > 1: #If there is only 1 duplicate, there is no need to discard artifacts
        turbomix.operations.append(DISCARD_ARTIFACTS)
    if args.blacklist:
        turbomix.operations.append(REMOVE_REGION)
    if args.control and not args.no_subtract:
        turbomix.operations.append(NORMALIZE)
        turbomix.operations.append(SUBTRACT)

    turbomix.run()


