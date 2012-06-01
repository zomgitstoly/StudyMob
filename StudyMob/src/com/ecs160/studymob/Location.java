package com.ecs160.studymob;

import java.util.ArrayList;

public class Location {
	private String name;
	private double latitude;
	private double longitude;
	private ArrayList<Mob> mobs = new ArrayList<Mob>();
	
	/* Constructor */
	public Location (String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/* Setter methods */
	public void setName(String name) { this.name = name; }
	public void setLat(double latitude) { this.latitude = latitude; }
	public void setLong(double longitude) { this.longitude = longitude; }
	
	/* Getter methods */
	public String getName() { return name; }
	public double getLat() { return latitude; }
	public double getLong() { return longitude; }
	
	/* Add a mob to location */
	public void addMob(Mob mob) { mobs.add(mob); }
	/* Delete a mob from location */
	public void deleteMob(Mob mob) { mobs.remove(mob); }
	
}
