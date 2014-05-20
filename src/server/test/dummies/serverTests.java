package server.test.dummies;

public class serverTests {

	public static void main(String args[]) throws Exception {
		Login.login("Splutt");

		searchtest("");


		Login.logout();
	}

	public void renameannotest() throws Exception {
		Annotations.sendGetAnnotationInformation();
		Annotations.sendDeleteAnnotation("testanno space2");
		Annotations.sendAddAnnotation("testanno space");
		Annotations.sendRenameAnnotationField("testanno space", "testanno space2");
		Annotations.sendAddAnnotationValue("testanno space2", "val space");
		Annotations.sendAddAnnotationValue("testanno space2", "val space");
		Annotations.sendRenameAnnotationValue("testanno space2", "val space", "newval space");
		Annotations.sendDeleteAnnotationValue("testanno space2", "newval space");
		Annotations.sendGetAnnotationInformation();
		Annotations.sendDeleteAnnotation("testanno space2");
	}

	public static void specialannotest() throws Exception {
		Annotations.sendAddAnnotation("@/@@@@@2$????");
		Annotations.sendGetAnnotationInformation();
		Annotations.sendDeleteAnnotation("@/@@@@@2$????");
		Annotations.sendGetAnnotationInformation();
	}

	public void exptest() throws Exception {
		Experiment.sendDeleteExperiment("testExp22");
		Experiment.sendAddExperiment("testExp22");
		Experiment.sendGetExperiment("testExp22");
		Experiment.sendGetExperiment("testExp22gfert3453");
		Experiment.sendDeleteExperiment("testExp22");
	}

	public static void searchtest(String query) throws Exception {
		Search.sendSearchRequest(query);
	}

	public void genometest() throws Exception {
		GenomeRelease.sendGetGenomeRelease();
		GenomeRelease.sendGetGenomeReleaseSpecies();
	}

	public void processtest() throws Exception {
		Process.sendRawToProfile();
		Process.sendGetProcessStatus();
	}
}
