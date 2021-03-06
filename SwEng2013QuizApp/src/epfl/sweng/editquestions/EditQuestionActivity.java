package epfl.sweng.editquestions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.authentication.CredentialManager;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsBackgroundTask;
import epfl.sweng.servercomm.HttpCommsProxy;
import epfl.sweng.servercomm.HttpcommunicationsAdapter;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.JSONParser;

/**
 * 
 * This activity enables the user to submit new quiz questions.
 * 
 * @author CanGuzelhan, JavierRivas, LorenzoLeon
 * 
 */

public class EditQuestionActivity extends Activity implements HttpcommunicationsAdapter {

    public static final String CHECK_SYM = null;
    public static final String ERROR_MESSAGE = "Could not upload the question to the server";
    private Button addAnswerButton;
    private final String answersHint = "Type in the answer";
    private AnswerAdapter mAdapter;
    private ListView mListView;

    private Pattern mPatternTags = Pattern.compile("([A-Za-z0-9]+)");
    private boolean mReset = true;
    private EditText questionEditText;
    private final String questionHint = "Type in the question's text body";
    private Button submitButton;
    private EditText tagsEditText;
    private final String tagsHint = "Type in the question's tags";

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
        questionEditText = (EditText) findViewById(R.id.edit_questionText);
        questionEditText.addTextChangedListener(watcher);
    
        tagsEditText = (EditText) findViewById(R.id.edit_tagsText);
        tagsEditText.addTextChangedListener(watcher);
        submitButton = (Button) findViewById(R.id.submit_button);
        addAnswerButton = (Button) findViewById(R.id.add_answer);
        mReset = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_question, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        TestCoordinator.check(TTChecks.EDIT_QUESTIONS_SHOWN);
    }

    /**
     * Whenever the button with the plus sign (+) is clicked, it adds a new possible answer with the hint
     * "Type in the answer" and it is marked as incorrect.
     * 
     * @param view
     */
    public void addNewSlot(View view) {
        Answer temp = new Answer(getResources().getString(R.string.heavy_ballot_x), "");
        mAdapter.add(temp);
        Log.v(getClass().getName(), temp.toString());
        mAdapter.notifyDataSetChanged();
        if (!isReset()) {
            TestCoordinator.check(TTChecks.QUESTION_EDITED);
        }
        mListView.setSelection(mListView.getCount() - 1);
    }

    /**
     * When the user clicks on the button labeled "Submit.", this method first verifies that the quiz question is valid.
     * If the quiz question is valid, then it saves each answer typed in the answer slots and posts it to the SwEng quiz
     * server. After the question is submitted, EditQuestionActivity is brought to the state identical to that when the
     * user freshly started it.
     * 
     * @param view
     *            The view that was clicked.
     */
    public void submitQuestion(View view) {
        new HttpCommsBackgroundTask(this).execute();
    }

    @Override
    public HttpResponse requete() {
    
        QuizQuestion question = createQuestion();
        JSONObject json = convertQuizQtoJson(question);
    
        return HttpCommsProxy.getInstance().postJSONObject(HttpComms.URL_SWENG_PUSH, json);
    
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

    /**
     * Return the the reset status of {@link EditQuestionActivity}
     * 
     * @return the reset
     */
    public boolean isReset() {
        return mReset;
    }

    /**
     * After a successful submission of a quiz question, EditQuestionActivitys UI is reinitialized.
     */
    public void reset() {
        mReset = true;
        questionEditText.setText("");
        tagsEditText.setText("");
        submitButton.setEnabled(false);
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
     * Called if any text on the components of the {@link EditQuestionActivity} has changed; {@link String}
     * 
     */
    public void updateTextchanged() {
        Log.v(this.getClass().getName(), "Fired filled update");
        if (!mReset) {
            if (isValid()) {
                submitButton.setEnabled(true);
            } else {
                submitButton.setEnabled(false);
            }
            TestCoordinator.check(TTChecks.QUESTION_EDITED);
        }
    }

    public int auditErrors() {
        return auditAnswers() + auditButtons() + auditEditTexts() + auditSubmitButton();
    }

    /**
     * When the user clicks on the submission button, this method is triggered to verify all the four requirements
     * defining a valid quiz question : 1) The question body must be a no empty {@link String} or only white spaces
     * spaces. 2) None of the answers of a quiz question may be empty or contain only white spaces. 3) There must be at
     * least 2 answers. 4) One of the answers must be marked as correct.
     * 
     * @return True if all requirements defining a valid quiz question are verified, otherwise false.
     */
    public boolean isValid() {
        String questionText = questionEditText.getText().toString();
        String tagsText = tagsEditText.getText().toString();
    
        return mPatternTags.matcher(tagsText).find() && !questionText.trim().equals("") && mAdapter.getCount() >= 2
                && !mAdapter.hasEmptyAnswer() && mAdapter.hasOneCorrectAnswer();
    }

    /**
     * This method is called when the quiz question is valid and all answers the user typed in are saved in order to
     * create a quiz question in JSON format for the SwEng quiz server.
     * 
     * @return The quiz question is JSON format.
     */
    private QuizQuestion createQuestion() throws IllegalArgumentException {
        String questionString = questionEditText.getText().toString();
        List<String> answers = new LinkedList<String>();
        int solIndex = 0;
        boolean check = true;
        QuizQuestion questionCreated = null;
    
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
    
        Pattern patternTags = Pattern.compile("([A-Za-z0-9]+)");
        Set<String> tags = new HashSet<String>();
    
        Matcher matcher = patternTags.matcher(tagsEditText.getText().toString());
        while (matcher.find()) {
            tags.add(matcher.group(1));
        }
        questionCreated = new QuizQuestion(questionString, answers, solIndex, tags, QuizQuestion.ID,
                CredentialManager.getInstance().getUserCredential());
    
        return questionCreated;
    }

    private JSONObject convertQuizQtoJson(QuizQuestion question) {
    
        JSONObject json = null;
    
        try {
            json = JSONParser.parseQuiztoJSON(question);
        } catch (JSONException e) {
            Log.e(getClass().getName(), e.getMessage(), e);
        }
        return json;
    }

    /**
     * Used to show a {@link Toast} in case of a successful submission
     */
    private void printSuccess() {
        Toast.makeText(this, "Your submission was successful!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Used to show a {@link Toast} in case of a failed submission
     * 
     */
    private void printFail() {
        Toast.makeText(this, ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    private int auditAnswers() {
        int numberErrors = 0;
        int numberOfAnswers = 0;

        Log.v(this.getClass().getName(), "" + mListView.getChildCount());
        Log.v(this.getClass().getName(), "" + mListView.getCount());

        for (int i = 0; i < mListView.getChildCount(); i++) {
            Button check = (Button) mListView.getChildAt(i).findViewById(R.id.edit_buttonProperty);
            String mark = getResources().getString(R.string.heavy_check_mark);
            Log.v(this.getClass().getName(), check.getText() + " vs " + mark);

            if (check.getText().equals(getResources().getString(R.string.heavy_check_mark))) {
                numberOfAnswers++;
            }
        }

        if (numberOfAnswers > 1) {
            numberErrors++;
        }
        return numberErrors;
    }

    private int auditButtons() {
        int numberErrors = 0;
        if (!addAnswerButton.getText().equals("+") || addAnswerButton.getVisibility() != View.VISIBLE) {
            numberErrors++;
        }
        if (!submitButton.getText().equals("Submit") || submitButton.getVisibility() != View.VISIBLE) {
            numberErrors++;
        }
        for (int i = 0; i < mListView.getChildCount(); i++) {
            Button removeAnswer = (Button) mListView.getChildAt(i).findViewById(R.id.edit_cancelAnswer);
            if (!removeAnswer.getText().equals("-") || removeAnswer.getVisibility() != View.VISIBLE) {
                numberErrors++;
            }
            Button check = (Button) mListView.getChildAt(i).findViewById(R.id.edit_buttonProperty);
            CharSequence checkTxt = check.getText();
            String juste = getString(R.string.heavy_check_mark);
            String faux = getString(R.string.heavy_ballot_x);
            if (!(checkTxt.equals(juste) || checkTxt.equals(faux)) || check.getVisibility() != View.VISIBLE) {
                numberErrors++;
            }
        }
        return numberErrors;
    }

    private int auditEditTexts() {
        int numberErrors = 0;
        if (!questionEditText.getHint().equals(questionHint) || questionEditText.getVisibility() != View.VISIBLE) {
            numberErrors++;
        }
        for (int i = 0; i < mListView.getChildCount(); i++) {
            EditText answer = (EditText) mListView.getChildAt(i).findViewById(R.id.edit_answerText);
            if (!answer.getHint().equals(answersHint) || answer.getVisibility() != View.VISIBLE) {
                numberErrors++;
            }
        }
        if (!tagsEditText.getHint().equals(tagsHint) || tagsEditText.getVisibility() != View.VISIBLE) {
            numberErrors++;
        }
        Log.v(this.getClass().getName(), "errors en answers : " + numberErrors);
        return numberErrors;
    }

    private int auditSubmitButton() {
        int numberErrors = 0;
        QuizQuestion currentQQ = createQuestion();
        if (currentQQ.auditErrors() != 0) {
            if (submitButton.isEnabled()) {
                numberErrors++;
            }
        } else {
            if (!submitButton.isEnabled()) {
                numberErrors++;
            }
        }
        return numberErrors;
    }

}
