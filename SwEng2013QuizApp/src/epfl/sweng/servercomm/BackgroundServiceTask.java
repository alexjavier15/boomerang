/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.accounts.NetworkErrorException;
import android.os.AsyncTask;

/**
 * @author Alex
 * 
 */
public class BackgroundServiceTask extends AsyncTask<CacheManagerService, Boolean, Boolean> {

    private CacheManagerService mService;

    /**
     * @param service
     */
    public BackgroundServiceTask(CacheManagerService service) {
        super();
        mService = service;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Boolean doInBackground(CacheManagerService... params) {
        boolean res = false;
        try {
            res = mService.syncPostCachedQuestions();
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
        return res;
    }

}
