import unittest
from pyicoteolib.core import Cluster, BED, ELAND, PK, ELAND_EXPORT, WIG
from pyicoteolib.turbomix import Turbomix
from pyicoteolib.defaults import *
import pyicoteolib.utils as utils

import math

class TestOperations(unittest.TestCase):
    
    def setUp(self):
        self.files_dir = '/tmp/'
        self.dummy_path = self.files_dir + 'dummy_path.pk'
        
        self.bed_path =self.files_dir + 'test_convert.bed'
        self.bed_file = open(self.bed_path,'w+')
        self.bed_file.write("chr1\t37\t52\t0\tnoname\t-\n")
        self.bed_file.write("chr10\t1\t100\t0\tnoname\t+\n")
        self.bed_file.write("chr10\t301\t380\t0\tnoname\t+\n")
        self.bed_file.write("chr18\t270\t304\t0\tnoname\t+\n")
        self.bed_file.write("chr18\t290\t324\t0\tnoname\t+\n")
        self.bed_file.write("chr18\t370\t404\t0\tnoname\t+\n")
        self.bed_file.write("chr18\t1290\t1324\t0\tnoname\t+\n")
        self.bed_file.write("chrX\t2270\t2304\t0\tnoname\t+\n")
        self.bed_file.write("chrX\t2290\t2324\t0\tnoname\t+\n")
        self.bed_file.write("chrX\t2290\t2324\t0\tnoname\t+\n")
        self.bed_file.write("chrX\t2290\t2324\t0\tnoname\t+\n")
        self.bed_file.write("chrX\t2300\t2334\t0\tnoname\t+\n")
        self.bed_file.flush()

        self.conversion_result_path = self.files_dir + 'result_convert_cluster.pk'
        self.pk_path = self.files_dir + 'test_convert.pk'
        self.pk_file = open(self.pk_path,'w+')
        self.pk_file.write('chr1\t37\t52\t16:1.00\t1.0\t-\t44\t16.0\n')
        self.pk_file.write('chr10\t1\t100\t100:1.00\t1.0\t+\t50\t100.0\n')
        self.pk_file.write('chr10\t301\t380\t80:1.00\t1.0\t+\t340\t80.0\n')
        self.pk_file.write('chr18\t270\t324\t20:1.00|15:2.00|20:1.00\t2.0\t+\t297\t70.0\n')
        self.pk_file.write('chr18\t370\t404\t35:1.00\t1.0\t+\t387\t35.0\n')
        self.pk_file.write('chr18\t1290\t1324\t35:1.00\t1.0\t+\t1307\t35.0\n')
        self.pk_file.write('chrX\t2270\t2334\t20:1.00|10:4.00|5:5.00|20:4.00|10:1.00\t5.0\t+\t2302\t175.0\n')
        self.pk_file.flush()
        
        self.extended_result_path = self.files_dir + 'result_extend.bed'
        self.bed_extended_path = self.files_dir + 'test_extended.bed'
        self.bed_extended_file = open(self.bed_extended_path,'w+')
        self.bed_extended_file.write("chr10\t1\t100\t0\tnoname\t+\n")
        self.bed_extended_file.write("chr10\t301\t400\t0\tnoname\t+\n")
        self.bed_extended_file.write("chr18\t270\t369\t0\tnoname\t+\n")
        self.bed_extended_file.write("chr18\t290\t389\t0\tnoname\t+\n")
        self.bed_extended_file.write("chr18\t370\t469\t0\tnoname\t+\n")
        self.bed_extended_file.write("chr18\t1290\t1389\t0\tnoname\t+\n")
        self.bed_extended_file.write("chrX\t2270\t2369\t0\tnoname\t+\n")
        self.bed_extended_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.bed_extended_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.bed_extended_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.bed_extended_file.write("chrX\t2300\t2399\t0\tnoname\t+\n")
        self.bed_extended_file.flush()

        self.notsubtracted_path = self.files_dir + 'test_notsubtracted.bed'
        self.notsubtracted_file = open(self.notsubtracted_path,'w+')
        self.notsubtracted_file.write("chr1\t1\t100\t0\t200\t+\n")
        self.notsubtracted_file.write("chr1\t1\t100\t0\t200\t+\n")
        self.notsubtracted_file.write("chr1\t1\t100\t0\t200\t+\n")
        self.notsubtracted_file.write("chr1\t1\t100\t0\t200\t+\n")
        self.notsubtracted_file.write("chr2\t1\t100\t0\t210\t+\n")
        self.notsubtracted_file.write("chrX\t101\t200\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chrX\t101\t200\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chr10\t301\t400\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chr10\t534\t600\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chr10\t534\t600\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chr18\t270\t369\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chr18\t290\t389\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chr18\t370\t469\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chr18\t1290\t1389\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chrX\t2270\t2369\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.notsubtracted_file.write("chrX\t2300\t2399\t0\tnoname\t+\n")
        self.notsubtracted_file.flush()

        self.subtract_result_path = self.files_dir + 'result_subtract.pk'
        self.bed_control_path = self.files_dir + 'test_subtract.bed'
        self.bed_control_file = open(self.bed_control_path,'w+')
        self.bed_control_file.write("wegqwergawhaqgwe gaqw eg awe ga we gaw ega we gaw e aga\n")
        self.bed_control_file.write("chr1\t1\t100\t0\tnoname\t+\n")
        self.bed_control_file.write("chrX\t50\t101\t0\tnoname\t+\n")
        self.bed_control_file.write("chrX\t150\t301\t0\tnoname\t+\n")
        self.bed_control_file.write("chrX\t150\t301\t0\tnoname\t+\n")
        self.bed_control_file.write("chr10\t1\t1000\t0\tnoname\t+\n")
        self.bed_control_file.write("chr10\t1\t1000\t0\tnoname\t+\n")
        self.bed_control_file.write("chr10\t1\t1000\t0\tnoname\t+\n")
        self.bed_control_file.write("chr10\t1\t1000\t0\tnoname\t+\n")
        self.bed_control_file.write("chr10\t1\t1000\t0\tnoname\t+\n")
        self.bed_control_file.write("chr18\t270\t359\t0\tnoname\t+\n")
        self.bed_control_file.write("chr18\t270\t359\t0\tnoname\t+\n")
        self.bed_control_file.write("chr18\t270\t359\t0\tnoname\t+\n")
        self.bed_control_file.write("chr18\t270\t359\t0\tnoname\t+\n")
        self.bed_control_file.write("chr18\t270\t359\t0\tnoname\t+\n")
        self.bed_control_file.write("chr18\t375\t459\t0\tnoname\t+\n")
        self.bed_control_file.write("chr18\t1290\t1389\t0\tnoname\t+\n")
        self.bed_control_file.write("chr19\t1290\t1389\t0\tnoname\t+\n")
        self.bed_control_file.write("chr19\t1290\t1389\t0\tnoname\t+\n")
        self.bed_control_file.write("chr19\t1290\t1389\t0\tnoname\t+\n")
        self.bed_control_file.write("chr21\t1211190\t1311189\t0\tnoname\t+\n")
        self.bed_control_file.write("chrX\t2\t23\t0\tnoname\t+\n")
        self.bed_control_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.bed_control_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.bed_control_file.write("chrX\t2290\t2389\t0\tnoname\t+\n")
        self.bed_control_file.write("chrX\t2320\t2399\t0\tnoname\t+\n")
        self.bed_control_file.flush()
        
        self.subtracted_path = self.files_dir + 'test_subtracted.pk'
        self.subtracted_file = open(self.subtracted_path,'w+')
        self.subtracted_file.write('chr1\t1\t100\t100:3.00\t3.0\t.\t50\t300.0\n')
        self.subtracted_file.write('chr18\t360\t389\t15:2.00|15:1.00\t2.0\t.\t367\t45.0\n')
        self.subtracted_file.write('chr18\t460\t469\t10:1.00\t1.0\t.\t464\t10.0\n')
        self.subtracted_file.write('chr2\t1\t100\t100:1.00\t1.0\t.\t50\t100.0\n')
        self.subtracted_file.write('chrX\t101\t149\t1:1.00|48:2.00\t2.0\t.\t125\t97.0\n')
        self.subtracted_file.write('chrX\t2270\t2369\t30:1.00|20:2.00|50:1.00\t2.0\t.\t2309\t120.0\n')
        self.subtracted_file.flush()

        self.convert_operation = Turbomix(self.bed_path, self.conversion_result_path, experiment_format=BED, output_format=PK, rounding=False, verbose=False) 

        #self.convert_operation = Turbomix(self.bed_path, self.conversion_subtraction_result_path, experiment_format=BED, output_format=PK, debug = True, rounding=False)

        self.extend_operation = Turbomix(self.bed_path, self.extended_result_path, experiment_format=BED, output_format=BED,  rounding=False, frag_size = 100, verbose=False)
        self.extend_operation.operations = [EXTEND]

        self.subtract_operation = Turbomix(self.notsubtracted_path, self.subtract_result_path, experiment_format=BED, rounding=False, control_path = self.bed_control_path, control_format = BED, verbose=False)
        self.subtract_operation.operations = [SUBTRACT]
    
    def test_add_slash_to_path(self):
        self.assertEqual(self.convert_operation._add_slash_to_path('/random/path'), '/random/path/')
        self.assertEqual(self.convert_operation._add_slash_to_path('/random/path/'), '/random/path/')
     
    def test_convert_clustering(self):
        self.convert_operation.run()
        self.pk_file.seek(0)
        for result_line in file(self.conversion_result_path):
            self.assertEqual(result_line, self.pk_file.next())
    
    def test_subtract(self):
        self.subtract_operation.run()
        self.subtracted_file.seek(0)
        for result_line in file(self.subtract_result_path):
            self.assertEqual(result_line, self.subtracted_file.next())
    
    def fact(self, x):
        return (1 if x==0 else x * self.fact(x-1))

    def test_poisson(self):
        k = 5
        lamb = 8
        poisson_naive = (math.e**(-lamb)*lamb**k)/self.fact(k)
        self.assertAlmostEqual(utils.poisson(k,lamb), poisson_naive)

    def test_extend(self):
        self.extend_operation.run()
        self.bed_extended_file.seek(0)
        for result_line in file(self.extended_result_path):
            self.assertEqual(result_line, self.bed_extended_file.next())  

    """
    Outdated test, extract and write is not controlling the artifacts anymore

    def test_write_if(self):
        weirdOperation = Turbomix(self.bed_path, self.dummy_path,input_format=BED, output_format=PK, debug = True, rounding=False)
        weirdOperation.operations = [NOWRITE]
        artifact1 = Cluster(read=PK, write=PK)
        artifact1.read_line('chr1     16      40      5:1|10:5|5:7|5:8')
        significant = Cluster(read=PK, write=PK)
        significant.read_line('chr1     16      40      5:1|100:5|5:27|5:8')
        artifact2 = Cluster(read=PK, write=PK)
        artifact2.read_line('chr1     16      40      5:1|10:5|500:27|5:8')
        f = file(self.dummy_path, 'w+')
        weirdOperation.extract_and_write(artifact1, f)
        weirdOperation.extract_and_write(significant, f)
        weirdOperation.extract_and_write(artifact2, f)
        length = 0
        f.seek(0)
        for line in f:
            length += 1
        self.assertEqual(length, 0)

        weirdOperation.operations = [Cut, DISCARD_ARTIFACTS]
        weirdOperation.extract_and_write(artifact1, f)
        weirdOperation.extract_and_write(significant, f)
        weirdOperation.extract_and_write(artifact2, f)
        f.seek(0)
        for line in f:
            length += 1
        self.assertEqual(length, 1)
    """

def suite():
   suite = unittest.TestSuite()
   suite.addTest(unittest.makeSuite(TestOperations))
   return suite

if __name__ == '__main__':
    unittest.main()
