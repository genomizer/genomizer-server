package requests;

/**
 * Request for getting process feedback.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ProcessFeedbackRequest extends Request {

    /**
     * Request to send.
     */
    public ProcessFeedbackRequest() {
        super("processfeedback", "/process", "GET");
    }
}
