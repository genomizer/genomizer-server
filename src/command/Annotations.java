package command;

import com.google.gson.annotations.Expose;

//TODO: Add set/getters.

/**
 * Class used to store annotation.
 * @author tfy09jnn
 * @version 1.0
 */
public class Annotations {

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
