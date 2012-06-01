package com.ecs160.studymob;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private long last_time = -1; //Used to do double click
	private long this_time; //Used to do double click
	@Override
	protected OverlayItem createItem(int i) {

		return mOverlays.get(i);
	}
	
	public HelloItemizedOverlay(Drawable defaultMarker) {
		  super(boundCenter(defaultMarker));
		}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenter(defaultMarker));
		  mContext = context;
	}
	
	protected boolean onTap(int index) {
		  this_time = System.currentTimeMillis();
		  Log.i("testing double tap", "lasttime is "  + last_time);
		  Log.i("testing double tap", "thistime is "  + this_time);
		  if(this_time - last_time < 600){ //Sets the amount of time to figure out if double click or single
			  Toast.makeText(mContext, "Double Clicked Loading Mob List", Toast.LENGTH_SHORT);
			  OverlayItem item = mOverlays.get(index);
			  last_time = -1;
			  Log.i("testing double tap", "double tapped");
			  Intent m = new Intent(mContext, LocationMobs.class);
			  Bundle b = new Bundle();
			  b.putString("name", item.getTitle()); //Put things into bundle so that can display groups for the location 
			  b.putString("location", MapMenu.locationid.get(item.getTitle()).toString());
			  Log.i("Map Location Id", MapMenu.locationid.get(item.getTitle()).toString());
			  m.putExtras(b);
			  mContext.startActivity(m);
		  }
		  else { //this is the single click
			  OverlayItem item = mOverlays.get(index);
			  Toast.makeText(mContext, item.getTitle() + " "+ item.getSnippet(), Toast.LENGTH_SHORT).show();
			  last_time = this_time;
		  }
		  
		  return true;
		}
}
