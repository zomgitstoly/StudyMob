package com.ecs160.studymob;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateStudyMob extends Activity {
	private String response;
	private ArrayList<String> classes = StudyMob.classes;
	private ArrayList<String> locations = StudyMob.locations;
	private ArrayList<String> depts = StudyMob.depts;
	private ArrayList<String> filtered_classes = new ArrayList<String>();
	private String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	private String days[] = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17",
							 "18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	private String maxsizes[] = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17",
			 					"18","19","20"};
	private String years[] = {"2011","2012"};
	private String times[] = {"1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00","12:00"};
	private String ampm_arr[] = {"AM","PM"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_study_mob);

		setDateTime();
		setLocations();
		setDept();
		setMaxSize();
	}
	
	public void onClick(View v) {
		if (v.equals(findViewById(R.id.create_mob))) {
			// If "Create Mob" button is clicked, then send form info to server.
			EditText name_field = (EditText) findViewById(R.id.group_name_field);
			EditText topic_field = (EditText) findViewById(R.id.topic_field);
			Spinner class_field = (Spinner) findViewById(R.id.class_spinner);
			Spinner location_field = (Spinner) findViewById(R.id.location_spinner);
			Spinner month_field = (Spinner) findViewById(R.id.months_spinner);
			Spinner day_field = (Spinner) findViewById(R.id.days_spinner);
			Spinner year_field = (Spinner) findViewById(R.id.year_spinner);
			Spinner time_field = (Spinner) findViewById(R.id.time_spinner);
			Spinner ampm_field = (Spinner) findViewById(R.id.ampm_spinner);
			Spinner maxsize_field = (Spinner) findViewById(R.id.maxsize_spinner);
			Spinner end_month_field = (Spinner) findViewById(R.id.end_months_spinner);
			Spinner end_day_field = (Spinner) findViewById(R.id.end_days_spinner);
			Spinner end_year_field = (Spinner) findViewById(R.id.end_year_spinner);
			Spinner end_time_field = (Spinner) findViewById(R.id.end_time_spinner);
			Spinner end_ampm_field = (Spinner) findViewById(R.id.end_ampm_spinner);

			String group_name = name_field.getText().toString().trim();
			String topic = topic_field.getText().toString().trim();
			String class_name = class_field.getSelectedItem().toString().trim();
			String location = location_field.getSelectedItem().toString().trim();
			String month = month_field.getSelectedItem().toString().trim();
			String day = day_field.getSelectedItem().toString().trim();
			String year = year_field.getSelectedItem().toString().trim();
			String time = time_field.getSelectedItem().toString().trim();
			String ampm = ampm_field.getSelectedItem().toString().trim();
			int maxsize = Integer.parseInt(maxsize_field.getSelectedItem().toString());
			String end_month = end_month_field.getSelectedItem().toString().trim();
			String end_day = end_day_field.getSelectedItem().toString().trim();
			String end_year = end_year_field.getSelectedItem().toString().trim();
			String end_time = end_time_field.getSelectedItem().toString().trim();
			String end_ampm = end_ampm_field.getSelectedItem().toString().trim();
			
			// returns group_id on success
			response =StudyMob.model.newMob(Login.mainuser.getUserID(), class_name, group_name, topic, location,
					month, day, year, time, ampm, maxsize, end_month, end_day, end_year, end_time, end_ampm);
			if (!response.equals("Error: could not insert")) {
				//StudyMob.mobs.add(created_mob);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("StudyMob successfully Created!")
				       .setCancelable(false)
				       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                CreateStudyMob.this.finish();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
			else {
				Toast.makeText(this, "There seems to be an error creating this group.", Toast.LENGTH_SHORT).show();
			}
			
		}
		else if (v.equals(findViewById(R.id.cancel))) {
			finish();
		}
	}
	
	private void setLocations() {
		locations.add("--");
		Spinner location_spinner = (Spinner) findViewById(R.id.location_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,locations);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		location_spinner.setAdapter(adapter);
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
		        setClasses(selected_dept);
		    }

		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }
		});
	}
	
	private void setClasses(String selected_dept) {
		Spinner class_spinner = (Spinner) findViewById(R.id.class_spinner);
		if (selected_dept.equals("--")) {
			class_spinner.setEnabled(false);
		}
		else {
			class_spinner.setEnabled(true);
			filtered_classes.clear();
			filtered_classes.add("--");
			// Filter the classes string.
			String pull_dept;
			for (int i = 0; i < classes.size(); i++) {
				pull_dept = classes.get(i).substring(0,3);
				if (pull_dept.equals(selected_dept))
					filtered_classes.add(classes.get(i));
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,filtered_classes);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			class_spinner.setAdapter(adapter);
		}
	}
	
	private void setDateTime() {
		/* START DATE/TIME */
		Spinner months_spinner = (Spinner) findViewById(R.id.months_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,months);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		months_spinner.setAdapter(adapter);
		
		Spinner days_spinner = (Spinner) findViewById(R.id.days_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,days);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		days_spinner.setAdapter(adapter);
		
		Spinner years_spinner = (Spinner) findViewById(R.id.year_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,years);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		years_spinner.setAdapter(adapter);
		
		Spinner time_spinner = (Spinner) findViewById(R.id.time_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,times);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		time_spinner.setAdapter(adapter);
		
		Spinner ampm_spinner = (Spinner) findViewById(R.id.ampm_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ampm_arr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ampm_spinner.setAdapter(adapter);
		
		/* END DATE/TIME */
		Spinner end_months_spinner = (Spinner) findViewById(R.id.end_months_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,months);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		end_months_spinner.setAdapter(adapter);
		
		Spinner end_days_spinner = (Spinner) findViewById(R.id.end_days_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,days);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		end_days_spinner.setAdapter(adapter);
		
		Spinner end_years_spinner = (Spinner) findViewById(R.id.end_year_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,years);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		end_years_spinner.setAdapter(adapter);
		
		Spinner end_time_spinner = (Spinner) findViewById(R.id.end_time_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,times);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		end_time_spinner.setAdapter(adapter);
		
		Spinner end_ampm_spinner = (Spinner) findViewById(R.id.end_ampm_spinner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ampm_arr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		end_ampm_spinner.setAdapter(adapter);
	}
	
	private void setMaxSize() {
		Spinner sp = (Spinner) findViewById(R.id.maxsize_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,maxsizes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
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
