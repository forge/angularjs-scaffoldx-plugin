angular.module('test').factory('AddressResource', function($resource){
    var resource = $resource('/test/rest/addresss/:AddressId',{AddressId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});