package epfl.sweng.editquestions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observer;
import java.util.Set;

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
import epfl.sweng.R;
import epfl.sweng.testing.Debug;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;

/**
 * 
 * @author CanGuzelhan, JavierRivas
 * 
 */

public class AnswerAdapter extends ArrayAdapter<Answer> {
	private Answer mAnswerChecked = null;
	private boolean mOneCorrectAnswer = false;
	private Set<Answer> mEmptyAnswers = new HashSet<Answer>();

	public AnswerAdapter(Activity context, int resourceId, ArrayList<Answer> entries, Observer activity) {
		super(context, resourceId, entries);
		setDefault();

	}

	@Override
	public void add(Answer object) {
		// TODO Auto-generated method stub
		super.add(object);
		mEmptyAnswers.add(object);
		Debug.out("empty ans : " + mEmptyAnswers.size());
	}

	/**
	 * Overrided
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final AnswerHolder holder;
		View newView = null;
		if (view == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			newView = inflater.inflate(R.layout.activity_answer_slot, null);
			holder = new AnswerHolder();
			holder.setCheckButton((Button) newView.findViewById(R.id.edit_buttonProperty));
			EditText editText = (EditText) newView.findViewById(R.id.edit_answerText);
			holder.setAnswerText(editText);
			holder.setRemoveButton((Button) newView.findViewById(R.id.edit_cancelAnswer));
			holder.getAnswerText().setTag(this.getItem(position));
			holder.getCheckButton().setTag(this.getItem(position));
			holder.getRemoveButton().setTag(this.getItem(position));

			newView.setTag(holder);

			holder.getAnswerText().addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
					if (!EditQuestionActivity.isReset()) {
						((Answer) holder.getAnswerText().getTag()).setAnswer(s.toString());

					}
					if (s.toString().trim().equals("")) {
						((EditQuestionActivity) AnswerAdapter.this.getContext())
							.updateEmptyText();
						mEmptyAnswers.add(((Answer) holder.getAnswerText().getTag()));
						Debug.out("empty ans : " + mEmptyAnswers.size());

					} else {
						mEmptyAnswers.remove((holder.getAnswerText().getTag()));
						Debug.out("empty ans : " + mEmptyAnswers.size());

						((EditQuestionActivity) AnswerAdapter.this.getContext())
							.updateTextchanged();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}
			});

			holder.getCheckButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mAnswerChecked != null) {
						mAnswerChecked.setChecked(getContext().getResources().getString(
							R.string.heavy_ballot_x));
						mAnswerChecked.setCorrect(false);
					}

					((Answer) v.getTag()).setChecked(getContext().getResources().getString(
						R.string.heavy_check_mark));
					((Answer) v.getTag()).setCorrect(true);
					mAnswerChecked = (Answer) v.getTag();
					mOneCorrectAnswer = true;
					notifyDataSetChanged();
					TestCoordinator.check(TTChecks.QUESTION_EDITED);
				}
			});

			holder.getRemoveButton().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (AnswerAdapter.this.getCount() > 0) {
						if (((Answer) v.getTag()).isCorrect()
							|| AnswerAdapter.this.getCount() < 2) {
							mOneCorrectAnswer = false;
							((EditQuestionActivity) AnswerAdapter.this.getContext())
								.updateEmptyText();
						}
						mEmptyAnswers.remove(v.getTag());
						AnswerAdapter.this.remove((Answer) v.getTag());
						AnswerAdapter.this.notifyDataSetChanged();
						TestCoordinator.check(TTChecks.QUESTION_EDITED);
					} else {
						Toast.makeText(AnswerAdapter.this.getContext(),
							"A question without an answer is useless, isn't it?",
							Toast.LENGTH_SHORT).show();
					}
				}

			});

		} else {
			holder = (AnswerHolder) view.getTag();
			newView = view;
			((AnswerHolder) view.getTag()).getAnswerText().setTag(this.getItem(position));
			((AnswerHolder) view.getTag()).getCheckButton().setTag(this.getItem(position));
			((AnswerHolder) view.getTag()).getRemoveButton().setTag(this.getItem(position));
		}

		holder.getAnswerText().setText(AnswerAdapter.this.getItem(position).getAnswer());
		holder.getCheckButton().setText(AnswerAdapter.this.getItem(position).getChecked());
		return newView;
	}

	/**
	 * Check if the set of empty answers if empty
	 * 
	 * @return true if the set is not empty or false otherwise.
	 */

	public boolean hasEmptyAnswer() {
		return !mEmptyAnswers.isEmpty();
	}

	/**
	 * @return
	 */
	public boolean hasOneCorrectAnswer() {
		return mOneCorrectAnswer;
	}

	/**
	 * Set defaults for the {@link AnswerAdapter}
	 * 
	 */
	public void setDefault() {
		mAnswerChecked = null;
		mOneCorrectAnswer = false;

		clear();
	}
}
