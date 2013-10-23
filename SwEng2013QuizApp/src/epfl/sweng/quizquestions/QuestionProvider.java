package epfl.sweng.quizquestions;

/**
 * 
 * @author Noortch
 * 
 */
public interface QuestionProvider {

    boolean checkAnswer(int sol);

    String getCorrectAnswer();

}
