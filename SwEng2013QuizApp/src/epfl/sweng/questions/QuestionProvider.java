package epfl.sweng.questions;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author Noortch
 * 
 */
public interface QuestionProvider {

	public int getID();

	public String getQuestion();

	public String getCorrectAnswer();

	public int getIndex();

	public List<String> getAnswers();

	public boolean checkAnswer(int sol);

	public Set<String> getSetOfTags();

}
