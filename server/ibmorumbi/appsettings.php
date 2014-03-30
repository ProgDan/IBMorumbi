<?php
	# Configura o Banco de Dados
	include './includes/configdb.inc.php';
	# Abre o Banco de Dados
	include './includes/opendb.inc.php';
	
	$json = array();
	$query = "SELECT `key`,`data` FROM `config`";
	$result = mysql_query($query) or die(mysql_error());

	while(list($key,$data) = mysql_fetch_array($result)){
		$json[] = "\"$key\" : \"$data\"";
	}
	
	echo "{\n\t";
	echo implode(",\n\t", $json);
	echo "\n}\n";
	
	# Fecha o Banco de Dados
	include './includes/close.inc.php';
?>
