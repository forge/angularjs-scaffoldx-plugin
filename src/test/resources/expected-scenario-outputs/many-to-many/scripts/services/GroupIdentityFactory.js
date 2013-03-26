angular.module('test').factory('GroupIdentityResource', function($resource){
    var resource = $resource('/test/rest/groupidentitys/:GroupIdentityId',{GroupIdentityId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});