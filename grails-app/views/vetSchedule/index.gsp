<%@ page import="org.grails.samples.Speciality; org.grails.plugin.easygrid.JsUtils" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Overview</title>
    <r:require modules="easygrid-jqgrid-dev,export"/>

</head>

<body id="pets">
<h2>Schedule:</h2>
<br/>
Open any row to see a subgrid with all the visits.
<br/>
The subgrid is a normal grid definition itself ( similat to the master -grid example)
<br/>
Source code:
<a href="https://github.com/tudor-malene/grails-petclinic/blob/master/grails-app/controllers/org/grails/samples/VetScheduleController.groovy">Controller</a>
<br/>
<a href="https://github.com/tudor-malene/grails-petclinic/blob/master/grails-app/views/vetSchedule/index.gsp">GSP</a>
<br/>

<grid:grid name="schedule">
    <grid:set width="800" height="450"/>
    <grid:set col="speciality" stype="select"
              searchoptions.sopt="['eq']"
              searchoptions.value="${JsUtils.convertListToString{Speciality.listOrderByName()}}"/>
</grid:grid>

<br/>

</body>
</html>
