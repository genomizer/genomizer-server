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
import database.testSuite.unitTests.MockUserTests;
import database.testSuite.unitTests.ProcessRawToProfileTests;
import database.testSuite.unitTests.PubMedIsValidTest;
import database.testSuite.unitTests.PubMedToSQLConverterTests;
import database.testSuite.unitTests.SearchDatabaseTests;
import database.testSuite.unitTests.TestAnnotationRequiredDefault;
import database.testSuite.unitTests.TestChainFiles;
import database.testSuite.unitTests.TestChangeAnnotationValue;
import database.testSuite.unitTests.TestFileNameValidator;
import database.testSuite.unitTests.TestFilePathGEN;
import database.testSuite.unitTests.TestGetAnnotationObject;
import database.testSuite.unitTests.UpdateExperimentTest;
import database.testSuite.unitTests.UserInfoTests;
import database.testSuite.unitTests.removeAnnotationValueTest;


@RunWith(Suite.class)
@SuiteClasses({ AddAnnotationPossibilitiesTests.class,
        ExperimentTests.class, AddDropDownValueTest.class,
        FileTableTests.class, PubMedToSQLConverterTests.class,
        SearchDatabaseTests.class, AddNewFileTests.class,
        TestAnnotationRequiredDefault.class, TestChainFiles.class,
        TestChangeAnnotationValue.class, TestFileNameValidator.class,
        TestFilePathGEN.class, TestGetAnnotationObject.class,
        AddDropDownValueTest.class, removeAnnotationValueTest.class,
        UpdateExperimentTest.class, UserInfoTests.class,
        ProcessRawToProfileTests.class,
        GenomeReleaseTableTests.class,
        ChangeAnnotationLabelTest.class, PubMedIsValidTest.class,
        GitHubIssuesTests.class, MockUserTests.class })
public class AllTests {

}
