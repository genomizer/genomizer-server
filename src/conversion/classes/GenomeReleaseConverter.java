package conversion.classes;

import java.io.IOException;

import process.classes.Executor;
/**A class that runs liftover to convert old profile data to a newer genome release
 * 
 * 
 * @author c11wth
 *
 */
public class GenomeReleaseConverter extends Executor {
	
	public String procedure(String inFilePath, String outFilePath, String chainFilePath){
		String outString;
		String [] liftover = new String[4];
		liftover[0] = "liftOver";
		liftover[1] = inFilePath;
		liftover[2] = chainFilePath;
		liftover[3] = outFilePath;
		
		try {
			outString = executeProgram(liftover);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outString;
		
		
	}

	
	
}
