

angular.module('test').controller('EditUserIdentityController', function($scope, $routeParams, $location, UserIdentityResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.userIdentity = new UserIdentityResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/UserIdentitys");
        };
        UserIdentityResource.get({UserIdentityId:$routeParams.UserIdentityId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.userIdentity);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.userIdentity.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/UserIdentitys");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/UserIdentitys");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.userIdentity.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});