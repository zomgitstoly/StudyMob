package com.ecs160.studymob;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class BuddyList extends Activity {
	//private WebServer server = StudyMob.server;
	private ArrayList<String> buddies = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddylist);
		//setTitle();
		
		// Get the buddy list of the current user.
		getBuddies();
	}
	
	private void getBuddies() {
		/*
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_buddies");
			request.put("user_id", Login.mainuser.getUserID());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String response = server.sendHttpRequest(request);
		
		JSONArray buddies_json = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			buddies_json = json.getJSONArray("groups");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		size = buddies_json.length();
		int buddyid = 0;
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = buddies_json.getJSONObject(i);
				//Log.i( this.toString() , another_json_object.getString("dept") );
				buddyid = Integer.parseInt(another_json_object.getString("value"));
				buddies.add(getUser(buddyid));
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
	}
	/*
	private String getUser(int userid) {

		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_user");
			request.put("user_id", userid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return server.sendHttpRequest(request);
	}
	
	private void setTitle() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(Login.mainuser.getName() + "'s Buddy List");
	}*/
	
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
