package epfl.sweng.quizquestions;

import java.io.IOException;
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
public class QuizQuestion implements QuestionProvider {
    public static final int ID = -1;
    private List<String> answers = new ArrayList<String>();
    private long id;
    private String question;
    private int solutionIndex;
    private Set<String> tags = new HashSet<String>();
    private String owner;

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
    public QuizQuestion(final String question, final List<String> answers, final int
        solutionIndex, final Set<String> tags, final int id, final String owner) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.solutionIndex = solutionIndex;
        this.tags = tags;
        this.owner=owner;
    }
    
    public QuizQuestion(final String jsonInput) throws JSONException  {
        JSONObject parser = new JSONObject(jsonInput);
        int id = -1;
        String question="";
        List<String> answers = null;
        int solutionIndex = -1;
        Set<String> tags = null;
        String owner = "";
        try {
            id = parser.getInt("id");
            question = parser.getString("question");
            answers = JSONParser.jsonArrayToList(parser.getJSONArray("answers"));
            solutionIndex = parser.getInt("solutionIndex");
            tags = new HashSet<String>(JSONParser.jsonArrayToList(parser.getJSONArray("tags")));
            owner = parser.getString("owner");
            
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.question=question;
        this.answers=answers;
        this.solutionIndex=solutionIndex;
        this.tags=tags;
        this.id=id;
        this.owner= owner;

    
        
        
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

    public long getID() {
        return id;
    }

    public int getIndex() {
        return solutionIndex;
    }

    public String getQuestion() {
        return question;
    }

    public Set<String> getTags() {
        return tags;
    }

    /**Return the the owner
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }


}
