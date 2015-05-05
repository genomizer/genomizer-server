INSERT INTO Experiment VALUES('Exp1');
INSERT INTO Experiment VALUES('Exp2');
INSERT INTO Experiment VALUES('Exp3');
INSERT INTO Experiment VALUES('Exp4');

INSERT INTO Genome_Release VALUES('hg38', 'Human', '/var/www/data/genome_releases/Human/hg38/');
INSERT INTO Genome_Release VALUES('hg19', 'Human', '/var/www/data/genome_releases/Human/hg19/');
INSERT INTO Genome_Release VALUES('hg18', 'Human', '/var/www/data/genome_releases/Human/hg18/');
INSERT INTO Genome_Release VALUES('rn5', 'Rat', '/var/www/data/genome_releases/Rat/rn5/');
INSERT INTO Genome_Release VALUES('rn4', 'Rat', '/var/www/data/genome_releases/Rat/rn4/');
INSERT INTO Genome_Release VALUES('rn3', 'Rat', '/var/www/data/genome_releases/Rat/rn3/');

INSERT INTO Genome_Release_Files VALUES('hg38', 'hg38.fasta', DEFAULT);
INSERT INTO Genome_Release_Files VALUES('hg38', 'hg38(2).fasta', DEFAULT);
INSERT INTO Genome_Release_Files VALUES('hg19', 'hg19.fasta', DEFAULT);
INSERT INTO Genome_Release_Files VALUES('hg19', 'hg19(2).fasta', DEFAULT);
INSERT INTO Genome_Release_Files VALUES('hg18', 'hg18.fasta', DEFAULT);
INSERT INTO Genome_Release_Files VALUES('rn5', 'rn5.fasta', DEFAULT);
INSERT INTO Genome_Release_Files VALUES('rn4', 'rn4.fasta', DEFAULT);
INSERT INTO Genome_Release_Files VALUES('rn3', 'rn3.fasta', DEFAULT);

INSERT INTO Chain_File VALUES('rn3', 'rn4', '/var/www/data/chain_files/Rat/rn3 - rn4/');
INSERT INTO Chain_File VALUES('rn4', 'rn5', '/var/www/data/chain_files/Rat/rn4 - rn5/');
INSERT INTO Chain_File VALUES('hg18', 'hg38', '/var/www/data/chain_files/Human/hg18 - hg38/');
INSERT INTO Chain_File VALUES('hg38', 'hg18', '/var/www/data/chain_files/Human/hg38 - hg18/');

INSERT INTO Chain_File_Files VALUES('rn3', 'rn4', 'rn3ToRn4.chain.over', DEFAULT);
INSERT INTO Chain_File_Files VALUES('rn3', 'rn4', 'rn3ToRn4.chain(2).over', DEFAULT);
INSERT INTO Chain_File_Files VALUES('rn3', 'rn4', 'rn3ToRn4.chain(3).over', DEFAULT);
INSERT INTO Chain_File_Files VALUES('rn4', 'rn5', 'rn4ToRn5.over.chain', DEFAULT);
INSERT INTO Chain_File_Files VALUES('hg18', 'hg38', 'hg18ToHg38.over.chain', DEFAULT);
INSERT INTO Chain_File_Files VALUES('hg38', 'hg18', 'hg38ToHg18.over.chain', DEFAULT);
INSERT INTO Chain_File_Files VALUES('hg38', 'hg18', 'hg38ToHg18.over(2).chain', DEFAULT);

INSERT INTO Annotation VALUES ('Species', 'DropDown', NULL, TRUE);
INSERT INTO Annotation_Choices VALUES('Species', 'Human');
INSERT INTO Annotation_Choices VALUES('Species', 'Fly');
INSERT INTO Annotation_Choices VALUES('Species', 'Rat');


INSERT INTO Annotation VALUES('Sex', 'DropDown', 'Unknown', FALSE);
INSERT INTO Annotation_Choices VALUES('Sex', 'Female');
INSERT INTO Annotation_Choices VALUES('Sex', 'Male');
INSERT INTO Annotation_Choices VALUES('Sex', 'Unknown');
INSERT INTO Annotation_Choices  VALUES('Sex', 'Does not matter');


INSERT INTO Annotation VALUES('Tissue', 'FreeText', NULL, TRUE);

INSERT INTO Annotation VALUES('Development Stage', 'FreeText', NULL, FALSE);


INSERT INTO Annotated_With  VALUES('Exp1', 'Species', 'Human');
INSERT INTO Annotated_With  VALUES('Exp1', 'Sex', 'Unknown');
INSERT INTO Annotated_With  VALUES('Exp1', 'Tissue', 'Arm');
INSERT INTO Annotated_With  VALUES('Exp1', 'Development Stage', 'Adult');

INSERT INTO Annotated_With  VALUES('Exp2', 'Species', 'Human');
INSERT INTO Annotated_With  VALUES('Exp2', 'Sex', 'Does not matter');
INSERT INTO Annotated_With  VALUES('Exp2', 'Tissue', 'Arm');
INSERT INTO Annotated_With  VALUES('Exp2', 'Development Stage', 'Child');

INSERT INTO Annotated_With  VALUES('Exp3', 'Species', 'Rat');
INSERT INTO Annotated_With  VALUES('Exp3', 'Development Stage', 'Child');

-- This creates a user called "testuser" with the obvious password
INSERT INTO User_Info VALUES('testuser', '2287314fffa14d77e1868fb51700ff893066472b4d6e94f1c498863bde10b992', 'a36af7fe4a6d300cb9a76711695b9e8ec5c40bc2a3c9c0a63e005b81fed153e4', 'admin','Test User','test.user.pvt@cs.umu.se');


INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp1/raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/var/www/data/Exp1/raw/file1_input.fastq', 'Umeå Uni', 'user1', 'True', 'Exp1', NULL, DEFAULT);

INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp1/profile/0/file1.sam', 'Profile', 'file1.sam', CURRENT_TIMESTAMP, '-n 1 --best', '/var/www/data/Exp1/Profile/0/file1_input.sam', 'Genomizer', 'user1', 'false', 'Exp1', 'hg38', DEFAULT);


INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp2/raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/var/www/data/Exp2/raw/file1_input.fastq', 'UCSC', 'user1', 'false', 'Exp2', NULL, DEFAULT);


INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp3/raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/var/www/data/Exp3/raw/file1_input.fastq', 'UCSC', 'user1', 'false', 'Exp3', NULL, DEFAULT);

INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp3/profile/0/file1.sam', 'Profile', 'file1.sam', CURRENT_TIMESTAMP, '-n 1 --best', '/var/www/data//Exp3/profile/0/file1_input.sam', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp3/profile/1/file1.sam', 'Profile', 'file2.sam', CURRENT_TIMESTAMP, '-n 2 --best', '/var/www/data/Exp3/profile/1/file1_input.sam', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);


INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/BED-testdata.bed', 'Profile', 'BED-testdata.bed', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/BED-testdata.bed', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/GFF-testdata.gff', 'Profile', 'GFF-testdata.gff', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/GFF-testdata.gff', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/SGR-testdata.sgr', 'Profile', 'SGR-testdata.sgr', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/SGR-testdata.sgr', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/SGR-testdata-2.sgr', 'Profile', 'sgrTest2.sam', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/SGR-testdata-2.sgr', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/SGR-testdata-3.sgr', 'Profile', 'sgrTest3.sam', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/SGR-testdata-3.sgr', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/SGR-testdata-4.sgr', 'Profile', 'sgrTest4.sam', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/SGR-testdata-4.sgr', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/WIG-from-SGR-testdata.sgr', 'Profile', 'sgr2wigTest.wig', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/WIG-from-SGR-testdata.sgr', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/WIG-testdata.wig', 'Profile', 'WIG-testdata.wig', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/WIG-testdata.wig', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);

INSERT INTO File VALUES(DEFAULT, 'resources/conversionTestData/WIG-varstep-testdata.wig', 'Profile', 'wigVarStepTest.wig', CURRENT_TIMESTAMP, '-n --best', 'resources/conversionTestData/WIG-varstep-testdata.wig', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5', DEFAULT);
