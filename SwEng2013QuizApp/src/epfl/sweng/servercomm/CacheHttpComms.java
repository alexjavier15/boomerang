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
import android.content.Context;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */
public final class CacheHttpComms implements IHttpConnectionHelper {
    private static CacheHttpComms sCache = null;
    private static Context sContext = null;

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

    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String)
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) throws ClientProtocolException, IOException,
            NetworkErrorException {
        HttpResponse reponse = null;

        if (urlString.equals(HttpComms.URL)) {

            try {
                reponse = CacheManager.getInstance().getRandomQuestion();
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

            return CacheManager.getInstance().addQuestionForSync(quizQuestion);

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
            CacheManager.getInstance().pushFetchedQuestion(quizQuestion);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
