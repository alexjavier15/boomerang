package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

import epfl.sweng.testing.TestCoordinator.TTChecks;

public class ShowQuestionServerFailures extends ShowQuestionActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testGet400HttpResponse() {

        pushCannedResponse("GET", HttpStatus.SC_BAD_REQUEST);
        @SuppressWarnings("static-access")
        boolean errorText = getSolo().searchText(getActivity().ERROR_MESSAGE);
        assertTrue("Error Toast Shown : ", errorText);
        popCannedResponse();
        getActivity().finish();

    }

    public void test2Get500HttpResponse() {
        pushCannedResponse("GET", HttpStatus.SC_INTERNAL_SERVER_ERROR);
        getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
        @SuppressWarnings("static-access")
        boolean errorText = getSolo().searchText(getActivity().ERROR_MESSAGE);
        assertTrue("Error Toast Shown : ", errorText);
        popCannedResponse();
        getActivity().finish();

    }

    public void testGetMalformedResponse() {
        pushMalformedCannedResponse("GET", HttpStatus.SC_OK);
        getActivityAndWaitFor(TTChecks.QUESTION_SHOWN);
        @SuppressWarnings("static-access")
        boolean errorText = getSolo().searchText(getActivity().ERROR_MESSAGE);
        assertTrue("Error Toast Shown : ", errorText);
        popCannedResponse();
        getActivity().finish();

    }
}
