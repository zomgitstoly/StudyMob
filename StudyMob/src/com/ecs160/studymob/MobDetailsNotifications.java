package com.ecs160.studymob;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MobDetailsNotifications extends Activity {
	//private WebServer server = StudyMob.server;
	private int selected_mob_id;
	private String selected_mob_name;
	private boolean userisowner;
	private boolean useringroup;
	private String owner;
	private String topic;
	private String course;
	private String time;
	private String location;
	private ArrayList<String> members = new ArrayList<String>();
	private ListView mylist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mob_details_notifications);
		
		Bundle b = getIntent().getExtras();
		selected_mob_id = b.getInt("mobid");
		selected_mob_name = b.getString("mobname");
		
		// Get the group information from the server and set the appropriate fields.
		getGroupInfo();
		setFields();
		
		
		//Set the list adapter, maybe get rid of the Owner field and list the owner as a part of the member list?
		mylist = (ListView)findViewById(R.id.memberlist);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mylist_item_center, members);
		mylist.setAdapter(adapter);
		mylist.setItemsCanFocus(false);
	}
	
	public void onClick(View v) {
		if (v.equals(findViewById(R.id.back))) {
			finish();
		}
	}
	
	private void getGroupInfo() {
		JSONObject request = new JSONObject();
		try {
			request.put("action","get_group");
			request.put("group_id",selected_mob_id);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		String response = StudyMob.model.server.sendHttpRequest(request);
		JSONArray the_json_array = null;
		int size;
		int ownerid = 0, locationid = 0, classid = 0;
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		getLocation(locationid);
		getOwner(ownerid);
		getClass(classid);
		getMembers();
	}
	
	private void getOwner(int ownerid) {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_user");
			request.put("user_id", ownerid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String response = StudyMob.model.server.sendHttpRequest(request);
		StringTokenizer st = new StringTokenizer(response, ",");
		String fname = st.nextToken();
		String lname = st.nextToken();
		owner = fname + " " + lname;
		members.add(owner);
	}
	
	private void getLocation(int locationid) {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_location");
			request.put("location_id", locationid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		location = StudyMob.model.server.sendHttpRequest(request);
	}
	
	private void getClass(int classid) {
		String response;
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_class");
			request.put("class_id", classid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response =StudyMob.model.server.sendHttpRequest(request);
		
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
	
	private void getMembers() {
		String response;
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_group_users");
			request.put("group_id", selected_mob_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = StudyMob.model.server.sendHttpRequest(request);
		
		JSONArray the_json_array = null;
		int size, memberid = 0;
		try {
			JSONObject json = new JSONObject(response);
			the_json_array = json.getJSONArray("users");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		size = the_json_array.length();
		if (size != 0) {
			// No other members.  Don't add to list.
			for (int i = 0; i < size; i++) {
				try {
					JSONObject another_json_object = the_json_array.getJSONObject(i);
					memberid = another_json_object.getInt("value");
					members.add(getMemberName(memberid));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String getMemberName(int memberid) {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_user");
			request.put("", memberid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String response = StudyMob.model.server.sendHttpRequest(request);
		StringTokenizer st = new StringTokenizer(response, ",");
		String fname = st.nextToken();
		String lname = st.nextToken();
		return fname + " " + lname;
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

}
