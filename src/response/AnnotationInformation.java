package response;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class AnnotationInformation {


	@Expose
	private int id;

	@Expose
	private String name;

	@Expose
	private ArrayList<String> values;

	@Expose
	private boolean forced;

	public AnnotationInformation(int id, String name,
			ArrayList<String> values, boolean forced) {

		this.id = id;
		this.name = name;
		this.values = values;
		this.forced = forced;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

//	public int getType() {
//		return type;
//	}

	public ArrayList<String> getValues() {
		return values;
	}

	public boolean isForced() {

		return forced;
	}

	public void setId(int newid) {
		id = newid;
	}

	public String toString() {
		String returnstring = "ID: " + id + "\nNAME: " + name + "\nFORCED: " + forced + "\nVALUES:\n" ;

		if(values != null) {
			for(int i = 0; i < values.size(); i++) {
				returnstring = returnstring + "     " + values.get(i) + "\n";
			}
		}


		return returnstring;

	}


}
