<#assign angularApp = "${projectId}">
'use strict';

angular.module('${angularApp}',['ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      <#list entityNames as entityName>
      <#assign
                searchEntityController = "Search${entityName}Controller"
                newEntityController = "New${entityName}Controller"
                editEntityController = "Edit${entityName}Controller"
                entityIdJsVar = "${entityName}Id"
                entityRoute = "/${entityName}s"
                entityPartialsLocation = "views/${entityName}">
      .when('${entityRoute}',{templateUrl:'${entityPartialsLocation}/search.html',controller:'${searchEntityController}'})
      .when('${entityRoute}/new',{templateUrl:'${entityPartialsLocation}/detail.html',controller:'${newEntityController}'})
      .when('${entityRoute}/edit/:${entityIdJsVar}',{templateUrl:'${entityPartialsLocation}/detail.html',controller:'${editEntityController}'})
      </#list>
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        return ($location.path().startsWith("/" + route));
    };
  });
