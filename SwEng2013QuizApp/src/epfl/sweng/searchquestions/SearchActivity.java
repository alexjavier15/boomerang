package epfl.sweng.searchquestions;

import epfl.sweng.R;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.testing.TestingTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	protected void onStart() {
		TestCoordinator.check(TTChecks.SEARCH_ACTIVITY_SHOWN);
	}

	public void search() {

	}

}
