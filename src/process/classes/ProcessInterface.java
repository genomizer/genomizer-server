package process.classes;

public interface ProcessInterface {

	public String runExecutable(String process, String[] param,
									String inFilePath, String outFilePath);

	public String runConversion(String conversion, String inFilePath,
									String outFilePath);







}
