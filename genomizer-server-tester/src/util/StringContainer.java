package util;

/**
 * Class which contains a String which is used as a placeholder in those cases
 * where you need to set the reference during runtime.
 * Contains some standard getters and setters.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class StringContainer {
    private String s;

    public StringContainer(String s){
        this.s = s;
    }

    public String getString() {
        return s;
    }

    @Override
    public String toString() {
        return s;
    }

    public void setString(String s) {
        this.s = s;
    }
}
