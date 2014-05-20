package command.test;

import static org.junit.Assert.*;



import org.junit.Test;

import response.Response;

import command.CommandHandler;
import command.GetAnnotationInformationCommand;

public class GetAnnotationInformationCommandTest {

	@Test
	public void testDatabase() {
		GetAnnotationInformationCommand cmd = new GetAnnotationInformationCommand();
		Response rsp = cmd.execute();


	}

}
