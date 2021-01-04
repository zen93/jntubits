package in.momofactory.jntubits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

public class SelectOptions extends AppCompatActivity {
    private RadioGroup regGroup;
    private RadioButton regButton;
    private Spinner branchSpinner, yearSpinner, semSpinner;
    private String regulation, branch, year, sem, mid;
    private Boolean learning, revision;
    private AppSettings appSettings;
    private DBHelper dbHelper;

    private boolean validate() {
        //Regulation
        regGroup = (RadioGroup) findViewById(R.id.regulation);
        regButton = (RadioButton) findViewById(regGroup.getCheckedRadioButtonId());

        regulation = regButton.toString();
        branch = branchSpinner.getSelectedItem().toString();
        year = yearSpinner.getSelectedItem().toString();
        sem = semSpinner.getSelectedItem().toString();
        mid = "Mid I";
        learning = true;
        revision = false;

        String empty = "---";
        if(branch.equals(empty)) {
            errorToast("Please select a branch");
            return false;
        }
        if(year.equals(empty)) {
            errorToast("Please select a year");
            return false;
        }
        if(sem.equals(empty) && (!year.equals("I"))) {
            errorToast("Please select a semester");
            return false;
        }
        if(!sem.equals(empty) && year.equals("I")) {
            errorToast("Please select correct year and semester");
            return false;
        }
        return true;
    }
    private void errorToast(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT)
                .show();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        appSettings = new AppSettings(getApplicationContext());

        //List of Branches
        branchSpinner = (Spinner) findViewById(R.id.branch);
        ArrayAdapter<CharSequence> bAdapter = ArrayAdapter.createFromResource(this, R.array.branch_selection, android.R.layout.simple_spinner_item);
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(bAdapter);

        //Year
        yearSpinner = (Spinner) findViewById(R.id.year);
        ArrayAdapter<CharSequence> yAdapter = ArrayAdapter.createFromResource(this, R.array.year_selection, android.R.layout.simple_spinner_item);
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yAdapter);

        //Sem
        semSpinner = (Spinner) findViewById(R.id.sem);
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this, R.array.sem_selection, android.R.layout.simple_spinner_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semSpinner.setAdapter(sAdapter);



        //Validate
        Button submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener( new OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 if(validate()) {
                                                     appSettings.saveSettings(regulation, year, branch, sem, mid, learning, revision);
                                                     try {
                                                         DBHelper dbHelper = new DBHelper(getApplicationContext());
                                                     }
                                                     catch(IOException e) {
                                                         Log.e("jntubits", "Can't init DB!");
                                                     }
                                                     Intent i = new Intent(SelectOptions.this, MainActivity.class);
                                                     startActivity(i);
                                                     finish();
                                                 }
                                             }
                                         }
        );
    }

}
