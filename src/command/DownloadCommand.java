package command;

import com.google.gson.annotations.Expose;

public class DownloadCommand extends Command {

	//@SerializedName("attr1")
	@Expose
	private String attr1;

	//@SerializedName("attr2")
	@Expose
	private String attr2;

	//@SerializedName("attr3")
	@Expose
	private String attr3;

	//@SerializedName("attr4")
	@Expose
	private String attr4;

	//Constructor.
	public DownloadCommand() {
	}


	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initiateJson(String[] values) {

		attr1 = values[0];
		attr2 = values[1];
		attr3 = values[2];
		attr4 = values[3];
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attr1 == null) ? 0 : attr1.hashCode());
		result = prime * result + ((attr2 == null) ? 0 : attr2.hashCode());
		result = prime * result + ((attr3 == null) ? 0 : attr3.hashCode());
		result = prime * result + ((attr4 == null) ? 0 : attr4.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DownloadCommand other = (DownloadCommand) obj;
		if (attr1 == null) {
			if (other.attr1 != null)
				return false;
		} else if (!attr1.equals(other.attr1))
			return false;
		if (attr2 == null) {
			if (other.attr2 != null)
				return false;
		} else if (!attr2.equals(other.attr2))
			return false;
		if (attr3 == null) {
			if (other.attr3 != null)
				return false;
		} else if (!attr3.equals(other.attr3))
			return false;
		if (attr4 == null) {
			if (other.attr4 != null)
				return false;
		} else if (!attr4.equals(other.attr4))
			return false;
		return true;
	}

}
