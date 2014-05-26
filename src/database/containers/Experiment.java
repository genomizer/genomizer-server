package database.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Container class for holding a list of all {@link FileTuple} objects and a
 * hashMap with all annotations of an Experiment. All annotations and filetuples
 * must be added after initialization.
 */
public class Experiment {

    private final String id;
    private HashMap<String, String> annotations = new HashMap<String, String>();
    private ArrayList<FileTuple> files = new ArrayList<FileTuple>();

    /**
     * Initializes an experiment. Parameter: ID of experiment (String)
     *
     * @param id String
     */
    public Experiment(String id) {

        this.id = id;
    }

    /**
     * Gets the experiment ID
     *
     * @return id String
     */
    public String getID() {

        return id;
    }

    /**
     * Returns a hashMap with all annotations of the experiment. The key is
     * The annotation in question and the value is its value.
     *
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getAnnotations() {

        return annotations;
    }

    /**
     * Adds an annotation to the experiment. Parameters: label (annotation
     * label, String) and value (annotation value, String) to the hashMap of
     * annotations.
     *
     * @param label String
     * @param value String
     */
    public void addAnnotation(String label, String value) {

        annotations.put(label, value);
    }

    /**
     * Returns the list with {@link FileTuple}.
     *
     * @return ArrayList<FileTuple>
     */
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

    /**
     * Adds a FileTuple object to the experiment. Parameter: {@link FileTuple}
     * object
     *
     * @param ft {@link FileTuple}
     */
    public void addFile(FileTuple ft) {

        files.add(ft);
    }

    /**
     * Outputs the experiment as a {@link String}.
     */
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
