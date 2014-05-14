INSERT INTO Experiment VALUES('Exp1');
INSERT INTO Experiment VALUES('Exp2');
INSERT INTO Experiment VALUES('Exp3');
INSERT INTO Experiment VALUES('Exp4');

INSERT INTO Genome_Release VALUES('hg38', 'Human', '/var/www/data/GenomeRelease/Human/hg38.fasta');
INSERT INTO Genome_Release VALUES('hg19', 'Human', '/var/www/data/GenomeRelease/Human/hg19.fasta');
INSERT INTO Genome_Release VALUES('hg18', 'Human', '/var/www/data/GenomeRelease/Human/hg18.fasta');
INSERT INTO Genome_Release VALUES('rn5', 'Rat', '/var/www/data/GenomeRelease/Rat/rn5.fasta');
INSERT INTO Genome_Release VALUES('rn4', 'Rat', '/var/www/data/GenomeRelease/Rat/rn4.fasta');
INSERT INTO Genome_Release VALUES('rn3', 'Rat', '/var/www/data/GenomeRelease/Rat/rn3.fasta');

INSERT INTO Chain_File VALUES('rn3', 'rn4', '/var/www/data/Chain_File/Rat/rn3-rn4.fasta');
INSERT INTO Chain_File VALUES('rn4', 'rn5', '/var/www/data/Chain_File/Rat/rn4-rn5.fasta');
INSERT INTO Chain_File VALUES('hg19', 'hg38', '/var/www/data/Chain_File/Human/hg19-hg38.fasta');
INSERT INTO Chain_File VALUES('hg18', 'hg19', '/var/www/data/Chain_File/Human/hg18-hg19.fasta');

INSERT INTO Annotation VALUES('Species', 'DropDown', NULL, TRUE);
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

INSERT INTO User_Info VALUES('user1', 'secret1', 'role1');

INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp1/raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/var/www/data/Exp1/raw/file1_input.fastq', 'Umeå Uni', 'user1', 'True', 'Exp1', NULL);

INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp1/profile/file1.sam', 'Profile', 'file1.sam', CURRENT_TIMESTAMP, '-n 1 --best', '/var/www/data/Exp1/Profile/file1_input.sam', 'Genomizer', 'user1', 'false', 'Exp1', 'hg38');


INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp2/raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/var/www/data/Exp2/raw/file1_input.fastq', 'UCSC', 'user1', 'false', 'Exp2', NULL);


INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp3/raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/var/www/data/Exp3/raw/file1_input.fastq', 'UCSC', 'user1', 'false', 'Exp3', NULL);

INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp3/profile/file1.sam', 'Profile', 'file1.sam', CURRENT_TIMESTAMP, '-n 1 --best', '/var/www/data//Exp3/profile/file1_input.sam', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5');

INSERT INTO File VALUES(DEFAULT, '/var/www/data/Exp3/profile/file2.sam', 'Profile', 'file2.sam', CURRENT_TIMESTAMP, '-n 2 --best', '/var/www/data/Exp3/profile/file2_input.sam', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5');

