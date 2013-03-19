<table class="table table-bordered table-striped clearfix">
    <thead>
        <tr>
        <#list properties as property>
        <#if (property.hidden!"false") != "true">
            <#if (property["n-to-many"]!"false") != "true">
            <#-- Display only singular properties for now. Exclude collections as they cannot be displayed "meaningfully". -->
            <th>${property.name?cap_first}</th>
            </#if>
        </#if>
        </#list>
        </tr>
    </thead>
    <tbody id="search-results-body">
        <tr ng-repeat="result in searchResults | searchFilter:searchResults | startFrom:currentPage*pageSize | limitTo:pageSize">
        <#list properties as property>
        <#if (property.hidden!"false") != "true">
            <#if (property["many-to-one"]!"false") == "true" || (property["one-to-one"]!"false") == "true">
            <td><a href="#/${entityName}s/edit/{{result.id}}">{{result.${property.name}.${property.optionLabel}}}</a></td>
            <#elseif (property["n-to-many"]!"false") == "true">
            <#-- Do nothing. We won't allow for display of collection properties in search results for now. -->
            <#else>
            <td><a href="#/${entityName}s/edit/{{result.id}}">{{result.${property.name}}}</a></td>
            </#if>
        </#if>
        </#list>
        </tr>
    </tbody>
</table>