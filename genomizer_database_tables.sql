CREATE TABLE File
(
    FileID SERIAL NOT NULL,
    Path VARCHAR(128) UNIQUE NOT NULL,
    FileType VARCHAR(32) NOT NULL,
    Date DATE NOT NULL,
    MetaData VARCHAR(256),
    Author VARCHAR(32) NOT NULL,
    Uploader VARCHAR(32),
    IsPrivate BOOLEAN NOT NULL,
    ExpID VARCHAR(64),
    GRVersion VARCHAR(16),
    CONSTRAINT pkey_file PRIMARY KEY(FileID)
);

CREATE TABLE Annotation
(
    Label VARCHAR(32) NOT NULL,
    DataType VARCHAR(16) NOT NULL,
    CONSTRAINT pkey_annotation PRIMARY KEY(Label)
);

CREATE TABLE Experiment
(
    ExpID VARCHAR(64) NOT NULL,
    CONSTRAINT pkey_experiment PRIMARY KEY(ExpID)
);

ALTER TABLE File ADD CONSTRAINT fkey_expid FOREIGN KEY (ExpID) REFERENCES Experiment(ExpID);

CREATE TABLE Annotated_With
(
    ExpID VARCHAR(64) NOT NULL,
    Label VARCHAR(32) NOT NULL,
    Value VARCHAR(32) NOT NULL,
    CONSTRAINT pkey_annotated_with PRIMARY KEY(ExpID, Label),
    CONSTRAINT fkey_expid FOREIGN KEY (ExpID) REFERENCES Experiment(ExpID) ON DELETE CASCADE,
    CONSTRAINT fkey_label FOREIGN KEY (Label) REFERENCES Annotation(Label) ON DELETE CASCADE
);

CREATE TABLE Annotation_Choices
(
    Label VARCHAR(32) NOT NULL,
    Value VARCHAR(32) NOT NULL,
    CONSTRAINT pkey_annotation_choices PRIMARY KEY(Label, Value),
    CONSTRAINT fkey_label FOREIGN KEY (Label) REFERENCES Annotation(Label) ON DELETE CASCADE
);

CREATE TABLE User_Info
(
    Username VARCHAR(32) NOT NULL,
    Password VARCHAR(32) NOT NULL,
    Role VARCHAR(32) NOT NULL,
    CONSTRAINT pkey_user_info PRIMARY KEY(Username)
);

ALTER TABLE File ADD CONSTRAINT fkey_uploader FOREIGN KEY (Uploader) REFERENCES User_Info(Username) ON DELETE SET NULL;

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
    FilePath VARCHAR(128) NOT NULL,
    CONSTRAINT pkey_genome_release PRIMARY KEY(Version)
);

ALTER TABLE File ADD CONSTRAINT fkey_grversion FOREIGN KEY (GRVersion) REFERENCES Genome_Release(Version);

CREATE TABLE Chain_File
(
    FromVersion VARCHAR(16) NOT NULL,
    ToVersion VARCHAR(16) NOT NULL,
    FilePath VARCHAR(128) NOT NULL,
    CONSTRAINT pkey_chain_file PRIMARY KEY(FromVersion, ToVersion),
    CONSTRAINT fkey_fromversion FOREIGN KEY (FromVersion) REFERENCES Genome_Release(Version),
    CONSTRAINT fkey_toversion FOREIGN KEY (ToVersion) REFERENCES Genome_Release(Version)
);
