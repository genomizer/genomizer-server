package testSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import testSuite.unitTests.AddAnnotationPossibilitiesTests;
import testSuite.unitTests.AddDropDownValueTest;
import testSuite.unitTests.ChangeAnnotationLabelTest;
import testSuite.unitTests.ExperimentTests;
import testSuite.unitTests.FileTableTests;
import testSuite.unitTests.GenomeReleaseTableTests;
import testSuite.unitTests.ProcessRawToProfileTests;
import testSuite.unitTests.PubMedIsValidTest;
import testSuite.unitTests.PubMedToSQLConverterTests;
import testSuite.unitTests.SearchDatabaseTests;
import testSuite.unitTests.AddNewFileTests;
import testSuite.unitTests.TestAnnotationRequiredDefault;
import testSuite.unitTests.TestChainFiles;
import testSuite.unitTests.TestChangeAnnotationValue;
import testSuite.unitTests.TestFileNameValidator;
import testSuite.unitTests.TestFilePathGEN;
import testSuite.unitTests.TestGetAnnotationObject;
import testSuite.unitTests.UpdateExperimentTest;
import testSuite.unitTests.UserInfoTests;
import testSuite.unitTests.removeAnnotationValueTest;


@RunWith(Suite.class)
@SuiteClasses({ AddAnnotationPossibilitiesTests.class, ExperimentTests.class,
        AddDropDownValueTest.class, FileTableTests.class, PubMedToSQLConverterTests.class,
        SearchDatabaseTests.class, AddNewFileTests.class,
        TestAnnotationRequiredDefault.class, TestChainFiles.class,
        TestChangeAnnotationValue.class, TestFileNameValidator.class,
        TestFilePathGEN.class, TestGetAnnotationObject.class,
        AddDropDownValueTest.class, removeAnnotationValueTest.class,
        UpdateExperimentTest.class, UserInfoTests.class,
        ProcessRawToProfileTests.class, GenomeReleaseTableTests.class,
        ChangeAnnotationLabelTest.class, PubMedIsValidTest.class})

public class AllTests {

}
