package in.momofactory.jntubits;

import android.content.Context;
import android.database.sqlite.*;
/**
 * Created by Akshay on 19-Dec-15.
 */
public class DBHelperOld extends SQLiteOpenHelper {
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



    public DBHelperOld(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BRANCH_TABLE = "CREATE TABLE IF NOT EXISTS `Branch` " +
                "( `BranchId` INT NOT NULL PRIMARY KEY," +
                "  `BranchName` VARCHAR(100) NULL);";

        String CREATE_SEMESTER_TABLE = "CREATE TABLE IF NOT EXISTS `Semester` (" +
                "  `SemesterId` INT NOT NULL  ," +
                "  `Year` SMALLINT(1) NULL ," +
                "  `Semester` SMALLINT(1) NULL ," +
                "  PRIMARY KEY (`SemesterId`)  );";

        String CREATE_MID_TABLE = "CREATE TABLE IF NOT EXISTS `Mid` (" +
                "  `MidId` INT NOT NULL  ," +
                "  `SemesterId` INT NULL ," +
                "  `Mid` SMALLINT(1) NULL ," +
                "  PRIMARY KEY (`MidId`)  ," +
                "  CONSTRAINT `SemesterId`" +
                "    FOREIGN KEY (`SemesterId`)" +
                "    REFERENCES `Semester` (`SemesterId`)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE);";

        String CREATE_BATCH_TABLE = "CREATE TABLE IF NOT EXISTS `Batch` (" +
                "  `BatchId` INT NOT NULL  ," +
                "  `SemesterId` INT NULL ," +
                "  `BranchId` INT NULL ," +
                "  PRIMARY KEY (`BatchId`)  ," +
                "  CONSTRAINT `Branch`" +
                "    FOREIGN KEY (`BranchId`)" +
                "    REFERENCES `Branch` (`BranchId`)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE," +
                "  CONSTRAINT `Sem`" +
                "    FOREIGN KEY (`SemesterId`)" +
                "    REFERENCES `Semester` (`SemesterId`)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE" +
                ");";

        String CREATE_SUBJECT_TABLE = "CREATE TABLE IF NOT EXISTS `Subject` (" +
                "  `SubjectId` INT NOT NULL  ," +
                "  `BatchId` INT NULL ," +
                "  `SubjectName` VARCHAR(255) NOT NULL ," +
                "  PRIMARY KEY (`SubjectId`)  ," +
                "  CONSTRAINT `BatchId`" +
                "    FOREIGN KEY (`BatchId`)" +
                "    REFERENCES `Batch` (`BatchId`)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE);";

        String CREATE_QUESTION_TABLE = "CREATE TABLE IF NOT EXISTS `Question` (" +
                "  `QuestionId` INT NOT NULL  ," +
                "  `SubjectId` INT NULL ," +
                "  `MidId` INT NULL ," +
                "  `Question` VARCHAR(255) NULL ," +
                "  `Answered` TINYINT(1) NULL ," +
                "  `Correct` TINYINT(1) NULL ," +
                "  `Type` TINYINT(1) NULL ," +
                "  PRIMARY KEY (`QuestionId`)  ," +
                "  CONSTRAINT `Mid_id`" +
                "    FOREIGN KEY (`MidId`)" +
                "    REFERENCES `Mid` (`MidId`)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE," +
                "  CONSTRAINT `Subject`" +
                "    FOREIGN KEY (`SubjectId`)" +
                "    REFERENCES `Subject` (`SubjectId`)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE);";

        String CREATE_ANSWER_TABLE = "CREATE TABLE IF NOT EXISTS `Answer` (" +
                "  `AnswerId` INT NOT NULL  ," +
                "  `QuestionId` INT NULL ," +
                "  `Answer` VARCHAR(100) NULL ," +
                "  `Correct` TINYINT(1) NULL ," +
                "  PRIMARY KEY (`AnswerId`)  ," +
                "  CONSTRAINT `QuestionId`" +
                "    FOREIGN KEY (`QuestionId`)" +
                "    REFERENCES `Question` (`QuestionId`)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE);";

        String CREATE_SUBJECT_SCORE_TABLE = "CREATE TABLE IF NOT EXISTS `SubjectScore` (" +
                "  `SubjectScoreId` INT NOT NULL  ," +
                "  `SubjectId` INT NULL ," +
                "  `Score` INT NULL ," +
                "  `Answered` INT NULL ," +
                "  `Correct` INT NULL ," +
                "  PRIMARY KEY (`SubjectScoreId`)  ," +
                "  CONSTRAINT `subject_id`" +
                "    FOREIGN KEY (`SubjectId`)" +
                "    REFERENCES `Subject` (`SubjectId`)" +
                "    ON DELETE CASCADE" +
                "    ON UPDATE CASCADE);";

        String CREATE_SCORE_VIEW = "CREATE VIEW `Score` " +
                "AS SELECT SUM(Score) AS total_score, SUM(Answered) AS total_answered, SUM(Correct) AS total_correct " +
                "FROM SubjectScore;";


        db.execSQL(CREATE_BRANCH_TABLE);
        db.execSQL(CREATE_SEMESTER_TABLE);
        db.execSQL(CREATE_MID_TABLE);
        db.execSQL(CREATE_BATCH_TABLE);
        db.execSQL(CREATE_SUBJECT_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
        db.execSQL(CREATE_ANSWER_TABLE);
        db.execSQL(CREATE_SUBJECT_SCORE_TABLE);
        db.execSQL(CREATE_SCORE_VIEW);

        //Add default data
        String ADD_BRANCH = "INSERT INTO Branch VALUES ('1', 'CSE'), ('2', 'MECH'), ('3', 'ECE')";
        String ADD_SEMESTER = "INSERT INTO Semester VALUES (1, 1, 0), (2, 2, 1), (3, 2, 2), (4, 3, 1), (5, 3, 2), (6, 4, 1), (7, 4, 2)";
        String ADD_MID = "INSERT INTO Mid VALUES (1, 1, 1), (2,1,2), (3,1,3), (4,2,1), (5,2,2), (6,3,1), (7,3,2), (8,4,1), (9,4,2), (10,5,1), (11,5,2), (12,6,1), (13,6,2), (14,7,1), (15,7,2)";
        String ADD_BATCH = "INSERT INTO Batch VALUES (1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 2, 1), (5, 2, 2), (6, 2, 3), (7, 3, 1), (8, 3, 2), (9, 3, 3), (10, 4, 1), (11, 4, 2), (12, 4, 3), (13, 5, 1), (14, 5, 2), (15, 5, 3), (16, 6, 1), (17, 6, 2), (18, 6, 3), (19, 7, 1), (20, 7, 2), (21, 7, 3)";
        String ADD_SUBJECTS = "INSERT INTO Subject VALUES (1, 1, 'English') , (2, 1, 'Mathematics - 1') , (3, 1, 'Mathematical Methods') , (4, 1, 'Engineering Physics') , (5, 1, 'Engineering Chemistry') , (6, 1, 'Computer Programming') , (7, 4, 'Probability and Statistics') , (8, 4, 'Mathematical Foundations of Computer Science') , (9, 4, 'Data Structures') , (10, 4, 'Digital Logic Design') , (11, 4, 'Basic Electronic Devices and Circuits') , (12, 4, 'Electrical Engineering') , (13, 7, 'Computer Organization') , (14, 7, 'Database Management Systems') , (15, 7, 'Java Programming') , (16, 7, 'Environmental studies') , (17, 7, 'Formal Languages and Automata Theory') , (18, 7, 'Design and Analysis of Algorithms') , (19, 10, 'Principles of Programming Languages') , (20, 10, 'Disaster Management') , (21, 10, 'Software Engineering') , (22, 10, 'Compiler Design') , (23, 10, 'Operating Systems') , (24, 10, 'Computer Networks') , (25, 13, 'Distributed Systems') , (26, 13, 'Information Security') , (27, 13, 'Object Oriented Analysis and Design') , (28, 13, 'Software Testing Methodologies') , (29, 13, 'Managerial Economics and Financial Analysis') , (30, 13, 'Web Technologies') , (31, 16, 'linux programming') , (32, 16, 'design patterns') , (33, 16, 'Data Warehousing and Data Mining') , (34, 16, 'Cloud Computing') , (35, 16, 'Computer Graphics') , (36, 16, 'Computer Graphics') , (37, 19, 'Web Services') , (38, 19, 'Database Security') , (39, 19, 'Storage Area Networks');";


        db.execSQL(ADD_BRANCH);
        db.execSQL(ADD_SEMESTER);
        db.execSQL(ADD_MID);
        db.execSQL(ADD_BATCH);
        db.execSQL(ADD_SUBJECTS);

        //Add Questions and stuff
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BATCH );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWER );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEMESTER );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECT_SCORE );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MID );
        db.execSQL("DROP VIEW IF EXISTS " + VIEW_SCORE );

        onCreate(db);
    }
}
