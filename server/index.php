<html>
	<head>
		<style type="text/css">
			BODY { background-color: #F3F3F3 }
			#message {
				display : none ;
				margin: auto;
				width: 500px;
				height: 40px;
				line-height: 20px;
				font-size: 14px;
				letter-spacing: 2px;
				font-family: Arial;
				border-radius: 10px;
				border: 5px solid #333;
				padding: 4px;
				text-align: center;
				background-color: #fff;
				margin-bottom: 8px;
			}
			#log, #create { width: 100%; }
			#content {
				text-align: center;
				margin: auto;
				width: 500px;
				border-radius: 10px;
				border: 5px solid #777;
				background-color: #FDFDFD;
				font-family: Arial;
				letter-spacing: 1px;
				font-size: 14px;
				line-height: 20px;
				padding: 4px;
			}
			#password {
				width: 360px
			}
			.title {
				font-family: Arial;
				font-size: 18px;
				letter-spacing: 2px;
				font-weight: bold;
				border-radius: 5px;
				background-color: #fff;
				line-height: 32px;
				padding: 4px;
				border-bottom: 2px solid #000;
			}
			#signup_form {
				display: none;
				width: 100%;
			}
			input {
				width: 100%;
			}
			#signup_form select {
				width: 100%;
			}
		</style>
		<script type="text/javascript" src="debug/jQuery_1.7.2.js"></script>
		<script type="text/javascript">
			$( document ).ready( function() {
				var user;
				var consume_list;
				var provide_list;
				$.get( "./action.php" , { action: "get_classes" } , function( data ) {
					var len = data.length;
					for ( i = 0 ; i < len; i++ ) {
						var el = $( document.createElement( "option" ) );
						var el2 = $( document.createElement( "option" ) );
						$( el ).html( data[ i ]["name"] );
						$( el ).attr( "value" , data[ i ]["class_id"] );
						$( el ).attr( "id" , "consume_" + data[ i ]["class_id"] );
						$( el2 ).html( data[ i ]["name"] );
						$( el2 ).attr( "value" , data[ i ]["class_id"] );
						$( el2 ).attr( "id" , "provide_" + data[ i ]["class_id"] );
						$( el ).appendTo( $( "#consume_list" ) );
						$( el2 ).appendTo( $( "#provide_list" ) );
					}
				} , "json" );
				$( "#forgot" ).click( function() {
					var email = $( "#email" ).val();
					$.get( "./action.php" , { action: "forgot_password" , email: email } , function( data ) {
						$( "#message" ).css( "display" , "block" );
						$( "#message" ).html( "Check your email dude." );
					} );
				} );
			
				$( "#log" ).click( logIn );
				$( "#create" ).click( function() {
					if ( $( "#signup_form" ).css( "display" )=="none" ) {
						$( "#signup_form" ).css( "display" , "block" );
					} else {
						// add the account
						var email = $( "#email" ).val();
						var password = $( "#password" ).val();
						var first_name = $( "#first_name" ).val();
						var last_name = $( "#last_name" ).val();
						
						var selected_list = $( "#consume_list option:selected" );
						var len = selected_list.length;
						consume = [];
						for ( i = 0; i < len; i++ )
						{
							consume.push( $( selected_list[ i ] ).attr( "value" ) );
						}
						
						selected_list = $( "#provide_list option:selected" );
						len = selected_list.length;
						provide = [];
						for ( i = 0; i < len; i++ )
						{
							provide.push( $( selected_list[ i ] ).attr( "value" ) );
						}
						
						$.get( "./action.php" , { action: "new_account" , email: email , password: password , first_name: first_name , last_name: last_name , provide_list: provide , consume_list: consume } , function ( data ) {
							$( "#message" ).css( "display" , "block" );
							console.log( data );
							if ( data.length > 0 ) {
								// if there was an error
								$( "#message" ).css( "display" , "block" );
								$( "#message" ).html( data );
							} else {
								$( "#message" ).css( "display" , "none" );
								$( "#message" ).html( "" );
								logIn();
							}
						} );
					}
				} );
				function logIn() {
					var email = $( "#email" ).val();
					var password = $( "#password" ).val();
					console.log( "trying to log in" );
					$.get( "./action.php" , { action: "login" , email: email , password: password } , function ( data ) {
						$( "#message" ).css( "display" , "block" );
						console.log( "beep");
						if ( data > 0 ) {
							$( "#form" ).css( "display" , "none" );
							$( "#message" ).html( "Logging in..." );
							$.get( "./action.php" , { action: "get_user" , user_id: data } , function ( data ) {
								user = data[ 0 ];
								console.log( user );
								$( "#message" ).html( "Welcome back " + user[ "first_name" ] + " " + user[ "last_name" ] + "." );
								$.get( "./action.php" , { action: "get_user_provide" , user_id: user[ "user_id" ] } , function ( data ) {
									provide_list = data[ 0 ];
								} , "json" );
								$.get( "./action.php" , { action: "get_user_consume" , user_id: user[ "user_id" ] } , function ( data ) {
									consume_list = data[ 0 ];
								} , "json" );
							} , "json" );
						} else if ( data.length > 0 ) {
							$( "#message" ).html( data );
							if ( data=="forgot password successful" ) {
								// show the update password page
							}
						} 
					} );
				}
			} );
		</script>
	</head>
	<body>
		<div id="message"></div>
		<div id="content">
			<div class="title">
				StudyMob
			</div>
			<form id="form">
				<table>
					<tr><td>Email:</td><td><input type="text" id="email" /></td></tr>
					<tr><td>Password:</td><td><input type="password" id="password" value="" /></td></tr>
					<tr><td colspan="2">
						<table id="signup_form">
							<tr><td>First Name:</td><td><input type="text" id="first_name" value="" /></td></tr>
							<tr><td>Last Name:</td><td><input type="text" id="last_name" value="" /></td></tr>
							<tr><td>Provide Classes:</td><td>
								<select multiple="multiple" id="provide_list">
								</select>
							</td></tr>
							<tr><td>Consume Classes:</td><td>
								<select multiple="multiple" id="consume_list">
								</select>
							</td></tr>
						</table>
					</td></tr>
					<tr><td><input type="button" value="Create Account" id="create" /></td><td><input type="button" value="Forgot Password" id="forgot" /><input type="button" value="Log In" id="log" /></td></tr>
				</table>
			</form>
		</div>
	</body>
</html>