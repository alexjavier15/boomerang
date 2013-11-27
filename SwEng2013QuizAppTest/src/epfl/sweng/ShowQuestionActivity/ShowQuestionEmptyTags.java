package epfl.sweng.ShowQuestionActivity;

import org.apache.http.HttpStatus;

import epfl.sweng.showquestions.ShowQuestionsActivity;

public class ShowQuestionEmptyTags extends ShowQuestionActivityTemplate {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        pushCannedResponse("GET", HttpStatus.SC_OK, DEFAULT_QUESTION, "");

    }

    public void testEmptyTags() {

        getActivity();
        boolean emptyTagMsg = getSolo().searchText(ShowQuestionsActivity.EMPTY_TAGS_MSG);
        assertTrue("Error Toast Shown : ", emptyTagMsg);

    }
}
