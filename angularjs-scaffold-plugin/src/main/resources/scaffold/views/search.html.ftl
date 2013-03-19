<h2>Search for ${entityName}s</h2>
<form id="${entityName}Search" class="form-horizontal">
    <#list properties as property>
    <#include "includes/searchFormInput.html.ftl" >
    </#list>
    <div class="control-group">
        <div class="controls">
            <a id="Search" name="Search" class="btn btn-primary" ng-click="performSearch()"><i class="icon-search icon-white"></i> Search</a>
            <a id="Create" name="Create" class="btn" href="#/${entityName}s/new"><i class="icon-plus-sign"></i> Create New</a>
        </div>
    </div>
</form>
<div id="search-results">
    <#include "includes/searchResults.html.ftl" >

    <#include "includes/searchResultsPaginator.html.ftl" >
</div>