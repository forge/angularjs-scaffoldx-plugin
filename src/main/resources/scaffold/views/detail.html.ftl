<#assign formName = "${entityName}Form"
        model = "${entityName?uncap_first}">
<header ng-switch on="$location.path().indexOf('/${entityName}s/new') > -1">
    <h3 ng-switch-when="true">Create ${entityName}</h3>
    <h3 ng-switch-when="false">View or edit ${entityName}</h3>
</header>
<form id="${formName}" name="${formName}" class="form-horizontal">
    <div ng-show="displayError" class="alert alert-error">
        <strong>Error!</strong> Something broke. Retry, or cancel and start afresh.
    </div>
    <#list properties as property>
        <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
        <#include "includes/nToOnePropertyDetail.html.ftl" >
        <#elseif (property["n-to-many"]!) == "true">
        <#include "includes/nToManyPropertyDetail.html.ftl" >
        <#elseif property["lookup"]??>
        <#include "includes/lookupPropertyDetail.html.ftl" >
        <#else>
        <#include "includes/basicPropertyDetail.html.ftl" >
        </#if>
    </#list>
    <div class="control-group">
        <div class="controls">
            <button id="save${entityName}" name="save${entityName}" class="btn btn-primary" ng-disabled="isClean() || ${formName}.$invalid" ng-click="save()"><i class="icon-ok-sign icon-white"></i> Save</button>
            <button id="cancel" name="cancel" class="btn" ng-click="cancel()"><i class="icon-remove-sign"></i> Cancel</button>
            <button id="delete${entityName}" name="delete${entityName}" class="btn btn-danger" ng-show="${model}.${entityId}" ng-click="remove()"><i class="icon-trash icon-white"></i> Delete</button>
        </div>
    <div>
</form>