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

public class InviteList extends ListActivity {
	
	private String response;
	private ListView mylist;
	private ArrayList<String> mobs = new ArrayList<String>();
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter;
	protected static int selected_class_id;
	protected static int selected_group_id;
	protected static int selected_user_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getIntent().getExtras();
		
		setContentView(R.layout.invites);
		TextView test = (TextView) findViewById(R.id.invites);
		test.setText("Invite: Please Choose a person to add to the group");
		mylist = (ListView)findViewById(android.R.id.list); 
		
		selected_class_id = b.getInt("class_id");
		selected_group_id = b.getInt("group_id");
		//****Getting the consumers for the class
		response = StudyMob.model.getGroupConsumers(selected_class_id);
		Log.i("Response", response);
		
		JSONArray groups_json = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			groups_json = json.getJSONArray("consumers");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		size = groups_json.length();
		String user_id;
		String user_name;
		if(size > 0){
			for (int i = 0; i < size; i++) {
				try {
					JSONObject temporary_json = groups_json.getJSONObject(i);	
					user_id = temporary_json.getString("user_id");
	
					//Ask Server for the name of the class
					user_name = StudyMob.model.getUser(Integer.parseInt(user_id));
	
					mobs.add(user_name + " : " + user_id);
					HashMap<String,String> temp = new HashMap<String,String>();
					temp.put("userid", user_id);
					temp.put("name", user_name);
					if(list.contains(temp) == false){
						list.add(temp);
					}
	
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		

			//Getting the Providers
			response = StudyMob.model.getGroupProviders(selected_class_id);
			Log.i("Response", response);
			
			groups_json = null;
			size = 0;
			try {
				JSONObject json = new JSONObject( response );
				groups_json = json.getJSONArray("providers");
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			size = groups_json.length();
			user_id = null;
			user_name = null;
	
			for (int i = 0; i < size; i++) {
				try {
					JSONObject temporary_json = groups_json.getJSONObject(i);	
					user_id = temporary_json.getString("user_id");
	
					//Ask Server for the name of the class
					user_name = StudyMob.model.getUser(Integer.parseInt(user_id));
	
					mobs.add(user_name + " : " + user_id);
					HashMap<String,String> temp = new HashMap<String,String>();
					temp.put("userid", user_id);
					temp.put("name", user_name);
					if(list.contains(temp) == false){
						list.add(temp);
					}
	
				}  catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			adapter = new SimpleAdapter(this, list, R.layout.mob_list_invite,
					new String[] {"name"},
					new int[] {R.id.name_field});
			mylist.setAdapter(adapter);
		}
	}
	
	public void onClick(View v) {
		
		if (v.equals(findViewById(R.id.back))) {
			this.finish();
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// When clicked, store the username into selectedFriend
		@SuppressWarnings("rawtypes")
		HashMap selected_item = (HashMap) mylist.getItemAtPosition(position);
		selected_user_name = Integer.parseInt(selected_item.get("userid").toString());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to invite this user?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   	//**SEND REQUEST TO SERVER WITH THE ACCEPT
		        	   	StudyMob.model.inviteToGroup(selected_user_name,selected_group_id);
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
