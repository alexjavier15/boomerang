/**
 * 
 */
package epfl.sweng.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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

    public static final int DATABASE_VERSION = 4;
    public static final String TABLE_NAME = "quizQuestions1";
    public static final String COLUMN_NAME_JSON_QUESTION = "jsonQuestion";
    public static final String COLUMN_NAME_TAGS = "questionTags";
    private static final int CULUMN_JSON_INDEX = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID
            + " INTEGER PRIMARY KEY," + COLUMN_NAME_JSON_QUESTION + TEXT_TYPE + ", " + COLUMN_NAME_TAGS + TEXT_TYPE
            + ", UNIQUE (" + COLUMN_NAME_JSON_QUESTION + ") " + "ON CONFLICT REPLACE" + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    // private String query = null;
    private Cursor queriedCursor = null;

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
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(this.getClass().getName(), "upgrading");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long addQuizQuestion(String question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_JSON_QUESTION, question);
        try {
            JSONObject parser = new JSONObject(question);
            List<String> tagArray = JSONParser.jsonArrayToList(parser.getJSONArray("tags"));
            StringBuffer tags = new StringBuffer();
            for (String g : tagArray) {
                tags.append(g + " ");
            }
            values.put(COLUMN_NAME_TAGS, tags.toString());
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
        }

        // Inserting Row
        long id = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        // db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public void deleteQuizQuestion(String index) {

        Log.v(getClass().getName(), "gAttempt to delete " + index);

        SQLiteDatabase db = this.getWritableDatabase();
        Log.v(getClass().getName(), "" + DatabaseUtils.queryNumEntries(db, TABLE_NAME));

        db.delete(TABLE_NAME, _ID + "= ?", new String[] {index});

        Log.v(getClass().getName(), "" + DatabaseUtils.queryNumEntries(db, TABLE_NAME));
        db.close();

    }

    public String[] getFirstPostQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cursorArray = new String[2];

        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, null, null, null, null,
                _ID + " ASC");
        if (cursor.moveToFirst()) {
            cursorArray[0] = String.valueOf(cursor.getInt(0));
            cursorArray[1] = cursor.getString(CULUMN_JSON_INDEX);

        }
        db.close();
        cursor.close();

        return cursorArray;

    }

    public String getRandomQuizQuestion() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, null, null, null, null,
                "RANDOM()", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            String quizQuestion = cursor.getString(CULUMN_JSON_INDEX);
            cursor.close();
            db.close();

            return quizQuestion;

        } else {
            db.close();
            return null;
        }
    }

    public String getQuestionbyID(long indexID) {
        String quizQuestion = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String selection = _ID + " = " + indexID;
        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, selection, null, null,
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            quizQuestion = cursor.getString(1);
        }
        cursor.close();
        db.close();

        return quizQuestion;
    }

    public List<Long> getQueriedQuestions(String queryS) throws UnsupportedOperationException, SQLiteException {
        List<Long> quList = new ArrayList<Long>();

        /*
         * if (queryS == null || !this.query.equals(queryS)) { if (queriedCursor != null) { queriedCursor.close(); }
         */
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = translateQuery(queryS);
        // this.query = queryS;
        queriedCursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, selection, null, null,
                null, "_ID DESC", null);

        // }
        queriedCursor.moveToFirst();

        while (!queriedCursor.isAfterLast()) {
            long id = queriedCursor.getLong(0);
            String st = queriedCursor.getString(1);
            Log.i(this.getDatabaseName() + "Question found: ", st);
            quList.add(id);
            queriedCursor.moveToNext();
        }
        db.close();
        queriedCursor.close();
        return quList;
    }

    private String translateQuery(String queryS) {
        Log.d("Query before trans was: ", queryS);
        StringBuffer translatedQuery = new StringBuffer();
        // saves a list of either words or logical operators
        Pattern pattern1 = Pattern.compile("([a-zA-Z0-9]+|[\\(\\)\\+\\*])");
        Matcher matcher1 = pattern1.matcher(queryS);        
        
        boolean lastWasWord = false;
        while (matcher1.find()) {
            if (matcher1.group(1).equals("(")) {
                translatedQuery.append("( ");
                lastWasWord = false;
            } else if (matcher1.group(1).equals(")")) {
                translatedQuery.append(")");
                lastWasWord = true;
            } else if (matcher1.group(1).equals("+")) {
                translatedQuery.append(" OR ");
                lastWasWord = false;
            } else if (matcher1.group(1).equals("*")) {
                translatedQuery.append(" AND ");
                lastWasWord = false;
            } else {
                if (lastWasWord) {
                    translatedQuery.append(" AND ");
                }
                translatedQuery.append(COLUMN_NAME_TAGS + " LIKE '%" + matcher1.group(1) + "%'");
                lastWasWord = true;
            }

        }
        return translatedQuery.toString();
    }

}
