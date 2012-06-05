package com.ecs160.studymob;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditName extends Activity {
	private String response;
	protected static int user_id;
	private String email;
	private String last_name;
	private String first_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_account);
		EditText first_name_field = (EditText)findViewById(R.id.first_name_field);
		first_name_field.setText(Login.mainuser.getFName());
		EditText last_name_field = (EditText)findViewById(R.id.last_name_field);
		last_name_field.setText(Login.mainuser.getLName());
		EditText email_field = (EditText)findViewById(R.id.email_field);
		email_field.setText(Login.mainuser.getEmail());
	}
	
	public void onClick (View v) {
		// When a new account is created, we want to send the information to the server to be added into the database.
		if (v.equals(findViewById(R.id.cont))) {

			// Pull information from the filled out form
			EditText first_name_field = (EditText)findViewById(R.id.first_name_field);
			first_name = first_name_field.getText().toString().trim();
			EditText last_name_field = (EditText)findViewById(R.id.last_name_field);	
			last_name = last_name_field.getText().toString().trim();		
			EditText email_field = (EditText)findViewById(R.id.email_field);
			email = email_field.getText().toString().trim();
			TextView error_message = (TextView) findViewById(R.id.error_message);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You sure you want to edit your name or your email?")
			       .setCancelable(false)
			       .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   	StudyMob.model.editUser(first_name, last_name, email);	
			        	   	Login.mainuser.setName(first_name, last_name);
			    			Intent i = new Intent(EditName.this, EditProfile.class);
			    			startActivity(i);
			    			finish();
			           }
			       })    			       
			       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
    			
    			AlertDialog alert = builder.create();
    			alert.show();
    		
		}
		
		else if (v.equals(findViewById(R.id.cancel)))
			finish();
		
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
        		finish(); //finish the webpage activity
				Intent i = new Intent(this, EditProfile.class);
   	    		startActivity(i);
        	
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
