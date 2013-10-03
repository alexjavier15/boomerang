package epfl.sweng.editquestions;

import java.util.ArrayList;
import epfl.sweng.R;
import epfl.sweng.testing.Debug;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class AnswerAdapter extends ArrayAdapter<Answer> {
	private Activity context;
	private View resource;

	public ArrayList<Answer> getEntries() {
		return entries;

	}

	private ArrayList<Answer> entries;

	public class ViewHolder {
		public Button checkButton;
		public EditText answerText;
		public Button removeButton;

		public Button getCheckButton() {
			return checkButton;
		}

		public EditText getAnswerText() {
			return answerText;
		}

		public Button getRemoveButton() {
			return removeButton;
		}

	}

	public AnswerAdapter(Activity context, int resourceId,
			ArrayList<Answer> entries) {
		super(context, resourceId, entries);
		this.context = context;
		this.entries = entries;
		resource = context.findViewById(resourceId);
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		final ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.activity_new_answer, null);
			holder = new ViewHolder();
			holder.checkButton = (Button) view
					.findViewById(R.id.edit_buttonProperty);
			holder.answerText = (EditText) view
					.findViewById(R.id.edit_answerText);
			holder.removeButton = (Button) view
					.findViewById(R.id.edit_cancelAnswer);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.checkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (Answer answer : entries) {
					answer.setChecked(context.getResources().getString(
							R.string.heavy_ballot_x));
				}
				entries.get(position).setChecked(
						context.getResources().getString(
								R.string.heavy_check_mark));
				
				AnswerAdapter.this.notifyDataSetChanged();
			}
		});

		holder.removeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (entries.size() > 1) {
					entries.remove(position);
					AnswerAdapter.this.notifyDataSetChanged();
				} else {
					Toast.makeText(
							context,
							"A question without an answer is useless, isn't it?",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		Answer answer = entries.get(position);
		if (answer != null) {

			holder.checkButton.setText(answer.getChecked());
			holder.answerText.setText(answer.getAnswer());
			holder.removeButton.setText(answer.getRemoved());

		}
		return view;
	}

	
	
	
	@Override	
	public void notifyDataSetChanged() {
	
		ListView listView = (ListView)this.resource;
		
		int size = listView.getChildCount();
		for (int i = 0; i < size; i++) {
			View view = listView.getChildAt(i);
			ViewHolder holder = (ViewHolder) view.getTag();
			Answer currentAnswer = entries.get(i);
			currentAnswer
					.setAnswer(holder.getAnswerText().getText().toString());
			
			Debug.out(holder.getAnswerText() + "UPDATING..");
				
			super.notifyDataSetChanged();
			
			

		}

		super.notifyDataSetChanged();
	}
}
