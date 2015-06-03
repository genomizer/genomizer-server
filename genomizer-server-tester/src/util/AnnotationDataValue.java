package util;

/**
 * Class representing an annotation. Contains the name of the annotation, the
 * corresponding value and the annotation id.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AnnotationDataValue extends AnnotationData {

    public String value;

    // TODO: What is 'id', and what runTests purpose? (OO)
    public AnnotationDataValue(String id, String name, String value) {
        super(name);
        this.value = value;
    }

    // Test purpose
    public String getValue() {
        return value;
    }

    @Override
    public String toString(){
        return "Annotation Data Value: " + super.toString() + ", "+ getValue();
    }

}
