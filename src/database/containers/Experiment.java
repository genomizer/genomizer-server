package database.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;




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

    public HashMap<String, String> getAnnotations() {
        return annotations;
    }

    public void addAnnotation(String label, String value) {
        annotations.put(label, value);
    }

    public ArrayList<FileTuple> getFiles() {
        return files;
    }

    public int getNrRawFiles() {
        int res = 0;
        for (FileTuple ft: files) {
            if (ft.type.equalsIgnoreCase("raw")) {
                res ++;
            }
        }
        return res;
    }

    public void addFile(FileTuple ft) {
        files.add(ft);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(id);
        for (Entry<String, String> a: annotations.entrySet()) {
            sb.append(" (");
            sb.append(a.getKey());
            sb.append(", ");
            sb.append(a.getValue());
            sb.append("),");
        }

        for (FileTuple ft: files) {
            sb.append("\n    ");
            sb.append(ft.path);
            sb.append(", ");
            sb.append(ft.type);
            sb.append(", ");
            sb.append(ft.uploader);
            sb.append(", ");
            sb.append(ft.author);
        }
        return sb.toString();
    }

}
