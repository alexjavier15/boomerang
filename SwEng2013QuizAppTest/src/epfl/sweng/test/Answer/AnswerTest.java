package epfl.sweng.test.Answer;

import junit.framework.TestCase;
import epfl.sweng.editquestions.Answer;

public class AnswerTest extends TestCase {

    private Answer answer = new Answer();

    public void answerTest() {
        answer.setAnswer("salut");
        assertEquals(answer.getAnswer(), "salut");
        answer.setChecked("lol");
        assertEquals(answer.getChecked(), "lol");
        answer.setCorrect(true);
        assertTrue(answer.isCorrect());
    }
}
