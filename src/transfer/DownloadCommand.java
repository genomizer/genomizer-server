package transfer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DownloadCommand extends Command {

	private String filePath;

	public DownloadCommand(String filePath) {
		this.filePath = filePath;

	}


	public void Execute() {




	}

	private String urlFactory() {
		String url;

		String host = null;
		try {
			host = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		url = "file://" + host + "/" + filePath;

		return url;
	}


}
