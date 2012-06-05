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
import android.widget.Toast;

public class Invites extends ListActivity {
	private String location_id;
	private String response;
	private ListView mylist;
	private ArrayList<String> mobs = new ArrayList<String>();
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter;
	private int size_of_group = 0;
	private int maxSize = 0;
	protected static int selected_group_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invites);
		TextView test = (TextView) findViewById(R.id.invites);
		test.setText("Invite: Please Choose a Mob to Accept an Invite");
		mylist = (ListView)findViewById(android.R.id.list); 

		//****Change this to groups the user has invites for
		response = StudyMob.model.getInviteRequests(Login.mainuser.getUserID());
		Log.i("Response", response);
		
		JSONArray groups_json = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			groups_json = json.getJSONArray("invites");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		size = groups_json.length();
		String group_id;
		String group_name;
		String group_topic;
		String class_name_srvr;
		String class_name;
		String class_id;
		for (int i = 0; i < size; i++) {
			try {
				JSONObject temporary_json = groups_json.getJSONObject(i);
				
				group_id = temporary_json.getString("value");

				checkGroupSize(Integer.parseInt(group_id));
				
				class_id = StudyMob.model.getGroup(Integer.parseInt(group_id));
				//Ask Server for the name of the class
				//Extract the actual name from the array
				JSONArray tmp_json = null;
				try {
					JSONObject json = new JSONObject(class_id);
					tmp_json = json.getJSONArray("group");
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				
				JSONObject tmp2_json = tmp_json.getJSONObject(0);
				group_name = tmp2_json.getString("name");
				group_topic = tmp2_json.getString("topic");
				maxSize = Integer.parseInt(tmp2_json.getString("max_size"));
				class_name = getClass(Integer.parseInt(tmp2_json.getString("class_id")));
				//Display the class name followed by group name and the topic
				mobs.add(group_name + " : " + group_topic);
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put("groupid", group_id);
				temp.put("class", class_name);
				temp.put("name", group_name);
				temp.put("topic", group_topic);
				list.add(temp);

			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		adapter = new SimpleAdapter(this, list, R.layout.mob_list_item,
				new String[] {"class", "name", "topic"},
				new int[] {R.id.class_field, R.id.name_field, R.id.topic_field});
		mylist.setAdapter(adapter);
	}
	
	public void onClick(View v) {
		if (v.equals(findViewById(R.id.back))) {
			Intent i = new Intent(this, Notifications.class);
			startActivity(i);
			this.finish();
		}
	}
	private void checkGroupSize(int groupid){
		String maxsize = StudyMob.model.getGroupUsers(groupid);
		
		JSONArray json_users = null;
		
		try {
			JSONObject json = new JSONObject(maxsize);
			json_users = json.getJSONArray("users");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		size_of_group = json_users.length();
		
	}
	private String getClass(int classid) {
		String response = StudyMob.model.getClass(Integer.toString(classid));
		String course = new String();
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
		return course;
	}	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// When clicked, store the username into selectedFriend
		@SuppressWarnings("rawtypes")
		HashMap selected_item = (HashMap) mylist.getItemAtPosition(position);
		selected_group_id = Integer.parseInt(selected_item.get("groupid").toString());

		
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder2.setMessage("Sorry but the group is full")
		       .setCancelable(false)
		       .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
	        		   dialog.cancel();	        	   
		        	   startActivity(getIntent());
		        	   finish();
		           }
		       });
		final AlertDialog alert2 = builder2.create();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want to accept this invite?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   	//**ACCEPT INVITE
		        	   if(size_of_group >= maxSize){
		        		   StudyMob.model.denyJoin(Login.mainuser.getUserID(),selected_group_id);
		        		   dialog.cancel();
			        	   alert2.show();		        		   
		        	   }
		        	   else{
		        	   	StudyMob.model.acceptJoin(Login.mainuser.getUserID(),selected_group_id);
		        		startActivity(getIntent());
		        		finish();
		        	   }
		           }
		       })
		       .setNeutralButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   	//**DENY INVITE
		        	   	//StudyMob.model.denyJoin(selected_user_name,selected_mob_id);
		        	   	StudyMob.model.denyJoin(Login.mainuser.getUserID(),selected_group_id);
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
