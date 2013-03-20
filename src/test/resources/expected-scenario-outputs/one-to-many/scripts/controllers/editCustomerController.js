

angular.module('test').controller('EditCustomerController', function($scope, $routeParams, $location, CustomerResource , StoreOrderResource) {
    var self = this;
    $scope.disabled = false;

    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.customer = new CustomerResource(self.original);
            StoreOrderResource.queryAll(function(data) {
                $scope.ordersList = data;
                angular.forEach($scope.ordersList, function(datum){
                    angular.forEach($scope.customer.orders, function(nestedDatum,index){
                        if(angular.equals(datum,nestedDatum)) {
                            $scope.customer.orders[index] = datum;
                            self.original.orders[index] = datum;
                        }
                    });
                });
            });
        };
        var errorCallback = function() {
            $location.path("/Customers");
        };
        CustomerResource.get({CustomerId:$routeParams.CustomerId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.customer);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.customer.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Customers");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Customers");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.customer.$remove(successCallback, errorCallback);
    };
    
    $scope.removeorders = function(index) {
        $scope.customer.orders.splice(index, 1);
    };
    
    $scope.addorders = function() {
        $scope.customer.orders = $scope.customer.orders || [];
        $scope.customer.orders.push(new StoreOrderResource());
    };
    
    $scope.get();
});