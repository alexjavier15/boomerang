package epfl.sweng.showquestions;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.CacheQueryProxy;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpCommsProxy;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.JSONParser;

/**
 * 
 * @author AlbanMarguet & LorenzoLeon
 * 
 */

public class ShowQuestionsActivity extends Activity implements HttpcommunicationsAdapter {

    public static final String ERROR_QUERY = "No questions match your query";
    public static final String EMPTY_TAGS_MSG = "No tags for this question";
    public static final String ERROR_MESSAGE = "There was an error retrieving the question";

    private ArrayAdapter<String> adapter;
    private ListView answerChoices;
    private OnItemClickListener answerListener = null;
    private QuizQuestion currrentQuestion;

    private TextView tags;
    private TextView text;

    private int lastChoice = -1;
    private String url = null;
    private boolean isQuery = false;

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     * 
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_questions, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);

        ((Button) findViewById(R.id.next_question)).setEnabled(false);
        answerChoices = (ListView) findViewById(R.id.answer_choices);

        tags = (TextView) findViewById(R.id.show_tags);

        text = (TextView) findViewById(R.id.show_question);

        answerListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listAdapter, View view, int selectedAnswer, long arg3) {

                ListView list = (ListView) listAdapter;
                TextView textListener = (TextView) list.getChildAt(selectedAnswer);

                if (lastChoice != -1) {
                    TextView lastChild = (TextView) list.getChildAt(lastChoice);
                    if (lastChild != null) {
                        String lastAnswer = lastChild.getText().toString();
                        if (lastAnswer.contains("\u2718")) {
                            lastAnswer = lastAnswer.substring(0, lastAnswer.length() - 1);
                        }
                        lastChild.setText(lastAnswer);
                    }
                }

                String question = getResources().getString(R.string.heavy_ballot_x);

                if (currrentQuestion.checkAnswer(selectedAnswer)) {
                    question = getResources().getString(R.string.heavy_check_mark);
                    ((Button) findViewById(R.id.next_question)).setEnabled(true);
                    list.setOnItemClickListener(null);
                }

                TestCoordinator.check(TTChecks.ANSWER_SELECTED);

                String newText = textListener.getText().toString() + question;
                textListener.setText(newText);
                lastChoice = selectedAnswer;

            }

        };
        answerChoices.setOnItemClickListener(answerListener);

        if (getIntent().hasExtra("query_mode")) {
            isQuery = true;
            url = HttpComms.URL_SWENG_QUERY_POST;
        } else {
            url = HttpComms.URL_SWENG_RANDOM_GET;
        }

        try {

            processHttpReponse(new HttpCommsBackgroundTask(this, false).execute().get());

        } catch (InterruptedException e) {
            toast(ERROR_MESSAGE);
            Log.e(this.getClass().getName(), e.getMessage(), e);
        } catch (ExecutionException e) {
            toast(ERROR_MESSAGE);
            Log.e(this.getClass().getName(), e.getMessage(), e);
        }
    }

    /**
     * Launches a new async task which will fetch a new question when clicking on the button labeled "Next Question"
     * 
     * @param view
     */
    public void askNextQuestion(View view) {

        ((Button) findViewById(R.id.next_question)).setEnabled(false);
        new HttpCommsBackgroundTask(this, true).execute();
    }

    @Override
    public HttpResponse requete() {

        return CacheQueryProxy.getInstance().getHttpResponse(url);

    }

    @Override
    public void processHttpReponse(HttpResponse httpResponse) {
        QuizQuestion quizQuestion = null;
        JSONObject jsonObj = null;
        String jsonString = null;
        try {
            jsonObj = JSONParser.getParser(httpResponse);
            if (jsonObj != null) {
                jsonString = jsonObj.toString();
            }
            quizQuestion = new QuizQuestion(jsonString);
        } catch (JSONException e) {
            HttpCommsProxy.getInstance().setOnlineMode(false);
            toast(ERROR_MESSAGE);
            Log.e(getClass().getName(), e.getMessage(), e);
        }
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            setQuestion(quizQuestion);
        }

        TestCoordinator.check(TTChecks.QUESTION_SHOWN);

    }

    /**
     * Get the tags of the question to display them on the screen
     * 
     * @param set
     *            : set of Strings
     * @return the tags
     */
    private String displayTags(Set<String> set) {
        if (set.size() > 0) {
            String tagsInString = "";
            int counter = 0;
            for (String s : set) {
                counter++;
                if (counter == set.size()) {
                    tagsInString += s;
                } else {
                    tagsInString += s + ", ";
                }
            }
            return tagsInString;
        } else {
            return EMPTY_TAGS_MSG;
        }
    }

    private void setQuestion(QuizQuestion quizQuestion) {
        try {
            currrentQuestion = quizQuestion;

            text.setText(quizQuestion.getQuestion());
            tags.setText(displayTags(quizQuestion.getTags()));
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quizQuestion.getAnswers());

            answerChoices.setAdapter(adapter);

            adapter.setNotifyOnChange(true);
            answerChoices.setOnItemClickListener(answerListener);
        } catch (NullPointerException e) {
            text.append("No question can be obtained !");
            toast(getErrorMessage());
            Log.e(getClass().getName(), e.getMessage(), e);
        }
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public String getErrorMessage() {
        if (isQuery) {
            return ERROR_QUERY;
        } else {
            return ERROR_MESSAGE;
        }
    }

}
