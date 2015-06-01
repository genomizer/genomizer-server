INSERT INTO Experiment VALUES('ExpSmall');
INSERT INTO Experiment VALUES('ExpBig');



INSERT INTO Annotation VALUES('Species', 'DropDown', NULL, TRUE);
INSERT INTO Annotation_Choices  VALUES('Species', '');
INSERT INTO Annotation_Choices VALUES('Species', 'Fly');
INSERT INTO Annotation_Choices VALUES('Species', 'Rat');
INSERT INTO Annotation_Choices VALUES('Species', 'Human');

-- This creates a user called "testuser" with the obvious password
INSERT INTO User_Info VALUES('testadmin', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', 'ADMIN','Test Admin','test.admin.pvt@cs.umu.se');
INSERT INTO User_Info VALUES('testuser', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', 'USER','Test User','test.user.pvt@cs.umu.se');
INSERT INTO User_Info VALUES('testguest', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', 'GUEST','Test Guest','test.guest.pvt@cs.umu.se');


INSERT INTO Genome_Release VALUES('GenomV1', 'Fly', '/data/genome_releases/Incect/GenomV1/');

INSERT INTO Genome_Release_Files VALUES('GenomV1', 'd_melanogaster_fb5_22.1.ebwt', NULL, 'Done');
INSERT INTO Genome_Release_Files VALUES('GenomV1', 'd_melanogaster_fb5_22.2.ebwt', NULL, 'Done');
INSERT INTO Genome_Release_Files VALUES('GenomV1', 'd_melanogaster_fb5_22.3.ebwt', NULL, 'Done');
INSERT INTO Genome_Release_Files VALUES('GenomV1', 'd_melanogaster_fb5_22.4.ebwt', NULL, 'Done');
INSERT INTO Genome_Release_Files VALUES('GenomV1', 'd_melanogaster_fb5_22.rev.1.ebwt', NULL, 'Done');
INSERT INTO Genome_Release_Files VALUES('GenomV1', 'd_melanogaster_fb5_22.rev.2.ebwt', NULL, 'Done');



INSERT INTO File VALUES(DEFAULT, '/data/ExpSmall/raw/smalltest1.fastq', 'Raw', 'smalltest1.fastq', CURRENT_TIMESTAMP, NULL, NULL, 'UCSC', 'user1', 'false', 'ExpSmall', 'GenomV1', 'Done', NULL);
INSERT INTO File VALUES(DEFAULT, '/data/ExpSmall/raw/smalltest2.fastq', 'Raw', 'smalltest2.fastq', CURRENT_TIMESTAMP, NULL, NULL, 'UCSC', 'user1', 'false', 'ExpSmall', 'GenomV1', 'Done', NULL);
INSERT INTO File VALUES(DEFAULT, '/data/ExpBig/raw/bigtest1.fastq', 'Raw', 'bigtest1.fastq', CURRENT_TIMESTAMP, NULL, NULL, 'UCSC', 'user1', 'false', 'ExpBig', 'GenomV1', 'Done', NULL);
INSERT INTO File VALUES(DEFAULT, '/data/ExpBig/raw/bigtest2.fastq', 'Raw', 'bigtest2.fastq', CURRENT_TIMESTAMP, NULL, NULL, 'UCSC', 'user1', 'false', 'ExpBig', 'GenomV1', 'Done', NULL);



INSERT INTO Annotated_With  VALUES('ExpSmall', 'Species', 'Fly');
INSERT INTO Annotated_With  VALUES('ExpBig', 'Species', 'Fly');