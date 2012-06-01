package com.ecs160.studymob;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class EditProviders extends ListActivity {
	protected int user_id= Login.mainuser.getUserID();
	protected String teststring;
	private ArrayList<String> cur_classes = new ArrayList<String>();
	private String response;

	private ListView mylist;
	private int count;
	private ArrayList<String> classes = StudyMob.classes;
	private ArrayList<String> depts = StudyMob.depts;
	private ArrayList<String> filtered_classes = new ArrayList<String>();
	private ArrayList<String> checked_classes = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.providers);
		count =0;
		response = StudyMob.model.getUserProvide(user_id);
		Button action = (Button) findViewById(R.id.cont);
		action.setText("Submit");
		
		JSONArray the_json_array = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			the_json_array = json.getJSONArray("providers");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array.getJSONObject(i);
				Log.i( this.toString() , another_json_object.getString("name") );
				cur_classes.add(another_json_object.getString("name"));
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for(int i =0; i< cur_classes.size(); i++){
			checked_classes.add(cur_classes.get(i));
		}
		setDept();
	}
	private void initList(){
		for(int i =0; i< filtered_classes.size(); i++){
			String temp = mylist.getItemAtPosition(i).toString();
			if(cur_classes.contains(temp)){
				mylist.setItemChecked(i, true);
			}
		}
		
	}
	
	private void addChecked(){
		SparseBooleanArray  a = mylist.getCheckedItemPositions();
		for (int j = 0; j < filtered_classes.size(); j++) {
			if (a.get(j) == true && checked_classes.contains(filtered_classes.get(j)) == false) {
				Log.i("Checked Classes", filtered_classes.get(j));
				checked_classes.add(filtered_classes.get(j));
			}//If it isnt checked and we have it in the classes checked remove it.
			else if (a.get(j) == false){
				if(checked_classes.contains(filtered_classes.get(j))){
					Log.i("Removed Classes", filtered_classes.get(j));
					checked_classes.remove(filtered_classes.get(j));
				}
			}
		}
	}
	
	public void onClick(View v) {
		// When a new account is created, we want to send the information to the server to be added into the database.
		if (v.equals(findViewById(R.id.cont))) {
			// If the Continue button is pressed, send the checked classes to server to add to database
			
			//String checked[] = new String[classes.length];

			// Scan selected items to see if checked. If checked, add to array list.
			SparseBooleanArray  a = mylist.getCheckedItemPositions();
			for (int j = 0; j < filtered_classes.size(); j++) {
				if (a.get(j) == true && checked_classes.contains(filtered_classes.get(j)) == false) {
					Log.i("Checked Classes", filtered_classes.get(j));
					checked_classes.add(filtered_classes.get(j));
					//checked[j] = classes[j];
				}//If it isnt checked and we have it in the classes checked remove it.
				else if (a.get(j) == false){
					if(checked_classes.contains(filtered_classes.get(j))){
						Log.i("Removed Classes", filtered_classes.get(j));
						checked_classes.remove(filtered_classes.get(j));
					}
				}
			}
			response = StudyMob.model.editProviders(checked_classes, user_id);
			
			// If everything is validated, then go 
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to change the classes you can provide help for?")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   	response = StudyMob.model.editProviders(checked_classes, user_id);
			        	   	Toast.makeText(EditProviders.this, "List of classes you can help with has been changed successfully", Toast.LENGTH_LONG).show();
			    			Intent i = new Intent(EditProviders.this, EditProfile.class);
			    			startActivity(i);
			                EditProviders.this.finish();
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
		
		else if (v.equals(findViewById(R.id.back))) {
			Intent i = new Intent(this, EditProfile.class);
			startActivity(i);
			finish();
		}
	}
	
	private void setDept() {
		depts.add("--");
		Spinner dept_spinner = (Spinner) findViewById(R.id.dept_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,depts);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dept_spinner.setAdapter(adapter);
		
		// When the user changes a department, we want to filter the class spinner to display
		// a list of classes that correspond with the department selected.
		dept_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        Spinner spinner = (Spinner) findViewById(R.id.dept_spinner);
		        String selected_dept = spinner.getSelectedItem().toString().trim();
		        if(count > 0){
		        	Log.i("Addidng Classes", "addCheckedMethod called");
		        	addChecked();
		        }
		        count++;
		        setClasses(selected_dept);
		    }

		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});
	}
	
	private void setClasses(String selected_dept) {
		filtered_classes.clear();
		filtered_classes.add("None");
		String pull_dept;
		for (int i = 0; i < classes.size(); i++) {
			pull_dept = classes.get(i).substring(0,3);
			if (pull_dept.equals(selected_dept))
				filtered_classes.add(classes.get(i));
		}
		
		mylist = (ListView)findViewById(android.R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, filtered_classes);
		mylist.setAdapter(adapter);
		mylist.setItemsCanFocus(false);
		mylist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		mylist.setTextFilterEnabled(true);
		initList();
	}
	
	/* Handle when the back button is pressed. After the Twitter 'Accept' webpage
     * has issued you a pin, you need to enter it, and the pressing the back button allows you
     * to go back to your Application start page, as opposed to ending the Activity.
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
        	finish(); //finish the webpage activity
			Intent i = new Intent(this, EditProfile.class);
			startActivity(i);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
