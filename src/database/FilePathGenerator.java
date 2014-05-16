package database;

import java.io.File;
import java.io.IOException;

public class FilePathGenerator {

    private String rootDir;

    private static String rawFolderName = "raw";
    private static String profileFolderName = "profile";
    private static String regionFolderName = "region";
    private static String unknownFolderName = "unknown";

    private static String genomeReleaseFolderName = "genome_releases";

    public FilePathGenerator(String rootDir) throws IOException {
        String regex = "([a-zA-Z]:)?(/[a-zA-Z0-9._\" \"-]+)+/?";
        if (rootDir.endsWith(File.separator) && rootDir.matches(regex)) {
            this.rootDir = rootDir;
        } else {
            throw new IOException("rootDir has the wrong format. Make sure it"
                    + "ends with a separator.");
        }
    }

    /**
     * Used when uploading and downloading files. Returns a Dir string where the
     * file is or where is should be saved.
     *
     * @param expID
     *            - the ID for the experiment.
     * @param fileType
     *            - the type of the file.
     * @param fileName
     *            - the name of the file.
     * @return the string for the file.
     */
    @Deprecated
    public String generateFilePath(String expID, String fileType,
            String fileName) {

        fileType = fileType.toLowerCase();

        StringBuilder filePath = new StringBuilder();

        filePath.append(rootDir);
        filePath.append(expID);
        filePath.append(File.separator);
        filePath.append(fileType);
        filePath.append(File.separator);
        filePath.append(fileName);
        return filePath.toString();
    }

    public String generateFilePath(String expID, int fileType, String fileName) {

        StringBuilder filePath = new StringBuilder();

        switch (fileType) {
        case FileTuple.RAW:
            filePath.append(rootDir);
            filePath.append(expID);
            filePath.append(File.separator);
            filePath.append(rawFolderName);
            break;
        case FileTuple.PROFILE:
            filePath.append(generateProfileFolder(expID));
            break;
        case FileTuple.REGION:
            filePath.append(rootDir);
            filePath.append(expID);
            filePath.append(File.separator);
            filePath.append(regionFolderName);
            break;
        default:
            filePath.append(rootDir);
            filePath.append(expID);
            filePath.append(File.separator);
            filePath.append(unknownFolderName);
            break;
        }

        filePath.append(File.separator);
        filePath.append(fileName);
        return filePath.toString();
    }

    /**
     * Used when first adding a new experiment. Creates a folder for the
     * experiment and subfolders for files
     *
     * @param expID
     *            - the ID for the experiment.
     */
    public void generateExperimentFolders(String expID) {

        File file = new File(rootDir + expID + File.separator + rawFolderName
                + File.separator);
        file.mkdirs();

        file = new File(rootDir + expID + File.separator + profileFolderName
                + File.separator);
        file.mkdirs();

        file = new File(rootDir + expID + File.separator + regionFolderName
                + File.separator);
        file.mkdirs();

        file = new File(rootDir + expID + File.separator + unknownFolderName
                + File.separator);
        file.mkdirs();
    }

    /**
     * Creates and returns the path to the folder where the chain files should
     * be stored.
     *
     * @param species
     * @param fromVersion
     * @param toVersion
     * @return The path to the folder where the chain files should be stored.
     */
    public String generateChainFolderPath(String species, String fromVersion,
            String toVersion) {

        StringBuilder folderPath = new StringBuilder();

        folderPath.append(rootDir);
        folderPath.append("chain_files");
        folderPath.append(File.separator);
        folderPath.append(species);
        folderPath.append(File.separator);
        folderPath.append(fromVersion);
        folderPath.append(" - ");
        folderPath.append(toVersion);
        folderPath.append(File.separator);

        File chainFolder = new File(folderPath.toString());

        if (!chainFolder.exists()) {
            chainFolder.mkdirs();
        }

        return folderPath.toString();
    }

    /**
     * Creates and returns the path to the folder where the genome release files
     * should be stored.
     *
     * @param String
     *            version, the genome version of the file,
     * @param String
     *            species, the species that the genome versions belongs to.
     * @return String the path to the folder where the files can be stored.
     */
    public String generateGenomeReleaseFolder(String version, String species) {

        String genomeReleaseFolderPath = getGenomeReleaseFolderPath(version,
                species);
        File genomeReleaseFolder = new File(genomeReleaseFolderPath);

        if (!genomeReleaseFolder.exists()) {
            genomeReleaseFolder.mkdirs();
        }

        return genomeReleaseFolderPath;
    }

    public String getGenomeReleaseFolderPath(String version, String species) {

        StringBuilder folderPath = new StringBuilder();

        folderPath.append(rootDir);
        folderPath.append(genomeReleaseFolderName);
        folderPath.append(File.separator);
        folderPath.append(species);
        folderPath.append(File.separator);
        folderPath.append(version);
        folderPath.append(File.separator);
        return folderPath.toString();
    }

    public String generateProfileFolder(String expId) {

        StringBuilder folderPath = new StringBuilder();

        folderPath.append(rootDir);
        folderPath.append(expId);
        folderPath.append(File.separator);
        folderPath.append(profileFolderName);
        folderPath.append(File.separator);

        File profileFolder = new File(folderPath.toString());
        if (!profileFolder.exists()) {
            profileFolder.mkdirs();
        }

        Integer folderNumber = profileFolder.list().length;
        File profileSubFolder = new File(folderPath.toString()
                + folderNumber.toString() + File.separator);

        while (profileSubFolder.exists()) {
            folderNumber++;
            profileSubFolder = new File(folderPath.toString()
                    + folderNumber.toString() + File.separator);
        }

        profileSubFolder.mkdirs();

        folderPath.append(folderNumber.toString());
        folderPath.append(File.separator);
        return folderPath.toString();
    }

    public void setRootDirectory(String rootDir) throws IOException {
        String regex = "([a-zA-Z]:)?(/[a-zA-Z0-9._\" \"-]+)+/?";
        if (rootDir.endsWith(File.separator) && rootDir.matches(regex)) {
            this.rootDir = rootDir;
        } else {
            throw new IOException(
                    "Root directory has the wrong format. Make sure it"
                            + "ends with a separator.");
        }
    }

    /**
     * Tests if the requested filename(not whole path) is valid on the server
     * file system or a file is already using that name. Returns true if the
     * name can be used, false if it cannot.
     *
     * @param String
     *            fileName
     * @return boolean
     */
    // public boolean isNameOk(String fileName) {
    //
    // File file = new File(homeDir + fileName);
    // boolean isOk = false;
    //
    // if (!file.exists()) {
    // try {
    // isOk = file.createNewFile();
    // } catch (IOException e) {
    // isOk = false;
    // }
    //
    // if (isOk) {
    // file.delete();
    // }
    //
    // }
    //
    // return isOk;
    // }

}