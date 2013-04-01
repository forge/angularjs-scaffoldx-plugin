<#assign
    angularApp = "${projectId}"
    angularController = "Edit${entityName}Controller"
    angularResource = "${entityName}Resource"
    entityId = "${entityName}Id"
    model = "$scope.${entityName?uncap_first}"
    entityRoute = "/${entityName}s"
>

<#assign relatedResources>
<#list properties as property>
<#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true" || (property["n-to-many"]!) == "true">
, ${property.simpleType}Resource<#t>
</#if>
</#list>
</#assign>

angular.module('${angularApp}').controller('${angularController}', function($scope, $routeParams, $location, ${angularResource} ${relatedResources}) {
    var self = this;
    $scope.disabled = false;

    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            ${model} = new ${angularResource}(self.original);
            <#list properties as property>
            <#assign
                relatedResource = "${property.simpleType!}Resource"
                relatedCollection = "$scope.${property.name}List"
                selectCollection="$scope.${property.name}SelectionList"
                modelProperty = "${model}.${property.name}"
                originalProperty = "self.original.${property.name}">
            <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
            ${relatedResource}.queryAll(function(items) {
                ${selectCollection} = $.map(items, function(item) {
                    var wrappedObject = {
                        value : item,
                        text : item.${property.optionLabel}
                    };
                    if(item.id == ${modelProperty}.id) {
                        $scope.${property.name}Selection = wrappedObject;
                    }
                    return wrappedObject;
                });
            });
            <#elseif (property["n-to-many"]!) == "true">
            ${relatedResource}.queryAll(function(items) {
                ${selectCollection} = $.map(items, function(item) {
                    var wrappedObject = {
                        value : item,
                        text : item.${property.optionLabel}
                    };
                    for(var ctr = 0 ; ctr< $scope.groupIdentity.users.length;ctr++) {
                        if(item.id == ${modelProperty}[ctr].id) {
                            $scope.${property.name}Selection.push(wrappedObject);
                        }
                    }
                    return wrappedObject;
                });
            });
            </#if>
            </#list>
        };
        var errorCallback = function() {
            $location.path("${entityRoute}");
        };
        ${angularResource}.get({${entityId}:$routeParams.${entityId}}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, ${model});
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        ${model}.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("${entityRoute}");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("${entityRoute}");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        ${model}.$remove(successCallback, errorCallback);
    };
    
    <#list properties as property>
    <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
    <#assign
            modelProperty = "${model}.${property.name}"
            selectedItem="${property.name}Selection">
    $scope.$watch("${selectedItem}", function(selection) {
        if ( typeof selection != 'undefined') {
            ${modelProperty} = selection.value;
        }
    });
    <#elseif (property["n-to-many"]!"false") == "true">
    <#assign
            modelProperty = "${model}.${property.name}"
            selectedItem="${property.name}Selection">
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
            lookupCollection = "$scope.${property.name}List">
    ${lookupCollection} = [
    <#list property["lookup"]?split(",") as option>
        "${option}"<#if option_has_next>,</#if>  
    </#list>
    ];
    </#if>
    </#list>
    
    $scope.get();
});