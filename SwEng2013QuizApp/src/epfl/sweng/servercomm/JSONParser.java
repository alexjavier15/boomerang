package epfl.sweng.servercomm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 *         This class is used to parse JSON Objects to QuizQuestion and vice
 *         versa.
 * 
 */
public class JSONParser {

	static final String[] QUIZ_KEYS = { "id", "question", "answers",
			"solutionIndex", "tags" };
	static final String[] TOKEN_KEYS = { "token", "message" };

	/**
	 * Parses a JSONObject from an HttpResponse to a QuizQuestion
	 * 
	 * @param response
	 * @return QuizQuestion
	 * @throws JSONException
	 * @throws IOException
	 */

	private static Map<String, Object> extractJSONMap(HttpResponse response,
			String[] keys) throws JSONException, IOException {

		Map<String, Object> jsonMap = new HashMap<String, Object>();

		if (response == null) {
			throw new HttpResponseException(404, "Empty response");
		}

		BasicResponseHandler responseHandler = new BasicResponseHandler();

		JSONObject parser = new JSONObject(
				responseHandler.handleResponse(response));

		for (String key : keys) {

			if (parser.has(key)) {

				jsonMap.put(key, parser.get(key));
			} else {
				throw new JSONException("Malformed JSONObject, key: " + key
						+ " doesn't exist");

			}

		}

		return jsonMap;

	}

	public static QuizQuestion parseJsonToQuiz(HttpResponse response)
			throws JSONException, IOException {

		Map<String, Object> jsonMap = extractJSONMap(response, QUIZ_KEYS);

		int id = Integer.valueOf((String) jsonMap.get(QUIZ_KEYS[0]));
		String question = (String) jsonMap.get(QUIZ_KEYS[1]);
		List<String> answers = jsonArrayToList((JSONArray) jsonMap
				.get(QUIZ_KEYS[2]));
		int solutionIndex = Integer.valueOf((String) jsonMap.get(QUIZ_KEYS[3]));
		List<String> tags = jsonArrayToList((JSONArray) jsonMap
				.get(QUIZ_KEYS[4]));

		return new QuizQuestion(id, question, (ArrayList<String>) answers,
				solutionIndex, tags);

	}

	/**
	 * Parses a QuizQuestion to a JSONObject
	 * 
	 * @param question
	 * @return jsonObject
	 * @throws JSONException
	 */
	public static JSONObject parseQuiztoJSON(QuizQuestion question)
			throws JSONException {

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
	public static List<String> jsonArrayToList(JSONArray array)
			throws JSONException {
		int size = array.length();
		List<String> stringList = new ArrayList<String>(size);

		for (int i = 0; i < size; i++) {
			stringList.add(array.getString(i));
		}

		return stringList;
	}
}
