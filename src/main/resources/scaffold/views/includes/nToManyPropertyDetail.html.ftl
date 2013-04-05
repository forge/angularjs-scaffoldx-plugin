<#assign formName="${entityName}Form"
        formProperty = "${formName}.${property.name}"
        modelProperty = "${entityName?uncap_first}.${property.name}"
        propertyLabel = "${property.label}">
<#if (property.hidden!"false") == "false">
    <div class="control-group" ng-class="{error: ${formProperty}.$invalid}">
        <label for="${property.name}" class="control-label">${propertyLabel}</label>
        <div class="controls">
            <select id="${property.name}{{$index}}" name="${property.name}{{$index}}" multiple ng-model="${property.name}Selection" ng-options="${property.name?substring(0, 1)}.text for ${property.name?substring(0, 1)} in ${property.name}SelectionList">
                <option value="">Choose a ${propertyLabel}</option>
            </select>
        </div>
    </div>
</#if>