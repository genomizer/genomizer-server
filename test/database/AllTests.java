package database.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import database.test.unittests.AddAnnotationPossibilitiesTests;
import database.test.unittests.AddDropDownValueTest;
import database.test.unittests.AddNewFileTests;
import database.test.unittests.ChangeAnnotationLabelTest;
import database.test.unittests.ExperimentTests;
import database.test.unittests.FileTableTests;
import database.test.unittests.GenomeReleaseTableTests;
import database.test.unittests.GitHubIssuesTests;
import database.test.unittests.HackingTest;
import database.test.unittests.MockUserTests;
import database.test.unittests.ProcessRawToProfileTests;
import database.test.unittests.PubMedIsValidTest;
import database.test.unittests.PubMedToSQLConverterTests;
import database.test.unittests.SearchDatabaseTests;
import database.test.unittests.AnnotationRequiredDefaultTest;
import database.test.unittests.ChainFilesTest;
import database.test.unittests.ChangeAnnotationValueTest;
import database.test.unittests.FileNameValidatorTest;
import database.test.unittests.FilePathGeneratorTest;
import database.test.unittests.GetAnnotationObjectTest;
import database.test.unittests.UpdateExperimentTest;
import database.test.unittests.UserInfoTests;
import database.test.unittests.RemoveAnnotationValueTest;


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
