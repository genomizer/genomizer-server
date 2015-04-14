package server.test.dummies;

import authentication.PasswordHash;

public class JonasM_serverTests {

	public static void main(String args[]) throws Exception {

		String username;
		for(int i = 0; i < 10; i++) {
			username =  "Jonas" + i;
			Login.login(username, "umea@2014");
			//Login.token = null;
		}
	}

	public static void renameannotest() throws Exception {
		Annotations.sendGetAnnotationInformation();
		Annotations.sendDeleteAnnotation("testanno space2");
		Annotations.sendAddAnnotation("testanno space");
		Annotations.sendRenameAnnotationField("testanno space", "testanno space2");
		Annotations.sendAddAnnotationValue("testanno space2", "val space");
		Annotations.sendAddAnnotationValue("testanno space2", "val space");
		Annotations.sendRenameAnnotationValue("testanno space2", "val space", "newval space");
		Annotations.sendDeleteAnnotationValue("testanno space2", "newval space");
		Annotations.sendDeleteAnnotation("testanno space2");
		Annotations.sendGetAnnotationInformation();
	}

	public static void usertests() throws Exception {
		User.sendDeleteUser("c11jmm");
		User.sendCreateUser("c11jmm", "bajs", "minion", "Jonas Erik Markstr�m", "c11jmm@cs.umu.se");
		User.sendCreateUser("c11jmm", "bajs", "minion", "Jonas Erik Markstr�m", "c11jmm@cs.umu.se"); // Trying to create duplicate user, should give error.
		Administrator.sendUpdateUserPrivileges("c11jmm", "basic");
		Administrator.sendUpdateUserPrivileges("c11jmm2", "mastah"); // User does not exist, should return error code.
		User.sendDeleteUser("c11jmm");

	}

	public static void specialannotest() throws Exception {
		Annotations.sendAddAnnotation("@@@@@234@2$????");
		//Annotations.sendGetAnnotationInformation();
		//Annotations.sendDeleteAnnotation("@/@@@@@2$????");
		//Annotations.sendGetAnnotationInformation();
	}

	public static void exptest() throws Exception {
		//Experiment.sendDeleteExperiment("testExp22");
		Experiment.sendAddExperiment("testExp2233");
		//Experiment.sendGetExperiment("testExp22");
		//Experiment.sendGetExperiment("testExp22gfert3453");
		//Experiment.sendDeleteExperiment("testExp22");
	}

	public static void searchtest(String query) throws Exception {
		Search.sendSearchRequest(query);
	}

	public static void genometest() throws Exception {
		GenomeRelease.sendGetGenomeRelease();
		//GenomeRelease.sendAddGenomeRelease("Fly");
		GenomeRelease.sendGetGenomeReleaseSpecies("Fly");
	}

	public static void processtest() throws Exception {
		Process.sendRawToProfile();
		Process.sendGetProcessStatus();
	}
}
