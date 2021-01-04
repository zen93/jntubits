package in.momofactory.jntubits;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
	private static int SPLASH_TIME_OUT = 2000;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				SPLASH_TIME_OUT = 0;
				AppSettings appSettings = new AppSettings(getApplicationContext());
				boolean firstLaunch = appSettings.isFirstLaunch();
				
				if(firstLaunch) {					
					//Launch Welcome screen
					Intent welcome = new Intent(SplashScreen.this, WelcomeActivity.class);
					startActivity(welcome);					
				}
				else {
					Intent intent = new Intent(SplashScreen.this, MainActivity.class);
					startActivity(intent);
				}
								
				finish();
			}
		}, SPLASH_TIME_OUT);
		
	}

}
