package com.ecs160.studymob;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyConsumers extends Activity {
	private int user_id = Login.mainuser.getUserID();
	private ListView mylist;
	private ArrayList<String> classes = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myconsumers);
		
		String response = StudyMob.model.getUserConsumer(user_id);
		
		JSONArray the_json_array = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			the_json_array = json.getJSONArray("consumers");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array.getJSONObject(i);
				Log.i( this.toString() , another_json_object.getString("name") );
				classes.add(another_json_object.getString("name"));
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mylist = (ListView)findViewById(android.R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mylist_item_center, classes);
		mylist.setAdapter(adapter);
		mylist.setItemsCanFocus(false);
	}
	
	public void onClick(View v) {
		if(v.equals(findViewById(R.id.back))) {
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
