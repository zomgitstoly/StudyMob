package com.ecs160.studymob;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class StudyMob extends Activity {
	protected static ServerModel model = new ServerModel();
	public static final String CLASS_PREF = "catalog";
	private String response;
	protected static ArrayList<String> classes = new ArrayList<String>();
	protected static ArrayList<String> locations = new ArrayList<String>();
	protected static ArrayList<String> depts = new ArrayList<String>();
	private ProgressDialog pd;
	private AsyncBlock task = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        task = new AsyncBlock();
        task.execute();
    }
    
    private void setClasses(String response) {
		JSONArray the_json_array = null;
		int size;
		try {
			JSONObject json = new JSONObject(response);
			the_json_array = json.getJSONArray("classes");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array
						.getJSONObject(i);
				//Log.i(this.toString(), another_json_object.getString("name"));
				classes.add(another_json_object.getString("name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    private void setLocations(String response) {
		locations.add("Select a Location");
		
		JSONArray the_json_array = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			the_json_array = json.getJSONArray("locations");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array.getJSONObject(i);
				//Log.i( this.toString() , another_json_object.getString("name") );
				locations.add(another_json_object.getString("name"));
			}  catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
    
    private void setDpt(String response){
		depts.add("Select a Department");
		JSONArray the_json_array = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			the_json_array = json.getJSONArray("depts");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		size = the_json_array.length();
		for (int i = 0; i < size; i++) {
			try {
				JSONObject another_json_object = the_json_array.getJSONObject(i);
				//Log.i( this.toString() , another_json_object.getString("dept") );
				depts.add(another_json_object.getString("dept"));
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
    }
    private void start() {		
		SharedPreferences settings = getSharedPreferences(CLASS_PREF, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    String shrd_classes;
	    String shrd_dpts;
	    String shrd_locs;
		//task = new AsyncBlock();
		//ArrayList<String> send = new ArrayList<String>();
	   // send.add("getClassUpdate");
		//IF WE HAVE AN OLD VERSION
	    //If we haven't fetched catalog yet, or if the time we have doesn't equal to the time on the server then we initliaze from the start
		String catalog_time = settings.getString("time", "No Time"); //Time of the Catalog we have
		String current_time = model.getClassUpdate();
		if(catalog_time.compareTo("No Time") == 0 || catalog_time.compareTo(current_time) != 0) 
		{
			Log.i("INIT", "WE DO NOT HAVE THE JSONS OR ITS AN OLD VERSION");
			editor.putString("time", current_time);
			//Get the Departments
			response = model.getDepts();

			//send.add("getDepts");
			//task.execute(send);
			editor.putString("departments", response); //Put the department JSON into shared preferences
			setDpt(response);
			
			//Get the Classes
			response = model.getClasses();
		    //Putting the JSON for classes
			editor.putString("classes", response);	
			setClasses(response);
			
			//Get the Locations
			response = model.getLocations();
		    //Putting the JSON for locations
			editor.putString("locations", response);	
			setLocations(response);
			
			editor.commit();
		}
		//ELSE WE JUST TAKE THE JSONS WE HAVE STORED AND TRANSLATE THEM
		else{
				Log.i("INIT", "WE HAVE THE JSONS STORED");
				//Set the Classes with the JSON stored
				shrd_classes = settings.getString("classes", "Doesnt Exists");
				if(shrd_classes.compareTo("Doesnt Exists") != 0){
					setClasses(shrd_classes);
				}
				else{
					Log.i("INIT", "ERROR CLASSES NOT IN SHARED PREFFENCES");
				}
				
				//Set the Departments using the JSON Stored
				shrd_dpts = settings.getString("departments", "Doesnt Exists");
				if(shrd_dpts.compareTo("Doesnt Exists") != 0){
					setDpt(shrd_dpts);
				}
				else{
					Log.i("INIT", "ERROR DEPARTMENTS NOT IN SHARED PREFFENCES");
				}
				
				//Set the Departments using the JSON Stored
				shrd_locs = settings.getString("locations", "Doesnt Exists");
				if(shrd_locs.compareTo("Doesnt Exists") != 0){
					setLocations(shrd_locs);
				}
				else{
					Log.i("INIT", "ERROR LOCATIONS NOT IN SHARED PREFFENCES");
				}
		}
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
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

    private class AsyncBlock extends AsyncTask<Void, Void, Void> {
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		showProgress();
    	}

		@Override
		protected Void doInBackground(Void...params) {
			start();
			//Log.i("ASYNC TASK RESPONSE", response);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (!isCancelled())
				notifyFinish();
		}		
    }
    
    private void notifyFinish() {
    	if (pd != null) pd.dismiss();
    	Toast.makeText(this, "Done Loading", Toast.LENGTH_LONG).show();
    }
    private void showProgress() {
		
		// This will show a progress dialog on the screen
		pd = new ProgressDialog(this);
		pd.setTitle("");
		pd.setMessage("Loading...");
		pd.setButton("Cancel", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				pd.cancel();
			}
		});
		pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				if (task != null) task.cancel(true);
				Toast.makeText(StudyMob.this, "Cancelled", Toast.LENGTH_SHORT).show();
			}
		});
		pd.show();
    }
}

