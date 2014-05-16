package server.test.dummies;


public class serverTests {

	public static void main(String args[]) throws Exception {
		Login.login();

		Annotations.sendDeleteAnnotation("testanno space2");
		Annotations.sendDeleteAnnotation("testanno space");
		Annotations.sendAddAnnotation("testanno space");
		Annotations.sendRenameAnnotationField("testanno space", "testanno space2");
		Annotations.sendAddAnnotationValue("testanno space2", "val space");
		Annotations.sendAddAnnotationValue("testanno space2", "val space");
//		Annotations.sendRenameAnnotationValue("testanno space2", "val space", "newval space");
//		Annotations.sendDeleteAnnotationValue("testanno space2", "newval space");
//		Annotations.sendDeleteAnnotation("testanno space2");


//		Experiment.sendDeleteExperiment("testExp22");
//		Experiment.sendAddExperiment("testExp22");
		Experiment.sendGetExperiment("testExp22");
//		Experiment.sendDeleteExperiment("testExp22");
		Login.logout();

	}
}
