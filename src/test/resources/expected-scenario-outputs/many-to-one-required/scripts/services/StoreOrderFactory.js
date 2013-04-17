angular.module('test').factory('StoreOrderResource', function($resource){
    var resource = $resource('/test/rest/storeorders/:StoreOrderId',{StoreOrderId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});