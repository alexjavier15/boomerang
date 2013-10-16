package epfl.sweng.servercomm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Html;

import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.testing.Debug;

/**
 * 
 * @author LorenzoLeon
 * 
 *         This class is used to parse JSON Objects to QuizQuestion and vice versa.
 * 
 */
public class JSONParser {

    public interface Keys {
        public String name();

    }

    public enum QuizKeys implements Keys {
        id, question, answers, solutionIndex, tags
    }

    public enum TokenKeys implements Keys {
        token, message
    };

    public enum SessionKeys implements Keys {
        session, message
    };

    public enum Tequila {
        token, session
    };

    /**
     * Parses a JSONObject from an HttpResponse to a QuizQuestion
     * 
     * @param response
     * @return QuizQuestion
     * @throws JSONException
     * @throws IOException
     */

    private static Map<String, Object> extractJSONMap(HttpResponse response, Keys[] keys) throws JSONException,
            IOException {

        Map<String, Object> jsonMap = new HashMap<String, Object>();

        if (response == null) {
            throw new HttpResponseException(404, "Empty response");
        }

        BasicResponseHandler responseHandler = new BasicResponseHandler();

        // Debug.out(response.getEntity().getContent().toString());
        JSONObject parser = new JSONObject(responseHandler.handleResponse(response));

        for (Keys key : keys) {

            if (parser.has(key.name())) {

                jsonMap.put(key.name(), parser.get(key.name()));
                Debug.out(parser.get(key.name()));
            } else {
                throw new JSONException("Malformed JSONObject, key: " + key + " doesn't exist");

            }

        }

        return jsonMap;

    }

    public static QuizQuestion parseJsonToQuiz(HttpResponse response) throws JSONException, IOException {

        Map<String, Object> jsonMap = extractJSONMap(response, QuizKeys.values());

        long id = ((Long) jsonMap.get(QuizKeys.id.name()));
        String question = (String) jsonMap.get(QuizKeys.question.name());
        List<String> answers = jsonArrayToList((JSONArray) jsonMap.get(QuizKeys.answers.name()));
        int solutionIndex = ((Integer) jsonMap.get(QuizKeys.solutionIndex.name()));
        List<String> tags = jsonArrayToList((JSONArray) jsonMap.get(QuizKeys.tags.name()));

        return new QuizQuestion(id, question, (ArrayList<String>) answers, solutionIndex, tags);

    }

    public static HashMap<String, String> parseJsonTequila(HttpResponse response, Tequila option) throws JSONException,
            IOException {

        HashMap<String, String> tequilaMap = new HashMap<String, String>(2);
        Map<String, Object> jsonMap = null;

        switch (option) {

            case token:

                jsonMap = extractJSONMap(response, TokenKeys.values());
                for (TokenKeys key : TokenKeys.values()) {
                    if (jsonMap.containsKey(key.name())) {

                        tequilaMap.put(key.name(), (String) jsonMap.get(key.name()));
                    }

                }

                return tequilaMap;
            case session:
                jsonMap = extractJSONMap(response, SessionKeys.values());
                for (SessionKeys key : SessionKeys.values()) {
                    if (jsonMap.containsKey(key.name())) {

                        tequilaMap.put(key.name(), (String) jsonMap.get(key.name()));
                    }

                }

                return tequilaMap;
            default:
                return null;
        }

    }

    /**
     * Parses a QuizQuestion to a JSONObject
     * 
     * @param question
     * @return jsonObject
     * @throws JSONException
     */
    public static JSONObject parseQuiztoJSON(QuizQuestion question) throws JSONException {

        JSONObject jsonQuestion = new JSONObject();
        jsonQuestion.put("tags", new JSONArray(question.getTags()));
        jsonQuestion.put("solutionIndex", question.getIndex());
        jsonQuestion.put("answers", new JSONArray(question.getAnswers()));
        jsonQuestion.put("question", question.getQuestion());

        Debug.out(jsonQuestion);

        return jsonQuestion;
    }

    /**
     * Parses a JSONArray to a StringArray
     * 
     * @param array
     * @return String[]
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
}
