<#assign
    angularApp = "${projectId}"
    angularController = "New${entityName}Controller"
    angularResource = "${entityName}Resource"
    entityId = "${entityName}Id"
    model = "$scope.${entityName?uncap_first}"
    entityRoute = "/${entityName}s"
>
<#--An assignment expression that 'captures' all the related resources -->
<#assign relatedResources>
<#list properties as property>
<#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true" || (property["n-to-many"]!) == "true">
, ${property.simpleType}Resource<#t>
</#if>
</#list>
</#assign>

angular.module('${angularApp}').controller('${angularController}', function ($scope, $location, locationParser, ${angularResource} ${relatedResources}) {
    $scope.disabled = false;
    
    <#list properties as property>
    <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
    <#assign
        relatedResource="${property.simpleType}Resource"
        modelCollection="$scope.${property.name}List">
    ${relatedResource}.queryAll(function(data){
        ${modelCollection} = angular.fromJson(JSON.stringify(data));
    });
    </#if>
    </#list>
    
    <#list properties as property>
    <#if (property["n-to-many"]!) == "true">
    <#assign
        relatedResource = "${property.simpleType}Resource"
        relatedCollection = "$scope.${property.name}List"
        modelProperty = "${model}.${property.name}"
        removeExistingItemFunction = "$scope.remove${property.name}"
        addNewItemFunction = "$scope.add${property.name}">
    ${relatedResource}.queryAll(function(data){
        ${relatedCollection} = angular.fromJson(JSON.stringify(data));
    });
    
    ${removeExistingItemFunction} = function(index) {
        ${modelProperty}.splice(index, 1);
    };
    
    ${addNewItemFunction} = function() {
        ${modelProperty} = ${modelProperty} || [];
        ${modelProperty}.push(new ${relatedResource}());
    };
    </#if>
    </#list>
    
    <#list properties as property>
    <#if property["lookup"]??>
    <#assign
        lookupList="$scope.${property.name}List">
    ${lookupList} = [
    <#list property["lookup"]?split(",") as option>
        "${option}"<#if option_has_next>,</#if>
    </#list>
    ];
    </#if>
    </#list>

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('${entityRoute}/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        ${angularResource}.save(${model}, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("${entityRoute}");
    };
});