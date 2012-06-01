package com.ecs160.studymob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class Notifications extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications);
		//** Ask for how many invites and requests does a person have
	}
	
	public void onClick(View v) {
		Intent i;
		if (v.equals(findViewById(R.id.requests))) {
			i = new Intent(this,Requests.class);
			startActivity(i);
		}
		else if (v.equals(findViewById(R.id.invites))) {
			i = new Intent(this, Invites.class);
			startActivity(i);
		}
		else if (v.equals(findViewById(R.id.back))) {
			i = new Intent(this, MainMenu.class);
			startActivity(i);
			finish();
		}
	}
	

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
        	Intent i = new Intent(this,MainMenu.class);
        	startActivity(i);
        	finish(); //finish the webpage activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
}
