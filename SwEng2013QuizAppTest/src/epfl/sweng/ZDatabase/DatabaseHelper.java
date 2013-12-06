package epfl.sweng.ZDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.json.JSONException;

import android.util.Log;

import epfl.sweng.cache.QuizQuestionDBHelper;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.tools.JSONParser;


public class DatabaseHelper extends TestCase {
	
	private QuizQuestionDBHelper database;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		database = new QuizQuestionDBHelper(QuizApp.getContexStatic(), "testDB");
	}
	
	public void testMethodsInDBHelper() {
		assertTrue("Good name", database.getDatabaseName().equals("testDB"));
		
		QuizQuestion question = createQuestion();
		String jsonQuestion = "";
		try {
			jsonQuestion = JSONParser.parseQuiztoJSON(question).toString();
		} catch (JSONException e) {
			Log.e(getClass().getName(), e.getMessage(), e);
		}
		
		database.addQuizQuestion(jsonQuestion);
		
		String[] questionTab = database.getFirstPostQuestion();
		assertTrue("Same question", questionTab[1].equals(jsonQuestion));
		
		String random = database.getRandomQuizQuestion();
		
		assertTrue("Same question", random.equals(jsonQuestion));
		
		
		List<Long> list = database.getQueriedQuestions("h2g2");
		assertFalse(list.size() == 0);
		
		list = database.getQueriedQuestions("blabla");
		assertTrue(list.size() == 0);
		
		database.deleteQuizQuestion("0");
	}
	
	private QuizQuestion createQuestion() {
        String question = "What is the answer to life, the universe, and everything?";
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Forty-two");
        answers.add("Twenty-seven");
        int sol = 0;
        Set<String> tags = new HashSet<String>(Arrays.asList("h2g2", "trivia"));
        return new QuizQuestion(question, answers, sol, tags, -1, "owner");
    }
	
}
