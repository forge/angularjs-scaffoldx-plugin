

angular.module('test').controller('EditStoreOrderController', function($scope, $routeParams, $location, StoreOrderResource , CustomerResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.storeOrder = new StoreOrderResource(self.original);
            CustomerResource.queryAll(function(items) {
                $scope.customerSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.id
                    };
                    if($scope.storeOrder.customer && item.id == $scope.storeOrder.customer.id) {
                        $scope.customerSelection = labelObject;
                        $scope.storeOrder.customer = wrappedObject;
                        self.original.customer = $scope.storeOrder.customer;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            $location.path("/StoreOrders");
        };
        StoreOrderResource.get({StoreOrderId:$routeParams.StoreOrderId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.storeOrder);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.storeOrder.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/StoreOrders");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/StoreOrders");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.storeOrder.$remove(successCallback, errorCallback);
    };
    
    $scope.$watch("customerSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.storeOrder.customer = {};
            $scope.storeOrder.customer.id = selection.value;
        }
    });
    
    $scope.get();
});