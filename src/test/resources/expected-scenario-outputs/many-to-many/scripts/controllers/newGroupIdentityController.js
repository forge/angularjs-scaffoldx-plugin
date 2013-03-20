
angular.module('test').controller('NewGroupIdentityController', function ($scope, $location, locationParser, GroupIdentityResource , UserIdentityResource) {
    $scope.disabled = false;
    
    
    UserIdentityResource.queryAll(function(data){
        $scope.usersList = angular.fromJson(JSON.stringify(data));
    });
    
    $scope.removeusers = function(index) {
        $scope.groupIdentity.users.splice(index, 1);
    };
    
    $scope.addusers = function() {
        $scope.groupIdentity.users = $scope.groupIdentity.users || [];
        $scope.groupIdentity.users.push(new UserIdentityResource());
    };

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/GroupIdentitys/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        GroupIdentityResource.save($scope.groupIdentity, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/GroupIdentitys");
    };
});