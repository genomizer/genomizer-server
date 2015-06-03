package api.commands;

/**
 * Super class for the commands which can be sent.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public abstract class SuperTestCommand  {
    public String ident;
    public boolean expectedResult;
    public boolean finalResult;

    /**
     * Creates the command with the given identity and expected result.
     * @param ident The identity of the command.
     * @param expectedResult The expected result. true if it should succeed,
     *                       otherwise false.
     */
    public SuperTestCommand(String ident, boolean expectedResult){
        this.ident = ident;
        this.expectedResult = expectedResult;
        this.finalResult = false;

    }

    /**
     * Abstract method which should execute the command in similar way to
     * the real way. Should send the request to the server, verify the
     * response returned and if something went wrong, log the error.
     */
    public abstract void execute();

    /**
     * Returns the identity String for the command.
     * @return The command's identity.
     */
    @Override
    public String toString(){
        return ident;
    }
}
