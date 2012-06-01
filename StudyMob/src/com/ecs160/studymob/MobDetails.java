package com.ecs160.studymob;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MobDetails extends Activity {
	private int selected_mob_id;
	private String selected_mob_name;
	private boolean userisowner;
	private boolean useringroup;
	private int ownerid;
	private String owner;
	private String owner_email;
	private String topic;
	private String course;
	private String time;
	private String location;
	private int classid;
	private int maxsize;
	private ArrayList<Integer> members_id = new ArrayList<Integer>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		setContentView(R.layout.mob_details);
		
		selected_mob_id = b.getInt("mobid");
		selected_mob_name = b.getString("mobname");
		
		// Get the group information from the server and set the appropriate fields.
		getGroupInfo();
		setFields();
		setButtons();
	}
	
	public void onClick(View v) {
		if (v.equals(findViewById(R.id.join_or_add))) {
			String response;
			Context context = getApplicationContext();
			// if user is the owner, invite his or her buddies to join
			if (userisowner) {
				//response = StudyMob.model.inviteToGroup(Login.mainuser.getUserID(),selected_mob_id);
				Intent i = new Intent(this, InviteList.class);
				Bundle b = new Bundle();
				//Log.i("CLASS ID IS:",classid);
				b.putInt("class_id", classid);
				b.putInt("group_id", selected_mob_id);
				i.putExtras(b);
				startActivity(i);
			}
			// else if the user isn't the owner and isn't already in the group, ask to join group
			else if (!useringroup && !userisowner) {
				// if group capacity is reached, show error
				if (members_id.size() >= maxsize)
					Toast.makeText(context, "Group Capacity Reached!", Toast.LENGTH_SHORT).show();
				else {
					response = StudyMob.model.joinGroup(Login.mainuser.getUserID(), selected_mob_id);
					Toast.makeText(context, "Request sent to owner. Waiting for approval.", Toast.LENGTH_SHORT).show();
				}
			}
			// else if the user is already in the group, leave
			else if (useringroup && !userisowner) {
				response = StudyMob.model.leaveGroup(Login.mainuser.getUserID(), selected_mob_id);
				Button b = (Button) findViewById(R.id.join_or_add);
				b.setText("Join Mob");
				Toast.makeText(context, "You have left the Mob.", Toast.LENGTH_SHORT).show();
			}
		}
		if (v.equals(findViewById(R.id.memberlist))) {
			Intent i = new Intent(this, MobMembers.class);
			Bundle b = new Bundle();
			b.putInt("mob_id", selected_mob_id);
			b.putString("mob_name", selected_mob_name);
			b.putString("owner", owner);
			b.putString("owner_email", owner_email);
			i.putExtras(b);
			startActivity(i);
		}
		if (v.equals(findViewById(R.id.back))) {
			finish();
		}
	}
	
	private void getGroupInfo() {
		String response = StudyMob.model.getGroup(selected_mob_id);
		JSONArray the_json_array = null;
		int size;
		int locationid = 0; 
		classid = 0;
		try {
			JSONObject json = new JSONObject(response);
			the_json_array = json.getJSONArray("group");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array.getJSONObject(i);
				ownerid = another_json_object.getInt("user_id");
				topic = another_json_object.getString("topic");
				time = another_json_object.getString("time");
				locationid = another_json_object.getInt("location_id");
				classid = another_json_object.getInt("class_id");
				maxsize = another_json_object.getInt("max_size");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		getLocation(locationid);
		getOwner(ownerid);
		getClass(classid);
		//getMembers();
	}
	
	private void getOwner(int ownerid) {
		String response = StudyMob.model.getUserInfo(ownerid);
		
		JSONArray the_json_array = null;
		try {
			JSONObject json = new JSONObject( response );
			the_json_array = json.getJSONArray("user");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject another_json_object;
		String fname = "", lname = "";
		try {
			another_json_object = the_json_array.getJSONObject(0);
			fname = another_json_object.getString("first_name");
			lname = another_json_object.getString("last_name");
			owner_email = another_json_object.getString("email");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		owner = fname + " " + lname;
	}
	
	private String getMemberStatus() {
		return StudyMob.model.getMemberStatus(Login.mainuser.getUserID(), selected_mob_id);
	}
	
	private void getLocation(int locationid) {
		location = StudyMob.model.getLocation(locationid);
	}
	
	private void getClass(int classid) {
		String response = StudyMob.model.getClass(Integer.toString(classid));
		
		JSONArray the_json_array = null;
		int size;
		try {
			JSONObject json = new JSONObject(response);
			the_json_array = json.getJSONArray("class");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array.getJSONObject(i);
				course = another_json_object.getString("name");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setButtons() {
		// Check if the logged in user is the owner of the group
		Button action = (Button) findViewById(R.id.join_or_add);
		if (Login.mainuser.getUserID() == ownerid) {
			userisowner = true;
			action.setText("Invite");
		}
		else {
			userisowner = false;
			//if (members_id.contains(Login.mainuser.getUserID())) {
			if (getMemberStatus().equals("in group")) {
				useringroup = true;
				action.setText("Leave Mob");
			}
			else {
				useringroup = false;
				action.setText("Join Mob");
			}
		}
	}
	
	private void setFields() {
		TextView title = (TextView) findViewById(R.id.title);
		TextView owner_textview = (TextView) findViewById(R.id.owner_field);
		TextView topic_textview = (TextView) findViewById(R.id.topic_field);
		TextView class_field = (TextView) findViewById(R.id.class_field);
		TextView date_field = (TextView) findViewById(R.id.date_field);
		TextView location_field = (TextView) findViewById(R.id.location_field);
		
		title.setText(selected_mob_name);
		owner_textview.setText(owner);
		topic_textview.setText(topic);
		class_field.setText(course);
		date_field.setText(time);
		location_field.setText(location);
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
