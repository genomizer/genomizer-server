package responses;

import model.ErrorLogger;
import util.*;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Parses the different responses.
 * Add the new classes here if they have a different format.
 *
 * @author c10mjn, ens11afk, c12slm
 * @version 1.0
 * 03 June 2015
 *
 */
public class ResponseParser {

    private static Gson gson = new Gson();

    public static LoginResponse parseLoginResponse(String json) {
        LoginResponse loginResponse;
        try {
            loginResponse = gson.fromJson(json, LoginResponse.class);
        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            return null;
        }
        return loginResponse;
    }

    public static ExperimentData parseRetrieveExp(String json) {
        ExperimentData retrieveExpResponse;
        try {
            retrieveExpResponse = gson.fromJson(json, ExperimentData.class);
        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            return null;
        }
        return retrieveExpResponse;
    }

    public static ExperimentData[] parseSearchResponse(String json) {
        ExperimentData[] searchResponses;
        try {
            searchResponses = gson.fromJson(json, ExperimentData[].class);
        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            return null;
        }
        return searchResponses;
    }

    public static UserList parseUserList(String json) {
        UserList userList;
        try {
            userList = gson.fromJson(json, UserList.class);
        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            return null;
        }
        return userList;
    }

    public static AnnotationDataType[] parseGetAnnotationResponse(String json) {
        AnnotationDataType[] annotationResponses;
        try {
            annotationResponses = gson.fromJson(json,
                    AnnotationDataType[].class);
        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            return null;
        }
        return annotationResponses;
    }

    public static GenomeReleaseData[] parseGetGenomeReleaseResponse(String json) {

        GenomeReleaseData[] genomeReleaseResponses;
        try {

            genomeReleaseResponses = gson.fromJson(json,
                    GenomeReleaseData[].class);

        } catch (JsonParseException e) {
            ErrorLogger.log(e);
//            System.out.println("Could not parse json GR.");
            return null;
        }

        return genomeReleaseResponses;
    }

    public static AddFileToExperimentResponse parseUploadResponse(String json) {
        AddFileToExperimentResponse url = null;
        try {

            url = gson.fromJson(json, AddFileToExperimentResponse.class);
        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            // TODO hantera exception CF
//            System.out.println("parse error");
        }
        return url;
    }

    public static AddGenomeReleaseResponse[] parseGenomeUploadResponse(
            String json) {

        AddGenomeReleaseResponse[] urls = null;
        try {
            urls = gson.fromJson(json, AddGenomeReleaseResponse[].class);

        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            // TODO Hantera exception CF
//            System.err
//                    .println("Could not parse url response for adding genome release.");
        }

        return urls;
    }

    public static ProcessFeedbackData[] parseProcessFeedbackResponse(String json) {
        ProcessFeedbackData[] processFeedbackData;
        try {
            processFeedbackData = gson.fromJson(json,
                    ProcessFeedbackData[].class);
        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            // TODO: StackTrace ;/
            e.printStackTrace();
            return null;
        }
        return processFeedbackData;
    }

    public static ErrorResponse parseErrorResponse(String json) {

        ErrorResponse response;
        try {
            response = gson.fromJson(json, ErrorResponse.class);
        } catch (JsonParseException e) {
            ErrorLogger.log(e);
            // TODO: StackTrace ;/
            response = new ErrorResponse();
            response.message = "Unknown error";
        }
        return response;
    }
}
