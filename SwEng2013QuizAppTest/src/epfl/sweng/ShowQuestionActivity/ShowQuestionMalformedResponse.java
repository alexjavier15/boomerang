package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

public class ShowQuestionMalformedResponse extends ShowQuestionActivityTemplate {

    protected void setUp() throws Exception {// TODO Auto-generated method stub
        super.setUp();
        pushMalformedCannedResponse("GET", HttpStatus.SC_OK);

    }

    public void test1GetMalformedResponse() {

        @SuppressWarnings("static-access")
        boolean errorText = getSolo().waitForText(getActivity().ERROR_MESSAGE);
        assertTrue("Error Toast Shown : ", errorText);

    }

    public void test2Get300HttpResponse() {
        popCannedResponse();
        pushCannedResponse("GET", HttpStatus.SC_MULTIPLE_CHOICES);
        @SuppressWarnings("static-access")
        boolean errorText = getSolo().waitForText(getActivity().ERROR_MESSAGE);
        assertTrue("Error Toast Shown : ", errorText);

    }

    public void test3Get400HttpResponse() {
        popCannedResponse();
        pushCannedResponse("GET", HttpStatus.SC_BAD_REQUEST);
        @SuppressWarnings("static-access")
        boolean errorText = getSolo().waitForText(getActivity().ERROR_MESSAGE);
        assertTrue("Error Toast Shown : ", errorText);

    }

}
