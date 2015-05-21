import unittest
import tempfile
from pyicoteolib.regions import *
import os

class TestRegions(unittest.TestCase):
    def setUp(self):
        gff_text = """chr1	TESTSRC	gene	1000	3000	.	+	.	gene_id "TESTGENE001"; transcript_id "TESTGENE001"; gene_type "protein_coding"; transcript_type "protein_coding"
chr1	TESTSRC	transcript	1000	2000	.	+	.	gene_id "TESTGENE001"; transcript_id "TESTTRANSCRIPT001"; gene_type "protein_coding"; transcript_type "protein_coding"
chr1	TESTSRC	exon	1000	1400	.	+	.	gene_id "TESTGENE001"; transcript_id "TESTTRANSCRIPT001"; gene_type "protein_coding"; transcript_type "protein_coding"
chr1	TESTSRC	exon	1500	2000	.	+	.	gene_id "TESTGENE001"; transcript_id "TESTTRANSCRIPT001"; gene_type "protein_coding"; transcript_type "protein_coding"
chr1	TESTSRC	gene	5000	6500	.	-	.	gene_id "TESTGENE002"; transcript_id "TESTGENE002"; gene_type "protein_coding"; transcript_type "protein_coding"
chr1	TESTSRC	transcript	5500	6500	.	-	.	gene_id "TESTGENE002"; transcript_id "TESTTRANSCRIPT002"; gene_type "protein_coding"; transcript_type "protein_coding"
chr1	TESTSRC	exon	5500	5700	.	-	.	gene_id "TESTGENE002"; transcript_id "TESTTRANSCRIPT002"; gene_type "protein_coding"; transcript_type "protein_coding"
chr1	TESTSRC	exon	5800	6000	.	-	.	gene_id "TESTGENE002"; transcript_id "TESTTRANSCRIPT002"; gene_type "protein_coding"; transcript_type "protein_coding"
chr1	TESTSRC	exon	6100	6500	.	-	.	gene_id "TESTGENE002"; transcript_id "TESTTRANSCRIPT002"; gene_type "protein_coding"; transcript_type "protein_coding"
chr2	TESTSRC	gene	1000	4000	.	+	.	gene_id "TESTGENE003"; transcript_id "TESTGENE003"; gene_type "protein_coding"; transcript_type "protein_coding"
chr2	TESTSRC	transcript	1500	2500	.	+	.	gene_id "TESTGENE003"; transcript_id "TESTTRANSCRIPT003"; gene_type "protein_coding"; transcript_type "protein_coding"
chr2	TESTSRC	exon	1500	2000	.	+	.	gene_id "TESTGENE003"; transcript_id "TESTTRANSCRIPT003"; gene_type "protein_coding"; transcript_type "protein_coding"
chr2	TESTSRC	exon	2000	2500	.	+	.	gene_id "TESTGENE003"; transcript_id "TESTTRANSCRIPT003"; gene_type "protein_coding"; transcript_type "protein_coding"
"""
        self.temp_gff = tempfile.mkstemp(prefix="gfftest")[1]
        f = open(self.temp_gff, 'w')
        f.write(gff_text)
        f.flush()
        f.close()
        self.tempfiles = [self.temp_gff]
        

    def tearDown(self):
        for f in self.tempfiles: # remove all created temporary files
            os.remove(f)


    def test_get_exons(self):
        expected_result = """chr1	1000	1400	TESTGENE001:TESTTRANSCRIPT001:1000:1400	0	+
chr1	1500	2000	TESTGENE001:TESTTRANSCRIPT001:1500:2000	0	+
chr1	5500	5700	TESTGENE002:TESTTRANSCRIPT002:5500:5700	0	-
chr1	5800	6000	TESTGENE002:TESTTRANSCRIPT002:5800:6000	0	-
chr1	6100	6500	TESTGENE002:TESTTRANSCRIPT002:6100:6500	0	-
chr2	1500	2000	TESTGENE003:TESTTRANSCRIPT003:1500:2000	0	+
chr2	2000	2500	TESTGENE003:TESTTRANSCRIPT003:2000:2500	0	+
"""
        exons_path = tempfile.mkstemp(prefix="exonstest")[1]
        self.tempfiles.append(exons_path)
        exons_file = open(exons_path, 'w')
        regwriter = RegionWriter(self.temp_gff, exons_file, ["exons"], write_as=BED)
        regwriter.write_regions()
        exons_file.flush()
        exons_file.close()

        exons_file = open(exons_path, 'r')
        exons_text = ''.join(exons_file.readlines())
        exons_file.close()
        self.assertEqual(exons_text, expected_result)


    def test_get_introns(self):
        expected_result = """chr1	1400	1500	TESTGENE001:TESTTRANSCRIPT001:1500:2000	0	+
chr1	5700	5800	TESTGENE002:TESTTRANSCRIPT002:5800:6000	0	-
chr1	6000	6100	TESTGENE002:TESTTRANSCRIPT002:6100:6500	0	-
"""
        introns_path = tempfile.mkstemp(prefix="intronstest")[1]
        self.tempfiles.append(introns_path)
        introns_file = open(introns_path, 'w')
        regwriter = RegionWriter(self.temp_gff, introns_file, ["introns"], write_as=BED)
        regwriter.write_regions()
        introns_file.flush()
        introns_file.close()

        introns_file = open(introns_path, 'r')
        introns_text = ''.join(introns_file.readlines())
        introns_file.close()
        self.assertEqual(introns_text, expected_result)


    def test_generate_windows_inter(self):
        expected_result = """chr1	3000	4000	3000:4000	0	.
chr1	3500	4500	3500:4500	0	.
chr1	4000	5000	4000:5000	0	.
chr1	4000	5000	4000:5000	0	.
chr1	6500	7500	6500:7500	0	.
chr1	7000	8000	7000:8000	0	.
chr1	7000	8000	7000:8000	0	.
chr2	4000	5000	4000:5000	0	.
chr2	4500	5500	4500:5500	0	.
chr2	5000	6000	5000:6000	0	.
chr2	5500	6500	5500:6500	0	.
chr2	6000	7000	6000:7000	0	.
chr2	6500	7500	6500:7500	0	.
chr2	7000	8000	7000:8000	0	.
chr2	7500	8500	7500:8500	0	.
chr2	8000	9000	8000:9000	0	.
chr2	8000	9000	8000:9000	0	.
"""
        chrlen_path = tempfile.mkstemp(prefix="chrlentest")[1]
        self.tempfiles.append(chrlen_path)
        chrlen_file = open(chrlen_path, 'w')
        chrlen_file.write("""chr1	8000
chr2	9000
""")
        chrlen_file.flush()
        chrlen_file.close()

        slideinter_path = tempfile.mkstemp(prefix="slideintertest")[1]
        self.tempfiles.append(slideinter_path)
        slideinter_file = open(slideinter_path, 'w')
        regwriter = RegionWriter(self.temp_gff, slideinter_file, ["slide", 1000, 500, "inter", chrlen_path], write_as=BED)
        regwriter.write_regions()
        slideinter_file.flush()
        slideinter_file.close()

        slideinter_file = open(slideinter_path, 'r')
        slideinter_text = ''.join(slideinter_file.readlines())
        slideinter_file.close()
        self.assertEqual(slideinter_text, expected_result)


    def test_generate_windows_intra(self):
        expected_result = """chr1	1000	2000	1000:2000	0	.
chr1	1500	2500	1500:2500	0	.
chr1	2000	3000	2000:3000	0	.
chr1	2000	3000	2000:3000	0	.
chr1	5000	6000	5000:6000	0	.
chr1	5500	6500	5500:6500	0	.
chr1	5500	6500	5500:6500	0	.
chr2	1000	2000	1000:2000	0	.
chr2	1500	2500	1500:2500	0	.
chr2	2000	3000	2000:3000	0	.
chr2	2500	3500	2500:3500	0	.
chr2	3000	4000	3000:4000	0	.
chr2	3000	4000	3000:4000	0	.
"""
        slideintra_path = tempfile.mkstemp(prefix="slideintratest")[1]
        self.tempfiles.append(slideintra_path)
        slideintra_file = open(slideintra_path, 'w')
        regwriter = RegionWriter(self.temp_gff, slideintra_file, ["slide", 1000, 500, "intra"], write_as=BED)
        regwriter.write_regions()
        slideintra_file.flush()
        slideintra_file.close()

        slideintra_file = open(slideintra_path, 'r')
        slideintra_text = ''.join(slideintra_file.readlines())
        slideintra_file.close()
        self.assertEqual(slideintra_text, expected_result)


    def test_get_tss(self):
        expected_result = """chr1	900	1050	TESTTRANSCRIPT001	0	+
chr1	6450	6600	TESTTRANSCRIPT002	0	-
chr2	1400	1550	TESTTRANSCRIPT003	0	+
"""
        tss_path = tempfile.mkstemp(prefix="tsstest")[1]
        self.tempfiles.append(tss_path)
        tss_file = open(tss_path, 'w')
        regwriter = RegionWriter(self.temp_gff, tss_file, ["tss", 100, 50], write_as=BED)
        regwriter.write_regions()
        tss_file.flush()
        tss_file.close()

        tss_file = open(tss_path, 'r')
        tss_text = ''.join(tss_file.readlines())
        tss_file.close()
        self.assertEqual(tss_text, expected_result)


def suite():
    suite = unittest.TestSuite()
    suite.addTest(unittest.makeSuite(TestRegions))
    return suite

