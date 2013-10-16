package epfl.sweng.editquestions;

import java.util.ArrayList;

import epfl.sweng.R;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import android.app.Activity;
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
	private Activity activity;
	private int isChecked;

	private static boolean oneCorrectAnswer = false;
	private static boolean noEmptyAnswer = false;

	public AnswerAdapter(Activity context, int resourceId,
			ArrayList<Answer> entries) {
		super(context, resourceId, entries);
		this.activity = context;
		this.isChecked = -1;
	}

	public static boolean isOneCorrectAnswer() {
		return oneCorrectAnswer;
	}

	public static boolean isNoEmptyAnswer() {
		return noEmptyAnswer;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final AnswerHolder holder;
		View newView = null;
		if (view == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
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
					if (((EditQuestionActivity) activity).isReset()) {
						((Answer) holder.getAnswerText().getTag()).setAnswer(s
								.toString());
						// We need to check if an answer is written in each
						// slot and in that case we set noEmptyAnswer to true.
						for (int i = 0; i < getCount(); i++) {
							if (getItem(i).getAnswer().trim().equals("")) {
								noEmptyAnswer = false;
								break;
							} else if (i == getCount()) {
								noEmptyAnswer = true;
							}
						}
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					/*
					 * We need to check if the quiz question is valid and if so
					 * we must enable the submit button. The problem here is
					 * currently there is no way to access the submit button
					 * from here.
					 */
				}
			});

			holder.getCheckButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isChecked >= 0) {
						getItem(isChecked).setChecked(
								activity.getResources().getString(
										R.string.heavy_ballot_x));
					}
					isChecked = position;
					getItem(position).setChecked(
							activity.getResources().getString(
									R.string.heavy_check_mark));
					oneCorrectAnswer = true;
					notifyDataSetChanged();
					TestCoordinator.check(TTChecks.QUESTION_EDITED);
				}
			});

			holder.getRemoveButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (AnswerAdapter.this.getCount() > 1) {
						if (isChecked == position) {
							isChecked = -1;
							oneCorrectAnswer = false;
						}
						AnswerAdapter.this.remove(AnswerAdapter.this
								.getItem(position));
						AnswerAdapter.this.notifyDataSetChanged();
						TestCoordinator.check(TTChecks.QUESTION_EDITED);
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

	public void setDefault() {
		isChecked = -1;
		oneCorrectAnswer = false;
		noEmptyAnswer = false;
		clear();
	}
}