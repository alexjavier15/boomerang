package epfl.sweng.showquestions;

import java.io.IOException;
import java.util.LinkedList;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.util.Log;

import epfl.sweng.servercomm.CacheManager;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsProxy;
import epfl.sweng.tools.JSONParser;

/**
 * @author LorenzoLeon
 * 
 */
public class QueryConManager extends ConnectionManager {

    public static final String ERROR_NO_MORE = "No more questions match your query";
    public static final String ERROR_NONE_FOUND = "No questions match your query";
    private ShowQuestionsActivity shower;
    private String query = null;
    private int qCount = 0;
    private int questionIndex = 0;
    private boolean hasNext = false;
    private String next = null;
    private LinkedList<HttpResponse> quList = new LinkedList<HttpResponse>();

    public QueryConManager(ShowQuestionsActivity show) {
        this.shower = show;
        query = shower.getIntent().getStringExtra("query");
    }

    @Override
    public void processHttpReponse(HttpResponse response) {
        shower.parseResponse(response);

    }

    @Override
    public HttpResponse requete() {
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

        // response =
        // HttpCommsProxy.getInstance().getQueryQuestion(questionIndex);
        response = quList.poll();
        questionIndex++;

        return response;
    }

    /**
     * posts the a JsonObject (either query or next) to Sweng and if questions are received it saves them in cache
     * continuing or starting a query index.
     * 
     * @param joll
     */

    private void postQuery(JSONObject joll) {
        try {
            HttpResponse response = HttpCommsProxy.getInstance().postJSONObject(HttpComms.URL_SWENG_QUERY_POST, joll);
            JSONObject jsonResponse = JSONParser.getParser(response);
            hasNext = !jsonResponse.isNull("next");
            if (hasNext) {
                next = jsonResponse.getString("next");
                Log.d(this.getClass().getName(), "has next message in query response saving: " + next);
            }

            JSONArray questionArray = jsonResponse.getJSONArray("questions");

            if (questionArray.length() > 0) {
                for (int i = 0; i < questionArray.length(); i++) {
                    HttpResponse question = CacheManager.getInstance().wrapQuizQuestion(questionArray.getString(i));
                    quList.add(question);
                    HttpCommsProxy.getInstance().saveQuery(question);
                    qCount++;
                }
            }

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
    }

    public String getErrorMessage() {
        if (qCount > 0) {
            return ERROR_NO_MORE;
        } else {
            return ERROR_NONE_FOUND;
        }
    }
}
