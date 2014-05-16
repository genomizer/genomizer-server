package database;

import java.io.IOException;

public class DuplicatePrimaryKeyException extends IOException {

    private static final long serialVersionUID = 201405161940L;


    public DuplicatePrimaryKeyException(String string) {
        super(string);
    }
}
