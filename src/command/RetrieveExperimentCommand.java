package command;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//TODO: Test JSON - convert.

/**
 * Class used to retrieve an experiment.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class RetrieveExperimentCommand extends Command {

	@Expose
	private String name;

	@SerializedName("created by")
	@Expose
	private String created_by;

	@Expose
	private Annotations annotations = new Annotations();

	/**
	 * Empty constructor.
	 */
	public RetrieveExperimentCommand() {

	}

	@Override
	public boolean validate() {

		// TODO Auto-generated method stub
		return false;

	}

	@Override
	public void execute() {

		// TODO Auto-generated method stub

	}

	/**
	 * Class used to store annotation.
	 * @author tfy09jnn
	 * @version 1.0
	 */
	class Annotations {

		@Expose
		private String pubmedId;

		@Expose
		private String type;

		@Expose
		private String specie;

		@Expose
		private String genoRelease;

		@Expose
		private String cellLine;

		@Expose
		private String devStage;

		@Expose
		private String sex;

		@Expose
		private String tissue;

		/**
		 * Empty constructor.
		 */
		public Annotations() {

		}

	}

}