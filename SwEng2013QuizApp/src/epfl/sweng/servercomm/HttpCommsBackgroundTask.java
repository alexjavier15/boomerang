package epfl.sweng.servercomm;

import org.apache.http.HttpResponse;

import android.os.AsyncTask;

/**
 * 
 * @author LorenzoLeon
 * 
 */
public class HttpCommsBackgroundTask extends AsyncTask<Void, Void, HttpResponse> {

    private HttpcommunicationsAdapter adapter;
    private Boolean doPostExecute = true;

    public HttpCommsBackgroundTask(HttpcommunicationsAdapter adapt) {
        this(adapt, true);
    }

    public HttpCommsBackgroundTask(HttpcommunicationsAdapter adapt, Boolean doPost) {
        super();
        adapter = adapt;
        doPostExecute = doPost;
    }

    /**
     * Getting the question on the server asynchronously. Called by execute().
     */
    @Override
    protected HttpResponse doInBackground(Void... arg) {

        HttpResponse response = null;

        response = adapter.requete();

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