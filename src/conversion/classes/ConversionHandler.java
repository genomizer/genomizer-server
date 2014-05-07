package conversion.classes;


public class ConversionHandler {
	public ConversionHandler createProcessHandler() {
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

	public void executeGenomeReleaseConversion(String chainFilePath, String inFilePath,String outFilePath){
		String fileType = checkFileType(inFilePath);
		String path = inFilePath;
		GenomeReleaseConverter converter = new GenomeReleaseConverter();
		
		
		
		//TODO convert to bed type
		switch(fileType){
		case "wig":
			
			break;
		case "sgr":
			
			break;
		case "bed":
			break;
		default:
			throw new IllegalArgumentException();
		}
		converter.procedure(path, outFilePath, chainFilePath);
		
		
		//TODO Convert back to original filetype
		
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
