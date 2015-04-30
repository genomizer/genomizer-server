package command;

import process.SRADownloader;
import response.Response;

public class SRADownloadCommand extends Command {

	private String runID;
	private String studyID;

	private SRADownloader sraDownloader;

	public SRADownloadCommand(String runID, String studyID) {
		this.runID = runID;
		this.studyID = studyID;

		sraDownloader = new SRADownloader();
	}

	@Override
	public void validate() throws ValidateException {
		validateName(runID, 10, "Invalid runID");
		validateName(studyID, 10, "Invalid studyID");


	}

	@Override
	public Response execute() {
		// TODO Auto-generated method stub
		return null;
	}



}
