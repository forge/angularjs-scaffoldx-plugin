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
                modelProperty = "${model}.${property.name}"
                originalProperty = "self.original.${property.name}">
            <#if (property["many-to-one"]!) == "true" || (property["one-to-one"]!) == "true">
            ${relatedResource}.queryAll(function(data) {
                ${relatedCollection} = data;
                angular.forEach(${relatedCollection}, function(datum){
                    if(angular.equals(datum, ${modelProperty})) {
                        ${modelProperty} = datum;
                        ${originalProperty} = datum;
                    }
                });
            });
            <#elseif (property["n-to-many"]!) == "true">
            ${relatedResource}.queryAll(function(data) {
                ${relatedCollection} = data;
                angular.forEach(${relatedCollection}, function(datum){
                    angular.forEach(${modelProperty}, function(nestedDatum,index){
                        if(angular.equals(datum,nestedDatum)) {
                            ${modelProperty}[index] = datum;
                            ${originalProperty}[index] = datum;
                        }
                    });
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
    <#if (property["n-to-many"]!"false") == "true">
    <#assign
            modelProperty = "${model}.${property.name}"
            removeExistingItemFunction = "$scope.remove${property.name}"
            addNewItemFunction = "$scope.add${property.name}">
    ${removeExistingItemFunction} = function(index) {
        ${modelProperty}.splice(index, 1);
    };
    
    ${addNewItemFunction} = function() {
        ${modelProperty} = ${modelProperty} || [];
        ${modelProperty}.push(new ${relatedResource}());
    };
    </#if>
    </#list>
    
    $scope.get();
});