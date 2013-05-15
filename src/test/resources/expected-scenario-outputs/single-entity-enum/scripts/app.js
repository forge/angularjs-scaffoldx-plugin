'use strict';

angular.module('test',['ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/Customers',{templateUrl:'views/Customer/search.html',controller:'SearchCustomerController'})
      .when('/Customers/new',{templateUrl:'views/Customer/detail.html',controller:'NewCustomerController'})
      .when('/Customers/edit/:CustomerId',{templateUrl:'views/Customer/detail.html',controller:'EditCustomerController'})
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
