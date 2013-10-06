package epfl.sweng.editquestions;

import java.util.ArrayList;

import epfl.sweng.R;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;
import android.app.Activity;
import android.content.Context;
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
	private Context activity;
	private int isChecked;

	public AnswerAdapter(Context context, int resourceId,
			ArrayList<Answer> entries) {
		super(context, resourceId, entries);
		this.activity = context;
		this.isChecked = 0;
	}

	public int getWhoIsChecked() {
		return isChecked;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final AnswerHolder holder;

		if (view == null) {
			LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
			view = inflater.inflate(R.layout.activity_answer_slot, null);
			holder = new AnswerHolder();
			holder.setCheckButton((Button) view
					.findViewById(R.id.edit_buttonProperty));
			holder.setAnswerText((EditText) view
					.findViewById(R.id.edit_answerText));
			holder.setRemoveButton((Button) view
					.findViewById(R.id.edit_cancelAnswer));

			view.setTag(holder);
		} else {
			holder = (AnswerHolder) view.getTag();
		}
		holder.getCheckButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AnswerAdapter.this.getItem(isChecked).setChecked(
						activity.getResources().getString(
								R.string.heavy_ballot_x));
				AnswerAdapter.this.isChecked = position;
				AnswerAdapter.this.getItem(position).setChecked(
						activity.getResources().getString(
								R.string.heavy_check_mark));
				AnswerAdapter.this.notifyDataSetChanged();
				TestingTransactions.check(TTChecks.QUESTION_EDITED);
			}
		});
		holder.getRemoveButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AnswerAdapter.this.getCount() > 1) {
					AnswerAdapter.this.getItem(position).setAnswer("");
					holder.getAnswerText().setText(
							AnswerAdapter.this.getItem(position).getAnswer());
					AnswerAdapter.this.remove(AnswerAdapter.this
							.getItem(position));
					AnswerAdapter.this.notifyDataSetChanged();
					TestingTransactions.check(TTChecks.QUESTION_EDITED);
				} else {
					Toast.makeText(
							activity,
							"A question without an answer is useless, isn't it?",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		holder.getCheckButton().setText(
				AnswerAdapter.this.getItem(position).getChecked());

		return view;
	}
}
