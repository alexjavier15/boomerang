package epfl.sweng.questions;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class QuizQuestionTest extends TestCase {
	
	QuizQuestion question = createQuestion();
	
	public void testGettersQuestion() {
		assertEquals(question.getID(), -1);
		assertEquals(question.getCorrectAnswer(), "Forty-two");
		assertEquals(question.checkAnswer(0), true);
		ArrayList<String> ans = new ArrayList<String>();
		ans.add("Forty-two");
		ans.add("Twenty-seven");
		assertEquals(question.getAnswers(), ans);
		List<String> tags = new ArrayList<String>();
		tags.add("h2g2");
		tags.add("trivia");
		assertEquals(question.getTags(), tags);
		assertEquals(question.getIndex(), 0);
	}

	private QuizQuestion createQuestion() {
		String question = "What is the answer to life, the universe, and everything?";
		ArrayList<String> answers = new ArrayList<String>();
		answers.add("Forty-two");
		answers.add("Twenty-seven");
		int sol = 0;
		List<String> tags = new ArrayList<String>();
		tags.add("h2g2");
		tags.add("trivia");
		return new QuizQuestion(-1, question, answers, sol, tags);
	}
	
	
}
