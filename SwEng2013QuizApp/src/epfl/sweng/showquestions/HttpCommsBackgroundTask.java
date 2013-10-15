package epfl.sweng.showquestions;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import epfl.sweng.servercomm.HttpcommunicationsAdapter;

import android.os.AsyncTask;

public class HttpCommsBackgroundTask extends AsyncTask<Void, Void, HttpResponse> {

    private HttpcommunicationsAdapter adapter;
    private Boolean onPostExecute = true;

    public HttpCommsBackgroundTask(HttpcommunicationsAdapter adapter, Boolean onPostExecute) {
        super();
        this.adapter = adapter;
        this.onPostExecute = onPostExecute;
    }

    public HttpCommsBackgroundTask(HttpcommunicationsAdapter adapter) {

        this(adapter, true);
    }

    /**
     * Getting the question on the server asynchronously. Called by execute().
     */
    @Override
    protected HttpResponse doInBackground(Void... arg) {

        HttpResponse response = null;

        try {
            response = adapter.requete();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Send the informations of the fetched random question. Called by execute() right after doInBackground().
     */
    @Override
    protected void onPostExecute(HttpResponse result) {
        if (onPostExecute) {
            adapter.processHttpReponse(result);
        }

    }
}