package testSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testSuite.unitTests.AddAnnotationPossibilitiesTests;
import testSuite.unitTests.ExperimentTests;
import testSuite.unitTests.FileTableTests;
import testSuite.unitTests.PubMedToSQLConverterTests;
import testSuite.unitTests.SearchDatabaseTests;
import testSuite.unitTests.ServerAddFileTests;
import testSuite.unitTests.TestAnnotationRequiredDefault;
import testSuite.unitTests.TestChainFiles;
import testSuite.unitTests.TestChangeAnnotationValue;
import testSuite.unitTests.TestFileNameValidator;
import testSuite.unitTests.TestFilePathGEN;
import testSuite.unitTests.TestGetAnnotationObject;
import testSuite.unitTests.UserInfoTests;



@RunWith(Suite.class)
@SuiteClasses({ AddAnnotationPossibilitiesTests.class, ExperimentTests.class, FileTableTests.class,
				PubMedToSQLConverterTests.class, SearchDatabaseTests.class, ServerAddFileTests.class,
				TestAnnotationRequiredDefault.class, TestChainFiles.class, TestChangeAnnotationValue.class,
				TestFileNameValidator.class, TestFilePathGEN.class, TestGetAnnotationObject.class,
				UserInfoTests.class })
public class AllTests {

}
