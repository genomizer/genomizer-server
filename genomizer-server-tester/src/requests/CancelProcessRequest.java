package requests;
/**
 * Request for canceling a process.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class CancelProcessRequest extends Request {

    public String PID;
    /**
     * A constructor creating the request
     * @param PID Process-ID
     */
    public CancelProcessRequest(String PID) {
        super("cancelprocess","/process/cancelprocess" , "PUT");
        this.PID = PID;
    }

}
