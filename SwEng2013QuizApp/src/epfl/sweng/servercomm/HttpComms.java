package epfl.sweng.servercomm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import epfl.sweng.authentication.CredentialManager;

/**
 * @author LorenzoLeon & Noortch
 * 
 *         This class is used to communicate with the server
 * 
 */
public final class HttpComms implements IHttpConnectionHelper {

    public final static String HEADER = "Authorization";
    private static HttpComms singleHTTPComs = null;
    public final static int STRING_ENTITY = 1;
    public final static String URL = "https://sweng-quiz.appspot.com/quizquestions/random";
    public final static String URL_SWENG_SWERVER_LOGIN = "https://sweng-quiz.appspot.com/login";
    public final static String URL_TEQUILA = "https://tequila.epfl.ch/cgi-bin/tequila/login";
    public final static String URLPUSH = "https://sweng-quiz.appspot.com/quizquestions/";
    public final static String URLQUERYPOST = "https://sweng-quiz.appspot.com/search";

    public static HttpComms getInstance() {

        if (singleHTTPComs == null) {
            singleHTTPComs = new HttpComms();
        }

        return singleHTTPComs;
    }

    private String authenticationValue = null;

    private HttpComms() {

    }

    private boolean checkLoginStatus() {
        String value = CredentialManager.getInstance().getUserCredential();
        if (!value.equals("")) {
            authenticationValue = "Tequila " + value;
            return true;
        } else {
            return false;
        }
    }

    private HttpResponse execute(HttpUriRequest request) {
        HttpResponse response = null;

        try {

            if (checkLoginStatus()) {
                request.addHeader(HEADER, authenticationValue);
            }
            response = SwengHttpClientFactory.getInstance().execute(request);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }

    /**
     * Gets an HttpResponse from the server in parameter
     * 
     * @param urlString
     *            The URL of the server on which we want to connect to.
     * @return The HttpResponse from the server.
     * @throws ClientProtocolException
     * @throws IOException
     * @throws NetworkErrorException
     */
    @Override
    public HttpResponse getHttpResponse(String urlString) {
        HttpGet request = new HttpGet(urlString);

        return execute(request);

    }

    @Override
    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) QuizApp.getContexStatic().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();

    }

    public HttpResponse postEntity(String url, HttpEntity entity) throws ClientProtocolException, IOException,
            NetworkErrorException {

        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        return execute(post);

    }

    /**
     * Posts a JSONObject question on the server in parameter Returns true if the question is valid, false if not
     * 
     * @param url
     *            URL of the server on which we want to post the question.
     * @param question
     *            The question that we want to post on the server.
     * @return boolean true if the server has received the Question
     */
    // TODO do so no code is repeated
    @Override
    public HttpResponse postJSONObject(String url, JSONObject question) {

        HttpPost post = new HttpPost(url);
        HttpResponse response = null;
        try {
            post.setEntity(new StringEntity(question.toString(STRING_ENTITY)));
            post.setHeader("Content-type", "application/json");
            response = execute(post);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }
}
