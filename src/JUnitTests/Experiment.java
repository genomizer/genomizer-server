package JUnitTests;

import java.util.HashMap;
import java.util.Map;

public class Experiment {
    
    private final String id;
    private Map<String, String> annotations = new HashMap<String, String>();
    
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

}
