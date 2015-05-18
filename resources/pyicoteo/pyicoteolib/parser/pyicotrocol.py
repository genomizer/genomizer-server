from pyicoteolib.defaults import *
from common import *
import argparse
import ConfigParser

def create_parser():
    parser = argparse.ArgumentParser(version=VERSION, 
                                     description="""Pyicotrocol, part of the Pyicoteo suite, is a convenient way of accessing and combining the different parts
                                                  of the Pyicoteo suite (parts of Pyicos with parts of Pyicoenrich, for example). Based on .ini files.""",
                                     )
    parser.add_argument('protocol_name', help='The protocol configuration file. (.ini format)')
    return parser

def config_section_map(section, config_file):
    dict1 = {}
    options = config_file.options(section)
    for option in options:
        try:
            dict1[option] = config_file.get(section, option)
            if dict1[option] == -1:
                DebugPrint("skip: %s" % option)
        except:
            print("exception on %s!" % option)
            dict1[option] = None
    return dict1



def run_parser():

    parser = create_parser()
    args = parse_validate_args(parser)

    config = ConfigParser.ConfigParser()
    config.read(args.protocol_name)

    try:
        section = config_section_map("Pyicotrocol", config)
    except ConfigParser.NoSectionError:
        print "\nERROR: %s is not a Pyicotrocol file, is missing the [Pyicotrocol] header or it doesn't exist\n"%args.protocol_name
        sys.exit(0)

    for key, value in section.items(): #this works fine for all string values
        try:
            t = type(parser._defaults[key])
            if t == int:
                args.__dict__[key] = config.getint("Pyicotrocol", key)
            elif t == float:
                args.__dict__[key] = config.getfloat("Pyicotrocol", key)
            elif t == bool:
                args.__dict__[key] = config.getboolean("Pyicotrocol", key)
            elif t == str:
                args.__dict__[key] = config.get("Pyicotrocol", key)
        except KeyError:
            if key == 'input':
                args.__dict__['experiment'] = config.get("Pyicotrocol", 'input')
                print "\nWARNING: The keyword 'input' for the protocol files is deprecated, please use 'experiment' instead"

            elif key != 'operations':
                print 'ERROR: There is an error in your protocol file.  "%s" is not a Pyicotrocol parameter'%key
                sys.exit(0)

    turbomix = init_turbomix(args, parser_name="pyicotrocol")
    #add the operations from the script
    operations = section['operations'].split(',')
    for operation in operations:
        print "Adding operation %s to protocol..."%operation
        turbomix.operations.append(operation.strip())   

    turbomix.run()

