<#assign
    angularApp = "${projectId}"
    angularController = "New${entityName}Controller"
    angularResource = "${entityName}Resource"
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
    ${model} = ${model} || {};
    
    <#list properties as property>
    <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
    <#assign
        relatedResource="${property.simpleType}Resource"
        relatedCollection="$scope.${property.name}List"
        modelProperty = "${model}.${property.name}"
        selectCollection="$scope.${property.name}SelectionList"
        selectedItem="${property.name}Selection">
    ${relatedCollection} = ${relatedResource}.queryAll(function(items){
        ${selectCollection} = $.map(items, function(item) {
            return ( {
                value : item,
                text : item.${property.optionLabel}
            });
        });
    });
    
    $scope.$watch("${selectedItem}", function(selection) {
        if ( typeof selection != 'undefined') {
            ${modelProperty} = selection.value;
        }
    });
    <#elseif (property["n-to-many"]!) == "true">
    <#assign
        relatedResource = "${property.simpleType}Resource"
        relatedCollection = "$scope.${property.name}List"
        modelProperty = "${model}.${property.name}"
        selectCollection="$scope.${property.name}SelectionList"
        selectedItem="${property.name}Selection">
    ${relatedCollection} = ${relatedResource}.queryAll(function(items){
        ${selectCollection} = $.map(items, function(item) {
            return ( {
                value : item,
                text : item.${property.optionLabel}
            });
        });
    });
    
    $scope.$watch("${selectedItem}", function(selection) {
        if (typeof selection != 'undefined') {
            ${modelProperty} = [];
            $.each(selection, function(idx,selectedItem) {
                ${modelProperty}.push(selectedItem.value);
            });
        }
    });
    <#elseif property["lookup"]??>
    <#assign
        lookupCollection ="$scope.${property.name}List">
    ${lookupCollection} = [
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