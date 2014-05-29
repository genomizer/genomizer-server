package conversion.classes;

import java.io.File;


/**
 * Class that acts as a handler for all the conversions that will be done.
 * All conversions should go through this class.
 *
 */
public class ConversionHandler {

	public ConversionHandler createConversionHandler() {
		return new ConversionHandler();
	}

	/**
	 * Converts profile data from wig, sgr, gff3 and bed to all the others
	 *
	 *
	 * @param secondColumn -- IMPORTANT, null unless converting to bed
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String executeProfileDataConversion(String conversionName,
			String inFile, String outFile, String secondColumn) throws InterruptedException, IOException {

<<<<<<< HEAD
	public String executeProfileDataConversion(String conversionName, String inFile){
		switch(conversionName){
		case "wigToSgr":
			break;
		case "wigToBed":
			break;
		case "wigToGff3":
			break;
		case "bedToWig":
			break;
		case "bedToSgr":
			break;
		case "bedToGff3":
			break;
		case "sgrToWig":
			break;
		case "sgrToBed":
			break;
		case "sgrToGff3":
			break;
		case "gff3ToBed":
			break;
		case "gff3ToSgr":
			break;
		case "gff3ToWig":
			break;
		default: throw new IllegalArgumentException();
		}
		return null;
=======
		checkValidity(conversionName, inFile, outFile, secondColumn);


		ProfileDataConverter PDC = new ProfileDataConverter();

				switch (conversionName) {
				case "wigToSgr":
					PDC.wigToSgr(inFile, outFile);
					break;
				case "wigToBed":
					PDC.wigToBed(inFile, outFile, secondColumn);
					break;
				case "bedToSgr":
						PDC.bedToSgr(inFile, outFile);
					break;
				case "sgrToBed":
					PDC.sgrToBed(inFile, outFile, secondColumn);
					break;
				case "gff3ToBed":
					PDC.gff3ToBed(inFile, outFile, secondColumn);
					break;
				case "gff3ToSgr":
						PDC.gff3ToSgr(inFile, outFile);
					break;
				case "gff3ToWig":
					PDC.gff3ToWig(inFile, outFile);
				break;
				case "sgrToWig":
					PDC.sgrToWig(inFile, outFile);
				break;
				case "bedToWig":
					PDC.bedToWig(inFile, outFile);
				break;
				default:
					throw new IllegalArgumentException();
				}
		return "success";
>>>>>>> refs/remotes/origin/development
	}

	public String executeRegionDataConversion(String conversionName, String inFile){
		switch(conversionName){
		case "":
			break;

		default: throw new IllegalArgumentException();
		}
		return null;
	}

<<<<<<< HEAD
	public void executeGenomeReleaseConversion(String chainfilePath, String infilePath,String outfilePath,String unliftedPath){
		String fileType = checkFileType(infilePath);
		String path = infilePath;
		GenomeReleaseConverter genomeConverter = new GenomeReleaseConverter();
		ProfileDataConverter typeConverter = new ProfileDataConverter();

		// turn the inputfile to bed
		switch(fileType){
		case "wig":
			path = typeConverter.wigToBed(infilePath);
			break;
		case "sgr":
			path = typeConverter.sgrToBed(infilePath);
			break;
		case "gff3":
			path = typeConverter.gff3ToBed(infilePath);
		case "bed":
			break;
		default:
			throw new IllegalArgumentException();
		}


		String outfileBed = outfilePath.split("\\.")[0];
		outfileBed = outfileBed+".bed";

		genomeConverter.procedure(path, outfileBed, chainfilePath, unliftedPath);

		//convert the outputfile to the inputfiles original filetype
		switch(fileType){
		case "wig":
			typeConverter.bedToWig(outfileBed);
			File file = new File(outfileBed);
			file.delete();
			break;
		case "sgr":
			typeConverter.bedToSgr(outfileBed);
			File file = new File(outfileBed);
			file.delete();
			break;
		case "bed":
			break;
		default:
			throw new IllegalArgumentException();
		}

		}
=======
>>>>>>> refs/remotes/origin/development
	public String checkFileType(String filePath){
		String type = "";
		if((filePath.length()-filePath.replaceAll("\\.","").length())>=1){
			String[] splitArray = filePath.split("\\.");
			int lengthOfArray = splitArray.length;
			type = splitArray[lengthOfArray-1];
			System.err.println(type);
		}



		return type;
<<<<<<< HEAD
=======
	}


// TODO
//	public String executeRegionDataConversion(String conversionName,
//			String inFile) {
//
//		//convert the outputfile to the inputfiles original filetype
//		switch(fileType){
//		case "wig":
//			typeConverter.bedToWig(outfileBed);
//			File file = new File(outfileBed);
//			file.delete();
//			break;
//		case "sgr":
//			typeConverter.bedToSgr(outfileBed);
//			File file = new File(outfileBed);
//			file.delete();
//			break;
//		case "bed":
//
//			break;
//
//		default:
//			throw new IllegalArgumentException();
//		}
//
//		return null;
//
//		}
>>>>>>> refs/remotes/origin/development

<<<<<<< HEAD


	}
}
=======
	// public void executeGenomeReleaseConversion(String chainfilePath, String
	// infilePath,String outfilePath){
	// String fileType = checkFileType(infilePath);
	// String path = infilePath;
	// GenomeReleaseConverter genomeConverter = new GenomeReleaseConverter();
	// ProfileDataConverter typeConverter = new ProfileDataConverter();
	//
	// // turn the inputfile to bed
	// switch(fileType){
	// case "wig":
	// path = typeConverter.wigToBed(infilePath);
	// break;
	// case "sgr":
	// path = typeConverter.sgrToBed(infilePath);
	// break;
	// case "gff3":
	// path = typeConverter.gff3ToBed(infilePath);
	// case "bed":
	// break;
	// default:
	// throw new IllegalArgumentException();
	// }
	//
	//
	// String outfileBed = outfilePath.split("\\.")[0];
	// outfileBed = outfileBed+".bed";
	//
	// genomeConverter.procedure(path, outfileBed, chainfilePath);
	//
	// //convert the outputfile to the inputfiles original filetype
	// switch(fileType){
	// case "wig":
	// typeConverter.bedToWig(outfileBed);
	// break;
	// case "sgr":// public void executeGenomeReleaseConversion(String
	// chainfilePath, String infilePath,String outfilePath){
	// String fileType = checkFileType(infilePath);
	// String path = infilePath;
	// GenomeReleaseConverter genomeConverter = new GenomeReleaseConverter();
	// ProfileDataConverter typeConverter = new ProfileDataConverter();
	//
	// // turn the inputfile to bed
	// switch(fileType){
	// case "wig":
	// path = typeConverter.wigToBed(infilePath);
	// break;
	// case "sgr":
	// path = typeConverter.sgrToBed(infilePath);
	// break;
	// case "gff3":
	// path = typeConverter.gff3ToBed(infilePath);
	// case "bed":
	// break;
	// default:
	// throw new IllegalArgumentException();
	// }
	//
	//
	// String outfileBed = outfilePath.split("\\.")[0];
	// outfileBed = outfileBed+".bed";
	//
	// genomeConverter.procedure(path, outfileBed, chainfilePath);
	//
	// //convert the outputfile to the inputfiles original filetype
	// switch(fileType){
	// case "wig":
	// typeConverter.bedToWig(outfileBed);
	// break;
	// case "sgr":
	// typeConverter.bedToSgr(outfileBed);
	// break;
	// case "gff3":
	// typeConverter.bedToGff3(outfileBed);
	// case "bed":
	// break;
	// default:
	// throw new IllegalArgumentException();
	// }
	//
	// //delete the bedfile
	// File file = new File(outfileBed);
	// file.delete();
	//
	// }
	// public String checkFileType(String filePath){
	// String type = "";
	// if((filePath.length()-filePath.replaceAll("\\.","").length())==1){
	// type = filePath.split("\\.", 10)[1];
	// System.err.println(type);
	// }
	//
	//
	//
	// return type;
	//
	//
	//
	// }
	// typeConverter.bedToSgr(outfileBed);
	// break;
	// case "gff3":
	// typeConverter.bedToGff3(outfileBed);
	// case "bed":
	// break;
	// default:
	// throw new IllegalArgumentException();
	// }
	//
	// //delete the bedfile
	// File file = new File(outfileBed);
	// file.delete();
	//
	// }
	// public String checkFileType(String filePath){
	// String type = "";
	// if((filePath.length()-filePath.replaceAll("\\.","").length())==1){
	// type = filePath.split("\\.", 10)[1];
	// System.err.println(type);
	// }
	//
	//
	//
	// return type;
	//
	//
	//
	// }
}
>>>>>>> refs/remotes/origin/development
