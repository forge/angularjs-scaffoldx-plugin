
angular.module('test').controller('NewUserIdentityController', function ($scope, $location, locationParser, UserIdentityResource ) {
    $scope.disabled = false;
    $scope.userIdentity = $scope.userIdentity || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/UserIdentitys/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        UserIdentityResource.save($scope.userIdentity, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/UserIdentitys");
    };
});