package in.momofactory.jntubits;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class QuestionsListActivity extends AppCompatActivity {
    private String subject;
    private String subjectId;
    private String midId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_list);

        Intent i = getIntent();
        subject = i.getStringExtra("subjectName");
        subjectId = i.getStringExtra("subjectId");
        midId = i.getStringExtra("midId");
        //Toast.makeText(this, "Subject: " + subject + " subId: " + subjectId + " midId: " + midId, Toast.LENGTH_SHORT).show();

        Bundle args = new Bundle();
        args.putString("subjectName", subject);
        args.putString("subjectId", subjectId);
        args.putString("midId", midId);

        ViewPager viewPager = (ViewPager) findViewById(R.id.questionsListViewPager);
        viewPager.setAdapter(new QuestionsListAdapter(getSupportFragmentManager(), QuestionsListActivity.this, args));
        viewPager.setOffscreenPageLimit(2); //may not need this

        TabLayout tabLayout = (TabLayout) findViewById(R.id.questionsListTabs);
        tabLayout.setupWithViewPager(viewPager);

        Toast.makeText(getApplicationContext(), subject, Toast.LENGTH_SHORT).show();

    }
}
