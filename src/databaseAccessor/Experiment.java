package databaseAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Experiment {

    private final String id;
    private HashMap<String, String> annotations = new HashMap<String, String>();
    private ArrayList<FileTuple> files = new ArrayList<FileTuple>();

    public Experiment(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(String label, String value) {
        annotations.put(label, value);
    }

    public List<FileTuple> getFiles() {
        return files;
    }

    public void addFile(FileTuple ft) {
        files.add(ft);
    }

}
