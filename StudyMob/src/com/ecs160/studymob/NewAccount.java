package com.ecs160.studymob;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewAccount extends Activity {
	private String response;
	protected static int user_id;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_account);
		context = getApplicationContext();
	}
	
	public void onClick (View v) {
		// When a new account is created, we want to send the information to the server to be added into the database.
		if (v.equals(findViewById(R.id.cont))) {
			// Pull information from the filled out form
			EditText first_name_field = (EditText)findViewById(R.id.first_name_field);
			String first_name = first_name_field.getText().toString().trim();
			EditText last_name_field = (EditText)findViewById(R.id.last_name_field);
			String last_name = last_name_field.getText().toString().trim();		
			EditText email_field = (EditText)findViewById(R.id.email_field);
			String email = email_field.getText().toString().trim();
			EditText password_field = (EditText)findViewById(R.id.password_field);
			String password = password_field.getText().toString().trim();
			EditText retype_password_field = (EditText)findViewById(R.id.retype_password_field);
			String retype = retype_password_field.getText().toString().trim();
			
			if (first_name.equals(""))
				Toast.makeText(context, "Fill out your first name!", Toast.LENGTH_SHORT).show();
			else if (last_name.equals(""))
				Toast.makeText(context, "Fill out your last name!", Toast.LENGTH_SHORT).show();
			else if (email.equals(""))
				Toast.makeText(context, "Fill out your email!", Toast.LENGTH_SHORT).show();
			else if (password.equals(""))
				Toast.makeText(context, "Fill out your password!", Toast.LENGTH_SHORT).show();
			else if (!retype.equals(password))
				Toast.makeText(context, "Make sure your passwords are matching!", Toast.LENGTH_SHORT).show();
			else {
	    		response = StudyMob.model.signUp(first_name, last_name, email, password);
	    		// Verify new account details
	    		if (response.equals("ERROR: invalid email"))
	    			Toast.makeText(context, "Invalid email!", Toast.LENGTH_SHORT).show();
	    		else if (response.equals("ERROR: invalid password"))
	    			Toast.makeText(context, "Invalid password!", Toast.LENGTH_SHORT).show();
	    		else if (response.equals("ERROR: the email is already registered"))
	    			Toast.makeText(context, "Email already in use!", Toast.LENGTH_SHORT).show();
	    		else {
	    			user_id = Integer.parseInt(response);
	    			finish();
	    			Intent i = new Intent(this, Providers.class);
	    			startActivity(i);
	    		}
			}
		}
		
		// If cancel button is clicked, cancel and return to main page
		else if (v.equals(findViewById(R.id.cancel)))
			finish();
		
	}
	
	/* Handle when the back button is pressed. After the Twitter 'Accept' webpage
     * has issued you a pin, you need to enter it, and the pressing the back button allows you
     * to go back to your Application start page, as opposed to ending the Activity.
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
        	finish(); //finish the webpage activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
