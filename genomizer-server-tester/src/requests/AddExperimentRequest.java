package requests;

import util.AnnotationDataValue;

/**
 * Request for adding a new experiment.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class AddExperimentRequest extends Request {

    public String name;
    public AnnotationDataValue[] annotations;

    /**
     * Constructor creating the request. Removes the annotations without
     * any value.
     *
     * @param experimentName String representing the name of the experiment.
     * @param annotations An array representing the annotations assigned to the
     *            experiment.
     */
    public AddExperimentRequest(String experimentName,
            AnnotationDataValue[] annotations) {
        super("addexperiment", "/experiment", "POST");
        this.name = experimentName;
        //this.annotations = annotations;
        int i = 0;
        for(AnnotationDataValue a: annotations) {
            if(a.value.isEmpty()) {
                i++;
            }
        }

        int j = 0;
        this.annotations = new AnnotationDataValue[annotations.length-i];
        for(AnnotationDataValue a: annotations) {
            if(!a.value.isEmpty()) {
                this.annotations[j] = a;
                j++;
            }
        }
    }
}
