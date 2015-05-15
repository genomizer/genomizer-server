from pyicoteolib.defaults import *
from common import *
import argparse

def create_parser():
    region_parser = new_subparser()
    region_parser.add_argument('gff_file', help="GFF input file")
    output = new_subparser()
    output.add_argument('output', help='The output file')
    output_flags_region = new_subparser()
    output_flags_region.add_argument('-O','--open-output', action='store_true', default=OPEN_OUTPUT, help='Define if the output is half-open or closed notation. [Default closed]')
    output_flags_region.add_argument('-F','--output-format',default="bed", help='Format desired for the output. You can choose between %s. [default BED]'%write_formats)
    output_flags_region.add_argument('--no-duplicates', default=False, action="store_true", help='Remove duplicated regions (same coordinates) from the output')
    parser = argparse.ArgumentParser(version=VERSION, 
                                     description='Standalone region operations', 
                                     parents=[region_parser, output, output_flags_region, basic_parser, magic_flag]
                                     )
    return parser

def run_parser(parser, test_args=None):
    args = parse_validate_args(parser, test_args)
    turbomix = init_turbomix(args, parser_name="pyicoregion")
    turbomix.operations = [REGIONS]
    turbomix.run()


