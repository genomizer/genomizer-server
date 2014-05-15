package server.test.dummies;


public class serverTests {

	public static void main(String args[]) throws Exception {
		Login.login();
/*
		Annotations.sendDeleteAnnotation("test3");
		Annotations.sendAddAnnotation("test3");
		Annotations.sendRenameAnnotationField("test3", "test4");
		Annotations.sendAddAnnotationValue("test4", "val2");
		Annotations.sendRenameAnnotationValue("test4", "val2", "val3");
		Annotations.sendDeleteAnnotationValue("test4", "val3");
		Annotations.sendDeleteAnnotation("test4");
		Experiment.sendAddExperiment("testExp22");
		Experiment.sendDeleteExperiment("testExp22");
*/
		Process.process();


		Login.logout();

	}
}
