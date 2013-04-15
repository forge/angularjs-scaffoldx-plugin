
angular.module('test').controller('NewCustomerController', function ($scope, $location, locationParser, CustomerResource ) {
    $scope.disabled = false;
    
    $scope.paymentTypeList = [
        "CASH",
        "CREDIT_CARD",
        "DEBIT_CARD"
    ];

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