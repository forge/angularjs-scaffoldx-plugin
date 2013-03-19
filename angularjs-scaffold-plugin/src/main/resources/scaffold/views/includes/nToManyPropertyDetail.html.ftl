<#assign formName="${entityName}Form"
        formProperty = "${formName}.${property.name}"
        modelProperty = "${entityName?uncap_first}.${property.name}"
        propertyLabel = "${property.name?cap_first}">
<#if (property.hidden!"false") == "false">
    <div class="control-group" ng-class="{error: ${formProperty}.$invalid}">
        <label for="${property.name}" class="control-label">${propertyLabel}</label>
        <div class="controls">
            <div ng-repeat="${property.name}Element in ${modelProperty}">
                <select id="${property.name}{{$index}}" name="${property.name}{{$index}}" ng-model="${modelProperty}[$index]" ng-options="${property.name?substring(0, 1)} as ${property.name?substring(0, 1)}.${property.optionLabel} for ${property.name?substring(0, 1)} in ${property.name}List">
                    <option value="">Choose a ${propertyLabel}</option>
                </select> 
                <button class="btn btn-mini btn-danger" ng-click="remove${property.name}($index)">-</button>
            </div>
            <button id="add${property.name}" class="btn btn-mini" ng-click="add${property.name}()">+</button>
        </div>
    </div>
</#if>