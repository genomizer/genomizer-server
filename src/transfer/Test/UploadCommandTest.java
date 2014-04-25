package transfer.Test;

import org.junit.Before;
import org.junit.Test;
import transfer.UploadCommand;

import static org.junit.Assert.assertNotNull;

/**
 * Project: genomizer-Server
 * Package: transfer.Test
 * User: c08esn
 * Date: 4/25/14
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadCommandTest {

    private UploadCommand uploadCommand;

    @Before
    public void setup() {
        uploadCommand = new UploadCommand("fake path");
    }

    @Test
    public void shouldHavePath() {
        assertNotNull(uploadCommand.getPath());
    }

    @Test
    public void shouldHaveSocket() {

    }
}
