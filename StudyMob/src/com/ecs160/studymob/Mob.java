package com.ecs160.studymob;

import java.util.ArrayList;

public class Mob {
	private String name;
	private String course;
	private String date;
	private String time;
	private String location;
	private String topic;
	private User owner;
	private ArrayList<User> members = new ArrayList<User>();
	
	/* Constructor */
	public Mob(String name, String course, String date, String time, String topic, String location, User owner) {
		this.name = name;
		this.course = course;
		this.date = date;
		this.time = time;
		this.location = location;
		this.topic = topic;
		this.owner = owner;
		// initially 0 members when Mob first created
	}
	
	/* Setter Methods*/
	public void setName(String name) { this.name = name; }
	public void setCourse(String course) { this.course = course; }
	public void setDate(String date) { this.date = date; }
	public void setTime(String time) { this.time = time; }
	public void setLocation(String location) { this.location = location; }
	public void setTopic(String topic) { this.topic = topic; }
	
	/* Getter Methods */
	public String getName() { return name; }
	public String getCourse() { return course; }
	public String getDate() { return date; }
	public String getTime() { return time; }
	public String getLocation() { return location; }
	public String getTopic() { return topic; }
	public User getOwner() { return owner; }
	
	/* Add a member to the mob */
	public void addMember(User member) { members.add(member); }
	/* Delete a member from the mob */
	public void deleteMember(User member) { members.remove(member); }
	
	
}
