package com.ecs160.studymob;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Providers extends ListActivity {
	protected int user_id = NewAccount.user_id;
	private String response;
	private ListView mylist;
	private ArrayList<String> classes = StudyMob.classes;
	private ArrayList<String> depts = StudyMob.depts;
	private ArrayList<String> filtered_classes = new ArrayList<String>();
	private ArrayList<String> checked_classes = new ArrayList<String>();
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.providers);
		count =0;
		setDept();
	}
	
	public void onClick(View v) {
		// When a new account is created, we want to send the information to the server to be added into the database.
		if (v.equals(findViewById(R.id.cont))) {
			// If the Continue button is pressed, send the checked classes to server to add to database
			
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
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You sure you want to submit the list of classes you can provide help for?")
			       .setCancelable(false)
			       .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   	response = StudyMob.model.provideClassList(checked_classes, user_id);
			        	   	Log.i(this.toString(), "TESTING: " + response);
			    			Intent i = new Intent(Providers.this, Consumers.class);
			    			startActivity(i);
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
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
        	finish(); //finish the webpage activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
