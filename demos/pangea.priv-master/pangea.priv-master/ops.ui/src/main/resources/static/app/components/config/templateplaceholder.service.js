var placeholderServices = angular.module('PlaceholderServices', ['ngResource', 'configService']);

placeholderServices.factory('PlaceholderFactory', ['PlaceholderService','DataContextService',
    function(PlaceholderService,DataContextService) {
	return {
		placeholders : function() {
			return PlaceholderService.query();
		},
		placeholder : function(placeholderCode) {
			return PlaceholderService.get({placeholderCode : placeholderCode});
		},
        saveplaceholder : function(placeholder) {
            return PlaceholderService.save({placeholderCode : placeholder.code},placeholder);
        },
        dataContext : function(){
            return DataContextService.get();
        }
	}
}]);

placeholderServices.factory('PlaceholderService', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/placeholders/:placeholderCode',  {placeholderCode : '@placeholderCode'}, {
        query : {
            method : 'GET',
            isArray : true,
            withCredentials : true
        },
    	save : {
        	method : 'POST',
            withCredentials : true
        }

    });
}]);

placeholderServices.factory('DataContextService', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/deal/data/context',  {}, {});
}]);