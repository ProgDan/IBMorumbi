<?php
	# Configura o Banco de Dados
	include './includes/configdb.inc.php';
	# Abre o Banco de Dados
	include './includes/opendb.inc.php';
	# Funções para captura de informações do cliente
	include './includes/client_tools.inc.php';
	
	if(isset($_GET["platform"])){
		$query = "INSERT INTO `log`(`ip`,`url`,`platform`,`device`,`os`,`client`,`user_agent`)VALUES('".get_client_ip()."','".full_url($_SERVER)."','".$_GET["platform"]."','".$_GET["device"]."','".$_GET["os"]."','".$_GET["client"]."','".$_SERVER['HTTP_USER_AGENT']."')";
	}
	else {
		$query = "INSERT INTO `log`(`ip`,`url`,`device`,`user_agent`)VALUES('".get_client_ip()."','".full_url($_SERVER)."','".get_device()."','".$_SERVER['HTTP_USER_AGENT']."')";
	}
	mysql_query($query) or die(mysql_error());	
	
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
