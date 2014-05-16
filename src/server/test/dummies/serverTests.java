package server.test.dummies;

import java.io.IOException;


public class serverTests {

	public static void main(String args[]) throws Exception {

		Login.login("Splutt");

//		Annotations.sendDeleteAnnotation("testanno space2");
//		Annotations.sendDeleteAnnotation("testanno space");
//		Annotations.sendAddAnnotation("testanno space");
//		Annotations.sendRenameAnnotationField("testanno space", "testanno space2");
//		Annotations.sendAddAnnotationValue("testanno space2", "val space");
//		Annotations.sendAddAnnotationValue("testanno space2", "val space");

		GenomeRelease.sendGetGenomeRelease();

//		Process.sendRawToProfile();
		Login.logout();

	}
}
