package epfl.sweng.questions;

/**
 * 
 * @author Noortch
 *
 */
public interface QuestionProvider {

	public String getQuestion();
	public String getAnswer();
	public boolean checkAnswer(int sol);
	
}
