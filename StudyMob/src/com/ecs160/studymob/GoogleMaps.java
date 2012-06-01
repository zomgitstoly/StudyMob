package com.ecs160.studymob;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class GoogleMaps extends MapActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_maps);
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    MapController test = mapView.getController();
	    GeoPoint davis = new GeoPoint(38552596,-121744836);
	    test.setCenter(davis);
	    test.setZoom(18);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
	
}


