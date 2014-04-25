package transfer;

/**
 * Project: genomizer-Server
 * Package: transfer
 * User: c08esn
 * Date: 4/25/14
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadCommand extends Command {

    private String path;

    public UploadCommand(String path) {
        this.path = path;
    }

    @Override
    public void Execute() {

    }

    public String getPath() {
        return path;
    }
}
