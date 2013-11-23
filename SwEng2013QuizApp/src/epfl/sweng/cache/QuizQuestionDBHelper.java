/**
 * 
 */
package epfl.sweng.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import epfl.sweng.tools.JSONParser;

/**
 * @author Alex
 * 
 */

public class QuizQuestionDBHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "quizQuestions1";
    public static final String COLUMN_NAME_JSON_QUESTION = "jsonQuestion";
    public static final String COLUMN_NAME_TAGS = "questionTags";
    public static final String COLUMN_NAME_IXHASH = "indexHash";
    private static final int CULUMN_JSON_INDEX = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID
            + " INTEGER PRIMARY KEY," + COLUMN_NAME_JSON_QUESTION + TEXT_TYPE + ", " + ", " + COLUMN_NAME_TAGS
            + TEXT_TYPE + COLUMN_NAME_IXHASH + TEXT_TYPE + ", UNIQUE (" + COLUMN_NAME_JSON_QUESTION + ") "
            + "ON CONFLICT REPLACE" + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private int last = -1;

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public QuizQuestionDBHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite .SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(this.getClass().getName(), "upgrading");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void addQuizQuestion(String question, int indexHash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_JSON_QUESTION, question);
        try {
            JSONObject parser = new JSONObject(question);
            List<String> tagArray = JSONParser.jsonArrayToList(parser.getJSONArray("tags"));
            String tags = "";
            for (String g : tagArray) {
                tags += g + " ";
            }
            values.put(COLUMN_NAME_TAGS, tags);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.put(COLUMN_NAME_IXHASH, indexHash);

        // Inserting Row
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String getRandomQuizQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, null, null, null, null,
                "RANDOM()", "1");
        if (cursor != null) {
            cursor.moveToFirst();
            String quizQuestion = cursor.getColumnName(1);
            db.close();
            return quizQuestion;
        } else {
            db.close();
            return null;
        }
    }

    public String getFirstPostQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, null, null, null, null,
                _ID + " ASC");
        if (cursor.moveToFirst()) {
            last = cursor.getInt(0);
            Log.d(this.getClass().getName(),
                    "showing question name for debug: " + cursor.getString(CULUMN_JSON_INDEX) + " and idk _ " + last);

            return cursor.getColumnName(1);
        } else {
            last = -1;
            Log.d(this.getClass().getName(), "nomore question to sync");

            return null;
        }
    }

    public void deleteQuizQuestion() {
        Log.d(this.getClass().getName(), "gAttempt to delete " + last);
        if (last != -1) {

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, _ID + " = ?", new String[] {String.valueOf(last)});
            db.close();
        }
    }

    public String getHashedQuestion(int indexHash) {
        String quizQuestion = null;
        
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_NAME_IXHASH + " = " + indexHash;
        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, selection, null, null,
                null, "RANDOM()", "1");
        if (!cursor.isAfterLast()) {
            cursor.moveToFirst();
            quizQuestion = cursor.getColumnName(1);
        }
        db.close();

        return quizQuestion;
    }

    public List<String> getQueriedQuestions(String query) throws UnsupportedOperationException, SQLiteException {
        List<String> quList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = translateQuery(query);

        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, selection, null, null,
                null, "_ID DESC", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String st = cursor.getString(1);
            Log.i(this.getDatabaseName() + "Question found: ", st);
            quList.add(st);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return quList;
    }

    private String translateQuery(String query) {
        Log.d("Query before trans was: ", query);
        String translatedQuery = "";
        // saves a list of either words or logical operators
        Pattern pattern1 = Pattern.compile("([a-zA-Z0-9]+|[\\(\\)\\+\\*])");
        Matcher matcher1 = pattern1.matcher(query);

        boolean lastWasWord = false;
        while (matcher1.find()) {
            if (matcher1.group(1).equals("(")) {
                translatedQuery += "( ";
                lastWasWord = false;
            } else if (matcher1.group(1).equals(")")) {
                translatedQuery += ")";
                lastWasWord = true;
            } else if (matcher1.group(1).equals("+")) {
                translatedQuery += " OR ";
                lastWasWord = false;
            } else if (matcher1.group(1).equals("*")) {
                translatedQuery += " AND ";
                lastWasWord = false;
            } else {
                if (lastWasWord) {
                    translatedQuery += " AND ";
                }
                translatedQuery += COLUMN_NAME_TAGS + " LIKE '%" + matcher1.group(1) + "%'";
                lastWasWord = true;
            }

        }
        Log.d("query after trans is : ", translatedQuery);

        return translatedQuery;
    }

}
