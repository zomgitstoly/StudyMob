package com.ecs160.studymob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class MyProfile extends Activity {
	private String email = Login.mainuser.getEmail();
	private String fname = Login.mainuser.getName();
	//private String lname = Login.lname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile);
		
		TextView name_field = (TextView) findViewById(R.id.name_field);
		TextView email_field = (TextView) findViewById(R.id.email_field);
		name_field.setText(fname);
		email_field.setText(email);
	}
	
	public void onClick (View v) {
		if (v.equals(findViewById(R.id.provide_button))) {
			Intent i = new Intent(this, MyProviders.class);
			startActivity(i);
		}
		else if (v.equals(findViewById(R.id.consume_button))){
			Intent i = new Intent(this, MyConsumers.class);
			startActivity(i);
		}
		
		else if (v.equals(findViewById(R.id.edit_profile_button))){
			Intent i = new Intent(this, EditProfile.class);
			startActivity(i);
		}
		else if (v.equals(findViewById(R.id.mymobs_button))) {
			Intent i = new Intent(this, MyMobs.class);
			startActivity(i);
		}
		else if (v.equals(findViewById(R.id.back)))
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
			Intent i = new Intent(this, MainMenu.class);
			startActivity(i);     	
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	
}
