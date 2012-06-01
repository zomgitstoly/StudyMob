package com.ecs160.studymob;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchMobs extends Activity {
	private ArrayList<String> filtered_classes = new ArrayList<String>();
	private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private ListView mylist;
	private SimpleAdapter adapter;
	private TextView nothing_selected;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_mobs);
		nothing_selected = (TextView) findViewById(R.id.nothing_selected);
		setTitle();
		Spinner sp = (Spinner) findViewById(R.id.class_spinner);
		sp.setEnabled(false);
		
		searchDept();
	}
	
	// Set the List of StudyMobs found through the search
	private void setList(String selected_class) {
		list.clear();
		
		// if nothing is selected
		if(selected_class.equals("--")) {
			nothing_selected.setVisibility(View.VISIBLE);
		}
		
		else {
			mylist = (ListView) findViewById(android.R.id.list);
			// TODO Finish up adapting the list and fetching info from the server
			getList(selected_class);
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
	}
	
	private void getList(String selected_class) {
		String response = StudyMob.model.getGroupInClass(selected_class);
		
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
		if (size == 0) {
			nothing_selected.setText("No Groups For This Class");
			nothing_selected.setVisibility(View.VISIBLE);
		}
		else
			nothing_selected.setVisibility(View.GONE);
		
		String group_id;
		String group_name;
		String group_topic;
		String class_name_srvr;
		String class_name;
		for (int i = 0; i < size; i++) {
			try {
				JSONObject temporary_json = groups_json.getJSONObject(i);
				Log.i( this.toString() , temporary_json.getString("name") );
				
				group_id = temporary_json.getString("group_id");
				group_name =  temporary_json.getString("name"); //name of the group
				group_topic = temporary_json.getString("topic"); //name of the topic
				//***** prob can can some code here to get the group_id to set up the details intent if they click on the item
				//***** might need to add new array to keep them in the same order as they appear so you know the index 

				//Ask Server for the name of the class
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
	}

	private void searchByClass(String selected_dept) {
		Spinner sp = (Spinner) findViewById(R.id.class_spinner);
		if (selected_dept.equals("Select a Department")) {
			sp.setEnabled(false);
		}
		else {
			sp.setEnabled(true);
			filtered_classes.clear();
			filtered_classes.add("--");
			// Filter the classes string.
			String pull_dept;
			for (int i = 0; i < StudyMob.classes.size(); i++) {
				pull_dept = StudyMob.classes.get(i).substring(0,3);
				if (pull_dept.equals(selected_dept))
					filtered_classes.add(StudyMob.classes.get(i));
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,filtered_classes);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp.setAdapter(adapter);
			
			sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
			        Spinner spinner = (Spinner) findViewById(R.id.class_spinner);
			        String selected_class = spinner.getSelectedItem().toString().trim();
			        setList(selected_class);
			    }

			    public void onNothingSelected(AdapterView<?> parentView) {
			        // your code here
			    }
			});
		}
	}
	
	private void searchDept() {
		Spinner sp = (Spinner) findViewById(R.id.dept_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, StudyMob.depts);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        Spinner spinner = (Spinner) findViewById(R.id.dept_spinner);
		        String selected_dept = spinner.getSelectedItem().toString().trim();
		        searchByClass(selected_dept);
		    }

		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});
	}
	
	private void setTitle() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("Search Study Mobs");
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
