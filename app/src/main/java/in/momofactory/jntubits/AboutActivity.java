package in.momofactory.jntubits;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(true);

        String help = "<h1>About Us</h1>" +
                "This application was developed by the ingenious approach and sincere efforts of the following:" +
                "<h3>Akshay Sinha (Team Leader) </h3>" +
                "<p>“I am ecstatic that my team and I were finally able to implement our idea!! Developing this application was a whole new learning experience for us”</p>" +
                "<h3>Saranya Varma</h3>" +
                "<p>“At the start I was doubtful if we would be able to develop this application, but later on due to our thoroughgoing team work we were able to do it!! I feel proud to be a part of the development of this application”</p>" +
                "<h3>Vamshi Kumar</h3>" +
                "<p>“I am elated to say that this application is a whole new level of learning JNTU bits. I mainly worked on the data conversion process for this application. This app is the result of all the hard work we put into it”</p>" +
                "<p><b>Our sincere thanks to Sreyas Institute of Engineering & Technology and to the faculty members and HOD of CSE department.</b></p>";

        TextView tv = (TextView)findViewById(R.id.aboutTV);
        tv.setText(Html.fromHtml(help));
    }

}
