package response.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import command.Command;
import command.GetGenomeReleaseSpeciesCommand;

import database.DatabaseAccessor;
import database.Genome;

import response.GetGenomeReleaseRespons;
import response.Response;
import response.StatusCode;
import server.ServerSettings;

public class GetGenomReleaseResponsTest {

	@Test
	public void testIfResponseCodeIsCorrectForExisting() {



		ArrayList<Genome> genomeList;
		try {

			DatabaseAccessor db=new DatabaseAccessor(ServerSettings.databaseUsername, ServerSettings.databasePassword, ServerSettings.databaseHost, ServerSettings.databaseName);
			//genomeList = db.getAllGenomReleases();
			Command cmd=new GetGenomeReleaseSpeciesCommand("mouseTEST");
			Response rsp=cmd.execute();
			assertEquals(StatusCode.OK, rsp.getCode());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testIfResponseCodeIsCorrectNotExisting() {



		ArrayList<Genome> genomeList;
		try {

			DatabaseAccessor db=new DatabaseAccessor(ServerSettings.databaseUsername, ServerSettings.databasePassword, ServerSettings.databaseHost, ServerSettings.databaseName);
			//genomeList = db.getAllGenomReleases();
			Command cmd=new GetGenomeReleaseSpeciesCommand("hej");
			Response rsp=cmd.execute();
			assertEquals(StatusCode.BAD_REQUEST, rsp.getCode());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testPrintResponse() {


		ArrayList<Genome> genomeList;
		try {
			DatabaseAccessor db=new DatabaseAccessor(ServerSettings.databaseUsername, ServerSettings.databasePassword, ServerSettings.databaseHost, ServerSettings.databaseName);
			genomeList = db.getAllGenomReleases();

		/*	for(int i=0; i<db.getAllAnnotationLabels().size();i++){
				System.out.println(db.getAllAnnotationLabels().get(i));
			}
			*/
			/*for(int i=0; i<db.getChoices("SpeciesTEST").size();i++){
				System.out.println(db.getChoices("SpeciesTEST"));

			}*/

			//genomeList =db.getAllGenomReleasesForSpecies("Human");
			GetGenomeReleaseRespons gResp=new GetGenomeReleaseRespons(StatusCode.OK,genomeList);
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
