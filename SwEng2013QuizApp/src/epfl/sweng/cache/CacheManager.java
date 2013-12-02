/**
 * 
 */
package epfl.sweng.cache;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.util.Log;
import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsProxy;
import epfl.sweng.servercomm.QuizApp;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */
public final class CacheManager {
    public static final String POST_SYNC_DB_NAME = "PostSync.db";
    public static final String QUESTION_CACHE_DB_NAME = "Cache.db";
    private static CacheManager sCacheManager = null;
    private static QuizQuestionDBHelper sPostQuestionDB;
    private static QuizQuestionDBHelper sQuizQuestionDB;

    private CacheManager() {
        sQuizQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), QUESTION_CACHE_DB_NAME);
        sPostQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), POST_SYNC_DB_NAME);
        Log.v("Cahce has beed initialized :", sQuizQuestionDB.getReadableDatabase().getPath());
        Log.v("Cahce has beed initialized :", sPostQuestionDB.getReadableDatabase().getPath());
        sQuizQuestionDB.close();
        sPostQuestionDB.close();

    }

    public static CacheManager getInstance() {
        if (sCacheManager == null) {
            sCacheManager = new CacheManager();
        }
        return sCacheManager;
    }

    public static void reset() {
        sCacheManager = new CacheManager();

    }

    public void init() {
        Debug.out(this.getClass(), "onChange in chache manager called");
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

    /**
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public HttpResponse getRandomQuestion() {

        String question = sQuizQuestionDB.getRandomQuizQuestion();
        return wrapQuizQuestion(question);
    }

    /**
     * @param question
     */
    public long pushFetchedQuestion(String question) {
        return sQuizQuestionDB.addQuizQuestion(question);
    }

    private void syncPostCachedQuestions() {
        long num = DatabaseUtils.queryNumEntries(sPostQuestionDB.getReadableDatabase(),
                QuizQuestionDBHelper.TABLE_NAME);
        Debug.out(this.getClass(), "row count =" + num);
        (new BackgroundServiceTask()).execute();

    }

    public HttpResponse wrapQuizQuestion(String question) {
        HttpResponse response = null;
        DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();

        if (question == null) {
            response = httpResFactory.newHttpResponse(new BasicStatusLine((new HttpPost()).getProtocolVersion(),
                    HttpStatus.SC_BAD_REQUEST, null), null);
        } else {
            response = httpResFactory.newHttpResponse(new BasicStatusLine((new HttpPost()).getProtocolVersion(),
                    HttpStatus.SC_OK, null), null);
            try {
                response.setEntity(new StringEntity(question));
            } catch (UnsupportedEncodingException e) {
                Log.e(this.getClass().getName(), e.getMessage());
            }
        }
        return response;
    }

    public HttpResponse getQueriedQuestions(String query) {
        HttpResponse response = null;
        String answer = null;

        try {
            JSONArray array = new JSONArray(sQuizQuestionDB.getQueriedQuestions(query));
            JSONObject questionObj = (new JSONObject()).put("cacheResponse", array);
            answer = questionObj.toString(HttpComms.STRING_ENTITY);
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }

        response = wrapQuizQuestion(answer);
        return response;
    }

    public HttpResponse getQuestion(long id) {
        return wrapQuizQuestion(sQuizQuestionDB.getQuestionbyID(id));
    }

    private class BackgroundServiceTask extends AsyncTask<Void, Void, Void> {

        /*
         * (non-Javadoc)
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(Void... params) {
            Debug.out(this.getClass(), "Attempting to sync file");
            HttpResponse response = null;

            String[] jsonString = new String[2];
            QuizQuestion quizQuestion = null;

            do {
                jsonString = sPostQuestionDB.getFirstPostQuestion();
                try {

                    quizQuestion = new QuizQuestion(jsonString[1]);
                    response = HttpCommsProxy.getInstance().postJSONObject(HttpComms.URL_SWENG_PUSH,
                            JSONParser.parseQuiztoJSON(quizQuestion));
                    boolean inSync = response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED;
                    if (inSync) {
                        if (response.getEntity() != null) {
                            response.getEntity().consumeContent();
                        }
                        sPostQuestionDB.deleteQuizQuestion(jsonString[0]);
                    }

                } catch (JSONException e) {

                    Debug.out(this.getClass(), "No more post-questions to sync");
                    // e.printStackTrace();
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
