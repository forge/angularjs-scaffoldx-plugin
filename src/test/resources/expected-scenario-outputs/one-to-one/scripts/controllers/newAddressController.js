
angular.module('test').controller('NewAddressController', function ($scope, $location, locationParser, AddressResource ) {
    $scope.disabled = false;
    
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Addresss/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        AddressResource.save($scope.address, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Addresss");
    };
});