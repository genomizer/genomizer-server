package util;

/**
 * Contains the annotation name and the appropriate getters.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AnnotationData {

    public String name;

    public AnnotationData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){

        return getName();
    }
}
