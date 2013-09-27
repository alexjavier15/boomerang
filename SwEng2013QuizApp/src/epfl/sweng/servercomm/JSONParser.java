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

public class JSONParser {
	
	public static QuizQuestion parseJsonToQuiz(HttpResponse response)
			throws HttpResponseException, JSONException, IOException {
		
		if (response == null){
			throw new HttpResponseException(404,"Empty response");
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
	
	public static JSONObject parseQuiztoJSON(QuizQuestion question){
		JSONObject jsonQuestion = new JSONObject();
		jsonQuestion.put("id", question.getID());
		jsonQuestion.put("question", question.getQuestion());
		jsonQuestion.put("answers" , new JSONArray(question.getAnswers()) );
		jsonQuestion.put("solutionIndex" , question.getIndex() );
		jsonQuestion.put("tags", new JSONArray(question.getvalue()));
		
		return jsonQuestion;
	}
	
	

	private static String[] jsonArrayToStringArray(JSONArray array)
			throws JSONException {
		int numAnswers = array.length();
		String[] newStringArray = new String[numAnswers];
		for (int i = 0; i <= numAnswers; i++) {
			newStringArray[i] = array.getString(i);
		}
		return newStringArray;
	}
}
