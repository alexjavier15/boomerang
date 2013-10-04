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
import android.widget.Toast;

/**
 * 
 * @author CanGuzelhan
 * 
 */
public class AnswerAdapter extends ArrayAdapter<Answer> {
	private Activity context;
	private int lastChecked = -1;

	public static class ViewHolder {
		private Button checkButton;
		private EditText answerText;
		private Button removeButton;

	}

	public AnswerAdapter(Activity context, int resourceId,
			ArrayList<Answer> entries) {
		super(context, resourceId, entries);
		this.context = context;
		
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;

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
			Debug.out("pass pour" + position);
			holder = (ViewHolder) view.getTag();
		}

		holder.checkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AnswerAdapter.this.lastChecked != -1) {
					AnswerAdapter.this.getItem(lastChecked).setChecked(
							context.getResources().getString(
									R.string.heavy_ballot_x));
				}

				Debug.out(position);

				AnswerAdapter.this.getItem(position).setChecked(
						context.getResources().getString(
								R.string.heavy_check_mark));
				lastChecked = position;
				Debug.out(AnswerAdapter.this.getItem(position).getChecked()
						+ " answer : "
						+ AnswerAdapter.this.getItem(position).getAnswer());
				AnswerAdapter.this.notifyDataSetChanged();
			}
		});

		holder.removeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Debug.out(" pre " + AnswerAdapter.this.getCount());
				if (AnswerAdapter.this.getCount() > 1) {

					holder.answerText.setText("");

					AnswerAdapter.this.remove(AnswerAdapter.this
							.getItem(position));

					Debug.out("après " + AnswerAdapter.this.getCount());
					AnswerAdapter.this.notifyDataSetChanged();
				} else {
					Toast.makeText(
							context,
							"A question without an answer is useless, isn't it?",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		holder.checkButton.setText(this.getItem(position).getChecked());

		return view;
	}
}
