package epfl.sweng.showquestions;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import android.accounts.NetworkErrorException;
import epfl.sweng.servercomm.HttpComms;
import epfl.sweng.servercomm.HttpCommsProxy;

/**
 * @author LorenzoLeon
 * 
 */
public class RandomManager extends ConnectionManager {

        public static final String ERROR_MESSAGE = "There was an error retrieving the question";
        private ShowQuestionsActivity shower;

        public RandomManager(ShowQuestionsActivity show) {
                this.shower = show;
        }

        @Override
        public void processHttpReponse(HttpResponse response) {
                shower.parseResponse(response);
        }

        @Override
        public HttpResponse requete() {
                HttpResponse response = null;

                try {
                        response = HttpCommsProxy.getInstance().getHttpResponse(HttpComms.URL_SWENG_RANDOM_GET);
                } catch (ClientProtocolException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (NetworkErrorException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return response;
        }

        @Override
        public String getErrorMessage() {
                return ERROR_MESSAGE;
        }

}
