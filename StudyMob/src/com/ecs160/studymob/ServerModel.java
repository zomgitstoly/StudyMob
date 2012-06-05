package com.ecs160.studymob;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerModel {
	protected static WebServer server = new WebServer();
	private String response = new String();

	public void ServerModel() {}
	
	public String Task(ArrayList<String> parameters){
			String exec = parameters.get(0);
			if(exec.compareTo("getClasses") == 0){
				response = getClasses();
			}
			else if(exec.compareTo("getLocations") == 0){
				response = getLocations();
			}
			else if(exec.compareTo("getDepts") == 0){
				response = getDepts();
			}
			else if(exec.compareTo("getClassUpdate") == 0){
				response = getClassUpdate();
			}
			else if(exec.compareTo("getClass") == 0){
				response = getClass(parameters.get(1));
			}
			else if(exec.compareTo("newAccount") == 0){
				response = newAccount(parameters.get(1),parameters.get(2),parameters.get(3),parameters.get(4));
			}
			else if(exec.compareTo("newMob") == 0){
				response = newMob(Integer.parseInt(parameters.get(1)), parameters.get(2), parameters.get(3),parameters.get(4), parameters.get(5), parameters.get(6), parameters.get(7),
						parameters.get(8), parameters.get(9), parameters.get(10), Integer.parseInt(parameters.get(11)), parameters.get(12), parameters.get(13),
						parameters.get(14), parameters.get(15), parameters.get(16));
			}
			else if(exec.compareTo("forgotPassword") == 0){
				response = forgotPassword(parameters.get(1));
			}
			else if(exec.compareTo("getGroupsinLocation") == 0){
				response = getGroupsinLocation(parameters.get(1));
			}
			else if(exec.compareTo("login") == 0){
				response = login(parameters.get(1),parameters.get(2));
			}
			else if(exec.compareTo("getUser") == 0){
				response = getUser(Integer.parseInt(parameters.get(1)));
			}
			else if(exec.compareTo("getLocationCount") == 0){
				response = getLocationCount(parameters.get(1));
			}
			else if(exec.compareTo("getUserConsumer") == 0){
				response = getUserConsumer(Integer.parseInt(parameters.get(1)));
			}
			else if(exec.compareTo("getUserProvide") == 0){
				response = getUserProvide(Integer.parseInt(parameters.get(1)));
			}
			else if(exec.compareTo("editUser") == 0){
				response = editUser(parameters.get(1), parameters.get(2), parameters.get(3));
			}
			else if(exec.compareTo("getClassesinDept") == 0){
				response = getClassesinDept(parameters.get(1));
			}
			else if(exec.compareTo("getClassesinDept") == 0){
				response = getClassesinDept(parameters.get(1));
			}
					
		return response;
	}
	
	public String signUp(String first_name, String last_name, String email, String password) {
		JSONObject new_account_info = new JSONObject();
		
		try {
			new_account_info.put("action", "new_account");
			new_account_info.put("first_name",first_name);
			new_account_info.put("last_name",last_name);
			new_account_info.put("email",email);
			new_account_info.put("password",password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return server.sendHttpRequest(new_account_info);
	}
	
	public String getClasses(){ //DONE
		JSONObject getclasses = new JSONObject();
		try {
			getclasses.put("action", "get_classes");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = server.sendHttpRequest(getclasses);
		return response;
	}
	
	public String getClass(String class_id){ //DONE
		JSONObject class_json = new JSONObject();
		try {
			class_json.put("action","get_class");
			class_json.put("class_id", class_id);
		} catch (JSONException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
		}
		response = server.sendHttpRequest(class_json);
		return response;
	}
	
	
	public String getLocations(){ //DONE
		JSONObject get_locations = new JSONObject();
		try {
			get_locations.put("action","get_locations");
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = server.sendHttpRequest(get_locations);
		return response;
	}
	
	public String getDepts(){ //DONE
		JSONObject get_dept = new JSONObject();
		try {
			get_dept.put("action","get_depts");
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = server.sendHttpRequest(get_dept);
		return response;
	}
	
	public String newAccount(String first_name, String last_name, String email, String password){ //DONE
		JSONObject new_account_info = new JSONObject();
		
		try {
			new_account_info.put("action", "new_account");
			new_account_info.put("first_name",first_name);
			new_account_info.put("last_name",last_name);
			new_account_info.put("email",email);
			new_account_info.put("password",password);
		} catch (JSONException e) {
	
			e.printStackTrace();
		}
		
		response = server.sendHttpRequest(new_account_info);
		return response;
	}
	
	public String consumerClassList(ArrayList<String> checked_classes, int user_id){ //???
		JSONObject providers = new JSONObject();
		
		try {
			providers.put("action", "consume_class_list");
			providers.put("class_list", checked_classes);
			providers.put("user_id", user_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = server.sendHttpRequest(providers);
		return response;
	}
	
	public String provideClassList(ArrayList<String> checked_classes, int user_id){ //???
		JSONObject providers = new JSONObject();
		
		try {
			providers.put("action", "provide_class_list");
			providers.put("class_list", checked_classes);
			providers.put("user_id", user_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = server.sendHttpRequest(providers);
		return response;
	}
	
	public String newMob(int user_id, String class_name, String group_name, String topic, String location, 
			String month, String day, String year, String time, String ampm, int max_size, String end_month,
			String end_day, String end_year, String end_time, String end_ampm){
		JSONObject new_mob = new JSONObject(); //DONE
		try {
			new_mob.put("action", "create_group");
			new_mob.put("user_id", user_id);
			new_mob.put("class_name", class_name);
			new_mob.put("group_name", group_name);
			new_mob.put("topic", topic);
			new_mob.put("location", location);
			new_mob.put("month", month);
			new_mob.put("day", day);
			new_mob.put("year", year);
			new_mob.put("time", time);
			new_mob.put("ampm", ampm);
			new_mob.put("maxsize", max_size);
			new_mob.put("end_month", end_month);
			new_mob.put("end_day", end_day);
			new_mob.put("end_year", end_year);
			new_mob.put("end_time", end_time);
			new_mob.put("end_ampm", end_ampm);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// returns group_id on success
		response = server.sendHttpRequest(new_mob);
		return response;
	}
	
	public String forgotPassword(String email){ //DONE
		JSONObject retrieve_password = new JSONObject();
		
		try {
			retrieve_password.put("action", "forgot_password");
			retrieve_password.put("email",email);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Send the information to the server for validation
		response = server.sendHttpRequest(retrieve_password);
		return response;
	}
	
	public String getGroupsinLocation(String location_id){ //DONE
		JSONObject request_provide = new JSONObject();
		try {
			request_provide.put("action","get_groups_in_location");
			request_provide.put("location_id",location_id);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		response = server.sendHttpRequest(request_provide);
		return response;
	}
	
	public String login(String email, String password){ //DONE
		JSONObject login_info = new JSONObject();
		
		try {
			login_info.put("action", "login");
			login_info.put("email",email);
			login_info.put("password",password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// Send login information to server for validation
		response = server.sendHttpRequest(login_info);
		return response;
	}
	
	public String getUser(int user_id){ //DONE
		JSONObject account_info = new JSONObject();
		try {
			account_info.put("action", "get_user");
		account_info.put("user_id",user_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = server.sendHttpRequest(account_info);
		return response;
	}
	
	public String getUserInfo(int user_id) {
		JSONObject account_info = new JSONObject();
		try {
			account_info.put("action", "get_user_info");
		account_info.put("user_id",user_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = server.sendHttpRequest(account_info);
		return response;
	}
	
	public String getLocationCount(String location_id){ //DONE
		JSONObject count_json = new JSONObject();
		try {
			count_json.put("action","get_groups_in_location_count");
			count_json.put("location_id", location_id);
		} catch (JSONException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
		}
		response = server.sendHttpRequest(count_json);
		return response;

	}
	
	public String getUserConsumer(int user_id){ //DONE
		JSONObject request_consume = new JSONObject();
		
		try {
			request_consume.put("action","get_user_consume");
			request_consume.put("user_id",user_id);
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response = server.sendHttpRequest(request_consume);
		return response;
	}
	
	public String getUserProvide(int user_id){ //DONE
		JSONObject request_provide = new JSONObject();
		
		try {
			request_provide.put("action","get_user_provide");
			request_provide.put("user_id",user_id);
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response = server.sendHttpRequest(request_provide);
		return response;
	}
	
	public String editProviders(ArrayList<String> checked_classes, int user_id){ //???
		JSONObject providers = new JSONObject();
		
		try {
			//TODO: Change action
			providers.put("action", "edit_providers");
			providers.put("class_list", checked_classes);
			providers.put("user_id", user_id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response = server.sendHttpRequest(providers);
		return response;
	}
	
	public String editUser(String first_name, String last_name, String email){ //DONE
		JSONObject new_account_info = new JSONObject();
		
		try {
			new_account_info.put("action", "edit_account");
			new_account_info.put("first_name",first_name);
			new_account_info.put("last_name",last_name);
			new_account_info.put("email",email);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(new_account_info);
		return response;
	}
	
	public String editConsumers(ArrayList<String> checked_classes, int user_id){ //???
		JSONObject providers = new JSONObject();
		
		try {
			providers.put("action", "edit_consumers");
			providers.put("class_list", checked_classes);
			providers.put("user_id", user_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(providers);
		return response;
	}
	
	public String getClassesinDept(String department){ //DONE
		JSONObject classes = new JSONObject();
		
		try {
			classes.put("action", "get_classes_in_dept");
			classes.put("dept", department);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(classes);
		return response;
	}
	
	public String getClassUpdate(){ //DONE
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_class_update");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String getJoinRequests(String group_id){ //DONE
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_join_requests");
			update.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String getOwnedGroups(int user_id){ //DONE
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_owned_groups");
			update.put("user_id", user_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String denyJoin(int user_id, int group_id){ //DONE
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "deny_join");
			update.put("user_id", user_id);
			update.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String acceptJoin(int user_id, int group_id){ //DONE
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "accept_join");
			update.put("user_id", user_id);
			update.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String inviteToGroup(int user_id, int group_id){ //DONE
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "invite_to_group");
			update.put("user_id", user_id);
			update.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String joinGroup(int user_id, int group_id){ //DONE
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "join_group");
			update.put("user_id", user_id);
			update.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String leaveGroup(int user_id, int group_id){ //DONE
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "leave_group");
			update.put("user_id", user_id);
			update.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String getGroup(int group_id){
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_group");
			update.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String getLocation(int location_id){
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_location");
			update.put("location_id", location_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String getGroupUsers(int group_id){
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_group_users");
			update.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String getGroupInClass(String class_name){
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_groups_in_class");
			update.put("class_name", class_name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	
	
	public String getGroupConsumers(int class_id){
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_group_consumers");
			update.put("class_id", class_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String getGroupProviders(int class_id){
		JSONObject update = new JSONObject();
		
		try {
			update.put("action", "get_group_providers");
			update.put("class_id", class_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		response = server.sendHttpRequest(update);
		return response;
	}
	
	public String getMemberStatus(int user_id, int group_id) {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_member_status");
			request.put("user_id", user_id);
			request.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return server.sendHttpRequest(request);
	}
	
	public String getMyMobs(int user_id) {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_groups_by_user_id");
			request.put("user_id", user_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return server.sendHttpRequest(request);
	}
	
	
	public String getInviteRequests(int user_id) {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "get_invite_requests");
			request.put("user_id", user_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return server.sendHttpRequest(request);
	
	}
	
	public String acceptInvite(int user_id,int group_id) {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "accept_join");
			request.put("user_id", user_id);
			request.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return server.sendHttpRequest(request);
	
	}
	
	public String denyInvite(int user_id,int group_id) {
		JSONObject request = new JSONObject();
		try {
			request.put("action", "deny_invite");
			request.put("user_id", user_id);
			request.put("group_id", group_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return server.sendHttpRequest(request);
	}

}

