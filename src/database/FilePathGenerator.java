package database;

import java.io.File;
import java.io.IOException;

import database.containers.FileTuple;

/**
 * Generates paths and directories for files.
 */
public class FilePathGenerator {

    private String rootDir;

    private static String rawFolderName = "raw";
    private static String profileFolderName = "profile";
    private static String regionFolderName = "region";
    private static String unknownFolderName = "unknown";

    private static String genomeReleasesFolderName = "genome_releases";
    private static String chainFilesFolderName = "chain_files";

    public FilePathGenerator(String rootDir) throws IOException {
    	setRootDirectory(rootDir);
    }

    /**
     * Used when first adding a new experiment. Creates a folder for the
     * experiment and subfolders for files
     *
     * @param String expID, the ID for the experiment
     */
    public void generateExperimentFolders(String expID) {

        File file = new File(getRawFolderPath(expID));
        file.mkdirs();

        file = new File(getProfileFolderPath(expID));
        file.mkdirs();

        file = new File(getRegionFolderPath(expID));
        file.mkdirs();

        file = new File(getUnknownFolderPath(expID));
        file.mkdirs();

		Runtime.getRuntime().exec("chmod 777 -R " + ServerSettings.fileLocation);
    }

    /**
     * Gets the path to the RAW folder of an experiment.
     *
     * @param String expID, the ID for the experiment
     * @return String path to RAW folder
     */
    public String getRawFolderPath(String expID) {

        StringBuilder rawFolderPathBuilder = new StringBuilder(rootDir);
        rawFolderPathBuilder.append(expID);
        rawFolderPathBuilder.append(File.separator);
        rawFolderPathBuilder.append(rawFolderName);
        rawFolderPathBuilder.append(File.separator);
        return rawFolderPathBuilder.toString();
    }

    /**
     * Gets the path to the profile folder of an experiment.
     *
     * @param String expID, the ID for the experiment
     * @return String path to profile folder
     */
    public String getProfileFolderPath(String expID) {

        StringBuilder profileFolderPathBuilder = new StringBuilder(rootDir);
        profileFolderPathBuilder.append(expID);
        profileFolderPathBuilder.append(File.separator);
        profileFolderPathBuilder.append(profileFolderName);
        profileFolderPathBuilder.append(File.separator);
        return profileFolderPathBuilder.toString();
    }

    /**
     * Gets the path to the region folder of an experiment.
     *
     * @param String expID, the ID for the experiment
     * @return String, path to region folder
     */
    public String getRegionFolderPath(String expID) {

        StringBuilder regionFolderPathBuilder = new StringBuilder(rootDir);
        regionFolderPathBuilder.append(expID);
        regionFolderPathBuilder.append(File.separator);
        regionFolderPathBuilder.append(regionFolderName);
        regionFolderPathBuilder.append(File.separator);
        return regionFolderPathBuilder.toString();
    }

    /**
     * Gets the path to the unknown folder (misc files) of an experiment.
     *
     * @param String expID, the ID for the experiment
     * @return String path to unknown folder
     */
    public String getUnknownFolderPath(String expID) {

        StringBuilder unknownFolderPathBuilder = new StringBuilder(rootDir);
        unknownFolderPathBuilder.append(expID);
        unknownFolderPathBuilder.append(File.separator);
        unknownFolderPathBuilder.append(unknownFolderName);
        unknownFolderPathBuilder.append(File.separator);
        return unknownFolderPathBuilder.toString();
    }

    /**
     * Used when uploading and downloading files. Returns a string where the
     * file is or where is should be saved.
     *
     * @param String expID, the ID for the experiment.
     * @param String fileType, the type of the file.
     * @param String fileName, the name of the file.
     * @return String the path for the file.
     */

    public String generateFilePath(String expID, int fileType, String fileName) {

        String folderPath;

        switch (fileType) {
        case FileTuple.RAW:
            folderPath = getRawFolderPath(expID);
            break;
        case FileTuple.PROFILE:
            folderPath = getProfileFolderPath(expID);
            folderPath = generateNewProfileSubFolder(folderPath);
            break;
        case FileTuple.REGION:
            folderPath = getRegionFolderPath(expID);
            break;
        default:
            folderPath = getUnknownFolderPath(expID);
            break;
        }

        return folderPath + fileName;
    }

    /**
     * Creates subfolders in profile folder. Adds a number at the end of the
     * subfolder name according to the number of folders already there.
     *
     * @param String folderPath
     * @return String path to the subfolder
     */
    public String generateNewProfileSubFolder(String folderPath) {

        File profileFolder = new File(folderPath);

        if (!profileFolder.exists()) {
            profileFolder.mkdirs();
        }

        Integer folderNumber = profileFolder.list().length;

        File newProfileFolder = new File(folderPath + folderNumber.toString() + File.separator);

        while (newProfileFolder.exists()) {
            folderNumber++;
            newProfileFolder = new File(folderPath + folderNumber.toString() + File.separator);
        }

        newProfileFolder.mkdirs();

        return newProfileFolder.getPath() + File.separator;
    }

    /**
     * Creates the folder for chainfiles and returns the path to it.
     *
     * @param String
     *            species
     * @param String
     *            fromVersion
     * @param String
     *            toVersion
     * @return String folderPath
     */
    public String generateChainFolder(String species, String fromVersion,
            String toVersion) {

        String chainFolderPath = getChainFolderPath(species, fromVersion,
                toVersion);

        File chainFolder = new File(chainFolderPath);

        if (!chainFolder.exists()) {
            chainFolder.mkdirs();
        }

        return chainFolderPath;
    }

    /**
     * Gets the path to the folder where the chain files should be stored.
     *
     * @param String species
     * @param String fromVersion
     * @param String toVersion
     * @return String The path to the folder where the chain files should be stored.
     */
    public String getChainFolderPath(String species, String fromVersion,
            String toVersion) {

        StringBuilder folderPath = new StringBuilder();

        folderPath.append(rootDir);
        folderPath.append(chainFilesFolderName);
        folderPath.append(File.separator);
        folderPath.append(species);
        folderPath.append(File.separator);
        folderPath.append(fromVersion);
        folderPath.append(" - ");
        folderPath.append(toVersion);
        folderPath.append(File.separator);

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

    /**
     * Gets the filepath to the genome release folder.
     *
     * @param String version
     * @param String species
     * @return String path to genome folder
     */
    public String getGenomeReleaseFolderPath(String version, String species) {

        StringBuilder folderPath = new StringBuilder();

        folderPath.append(rootDir);
        folderPath.append(genomeReleasesFolderName);
        folderPath.append(File.separator);
        folderPath.append(species);
        folderPath.append(File.separator);
        folderPath.append(version);
        folderPath.append(File.separator);
        return folderPath.toString();
    }


    /**
     * Sets the root directory where all data is stored.
     *
     * @param String rootDir
     * @throws IOException
     */
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
     * Returns the path to the root folder.
     *
     * @return String rootpath
     */
    public String getRootDirectory() {
        return rootDir;
    }
}
