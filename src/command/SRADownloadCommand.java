package command;

import java.io.IOException;

import process.SRADownloader;
import response.Response;
import response.StatusCode;
import server.ServerSettings;

public class SRADownloadCommand extends Command {

	private String runID;
	private String studyID;

	public SRADownloadCommand(String runID, String studyID) {
		this.runID = runID;
		this.studyID = studyID;
	}

	@Override
	public void validate() throws ValidateException {
		if (!(runID.startsWith("SRR") || runID.startsWith("DRR") || runID.startsWith("ERR")))
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid run file prefix.");

		if (!(studyID.startsWith("SRP") || studyID.startsWith("DRP") || studyID.startsWith("ERP")))
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid study file prefix.");

		try {
			Integer.parseInt(runID.substring(3));
		} catch (NumberFormatException e) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid run file.");
		}

		try {
			Integer.parseInt(studyID.substring(3));
		} catch (NumberFormatException e) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid study file.");
		}


	}

	@Override
	public Response execute() {
		SRADownloader sh = new SRADownloader();
        try {
			sh.download(runID);
			sh.getMetaData(runID,studyID);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

	  public static void main(String[] args) {

	        try {
	            ServerSettings.readSettingsFile(System.getProperty("user.dir")+"/settings.cfg");
	            SRADownloadCommand sdc = new SRADownloadCommand("SRR1970533","SRP056905");
	            sdc.validate();
	            sdc.execute();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ValidateException e) {
				e.printStackTrace();
			}

	    }

}
