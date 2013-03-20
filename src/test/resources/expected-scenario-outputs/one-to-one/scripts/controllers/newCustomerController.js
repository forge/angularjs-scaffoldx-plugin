
angular.module('test').controller('NewCustomerController', function ($scope, $location, locationParser, CustomerResource , AddressResource) {
    $scope.disabled = false;
    
    AddressResource.queryAll(function(data){
        $scope.shippingAddressList = angular.fromJson(JSON.stringify(data));
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