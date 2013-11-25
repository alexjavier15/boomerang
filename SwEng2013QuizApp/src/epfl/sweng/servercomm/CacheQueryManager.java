package epfl.sweng.servercomm;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.util.Log;
import epfl.sweng.cache.CacheManager;
import epfl.sweng.tools.JSONParser;

public final class CacheQueryManager implements IHttpConnectionHelper {

    private static CacheQueryManager sCacheQuery = null;
    private boolean hasNext = false;
    private String query = null;
    private String next = null;
    private int qCount = 0;
    private int questionIndex = 0;
    private LinkedList<Long> idList = null;

    public static CacheQueryManager getInstance() {
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
        if (urlString.equals(HttpComms.URL_SWENG_RANDOM_GET)) {
            return HttpCommsProxy.getInstance().getHttpResponse(urlString);
        } else {
            JSONObject joll = null;
            try {
                joll = (new JSONObject()).put("query", query);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return postJSONObject(urlString, joll);
        }
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
                if (hasNext) {
                    question.put("from", next);
                }
                postQuery(question);
            } catch (JSONException e) {

            }

        }

        try {
            response = CacheManager.getInstance().getQuestion(idList.poll().longValue());
        } catch (NullPointerException e) {
            // TODO Big error

            update(null);
            return null;
        }
        questionIndex++;

        return response;
    }

    public void postQuery(JSONObject question) throws ClientProtocolException, NetworkErrorException, IOException,
            JSONException {

        HttpResponse response = HttpCommsProxy.getInstance().postJSONObject(HttpComms.URL_SWENG_QUERY_POST, question);
        JSONObject jsonResponse = JSONParser.getParser(response);
        if (jsonResponse.has("cacheResponse")) {

            hasNext = !jsonResponse.isNull("next");
            if (hasNext) {
                next = jsonResponse.getString("next");
                Log.d(this.getClass().getName(), "has next message in query response saving: " + next);
            }// if hasNext ending

            JSONArray questionArray = jsonResponse.getJSONArray("questions");

            if (questionArray.length() > 0) {
                for (int i = 0; i < questionArray.length(); i++) {
                    idList.add(CacheManager.getInstance().pushFetchedQuestion(questionArray.getString(i)));
                    Log.d(this.getClass().getName(),
                            "Question number " + qCount + " saved from " + questionArray.length() + " received.");
                    qCount++;
                }// for loop ending
            }// if array not empty ending
        } else {
            JSONArray idArray = jsonResponse.getJSONArray("cacheResponse");
            if (idArray.length() > 0) {
                for (int i = 0; i < idArray.length(); i++) {
                    idList.add(idArray.getLong(i));
                    qCount++;
                }
            }

        }
    }// postQuery() ending

    public void update(String queryText) {
        hasNext = false;
        next = null;
        qCount = 0;
        query = queryText;
        idList = new LinkedList<Long>();
    }

}
