import unittest
from pyicoteolib.utils import DualSortedReader
from pyicoteolib.core import BED

class TestUtils(unittest.TestCase):
    
    def test_dual_reader(self):
        reader = DualSortedReader("test_files/mini_sorted.bed", "test_files/mini_sorted2.bed", BED, False, False)
        merged_file = open("test_files/mini_sorted_merged.bed")
        for line in reader:
            if line:
                self.assertEqual(line, merged_file.next())

def suite():
   suite = unittest.TestSuite()
   suite.addTest(unittest.makeSuite(TestUtils))
   return suite
