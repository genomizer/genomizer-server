package requests;

import util.AnnotationDataValue;

/**
 * Request for editing experiments.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ChangeExperimentRequest extends Request {

    public String name;
    public AnnotationDataValue[] annotations;

    /**
     * Constructor creating the request. Removes the annotations without
     * any value.
     * @param experimentName String representing the name of the experiment.
     * @param annotations An array representing the annotations assigned to the
     *            experiment.
     */
    public ChangeExperimentRequest(String experimentName,
            AnnotationDataValue[] annotations) {
        super("addexperiment", "/experiment/"+experimentName, "PUT");
        this.name = experimentName;

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
