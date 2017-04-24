package rs.reviewer.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import rs.reviewer.MainActivity;
import rs.reviewer.R;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

public class SplashScreenActivity extends Activity
{
	private static final int REQUEST_CODE_EMAIL = 1;
	private static int SPLASH_TIME_OUT = 3000; // splash ce biti vidljiv minimum SPLASH_TIME_OUT milisekundi
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		// uradi inicijalizaciju u pozadinksom threadu
		new InitTask().execute();
	}
	
	private class InitTask extends AsyncTask<Void, Void, Void>
	{
		private long startTime;
		
		@Override
        protected void onPreExecute()
        {
			startTime = System.currentTimeMillis();
        }
		
		@Override
		protected Void doInBackground(Void... arg0)
		{
			continueLogin();
			return null;
		}
		
		private void continueLogin()
		{
			// sacekaj tako da splash bude vidljiv minimum SPLASH_TIME_OUT milisekundi
			long timeLeft = SPLASH_TIME_OUT - (System.currentTimeMillis() - startTime);
			if(timeLeft < 0) timeLeft = 0;
			SystemClock.sleep(timeLeft);
			
			// uloguj se
			login();
		}
	}
	
    /**
     * Proveri da li je logovan user, ako nije registruj ga.
     */
	private void login()
	{
			startMainActivity();
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_EMAIL)
        {
        	if(resultCode == RESULT_OK) // korisnik je odabrao nalog sa emailom
        	{
            	String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            	if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
            	{
            		startMainActivity();
            	}
            	else
            	{
            		Toast.makeText(this, R.string.no_valid_email_message, Toast.LENGTH_LONG).show();
            	}
        	}
        	else
        	{
        		Toast.makeText(this, R.string.select_account_with_email_message, Toast.LENGTH_LONG).show();
        	}
        }
    }
	
	private void startMainActivity()
	{
		startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
		finish(); // da nebi mogao da ode back na splash
	}
}
