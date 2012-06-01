package com.ecs160.studymob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MobMembers extends Activity {
	private ListView mylist;
	private int mob_id;
	private String mob_name;
	private String owner;
	private String owner_email;
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mob_member_list);
		
		Bundle b = getIntent().getExtras();
		mob_id = b.getInt("mob_id");
		mob_name = b.getString("mob_name");
		owner = b.getString("owner");
		owner_email = b.getString("owner_email");
		
		setTitle();
		getMembers();
		setOwner();
		
		mylist = (ListView)findViewById(R.id.memberlist);
		adapter = new SimpleAdapter(this, list, R.layout.member_list_item,
				new String[] {"id", "name", "email"},
				new int[] {R.id.id_field, R.id.name_field, R.id.email_field});
		mylist.setAdapter(adapter);
	}
	
	private void getMembers() {
		String response = StudyMob.model.getGroupUsers(mob_id);
		
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
					HashMap<String,String> temp = new HashMap<String,String>();
					memberid = another_json_object.getInt("value");
					temp.put("id",Integer.toString(memberid));
					temp.put("name", getMemberName(memberid));
					temp.put("email", getMemberEmail(memberid));
					list.add(temp);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private String getMemberName(int memberid) {
		String response = StudyMob.model.getUser(memberid);
		StringTokenizer st = new StringTokenizer(response, ",");
		String fname = st.nextToken();
		String lname = st.nextToken();
		return fname + " " + lname;
	}
	
	private String getMemberEmail(int memberid) {
		String response = StudyMob.model.getUserInfo(memberid);
		JSONArray the_json_array = null;
		try {
			JSONObject json = new JSONObject( response );
			the_json_array = json.getJSONArray("user");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject another_json_object;
		String member_email = "";
		try {
			another_json_object = the_json_array.getJSONObject(0);
			member_email = another_json_object.getString("email");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return member_email;
	}
	
	private void setOwner() {
		TextView name = (TextView) findViewById(R.id.owner);
		TextView email = (TextView) findViewById(R.id.owner_email);
		
		name.setText(owner);
		email.setText(owner_email);
	}
	
	private void setTitle() {
		TextView v = (TextView) findViewById(R.id.title);
		v.setText("Members of " + mob_name);
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
