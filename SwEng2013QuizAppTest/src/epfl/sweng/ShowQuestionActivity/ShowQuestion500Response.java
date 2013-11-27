package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

public class ShowQuestion500Response extends ShowQuestionActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pushCannedResponse("GET", HttpStatus.SC_INTERNAL_SERVER_ERROR);

    }

    public void test2Get500HttpResponse() {
       
        getActivity();
        @SuppressWarnings("static-access")
        boolean errorText = getSolo().waitForText(getActivity().ERROR_MESSAGE);
        assertTrue("Error Toast Shown : ", errorText);

    }

}
