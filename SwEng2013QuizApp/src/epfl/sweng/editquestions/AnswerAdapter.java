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

public class AnswerAdapter extends ArrayAdapter<Answer> {
	private Activity context;
	private ArrayList<Answer> entries;

	public static class ViewHolder {
		public Button checkButton;
		public EditText answerText;
		public Button removeButton;
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
		ViewHolder holder;
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
					answer.setChecked("✘");
				}
				entries.get(position).setChecked("✔");
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
}
