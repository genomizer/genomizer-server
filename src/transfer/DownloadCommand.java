package transfer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.

public class DownloadCommand extends Command {

	private String filePath;

	public DownloadCommand(String filePath) {
		this.filePath = filePath;

	}


	public void Execute() {
		String url = urlFactory();	
		
		
		
	}

	private String urlFactory() {
		String url;


		url = "file://" + "webservernamn" + "/" + filePath;

		return url;
	}


}
