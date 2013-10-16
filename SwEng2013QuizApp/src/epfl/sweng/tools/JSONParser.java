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

	public static final int HTTP_ERROR = 404;

	/*
	 * public enum QuizKeys { id, question, answers, solutionIndex, tags }
	 * public enum TokenKeys { token, message };
	 * /** Parses a JSONObject from an HttpResponse to a QuizQuestion
	 * @param response
	 * @return QuizQuestion
	 * @throws JSONException
	 * @throws IOException
	 * public static Map<String, Object> extractJSONMap(HttpResponse response, Enum<?>[] keys) throws JSONException,
	 * IOException {
	 * Map<String, Object> jsonMap = new HashMap<String, Object>();
	 * if (response == null) { throw new HttpResponseException(HTTP_ERROR, "Empty response"); }
	 * BasicResponseHandler responseHandler = new BasicResponseHandler();
	 * JSONObject parser = new JSONObject( responseHandler.handleResponse(response));
	 * for (Enum<?> key : keys) {
	 * if (parser.has(key.name())) {
	 * jsonMap.put(key.name(), parser.get(key.name())); Debug.out(parser.get(key.name())); } else { throw new
	 * JSONException("Malformed JSONObject, key: " + key + " doesn't exist");
	 * }
	 * }
	 * return jsonMap;
	 * }
	 * public static QuizQuestion parseJsonToQuiz(HttpResponse response) throws JSONException, IOException {
	 * Map<String, Object> jsonMap = extractJSONMap(response, QuizKeys.values());
	 * long id = (Long) jsonMap.get(QuizKeys.id.name()); String question = (String)
	 * jsonMap.get(QuizKeys.question.name()); List<String> answers = jsonArrayToList((JSONArray) jsonMap
	 * .get(QuizKeys.answers.name())); int solutionIndex = (Integer) jsonMap .get(QuizKeys.solutionIndex.name());
	 * List<String> tags = jsonArrayToList((JSONArray) jsonMap .get(QuizKeys.tags.name()));
	 * return new QuizQuestion(id, question, (ArrayList<String>) answers, solutionIndex, tags);
	 * }
	 */

	private static JSONObject getParser(HttpResponse response) throws JSONException, IOException {
		if (response == null) {
			throw new HttpResponseException(HTTP_ERROR, "Empty response");
		}
		BasicResponseHandler responseHandler = new BasicResponseHandler();
		return new JSONObject(responseHandler.handleResponse(response));
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

	public static String parseJsonGetKey(HttpResponse response, String key) throws JSONException, IOException {
		JSONObject parser = getParser(response);
		return parser.getString(key);
	}

	/**
	 * Parses a JSONObject from an HttpResponse to a QuizQuestion
	 * 
	 * @param response
	 * @return QuizQuestion
	 * @throws HttpResponseException
	 * @throws JSONException
	 * @throws IOException
	 */
	public static QuizQuestion parseJsonToQuiz(HttpResponse response) throws HttpResponseException, JSONException,
		IOException {
		JSONObject parser = getParser(response);

		int id = parser.getInt("id");

		String question = parser.getString("question");

		List<String> answers = jsonArrayToList(parser.getJSONArray("answers"));

		int solutionIndex = parser.getInt("solutionIndex");

		List<String> tags = jsonArrayToList(parser.getJSONArray("tags"));

		return new QuizQuestion(id, question, answers, solutionIndex, tags);

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

	public static JSONObject parseTokentoJSON(String token) throws JSONException {
		JSONObject jsontoken = new JSONObject();
		jsontoken.put("token", token);
		return jsontoken;
	}
}
