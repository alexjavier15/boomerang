package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

public class ShowQuestionMalformedResponse extends ShowQuestionActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pushMalformedCannedResponse("GET", HttpStatus.SC_OK);

    }

//    public void testGetMalformedResponse() {
//
//        @SuppressWarnings("static-access")
//    
//        boolean errorText = getSolo().searchText(getActivity().ERROR_MESSAGE);
//        assertTrue("Error Toast Shown : ", errorText);
//
//    }

}
