
angular.module('test').controller('NewCustomerController', function ($scope, $location, locationParser, CustomerResource , StoreOrderResource) {
    $scope.disabled = false;
    
    
    StoreOrderResource.queryAll(function(data){
        $scope.ordersList = angular.fromJson(JSON.stringify(data));
    });
    
    $scope.removeorders = function(index) {
        $scope.customer.orders.splice(index, 1);
    };
    
    $scope.addorders = function() {
        $scope.customer.orders = $scope.customer.orders || [];
        $scope.customer.orders.push(new StoreOrderResource());
    };

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Customers/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        CustomerResource.save($scope.customer, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Customers");
    };
});