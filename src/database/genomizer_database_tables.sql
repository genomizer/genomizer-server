CREATE TABLE File
(
    FileID SERIAL NOT NULL,
    Path VARCHAR(1024) UNIQUE NOT NULL,
    FileType VARCHAR(32) NOT NULL,
    FileName VARCHAR(512) NOT NULL,
    Date DATE NOT NULL,
    MetaData VARCHAR(256),
    InputFilePath VARCHAR(1024),
    Author VARCHAR(32),
    Uploader VARCHAR(32) NOT NULL,
    IsPrivate BOOLEAN NOT NULL,
    ExpID VARCHAR(256),
    GRVersion VARCHAR(16),
    Status VARCHAR(16) DEFAULT 'In Progress',
    CONSTRAINT pkey_file PRIMARY KEY(FileID)
);

CREATE TABLE Annotation
(
    Label VARCHAR(32) NOT NULL,
    DataType VARCHAR(16) NOT NULL,
    DefaultValue VARCHAR(32),
    Required BOOLEAN NOT NULL,
    CONSTRAINT pkey_annotation PRIMARY KEY(Label)
);

CREATE TABLE Experiment
(
    ExpID VARCHAR(256) NOT NULL,
    CONSTRAINT pkey_experiment PRIMARY KEY(ExpID)
);

ALTER TABLE File ADD CONSTRAINT fkey_expid FOREIGN KEY (ExpID) REFERENCES Experiment(ExpID) ON UPDATE CASCADE;

CREATE TABLE Annotated_With
(
    ExpID VARCHAR(256) NOT NULL,
    Label VARCHAR(32) NOT NULL,
    Value VARCHAR(32) NOT NULL,
    CONSTRAINT pkey_annotated_with PRIMARY KEY(ExpID, Label),
    CONSTRAINT fkey_expid FOREIGN KEY (ExpID) REFERENCES Experiment(ExpID) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fkey_label FOREIGN KEY (Label) REFERENCES Annotation(Label) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Annotation_Choices
(
    Label VARCHAR(32) NOT NULL,
    Value VARCHAR(32) NOT NULL,
    CONSTRAINT pkey_annotation_choices PRIMARY KEY(Label, Value),
    CONSTRAINT fkey_label FOREIGN KEY (Label) REFERENCES Annotation(Label) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE User_Info
(
    Username VARCHAR(32) NOT NULL,
    Password VARCHAR(32) NOT NULL,
    Role VARCHAR(32) NOT NULL,
    FullName VARCHAR(62),
    Email VARCHAR(62),
    CONSTRAINT pkey_user_info PRIMARY KEY(Username)
);

CREATE TABLE Working_on
(
    Username VARCHAR(32) NOT NULL,
	WorkspaceID VARCHAR(32) NOT NULL,
	CONSTRAINT pkey_working_on PRIMARY KEY(Username, WorkspaceID),
	CONSTRAINT fkey_username FOREIGN KEY (Username) REFERENCES User_Info(Username)
);

CREATE TABLE Workspace
(
	WorkspaceID VARCHAR(32) NOT NULL,
	Name VARCHAR(64) NOT NULL,
	CONSTRAINT pkey_workspace PRIMARY KEY(WorkspaceID)
);

ALTER TABLE Working_on ADD CONSTRAINT fkey_workspaceid FOREIGN KEY (WorkspaceID) REFERENCES Workspace(WorkspaceID);

CREATE TABLE Used_In
(
	FileID INT NOT NULL,
	WorkspaceID VARCHAR(32) NOT NULL,
	VPath VARCHAR(128) NOT NULL,
	CONSTRAINT pkey_used_in PRIMARY KEY(FileID, WorkspaceID, VPath),
	CONSTRAINT fkey_fileid FOREIGN KEY (FileID) REFERENCES File(FileID),
	CONSTRAINT fkey_workspaceid FOREIGN KEY (WorkspaceID) REFERENCES Workspace(WorkspaceID)
);

CREATE TABLE Published_In
(
    FileID INT NOT NULL,
    PMID INT NOT NULL,
    CONSTRAINT pkey_published_in PRIMARY KEY(FileID, PMID),
    CONSTRAINT fkey_fileid FOREIGN KEY (FileID) REFERENCES File(FileID)
);

CREATE TABLE Genome_Release
(
    Version VARCHAR(16) NOT NULL,
    Species VARCHAR(32) NOT NULL,
    FolderPath VARCHAR(512) NOT NULL,
    CONSTRAINT pkey_genome_release PRIMARY KEY(Version)
);

ALTER TABLE File ADD CONSTRAINT fkey_grversion FOREIGN KEY (GRVersion) REFERENCES Genome_Release(Version);

CREATE TABLE Genome_Release_Files
(
    Version VARCHAR(16) NOT NULL,
    FileName VARCHAR(512) NOT NULL,
    Status VARCHAR(16) DEFAULT 'In Progress',
    CONSTRAINT pkey_version_filename PRIMARY KEY(Version, FileName),
    CONSTRAINT fkey_version FOREIGN KEY (Version) REFERENCES Genome_Release(Version) ON DELETE CASCADE
);

CREATE TABLE Chain_File
(
    FromVersion VARCHAR(16) NOT NULL,
    ToVersion VARCHAR(16) NOT NULL,
    FolderPath VARCHAR(512) NOT NULL,
    CONSTRAINT pkey_chain_file PRIMARY KEY(FromVersion, ToVersion),
    CONSTRAINT fkey_from_version FOREIGN KEY (FromVersion) REFERENCES Genome_Release(Version),
    CONSTRAINT fkey_to_version FOREIGN KEY (ToVersion) REFERENCES Genome_Release(Version)
);

CREATE TABLE Chain_File_Files
(
    FromVersion VARCHAR(16) NOT NULL,
    ToVersion VARCHAR(16) NOT NULL,
    FileName VARCHAR(512) NOT NULL,
    Status VARCHAR(16) DEFAULT 'In Progress',
    CONSTRAINT pkey_chain_file_files PRIMARY KEY(FromVersion, ToVersion, fileName),
    CONSTRAINT fkey_from_version_to_version FOREIGN KEY (FromVersion, ToVersion) REFERENCES Chain_File(FromVersion, ToVersion) ON DELETE CASCADE
);

INSERT INTO Annotation VALUES ('Species', 'DropDown', 'Fly', TRUE);
INSERT INTO Annotation_Choices VALUES ('Species', 'Fly');










