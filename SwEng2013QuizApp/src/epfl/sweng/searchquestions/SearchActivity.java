package epfl.sweng.searchquestions;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import epfl.sweng.R;
import epfl.sweng.showquestions.ShowQuestionsActivity;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/**
 * This activity allows the user to enter in the text field a search query (with corresponding guidelines) that will be
 * forwarded to the sweng question server
 * 
 * @author LorenzoLeon
 * 
 */
public class SearchActivity extends Activity {

    private EditText searchQuery = null;
    private Button searchButton = null;
    private final int maxLengthOfQuery = 500;
    private TextWatcher watcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        watcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isQueryValid()) {
                    searchButton.setEnabled(true);
                } else {
                    searchButton.setEnabled(false);
                }
                TestCoordinator.check(TTChecks.QUERY_EDITED);
            }
        };

        searchQuery = (EditText) findViewById(R.id.edit_search_query);

        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is
        // present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    protected void onStart() {
        super.onStart();
        searchQuery.addTextChangedListener(watcher);
        TestCoordinator.check(TTChecks.SEARCH_ACTIVITY_SHOWN);
    }

    public void search(View view) {
        Toast.makeText(this, "You are on the page to enter a specific query for a random question",
                Toast.LENGTH_SHORT).show();
        Intent showQuestionActivityIntent = new Intent(this, ShowQuestionsActivity.class);
        showQuestionActivityIntent.putExtra("query_mode", true);
        String queryText = searchQuery.getText().toString();
        showQuestionActivityIntent.putExtra("query", queryText);
        this.startActivity(showQuestionActivityIntent);
    }

    private boolean isQueryValid() {
        String text = searchQuery.getText().toString();
        // Only alphanumeric or " ", "(", ")", "*", "+"
        Pattern pattern1 = Pattern.compile("[A-Za-z0-9 \\(\\)\\*\\+]+");
        Matcher matcher1 = pattern1.matcher(text);
        // At least one alphanumeric character
        Pattern pattern2 = Pattern.compile("[A-Za-z0-9]+");
        Matcher matcher2 = pattern2.matcher(text);
        return text.length() <= maxLengthOfQuery && matcher1.matches() && matcher2.find() && nestedText(text);
    }

    @SuppressWarnings("finally")
    private boolean nestedText(String text) {
        Stack<Character> stack = new Stack<Character>();
        try {
            for (char c : text.toCharArray()) {
                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    stack.pop();
                }
            }
        } catch (EmptyStackException e) {
            e.printStackTrace();
        } finally {
            return stack.isEmpty();
        }
    }
}
