package epfl.sweng.editquestions;

import java.util.ArrayList;
import epfl.sweng.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class EditQuestionActivity extends Activity {
	private AnswerAdapter adapter;
	private ArrayList<Answer> fetch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);

		final ListView listView = (ListView) findViewById(R.id.listview);
		Answer firstAnswer = new Answer("✘", null, "-");

		fetch = new ArrayList<Answer>();
		fetch.add(firstAnswer);
		adapter = new AnswerAdapter(this, R.id.listview, fetch);
		listView.setAdapter(adapter);

		findViewById(R.id.newAnswer).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Answer temp = new Answer("✘", null, "-");
				fetch.add(temp);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_question, menu);
		return true;
	}

	/**
	 * Called when the user clicks on the button labeled "Submit."
	 * 
	 * @param view
	 */
	public void initialState(View view) {
		Toast.makeText(this, "You submitted successfully a question!",
				Toast.LENGTH_SHORT).show();
	}
}
