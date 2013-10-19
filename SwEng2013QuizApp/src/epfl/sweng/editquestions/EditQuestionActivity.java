package epfl.sweng.editquestions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * 
 * This activity enables the user to submit new quiz questions.
 * 
 * @author CanGuzelhan, JavierRivas, LorenzoLeon
 * 
 */

public class EditQuestionActivity extends Activity implements HttpcommunicationsAdapter {

	public static final String ERROR_MESSAGE = "Could not upload the question to the server";
    private AnswerAdapter mAdapter;
    private ListView mListView;
    private Pattern mPatternTags = Pattern.compile("([A-Za-z0-9]+)");
    private boolean mReset = true;

    /**
     * Whenever the button with the plus sign (+) is clicked, it adds a new possible answer with the hint
     * "Type in the answer" and it is marked as incorrect.
     * 
     * @param view
     */
    public void addNewSlot(View view) {
        Answer temp = new Answer(getResources().getString(R.string.heavy_ballot_x), "");
        mAdapter.add(temp);
        Debug.out(temp);
        mAdapter.notifyDataSetChanged();
        if (!isReset()) {
            TestCoordinator.check(TTChecks.QUESTION_EDITED);
        }
        mListView.setSelection(mListView.getCount() - 1);
    }

    /**
     * This method is called when the quiz question is valid and all answers the user typed in are saved in order to
     * create a quiz question in JSON format for the SwEng quiz server.
     * 
     * @return The quiz question is JSON format.
     */
    private QuizQuestion createQuestion() {
        String questionString = ((EditText) findViewById(R.id.edit_questionText)).getText().toString();
        List<String> answers = new LinkedList<String>();
        int solIndex = 0;
        boolean check = true;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            Answer answerI = mAdapter.getItem(i);
            answers.add(answerI.getAnswer());
            if (check) {
                if (answerI.getChecked().equals(getResources().getString(R.string.heavy_check_mark))) {
                    check = false;
                } else {
                    solIndex++;
                }
            }
        }

        EditText tagsEditText = (EditText) findViewById(R.id.edit_tagsText);
        Pattern patternTags = Pattern.compile("([A-Za-z0-9]+)");
        Set<String> tags = new HashSet<String>();

        Matcher matcher = patternTags.matcher(tagsEditText.getText().toString());
        while (matcher.find()) {
            tags.add(matcher.group(1));
        }

        return new QuizQuestion(-1, questionString, answers, solIndex, Arrays.asList(tags.toArray(new String[1])));
    }

    /**
     * Return the the reset status of {@link EditQuestionActivity}
     * 
     * @return the reset
     */
    public boolean isReset() {
        return mReset;
    }

    /**
     * When the user clicks on the submission button, this method is triggered to verify all the four requirements
     * defining a valid quiz question : 1) The question body must be a no empty {@link String} or only white spaces
     * spaces. 2) None of the answers of a quiz question may be empty or contain only white spaces. 3) There must be
     * at
     * least 2 answers. 4) One of the answers must be marked as correct.
     * 
     * @return True if all requirements defining a valid quiz question are verified, otherwise false.
     */
    public boolean isValid() {
        String questionText = ((EditText) findViewById(R.id.edit_questionText)).getText().toString();
        String tagsText = ((EditText) findViewById(R.id.edit_tagsText)).getText().toString();

        return mPatternTags.matcher(tagsText).find() && !questionText.trim().equals("") && mAdapter.getCount() >= 2
            && !mAdapter.hasEmptyAnswer() && mAdapter.hasOneCorrectAnswer();
    }

    /**
     * Starts the window adding a modified ArrayAdapter to list the answers. Creates the multiple Test Listeners for
     * when text has been edited. Shows the view.
     * 
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        // Answer firstAnswer = new Answer(getResources().getString(R.string.heavy_ballot_x), "");
        // fetch.add(firstAnswer);

        TextWatcher watcher = new TextWatcher() {

            /**
             * Validates the whole question after each change to enable submit button.
             */
            @Override
            public void afterTextChanged(Editable s) {

                if (!isReset()) {
                    updateTextchanged();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        };
        mAdapter = new AnswerAdapter(this, R.id.listview, new ArrayList<Answer>());
        mAdapter.setNotifyOnChange(true);

        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        addNewSlot(null);
        EditText questionText = (EditText) findViewById(R.id.edit_questionText);
        questionText.addTextChangedListener(watcher);

        EditText tagsText = (EditText) findViewById(R.id.edit_tagsText);
        tagsText.addTextChangedListener(watcher);
        mReset = false;
        TestCoordinator.check(TTChecks.EDIT_QUESTIONS_SHOWN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_question, menu);
        return true;
    }

    /**
     * Used to show a {@link Toast} in case of a failed submission
     * 
     */
    private void printFail() {
        Toast.makeText(this, ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    /**
     * Used to show a {@link Toast} in case of a successful submission
     */
    private void printSuccess() {
        Toast.makeText(this, "Your submission was successful!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void processHttpReponse(HttpResponse response) {

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
            reset();
            printSuccess();
        } else {
            printFail();
        }
        TestCoordinator.check(TTChecks.NEW_QUESTION_SUBMITTED);
    }

    @Override
    public HttpResponse requete() {
        HttpResponse response = null;
        try {
            response = HttpComms.getInstance(this).postQuestion(HttpComms.URLPUSH,
                JSONParser.parseQuiztoJSON(createQuestion()));
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NetworkErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    /**
     * After a successful submission of a quiz question, EditQuestionActivity‘s UI is reinitialized.
     */
    public void reset() {
        mReset = true;
        ((EditText) findViewById(R.id.edit_questionText)).setText("");
        ((EditText) findViewById(R.id.edit_tagsText)).setText("");
        ((Button) findViewById(R.id.submit_button)).setEnabled(false);
        mAdapter.setDefault();
        addNewSlot(null);
        mReset = false;
    }

    /**
     * Set the reset value. Used for avoid TTchecks during updates in the ListView
     * 
     * @param reset
     *            the reset to set
     */
    public void setReset(boolean reset) {
        mReset = reset;
    }

    /**
     * When the user clicks on the button labeled "Submit.", this method first verifies that the quiz question is
     * valid.
     * If the quiz question is valid, then it saves each answer typed in the answer slots and posts it to the SwEng
     * quiz
     * server. After the question is submitted, EditQuestionActivity is brought to the state identical to that when
     * the
     * user freshly started it.
     * 
     * @param view
     *            The view that was clicked.
     */
    public void submitQuestion(View view) {
        new HttpCommsBackgroundTask(this).execute();

    }

    /**
     * Called if any text on the components of the {@link EditQuestionActivity} has changed; {@link String}
     * 
     */
    public void updateTextchanged() {

        Debug.out("Fired filled update");
        if (!mReset) {
            if (isValid()) {
                ((Button) findViewById(R.id.submit_button)).setEnabled(true);
            } else {

                ((Button) findViewById(R.id.submit_button)).setEnabled(false);
            }
            TestCoordinator.check(TTChecks.QUESTION_EDITED);
        }
    }

}
