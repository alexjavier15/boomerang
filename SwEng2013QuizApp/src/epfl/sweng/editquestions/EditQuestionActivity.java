package epfl.sweng.editquestions;

import java.util.ArrayList;

import epfl.sweng.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class EditQuestionActivity extends Activity {

	private ArrayList<Button> buttons = new ArrayList<Button>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);
		this.newAnswer(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_question, menu);
		return true;
	}

	public void newAnswer(View view) {
		LayoutInflater inflater = (LayoutInflater) this.getLayoutInflater();
		View newAnswerBlock = inflater.inflate(R.layout.activity_new_answer,
				null);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.answersBlock);
		linearLayout.addView(newAnswerBlock);

		buttons.add((Button) newAnswerBlock.findViewById(R.id.answersBlock));
	}

	public void checkAnswer(View view) {
		Button button = (Button) findViewById(R.id.edit_buttonProperty);
		button.setText(R.string.heavy_check_mark);
	}

	public void removeAnswer(View view) {

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
