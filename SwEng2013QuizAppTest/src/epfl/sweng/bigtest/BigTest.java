//package epfl.sweng.bigtest;
//
//import org.apache.http.impl.client.AbstractHttpClient;
//
//import android.test.ActivityInstrumentationTestCase2;
//import android.widget.EditText;
//import epfl.sweng.entry.MainActivity;
//import epfl.sweng.servercomm.SwengHttpClientFactory;
//import epfl.sweng.test.TestTemplate;
//import epfl.sweng.testing.TestCoordinator.TTChecks;
//
//public class BigTest extends ActivityInstrumentationTestCase2 {
//
//	public BigTest() {
//	}
//	
//	@Override
//	public void setUp() throws Exception {
//		super.setUp();
//	}
//	
//	AbstractHttpClient reelClient = SwengHttpClientFactory.getInstance();
//	
//	public void testNormalBehaviour() {
//		SwengHttpClientFactory.setInstance(reelClient);
//		getActivityAndWaitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
//		
//		getSolo().clickOnButton("Log in using Tequila");
//		waitFor(TTChecks.AUTHENTICATION_ACTIVITY_SHOWN);
//		
//		EditText username = getSolo().getEditText("GASPAR Username");
//		getSolo().enterText(username, "verstege");
//		EditText password = getSolo().getEditText("GASPAR Password");
//		getSolo().enterText(password, "W54i00ll96");
//		getSolo().clickOnButton("Log in using Tequila");
//		
//		//waitFor(TTChecks.MAIN_ACTIVITY_SHOWN);
//		
//	}
//
//}
