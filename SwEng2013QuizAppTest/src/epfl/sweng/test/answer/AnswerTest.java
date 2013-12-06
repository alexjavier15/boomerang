package epfl.sweng.test.answer;

import junit.framework.TestCase;
import epfl.sweng.editquestions.Answer;

public class AnswerTest extends TestCase {

    public void answerTest() {
        Answer answer = new Answer(null, null);
        answer.setAnswer("salut");
        assertEquals(answer.getAnswer(), "salut");
        answer.setChecked("lol");
        assertEquals(answer.getChecked(), "lol");
        answer.setCorrect(true);
        assertTrue(answer.isCorrect());
    }
}
