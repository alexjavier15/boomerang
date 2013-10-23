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

	private static JSONObject getParser(HttpResponse response)
		throws IOException {
		if (response == null) {
			throw new HttpResponseException(HTTP_ERROR, "Empty response");
		}
		BasicResponseHandler responseHandler = new BasicResponseHandler();
		JSONObject jO = null;
		try {
			jO = new JSONObject(responseHandler.handleResponse(response));
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}
		return jO;
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

	public static String parseJsonGetKey(HttpResponse response, String key)
		throws IOException {
		JSONObject parser = getParser(response);
		try {
			return parser.getString(key);
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}
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
	public static QuizQuestion parseJsonToQuiz(HttpResponse response)
		throws IOException {
		JSONObject parser = getParser(response);
		int id;
		String question;
		List<String> answers;
		int solutionIndex;
		List<String> tags;
		try {
			id = parser.getInt("id");
			question = parser.getString("question");
			answers = jsonArrayToList(parser.getJSONArray("answers"));
			solutionIndex = parser.getInt("solutionIndex");
			tags = jsonArrayToList(parser.getJSONArray("tags"));
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}

		return new QuizQuestion(id, question, answers, solutionIndex, tags);

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
		throws IOException {

		JSONObject jsonQuestion = new JSONObject();
		try {
			jsonQuestion.put("tags", new JSONArray(question.getTags()));
			jsonQuestion.put("solutionIndex", question.getIndex());
			jsonQuestion.put("answers", new JSONArray(question.getAnswers()));
			jsonQuestion.put("question", question.getQuestion());
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}

		Debug.out(jsonQuestion);

		return jsonQuestion;
	}

	public static JSONObject parseTokentoJSON(String token) throws IOException {
		JSONObject jsontoken = new JSONObject();
		try {
			jsontoken.put("token", token);
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		}
		return jsontoken;
	}
}
