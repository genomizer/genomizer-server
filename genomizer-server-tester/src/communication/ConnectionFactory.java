package communication;

/**
 * Creates the connections used by the program.
 *
 * @author c11 dkn
 * @version 1.0
 * 16 May 2015
 *
 */
public class ConnectionFactory {

    private String ip;

    public ConnectionFactory() {

    }

    public Connection makeConnection() {
        return new Connection(ip);
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getIP() {
        return ip;
    }

}
