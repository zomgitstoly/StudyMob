package com.ecs160.studymob;

import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MapMenu extends MapActivity {

	private String response;
	protected static Hashtable locationid = new Hashtable();
	protected static WebServer server = new WebServer();
	protected boolean isRouteDisplayed() {
	    return false;
	}
	MapView mapView;
	MapController test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_maps);
		mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    GeoPoint davis = getPoint(38.552305,-121.745025);
	    test = mapView.getController();
	    test.setCenter(davis);	
	    
	    //Setting up the Overlay
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.studybook); //Free Stock Source: http://unrestrictedstock.com/projects/office-icons-free-stock-vector-set/
	    HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
	    
		JSONObject get_locations = new JSONObject();
		//JSONObject classes = new JSONObject();
		
		try {
			get_locations.put("action","get_locations");
		} catch (JSONException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
		}
		response = server.sendHttpRequest(get_locations);
        JSONArray json_location = null;
		int size;
		try {
			JSONObject json = new JSONObject( response );
			json_location = json.getJSONArray("locations");
		}  catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		size = json_location.length();
		double temporary_lat;
		double temporary_long;
		String loc_name;
		String str_count;
		//Gets all the locations and sets up the overlay
		for (int i = 0; i < size; i++) {
			try {
				JSONObject temporary_json = json_location.getJSONObject(i);
				JSONObject count_json = new JSONObject();
				Log.i( this.toString() , temporary_json.getString("name") );
				
				temporary_lat =  Double.parseDouble(temporary_json.getString("latitude"));
				temporary_long = Double.parseDouble(temporary_json.getString("longitude"));
				
				//get the count
				try {
					count_json.put("action","get_groups_in_location_count");
					count_json.put("location_id", temporary_json.getString("location_id"));
				} catch (JSONException e) {
		   			// TODO Auto-generated catch block
		   			e.printStackTrace();
				}
				str_count = server.sendHttpRequest(count_json);
				Log.i("the count is" , str_count );
				locationid.put(temporary_json.getString("name"), temporary_json.getString("location_id"));
				
				GeoPoint pin_geo = getPoint(temporary_lat,temporary_long);
				loc_name = temporary_json.getString("name");
				OverlayItem pin = new OverlayItem(pin_geo,loc_name, str_count);
				itemizedoverlay.addOverlay(pin);
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    mapOverlays.add(itemizedoverlay);
	    
	    //Looking for a new location
	    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

	}


	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      // Called when a new location is found by the network location provider.
	      Log.i("GPS", "Recieved a new One");
	      moveMap(location);
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}

	  };

	// Register the listener with the Location Manager to receive location updates
	void moveMap(Location location){
		double lat = location.getLatitude();
		double log = location.getLongitude();
		int lat2 = (int) (lat *1000000);
		int log2 = (int) (log *1000000);
		GeoPoint current = new GeoPoint(lat2,log2);
		test.setCenter(current);
		test.setZoom(14);
		
	}
	
	private GeoPoint getPoint(double lat, double lon) {
	    Log.i("Davis","getPoint");
	    return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
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
