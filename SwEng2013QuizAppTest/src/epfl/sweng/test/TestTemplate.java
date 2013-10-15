package epfl.sweng.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpStatus;

import com.jayway.android.robotium.solo.Solo;

import epfl.sweng.minimalmock.MockHttpClient;
import epfl.sweng.questions.QuizQuestion;
import epfl.sweng.servercomm.SwengHttpClientFactory;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestingTransaction;
import android.test.ActivityInstrumentationTestCase2;


@SuppressWarnings("rawtypes")
public class TestTemplate<T> extends ActivityInstrumentationTestCase2 {
	
	private MockHttpClient httpClient;
    protected Solo solo;

	@SuppressWarnings("unchecked")
	public TestTemplate(Class activityClass) {
		super(activityClass);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        httpClient = new MockHttpClient();
        httpClient.pushCannedResponse(
				"GET (?:https?://[^/]+|[^/]+)?/+quizquestions/random\\b",
				HttpStatus.SC_OK,
				"{\"question\": \"What is the answer to life, the universe, and everything?\","
						+ " \"answers\": [\"Forty-two\", \"Twenty-seven\"], \"owner\": \"sweng\","
						+ " \"solutionIndex\": 0, \"tags\": [\"h2g2\", \"trivia\"], \"id\": \"1\" }",
				"application/json");
        SwengHttpClientFactory.setInstance(httpClient);
        solo = new Solo(getInstrumentation());
    }
	
	protected void getActivityAndWaitFor(final TestCoordinator.TTChecks expected) {
		TestCoordinator.run(getInstrumentation(), new TestingTransaction() {
			@Override
			public void initiate() {
				getActivity();
			}

			@Override
			public void verify(TestCoordinator.TTChecks notification) {
				assertEquals(String.format(
						"Expected notification %s, but received %s", expected,
						notification), expected, notification);
			}

			@Override
			public String toString() {
				return String.format("getActivityAndWaitFor(%s)", expected);
			}
		});
	}
	
	protected QuizQuestion createQuestionUniverse() {
		String question = "What is the answer to life, the universe, and everything?";
		int sol = 0;
		ArrayList<String> answers = new ArrayList<String>();
		answers.add("Forty-two");
		answers.add("Twenty-seven");
		Set<String> tags = new HashSet<String>(Arrays.asList("h2g2", "trivia"));
		return new QuizQuestion(-1, question, answers, sol, tags);
	}

}
