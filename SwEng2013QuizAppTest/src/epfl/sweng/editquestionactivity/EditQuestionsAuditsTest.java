package epfl.sweng.editquestionactivity;

import epfl.sweng.testing.TestCoordinator.TTChecks;

public class EditQuestionsAuditsTest extends EditQuestionActivityTemplate {

    public void testAuditError() {

        getActivityAndWaitFor(TTChecks.EDIT_QUESTIONS_SHOWN);

        assertEquals(0, getActivity().auditErrors());
        getActivity().finish();

    }

}
