package response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class AnnotationInformation {

	public static final int TYPE_DROP_DOWN = 1;
	public static final int TYPE_FREE_TEXT = 2;

	@Expose
	private int id;

	@Expose
	private String name;

	@Expose
	private int type;

	@Expose
	private ArrayList<String> values;

	@Expose
	private boolean forced;

	public AnnotationInformation(int id, String name, int type,
			ArrayList<String> values, boolean forced) {

		this.id = id;
		this.name = name;
		this.type = type;
		this.values = values;
		this.forced = forced;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public boolean isForced() {
		return forced;
	}

	public void setId(int newid) {
		id = newid;
	}


}
