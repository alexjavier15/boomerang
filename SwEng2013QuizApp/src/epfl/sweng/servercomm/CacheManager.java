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
import android.os.AsyncTask;
import android.util.Log;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */
public final class CacheManager {
    public static final String QUESTION_CACHE_DB_NAME = "Cache.db";
    public static final String POST_SYNC_DB_NAME = "PostSync.db";
    public static final String QUERY_CACHE_DB_NAME = "QueryCache.db";
    private static QuizQuestionDBHelper sQuizQuestionDB;
    private static QuizQuestionDBHelper sPostQuestionDB;
    private static QuizQuestionDBHelper sQueryQuestionDB;
    private static CacheManager sCacheManager = null;

    private CacheManager() {

        sQuizQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), QUESTION_CACHE_DB_NAME);
        sPostQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), POST_SYNC_DB_NAME);
        sQueryQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), QUERY_CACHE_DB_NAME);
        init();
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

        String question = sQuizQuestionDB.getRandomQuizQuestion();

        return wrapQuizQuestion(question);
    }

    public HttpResponse getNextQueryQuestion() throws IOException, JSONException {
        String question = sQueryQuestionDB.getRandomQuizQuestion();
        return wrapQuizQuestion(question);

    }

    private HttpResponse wrapQuizQuestion(String question) throws IOException, JSONException {
        HttpResponse reponse = null;
        QuizQuestion quizQuestion = null;
        DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();
        if (question != null) {
            quizQuestion = new QuizQuestion(question);
        }

        if (quizQuestion != null) {
            JSONObject questionObj = JSONParser.parseQuiztoJSON(quizQuestion);
            reponse = httpResFactory.newHttpResponse(new BasicStatusLine((new HttpPost()).getProtocolVersion(),
                    HttpStatus.SC_OK, null), null);
            reponse.setEntity(new StringEntity(questionObj.toString(HttpComms.STRING_ENTITY)));
        } else {
            reponse = httpResFactory.newHttpResponse(new BasicStatusLine((new HttpPost()).getProtocolVersion(),
                    HttpStatus.SC_INTERNAL_SERVER_ERROR, null), null);
        }

        return reponse;
    }

    /**
     * @param question
     * @return
     */
    public HttpResponse addQuestionForSync(String question) {

        sPostQuestionDB.addQuizQuestion(question);

        DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();
        HttpResponse reponse = httpResFactory.newHttpResponse(new BasicStatusLine(
                (new HttpPost()).getProtocolVersion(), HttpStatus.SC_CREATED, null), null);

        return reponse;

    }

    public void pushQueryQuestion(String query) {

        sQueryQuestionDB.addQuizQuestion(query);
    }

    private void syncPostCachedQuestions() {

        (new BackgroundServiceTask()).execute();

    }

    /**
     * @param question
     */
    public void pushFetchedQuestion(String question) {
        sQuizQuestionDB.addQuizQuestion(question);

    }

    private class BackgroundServiceTask extends AsyncTask<Void, Boolean, Boolean> {

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            Debug.out("Attempting to sync file");

            QuizQuestion quizQuestion = null;

            String jsonString = sPostQuestionDB.getFirstPostQuestion();

            do {
                if (quizQuestion != null) {
                    HttpResponse response = null;
                    try {
                        Debug.out("go to process post");
                        quizQuestion = new QuizQuestion(jsonString);
                        response = HttpCommsProxy.getInstance().postJSONObject(HttpComms.URLPUSH,
                                JSONParser.parseQuiztoJSON(quizQuestion));
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                            sPostQuestionDB.deleteQuizQuestion();
                            quizQuestion = new QuizQuestion(sPostQuestionDB.getFirstPostQuestion());
                            response.getEntity().consumeContent();
                        }
                        Debug.out("reponse got");

                    } catch (ClientProtocolException e) {
                        e.printStackTrace();

                    } catch (NetworkErrorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Log.e(this.getClass().getName(), e.getMessage());
                        sPostQuestionDB.deleteQuizQuestion();

                    }

                }

            } while (quizQuestion != null);

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
                QuizApp.getPreferences().edit().putBoolean(PreferenceKeys.ONLINE_MODE, false);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#
     * onSharedPreferenceChanged(android.content. SharedPreferences, java.lang.String)
     */

    public void init() {
        Debug.out("onChange in chache manager called");

        if (QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true)) {

            syncPostCachedQuestions();

        }
    }
}
