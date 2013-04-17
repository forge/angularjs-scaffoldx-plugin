
angular.module('test').controller('NewStoreOrderController', function ($scope, $location, locationParser, StoreOrderResource , CustomerResource) {
    $scope.disabled = false;
    
    $scope.customerList = CustomerResource.queryAll(function(items){
        $scope.customerSelectionList = $.map(items, function(item) {
            return ( {
                value : item,
                text : item.id
            });
        });
    });
    
    $scope.$watch("customerSelection", function(selection) {
        if ( typeof selection != 'undefined') {
            $scope.storeOrder.customer = selection.value;
        }
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