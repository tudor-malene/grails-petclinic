<%@ page import="org.grails.plugin.easygrid.JsUtils" defaultCodec="none" %>

<g:set var="gridId" value="${attrs.id}_table"/>
<g:set var="pagerId" value="${attrs.id}Pager"/>
<g:set var="conf" value="${gridConfig.jqgrid}"/>

<table id="${gridId}"></table>

<div id="${pagerId}"></div>

<jq:jquery>
    jQuery("#${gridId}").jqGrid({
    url: '${g.createLink(controller: attrs.controller, action: "${gridConfig.id}Rows", params: params)}',
    loadError: easygrid.loadError,
    pager: '#${pagerId}',
    ${JsUtils.convertToJs(conf - [navGrid: conf.navGrid] - [filterToolbar: conf.filterToolbar], true)},
    <g:if test="${gridConfig.subGrid}">
        subGrid: true,
        subGridRowExpanded: easygrid.subGridRowExpanded('${g.createLink(controller: attrs.controller, action: "${gridConfig.subGrid}Html")}'),
    </g:if>
    <g:if test="${gridConfig.childGrid}">
        "onSelectRow":easygrid.onSelectGridRowReloadGrid('${gridConfig.childGrid}','${gridConfig.childParamName}'),
    </g:if>
    <g:if test="${gridConfig.inlineEdit}">
        editurl: '${g.createLink(controller: attrs.controller, action: "${gridConfig.id}InlineEdit")}',
        cellurl: '${g.createLink(controller: attrs.controller, action: "${gridConfig.id}InlineEdit")}',
        onSelectRow: easygrid.onSelectRowInlineEdit('${attrs.id}_table'),
    </g:if>
    colModel: [
    <grid:eachColumn gridConfig="${gridConfig}">
        <g:if test="${col.render}">
            {${JsUtils.convertToJs(col.jqgrid + [name: col.name, search: col.enableFilter, sortable:col.sortable , label: g.message(code: col.label, default: col.label)], true)}
            <g:if test="${col.otherProperties}">
                ,${col.otherProperties}
            </g:if>
            },
        </g:if>
    </grid:eachColumn>
    ],
    <g:if test="${gridConfig.otherProperties}">
        ${gridConfig.otherProperties.trim()}   // render properties defined in the gsp
    </g:if>
    });
    <g:if test="${gridConfig.masterGrid}">%{--set the on select row of the master grid--}%
        jQuery('#${gridConfig.masterGrid}_table').jqGrid('setGridParam',{ "onSelectRow" : easygrid.onSelectGridRowReloadGrid('${attrs.id}_table','${gridConfig.childParamName}')});
    </g:if>
    <g:if test="${gridConfig.enableFilter}">
        jQuery('#${gridId}').jqGrid('filterToolbar', ${JsUtils.convertToJs(conf.filterToolbar)});
    </g:if>

    <g:if test="${gridConfig.addNavGrid}">
        jQuery('#${gridId}').jqGrid('navGrid','#${pagerId}',
        ${JsUtils.convertToJs(conf.navGrid.generalOpts)},
        ${JsUtils.convertToJs(conf.navGrid.editOpts)},     //edit
        ${JsUtils.convertToJs(conf.navGrid.addOpts)},     //add
        ${JsUtils.convertToJs(conf.navGrid.delOpts)},     //delete
        ${JsUtils.convertToJs(conf.navGrid.searchOpts)},     //search
        ${JsUtils.convertToJs(conf.navGrid.viewOpts)}     //view
        )
        <g:if test="${gridConfig.addUrl}">
            .jqGrid('navButtonAdd','#${pagerId}',{caption:"", buttonicon:"ui-icon-plus", onClickButton:function(){
            document.location = '${gridConfig.addUrl}';
        }});
        </g:if>
        <g:if test="${gridConfig.addFunction}">
            .jqGrid('navButtonAdd','#${pagerId}',{caption:"", buttonicon:"ui-icon-plus", onClickButton:${gridConfig.addFunction}});
        </g:if>
    </g:if>
</jq:jquery>

