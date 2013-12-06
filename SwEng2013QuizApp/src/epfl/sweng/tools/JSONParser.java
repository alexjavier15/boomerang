package epfl.sweng.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import epfl.sweng.authentication.CredentialManager;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * 
 * @author LorenzoLeon
 * 
 *         This class is used to parse JSON Objects to QuizQuestion and vice versa.
 * 
 */
public class JSONParser {

    public static final int HTTP_ERROR = 404;
    private static final String TAG = "JSONParser";

    public static JSONObject getParser(HttpResponse response) throws JSONException {
        JSONObject json = null;

        BasicResponseHandler responseHandler = new BasicResponseHandler();
        String jsonResponse = null;
        try {
            jsonResponse = responseHandler.handleResponse(response);
            json = new JSONObject(jsonResponse);
        } catch (IOException e) {
            Log.v(TAG, e.getMessage());
        }

        return json;

    }

    /**
     * Parses a JSONArray to a StringArray
     * 
     * @param array
     * @return String[]
     * @throws IOException
     * @throws JSONException
     */
    public static List<String> jsonArrayToList(JSONArray array) throws JSONException {
        int size = array.length();
        List<String> stringList = new ArrayList<String>(size);

        for (int i = 0; i < size; i++) {

            stringList.add(array.getString(i));

        }

        return stringList;
    }

    /**
     * Parses a QuizQuestion to a JSONObject
     * 
     * @param question
     * @return jsonObject
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject parseQuiztoJSON(QuizQuestion question) throws JSONException {

        JSONObject jsonQuestion = new JSONObject();

        jsonQuestion.put("owner", CredentialManager.getInstance().getUserCredential());
        jsonQuestion.put("id", question.getID());
        jsonQuestion.put("tags", new JSONArray(question.getTags()));
        jsonQuestion.put("solutionIndex", question.getIndex());
        jsonQuestion.put("answers", new JSONArray(question.getAnswers()));
        jsonQuestion.put("question", question.getQuestion());

        // Debug.out(jsonQuestion);

        return jsonQuestion;
    }

}
