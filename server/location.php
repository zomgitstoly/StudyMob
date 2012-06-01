<?php
	function getLocations() {
		$query = "SELECT * FROM location";
		return parseObject( "locations" , db_run_query( $query ) );
		//return json_encode( db_run_query( $query ) );
	}
	
	function getLocation( $location_id ) {
		$query = "SELECT * FROM location WHERE location_id = $location_id";
		$result = db_run_query( $query );
		return $result[0][ "name" ];
	}
?>