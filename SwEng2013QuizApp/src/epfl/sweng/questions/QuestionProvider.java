package epfl.sweng.questions;

import java.util.List;

/**
 * 
 * @author Noortch
 *
 */
public interface QuestionProvider {

	
	public String getQuestion();
	public String getAnswer();
	public List<String> getAnswerList();
	public boolean checkAnswer(int sol);
	
}
