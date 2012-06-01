package com.ecs160.studymob;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class LocationMobs extends ListActivity {
	private String location_id;
	private String response;
	private ListView mylist;
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		setContentView(R.layout.location_mobs);
		
		mylist = (ListView)findViewById(android.R.id.list); 
		TextView test = (TextView) findViewById(R.id.title_location_mob); //Set the title based on the location
		test.setText("Current StudyMobs at " + b.getString("name")); //gets the name of the location from the bundle
		location_id = b.getString("location"); //Get the location id from bundle this is used to get the groups
		Log.i("Location_id", location_id);
		//TODO: Fetch the mobs at this location
		/*
		//Send request for the groups in this location
		JSONObject request_provide = new JSONObject();
		try {
			request_provide.put("action","get_groups_in_location");
			request_provide.put("location_id",location_id);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}*/
		
		response = StudyMob.model.getGroupsinLocation(location_id);
		
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
		String class_name;
		for (int i = 0; i < size; i++) {
			try {
				JSONObject temporary_json = groups_json.getJSONObject(i);
				JSONObject class_json = new JSONObject();
				Log.i( this.toString() , temporary_json.getString("name") );
				
				group_id = temporary_json.getString("group_id");
				group_name =  temporary_json.getString("name"); //name of the group
				group_topic = temporary_json.getString("topic"); //name of the topic
				//***** prob can can some code here to get the group_id to set up the details intent if they click on the item
				//***** might need to add new array to keep them in the same order as they appear so you know the index 
				
				/*
				//Ask Server for the name of the class
				try {
					class_json.put("action","get_class");
					class_json.put("class_id", temporary_json.getString("class_id"));
				} catch (JSONException e) {
		   			// TODO Auto-generated catch block
		   			e.printStackTrace();
				}*/
				class_name_srvr = StudyMob.model.getClass(temporary_json.getString("class_id"));
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
				//Display the class name followed by group name and the topic
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
		
		//******* Need to add the cursor adapter and w/e u need to see which group they clicked on to start the details intent ************
		//********
		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.mob_list_item, mobs);
		mylist.setAdapter(adapter);
		mylist.setItemsCanFocus(false);
		mylist.setTextFilterEnabled(true);*/
		adapter = new SimpleAdapter(this, list, R.layout.mob_list_item,
				new String[] {"class", "name", "topic"},
				new int[] {R.id.class_field, R.id.name_field, R.id.topic_field});
		mylist.setAdapter(adapter);
	}
	
	public void onClick(View v) {
		
		if (v.equals(findViewById(R.id.back))) {
			finish();
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// When clicked, store the username into selectedFriend
		@SuppressWarnings("rawtypes")
		HashMap selected_item = (HashMap) mylist.getItemAtPosition(position);
		
		Bundle b = new Bundle();
		b.putString("mobname", selected_item.get("name").toString());
		b.putInt("mobid", Integer.parseInt(selected_item.get("groupid").toString()));
		Intent i = new Intent(this, MobDetails.class);
		i.putExtras(b);
		startActivity(i);
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
