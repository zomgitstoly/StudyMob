<?php
	include_once( "db_inc.php" );
	
	function provideClassList( $user_id , $provide_list ) {
		// add "provide" classes
		$len = 0;
		$tok = strtok( $provide_list, " [,]" );
		while ($tok !== false) {
			$provide[$len] = $tok;
			$tok = strtok( " [,]" );
			$len++;
		}
		
		//$len = count( $provide_list );
		for ( $i = 0 ; $i < $len ; $i++ ) {
			provideClass( $user_id , $provide[ $i ] );
		}
		return "successfully added";
	}
	
	function provideClass( $user_id , $provide ) {
//		$query = "SELECT class_id FROM class WHERE class_id=".$provide;
		$query = "SELECT class_id FROM class WHERE name='$provide'";
		$result = mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) == 0 )
			return "class no longer exists";
		$row = mysql_fetch_row( $result );
		
		// check if the user is already in the class or not
		$query = "SELECT value FROM user_metadata WHERE title='provide' AND user_id=".$user_id." and value='".$row[ 0 ]."'";
		$result = mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) > 0 ) {
			echo "skipped a class ($query)\n";
			return;
		}
		
		$query = "INSERT INTO user_metadata ( user_id , title , value ) VALUES ";
		$query .= "(" . $user_id . " , 'provide' , '".$row[ 0 ]."' );";
		$result = mysql_query( $query );
		if ( !$result )
			return "class (id #" . $row[0] . ") was not added (provide_list)";
	}
	
	function dropProvideClass( $user_id , $class ) {
		// remove the "provide" class		
		$query = "SELECT class_id FROM class WHERE class_id=". $class;
		$result = mysql_query( $query );
		if ( !result || mysql_num_rows( $result ) == 0 )
			return "class no longer exists";
		$row = mysql_fetch_row( $result );
		
		$query = "DELETE FROM user_metadata WHERE user_id=".$user_id." AND title='provide' AND value='".$row[ 0 ]."'";
		$result = mysql_query( $query );
		
		return "Successfully removed class";
	}
	
	function dropProvideClasses( $user_id ) {
		$query = "DELETE FROM user_metadata WHERE title='provide' and user_id=" .$user_id;
		mysql_query( $query );
	}
	
	function consumeClassList( $user_id , $consume_list ) {
		// add "consume" classes
		$len = 0;
		$tok = strtok( $consume_list, " [,]" );
		while ($tok !== false) {
			$consume[$len] = $tok;
			$tok = strtok( " [,]" );
			$len++;
		}
			
		for ( $i = 0 ; $i < $len ; $i++ ) {
			consumeClass( $user_id , $consume[ $i ] );
		}
		return "successfully added";
	}
	
	function consumeClass( $user_id , $consume ) {
//		$query = "SELECT class_id FROM class WHERE class_id=".$consume;
		$query = "SELECT class_id FROM class WHERE name='$consume'";
		$result = mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) == 0 )
			return "class (".$consume_list[ $i ].") no longer exists";
		$row = mysql_fetch_row( $result );
		
		// check if the user is already in the class or not
		$query = "SELECT value FROM user_metadata WHERE title='consume' AND user_id=".$user_id." and value='".$row[ 0 ]."'";
		$result = mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) > 0 ) {
			echo "skipped a class\n";
			return;
		}
		
		// add the class
		$query = "INSERT INTO user_metadata ( user_id , title , value ) VALUES ";
		$query .= "(" . $user_id . " , 'consume' , '".$row[ 0 ]."' );";
		$result = mysql_query( $query );
		if ( !$result )
			return "class (id #" . $row[0] . ") was not added (consume_list)";
	}
	
	function dropConsumeClass( $user_id , $class ) {
		// remove the "provide" class		
		$query = "SELECT class_id FROM class WHERE class_id=". $class;
		$result = mysql_query( $query );
		if ( !result || mysql_num_rows( $result ) == 0 )
			return "class no longer exists";
		$row = mysql_fetch_row( $result );
		
		$query = "DELETE FROM user_metadata WHERE user_id=".$user_id." AND title='consume' AND value='".$row[ 0 ]."'";
		$result = mysql_query( $query );
		
		return "Successfully removed class";
	}
	
	function dropConsumeClasses( $user_id ) {
		$query = "DELETE FROM user_metadata WHERE title='consume' and user_id=" .$user_id;
		mysql_query( $query );
	}
	
	// returns a list of classes in JSON format
	function getClasses( ) {
		$query = "SELECT * FROM class";
		return parseObject( "classes" , db_run_query( $query ) );
	}
	
	function getClass( $class_id ) {
		$query = "SELECT * FROM class WHERE class_id='".$class_id."'";
		return parseObject( "class" , db_run_query( $query ) );
	}
	
	function getClassesInDept( $dept ) {
		$query = "SELECT * FROM class WHERE dept='".$dept."'";
		return parseObject( "classes" , db_run_query( $query ) );
	}
	
	function getDepts( ) {
		$query = "SELECT DISTINCT dept FROM class";
		$result = db_run_query( $query );
		
		return parseObject( "depts" , $result );
	}
	
	function getClassUpdate() {
		$query = "SELECT time FROM `update` WHERE table_name='class'";
		$result = mysql_query( $query );
		$row = mysql_fetch_row( $result );
		
		return $row[ 0 ];
	}
?>