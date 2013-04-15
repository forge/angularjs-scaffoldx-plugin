
angular.module('test').controller('NewGroupIdentityController', function ($scope, $location, locationParser, GroupIdentityResource , UserIdentityResource) {
    $scope.disabled = false;
    
    $scope.usersList = UserIdentityResource.queryAll(function(items){
        $scope.usersSelectionList = $.map(items, function(item) {
            return ( {
                value : item,
                text : item.id
            });
        });
    });
    
    $scope.$watch("usersSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.groupIdentity.users = [];
            $.each(selection, function(idx,selectedItem) {
                $scope.groupIdentity.users.push(selectedItem.value);
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