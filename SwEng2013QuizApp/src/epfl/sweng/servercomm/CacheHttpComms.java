/**
 * 
 */
package epfl.sweng.servercomm;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import epfl.sweng.cache.CacheManager;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */
public final class CacheHttpComms implements IHttpConnectionHelper {
    private static CacheHttpComms sCache = null;

    public static CacheHttpComms getInstance() {
        if (sCache == null) {
            sCache = new CacheHttpComms();
        }
        return sCache;
    }

    private CacheHttpComms() {

        CacheManager.getInstance();

    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#getHttpResponse(java.lang.String)
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) {
        HttpResponse response = null;

        if (urlString.equals(HttpComms.URL_SWENG_RANDOM_GET)) {
            response = CacheManager.getInstance().getRandomQuestion();
        }
        return response;

    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#isConnected()
     */
    @Override
    public boolean isConnected() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see epfl.sweng.servercomm.IHttpConnection#postQuestion(java.lang.String, org.json.JSONObject)
     */
    @Override
    public HttpResponse postJSONObject(String url, JSONObject question) {
        if (url.equals(HttpComms.URL_SWENG_PUSH)) {
            return CacheManager.getInstance().addQuestionForSync(question.toString());
        } else if (url.equals(HttpComms.URL_SWENG_QUERY_POST)) {
            try {
                return CacheManager.getInstance().getQueriedQuestions(question.getString("query"));
            } catch (JSONException e) {
            	Log.e(getClass().getName(), e.getMessage(), e);
                throw new UnsupportedOperationException("Unsupported operation in offline mode");
            }
        } else {
            throw new UnsupportedOperationException("Unsupported operation in offline mode");
        }
    }

    public void pushQuestion(JSONObject quizQuestion) {

        CacheManager.getInstance().pushFetchedQuestion(quizQuestion.toString());

    }

    /**
     * @param reponse
     * @throws JSONException
     * @throws NullPointerException
     */
    public void pushQuestion(HttpResponse reponse) {

        JSONObject quizQuestion;

        try {
            quizQuestion = JSONParser.getParser(reponse);
            pushQuestion(quizQuestion);
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
        }

    }

}
