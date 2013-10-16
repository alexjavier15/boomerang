package epfl.sweng.questions;


/**
 * 
 * @author Noortch
 * 
 */
public interface QuestionProvider {

	String getCorrectAnswer();

	boolean checkAnswer(int sol);

}
