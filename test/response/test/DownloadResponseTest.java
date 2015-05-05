package response.test;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import response.DownloadResponse;

import java.util.ArrayList;

public class DownloadResponseTest {

    @Test
    public void shouldExposePrivateMembers() {
        ArrayList<String> arr = new ArrayList<>();
        arr.add("foo");
        arr.add("foo");
        arr.add("foo");
        arr.add("foo");
        arr.add("foo");
        DownloadResponse resp = new DownloadResponse(200, arr);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().disableHtmlEscaping().create();
        String jsonString = gson.toJson(resp);
        assertEquals("{\"experimentID\":\"foo\",\"fileName\":\"foo\",\"size\":\"foo\",\"type\":\"foo\",\"URL\":\"foo\"}", jsonString);
    }
}
