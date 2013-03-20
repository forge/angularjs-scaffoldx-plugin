

angular.module('test').controller('EditStoreOrderController', function($scope, $routeParams, $location, StoreOrderResource ) {
    var self = this;
    $scope.disabled = false;

    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.storeOrder = new StoreOrderResource(self.original);
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
    
    
    $scope.get();
});