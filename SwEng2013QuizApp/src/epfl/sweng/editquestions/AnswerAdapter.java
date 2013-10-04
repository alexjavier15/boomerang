package epfl.sweng.editquestions;

import java.util.ArrayList;

import epfl.sweng.R;

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
	private Context context;
	private int isChecked;

	public AnswerAdapter(Context context, int resourceId,
			ArrayList<Answer> entries) {
		super(context, resourceId, entries);
		this.context = context;
		this.isChecked = 0;
	}

	public int getWhoIsChecked() {
		return isChecked;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final AnswerHolder holder;

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(R.layout.activity_answer_slot, null);
			holder = new AnswerHolder();
			holder.checkButton = (Button) view
					.findViewById(R.id.edit_buttonProperty);
			holder.answerText = (EditText) view
					.findViewById(R.id.edit_answerText);
			holder.removeButton = (Button) view
					.findViewById(R.id.edit_cancelAnswer);
			view.setTag(holder);
		} else {
			holder = (AnswerHolder) view.getTag();
		}
		holder.checkButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AnswerAdapter.this.getItem(isChecked).setChecked(
						context.getResources().getString(
								R.string.heavy_ballot_x));
				AnswerAdapter.this.isChecked = position;
				AnswerAdapter.this.getItem(position).setChecked(
						context.getResources().getString(
								R.string.heavy_check_mark));
				AnswerAdapter.this.notifyDataSetChanged();
			}
		});
		holder.removeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AnswerAdapter.this.getCount() > 1) {
					AnswerAdapter.this.getItem(position).setAnswer("");
					holder.answerText.setText(AnswerAdapter.this.getItem(
							position).getAnswer());
					AnswerAdapter.this.remove(AnswerAdapter.this
							.getItem(position));
					AnswerAdapter.this.notifyDataSetChanged();
				} else {
					Toast.makeText(
							context,
							"A question without an answer is useless, isn't it?",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		holder.checkButton.setText(AnswerAdapter.this.getItem(position)
				.getChecked());

		return view;
	}

	public static class AnswerHolder {
		public Button checkButton;
		public EditText answerText;
		public Button removeButton;
	}
}
