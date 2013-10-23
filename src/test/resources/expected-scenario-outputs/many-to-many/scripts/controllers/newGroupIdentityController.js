
angular.module('test').controller('NewGroupIdentityController', function ($scope, $location, locationParser, GroupIdentityResource , UserIdentityResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.groupIdentity = $scope.groupIdentity || {};
    
    $scope.usersList = UserIdentityResource.queryAll(function(items){
        $scope.usersSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("usersSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.groupIdentity.users = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.groupIdentity.users.push(collectionItem);
            });
        }
    });
    

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