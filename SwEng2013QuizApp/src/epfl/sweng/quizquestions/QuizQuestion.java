package epfl.sweng.quizquestions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.tools.JSONParser;

/**
 * 
 * @author Noortch
 * 
 *         This class represents a question.
 */
public class QuizQuestion implements QuestionProvider, Serializable {
    public static final int ID = -1;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> answers = new ArrayList<String>();
    private int id;
    private final int maxAnswerLength = 500;
    private final int maxAnswersSize = 10;
    private final int maxQuestionLength = 500;
    private final int maxTagLength = 20;

    private final int maxTagsSize = 20;
    private String owner = "";
    private String question = "";
    private int solutionIndex = -1;
    private Set<String> tags = new HashSet<String>();

    public QuizQuestion(final String jsonInput) throws JSONException {

        if (jsonInput == null) {

            throw new JSONException("the input string can't be null");

        } else {
            JSONObject parser = new JSONObject(jsonInput);

            this.question = parser.getString("question");
            this.answers = JSONParser.jsonArrayToList(parser.getJSONArray("answers"));
            this.solutionIndex = parser.getInt("solutionIndex");
            this.tags = new HashSet<String>(JSONParser.jsonArrayToList(parser.getJSONArray("tags")));
            this.id = parser.getInt("id");
            this.owner = parser.getString("owner");
        }
    }

    /**
     * Constructor of a QuizQuestion : class to modelize a quiz question at the json format.
     * 
     * @param iD
     *            of the question
     * @param quest
     *            String
     * @param ans
     *            List of String
     * @param solIndex
     *            int
     * @param tags
     *            themas of the question
     */
    public QuizQuestion(final String question, final List<String> answers, final int solutionIndex,
            final Set<String> tags, final int id, final String owner) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.solutionIndex = solutionIndex;
        this.tags = tags;
        this.owner = owner;
        // if (auditErrors() != 0) {
        // throw new IllegalArgumentException();
        // }
    }

    public int auditErrors() {
        int numberOfErrors = 0;
        if (question.length() <= 0 || question.length() > maxQuestionLength || question.trim().length() <= 0) {
            numberOfErrors++;
        }
        for (String a : answers) {
            if (a.length() <= 0 || a.length() > maxAnswerLength || a.trim().length() <= 0) {
                numberOfErrors++;
            }
        }
        if (answers.size() < 2 || answers.size() > maxAnswersSize) {
            numberOfErrors++;
        }
        if (solutionIndex < 0 || solutionIndex > answers.size() - 1) {
            numberOfErrors++;
        }
        if (tags.size() < 1 || tags.size() > maxTagsSize) {
            numberOfErrors++;
        }
        for (String t : tags) {
            if (t.length() <= 0 || t.length() > maxTagLength || t.trim().length() <= 0) {
                numberOfErrors++;
            }
        }
        return numberOfErrors;
    }

    @Override
    public boolean checkAnswer(int sol) {
        return sol == solutionIndex;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Override
    public String getCorrectAnswer() {
        return answers.get(solutionIndex);
    }

    public int getID() {
        return id;
    }

    public int getIndex() {
        return solutionIndex;
    }

    /**
     * Return the the owner
     * 
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    public String getQuestion() {
        return question;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setAnswers(List<String> a) {
        this.answers = a;
    }

    // For JUnit test cases
    public void setQuestion(String q) {
        this.question = q;
    }

    public void setTags(Set<String> tagsIn) {
        this.tags = tagsIn;
    }

}
