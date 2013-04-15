

angular.module('test').controller('EditGroupIdentityController', function($scope, $routeParams, $location, GroupIdentityResource , UserIdentityResource) {
    var self = this;
    $scope.disabled = false;

    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.groupIdentity = new GroupIdentityResource(self.original);
            UserIdentityResource.queryAll(function(items) {
                $scope.usersSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        value : item,
                        text : item.id
                    };
                    if($scope.groupIdentity.users){
                        $.each($scope.groupIdentity.users, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.usersSelection.push(wrappedObject);
                            }
                        });
                    }
                    return wrappedObject;
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
    
    $scope.usersSelection = $scope.usersSelection || [];
    $scope.$watch("usersSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.groupIdentity) {
            $scope.groupIdentity.users = [];
            $.each(selection, function(idx,selectedItem) {
                $scope.groupIdentity.users.push(selectedItem.value);
            });
        }
    });
    
    $scope.get();
});