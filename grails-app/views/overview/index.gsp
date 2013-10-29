<html>
<head>
    <meta name="layout" content="main">
    <title>Overview</title>
    <r:require modules="easygrid-jqgrid-dev,export"/>

</head>

<body id="overview">
<h2>Owners and pets:</h2>
Click on any owner , to see the pets in the grid below. And click on a pet to see the vet visits.
<grid:grid name="owners" jqgrid.width='600' columns.id.jqgrid.formatter='customShowFormat'
           jqgrid.caption='"Owners"' addUrl="${g.createLink(controller: 'owner',action: 'add')}"/>
<grid:exportButton name="owners"/>
<br/>
<table>
    <tr>
        <td>
            <grid:grid name="pets" masterGrid="owners" childParamName="ownerId" jqgrid.width='290'
                       jqgrid.caption='"Pets"' addFunction="addPet" />
            <grid:exportButton name="pets" formats="['excel','csv']"/>
        </td>
        <td>&nbsp;</td>
        <td>
            <grid:grid name="visits" masterGrid="pets" childParamName="petId" jqgrid.width='330'
                       jqgrid.caption='"Visits"' addFunction="addVisit" />
            <grid:exportButton name="visits" formats="['excel','csv']"/>
        </td>
    </tr>
</table>

</body>

<r:script>

        function addPet(){
            addElement("${g.createLink(controller: 'pet',action: 'add')}", 'owners','owner.id', 'owner');
        }

        function addVisit(){
            addElement("${g.createLink(controller: 'pet',action: 'addVisit')}", 'pets', 'id', 'pet');
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
