<?php
	function db_run_query($query)
	{
		$result = mysql_query($query)
			or die("Error: " . mysql_error());
			
		$final = array();
		
		if(is_bool($result))
			return;
		
		while($row = mysql_fetch_assoc($result))
			array_push($final, $row);
		
		return $final;
	} //db_run_query
	
	function db_clean_data($data)
	{
		if(is_array($data))
		{
			foreach($data as &$value)
			{
				$value = mysql_real_escape_string($value);
				
				if(!is_string($value) && !is_int($value))
					trigger_error("Data not clean: " . $value);
			}
			return $data;
		} //if data in the form of an array
		else
		{
			$data = mysql_real_escape_string($data);
			if(is_string($data) || is_int($data))
				return $data;
			else
				trigger_error("Data not clean: " . $data);
		} //if data is by itself
	} //db_clean_data
?>