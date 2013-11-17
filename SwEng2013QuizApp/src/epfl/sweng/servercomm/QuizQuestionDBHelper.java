/**
 * 
 */
package epfl.sweng.servercomm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import epfl.sweng.tools.Debug;

/**
 * @author Alex
 * 
 */

public class QuizQuestionDBHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "quizQuestions";
    public static final String COLUMN_NAME_JSON_QUESTION = "jsonQuestion";
    private static final int CULUMN_JSON_INDEX = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID
            + " INTEGER PRIMARY KEY," + COLUMN_NAME_JSON_QUESTION + TEXT_TYPE + " )";

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
        Debug.out("upgrading");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void addQuizQuestion(String question) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_JSON_QUESTION, question);

        // Inserting Row
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
            Debug.out("showing question name for debug: " + cursor.getString(CULUMN_JSON_INDEX) + " and idk _ "
                    + last);

            return cursor.getString(CULUMN_JSON_INDEX);
        } else {
            last = -1;
            Debug.out("nomore question to sync");

            return null;
        }
    }

    public void deleteQuizQuestion() {
        
        Debug.out("gAttempt to delete " + last);
        if (last != -1) {

            SQLiteDatabase db = this.getWritableDatabase();
            Debug.out(DatabaseUtils.queryNumEntries(db, TABLE_NAME));
            db.delete(TABLE_NAME, _ID + "=?", new String[] {String.valueOf(last)});
            Debug.out(DatabaseUtils.queryNumEntries(db, TABLE_NAME));
            db.close();
        }
    }

}
