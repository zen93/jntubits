package in.momofactory.jntubits;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Akshay on 27-Sep-15.
 */
public class AppSettings {
    public static final String FIRST_LAUNCH = "in.momofactory.jntubits.first_launch";
    public static final String PREFERENCES = "in.momofactory.jntubits.prefs";
    public static final String REGULATION = "in.momofactory.jntubits.regulation";
    public static final String BRANCH = "in.momofactory.jntubits.branch";
    public static final String YEAR = "in.momofactory.jntubits.year";
    public static final String SEM = "in.momofactory.jntubits.sem";
    public static final String MID = "in.momofactory.jntubits.mid";
    public static final String LEARNING = "in.momofactory.jntubits.learning";
    public static final String REVISION = "in.momofactory.jntubits.revision";

    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;

    public AppSettings(Context context) {
        this.context = context;
        this.sp = this.context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        this.ed = sp.edit();
    }

    public void saveSettings(String regulation, String year, String branch, String sem, String mid, Boolean learning, Boolean revision ) {
        ed.putBoolean(FIRST_LAUNCH, false);
        ed.putString(REGULATION, regulation);
        ed.putString(YEAR, year);
        ed.putString(BRANCH, branch);
        ed.putString(SEM, sem);
        ed.putString(MID, mid);
        ed.putBoolean(LEARNING, learning);
        ed.putBoolean(REVISION, revision);
        ed.commit();
    }
    public void setMode(String mode) {
        if(mode.contentEquals(LEARNING)) {
            ed.putBoolean(LEARNING, true);
            ed.putBoolean(REVISION, false);
        } else if(mode.contentEquals(REVISION)) {
            ed.putBoolean(REVISION, true);
            ed.putBoolean(LEARNING, false);
        }
        ed.commit();
    }

    public void setMid(String mid) {
        ed.putString(MID, mid);
        ed.commit();
    }


    public boolean isFirstLaunch() {
        return sp.getBoolean(FIRST_LAUNCH, true);
    }

    public String getRegulation() {
        return sp.getString(REGULATION, "R09");
    }

    public String getBranch() {
        return sp.getString(BRANCH, "CSE");

    }

    public String getYear() {
        String yr = sp.getString(YEAR, "4");
        if(yr.contentEquals("I")) yr = "1";
        if(yr.contentEquals("II")) yr = "2";
        if(yr.contentEquals("III")) yr = "3";
        if(yr.contentEquals("IV")) yr = "4";
        return yr;
    }

    public String getSem() {
        String sem = sp.getString(SEM, "1");
        if(sem.contentEquals("---")) sem = "0";
        if(sem.contentEquals("I")) sem = "1";
        if(sem.contentEquals("II")) sem = "2";
        return sem;
    }

    public String getMid() {
        String mid = sp.getString(MID, "1");
        if(mid.contentEquals("Mid I")) mid = "1";
        if(mid.contentEquals("Mid II")) mid = "2";
        if(mid.contentEquals("Mid III")) mid = "3";
        return mid;

    }
    public String getMode() {
        String mode = "";
        boolean learning, revision;
        learning = sp.getBoolean(LEARNING, false);
        revision = sp.getBoolean(REVISION, false);

        if(learning) {
            mode = LEARNING;
        } else if(revision) {
            mode = REVISION;
        }

        return mode;
    }
}
