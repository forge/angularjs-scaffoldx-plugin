'use strict';

angular.module('test',['ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/Customers',{templateUrl:'views/Customer/search.html',controller:'SearchCustomerController'})
      .when('/Customers/new',{templateUrl:'views/Customer/detail.html',controller:'NewCustomerController'})
      .when('/Customers/edit/:CustomerId',{templateUrl:'views/Customer/detail.html',controller:'EditCustomerController'})
      .when('/StoreOrders',{templateUrl:'views/StoreOrder/search.html',controller:'SearchStoreOrderController'})
      .when('/StoreOrders/new',{templateUrl:'views/StoreOrder/detail.html',controller:'NewStoreOrderController'})
      .when('/StoreOrders/edit/:StoreOrderId',{templateUrl:'views/StoreOrder/detail.html',controller:'EditStoreOrderController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        return ($location.path().indexOf(route) != -1);
    };
  });
