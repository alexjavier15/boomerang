/**
 * 
 */
package epfl.sweng.servercomm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import epfl.sweng.quizquestions.QuizQuestion;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * @author Alex
 * 
 */
public final class CacheReaderContract {

    public CacheReaderContract() {
    }

    public static abstract class QuizQuestionTable implements BaseColumns {
       
        public static final String TABLE_NAME = "quizQuestions";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_QUESTION = "question";
        public static final String COLUMN_NAME_ANSWERS = "answers";
        public static final String COLUMN_NAME_SOLUTION = "solution";
        public static final String COLUMN_NAME_TAGS = "tags";
        public static final String COLUMN_NAME_OWNER = "owner";
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + QuizQuestionTable.TABLE_NAME + " ("
            + QuizQuestionTable._ID + " INTEGER PRIMARY KEY," + QuizQuestionTable.COLUMN_NAME_ID + TEXT_TYPE
            + COMMA_SEP + QuizQuestionTable.COLUMN_NAME_QUESTION + TEXT_TYPE + COMMA_SEP
            + QuizQuestionTable.COLUMN_NAME_ANSWERS + TEXT_TYPE + COMMA_SEP + QuizQuestionTable.COLUMN_NAME_SOLUTION
            + " INTEGER," + QuizQuestionTable.COLUMN_NAME_TAGS + TEXT_TYPE + COMMA_SEP
            + QuizQuestionTable.COLUMN_NAME_OWNER + TEXT_TYPE + COMMA_SEP + " )";

        private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + QuizQuestionTable.TABLE_NAME;

        public class QuizQuestionDBHelper extends SQLiteOpenHelper {

            public static final int DATABASE_VERSION = 2;
            public static final String DATABASE_NAME = "Cache.db";

            /**
             * @param context
             * @param name
             * @param factory
             * @param version
             */
            public QuizQuestionDBHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);

                // TODO Auto-generated constructor stub
            }

            /*
             * (non-Javadoc)
             * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
             */
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_ENTRIES);

            }

            /*
             * (non-Javadoc)
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
                values.put(COLUMN_NAME_ID, Long.toString(quizQuestion.getID()));
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

                Cursor cursor = db.query(TABLE_NAME, new String[] {COLUMN_NAME_ID, COLUMN_NAME_QUESTION,
                    COLUMN_NAME_ANSWERS, COLUMN_NAME_SOLUTION, COLUMN_NAME_TAGS, COLUMN_NAME_OWNER}, "*", null, null,
                    null, "RANDOM()", "1");
                if (cursor != null) {

                    List<String> answerList = Arrays.asList(cursor.getString(2).split(COMMA_SEP));
                    Set<String> tagsSet = new HashSet<String>(Arrays.asList(cursor.getString(4).split(COMMA_SEP)));

                    return new QuizQuestion(cursor.getString(1), answerList, cursor.getInt(3), tagsSet,
                        Integer.valueOf(cursor.getString(5)), cursor.getString(6));

                } else {

                    return null;
                }
            }
        }

    }

}
