package com.ecs160.studymob;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyMobs extends Activity {
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private ListView mylist;
	private SimpleAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymobs);
		
		setList();
	}
	
	private void setList() {
		getOwnedGroups();
		getGroupsByUserID();
		mylist = (ListView) findViewById(R.id.mymobs);
		// TODO Finish up adapting the list and fetching info from the server
		adapter = new SimpleAdapter(this, list, R.layout.mob_list_item,
				new String[] {"class", "name", "topic"},
				new int[] {R.id.class_field, R.id.name_field, R.id.topic_field});
		mylist.setAdapter(adapter);
		mylist.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				// When clicked, store the username into selectedFriend
				@SuppressWarnings("rawtypes")
				HashMap selected_item = (HashMap) mylist.getItemAtPosition(position);
				
				Bundle b = new Bundle();
				b.putString("mobname", selected_item.get("name").toString());
				b.putInt("mobid", Integer.parseInt(selected_item.get("groupid").toString()));
				Intent i = new Intent(v.getContext(), MobDetails.class);
				i.putExtras(b);
				startActivity(i);
			}
		});
		
	}
	
	private void getOwnedGroups() {
		String response = StudyMob.model.getOwnedGroups(Login.mainuser.getUserID());
		JSONArray the_json_array = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			the_json_array = json.getJSONArray("groups");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String group_id;
		String group_name;
		String group_topic;
		String class_name_srvr;
		String class_name;
		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array.getJSONObject(i);
				group_id = another_json_object.getString("group_id");
				group_name =  another_json_object.getString("name"); //name of the group
				group_topic = another_json_object.getString("topic"); //name of the topic
				
				//Ask Server for the name of the class
				class_name_srvr = StudyMob.model.getClass(another_json_object.getString("class_id"));
				//Extract the actual name from the array
				JSONArray tmp_json = null;
				try {
					JSONObject json = new JSONObject( class_name_srvr );
					tmp_json = json.getJSONArray("class");
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject tmp2_json = tmp_json.getJSONObject(0);
				class_name = tmp2_json.getString("name");
				
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put("groupid", group_id);
				temp.put("class", class_name);
				temp.put("name", group_name);
				temp.put("topic", group_topic);
				temp.put("status", "owner");
				list.add(temp);
			}  catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void getGroupsByUserID() {
		String response = StudyMob.model.getMyMobs(Login.mainuser.getUserID());
		JSONArray the_json_array = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			the_json_array = json.getJSONArray("groups");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String group_id;
		String group_name;
		String group_topic;
		String class_name_srvr;
		String class_name;
		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array.getJSONObject(i);
				group_id = another_json_object.getString("group_id");
				group_name =  another_json_object.getString("name"); //name of the group
				group_topic = another_json_object.getString("topic"); //name of the topic
				
				//Ask Server for the name of the class
				class_name_srvr = StudyMob.model.getClass(another_json_object.getString("class_id"));
				//Extract the actual name from the array
				JSONArray tmp_json = null;
				try {
					JSONObject json = new JSONObject( class_name_srvr );
					tmp_json = json.getJSONArray("class");
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject tmp2_json = tmp_json.getJSONObject(0);
				class_name = tmp2_json.getString("name");
				
				HashMap<String,String> temp = new HashMap<String,String>();
				temp.put("groupid", group_id);
				temp.put("class", class_name);
				temp.put("name", group_name);
				temp.put("topic", group_topic);
				temp.put("status", "member");
				list.add(temp);
			}  catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void onClick(View v) {
		if (v.equals(findViewById(R.id.back))) {
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
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
