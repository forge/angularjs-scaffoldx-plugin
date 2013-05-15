<#assign
    angularApp = "${projectId}"
    entityModule = "${entityName?uncap_first}Module"
    entityResource = "${entityName}Resource"
    entityIdJsVar = "${entityName}Id"
    entityResourceUrlFragment = "${entityName?lower_case}s"
>
angular.module('${angularApp}').factory('${entityResource}', function($resource){
    var resource = $resource('${parentDirectories}${resourceRootPath}/${entityResourceUrlFragment}/:${entityIdJsVar}',{${entityIdJsVar}:'@${entityId}'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});