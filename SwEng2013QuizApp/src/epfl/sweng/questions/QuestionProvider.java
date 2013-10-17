package epfl.sweng.questions;

/**
 * 
 * @author Noortch
 * 
 */
public interface QuestionProvider {

    boolean checkAnswer(int sol);

    String getCorrectAnswer();

}
