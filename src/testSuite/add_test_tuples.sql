INSERT INTO Experiment VALUES('Exp1');
INSERT INTO Experiment VALUES('Exp2');
INSERT INTO Experiment VALUES('Exp3');
INSERT INTO Experiment VALUES('Exp4');

INSERT INTO Genome_Release VALUES('hg38', 'Human', '/GenomeRelease/Human/hg38.fasta');
INSERT INTO Genome_Release VALUES('rn5', 'Rat', '/GenomeRelease/Rat/rn5.fasta');


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

INSERT INTO File VALUES(DEFAULT, '/Exp1/Raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/Exp1/Raw/file1_input.fastq', 'Umeå Uni', 'user1', 'True', 'Exp1', NULL);

INSERT INTO File VALUES(DEFAULT, '/Exp1/Profile/file1.sam', 'Profile', 'file1.sam', CURRENT_TIMESTAMP, '-n 1 --best', '/Exp1/Profile/file1_input.sam', 'Genomizer', 'user1', 'false', 'Exp1', 'hg38');


INSERT INTO File VALUES(DEFAULT, '/Exp2/Raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/Exp2/Raw/file1_input.fastq', 'UCSC', 'user1', 'false', 'Exp2', NULL);


INSERT INTO File VALUES(DEFAULT, '/Exp3/Raw/file1.fastq', 'Raw', 'file1.fastq', CURRENT_TIMESTAMP, NULL, '/Exp3/Raw/file1_input.fastq', 'UCSC', 'user1', 'false', 'Exp3', NULL);

INSERT INTO File VALUES(DEFAULT, '/Exp3/Profile/file1.sam', 'Profile', 'file1.sam', CURRENT_TIMESTAMP, '-n 1 --best', '/Exp3/Profile/file1_input.sam', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5');

INSERT INTO File VALUES(DEFAULT, '/Exp3/Profile/file2.sam', 'Profile', 'file2.sam', CURRENT_TIMESTAMP, '-n 2 --best', '/Exp3/Profile/file2_input.sam', 'Genomizer', 'user1', 'false', 'Exp3', 'rn5');






