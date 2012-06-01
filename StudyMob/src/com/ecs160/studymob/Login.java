package com.ecs160.studymob;

import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private String response;
	private String email;
	private ProgressDialog pd;
	private AsyncBlock task = null;
	protected static User mainuser = new User();
	private boolean error = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	}
	
	 public void onClick(View v){
	    	if (v.equals(findViewById(R.id.login))) {
	            task = new AsyncBlock();
	            task.execute();
	    	}
	    	else if (v.equals(findViewById(R.id.new_account))) {
	    		// When "Create Account" is clicked
	    		// GOAL: Start a new activity to create a new account
	    		
	    		// start a new activity to create a new account
	    		Intent i = new Intent(this, NewAccount.class);
	    		startActivity(i);
	    	}
	    	else if (v.equals(findViewById(R.id.forgot_password))) {
	    		// When "Forgot Password" is clicked
	    		// GOAL: Start a new activity to retrieve password
	    		
	    		// start a new activity to retrieve password
	    		Intent i = new Intent(this, ForgotPassword.class);
	    		startActivity(i);
	    	}
	    }	// When "Log In", "Create Account", or "Forgot Password is clicked
	    
	    public void setOnEditorActionListener(View v) {
	    	if (v.equals(findViewById(R.id.email_field))) {
	    		// Pull information from the email field and store in the email variable.
	    		EditText email_field = (EditText) findViewById(R.id.email_field);
	    		email = email_field.getText().toString().trim();
	    		
	    		// Create a JSONObject to send to request to server.
	    		JSONObject remember_psswd = new JSONObject();
	    		
	    		try {
	    			remember_psswd.put("action", "remember_psswd");
	    			remember_psswd.put("email", email);
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
		    		e.printStackTrace();
	    		}
	    		
	    		// TODO Have server side handle the remember password request.
	    		//response = server.sendHttpRequest(remember_psswd);
	    		
	    		Log.i(this.toString(), "Remember Psswd Response: " + response);
	    		
	    	}	// If user finishes fills out email address.
	    }	// Action Listener for EditText
	
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
    
    public void loginAsync(){

		EditText email_field = (EditText) findViewById(R.id.email_field);
		email = email_field.getText().toString().trim();
		EditText password_field = (EditText) findViewById(R.id.password_field);
		String password = password_field.getText().toString().trim();
		//TextView error_message = (TextView) findViewById(R.id.error_message);

		response = StudyMob.model.login(email, password);
		Intent i;
		
		if (response.compareTo("ERROR: invalid email") == 0) {
			//Toast.makeText(Login.this, "You have entered an invalid email address.", Toast.LENGTH_SHORT).show();
			Log.i(this.toString(), "You have entered an invalid email address");
			error = true;
			i = new Intent(this, Login.class);
    		startActivity(i);
    		finish();
		}
		else if (response.compareTo("ERROR: invalid password")  == 0 ){
			//error_message.setText("You have entered the incorrect password.");
			//Toast.makeText(Login.this, "You have entered the incorrect password.", Toast.LENGTH_SHORT).show();
			error = true;
			i = new Intent(this, Login.class);
    		startActivity(i);
    		finish();

		}	
		else if (response.compareTo("ERROR: please input an email")  == 0 ){
			//error_message.setText("Enter your email address.");
			//Toast.makeText(Login.this, "Enter your email address.", Toast.LENGTH_SHORT).show();
			error = true;
			i = new Intent(this, Login.class);
    		startActivity(i);
    		finish();

		}	
		else if (response.compareTo("ERROR: please input a password")  == 0){
			//error_message.setText("Enter your password.");
			//Toast.makeText(Login.this, "Enter your password.", Toast.LENGTH_SHORT).show();
			error = true;
			i = new Intent(this, Login.class);
    		startActivity(i);
    		finish();

		}	
		else if (response.compareTo("ERROR: invalid email")  == 0 ){
			//error_message.setText("Enter a valid email address.");
			//Toast.makeText(Login.this, "Enter a valid email address.", Toast.LENGTH_SHORT).show();
			error = true;
			i = new Intent(this, Login.class);
    		startActivity(i);
    		finish();

		}
		else if (response.compareTo("ERROR: could not log in with provided email: " + email)  == 0){
			//error_message.setText("Login information incorrect.");
			//Toast.makeText(Login.this, "Login information incorrect.", Toast.LENGTH_SHORT).show();
			error = true;
			i = new Intent(this, Login.class);
    		startActivity(i);
    		finish();
		}
		else {
				// Account is validated, proceed to main menu.
				int user_id;
				String fname, lname;
				// Store the response into user_id variable
				user_id = Integer.parseInt(response);

				response = StudyMob.model.getUser(user_id);
				
				StringTokenizer st = new StringTokenizer(response, ",");
				//st.nextToken();	// skip user_id
				//st.nextToken();	// skip email
				fname = st.nextToken();
				lname = st.nextToken();
				mainuser.setUser(user_id, fname, lname, email);
				Log.i(this.toString(), "First: " + fname + " Last: " + lname);
				
				i = new Intent(this, MainMenu.class);
	    		startActivity(i);

			}
    }
    
    private class AsyncBlock extends AsyncTask<Void, Void, Void> {
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		showProgress();
    	}

		@Override
		protected Void doInBackground(Void...params) {
			loginAsync();
			//Log.i("ASYNC TASK RESPONSE", response);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (!isCancelled() && error == false){
				notifyFinish();
			}
			else if(error){
				Toast.makeText(Login.this, "Error logging in, please check password, email and try again!", Toast.LENGTH_LONG).show();
			}
		}		
    }
    
    private void notifyFinish() {
    	if (pd != null) pd.dismiss();
    	Toast.makeText(this, "Logged In!", Toast.LENGTH_LONG).show();
    }
    private void showProgress() {
		
		// This will show a progress dialog on the screen
		pd = new ProgressDialog(this);
		pd.setTitle("");
		pd.setMessage("Loading...");
		pd.setButton("Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				pd.cancel();
			}
		});
		pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				if (task != null) task.cancel(true);
				Toast.makeText(Login.this, "Cancelled", Toast.LENGTH_SHORT).show();
			}
		});
		pd.show();
    }   
    
}
