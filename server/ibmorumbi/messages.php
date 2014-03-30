<?php
	# Configura o Banco de Dados
	include './includes/configdb.inc.php';
	# Abre o Banco de Dados
	include './includes/opendb.inc.php';
	
	$json = array();
	$query = "SELECT DATE_FORMAT(`date`,'%d/%m/%Y') as `data`, `pregador`, `pregador_img`, `cultos`, `tema`, `passagens`, `audio`, `video` FROM `messages` ORDER BY `date` DESC";
	$result = mysql_query($query) or die(mysql_error());

	while(list($data,$pregador,$pregador_img,$cultos,$tema,$passagens,$audio,$video) = mysql_fetch_array($result)){
		$json[] = "{\n\t\t\"data\" : \"$data\",\n\t\t\"pregador\" : \"$pregador\",\n\t\t\"pregador_img\" : \"$pregador_img\",\n\t\t\"cultos\" : \"$cultos\",\n\t\t\"tema\" : \"$tema\",\n\t\t\"passagens\" : \"$passagens\",\n\t\t\"audio\" : \"$audio\",\n\t\t\"video\" : \"$video\"\n\t}";
	}
	
	echo "[\n\t";
	echo implode(",\n\t", $json);
	echo "\n]\n";
	
	# Fecha o Banco de Dados
	include './includes/close.inc.php';
?>
