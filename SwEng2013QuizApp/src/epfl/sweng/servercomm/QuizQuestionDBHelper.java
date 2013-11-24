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

    public static final String COLUMN_NAME_JSON_QUESTION = "jsonQuestion";
    public static final int CULUMN_JSON_INDEX = 1;
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "quizQuestions";
    public static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID
            + " INTEGER PRIMARY KEY," + COLUMN_NAME_JSON_QUESTION + TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

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

    public void deleteQuizQuestion(String index) {

        Debug.out("gAttempt to delete " + index);

        SQLiteDatabase db = this.getWritableDatabase();
        Debug.out(DatabaseUtils.queryNumEntries(db, TABLE_NAME));

        db.delete(TABLE_NAME, _ID + "=?", new String[] {index});

        Debug.out(DatabaseUtils.queryNumEntries(db, TABLE_NAME));
        db.close();

    }

    public String[] getFirstPostQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cursorArray = new String[2];

        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, null, null, null, null,
                _ID + " ASC");
        if (cursor.moveToFirst()) {
            cursorArray[0] = String.valueOf(cursor.getInt(CULUMN_JSON_INDEX));
            cursorArray[1] = cursor.getString(CULUMN_JSON_INDEX);

        }
        return cursorArray;

    }

    public String getRandomQuizQuestion() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_JSON_QUESTION}, null, null, null, null,
                "RANDOM()", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            String quizQuestion = cursor.getString(CULUMN_JSON_INDEX);
            db.close();

            return quizQuestion;

        } else {
            db.close();
            return null;
        }
    }

}
