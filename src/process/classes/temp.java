package process.classes;

import java.io.IOException;

public class temp extends Executor{
	
	public temp(){
	}

	public void sds(){
		String [] FILEPATH = {"resources/"};
		try {
			executeScript(FILEPATH);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
