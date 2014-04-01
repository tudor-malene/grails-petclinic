<html>
<head>
    <meta name="layout" content="main">
    <title>Overview</title>
    <r:require modules="easygrid-jqgrid-dev,export"/>

</head>

<body id="overview">
<h2>Owners and pets:</h2>
<b>Click on any owner to see the pets in the grid below. And click on a pet to see the vet visits.</b>
<br/>
You can filter using the operators in the toolbar or play with the multi-clause filtering
<br/>
<grid:grid name="owners" addUrl="${g.createLink(controller: 'owner', action: 'add')}">
    <grid:set caption="Owners" width="800"/>
    <grid:set col="id" formatter="f:customShowFormat" />
    <grid:set col="nrPets" width="60" />
</grid:grid>
<grid:exportButton name="owners"/>
<br/>
<grid:grid name="pets" masterGrid="owners" childParamName="ownerId" addFunction="addPet">
    <grid:set caption="Pets" width="600" height="140"/>
    <grid:set col="nrVisits" width="60" />
</grid:grid>
<grid:exportButton name="pets" formats="['excel', 'csv']"/>
<br/>
<grid:grid name="visits" masterGrid="pets" childParamName="petId" addFunction="addVisit">
    <grid:set caption="Visits" width="800"/>
</grid:grid>
<grid:exportButton name="visits" formats="['excel', 'csv']"/>

</body>

<r:script>

        function addPet(){
            addElement("${g.createLink(controller: 'pet', action: 'add')}", 'owners','owner.id', 'owner');
        }

        function addVisit(){
            addElement("${g.createLink(controller: 'pet', action: 'addVisit')}", 'pets', 'id', 'pet');
        }

        function addElement(lnk, gridId, param, master){
            var elem = jQuery('#'+gridId+'_table').jqGrid('getGridParam', 'selrow');
            console.log(elem);
            if(jQuery.isEmptyObject(elem)){
                alert("You have to select a "+master+" first");
                return;
            }
            document.location = lnk+"?"+param+"="+elem;;

        }

        function customShowFormat(cellvalue, options, rowObject) {
            return "<a href='${g.createLink(controller: "owner", action: "show")}/" + cellvalue + "'>" + cellvalue + "</a> ";
        }

</r:script>
<jq:jquery>
    console.log(jQuery('#owners_table').jqGridMethod('setSelection', 1));
</jq:jquery>
</html>
