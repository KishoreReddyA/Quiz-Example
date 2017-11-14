package compindia.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by compindi on 25-10-2017.
 */

public class Databse extends SQLiteOpenHelper
{
public  static final int DATABASE_VERSION=1;
public  static final String DATABASE_NAME="QuizDatabase";
    public static final String QUIZ_TABLE="quiz_Table";
    public static final String QUESTION_NUMBER="Question_Number";
    public static final String QUESTION="Question";
    public static final String OPTION1="Option1";
    public static final String OPTION2="Option2";
    public static final String OPTION3="Option3";
    public static final String OPTION4="Option4";
    public static final String ANSWER="Answer";

    public Databse(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + QUIZ_TABLE + "(" + QUESTION_NUMBER + " INTEGER," + QUESTION + " TEXT,"
                + OPTION1 + " TEXT," + OPTION2 + " TEXT," + OPTION3 + " TEXT,"+OPTION4+" TEXT,"+ANSWER+" TEXT)";
        db.execSQL(CREATE_QUIZ_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OLD_VERSION, int NEW_VERSION)
    {

    }
    public boolean createQuestion(int questionNumber,String question,String option1,String option2,String option3,String option4,String answer)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(QUESTION_NUMBER,questionNumber);
        values.put(QUESTION,question);
        values.put(OPTION1,option1);
        values.put(OPTION2,option2);
        values.put(OPTION3,option3);
        values.put(OPTION4,option4);
        values.put(ANSWER,answer);
        long n= db.insert(QUIZ_TABLE,null,values);
        db.close();
        if (n>0) {
            return true;
        }
        return false;
    }
    public Cursor getQuestions(int questionNum)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * from "+QUIZ_TABLE+" WHERE "+QUESTION_NUMBER+"=?",new String[]{String.valueOf(questionNum)});
        return cursor;
    }
    public int getTableCount()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+QUIZ_TABLE,null);
        return cursor.getCount();
    }
    public int deleteQuiz()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Delete  from "+QUIZ_TABLE,null);
        return cursor.getCount();
    }

    public Cursor getAnswers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select " + ANSWER + " From " + QUIZ_TABLE, null);
        return cursor;
    }
}
