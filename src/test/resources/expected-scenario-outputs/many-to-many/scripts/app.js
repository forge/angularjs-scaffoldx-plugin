'use strict';

angular.module('test',['ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/GroupIdentitys',{templateUrl:'views/GroupIdentity/search.html',controller:'SearchGroupIdentityController'})
      .when('/GroupIdentitys/new',{templateUrl:'views/GroupIdentity/detail.html',controller:'NewGroupIdentityController'})
      .when('/GroupIdentitys/edit/:GroupIdentityId',{templateUrl:'views/GroupIdentity/detail.html',controller:'EditGroupIdentityController'})
      .when('/UserIdentitys',{templateUrl:'views/UserIdentity/search.html',controller:'SearchUserIdentityController'})
      .when('/UserIdentitys/new',{templateUrl:'views/UserIdentity/detail.html',controller:'NewUserIdentityController'})
      .when('/UserIdentitys/edit/:UserIdentityId',{templateUrl:'views/UserIdentity/detail.html',controller:'EditUserIdentityController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
