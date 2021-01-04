package in.momofactory.jntubits;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;


public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(true);

        String help = "<h1> Help Documentation </h1>"
                + "This document guides the users to operate this application effortlessly."
                + "<h3>To Reset the batch</h3> <p>Click on the navigation drawer icon present on the action bar of “choose a Subject” activity." +
                "Click on settings and select the option “batch”. </p> "
                + "<h3>To view your score</h3> <p>Click on the action bar overflow menu of “choose a subject” activity, then select score option. </p> " +
                "<h3>To set the Mode</h3> <p> Click on the navigation drawer icon present on the " +
                "action bar of “choose a Subject” activity, then select either learning mode or revision mode based on your requirement.</p>"
                + "<h3> To select the Mid-Term</h3> <p>Click on \"Mid\" present on the action bar of \"select a Subject\" activity, then select either Mid-1 or Mid-2 based on your requirement. </p>";

        TextView tv = (TextView)findViewById(R.id.helpTV);
        tv.setText(Html.fromHtml(help));
    }

}