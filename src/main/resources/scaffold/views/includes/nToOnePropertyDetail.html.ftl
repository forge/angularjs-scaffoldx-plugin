<#assign formName="${entityName}Form"
        formProperty = "${formName}.${property.name}"
        modelProperty = "${entityName?uncap_first}.${property.name}"
        propertyLabel = "${property.name?cap_first}">
<#if (property.hidden!"false") == "false">
    <div class="control-group" ng-class="{error: ${formProperty}.$invalid}">
        <label for="${property.name}" class="control-label">${propertyLabel}</label>
        <div class="controls">
        <select id="${property.name}" name="${property.name}" ng-model="${property.name}Selection" ng-options="${property.name?substring(0, 1)}.text for ${property.name?substring(0, 1)} in ${property.name}SelectionList" <#if (property.required!) == "true">required</#if> >
            <option value="">Choose a ${propertyLabel}</option>
        </select>
        <#if (property.required!) == "true">
        <span class="help-inline" ng-show="${formProperty}.$error.required">required</span> 
        </#if>
        </div>
    </div>
</#if>