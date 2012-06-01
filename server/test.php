<?php
	include_once( "db_inc.php" );	// sql functions
	include_once( "include.php" );
	$con = mysql_connect("localhost","isrobin_ecs160","2yvllbJFHK%1");
	if (!$con)
	  die('Could not connect: ' . mysql_error());
	mysql_select_db( "isrobin_ecs160" , $con );
	
	$query = "SELECT * FROM `group_metadata` WHERE title='member' AND group_id = 12";
	$result = db_run_query( $query );
	
	foreach($result as $r) {
		echo $r[ "value" ] . "<br>";
	}

?>