package com.ecs160.studymob;

import java.util.ArrayList;

public class User {
	private int userid;
	private String fname;
	private String lname;
	private String email;
	private ArrayList<User> mobsters = new ArrayList<User>();
	private ArrayList<String> providers = new ArrayList<String>();
	private ArrayList<String> consumers = new ArrayList<String>();
	
	/* Constructor */
	public User() {
		userid = 0;
		fname = "";
		lname = "";
		email = "";
	}
	
	public void setUser (int userid, String fname, String lname, String email) {
		this.userid = userid;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
	}
	
	/* Setter Methods */
	public void setName(String fname, String lname) { 
		this.fname = fname; 
		this.lname = lname; 
	}
	public void setEmail(String email) { this.email = email; }
	public void setUserID(int userid) { this.userid = userid; }
	
	/* Getter Methods */
	public int getUserID() { return userid; }
	public String getName() { return fname + " " + lname; }
	public String getFName() { return fname; }
	public String getLName() { return lname; }
	public String getEmail() { return email; }
	
	/* Add buddy to mobster list */
	public void addMobster(User buddy) { mobsters.add(buddy); }
	/* Remove buddy from mobster list */
	public void removeMobster(User ex_buddy) { mobsters.remove(ex_buddy); }
	/* Add course to providers list */
	public void addProvider(String course) { providers.add(course); }
	/* Remove course from providers list */
	public void removeProvider(String course) { providers.remove(course); }
	/* Add course to consumers list */
	public void addConsumer(String course) { consumers.add(course); }
	/* Remove course from consumers list */
	public void removeConsumer(String course) { consumers.remove(course); }

	
}
