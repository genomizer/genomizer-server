package command;

import com.google.gson.annotations.Expose;

public class ConvertRawToProfileCommand extends Command {


	@Expose
	private String token;


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
		return null;
	}

}
