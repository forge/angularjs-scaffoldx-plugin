<#assign angularApp = "${projectId}">
'use strict';

angular.module('${projectId}').filter('searchFilter', function() {

    return function(results) {

        this.filteredResults = [];
        for (var ctr = 0; ctr < results.length; ctr++) {
            var flag = true;
            var searchCriteria = this.search;
            var result = results[ctr];
            for (var key in searchCriteria) {
                if (searchCriteria.hasOwnProperty(key)) {
                    var expected = searchCriteria[key];
                    if (expected == null || expected === "") {
                        continue;
                    }
                    var actual = result[key];
                    if (actual == null) {
                        flag = false;
                    } else if (angular.isObject(expected)) {
                        flag = flag && angular.equals(expected, actual);
                    } else {
                        flag = flag && (actual.toString().indexOf(expected.toString()) != -1);
                    }
                }
            }
            if (flag == true) {
                this.filteredResults.push(result);
            }
        }
        this.numberOfPages();
        return this.filteredResults;
    };
});
