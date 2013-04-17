<#assign formName="${entityName}Form"
        formProperty = "${formName}.${property.name}"
        modelProperty = "${property.name}Selection"
        propertyLabel = "${property.label}"
        collectionVar = "${property.name?substring(0, 1)}"
        collection = "${property.name}SelectionList">
<#if (property.hidden!"false") == "false">
    <div class="control-group" ng-class="{error: ${formProperty}.$invalid}">
        <label for="${property.name}" class="control-label">${propertyLabel}</label>
        <div id="${property.name}Controls" class="controls">
            <select id="${property.name}" name="${property.name}" multiple ng-model="${property.name}Selection" ng-options="${collectionVar}.text for ${collectionVar} in ${collection}">
                <option value="">Choose a ${propertyLabel}</option>
            </select>
        </div>
    </div>
</#if>