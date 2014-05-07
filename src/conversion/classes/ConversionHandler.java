package conversion.classes;

import java.io.File;


public class ConversionHandler {
	public ConversionHandler createConversionHandler() {
		return new ConversionHandler();
	}


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
	}

	public String executeRegionDataConversion(String conversionName, String inFile){
		switch(conversionName){
		case "":
			break;

		default: throw new IllegalArgumentException();
		}
		return null;
	}

	public void executeGenomeReleaseConversion(String chainfilePath, String infilePath,String outfilePath){
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

		genomeConverter.procedure(path, outfileBed, chainfilePath);

		//convert the outputfile to the inputfiles original filetype
		switch(fileType){
		case "wig":
			typeConverter.bedToWig(outfileBed);
			break;
		case "sgr":
			typeConverter.bedToSgr(outfileBed);
			break;
		case "gff3":
			typeConverter.bedToGff3(outfileBed);
		case "bed":
			break;
		default:
			throw new IllegalArgumentException();
		}

		//delete the bedfile
		File file = new File(outfileBed);
		file.delete();

		}
	public String checkFileType(String filePath){
		String type = "";
		if((filePath.length()-filePath.replaceAll("\\.","").length())==1){
			type = filePath.split("\\.", 10)[1];
			System.err.println(type);
		}



		return type;



	}
}
