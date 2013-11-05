/**
 * 
 */
package epfl.sweng.servercomm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.LinkedList;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;

import epfl.sweng.authentication.PreferenceKeys;
import epfl.sweng.authentication.SharedPreferenceManager;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.servercomm.CacheReaderContract.QuizQuestionTable.QuizQuestionDBHelper;
import epfl.sweng.tools.JSONParser;

import android.accounts.NetworkErrorException;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * @author Alex
 * 
 */
public final class CacheManagerService extends IntentService {
    private static final String CACHE_FILE_NAME = "cache";
    private static final String SYNC_FILE_NAME = "sync.tmp";
    private static SQLiteDatabase quizQuestionDB = null;

    public CacheManagerService() {
        super("CacheManagerService");
        String cacheDir = this.getBaseContext().getFilesDir().getPath();
        String syncFilePath = cacheDir + File.pathSeparator + SYNC_FILE_NAME;
        

        File syncfile = new File(syncFilePath);

        if (!syncfile.exists()) {

            try {
                openFileOutput(SYNC_FILE_NAME, MODE_PRIVATE);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        quizQuestionDB = openOrCreateDatabase(CACHE_FILE_NAME, MODE_PRIVATE, null);
  
        

    }

    /*
     * (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle dataBundled = intent.getExtras();
        Set<String> dataKeys = dataBundled.keySet();

        for (String name : dataKeys) {
            try {
                if (name.equals("getQuestion")) {
                    writeInDB(CACHE_FILE_NAME, dataBundled.get(name));
                } else if (name.equals("postQuestion")) {
                    writeDataFile(SYNC_FILE_NAME, dataBundled.get(name));
                } else if (name.equals("sync")) {
                    syncOfflineData();
                } else {
                    throw new IllegalArgumentException("The data Intent cannot be procced. Unkown data type");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NetworkErrorException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    /**
     * @param cacheFileName
     * @param object
     */
    private void writeInDB(String cacheFileName, Object object) {
   
        
    }

    /**
     * @throws IOException
     * @throws StreamCorruptedException
     * @throws JSONException
     * @throws NetworkErrorException
     * 
     */
    private void syncOfflineData() throws StreamCorruptedException, IOException, NetworkErrorException, JSONException {

        FileInputStream in = openFileInput(SYNC_FILE_NAME);
        ObjectInputStream questionReader = new ObjectInputStream(in);
        try {
            LinkedList<QuizQuestion> questionList = new LinkedList<QuizQuestion>();
            QuizQuestion questionObj = null;
            do {

                questionObj = (QuizQuestion) questionReader.readObject();
                questionList.add(questionObj);

            } while (questionObj.getQuestion() != null);

            int listsize = questionList.size();
            for (int i = 0; i < listsize; i++) {

                QuizQuestion question = questionList.remove();
                HttpResponse reponse = HttpComms.getInstance().postJSONObject(HttpComms.URLPUSH,
                    JSONParser.parseQuiztoJSON(question));

                if (reponse.getStatusLine().getStatusCode() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {

                    questionList.addFirst(question);
                    SharedPreferenceManager.getInstance().writeBooleaPreference(PreferenceKeys.ONLINE_MODE, false);
                    backSyncInFailure(questionList);
                    break;

                }

            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param questionList
     * @throws IOException
     */
    private void backSyncInFailure(LinkedList<QuizQuestion> questionList) throws IOException {
        deleteFile(SYNC_FILE_NAME);
        openFileOutput(SYNC_FILE_NAME, MODE_PRIVATE);
        for (QuizQuestion question : questionList) {

            writeDataFile(SYNC_FILE_NAME, question);

        }

    }
    

    private void writeDataFile(String fileName, Object data) throws IOException {

        FileOutputStream out = openFileOutput(fileName, MODE_APPEND);
        ObjectOutputStream questionWriter = new ObjectOutputStream(out);
        questionWriter.writeObject(data);

    }

}
