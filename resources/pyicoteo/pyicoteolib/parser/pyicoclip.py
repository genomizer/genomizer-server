from pyicoteolib.defaults import *
from common import *
import argparse

def create_parser():
    parser = argparse.ArgumentParser(version=VERSION, 
                                     description="Pyicoclip, part of the Pyicoteo suite, is a peak caller specifically designed for CLIP-Seq analysis, based on the ModFDR method proposed by Yeo et al.",
                                     parents=[experiment, experiment_flags, basic_parser, optional_region, output, output_flags, stranded_flag, round, pvalue, repeats, remlabels, optional_gtf, magic_flag]
                                     )
    return parser

def run_parser(parser, test_args=None):
    args = parse_validate_args(parser, test_args)
    turbomix = init_turbomix(args, parser_name="pyicoclip")
    turbomix.operations = [MODFDR]
    turbomix.run()


