package response.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.subClasses.UserMethods;
import database.test.TestInitializer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import command.Command;
import command.GetGenomeReleaseSpeciesCommand;

import database.DatabaseAccessor;
//import database.Genome;

import response.GetGenomeReleaseResponse;
import response.HttpStatusCode;
import response.Response;
import server.ServerSettings;

@Ignore
public class GetGenomeReleaseResponseTest {

	@Before
	public void setup() {
		TestInitializer.setupServerSettings();
	}

	@Test
	public void testIfResponseCodeIsCorrectForExisting() {



		//ArrayList<Genome> genomeList;
		try {

			new DatabaseAccessor(ServerSettings.databaseUsername, ServerSettings.databasePassword, ServerSettings.databaseHost, ServerSettings.databaseName);
			//genomeList = db.getAllGenomReleases();
			Command cmd=new GetGenomeReleaseSpeciesCommand();
			Response rsp=cmd.execute();
			assertEquals(HttpStatusCode.OK, rsp.getCode());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// TODO: This returns 200 and an empty list instead of an error code. Not sure if correct or not.
	@Ignore
	@Test
	public void testIfResponseCodeIsCorrectNotExisting() {



		//ArrayList<Genome> genomeList;
		try {

			new DatabaseAccessor(ServerSettings.databaseUsername, ServerSettings.databasePassword, ServerSettings.databaseHost, ServerSettings.databaseName);
			//genomeList = db.getAllGenomReleases();
			Command cmd=new GetGenomeReleaseSpeciesCommand();
			Response rsp=cmd.execute();
			assertEquals(HttpStatusCode.BAD_REQUEST, rsp.getCode());

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testPrintResponse() {


		ArrayList<database.containers.Genome> genomeList;
		try {
			DatabaseAccessor db=new DatabaseAccessor(ServerSettings.databaseUsername, ServerSettings.databasePassword, ServerSettings.databaseHost, ServerSettings.databaseName);
			genomeList = (ArrayList<database.containers.Genome>) db.getAllGenomeReleases();

		/*	for(int i=0; i<db.getAllAnnotationLabels().size();i++){
				System.out.println(db.getAllAnnotationLabels().get(i));
			}
			*/
			/*for(int i=0; i<db.getChoices("SpeciesTEST").size();i++){
				System.out.println(db.getChoices("SpeciesTEST"));

			}*/

			//genomeList =db.getAllGenomeReleasesForSpecies("Human");
			GetGenomeReleaseResponse gResp=new GetGenomeReleaseResponse(HttpStatusCode.OK,genomeList);
			System.out.println(gResp.getBody());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
