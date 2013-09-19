package com.example.swengtutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

/**
 * @author AlbanMarguet
 */
public class ShowMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_message);
		Intent startingIntent = getIntent();
		String  userText = startingIntent.getStringExtra(MainActivity.class.getName());
		TextView textView = (TextView) findViewById(R.id.DisplayedText);
		textView.setText(userText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_message, menu);
		return true;
	}

}
