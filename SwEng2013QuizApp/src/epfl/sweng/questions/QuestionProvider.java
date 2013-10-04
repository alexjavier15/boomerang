package epfl.sweng.questions;

import java.util.List;
import java.util.Set;

/**
 * 
 * @author Noortch
 * 
 */
public interface QuestionProvider {

	long getID();

	String getQuestion();

	String getCorrectAnswer();

	int getIndex();

	List<String> getAnswers();

	boolean checkAnswer(int sol);

	Set<String> getSetOfTags();

}
