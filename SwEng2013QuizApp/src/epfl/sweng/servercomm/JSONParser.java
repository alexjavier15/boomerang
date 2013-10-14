package epfl.sweng.servercomm;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
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
 * 			This class is used to parse JSON Objects to QuizQuestion and vice versa.
 *
 */
public class JSONParser {

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
		throws HttpResponseException, JSONException, IOException {

		final int errorCode = 404;
		
		if (response == null) {
			throw new HttpResponseException(errorCode, "Empty response");
		}

		BasicResponseHandler responseHandler = new BasicResponseHandler();
		JSONObject parser = new JSONObject(
				responseHandler.handleResponse(response));
		int id = parser.getInt("id");
		
		String question = parser.getString("question");
		String[] answers = jsonArrayToStringArray(parser
				.getJSONArray("answers"));
		
		int solutionIndex = parser.getInt("solutionIndex");
		String[] tags = jsonArrayToStringArray(parser.getJSONArray("tags"));
		Set<String> set = new HashSet<String>(Arrays.asList(tags));
		
		return new QuizQuestion(id, question, Arrays.asList(answers),
				solutionIndex, set);

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
		jsonQuestion.put("tags", new JSONArray(question.getSetOfTags()));
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
	private static String[] jsonArrayToStringArray(JSONArray array)
		throws JSONException {
		
		int numAnswers = array.length();
		String[] newStringArray = new String[numAnswers];
		
		for (int i = 0; i < numAnswers; i++) {
			newStringArray[i] = array.getString(i);
		}
		
		return newStringArray;
	}
}
