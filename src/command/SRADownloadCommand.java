package command;

import process.ProcessException;
import process.SRADownloader;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.ServerSettings;

import java.io.IOException;

/**
 * Created by oi12fun & tfy12jsg on 2015-04-29.
 */

public class SRADownloadCommand extends Command {

	private String runID;

	public SRADownloadCommand(String runID) {
		this.runID = runID;
	}

	@Override
	public void validate() throws ValidateException {
		if (!(runID.startsWith("SRR") || runID.startsWith("DRR") || runID.startsWith("ERR")))
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid run file prefix.");

		if (!runID.substring(3).matches("^[0-9]{1,}$"))
			throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Invalid run file.");


	}

	@Override
	public Response execute() {

		SRADownloader sh = new SRADownloader();
		try {
			sh.downloadFromSRA(runID);
		} catch (ProcessException e) {
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return new MinimalResponse(HttpStatusCode.OK);
	}

	  public static void main(String[] args) {

	        try {
	            ServerSettings.readSettingsFile(System.getProperty("user.dir")+"/settings.cfg");
	            SRADownloadCommand sdc = new SRADownloadCommand("SRR1970533");
	            sdc.validate();
	            sdc.execute();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ValidateException e) {
				e.printStackTrace();
			}

	    }

}
