package in.momofactory.jntubits;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.*;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 19-Dec-15.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_PATH = "/data/data/in.momofactory.jntubits/databases/";
    private static final String DATABASE_NAME = "jntubitsdb";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BATCH = "batch";
    private static final String TABLE_MID = "mid";
    private static final String TABLE_BRANCH = "branch";
    private static final String TABLE_SEMESTER = "semester";
    private static final String TABLE_QUESTION = "question";
    private static final String TABLE_SUBJECT = "subject";
    private static final String TABLE_SUBJECT_SCORE = "subjectscore";
    private static final String TABLE_ANSWER = "answer";
    private static final String VIEW_SCORE = "score";
    public static final String TAG = "in.momofactory.JNTUBITS";

    private Context context;
    public SQLiteDatabase mydb;


    public DBHelper(Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        boolean dbexists = checkdatabase();
        if(dbexists) {
            try {
                opendatabase();
            }
            catch (SQLException e) {
                Log.e(TAG, "Cannot open db!");
            }
        }
        else {
            createdatabase();
        }
    }

    public void createdatabase() {
        boolean dbexists = checkdatabase();
        if(dbexists) {
            Log.i(TAG, "DB Exists");
        }
        else {
            this.getReadableDatabase();
            try {
                copydatabase();
            }
            catch (IOException e) {
                Log.e(TAG, "Error copying database!");
            }

        }

    }

    private boolean checkdatabase() {
        boolean checkdb = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            Log.e(TAG, "Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        InputStream myinput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        //String outfilename = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(DATABASE_PATH+DATABASE_NAME);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void opendatabase() throws SQLException {
        //Open the database
        String mypath = DATABASE_PATH + DATABASE_NAME;
        mydb = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }



    public synchronized void close() {
        if(mydb != null) {
            mydb.close();
        }
        super.close();
    }

    public void resetScore() {
        String query = "UPDATE Question SET answered = '0', correct = '0'";
        mydb.execSQL(query);
    }

    public void setAnswered(String questionId, String answered, String correct) {
        String query = "UPDATE Question SET Answered = '" + answered + "', Correct = '" + correct + "' WHERE QuestionId = '" + questionId + "'";
        mydb.execSQL(query);
    }

    public void updateSubjectScore(String subjectId) {
        String query = "SELECT MAX(SubjectScoreId) FROM subjectScore";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        int lastId;
        try {
            lastId = Integer.parseInt(cursor.getString(0)) + 1;
        }
        catch (CursorIndexOutOfBoundsException e) {
            lastId = 0;
        }
        query = "SELECT * FROM SubjectScore WHERE SubjectId = '"+subjectId + "'";
        cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        String result;
        try {
            result = cursor.getString(0);
            cursor.close();
        }
        catch (CursorIndexOutOfBoundsException e) {
            cursor.close();
            query = "INSERT INTO SubjectScore(SubjectScoreId, SubjectId, Score, Answered, Correct) VALUES ( '" + lastId +"' , '" + subjectId +"', '0', '0', '0' )";
            mydb.execSQL(query);
        }
        finally {
            cursor.close();
        }
        query = "UPDATE SubjectScore SET Answered = (SELECT SUM(Answered) FROM Question WHERE subjectId = '" + subjectId + "'), Correct = (SELECT SUM(Correct) FROM Question WHERE subjectId = '" + subjectId + "'), Score = ((SubjectScore.Correct*1.0/SubjectScore.Answered)*100) WHERE subjectId = '" + subjectId + "'";
        mydb.execSQL(query);
    }

    public String getSubjectScore(String subjectId) {
        String query = "SELECT Score FROM SubjectScore WHERE SubjectId = '"+subjectId + "'";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        String score =  cursor.getString(0);
        cursor.close();
        return score;
    }

    public void updateScore() {
        String query = "DROP VIEW `Score`";
        mydb.execSQL(query);
        query = "CREATE VIEW `Score` AS SELECT SUM(Score) AS total_score, SUM(Answered) AS total_answered, SUM(Correct) AS total_correct FROM SubjectScore";
        mydb.execSQL(query);
    }


    public String[] getScoreStats() {
        String query = "SELECT * FROM Score";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        String[] results = new String[3];
        for(int i=0;i<3;i++)
            results[i] =  cursor.getString(i);
        cursor.close();
        return results;
    }

    public String getSemId(String year, String semester) {
        String query = "SELECT semesterId FROM Semester WHERE year='"+year +"' AND semester='"+semester+"'";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor.getString(0);
    }

    public String getBranchId(String branchName) {
        String query = "SELECT branchId FROM Branch WHERE branchName='"+branchName +"'";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor.getString(0);
    }

    public String getBatchId(String semesterId, String branchId) {
        String query = "SELECT BatchId FROM Batch WHERE semesterid = '" + semesterId + "' AND branchid = '" + branchId + "'";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor.getString(0);
    }

    public String getMidId(String semesterId, String mid) {
        String query = "SELECT MidId FROM Mid WHERE semesterId = '" + semesterId + "' AND Mid = '" + mid + "'";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        return cursor.getString(0);
    }

    public List<String> getSubIds(String batchId) {
        String query = "SELECT SubjectId FROM Subject WHERE BatchId = '"+ batchId + "' ORDER BY SubjectId";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        List<String> subids = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            subids.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return subids;
    }

    public List<String> getSubjects(String batchId) {
        String query = "SELECT SubjectName FROM Subject WHERE BatchId = '"+ batchId + "' ORDER BY SubjectId";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        List<String> subs = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            subs.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return subs;
    }

    public List<String> getQuestionIds(String subId, String midId, String type) {
        String query = "SELECT QuestionId FROM Question WHERE SubjectId = '"+ subId + "' AND MidId = '" + midId + "' AND Type = '"+ type +"' ORDER BY QuestionId";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        List<String> questionids = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            questionids.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return questionids;
    }

    public List<String> getQuestions(String subId, String midId, String type) {
        String query = "SELECT Question FROM Question WHERE SubjectId = '"+ subId + "' AND MidId = '" + midId + "' AND Type = '"+ type +"'  ORDER BY QuestionId";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        List<String> questions = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            questions.add(cursor.getString(0).replace(":b:", "_____"));
            cursor.moveToNext();
        }
        return questions;
    }

    public String getQuestion(String qId) {
        String query = "SELECT Question FROM Question WHERE QuestionId = '"+ qId + "' ORDER BY QuestionId";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        String question = "";
        while(!cursor.isAfterLast()) {
            question = (cursor.getString(0).replace(":b:", "_____"));
            cursor.moveToNext();
        }
        return question;
    }


    public List<String> getAnswers(String qId) {
        String query = "SELECT Answer FROM Answer WHERE QuestionId = '"+ qId +"'  ORDER BY AnswerId";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        List<String> answers = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            answers.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return answers;
    }

    public List<String> getAnswerIds(String qId) {
        String query = "SELECT AnswerId FROM Answer WHERE QuestionId = '"+ qId +"'  ORDER BY AnswerId";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        List<String> answerids = new ArrayList<String>();
        while(!cursor.isAfterLast()) {
            answerids.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return answerids;
    }

    public String getCorrectAnswer(String qId) {
        String query = "SELECT Answer FROM Answer WHERE QuestionId = '"+ qId +"' AND Correct = '1'";
        Cursor cursor = mydb.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        String correctAnswer = "";
        while(!cursor.isAfterLast()) {
            correctAnswer = (cursor.getString(0));
            cursor.moveToNext();
        }
        return correctAnswer;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
