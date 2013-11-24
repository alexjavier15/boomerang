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

import android.database.DatabaseUtils;
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
    public static final String POST_SYNC_DB_NAME = "PostSync.db";
    public static final String QUERY_CACHE_DB_NAME = "QueryCache.db";
    public static final String QUESTION_CACHE_DB_NAME = "Cache.db";
    private static CacheManager sCacheManager = null;
    private static QuizQuestionDBHelper sPostQuestionDB;
    private static QuizQuestionDBHelper sQueryQuestionDB;

    private static QuizQuestionDBHelper sQuizQuestionDB;

    public static CacheManager getInstance() {
        if (sCacheManager == null) {
            sCacheManager = new CacheManager();

        }
        return sCacheManager;

    }

    private CacheManager() {

        sQuizQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), QUESTION_CACHE_DB_NAME);
        sPostQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), POST_SYNC_DB_NAME);
        sQueryQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), QUERY_CACHE_DB_NAME);

    }

    public void init() {
        Debug.out("onChange in chache manager called");

        if (QuizApp.getPreferences().getBoolean(PreferenceKeys.ONLINE_MODE, true)) {

            syncPostCachedQuestions();

        }
    }

    /**
     * @param question
     * @return
     */
    public HttpResponse addQuestionForSync(String question) {

        sPostQuestionDB.addQuizQuestion(question);

        DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();
        HttpResponse reponse = httpResFactory.newHttpResponse(
                new BasicStatusLine((new HttpPost()).getProtocolVersion(), HttpStatus.SC_CREATED, null), null);

        return reponse;

    }

    public HttpResponse getNextQueryQuestion() throws IOException, JSONException {
        String question = sQueryQuestionDB.getRandomQuizQuestion();
        return wrapQuizQuestion(question);

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

    /**
     * @param question
     */
    public void pushFetchedQuestion(String question) {
        sQuizQuestionDB.addQuizQuestion(question);

    }

    public void pushQueryQuestion(String query) {

        sQueryQuestionDB.addQuizQuestion(query);
    }

    private void syncPostCachedQuestions() {
        long num = DatabaseUtils.queryNumEntries(sPostQuestionDB.getReadableDatabase(),
                QuizQuestionDBHelper.TABLE_NAME);
        Debug.out("row count =" + num);

        (new BackgroundServiceTask()).execute();

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#
     * onSharedPreferenceChanged(android.content. SharedPreferences, java.lang.String)
     */

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

    private class BackgroundServiceTask extends AsyncTask<Void, Void, Void> {

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(Void... params) {
            Debug.out("Attempting to sync file");
            HttpResponse response = null;

            String[] jsonString = new String[2];
            QuizQuestion quizQuestion = null;

            do {
                jsonString = sPostQuestionDB.getFirstPostQuestion();
                try {

                    quizQuestion = new QuizQuestion(jsonString[1]);
                    response = HttpCommsProxy.getInstance().postJSONObject(HttpComms.URLPUSH,
                            JSONParser.parseQuiztoJSON(quizQuestion));
                    boolean inSync = response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED;
                    if (inSync) {
                        response.getEntity().consumeContent();
                        sPostQuestionDB.deleteQuizQuestion(jsonString[0]);
                    }

                } catch (JSONException e) {

                    Log.e(this.getClass().getName(), e.getMessage());
                    e.printStackTrace();
                    if (jsonString[0] != null) {
                        sPostQuestionDB.deleteQuizQuestion(jsonString[0]);

                    }

                } catch (ClientProtocolException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (HttpCommsProxy.getInstance().isConnected() && jsonString[0] != null);
            return null;

        }
    }
}
