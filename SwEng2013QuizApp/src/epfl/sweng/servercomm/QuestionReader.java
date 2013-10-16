package epfl.sweng.servercomm;

import epfl.sweng.questions.QuizQuestion;

/**
 * Creates a pattern to enable the AsyncTask to transfer the question from the
 * background task to the Activity that implements this task.
 * 
 * @author LorenzoLeon
 * 
 */
public interface QuestionReader {

	void readQuestion(QuizQuestion question);

}
