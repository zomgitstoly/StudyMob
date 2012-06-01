<?php
$content = file_get_contents( "ucdavisclasses.txt" );
$lines = explode( "\n" , $content );
$class_list_all = array();
for ( $i = 0 ; $i < count( $lines ) - 1; $i++ ) {
	$word = explode( " " , $lines[ $i ] );
	if ( $word[ 0 ]!="U" )
		array_push( $class_list_all , $word[ 1 ] . $word[ 2 ] );
}
$class_list = array_unique( $class_list_all , SORT_STRING );
for ( $i = 0 ; $i < count( $class_list )-1; $i++ ) {
	echo "INSERT INTO class ( name ) VALUES ( \"".next( $class_list )."\" );";
}
?>