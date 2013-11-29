package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

import epfl.sweng.testing.TestCoordinator.TTChecks;

public class ShowQuestion500Response extends ShowQuestionActivityTemplate {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		pushCannedResponse("GET", HttpStatus.SC_INTERNAL_SERVER_ERROR);

	}

	public void test2Get500HttpResponse() {

		getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
		@SuppressWarnings("static-access")
		boolean errorText = getSolo().searchText(getActivity().ERROR_MESSAGE);
		assertTrue("Error Toast Shown : ", errorText);

	}

}
