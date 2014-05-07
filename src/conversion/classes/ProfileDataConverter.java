package conversion.classes;

import process.classes.Executor;


public class ProfileDataConverter {

	public String bedToWig (String infilePath){
		return bedToWigConversion(infilePath);
	}
	public String bedToSgr (String infilePath){
		return bedToSgrConversion(infilePath);
	}
	public String bedToGff3 (String infilePath){
		return bedToGff3Conversion(infilePath);
	}
	public String wigToBed (String infilePath){
		return wigToBedConversion(infilePath);
	}
	public String sgrToBed (String infilePath){
		return sgrToBedConversion(infilePath);
	}
	public String gff3ToBed (String infilePath){
		return gff3ToBedConversion(infilePath);
	}
	public String wigToSgr (String infilePath){
		return bedToSgr(wigToBed(infilePath));
	}
	public String wigToGff3 (String infilePath){
		return bedToGff3(wigToBed(infilePath));
	}
	public String sgrToWig (String infilePath){
		return bedToWig(sgrToBed(infilePath));
	}
	public String sgrToGff3 (String infilePath){
		return bedToGff3(sgrToBed(infilePath));
	}
	public String gff3ToSgr (String infilePath){
		return bedToSgr(gff3ToBed(infilePath));
	}
	public String gff3ToWig (String infilePath){
		return bedToWig(gff3ToBed(infilePath));
	}

	// Actual conversion
	private String bedToWigConversion (String infilePath){
		return null;
	}
	private String bedToSgrConversion (String infilePath){
		return null;
	}
	private String bedToGff3Conversion (String infilePath){
		return null;
	}
	private String wigToBedConversion (String infilePath){
		return null;
	}
	private String sgrToBedConversion (String infilePath){
		return null;
	}
	private String gff3ToBedConversion (String infilePath){
		return null;
	}


}
