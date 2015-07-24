INSERT INTO User_Info VALUES('yuri', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', 'ADMIN','Yuri Schwartz','yuri.schwartz@molbiol.umu.se');

INSERT INTO User_Info VALUES('per', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', 'ADMIN','Per Stenberg','per.stenberg@molbiol.umu.se');

INSERT INTO User_Info VALUES('jan', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', '$2a$10$i31YyxPjtntIVvslbM.esOKv4TaWzssP.MkHw5DpOCs3NhJp4goKq', 'ADMIN','Jan Larsson','jan.larsson@molbiol.umu.se');

INSERT INTO Annotation VALUES ('Species', 'DropDown', NULL, TRUE); 
INSERT INTO Annotation_Choices VALUES('Species', ''); 
INSERT INTO Annotation_Choices VALUES('Species', 'Human'); 
INSERT INTO Annotation_Choices VALUES('Species', 'Fly'); 
INSERT INTO Annotation_Choices VALUES('Species', 'Rat'); 
INSERT INTO Annotation_Choices VALUES('Species', 'Insect'); 
 
INSERT INTO Annotation VALUES('Sex', 'DropDown', 'Unknown', FALSE); 
INSERT INTO Annotation_Choices  VALUES('Sex', ''); 
INSERT INTO Annotation_Choices VALUES('Sex', 'Female'); 
INSERT INTO Annotation_Choices VALUES('Sex', 'Male'); 
INSERT INTO Annotation_Choices VALUES('Sex', 'Unknown'); 
INSERT INTO Annotation_Choices  VALUES('Sex', 'Does_not_matter'); 
 
 
INSERT INTO Annotation VALUES('Tissue', 'FreeText', NULL, TRUE); 
 
INSERT INTO Annotation VALUES('Development_Stage', 'FreeText', NULL, FALSE);

