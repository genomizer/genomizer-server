from pyicoteolib.defaults import *
from common import *
import argparse
from ..counter import count_all


def create_parser():
    pyicount_flags = new_subparser()
    pyicount_flags.add_argument('-reads', nargs='+', help='The files to count from')
    pyicount_flags.add_argument('-output', help='Output count file')
    pyicount_flags.add_argument("--gff-file", type=str, help= "The GTF file")
    parser = argparse.ArgumentParser(   version=VERSION, 
                                        description="""Pyicount, part of the Pyicoteo suite. 
                                                     Count read files and generate a pyicos count file for 1 to N read files.""",
                                        parents=[pyicount_flags, experiment_flags, basic_parser, 
                                                 optional_region, magic_flag],
        )

    return parser

def run_parser(parser, test_args=None):
    args = parser.parse_args()
    count_all(args.reads, args.experiment_format, args.output, args.region, args.gff_file, args.region_magic, args.no_sort, args.keep_temp, args.debug)


