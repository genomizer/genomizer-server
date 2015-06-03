package util;

/**
 * Contains the annotation values and the appropriate getters and setters.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AnnotationDataType extends AnnotationData {

    public String[] values;
    public boolean forced;

    public AnnotationDataType(String name, String[] values, Boolean forced) {
        super(name);
        this.values = values;
        this.forced = forced;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String[] getValues() {
        return values;
    }

    public boolean isForced() {
        return forced;
    }

    public int indexOf(String value) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value)) {
                return i;
            }
        }
        return -1;

    }

    public void addValue(String string) {
        String[] newValues = new String[values.length + 1];
        int i = 0;
        for (String s : values) {
            newValues[i] = s;
            i++;
        }
        newValues[i] = string;
        values = newValues;
    }

}
