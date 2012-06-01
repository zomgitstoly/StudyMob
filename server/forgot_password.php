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
			#submit { width: 100%; }
			#form {
				text-align: center;
				margin: auto;
				width: 500px;
				height: 124px;
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
		</style>
		<script type="text/javascript" src="debug/jQuery_1.7.2.js"></script>
		<script type="text/javascript">
			$( document ).ready( function() {
				$( "#submit" ).click( function() {
					var email = "<? echo $_GET["email"]; ?>";
					var password = $( "#password" ).val();
					var token = "<? echo $_GET["token"]; ?>";
					$( "#submit" ).off( 'click' );
					$.get( "./action.php" , { action: "update_password" , email: email , password: password , token: token } , function ( data ) {
						$( "#message" ).css( "display" , "block" );
						if ( data.length > 0 ) {
							$( "#message" ).html( data );
						} else {
							$( "#message" ).html( "Your password was successfully changed!" );
						}
					} );
				} );
			} );
		</script>
	</head>
	<body>
		<div id="message"></div>
		<div id="form">
			<div class="title">
				StudyMob [Password Changer]
			</div>
			<form>
				<table>
					<tr><td>Email:</td><td><? echo $_GET["email"]; ?></td></tr>
					<tr><td>New Password:</td><td><input type="password" id="password" value="" /></td></tr>
					<tr><td colspan="2"><input type="button" value="Submit" id="submit" /></td></tr>
				</table>
			</form>
		</div>
	</body>
</html>