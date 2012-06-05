<?php
	include_once( "db_inc.php" );
	include_once( "include.php" );
	include_once( "class.php" );
	
	function logIn( $email , $password ) {
		if ( !isset( $email ) ) {
			return "ERROR: please input an email";
		}
		if ( !isset( $password ) ) {
			return "ERROR: please input a password";
		}
		if ( !validEmail( $email ) ) {
			return "ERROR: invalid email";
		}
		if ( !validPassword( $password ) ) {
			return "ERROR: invalid password";
		}
		$query = "SELECT user_id FROM user_metadata WHERE title='forgot' AND value='".$password."'";
		$result = mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) > 0 ) {
			return "forgot password successful";
		}
		
		$password = md5( $password );
		$query = "SELECT user_id FROM user WHERE email='".$email."' AND password='".$password."'";
		$result = mysql_query( $query );
		if ( mysql_num_rows( $result ) > 0 ) {
			$row = mysql_fetch_assoc( $result );
			return $row[ "user_id" ];
		}
		return "ERROR: could not log in with provided email: ".$email;
	}	// verify login information
	
	function signUp( $email , $password , $first_name , $last_name ) {
		if ( !isset( $email ) ) {
			return "ERROR: please input an email";
		}
		if ( !isset( $password ) ) {
			return "ERROR: please input a password";
		}
		if ( !isset( $first_name ) ) {
			return "ERROR: please input a first name";
		}
		if ( !isset( $last_name ) ) {
			return "ERROR: please input a last name";
		}
		if ( !validEmail( $email ) ) {
			return "ERROR: invalid email";
		}
		if ( !validPassword( $password ) ) {
			return "ERROR: invalid password";
		}
		
		$query = "SELECT user_id FROM user WHERE email='".$email."'";
		$result = mysql_query( $query );
		if ( !$result ) {
			return "ERROR: could not process query";
		}
			
		if ( mysql_num_rows( $result ) > 0 ) {
			return "ERROR: the email is already registered";
		}
		
		$password = md5( $password );
		$query = "INSERT INTO user( email , password , first_name , last_name ) VALUES ( '".$email."' , '".$password."' , '".$first_name."' , '".$last_name."' )";
		$result = mysql_query( $query );
		if ( !$result ) {
			return "ERROR: could not create new account";
		}
		
		return mysql_insert_id();
	}
	
	function getUser( $user_id ) {
		$query = "SELECT * FROM user WHERE user_id=".$user_id;
		$result = db_run_query( $query );
		$string = $result[0][ "first_name" ] . ", " . $result[0][ "last_name" ];
		return $string;
		//return parseObject( "user" , db_run_query( $query ) );
	}
	
	function getUserInfo( $user_id ) {
		$query = "SELECT user_id, email, first_name, last_name FROM user WHERE user_id=".$user_id;
		return parseObject( "user" , db_run_query( $query ) );
	}
	
	// returns a list of the user's provide classes in JSON format
	function getUserProvide( $user_id ) {
		$query = "SELECT name FROM class C, user_metadata U WHERE U.user_id = $user_id AND U.title = 'provide' AND C.class_id = U.value";
		return parseObject( "providers" , db_run_query( $query ) );
	}

	// returns a list of the user's consume classes in JSON format
	function getUserConsume( $user_id ) {
		$query = "SELECT name FROM class C, user_metadata U WHERE U.user_id = $user_id AND U.title = 'consume' AND C.class_id = U.value";
		return parseObject( "consumers" , db_run_query( $query ) );
	}
	
	function forgotPassword( $email ) {
		$query = "SELECT user_id FROM user WHERE email='".$email."'";
		$result = mysql_query( $query );
		if ( !$result || mysql_num_rows( $result )==0 ) {
			return "ERROR: email not in database";
		}
		$token = randomString( 5 );
		$row = mysql_fetch_assoc( $result );
		$user_id = $row[ "user_id" ];
		// let's give them a forgot token
		$query = 'SELECT value FROM user_metadata WHERE title="forgot" AND user_id='.$user_id;
		$result = mysql_query( $query );
		if ( $result && mysql_num_rows( $result ) > 0 ) {	// delete it first
			$query = 'UPDATE user_metadata SET value="'.$token.'" WHERE title="forgot" AND user_id='.$user_id;
			mysql_query( $query );
		} else {		
			$query = 'INSERT INTO user_metadata (value,title,user_id) VALUES ("'.$token.'","forgot" , '.$user_id.')';
			mysql_query( $query );
		}
		
		$message = "You apparently forgot your password. Your temporary code is \"".$token."\". You can type it in as a password into the StudyMob program. Or you can click <a href='http://www.isrobin.com/ecs160/forgot_password.php?email=".$email."&token=".$token."'>here</a> to change your password.";
		$headers  = 'MIME-Version: 1.0' . "\r\n";
		$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
		$headers .= 'From: ecs160_god@isrobin.com' . "\r\n";
		
		mail( $email , "StudyMob: Awesome [Forgot Password]" , $message , $headers );
		return $email;
	}
	
	function updatePassword( $email , $password , $token ) {
		// checks if the password is valid
		if ( !validPassword( $password ) ) {
			return "ERROR: invalid password";
		}
		// checks if the email exists
		$query = "SELECT user_id FROM user WHERE email='".$email."'";
		$result = mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) == 0 ) {
			return "Error: invalid email";
		}
		$row = mysql_fetch_assoc( $result );
		$user_id = $row[ "user_id" ];
		// checks if the token is valid
		$query = "SELECT user_metadata_id FROM user_metadata WHERE title='forgot' AND user_id=".$user_id." AND value='".$token."'";
		$result = mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) == 0 ) {
			return "Error: invalid token";
		}
		
		// delete the temporary password from the forgot in the metadata
		$query = "DELETE FROM user_metadata WHERE title='forgot' AND user_id=".$user_id;
		mysql_query( $query );
		
		// set the password
		$password = md5( $password );
		$query = "UPDATE user SET password='".$password."' WHERE user_id=".$user_id;
		mysql_query( $query );
	}
	
	function editAccount( $user_id , $hash , $first_name , $last_name ) {
		$query = "SELECT user_id FROM user WHERE user_id=".$user_id." and password='".$hash."'";
		mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) == 0 ) {
			return "Error: it does not seem you are logged in properly";
		}
		
		$query = "UPDATE user SET first_name='".$first_name."', last_name='".$last_name."'";
		mysql_query( $query );
	}
	
	function editPassword( $user_id , $hash , $password ) {
		$query = "SELECT user_id FROM user WHERE user_id=".$user_id." and password='".$hash."'";
		mysql_query( $query );
		if ( !$result || mysql_num_rows( $result ) == 0 ) {
			return "Error: it does not seem you are logged in properly";
		}
		$password = md5( $password );
		
		$query = "UPDATE user SET password='".$password."' WHERE user_id=".$user_id;
		mysql_query( $query );
		echo $password;
	}
	
	function getBuddies( $user_id ) {
		$query = "SELECT * FROM user_metadata WHERE title='buddy' and user_id = $user_id";
		return parseObject( "buddies" , db_run_query( $query ) );
	}
	
	function addBuddy( $user_id , $friend_id ) {
		$query = "INSERT INTO user_metadata ( user_id , title , value ) VALUES ( ".$user_id." , 'buddy' , '".$friend_id."' )";
		mysql_query( $query );
		return mysql_insert_id();
	}
	
	function removeBuddy( $user_id , $friend_id ) {
		$query = "DELETE FROM user_metadata WHERE user_id=".$user_id." AND title='buddy' AND value='".$friend_id."'";
		mysql_query( $query );
	}
	
	function getInviteRequests( $user_id ) {
		$query = "SELECT * FROM `user_metadata` WHERE title='invite' AND user_id = $user_id";
		return parseObject( "invites" , db_run_query( $query ) );
	}
	
?>