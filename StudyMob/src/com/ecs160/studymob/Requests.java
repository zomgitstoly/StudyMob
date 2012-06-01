package com.ecs160.studymob;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Requests extends ListActivity {
	
	private String location_id;
	private String response;
	private ListView mylist;
	private ArrayList<String> mobs = new ArrayList<String>();
	protected static int selected_user_name;
	protected static int selected_mob_id;
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		setContentView(R.layout.invites);
		TextView test = (TextView) findViewById(R.id.invites);
		test.setText("Requests");
		mylist = (ListView)findViewById(android.R.id.list); 
		
		response = StudyMob.model.getOwnedGroups(Login.mainuser.getUserID());
		Log.i("Response", response);
		
		JSONArray groups_json = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			groups_json = json.getJSONArray("groups");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		size = groups_json.length();
		String group_id;
		String group_name;
		String group_topic;
		String class_name_srvr;
		String req_id;
		String user_fname;
		String user_lname;
		for (int i = 0; i < size; i++) {
			try {
				JSONObject temporary_json = groups_json.getJSONObject(i);
				JSONObject class_json = new JSONObject();
				Log.i( this.toString() , temporary_json.getString("name") );
				
				group_id = temporary_json.getString("group_id");
				group_name =  temporary_json.getString("name"); //name of the group
				
				//Ask Server Users who requested
				class_name_srvr = StudyMob.model.getJoinRequests(group_id);
				//Extract the actual name from the array
				JSONArray tmp_json = null;
				try {
					JSONObject json = new JSONObject( class_name_srvr );
					tmp_json = json.getJSONArray("joinrequests");
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int j =0; j < tmp_json.length(); j++){
					JSONObject tmp2_json = tmp_json.getJSONObject(j);
					req_id = tmp2_json.getString("value");
					user_fname =tmp2_json.getString("first_name");
					user_lname =tmp2_json.getString("last_name");
					Log.i("WE ARE TRYING TO GET THE USER ID", req_id);
					//Display the class name followed by group name and the topic
					mobs.add(req_id + " : " + group_name);
					HashMap<String,String> temp = new HashMap<String,String>();
					temp.put("groupid", group_id);
					temp.put("class", user_fname + " " + user_lname); //UserName
					temp.put("userid",req_id);
					temp.put("name", group_name);
					list.add(temp);
				}
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		adapter = new SimpleAdapter(this, list, R.layout.mob_list_request,
				new String[] {"class", "name"},
				new int[] {R.id.class_field, R.id.name_field});
		mylist.setAdapter(adapter);
	}
	
	public void onClick(View v) {
		if (v.equals(findViewById(R.id.back))) {
			Intent i = new Intent(this, Notifications.class);
			startActivity(i);
			finish();
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("rawtypes")
		HashMap selected_item = (HashMap) mylist.getItemAtPosition(position);
		
		selected_mob_id = Integer.parseInt(selected_item.get("groupid").toString());
		selected_user_name = Integer.parseInt(selected_item.get("userid").toString());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to accept this user?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   	//**SEND REQUEST TO SERVER WITH THE ACCEPT
		        	   	StudyMob.model.acceptJoin(selected_user_name,selected_mob_id);
		        		startActivity(getIntent());
		        		finish();
		           }
		       })
		       .setNeutralButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   	//**SEND REQUEST TO SERVER WITH THE Deny
		        	   	StudyMob.model.denyJoin(selected_user_name,selected_mob_id);
		        		startActivity(getIntent());
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
			Intent i = new Intent(this, Notifications.class);
			startActivity(i);
        	finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
