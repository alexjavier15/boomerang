/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */
public final class CacheManager implements OnSharedPreferenceChangeListener {

	public static final String QUESTION_CACHE_DB_NAME = "Cache.db";
	public static final String POST_SYNC_DB_NAME = "PostSync.db";
	private static QuizQuestionDBHelper sQuizQuestionDB;
	private static QuizQuestionDBHelper sPostQuestionDB;
	private static CacheManager sCacheManager = null;

	private CacheManager() {
		QuizApp.getPreferences().registerOnSharedPreferenceChangeListener(this);
		sQuizQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(),
				QUESTION_CACHE_DB_NAME);
		sPostQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(),
				POST_SYNC_DB_NAME);

	}

	public static CacheManager getInstance() {
		if (sCacheManager == null) {
			sCacheManager = new CacheManager();
		}
		return sCacheManager;

	}

	/**
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public HttpResponse getRandomQuestion() throws IOException, JSONException {

		HttpResponse reponse = null;
		QuizQuestion quizQuestion = null;
		DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();

		quizQuestion = sQuizQuestionDB.getRandomQuizQuestion();
		if (quizQuestion != null) {
			JSONObject questionObj = JSONParser.parseQuiztoJSON(quizQuestion);
			reponse = httpResFactory.newHttpResponse(new BasicStatusLine(
					(new HttpPost()).getProtocolVersion(), HttpStatus.SC_OK,
					null), null);
			reponse.setEntity(new StringEntity(questionObj
					.toString(HttpComms.STRING_ENTITY)));
		} else {
			reponse = httpResFactory.newHttpResponse(new BasicStatusLine(
					(new HttpPost()).getProtocolVersion(),
					HttpStatus.SC_INTERNAL_SERVER_ERROR, null), null);
		}

		return reponse;
	}

	/**
	 * @param quizQuestion
	 * @return
	 */
	public HttpResponse addQuestionForSync(QuizQuestion quizQuestion) {

		sPostQuestionDB.addQuizQuestion(quizQuestion);

		DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();
		HttpResponse reponse = httpResFactory.newHttpResponse(
				new BasicStatusLine((new HttpPost()).getProtocolVersion(),
						HttpStatus.SC_CREATED, null), null);

		return reponse;

	}

	private void syncPostCachedQuestions() {

		(new BackgroundServiceTask()).execute();

	}

	/**
	 * @param quizQuestion
	 */
	public void pushFetchedQuestion(QuizQuestion quizQuestion) {
		sQuizQuestionDB.addQuizQuestion(quizQuestion);

	}

	private class BackgroundServiceTask extends
			AsyncTask<Void, Boolean, Boolean> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			Debug.out("Attempting to sync file");

			QuizQuestion quizQuestion = sPostQuestionDB.getFirstPostQuestion();
			while (quizQuestion != null) {

				HttpResponse response = null;
				try {
					Debug.out("go to process post");
					response = HttpCommsProxy.getInstance().postJSONObject(
							HttpComms.URLPUSH,
							JSONParser.parseQuiztoJSON(quizQuestion));
					response.getEntity().consumeContent();
					Debug.out("reponse got");

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NetworkErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
					sPostQuestionDB.deleteQuizQuestion(quizQuestion);
					quizQuestion = sPostQuestionDB.getFirstPostQuestion();

				} else {
					quizQuestion = null;
				}

			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (!result) {
				QuizApp.getPreferences().edit()
						.putBoolean(PreferenceKeys.ONLINE_MODE, false);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#
	 * onSharedPreferenceChanged(android.content. SharedPreferences,
	 * java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(PreferenceKeys.ONLINE_MODE)) {
			if (sharedPreferences.getBoolean(key, false) == true) {
				Debug.out("start sync");
				syncPostCachedQuestions();
			}
		}
	}
}
