package epfl.sweng.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.authentication.CredentialManager;
import epfl.sweng.quizquestions.QuizQuestion;

/**
 * 
 * @author LorenzoLeon
 * 
 *         This class is used to parse JSON Objects to QuizQuestion and vice
 *         versa.
 * 
 */
public class JSONParser {


    public static final int HTTP_ERROR = 404;

    public static JSONObject getParser(HttpResponse response)
        throws HttpResponseException, IOException {

        BasicResponseHandler responseHandler = new BasicResponseHandler();
        String jsonResponse = responseHandler.handleResponse(response);

        JSONObject jo;
        try {
            jo = new JSONObject(jsonResponse);
        } catch (JSONException e) {
            throw new IOException(e.getMessage());
        }
        return jo;

    }

    /**
     * Parses a JSONArray to a StringArray
     * 
     * @param array
     * @return String[]
     * @throws IOException
     * @throws JSONException
     */
    public static List<String> jsonArrayToList(JSONArray array)
        throws IOException {
        int size = array.length();
        List<String> stringList = new ArrayList<String>(size);

        for (int i = 0; i < size; i++) {
            try {
                stringList.add(array.getString(i));
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
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
    public static JSONObject parseQuiztoJSON(QuizQuestion question)
        throws JSONException {

        JSONObject jsonQuestion = new JSONObject();

        jsonQuestion.put("owner", CredentialManager.getInstance()
                .getUserCredential());
        jsonQuestion.put("id", question.getID());
        jsonQuestion.put("tags", new JSONArray(question.getTags()));
        jsonQuestion.put("solutionIndex", question.getIndex());
        jsonQuestion.put("answers", new JSONArray(question.getAnswers()));
        jsonQuestion.put("question", question.getQuestion());

        Debug.out(jsonQuestion);

        return jsonQuestion;
    }

    public static JSONObject parseKeyValuePairtoJSON(String key, String value)
        throws IOException {
        JSONObject jsontoken = new JSONObject();
        try {
            jsontoken.put(key, value);
        } catch (JSONException e) {
            throw new IOException(e.getMessage());
        }
        return jsontoken;
    }
}
