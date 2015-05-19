import os
from tempfile import gettempdir

from pyicoteolib.core import ReadRegion, ReadCluster  
from pyicoteolib.regions import RegionWriter
from pyicoteolib.utils import  SortedFileCountReader, BED, BigSort, sorting_lambda, get_logger, manage_temp_file

def count_all(paths, experiment_format, output_path, region_path, gff_file, region_magic, skip_sort=False, keep_temp=False, debug=False, overlap=EPSILON):
    logger = get_logger("pyicount.log")
    #Get the region
    if not region_path:#region magic
        calc_region_file = open("calcregion_pyicount_%s.bed"%os.getpid(), 'w')
        regwriter = RegionWriter(gff_file, calc_region_file, region_magic, no_sort=skip_sort, logger=logger, write_as=BED, debug=debug)
        regwriter.write_regions()
        region_path = calc_region_file.name
        calc_region_file.flush()
        calc_region_file.close()

    region_file = open(region_path)

    #Sort the files and load them up in readers
    tempfiles = False
    sorted_readers = []
    if skip_sort:
        logger.info('Sorting skipped')
        sorted_readers = path
    else:
        for i, path in enumerate(paths):
            logger.info('Sorting read files...')
            sorter = BigSort(experiment_format, logger=logger)
            sorted_path = "%s/tempcounter_%s_%s"%(gettempdir(), os.getpid(), i)
            sorter.sort(path, sorted_path, key=sorting_lambda(experiment_format))
            sorted_readers.append(SortedFileCountReader(sorted_path, experiment_format))
            tempfiles = True

    logger.info('Writing counts file...')
    output_file = open(output_path, 'w')
    #Iterate the region objects
    for region_line in region_file:
        c = ReadCluster(read=BED)
        c.read_line(region_line)
        region = ReadRegion(c.name, c.start, c.end)
        counts = []
        for reader in sorted_readers:
            counts.append(str(reader.get_overlaping_counts(region, overlap=overlap)))

        write_list = [c.name, str(c.start), str(c.end)]
        write_list.extend(counts)
        output_file.write('%s\n'%'\t'.join(write_list))

    if tempfiles:
        for sorter in sorted_readers:
            manage_temp_file(sorter.file_path, keep_temp, logger)





