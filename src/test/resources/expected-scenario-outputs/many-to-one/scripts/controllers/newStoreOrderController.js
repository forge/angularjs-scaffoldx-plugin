
angular.module('test').controller('NewStoreOrderController', function ($scope, $location, locationParser, StoreOrderResource , CustomerResource) {
    $scope.disabled = false;
    
    CustomerResource.queryAll(function(data){
        $scope.customerList = angular.fromJson(JSON.stringify(data));
    });
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/StoreOrders/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        StoreOrderResource.save($scope.storeOrder, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/StoreOrders");
    };
});