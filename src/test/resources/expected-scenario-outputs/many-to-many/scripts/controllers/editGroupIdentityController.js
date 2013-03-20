

angular.module('test').controller('EditGroupIdentityController', function($scope, $routeParams, $location, GroupIdentityResource , UserIdentityResource) {
    var self = this;
    $scope.disabled = false;

    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.groupIdentity = new GroupIdentityResource(self.original);
            UserIdentityResource.queryAll(function(data) {
                $scope.usersList = data;
                angular.forEach($scope.usersList, function(datum){
                    angular.forEach($scope.groupIdentity.users, function(nestedDatum,index){
                        if(angular.equals(datum,nestedDatum)) {
                            $scope.groupIdentity.users[index] = datum;
                            self.original.users[index] = datum;
                        }
                    });
                });
            });
        };
        var errorCallback = function() {
            $location.path("/GroupIdentitys");
        };
        GroupIdentityResource.get({GroupIdentityId:$routeParams.GroupIdentityId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.groupIdentity);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.groupIdentity.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/GroupIdentitys");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/GroupIdentitys");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.groupIdentity.$remove(successCallback, errorCallback);
    };
    
    $scope.removeusers = function(index) {
        $scope.groupIdentity.users.splice(index, 1);
    };
    
    $scope.addusers = function() {
        $scope.groupIdentity.users = $scope.groupIdentity.users || [];
        $scope.groupIdentity.users.push(new UserIdentityResource());
    };
    
    $scope.get();
});