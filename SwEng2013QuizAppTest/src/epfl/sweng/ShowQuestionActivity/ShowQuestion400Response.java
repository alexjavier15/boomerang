package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

public class ShowQuestion400Response extends ShowQuestionActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pushCannedResponse("GET", HttpStatus.SC_BAD_REQUEST);

    }

    public void test3Get400HttpResponse() {

        @SuppressWarnings("static-access")
        boolean errorText = getSolo().searchText(getActivity().ERROR_MESSAGE);
        assertTrue("Error Toast Shown : ", errorText);

    }

}
