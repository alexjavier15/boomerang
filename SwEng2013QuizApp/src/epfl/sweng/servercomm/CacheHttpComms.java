/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.CacheManagerService.CacheServiceBinder;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */
public final class CacheHttpComms implements IHttpConnectionHelper {
    private static CacheHttpComms sCache = null;
    private static Context sContext = null;
    private CacheManagerService mCacheService = null;
    private boolean isBound;

    public static CacheHttpComms getInstance() {
        if (sCache == null) {
            sCache = new CacheHttpComms();
        }
        return sCache;
    }

    /**
     * @param context
     */
    private CacheHttpComms() {
        sContext = QuizApp.getContexStatic();
        Intent intent = new Intent(sContext.getApplicationContext(), CacheManagerService.class);
        sContext.bindService(intent, mCacheConnection, Context.BIND_AUTO_CREATE);
        Debug.out("service bound: " + isBound);

    }

    private ServiceConnection mCacheConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CacheServiceBinder binder = (CacheServiceBinder) service;
            mCacheService = binder.getService();
            isBound = true;
            Debug.out("service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Debug.out("service disconnected");

        }

    };

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String)
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) throws ClientProtocolException, IOException,
            NetworkErrorException {
        HttpResponse reponse = null;

        if (urlString.equals(HttpComms.URL) && isBound) {

            try {
                reponse = mCacheService.getRandomQuestion();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            throw new UnsupportedOperationException("Unsupported operation in offline mode");
        }
        return reponse;

    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#postQuestion(java.lang.String, org.json.JSONObject)
     */
    @Override
    public HttpResponse postJSONObject(String url, JSONObject question) throws ClientProtocolException, IOException,
            JSONException, NetworkErrorException {
        if (url.equals(HttpComms.URLPUSH)) {
            QuizQuestion quizQuestion = null;

            try {
                quizQuestion = new QuizQuestion(question.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return mCacheService.addQuestionForSync(quizQuestion);

        } else {
            throw new UnsupportedOperationException("Unsupported operation in offline mode");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#isConnected()
     */
    @Override
    public boolean isConnected() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @param reponse
     */
    public void pushQuestion(HttpResponse reponse) {

        QuizQuestion quizQuestion;
        try {
            quizQuestion = JSONParser.parseJsonToQuiz(reponse, sContext);
            mCacheService.pushFetchedQuestion(quizQuestion);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
