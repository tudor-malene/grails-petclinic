<html>
	<head>
		<meta name="layout" content="main">
		<title>Welcome</title>
	</head>
	<body id="welcome">
		<g:img dir="images" file="pets.png" align="right" style="position:relative;right:30px;"/>
		<h2><g:message code="welcome"/></h2>

		<ul>
			<li><g:link controller="overview" >Easygrid Overview ( Master-Detail grids)</g:link></li>
			<li><g:link controller="vetSchedule" >Vet Schedule ( Grid + Subgrids)</g:link></li>
			<li><g:link controller="vetSchedule" action="overview">Visits report ( Complex query )</g:link></li>
			<li><g:link controller="pet" action="addVisit" params="[id:1]">Add Visit ( selection widget) </g:link></li>
		</ul>
	</body>
</html>
