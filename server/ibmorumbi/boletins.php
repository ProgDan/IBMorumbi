<?php
	# Configura o Banco de Dados
	include './includes/configdb.inc.php';
	# Abre o Banco de Dados
	include './includes/opendb.inc.php';
	
	$json = array();
	$query = "SELECT `date`,`file` FROM `boletins` ORDER BY `id` DESC";
	
	$result = mysql_query($query) or die(mysql_error());
	
	while(list($data,$file) = mysql_fetch_array($result)){
		$json[] = "{\n\t\t\"data\" : \"$data\",\n\t\t\"file\" : \"$file\"\n\t}";
	}
	
	echo "[\n\t";
	echo implode(",\n\t", $json);
	echo "\n]\n";
	
	# Fecha o Banco de Dados
	include './includes/close.inc.php';
?>
