package database.testSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import database.testSuite.unitTests.AddAnnotationPossibilitiesTests;
import database.testSuite.unitTests.AddDropDownValueTest;
import database.testSuite.unitTests.AddNewFileTests;
import database.testSuite.unitTests.ChangeAnnotationLabelTest;
import database.testSuite.unitTests.ExperimentTests;
import database.testSuite.unitTests.FileTableTests;
import database.testSuite.unitTests.GenomeReleaseTableTests;
import database.testSuite.unitTests.GitHubIssuesTests;
import database.testSuite.unitTests.HackingTest;
import database.testSuite.unitTests.MockUserTests;
import database.testSuite.unitTests.ProcessRawToProfileTests;
import database.testSuite.unitTests.PubMedIsValidTest;
import database.testSuite.unitTests.PubMedToSQLConverterTests;
import database.testSuite.unitTests.SearchDatabaseTests;
import database.testSuite.unitTests.AnnotationRequiredDefaultTest;
import database.testSuite.unitTests.ChainFilesTest;
import database.testSuite.unitTests.ChangeAnnotationValueTest;
import database.testSuite.unitTests.FileNameValidatorTest;
import database.testSuite.unitTests.FilePathGeneratorTest;
import database.testSuite.unitTests.GetAnnotationObjectTest;
import database.testSuite.unitTests.UpdateExperimentTest;
import database.testSuite.unitTests.UserInfoTests;
import database.testSuite.unitTests.RemoveAnnotationValueTest;


@RunWith(Suite.class)
@SuiteClasses({ AddAnnotationPossibilitiesTests.class,
        ExperimentTests.class, AddDropDownValueTest.class,
        FileTableTests.class, PubMedToSQLConverterTests.class,
        SearchDatabaseTests.class, AddNewFileTests.class,
        AnnotationRequiredDefaultTest.class, ChainFilesTest.class,
        ChangeAnnotationValueTest.class, FileNameValidatorTest.class,
        FilePathGeneratorTest.class, GetAnnotationObjectTest.class,
        AddDropDownValueTest.class, RemoveAnnotationValueTest.class,
        UpdateExperimentTest.class, UserInfoTests.class,
        ProcessRawToProfileTests.class,
        GenomeReleaseTableTests.class,
        ChangeAnnotationLabelTest.class, PubMedIsValidTest.class,
        GitHubIssuesTests.class, MockUserTests.class , HackingTest.class})
public class AllTests {

}
