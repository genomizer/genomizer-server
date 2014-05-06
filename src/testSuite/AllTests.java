package testSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testSuite.unitTests.AddAnnotationPossibilitiesTests;
import testSuite.unitTests.ExperimentTests;
import testSuite.unitTests.FileTableTests;
import testSuite.unitTests.PubMedToSQLConverterTests;
import testSuite.unitTests.SearchDatabaseTests;
import testSuite.unitTests.TestFilePathGEN;
import testSuite.unitTests.UserInfoTests;


@RunWith(Suite.class)
@SuiteClasses({ TestFilePathGEN.class, UserInfoTests.class, AddAnnotationPossibilitiesTests.class,
        ExperimentTests.class, FileTableTests.class, PubMedToSQLConverterTests.class, SearchDatabaseTests.class })
public class AllTests {

}
