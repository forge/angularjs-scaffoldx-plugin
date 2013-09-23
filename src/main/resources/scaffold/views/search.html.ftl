<div class="form-horizontal">
    <h3>Create a new ${entityName}.</h3>
    <div class="control-group">
        <div class="controls">
            <a id="Create" name="Create" class="btn btn-primary" href="#/${entityName}s/new"><i class="icon-plus-sign icon-white"></i> Create</a>
        </div>
    </div>
</div>
<hr/>
<div>
    <h3>Search for ${entityName}s</h3>
    <form id="${entityName}Search" class="form-horizontal">
        <#list properties as property>
        <#include "includes/searchFormInput.html.ftl">
        </#list>
        <div class="control-group">
            <div class="controls">
                <a id="Search" name="Search" class="btn btn-primary" ng-click="performSearch()"><i class="icon-search icon-white"></i> Search</a>
            </div>
        </div>
    </form>
</div>
<div id="search-results">
    <#include "includes/searchResults.html.ftl">
    <#include "includes/searchResultsPaginator.html.ftl">
</div>