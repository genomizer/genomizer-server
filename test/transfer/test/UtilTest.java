package transfer.test;

import org.junit.Test;

import java.io.IOException;

import static transfer.Util.validatePath;

public class UtilTest {

    @Test(expected=IOException.class)
    public void absolutePathsAreNotAllowed() throws IOException {
        validatePath("/an/absolute/path");
    }

    @Test(expected=IOException.class)
    public void pathsEndingWithSlashAreNotAllowed() throws IOException {
        validatePath("path/ending/with/slash/");
    }

    @Test(expected=IOException.class)
    public void neitherArePathsEndingAndStartingWithSlash() throws IOException {
        validatePath("/path/ending/and/starting/with/slash/");
    }

    @Test(expected=IOException.class)
    public void dotsAreNotAllowed() throws IOException {
        validatePath("a/path/with/a/dot/./in/it");
    }

    @Test(expected=IOException.class)
    public void doubleDotsAreNotAllowed() throws IOException {
        validatePath("a/path/with/a/double/dot/../in/it");
    }

    @Test(expected=IOException.class)
    public void multipleSlashesAreNotAllowed() throws IOException {
        validatePath("a/path/with/a/double/slash//in/it");
    }

    @Test(expected=IOException.class)
    public void invalidCharactersAreNotAllowed() throws IOException {
        validatePath("a/path/with/some/invalid!?#$%^*/characters/in/it");
    }

    @Test
    public void someSeeminglyInvalidCharactersAreAllowed() throws IOException {
        validatePath("a/path/with/some/seemingly/invalid/ÅÄÖ AWE90a/characters/in/it");
    }

}
