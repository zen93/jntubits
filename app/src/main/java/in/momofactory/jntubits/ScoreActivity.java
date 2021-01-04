package in.momofactory.jntubits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        LinearLayout ScoreLL = (LinearLayout) findViewById(R.id.ScoreActivityLL);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Intent i = getIntent();
        String batchId = i.getStringExtra("batchId");

        try {
            final DBHelper dbHelper = new DBHelper(getApplicationContext());

            List<String> subIds, subjects;
            String[] subjectIds, subjectNames;
            TextView[] subTV;

            subIds = dbHelper.getSubIds(batchId);
            subjects = dbHelper.getSubjects(batchId);

            subTV = new TextView[subIds.size()];
            subjectIds = new String[subIds.size()];
            subjectNames = new String[subjects.size()];

            subIds.toArray(subjectIds);
            subjects.toArray(subjectNames);

            for(int j=0;j<subjectIds.length;j++) {
                dbHelper.updateSubjectScore(subjectIds[j]);
                String score = dbHelper.getSubjectScore(subjectIds[j]);
                subTV[j] = new TextView(this);
                subTV[j].setLayoutParams(lparams);
                subTV[j].setText(subjectNames[j] + ": " + score);
                ScoreLL.addView(subTV[j]);
                Log.i("jntubits", subjectNames[j] +": " + score);
            }

            TextView[] scoreTV = new TextView[3];
            dbHelper.updateScore();
            String[] scoreStats = dbHelper.getScoreStats(); //Score-Ans-Correct
            for(int j=0;j<3;j++) {
                scoreTV[j] = new TextView(this);
                scoreTV[j].setLayoutParams(lparams);
            }
            scoreTV[0].setText("Total Score: " + scoreStats[0]);
            scoreTV[1].setText("Total Answered: " + scoreStats[1]);
            scoreTV[2].setText("Total Correct: " + scoreStats[2]);
            for (int j = 0; j < 3; j++)
                ScoreLL.addView(scoreTV[j]);
            Log.i("jntubits", "Score: " + scoreStats[0] + " Answered: " + scoreStats[1] + " Correct: " + scoreStats[2]);

            Button reset = new Button(this);
            reset.setText("Reset Scores");
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbHelper.resetScore();
                    Toast.makeText(getApplicationContext(), "Score has been reset", Toast.LENGTH_SHORT).show();
                }
            });

            ScoreLL.addView(reset);

        }
        catch (IOException e) {
            Log.e("jntubits", "DB Error in score");
        }

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(true);

    }
}
