package epfl.sweng.test.audits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import epfl.sweng.quizquestions.QuizQuestion;
import junit.framework.TestCase;

public class QuizQuestionAuditTest extends TestCase {
	
	private QuizQuestion question;
	
	public QuizQuestionAuditTest() {
		question = createQuestion();
	}
	
	public void testAuditErors() {
		assertTrue(question.auditErrors() == 0);
		question.setQuestion("  ");
		assertTrue(question.auditErrors() == 1);
		question.setAnswers(Arrays.asList("  ", "JeSuisJuste"));
		assertTrue(question.auditErrors() == 2);
	}
	
	private QuizQuestion createQuestion() {
        String questioni = "What is the answer to life, the universe, and everything?";
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Forty-two");
        answers.add("Twenty-seven");
        int sol = 0;
        Set<String> tags = new HashSet<String>(Arrays.asList("h2g2", "trivia"));
        return new QuizQuestion(questioni, answers, sol, tags, -1, "");
    }
	
}
