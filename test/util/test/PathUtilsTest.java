package util.test;

import org.junit.Test;
import util.PathUtils;

import static org.junit.Assert.*;

public class PathUtilsTest {

    @Test
    public void testJoin() {
        assertEquals("foo/bar", PathUtils.join("foo", "bar"));
        assertEquals("foo/bar", PathUtils.join("foo", "/bar"));
        assertEquals("foo/bar", PathUtils.join("foo/", "bar"));
        assertEquals("foo/bar", PathUtils.join("foo/", "/bar"));

        assertEquals("foo/bar/baz", PathUtils.join("foo", "bar/baz"));
        assertEquals("foo/bar/baz", PathUtils.join("foo", "/bar/baz"));
        assertEquals("foo/bar/baz", PathUtils.join("foo/", "bar/baz"));
        assertEquals("foo/bar/baz", PathUtils.join("foo/", "/bar/baz"));
    }
}
