package com.ecs160.studymob;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class ForgotPassword extends Activity {
	WebServer server = new WebServer();
	private String response;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
	}
	
	public void onClick (View v) {
		// When a new account is created, we want to send the information to the server to be added into the database.
		if (v.equals(findViewById(R.id.submit))) {
			// Retrieve information from the form
			EditText email_field = (EditText)findViewById(R.id.email_field);
			String email = email_field.getText().toString().trim();
			
			// Create JSON Object 
			JSONObject retrieve_password = new JSONObject();
			
			try {
				retrieve_password.put("action", "forgot_password");
				retrieve_password.put("email",email);
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
			
			// Send the information to the server for validation
			response = server.sendHttpRequest(retrieve_password);
			
			Log.i(this.toString(), "Retrieving password for " + email);
			Log.i(this.toString(), "Response from server: " + response);
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
