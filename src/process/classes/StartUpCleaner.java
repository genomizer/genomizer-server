package process.classes;

public class StartUpCleaner {
	
	private StartUpCleaner(){
		
	}
	
	
	public StartUpCleaner startUpCleanerFactory() {
		return new StartUpCleaner();
	}
	
}
