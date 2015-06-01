package conversion;

import database.DatabaseAccessor;
import database.containers.FileTuple;
import server.ServerSettings;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Class that acts as a handler for all the conversions that will be done.
 * All conversions should go through this class.
 *
 * Created 2015-05-04.
 * @author Albin Råstander <c12arr@cs.umu.se>
 * @author Martin Larsson <dv13mln@cs.umu.se>
 *
 */
public class ConversionHandler {
	private DatabaseAccessor db;
	private ProfileDataConverter pdc;
	private String fileInDB;

	/**
	 * Constructor
	 */
	public ConversionHandler() {
		pdc = new ProfileDataConverter();
	}

	/**
	 * Initiate database
	 * @return DatabaseAccessor
	 * @throws SQLException
	 * @throws IOException
	 */
	private DatabaseAccessor initDB() throws SQLException, IOException {
		return new DatabaseAccessor(ServerSettings.databaseUsername,
				ServerSettings.databasePassword, ServerSettings.databaseHost,
				ServerSettings.databaseName);
	}

	/**
	 * Convert between profile data formats
	 * @param newFormat Format to convert to
	 * @param id Database id of file to convert from
	 * @throws SQLException
	 * @throws IOException
	 */
	public FileTuple convertProfileData(String newFormat, int id) throws SQLException, IOException {
		db = initDB();
		FileTuple file = db.getFileTuple(id);
		fileInDB = file.path;
		String currentFormat = getFileType(file.path);
		String outputFilePath;

		switch (currentFormat) {
			case "bed":
				outputFilePath = convertFromBedTo(newFormat);
				break;
			case "gff":
				outputFilePath = convertFromGffTo(newFormat);
				break;
			case "sgr":
				outputFilePath = convertFromSgrTo(newFormat);
				break;
			case "wig":
				outputFilePath = convertFromWigTo(newFormat);
				break;
			default:
				throw new IllegalArgumentException("Invalid File Format!");
		}

		String inputFileName = file.path.substring(file.path.lastIndexOf('/')+1);
		String fileName = outputFilePath.substring(outputFilePath.lastIndexOf('/')+1);

		File outFile = new File(outputFilePath);
		FileTuple ft = db.addNewFile(file.expId, FileTuple.PROFILE, fileName,
				inputFileName, null, file.author,
				"ConversionHandler", file.isPrivate, file.grVersion, md5Hex(new FileInputStream(new File(outputFilePath))));

		db.updateFileSize(ft, outFile.length());
		
		db.close();

		Files.move(outFile.toPath(), new File(ft.path).toPath());

		return ft;
	}

	/**
	 * Get file extension
	 * @param filepath
	 * @return
	 */
	private String getFileType(String filepath) {
		return filepath.substring(filepath.lastIndexOf('.')+1);
	}

	/**
	 * All conversions from bed type
	 * @param format
	 * @throws IOException
	 */
	private String convertFromBedTo(String format) throws IOException {
		switch (format) {
			case "bed":
				throw new IllegalArgumentException("Cannot convert from bed to bed.");
			case "gff":
				throw new IllegalArgumentException("Conversion from bed to gff not implemented.");
			case "sgr":
				 return pdc.bedToSgr(fileInDB);
			case "wig":
				return pdc.bedToWig(fileInDB);
			default:
				throw new IllegalArgumentException("Unkown conversion.");
		}
	}

	/**
	 * All conversions from gff type
	 * @param format
	 * @throws IOException
	 */
	private String convertFromGffTo(String format) throws IOException {
		switch (format) {
			case "bed":
				throw new IllegalArgumentException("Conversion from gff to bed mot implemented.");
			case "gff":
				throw new IllegalArgumentException("Cannot convert from gff to gff");
			case "sgr":
				return pdc.gffToSgr(fileInDB);
			case "wig":
				return pdc.gffToWig(fileInDB);
			default:
				throw new IllegalArgumentException("Unknown conversion.");
		}
	}

	/**
	 * All conversion from sgr type
	 * @param format
	 * @throws IOException
	 */
	private String convertFromSgrTo(String format) throws IOException {
		switch (format) {
			case "bed":
				throw new IllegalArgumentException("Cannot convert from sgr to bed.");
			case "gff":
				throw new IllegalArgumentException("Cannot convert from sgr to gff.");
			case "sgr":
				throw new IllegalArgumentException("Cannot convert from sgr to sgr.");
			case "wig":
				return pdc.sgrToWig(fileInDB);
			default:
				throw new IllegalArgumentException("Unknown conversion.");
		}
	}

	/**
	 * All conversions from wig type
	 * @param format
	 * @throws IOException
	 */
	private String convertFromWigTo(String format) throws IOException {
		String wigType = checkWigType(fileInDB);
		switch (format) {
			case "bed":
				throw new IllegalArgumentException("Cannot convert from wig to bed.");
			case "gff":
				throw new IllegalArgumentException("Cannot convert from wig to gff.");
			case "sgr":
				return convertFromWigToSgr(fileInDB, wigType);
			case "wig":
				throw new IllegalArgumentException("Cannot convert from wig to wig.");
			default:
				throw new IllegalArgumentException("Unknown conversion.");
		}
	}

	/**
	 * Return type of wig file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private String checkWigType(String path) throws IOException {
		BufferedReader fr = new BufferedReader(new FileReader(path));

		fr.readLine();
		String line = fr.readLine();
		String[] columns = line.split("\\s+");

		if (columns[0].equals("variableStep"))
			return "variableStep";
		else if (columns[0].equals("fixedStep"))
			return "fixedStep";
		else
			return "bed";
	}

	/**
	 * All conversions from different wig types to sgr
	 * @param fileInDB
	 * @param wigType
	 * @throws IOException
	 */
	private String convertFromWigToSgr(String fileInDB, String wigType) throws IOException {
		switch (wigType) {
			case "variableStep":
				return pdc.wigToSgr("variableStep", fileInDB);
			case "fixedStep":
				return pdc.wigToSgr("fixedStep", fileInDB);
			case "bed":
				return pdc.wigToSgr("bed", fileInDB);
			default:
				throw new IllegalArgumentException("Unknown conversion.");
		}
	}
}
