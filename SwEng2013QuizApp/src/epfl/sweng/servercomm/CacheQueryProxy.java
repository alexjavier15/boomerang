package epfl.sweng.servercomm;

import java.io.IOException;
//import java.util.Collections;
import java.util.LinkedList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import epfl.sweng.cache.CacheManager;
import epfl.sweng.tools.JSONParser;

public final class CacheQueryProxy implements IHttpConnectionHelper {

    public static final String ERROR_QUERY = "No questions match your query";
    public static final String ERROR_QUERY2 = "No more questions match your query"; 
    public static final String ERROR_MESSAGE = "There was an error retrieving the question";
    private static CacheQueryProxy sCacheQuery = null;
    private boolean hasNext = false;
    private String query = null;
    private String next = null;
    private int qCount = 0;
    private int questionIndex = 0;
    private LinkedList<Long> idList = null;

    public static CacheQueryProxy getInstance() {
        if (sCacheQuery == null) {
            sCacheQuery = new CacheQueryProxy();
        }
        return sCacheQuery;
    }

    private CacheQueryProxy() {

    }

    @Override
    public HttpResponse getHttpResponse(String urlString) {
        if (urlString.equals(HttpComms.URL_SWENG_RANDOM_GET)) {
            return HttpCommsProxy.getInstance().getHttpResponse(urlString);
        } else {
            JSONObject joll = null;
            try {
                joll = (new JSONObject()).put("query", query);
            } catch (JSONException e) {
                Log.e(this.getClass().getName(), e.getMessage() + " trying to create Jsonquery failed", e);
            }
            return postJSONObject(urlString, joll);
        }
    }

    @Override
    public boolean isConnected() {
        return HttpCommsProxy.getInstance().isConnected();
    }

    @Override
    public HttpResponse postJSONObject(String url, JSONObject question) {

        HttpResponse response = null;
        if ((qCount == 0) || (qCount == questionIndex & hasNext)) {
            try {
                if (hasNext) {
                    question.put("from", next);
                }
                postQuery(question);
            } catch (JSONException e) {
                Log.e(getClass().getName(), e.getMessage(), e);
            } catch (HttpResponseException e) {
                Log.e(getClass().getName(), e.getMessage(), e);
            } catch (IOException e) {
                Log.e(getClass().getName(), e.getMessage(), e);
            }

        }
        long id = -1;

        try {
            id = idList.poll().longValue();
        } catch (NullPointerException e) {
        	Log.i(getClass().getName(), "No more questions cached.");
        }
        response = CacheManager.getInstance().getQuestion(id);
        questionIndex++;

        return response;
    }

    public void postQuery(JSONObject question) throws JSONException, HttpResponseException, IOException {

        HttpResponse response = HttpCommsProxy.getInstance().postJSONObject(HttpComms.URL_SWENG_QUERY_POST, question);
        JSONObject jsonResponse = JSONParser.getParser(response);
        if (jsonResponse.isNull("cacheResponse")) {

            hasNext = !jsonResponse.isNull("next");
            if (hasNext) {
                next = jsonResponse.getString("next");
                Log.d(this.getClass().getName(), "has next message in query response saving: " + next);
            }

            JSONArray questionArray = jsonResponse.getJSONArray("questions");

            if (questionArray.length() > 0) {
                for (int i = 0; i < questionArray.length(); i++) {
                    idList.add(CacheManager.getInstance().pushFetchedQuestion(questionArray.getString(i)));
                    qCount++;
                }
                Log.d(this.getClass().getName(), "Received: " + qCount + " questions");
                // Shuffle the list (optional)
                //Collections.shuffle(idList);
            }
        } else {
            JSONArray idArray = jsonResponse.getJSONArray("cacheResponse");
            if (idArray.length() > 0) {
                for (int i = 0; i < idArray.length(); i++) {
                    idList.add(idArray.getLong(i));
                    qCount++;
                }
            }

        }
    }

    public void update(String queryText) {
        Log.v(this.getClass().getName(), "Updating new query created");
        hasNext = false;
        next = null;
        qCount = 0;
        query = queryText;
        idList = new LinkedList<Long>();
    }

    public String getErrorMessage() {
        if (query != null) {
            if (questionIndex > 1) {
                return ERROR_QUERY2;                
            } else {
                return ERROR_QUERY;
            }
        } else {
            return ERROR_MESSAGE;
        }
    }

}
