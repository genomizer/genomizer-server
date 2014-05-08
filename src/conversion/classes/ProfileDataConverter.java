package conversion.classes;

import process.classes.Executor;


public class ProfileDataConverter {

	// Single steps
	public String wigToSgr (String infilePath){
		return wigToSgrConversion(infilePath);
	}
	public String sgrToWig (String infilePath){
		return sgrToWigConversion(infilePath);
	}
	public String gff3ToSgr (String infilePath){
		return gff3ToSgrConversion(infilePath);
	}
	public String sgrToBed (String infilePath){
		return sgrToBedConversion(infilePath);
	}
	public String bedToSgr (String infilePath){
		return bedToSgrConversion(infilePath);
	}

	//----- Doubble steps
	public String bedToWig (String infilePath){
		return sgrToWigConversion(bedToSgrConversion(infilePath));
	}
	public String wigToBed (String infilePath){
		return sgrToBedConversion(wigToSgrConversion(infilePath));
	}
	public String gff3ToBed (String infilePath){
		return sgrToBedConversion(gff3ToSgrConversion(infilePath));
	}
	public String gff3ToWig (String infilePath){
		return sgrToWigConversion(gff3ToSgrConversion(infilePath));
	}

	// ------ NOt possible yet
//	public String sgrToGff3 (String infilePath){
//	return (	(infilePath));
//}
//	public String wigToGff3 (String infilePath){
//		return (	(infilePath));
//	}
//	public String bedToGff3 (String infilePath){
//		return (infilePath);
//	}

	// Actual conversion
	private String sgrToBedConversion (String infilePath){
		return null;
	}
	private String sgrToWigConversion (String infilePath){
		return null;
	}
	private String bedToSgrConversion (String infilePath){
		return null;
	}
	private String wigToSgrConversion(String infilePath){
		return null;
	}
	private String gff3ToSgrConversion (String infilePath){
		return null;
	}

}
