package server.test.dummies;

import java.io.IOException;


public class serverTests {

	public static void main(String args[]) throws Exception {


		Login.login("Splutt");
//		Annotations.sendGetAnnotationInformation();
//		Annotations.sendDeleteAnnotation("testanno space2");
//		Annotations.sendAddAnnotation("testanno space");
//		Annotations.sendRenameAnnotationField("testanno space", "testanno space2");
//		Annotations.sendAddAnnotationValue("testanno space2", "val space");
//	Annotations.sendAddAnnotationValue("testanno space2", "val space");
//		Annotations.sendRenameAnnotationValue("testanno space2", "val space", "newval space");
//	Annotations.sendDeleteAnnotationValue("testanno space2", "newval space");
//		Annotations.sendGetAnnotationInformation();
//		Annotations.sendDeleteAnnotation("testanno space2");

//		Experiment.sendDeleteExperiment("testExp22");
//		Experiment.sendAddExperiment("testExp22");
//		Experiment.sendGetExperiment("testExp22");
//		Experiment.sendGetExperiment("testExp22gfert3453");
//		Experiment.sendDeleteExperiment("testExp22");

//		GenomeRelease.sendGetGenomeRelease();
//		GenomeRelease.sendGetGenomeReleaseSpecies();

//		Process.sendRawToProfile();
		Process.sendGetProcessStatus();

		Login.logout();

	}
}
