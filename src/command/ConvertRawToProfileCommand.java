package command;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

import com.google.gson.annotations.Expose;

public class ConvertRawToProfileCommand extends Command {


	private String fileID;


	public ConvertRawToProfileCommand() {

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Response execute() {
		// TODO Auto-generated method stub
		//Har ett filID, parsa i commandFactory
		//FileExists till databas
		//skicka filen till processmetoden, vilka param,returvärde?
		//return respons 201

		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}

}
