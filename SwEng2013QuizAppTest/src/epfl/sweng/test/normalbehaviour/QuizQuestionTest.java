package epfl.sweng.test.normalbehaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.accounts.NetworkErrorException;

import junit.framework.TestCase;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.tools.JSONParser;

public class QuizQuestionTest extends TestCase {

	private QuizQuestion mQuestion = createQuestion();

	public QuizQuestion createQuestion() {
		String question = "What is the answer to life, the universe, and everything?";
		ArrayList<String> answers = new ArrayList<String>();
		answers.add("Forty-two");
		answers.add("Twenty-seven");
		int sol = 0;
		Set<String> tags = new HashSet<String>(Arrays.asList("h2g2", "trivia"));
		return new QuizQuestion(question, answers, sol, tags, -1, "owner");
	}

	public void testGettersQuestion() {

		assertEquals(mQuestion.getID(), -1);
		assertEquals(mQuestion.getCorrectAnswer(), "Forty-two");
		assertEquals(mQuestion.checkAnswer(0), true);
		ArrayList<String> ans = new ArrayList<String>();
		ans.add("Forty-two");
		ans.add("Twenty-seven");
		assertEquals(mQuestion.getAnswers(), ans);
		Set<String> tags = new HashSet<String>(Arrays.asList("h2g2", "trivia"));
		assertEquals(mQuestion.getTags(), tags);
		assertEquals(mQuestion.getIndex(), 0);
		assertEquals(mQuestion.getOwner(), "owner");
		HttpResponse response = null;

		QuizQuestion question = createQuestion();
		for (int i = 0; i < 10; i++) {
			try {

				response = HttpComms.getInstance()
						.postJSONObject(HttpComms.URLPUSH,
								JSONParser.parseQuiztoJSON(question));
				System.out.println(response.getStatusLine());
				response.getEntity().consumeContent();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (NetworkErrorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		assertTrue(response != null);
	}

	public void testSetterQuestion() {
		try {
			String json = JSONParser.parseQuiztoJSON(mQuestion).toString();
			QuizQuestion test = new QuizQuestion(json);
			test.setQuestion("salut");
			assertEquals(test.getQuestion(), "salut");
			Set<String> tagsIn = new HashSet<String>(Arrays.asList(new String[] {"1", "2", "5"}));
			test.setTags(tagsIn);
			assertEquals(test.getTags(), tagsIn);
			List<String> strings = Arrays.asList(new String[] {"1", "2"});
			test.setAnswers(strings);
			assertEquals(test.getAnswers(), strings);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
