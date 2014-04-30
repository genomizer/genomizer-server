package command;

import response.Response;

import com.google.gson.annotations.Expose;

public class ProcessCommand extends Command {


	private String fileID;
	private String processType;
	private String outFilePath;
	private String inFilePath;

	public ProcessCommand() {

	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Response execute() {
		// TODO Auto-generated method stub
		//Har ett filID,,processtype,param till profile->region parsa i commandFactory
		//FileExists till databas
		//Kontrollera vart outfilepath,infilepath
		//skicka filen till processmetoden (runexecutable),returvärde massa text ifrån script?
		//return respons 201

		return null;
	}

}
