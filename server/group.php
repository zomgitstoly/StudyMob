<?php
// the 2 lines below lets you use a class_id
//	function createGroup( $user_id , $class_id , $name , $topic , $location_id , $time ) {
//		$query = 'INSERT INTO `group` ( user_id , class_id , name , topic , location_id , time ) VALUE ( '.$user_id.' , '.$class_id.' , "'.$name.'" , "'.$topic.'" , '.$location_id.' , "'.$time.'" )';
	function createGroup( $user_id , $class_name , $group_name , $topic , $location , $time , $end_time , $maxsize ) {
// the line below DOSE NOT include end time
//	function createGroup( $user_id , $class_name , $group_name , $topic , $location , $time , $maxsize ) {
		$query = "SELECT location_id FROM location WHERE name = '$location'";
		$result = mysql_query( $query );
		$row = mysql_fetch_array( $result );
		$location_id = $row[ 0 ];
		
		$query = "SELECT class_id FROM class WHERE name = '$class_name'";
		$result = mysql_query( $query );
		$row = mysql_fetch_array( $result );
		$class_id = $row[ 0 ];
		
		$group_name = db_clean_data( $group_name );
		$topic = db_clean_data( $topic );
		$location = db_clean_data( $location );
	
		$query = "INSERT INTO `group` ( user_id , class_id , name , topic , location_id , time , end_time , max_size ) VALUES ( ".$user_id.", ".$class_id.", '".$group_name."', '".$topic."', ".$location_id." , '".$time."', '".$end_time."', ".$maxsize." )";
// commented line DOES NOT include end_time
//		$query = "INSERT INTO `group` ( user_id , class_id , name , topic , location_id , time , max_size ) VALUES ( ".$user_id.", ".$class_id.", '".$group_name."', '".$topic."', ".$location_id." , '".$time."', ".$maxsize." )";
		$result = mysql_query( $query );
		if ( !$result )
			return "Error: could not insert";
		
		return mysql_insert_id();
	}
	
	function getGroups() {
		$query = "SELECT * FROM `group` WHERE time > date_add( CURRENT_TIMESTAMP , INTERVAL -1 DAY ) AND time < date_add( CURRENT_TIMESTAMP , INTERVAL 6 DAY ) AND end_time < CURRENT_TIMESTAMP";
		return parseObject( "groups" , db_run_query( $query ) );
	}
	
	function getGroup( $group_id ) {
		if ( !is_numeric( $group_id ) )
			return "Error: invalid group id (not a number)";
		if ( $group_id < 0 )
			return "Error: invalid group id (groups cannot be negative)";
		$query = "SELECT * FROM `group` WHERE group_id=".$group_id;
		return parseObject( "group" , db_run_query( $query ) );
	}
	
	function getGroupsInLocation( $location_id ) {
		$query = "SELECT * FROM `group` WHERE location_id=".$location_id." AND time > date_add( CURRENT_TIMESTAMP , INTERVAL -1 DAY ) AND time < date_add( CURRENT_TIMESTAMP , INTERVAL 6 DAY ) AND end_time < CURRENT_TIMESTAMP";
		return parseObject( "groups" , db_run_query( $query ) );
	}
	
	function getGroupsInLocationCount( $location_id ) {
		$query = "SELECT count( group_id ) AS number FROM `group` WHERE location_id=".$location_id." AND time > date_add( CURRENT_TIMESTAMP , INTERVAL -1 DAY ) AND time < date_add( CURRENT_TIMESTAMP , INTERVAL 6 DAY ) AND end_time < CURRENT_TIMESTAMP";
		$result = mysql_query( $query );
		$row = mysql_fetch_array( $result );
		return $row[ 0 ];
	}
	
	function getGroupsInClass( $class_name ) {
		$query = "SELECT * FROM class WHERE name = '$class_name'";
		$result = db_run_query( $query );
		$class_id = $result[0][ "class_id" ];
		
		$query = "SELECT * FROM `group` WHERE class_id=".$class_id." AND time > date_add( CURRENT_TIMESTAMP , INTERVAL -1 DAY ) AND time < date_add( CURRENT_TIMESTAMP , INTERVAL 6 DAY ) AND end_time < CURRENT_TIMESTAMP";
		return parseObject( "groups" , db_run_query( $query ) );
	}
	
	function joinGroup( $group_id , $user_id  ) {
		// check if the friend has been invited already
		$query = "DELETE FROM `user_metadata` WHERE user_id=".$user_id." AND title='invite' AND value='".$group_id."'";
		$result = mysql_query( $query );
	
		// check if they already are in the group
		$query = "SELECT value FROM `group_metadata` WHERE group_id=".$group_id." AND title='member' AND value='".$user_id."'";
		$result = mysql_query( $query );
		if ( mysql_num_rows( $result ) > 0 ) {
			return "Already in group";
		}
		
		// check if they already are in the group
		$query = "SELECT value FROM `group_metadata` WHERE group_id=".$group_id." AND title='joinrequest' AND value='".$user_id."'";
		$result = mysql_query( $query );
		if ( mysql_num_rows( $result ) > 0 ) {
			return "Already invited";
		}
	
		// check if the friend has been invited already
		$query = "SELECT user_id FROM `user_metadata` WHERE user_id=".$user_id." AND title='invite' AND value='".$group_id."'";
		$result = mysql_query( $query );
		if ( mysql_num_rows( $result ) > 0 ) {
			return "Already invited to group";
		}

		$query = "SELECT max_size FROM `group` WHERE group_id=".$group_id;
		$result = mysql_query( $query );
		$row = mysql_fetch_array( $result );
		$max_size = $row[ 0 ];
		
		$query = 'INSERT INTO `group_metadata` ( group_id , title , value ) SELECT '.$group_id.',"joinrequest","'.$user_id.'" FROM `group_metadata` WHERE group_id='.$group_id.' AND title="member" HAVING COUNT(title) < '.$max_size;
		$result = mysql_query( $query );
		
		if ( $result==false )
			return "Error: could not insert";
		else if ( mysql_affected_rows( )==0 )
			return "Error: max size";
				
		return mysql_insert_id();
	}
	
	function leaveGroup( $group_id , $user_id  ) {
		$query = 'DELETE FROM `group_metadata` WHERE group_id='.$group_id.' AND title="member" AND value="'.$user_id.'"';
		$result = mysql_query( $query );
		if ( !$result )
			return "Error: could not insert";
		
		// update owner's notifications saying that a user left the group.
			
		return mysql_insert_id();
	}
	
	function getGroupUsers( $group_id ) {
		if ( !is_numeric( $group_id ) )
			return "Error: invalid group id (not a number)";
		if ( $group_id < 0 )
			return "Error: invalid group id (groups cannot be negative)";
		//$query = "SELECT value AS name FROM `group_metadata` WHERE group_id='.$group_id.' AND title='member'";
		$query = "SELECT value FROM `group_metadata` WHERE title='member' AND group_id = $group_id";
		return parseObject( "users" , db_run_query( $query ) );
	}
	
	function inviteToGroup( $user_id , $group_id ) {
		// check if the friend is in the group
		$query = "SELECT value FROM `group_metadata` WHERE group_id=".$group_id." AND title='member' AND value='".$user_id."'";
		$result = mysql_query( $query );
		if ( mysql_num_rows( $result ) > 0 ) {
			return "Already in group";
		}
	
		// check if the friend has been invited already
		$query = "SELECT user_id FROM `user_metadata` WHERE user_id=".$user_id." AND title='invite' AND value='".$group_id."'";
		$result = mysql_query( $query );
		if ( mysql_num_rows( $result ) > 0 ) {
			return "Already invited to group";
		}
		
		$query = "SELECT max_size FROM `group` WHERE group_id=".$group_id;
		$result = mysql_query( $query );
		$row = mysql_fetch_array( $result );
		$max_size = $row[ 0 ];
		
		// otherwise invite them
		$query = 'INSERT INTO `user_metadata` ( user_id , title , value ) SELECT '.$user_id.',"invite","'.$group_id.'" FROM `group_metadata` WHERE group_id='.$group_id.' AND title="member" HAVING COUNT(title) < '.$max_size;
		$result = mysql_query( $query );
		if ( !$result )
			return "Error: could not insert";
		else if ( mysql_num_rows( $query )==0 )
			return "Error: max size";
			
	}
	
	function getJoinRequests( $group_id ) {
		$query = "SELECT value, first_name, last_name FROM `group_metadata` G, `user` U WHERE group_id=".$group_id." AND title='joinrequest' AND G.value = U.user_id";
		return parseObject( "joinrequests" , db_run_query( $query ) );
	}
	
	function getOwnedGroups( $user_id ) {
		$query = "SELECT * FROM `group` WHERE user_id=".$user_id;
		return parseObject( "groups" , db_run_query( $query ) );
	}
	
	function acceptJoin( $user_id , $group_id ) {
		$query = "DELETE FROM `user_metadata` WHERE user_id=".$user_id." AND title='invite' AND value='".$group_id."'";
		$result = mysql_query( $query );
		
		$query = "DELETE FROM `group_metadata` WHERE group_id = " . $group_id . " AND value = " . $user_id;
		$result = mysql_query( $query );
		
		$query = "SELECT max_size FROM `group` WHERE group_id=".$group_id;
		$result = mysql_query( $query );
		$row = mysql_fetch_array( $result );
		$max_size = $row[ 0 ];
		
		$query = "SELECT max_size FROM `group` WHERE group_id=".$group_id;
		$result = mysql_query( $query );
		$row = mysql_fetch_array( $result );
		$max_size = $row[ 0 ];
		
		$query = 'INSERT INTO `group_metadata` ( group_id , title , value ) SELECT '.$group_id.',"member","'.$user_id.'" FROM `group_metadata` WHERE group_id='.$group_id.' AND title="member" HAVING COUNT(title) < '.$max_size;
		$result = mysql_query( $query );
		if ( !$result )
			return "Error: could not insert";
		else if ( mysql_num_rows( $query )==0 )
			return "Error: max size";
		
		return "added to group!";
	}
	
	function denyJoin( $user_id , $group_id ) {
		$query = "DELETE FROM `user_metadata` WHERE user_id=".$user_id." AND title='invite' AND value='".$group_id."'";
		$result = mysql_query( $query );
		
		$query = "DELETE FROM `group_metadata` WHERE group_id=".$group_id." AND title='joinrequest' AND value=".$user_id;
		$result = mysql_query( $query );
	}
	
	function getGroupConsumers( $class_id ) {
		$query = "SELECT * FROM `user_metadata` WHERE title = 'consume' AND value = $class_id";
		return parseObject( "consumers" , db_run_query( $query ) );
	}

	function getGroupProviders( $class_id ) {
		$query = "SELECT * FROM `user_metadata` WHERE title = 'provide' AND value = $class_id";
		return parseObject( "providers" , db_run_query( $query ) );
	}
	
	function getMemberStatus( $user_id , $group_id ) {
		$query = "SELECT * FROM `group_metadata` WHERE title = 'member' AND value = $user_id AND group_id = $group_id";
		$result = mysql_query( $query );
		if( mysql_num_rows( $result ) > 0 )
			return "in group";
		else
			return "not in group";
	}
	
	function getGroupsByUserId( $user_id ) {
		$query = "SELECT * FROM `group` WHERE group_id IN (SELECT group_id FROM `group_metadata` WHERE value = $user_id AND title = 'member')";
		return parseObject( "groups" , db_run_query( $query ) );
	}
	
?>