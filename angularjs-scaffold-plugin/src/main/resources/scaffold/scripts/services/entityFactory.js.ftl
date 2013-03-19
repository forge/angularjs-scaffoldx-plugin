<#assign
    angularApp = "${projectId}"
    entityModule = "${entityName?uncap_first}Module"
    entityResource = "${entityName}Resource"
    entityId = "${entityName}Id"
    entityResourceUrlFragment = "${entityName?lower_case}s"
>
angular.module('${angularApp}').factory('${entityResource}', function($resource){
    var resource = $resource('${resourceRootPath}/${entityResourceUrlFragment}/:${entityId}',{${entityId}:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});