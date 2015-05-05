package conversion;

import java.io.IOException;

import process.Executor;
/**A class that runs liftover to convert old profile data to a newer genome release
 * 
 * 
 * @author c11wth
 *
 */
public class GenomeReleaseConverter extends Executor {
	
	public String procedure(String inFilePath, String outFilePath,
							String chainFilePath,String unliftedPath )
	throws InterruptedException, IOException{
		String outString = "";
		String [] liftover = new String[5];
		liftover[0] = "liftOver";
		liftover[1] = inFilePath;
		liftover[2] = chainFilePath;
		liftover[3] = outFilePath;
		liftover[4] = unliftedPath;

		/* Will throw exceptions if erroneous */
		outString = executeProgram(liftover);
		System.err.println(outString);
		
		return outString;
		
		
	}

	
	
}
