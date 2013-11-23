package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import epfl.sweng.cache.CacheManager;
import epfl.sweng.tools.JSONParser;

import android.accounts.NetworkErrorException;
import android.util.Log;

public final class CacheQueryManager implements IHttpConnectionHelper {

    private static CacheQueryManager sCacheQuery = null;
    private boolean hasNext = false;
    private String query = null;
    private String next = null;
    private int qCount = 0;
    private int questionIndex = 0;

    public static IHttpConnectionHelper getInstance() {
        if (sCacheQuery == null) {
            sCacheQuery = new CacheQueryManager();
        }
        return sCacheQuery;
    }

    private CacheQueryManager() {

    }

    @Override
    public HttpResponse getHttpResponse(String urlString) throws ClientProtocolException, IOException,
            NetworkErrorException, NullPointerException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isConnected() {
        return HttpCommsProxy.getInstance().isConnected();
    }

    @Override
    public HttpResponse postJSONObject(String url, JSONObject question) throws ClientProtocolException, IOException,
            NetworkErrorException {
        HttpResponse response = null;
        if ((qCount == 0) || (qCount == questionIndex & hasNext)) {
            try {
                JSONObject joll = (new JSONObject()).put("query", query);
                if (hasNext) {
                    joll.put("from", next);
                }
                postQuery(joll);
            } catch (JSONException e) {

            }

        }
        int hashIndex = query.hashCode() + questionIndex;
        try {
            response = HttpCommsProxy.getInstance().getQueryQuestion(hashIndex);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        questionIndex++;

        return response;
    }

    public void postQuery(JSONObject question) throws ClientProtocolException, NetworkErrorException, IOException,
            JSONException {

        if (!question.getString("query").equals(query)) {
            hasNext = false;
            next = null;
            qCount = 0;
            query = question.getString("query");
        }

        HttpResponse response = HttpCommsProxy.getInstance().postJSONObject(HttpComms.URL_SWENG_QUERY_POST, question);
        JSONObject jsonResponse = JSONParser.getParser(response);
        hasNext = !jsonResponse.isNull("next");
        if (hasNext) {
            next = jsonResponse.getString("next");
            Log.d(this.getClass().getName(), "has next message in query response saving: " + next);
        }

        JSONArray questionArray = jsonResponse.getJSONArray("questions");

        if (questionArray.length() > 0) {
            for (int i = 0; i < questionArray.length(); i++) {
                HttpResponse tempQuestion = CacheManager.getInstance().wrapQuizQuestion(questionArray.getString(i));
                int hashIndex = query.hashCode() + qCount;
                HttpCommsProxy.getInstance().saveQuery(tempQuestion, hashIndex);
                Log.d(this.getClass().getName(),
                        "Question number " + qCount + " saved from " + questionArray.length() + " received.");
                qCount++;
            }
        }
    }

}
