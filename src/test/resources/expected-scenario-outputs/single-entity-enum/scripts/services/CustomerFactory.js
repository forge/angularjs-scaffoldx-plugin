angular.module('test').factory('CustomerResource', function($resource){
    var resource = $resource('/test/rest/customers/:CustomerId',{CustomerId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});