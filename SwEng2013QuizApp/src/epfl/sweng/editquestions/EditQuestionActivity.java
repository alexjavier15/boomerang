package epfl.sweng.editquestions;

import epfl.sweng.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class EditQuestionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_question);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_question, menu);
		return true;
	}

	/**
	 * Called when the user clicks on the button labeled "Submit."
	 * @param view
	 */
	public void initialState(View view) {
		Toast.makeText(this, "You submitted successfully a question!",
				Toast.LENGTH_SHORT).show();
	}
}
