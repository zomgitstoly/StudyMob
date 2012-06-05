<?php
include_once( "user.php" );		// user information
include_once( "db_inc.php" );	// sql functions
include_once( "include.php" );	// some functions that are shared
include_once( "class.php" );	// class functions (adding classes)
include_once( "location.php" );	// location functions
include_once( "group.php" );	// group functions
 
$con = mysql_connect("localhost","isrobin_ecs160","2yvllbJFHK%1");
//$con = mysql_connect("localhost","isrobin_ecs160","bobiscool");
if (!$con)
  die('Could not connect: ' . mysql_error());
mysql_select_db( "isrobin_ecs160" , $con );

$json = json_decode( file_get_contents("php://input") , true );
if ( isset($_GET["action"]) ) { // for debugging purposes
	$json = $_GET;
}

if (!isset($json))
	echo "no json";
elseif (!isset( $json['action'] ))
	echo "no action";
else {
	switch ( $json['action'] ) {
		case "login":
			// basic functionality works
			// this one returns the user_id on success
			echo logIn( $json[ "email" ] , $json[ "password" ] );
			break;
		case "new_account":
			// basic functionality works
			// returns success message on success
			// else returns an error
			echo signUp( $json[ "email" ] , $json[ "password" ] , $json[ "first_name" ] , $json[ "last_name" ] );
			break;
		case "provide_class_list":
			// basic functionality works
			// returns success message on success
			// else returns an error
			echo provideClassList( $json[ "user_id" ] , $json["class_list"] );
			break;
		case "consume_class_list":
			// basic functionality works
			// returns success message on success
			// else returns an error
			echo consumeClassList( $json[ "user_id" ] , $json["class_list" ] );
			break;
		case "add_provide_class":
			// basic functionality works
			// returns success message on success
			// else returns an error
			echo provideClass( $json[ "user_id" ] , $json[ "class" ] );
			break;
		case "add_consume_class":
			// basic functionality works
			// returns success message on success
			// else returns an error
			echo consumeClass( $json[ "user_id" ] , $json[ "class" ] );
			break;
		case "drop_provide_class":
			// basic functionality works
			// returns success message on success
			// else returns an error
			echo dropProvideClass( $json[ "user_id" ] , $json[ "class" ] );
			break;
		case "drop_consume_class":
			// basic functionality works
			// returns success message on success
			// else returns an error
			echo dropConsumeClass( $json[ "user_id" ] , $json[ "class" ] );
			break;
		case "get_class":
			echo getClass( $json[ "class_id" ] );
			break;
		case "get_classes":
			// basic functionality works
			// returns a JSON array of all classes
			// else returns an error
			echo getClasses();
			break;
		case "get_user_provide":
			// basic functionality works
			// returns a JSON array of provide classes
			// else returns an error
			echo getUserProvide( $json[ "user_id" ] );
			break;
		case "get_user_consume":
			// basic functionality works
			// returns a JSON array of consume classes
			// else returns an error
			echo getUserConsume( $json[ "user_id" ] );
			break;
		case "login_success":
			echo isNewAccount( $json[ "email" ] );
			break;
		case "old_account":
			echo oldAccount( $json[ "email" ] );
			break;
		case "forgot_password":
			echo forgotPassword( $json[ "email" ] );
			break;
		case "update_password":
			echo updatePassword( $json[ "email" ] , $json[ "password" ] , $json["token"] );
			break;
		case "create_group":
		// returns error on error
		// returns group_id on success
			$monthList[ "Jan" ] = "01";
			$monthList[ "Feb" ] = "02";
			$monthList[ "Mar" ] = "03";
			$monthList[ "Apr" ] = "04";
			$monthList[ "May" ] = "05";
			$monthList[ "Jun" ] = "06";
			$monthList[ "Jul" ] = "07";
			$monthList[ "Aug" ] = "08";
			$monthList[ "Sep" ] = "09";
			$monthList[ "Oct" ] = "10";
			$monthList[ "Nov" ] = "11";
			$monthList[ "Dec" ] = "12";
			if ( strlen( $json[ "day" ] ) < 2 ) {
				$day = "0" . $json[ "day" ];
			} else {
				$day = $json[ "day" ];
			}
			$month = $monthList[ $json[ "month" ] ];
			$hour_minute = explode( ":" , $json[ "time" ] );
			$hour = $hour_minute[ 0 ];
			$meridian = $json[ "ampm" ];
			
			if ( $meridian=="PM" ) {
				$hour += 12;
			} else if ( $hour==12 ) {
				// it has to be AM
				// so just reduce it to 0
				$hour = 0;
			}
			if ( strlen( $hour ) < 2 ) {
				$hour = "0" . $hour;
			}
			
			if ( strlen( $json[ "day_end" ] ) < 2 ) {
				$day_end = "0" . $json[ "day_end" ];
			} else {
				$day_end = $json[ "day_end" ];
			}
			
			$time = $json[ "year" ] . "-" . $month . "-" . $day . " " . $hour . ":" . $hour_minute[ 1 ] . ":00";
			/*
			this has the end_time stuff
			$month_end = $monthList[ $json[ "month_end" ] ];
			$hour_minute_end = explode( ":" , $json[ "time_end" ] );
			$hour_end = $hour_minute_end[ 0 ];
			$meridian_end = $json[ "ampm_end" ];
			
			if ( $meridian_end=="PM" ) {
				$hour_end += 12;
			} else if ( $hour_end==12 ) {
				// it has to be AM
				// so just reduce it to 0
				$hour_end = 0;
			}
			if ( strlen( $hour_end ) < 2 ) {
				$hour_end = "0" . $hour_end;
			}
			$time_end = $json[ "year" ] . "-" . $month_end . "-" . $day_end . " " . $hour_end . ":" . $hour_minute_end[ 1 ] . ":00";
		*/
			echo createGroup( $json[ "user_id" ] , $json[ "class_name" ] , $json[ "group_name" ] , $json[ "topic" ] , $json[ "location" ] , $time , $json[ "maxsize" ] );
			// line below includes end_time
//			echo createGroup( $json[ "user_id" ] , $json[ "class_name" ] , $json[ "group_name" ] , $json[ "topic" ] , $json[ "location" ] , $time , $end_time , $json[ "maxsize" ] );
			break;
		case "join_group":
			echo joinGroup( $json[ "group_id" ] , $json[ "user_id" ] );
			break;
		case "leave_group":
			echo leaveGroup( $json[ "group_id" ] , $json[ "user_id" ] );
			break;
		case "get_group_users":
			echo getGroupUsers( $json[ "group_id" ] );
			break;
		case "get_group":
		// returns group info on success
		// otherwise error
			echo getGroup( $json[ "group_id" ] );
			break;
		case "get_groups":
			echo getGroups();
			break;
		case "get_locations":
		// returns the list of locations
			echo getLocations();
			break;
		case "get_location";
		// returns the location name
			echo getLocation( $json[ "location_id" ] );
			break;
		case "get_groups_in_location":
			echo getGroupsInLocation( $json[ "location_id" ] );
			break;
		case "get_groups_in_location_count":
			echo getGroupsInLocationCount( $json[ "location_id" ] );
			break;
		case "get_groups_in_class":
			echo getGroupsInClass( $json[ "class_name" ] );
			break;
		case "get_user":
			echo getUser( $json[ "user_id" ] );
			break;
		case "get_user_info":
			echo getUserInfo( $json[ "user_id" ] );
			break;
		case "edit_account":
			echo editAccount( $json[ "user_id" ] , $json[ "hash" ] , $json[ "first_name" ] , $json[ "last_name" ] );
			break;
		case "edit_password":
			// hash is the old password (hashed) REMEMBER TO SEND IT!
			// returns new hashed password on success
			echo editPassword( $json[ "user_id" ] , $json[ "hash" ] , $json[ "password" ] );
			break;
		case "edit_providers":
			dropProvideClasses( $json[ "user_id" ] );	// this neat function drops ALL provide classes
			echo provideClassList( $json[ "user_id" ] , $json["class_list" ] );
			break;
		case "edit_consumers":
			dropConsumeClasses( $json[ "user_id" ] );	// this neat function drops ALL consume classes
			echo consumeClassList( $json[ "user_id" ] , $json["class_list" ] );;
			break;
		case "get_depts":
			echo getDepts();
			break;
		case "get_classes_in_dept":
			echo getClassesInDept( $json[ "dept" ] );
			break;
		case "get_class_update":
			echo getClassUpdate();
			break;
		case "get_buddies":
			echo getBuddies( $json[ "user_id" ] );	// in user.php
			break;
		case "add_buddy":
			echo addBuddy( $json[ "user_id" ] , $json[ "friend_id" ] );	// in user.php
			break;
		case "remove_buddy":
			echo removeBuddy( $json[ "user_id" ] , $json[ "friend_id" ] );	// in user.php
			break;
		case "invite_to_group":
			echo inviteToGroup( $json[ "user_id" ] , $json[ "group_id" ] ); // in group.php
			break;
		case "get_join_requests":
			echo getJoinRequests( $json[ "group_id" ] );	// in group.php
			break;
		case "get_invite_requests":
			echo getInviteRequests( $json[ "user_id" ] ); // in user.php
			break;
		case "get_owned_groups":
			echo getOwnedGroups( $json[ "user_id" ] ); // in groups.php
			break;
		case "accept_join":
			echo acceptJoin( $json[ "user_id" ] , $json[ "group_id" ] );	// in groups.php
			break;
		case "deny_join":
			echo denyJoin( $json[ "user_id" ] , $json[ "group_id" ] );	// in groups.php
			break;
		case "get_group_consumers":
			echo getGroupConsumers( $json[ "class_id" ] );	// in groups.php
			break;
		case "get_group_providers":
			echo getGroupProviders( $json[ "class_id" ] );	// in groups.php
			break;
		case "get_member_status":
			echo getMemberStatus( $json[ "user_id" ] , $json[ "group_id" ] ); // in groups.php
			break;
		case "get_groups_by_user_id":
			echo getGroupByUserId( $json[ "user_id" ] );	// group.php
			break;
		default:
			echo "no action available";
			break;
	}
}
mysql_close($con);
?>
