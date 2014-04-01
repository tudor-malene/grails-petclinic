<%@ page import="org.grails.samples.PetType; org.grails.plugin.easygrid.JsUtils" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>Overview</title>
    <r:require modules="easygrid-jqgrid-dev,export"/>

</head>

<body id="pets">
<h2>Report that shows how many visits each Vet has had in a period of time (can be grouped by pet type too)</h2>
<br/>
Source code:
<a href="https://github.com/tudor-malene/grails-petclinic/blob/master/grails-app/controllers/org/grails/samples/VetScheduleController.groovy">Controller</a>
<br/>
<a href="https://github.com/tudor-malene/grails-petclinic/blob/master/grails-app/views/vetSchedule/overview.gsp">GSP</a>
<br/>

<grid:grid name="visitsOverview">
    <grid:set width="800" height="250"/>
    <grid:set col="name" label="Vet" width="200"/>
    <grid:set col="pet.type" label="Pet" width="100"
              stype="select"
              searchoptions.sopt="['eq']"
              searchoptions.value="${JsUtils.convertListToString { PetType.listOrderByName() }}"/>
    <grid:set col="nrVisits" label="Total Visits" width="60"/>
    <grid:set col="minDate" label="First Visit" width="130" searchoptions.dataInit="f:dateRender" searchoptions.sopt="['ge']"/>
    <grid:set col="maxDate" label="Last Visit" width="130"  searchoptions.dataInit="f:dateRender" searchoptions.sopt="['le']"/>
</grid:grid>
<grid:exportButton name="visitsOverview"/>

<br/>

</body>
<r:script>
function dateRender(elem){
    $(elem).datepicker({
        changeYear: true,
        changeMonth: true,
        onSelect: function (dateText, inst) {
            setTimeout(function () {
                $('#visitsOverview_table')[0].triggerToolbar();
            }, 50);
        }
    });
}

</r:script>
</html>
