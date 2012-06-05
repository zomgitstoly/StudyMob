package com.ecs160.studymob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class EditProfile extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);		
	}
	
	public void onClick (View v) {
		if (v.equals(findViewById(R.id.edit_name_button))) {
			Intent i = new Intent(this, EditName.class);
			startActivity(i);
			finish();
		}
		else if (v.equals(findViewById(R.id.edit_consumer_button))){
			Intent i = new Intent(this, EditConsumers.class);
			startActivity(i);
			finish();
		}
		else if (v.equals(findViewById(R.id.edit_provide_button))){
			Intent i = new Intent(this, EditProviders.class);
			startActivity(i);
			finish();
		}
		else if (v.equals(findViewById(R.id.back))) {
			Intent i = new Intent(this, MyProfile.class);
			startActivity(i);
			finish();
		}
	}
	
	/* Handle when the back button is pressed. After the Twitter 'Accept' webpage
     * has issued you a pin, you need to enter it, and the pressing the back button allows you
     * to go back to your Application start page, as opposed to ending the Activity.
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
        	finish(); //finish the webpage activity
			Intent i = new Intent(this, MyProfile.class);
	    	startActivity(i);
        	
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	
}
