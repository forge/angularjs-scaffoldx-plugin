
angular.module('test').controller('NewCustomerController', function ($scope, $location, locationParser, CustomerResource , StoreOrderResource) {
    $scope.disabled = false;
    $scope.customer = $scope.customer || {};
    
    $scope.ordersList = StoreOrderResource.queryAll(function(items){
        $scope.ordersSelectionList = $.map(items, function(item) {
            return ( {
                value : item,
                text : item.id
            });
        });
    });
    
    $scope.$watch("ordersSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.customer.orders = [];
            $.each(selection, function(idx,selectedItem) {
                $scope.customer.orders.push(selectedItem.value);
            });
        }
    });

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