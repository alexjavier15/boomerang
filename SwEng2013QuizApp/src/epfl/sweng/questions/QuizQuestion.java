package epfl.sweng.questions;

/**
 * 
 * @author Noortch
 *
 */
public interface QuizQuestion {

	public String getQuestion();
	public String getAnswer();
	public boolean checkAnswer(int sol);
	
}
