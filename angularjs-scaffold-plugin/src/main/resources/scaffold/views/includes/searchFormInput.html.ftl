<#-- Display only non-hidden singular properties as search criteria. Omit collections -->
<#if (property.hidden!"false") != "true" && (property["n-to-many"]!"false") != "true">
<div class="control-group">
    <label for="${property.name}" class="control-label">${property.name?cap_first}</label>
    <div class="controls">
        <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
        <select id="${property.name}" name="${property.name}" ng-model="search.${property.name}" ng-options="${property.name?substring(0, 1)} as ${property.name?substring(0, 1)}.${property.optionLabel} for ${property.name?substring(0, 1)} in ${property.name}List">
            <option value="">Choose a ${property.name?cap_first}</option>
        </select>
        <#else>
        <input id="${property.name}" name="${property.name}" type="text" ng-model="search.${property.name}" placeholder="Enter the ${entityName} ${property.name}"></input>
        </#if>
    </div>
</div>
</#if>