package epfl.sweng.editquestions;

import java.util.ArrayList;
import epfl.sweng.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class AnswerAdapter extends ArrayAdapter<Answer> {
	private Activity context;
	private ArrayList<Answer> entries;

	public static class ViewHolder {
		private Button checkButton;
		private EditText answerText;
		private Button removeButton;
		
		public Button getCheckButton() {
			return checkButton;
		}
		
		public void setCheckButton(Button button) {
			checkButton = button;
		}
		
		public EditText getAnswerText() {
			return answerText;
		}
		
		public void setAnswerText(EditText text) {
			answerText = text;
		}
		
		public Button getRemoveButton() {
			return removeButton;
		}
		
		public void setRemoveButton(Button button) {
			removeButton = button;
		}
	}

	public AnswerAdapter(Activity context, int resourceId,
			ArrayList<Answer> entries) {
		super(context, resourceId, entries);
		this.context = context;
		this.entries = entries;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.activity_new_answer, null);
			holder = new ViewHolder();
			holder.setCheckButton((Button) view
					.findViewById(R.id.edit_buttonProperty));
			holder.setAnswerText((EditText) view
					.findViewById(R.id.edit_answerText));
			holder.setRemoveButton((Button) view
					.findViewById(R.id.edit_cancelAnswer));

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.getCheckButton().setOnClickListener(new OnClickListener() {

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

		holder.getRemoveButton().setOnClickListener(new OnClickListener() {

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
			holder.getCheckButton().setText(answer.getChecked());
			holder.getAnswerText().setText(holder.answerText.getText());
			holder.getRemoveButton().setText(answer.getRemoved());
		}
		return view;
	}
}
