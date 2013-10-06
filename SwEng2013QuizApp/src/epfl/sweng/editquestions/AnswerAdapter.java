package epfl.sweng.editquestions;

import java.util.ArrayList;

import epfl.sweng.R;
import epfl.sweng.testing.TestingTransactions;
import epfl.sweng.testing.TestingTransactions.TTChecks;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
		View newView = null;
		if (view == null) {
			LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
			newView = inflater.inflate(R.layout.activity_answer_slot, null);
			holder = new AnswerHolder();
			holder.setCheckButton((Button) newView
					.findViewById(R.id.edit_buttonProperty));
			holder.setAnswerText((EditText) newView
					.findViewById(R.id.edit_answerText));
			holder.setRemoveButton((Button) newView
					.findViewById(R.id.edit_cancelAnswer));
			holder.getAnswerText().setTag(this.getItem(position));
			holder.getCheckButton().setTag(this.getItem(position));

			newView.setTag(holder);

			holder.getAnswerText().addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					((Answer) holder.getAnswerText().getTag()).setAnswer(s
							.toString());
				
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
				}
			});

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

		} else {
			holder = (AnswerHolder) view.getTag();
			newView = view;
			((AnswerHolder) view.getTag()).getAnswerText().setTag(
					this.getItem(position));
			((AnswerHolder) view.getTag()).getCheckButton().setTag(
					this.getItem(position));
		}

		holder.getAnswerText().setText(
				AnswerAdapter.this.getItem(position).getAnswer());
		holder.getCheckButton().setText(
				AnswerAdapter.this.getItem(position).getChecked());

		return newView;
	}
}
