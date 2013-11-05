/**
 * 
 */
package epfl.sweng.servercomm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;
import epfl.sweng.quizquestions.QuizQuestion;
import epfl.sweng.tools.Debug;

/**
 * @author Alex
 * 
 */

public class QuizQuestionDBHelper extends SQLiteOpenHelper implements BaseColumns {

    public enum ColumnPos {
        IDK, ID, QUESTION, ANSWERS, SOLUTION, TAGS, OWNER

    }

    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "quizQuestions";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_QUESTION = "question";
    public static final String COLUMN_NAME_ANSWERS = "answers";
    public static final String COLUMN_NAME_SOLUTION = "solution";
    public static final String COLUMN_NAME_TAGS = "tags";
    public static final String COLUMN_NAME_OWNER = "owner";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID
            + " INTEGER PRIMARY KEY," + COLUMN_NAME_ID + " INTEGER" + COMMA_SEP + COLUMN_NAME_QUESTION + TEXT_TYPE
            + COMMA_SEP + COLUMN_NAME_ANSWERS + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_SOLUTION + " INTEGER,"
            + COLUMN_NAME_TAGS + TEXT_TYPE + COMMA_SEP + COLUMN_NAME_OWNER + TEXT_TYPE + " )";

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

        // TODO Auto-generated constructor stub
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
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }

    public void addQuizQuestion(QuizQuestion quizQuestion) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_QUESTION, quizQuestion.getQuestion());
        values.put(COLUMN_NAME_ID, quizQuestion.getID());
        String answersString = TextUtils.join(COMMA_SEP, quizQuestion.getAnswers());
        values.put(COLUMN_NAME_ANSWERS, answersString);
        values.put(COLUMN_NAME_SOLUTION, quizQuestion.getIndex());
        String tagsString = TextUtils.join(COMMA_SEP, quizQuestion.getTags());
        values.put(COLUMN_NAME_TAGS, tagsString);
        values.put(COLUMN_NAME_OWNER, quizQuestion.getOwner());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public QuizQuestion getRandomQuizQuestion() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_ID, COLUMN_NAME_QUESTION,
            COLUMN_NAME_ANSWERS, COLUMN_NAME_SOLUTION, COLUMN_NAME_TAGS, COLUMN_NAME_OWNER}, null, null, null, null,
                "RANDOM()", "1");
        if (cursor != null) {
            cursor.moveToFirst();

            List<String> answerList = Arrays.asList(cursor.getString(ColumnPos.ANSWERS.ordinal()).split(COMMA_SEP));
            Set<String> tagsSet = new HashSet<String>(Arrays.asList(cursor.getString(ColumnPos.TAGS.ordinal()).split(
                    COMMA_SEP)));

            return new QuizQuestion(cursor.getString(ColumnPos.QUESTION.ordinal()), answerList,
                    cursor.getInt(ColumnPos.SOLUTION.ordinal()), tagsSet, cursor.getInt(ColumnPos.ID.ordinal()),
                    cursor.getString(ColumnPos.OWNER.ordinal()));

        } else {

            return null;
        }
    }

    public QuizQuestion getFirstPostQuestion() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {_ID, COLUMN_NAME_ID, COLUMN_NAME_QUESTION,
            COLUMN_NAME_ANSWERS, COLUMN_NAME_SOLUTION, COLUMN_NAME_TAGS, COLUMN_NAME_OWNER}, null, null, null, null,
                _ID + " ASC");
        if (cursor.moveToFirst()) {
            List<String> answerList = Arrays.asList(cursor.getString(ColumnPos.ANSWERS.ordinal()).split(COMMA_SEP));
            Set<String> tagsSet = new HashSet<String>(Arrays.asList(cursor.getString(ColumnPos.TAGS.ordinal()).split(
                    COMMA_SEP)));
            last = cursor.getInt(ColumnPos.IDK.ordinal());
            Debug.out("showing question name for debug: " + cursor.getString(ColumnPos.QUESTION.ordinal())
                    + " and idk _ " + last);
            return new QuizQuestion(cursor.getString(ColumnPos.QUESTION.ordinal()), answerList,
                    cursor.getInt(ColumnPos.SOLUTION.ordinal()), tagsSet, cursor.getInt(ColumnPos.ID.ordinal()),
                    cursor.getString(ColumnPos.OWNER.ordinal()));
        } else {
            last = -1;
            Debug.out("nomore question to sync");

            return null;
        }
    }

    public void deleteQuizQuestion(QuizQuestion quizQuestion) {
        Debug.out("gAttempt to delete " + last);
        if (last != -1) {

            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME, _ID + " = ?", new String[] {String.valueOf(last)});
            db.close();
        }
    }

}
