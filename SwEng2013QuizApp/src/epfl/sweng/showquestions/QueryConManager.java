package epfl.sweng.showquestions;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.util.Log;

import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsProxy;
import epfl.sweng.testing.TestCoordinator;
import epfl.sweng.testing.TestCoordinator.TTChecks;
import epfl.sweng.tools.Debug;
import epfl.sweng.tools.JSONParser;

/**
 * @author LorenzoLeon
 * 
 */
public class QueryConManager extends ConnectionManager {

    public static final String ERROR_QUERY = "No questions match your query";
    public static final String ERROR_MESSAGE = "There was an error retrieving the question";

    private ShowQuestionsActivity shower;

    private boolean isQuery = false;
    private boolean hasNext = false;
    private String query = null;

    public QueryConManager(ShowQuestionsActivity show) {
        this.shower = show;
        if (shower.getIntent().hasExtra("query_mode")) {
            isQuery = true;
            query = shower.getIntent().getStringExtra("query");
        }
    }

    @Override
    public void processHttpReponse(HttpResponse response) {
        QuizQuestion quizQuestion = null;
        if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            try {
                quizQuestion = new QuizQuestion(JSONParser.getParser(response).toString());
                Debug.out(this.getClass(), quizQuestion);
            } catch (IOException e) {
                HttpCommsProxy.getInstance().setOnlineMode(false);
                Log.e(this.getClass().getName(), e.getMessage());
            } catch (JSONException e) {
                HttpCommsProxy.getInstance().setOnlineMode(false);
                e.printStackTrace();
            }
            shower.setQuestion(quizQuestion);
        } else {
            if (response.getStatusLine().getStatusCode() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                HttpCommsProxy.getInstance().setOnlineMode(false);
            }
        }
        TestCoordinator.check(TTChecks.QUESTION_SHOWN);
    }

    @Override
    public HttpResponse requete() {
        HttpResponse response = null;

        try {
            if (isQuery) {
                JSONObject joll = (new JSONObject()).put("query", query);
                response = HttpCommsProxy.getInstance().pollQuestion(HttpComms.URL_SWENG_QUERY_POST, joll);
            } else {
                response = HttpCommsProxy.getInstance().getHttpResponse(HttpComms.URL_SWENG_RANDOM_GET);
            }
        } catch (ClientProtocolException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (NetworkErrorException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (IOException e) {
            Log.e(getClass().getName(), e.getMessage());
        } catch (JSONException e) {
            Log.e(getClass().getName(), e.getMessage());
        }

        return response;
    }

    public String getErrorMessage() {
        if (hasNext) {
            return ERROR_QUERY;
        } else {
            return ERROR_MESSAGE;
        }
    }
}
