package in.momofactory.jntubits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener( new OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent i = new Intent(WelcomeActivity.this, SelectOptions.class);
							startActivity(i);
							finish();
						}
					}
				);
	}
}
