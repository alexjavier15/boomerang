package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;


import android.os.AsyncTask;
/**
 * 
 * @author LorenzoLeon
 *
 */
public class HttpCommsBackgroundTask extends AsyncTask<Void, Void, HttpResponse> {

    private HttpcommunicationsAdapter adapter;
    private Boolean doPostExecute = true;

    public HttpCommsBackgroundTask(HttpcommunicationsAdapter adapt, Boolean doPost) {
        super();
        this.adapter = adapt;
        this.doPostExecute = doPost;
    }

    public HttpCommsBackgroundTask(HttpcommunicationsAdapter adapt) {
        this(adapt, true);
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

        if (doPostExecute) {
            adapter.processHttpReponse(result);
        }

    }

}