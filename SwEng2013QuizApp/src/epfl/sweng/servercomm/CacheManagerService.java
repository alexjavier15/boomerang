/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */
public final class CacheManagerService extends Service {

    public static final String QUESTION_CACHE_DB_NAME = "Cache.db";
    public static final String POST_SYNC_DB_NAME = "PostSync.db";

    private static QuizQuestionDBHelper sQuizQuestionDB;
    private static QuizQuestionDBHelper sPostQuestionDB;

    private final IBinder mBinder = new CacheServiceBinder();

    public CacheManagerService() {
        sQuizQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), QUESTION_CACHE_DB_NAME);
        sPostQuestionDB = new QuizQuestionDBHelper(QuizApp.getContexStatic(), POST_SYNC_DB_NAME);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mBinder;
    }

    public class CacheServiceBinder extends Binder {

        public CacheManagerService getService() {
            return CacheManagerService.this;
        }

    }

    /**
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public HttpResponse getRandomQuestion() throws IOException, JSONException {

        HttpResponse reponse = null;
        QuizQuestion quizQuestion = null;
        DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();

        quizQuestion = sQuizQuestionDB.getRandomQuizQuestion();
        if (quizQuestion != null) {
            JSONObject questionObj = JSONParser.parseQuiztoJSON(quizQuestion);
            reponse = httpResFactory.newHttpResponse(new BasicStatusLine((new HttpPost()).getProtocolVersion(),
                    HttpStatus.SC_OK, null), null);
            reponse.setEntity(new StringEntity(questionObj.toString(HttpComms.STRING_ENTITY)));
        } else {
            reponse = httpResFactory.newHttpResponse(new BasicStatusLine((new HttpPost()).getProtocolVersion(),
                    HttpStatus.SC_INTERNAL_SERVER_ERROR, null), null);
        }

        return reponse;
    }

    /**
     * @param quizQuestion
     * @return
     */
    public HttpResponse addQuestionForSync(QuizQuestion quizQuestion) {

        sPostQuestionDB.addQuizQuestion(quizQuestion);

        DefaultHttpResponseFactory httpResFactory = new DefaultHttpResponseFactory();
        HttpResponse reponse = httpResFactory.newHttpResponse(new BasicStatusLine(
                (new HttpPost()).getProtocolVersion(), HttpStatus.SC_CREATED, null), null);

        return reponse;

    }

    public boolean syncPostCachedQuestions() throws ClientProtocolException, NetworkErrorException, IOException,
            JSONException {
        QuizQuestion quizQuestion = sPostQuestionDB.getFirstPostQuestion();
        while (quizQuestion != null) {
            HttpResponse reponse = HttpComms.getInstance().postJSONObject(HttpComms.URLPUSH,
                    JSONParser.parseQuiztoJSON(quizQuestion));
            if (reponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                sPostQuestionDB.deleteQuizQuestion(quizQuestion);
                quizQuestion = sPostQuestionDB.getFirstPostQuestion();

            } else if (reponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                return false;
            } else {

                throw new ClientProtocolException("Unexpected Error submiting question from database." + " "
                        + reponse.getStatusLine());
            }

        }
        return true;

    }

    /**
     * @param quizQuestion
     */
    public void pushFetchedQuestion(QuizQuestion quizQuestion) {
        sQuizQuestionDB.addQuizQuestion(quizQuestion);

    }

}
