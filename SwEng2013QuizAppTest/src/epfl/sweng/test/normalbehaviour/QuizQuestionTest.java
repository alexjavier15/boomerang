package epfl.sweng.test.normalbehaviour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import epfl.sweng.quizquestions.QuizQuestion;

public class QuizQuestionTest extends TestCase {

    private QuizQuestion mQuestion = createQuestion();

    public QuizQuestion createQuestion() {
        String question = "What is the answer to life, the universe, and everything?";
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Forty-two");
        answers.add("Twenty-seven");
        int sol = 0;
        Set<String> tags = new HashSet<String>(Arrays.asList("h2g2", "trivia"));
        return new QuizQuestion(question, answers, sol, tags, -1, "");
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
    }

}
