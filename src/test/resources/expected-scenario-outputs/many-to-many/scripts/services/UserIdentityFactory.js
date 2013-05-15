angular.module('test').factory('UserIdentityResource', function($resource){
    var resource = $resource('rest/useridentitys/:UserIdentityId',{UserIdentityId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});