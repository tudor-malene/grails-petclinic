<html>
<head>
    <meta name="layout" content="main">
    <title>Overview</title>
    <r:require modules="easygrid-jqgrid-dev,export"/>

</head>

<body id="overview">
<h2>Owners and pets:</h2>
Click on any owner , to see the pets in the grid below. And click on a pet to see the vet visits.
<grid:grid name="ownersGrid" jqgrid.width='600' columns.id.jqgrid.formatter='customShowFormat'
           jqgrid.caption='"Owners"' addUrl="${g.createLink(controller: 'owner',action: 'add')}"/>
<grid:exportButton name="ownersGrid"/>
<br/>
<table>
    <tr>
        <td>
            <grid:grid name="petsGrid" masterGrid="ownersGrid" childParamName="ownerId" jqgrid.width='290'
                       jqgrid.caption='"Pets"' addFunction="addPet" />
            <grid:exportButton name="petsGrid"/>
        </td>
        <td>&nbsp;</td>
        <td>
            <grid:grid name="visitsGrid" masterGrid="petsGrid" childParamName="petId" jqgrid.width='330'
                       jqgrid.caption='"Visits"' addFunction="addVisit" />
            <grid:exportButton name="visitsGrid"/>
        </td>
    </tr>
</table>

</body>

<r:script>
        function addPet(){
            var lnk = "${g.createLink(controller: 'pet',action: 'add')}";
            var owner = jQuery('#ownersGrid_table').jqGrid('getGridParam', 'selrow');
            if(jQuery.isEmptyObject(owner)){
                alert("You have to select an owner first");
                return;
            }
            lnk+="?owner.id="+owner;
            document.location = lnk;
        }
        function addVisit(){
            var lnk = "${g.createLink(controller: 'pet',action: 'addVisit')}";
            var pet = jQuery('#petsGrid_table').jqGrid('getGridParam', 'selrow');
            if(jQuery.isEmptyObject(pet)){
                alert("You have to select a pet first");
                return;
            }
            lnk+="?id="+pet;
            document.location = lnk;
        }
        function customShowFormat(cellvalue, options, rowObject) {
            return "<a href='${g.createLink(controller: "owner", action: "show")}/" + cellvalue + "'>" + cellvalue + "</a> ";
        }

</r:script>
<jq:jquery>
    console.log(jQuery('#ownersGrid_table').jqGridMethod('setSelection', 1));
</jq:jquery>
</html>
