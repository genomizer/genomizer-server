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

	public String executeGenomeReleaseConversion(String conversionName, String inFile){
		switch(conversionName){
		case "":
			break;

		default: throw new IllegalArgumentException();
		}
		return null;
	}
}
