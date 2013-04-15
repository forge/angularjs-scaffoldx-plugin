

angular.module('test').controller('EditCustomerController', function($scope, $routeParams, $location, CustomerResource , StoreOrderResource) {
    var self = this;
    $scope.disabled = false;

    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.customer = new CustomerResource(self.original);
            StoreOrderResource.queryAll(function(items) {
                $scope.ordersSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        value : item,
                        text : item.id
                    };
                    if($scope.customer.orders){
                        $.each($scope.customer.orders, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.ordersSelection.push(wrappedObject);
                            }
                        });
                    }
                    return wrappedObject;
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
    
    $scope.ordersSelection = $scope.ordersSelection || [];
    $scope.$watch("ordersSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.customer) {
            $scope.customer.orders = [];
            $.each(selection, function(idx,selectedItem) {
                $scope.customer.orders.push(selectedItem.value);
            });
        }
    });
    
    $scope.get();
});