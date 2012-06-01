package com.ecs160.studymob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class MainMenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
	}
	
	public void onClick(View v) {
		Intent i;
		// If "Map" button is clicked
		if (v.equals(findViewById(R.id.map))) {
			i = new Intent(this,MapMenu.class);
			startActivity(i);
		}
		// If "Search StudyMobs" button is clicked
		
		else if (v.equals(findViewById(R.id.search))) {
			i = new Intent(this, SearchMobs.class);
			startActivity(i);
		}
		// If "Create StudyMob" button is clicked
		else if (v.equals(findViewById(R.id.create))) {
			i = new Intent(this, CreateStudyMob.class);
			startActivity(i);
		}
		
		// If "View Account" button is clicked
		else if (v.equals(findViewById(R.id.account))) {
			i = new Intent(this, MyProfile.class);
			startActivity(i);
		}

		// If "Notifications" button is clicked
		else if (v.equals(findViewById(R.id.notifications))) {
			i = new Intent(this, Notifications.class);
			startActivity(i);
		}
		// If "Buddylist" button is clicked
		else if (v.equals(findViewById(R.id.buddylist))) {
			
		}
		// If "Logout" button is clicked
		else if (v.equals(findViewById(R.id.logout))) {
			finish();
			i = new Intent(this, Login.class);
			startActivity(i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
}
