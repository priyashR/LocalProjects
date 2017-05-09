var messagetypeServices = angular.module('MessageTypeServices', ['ngResource', 'configService']);

messagetypeServices.factory('MessageTypeFactory', ['MessageTypeService',
    function(MessageTypeService) {
	return {
		messagetypes : function() {
			return MessageTypeService.query();
		},
		messagetype : function(messagetypeCode) {
			return MessageTypeService.get({messagetypeCode : messagetypeCode});
		},
        savemessagetype : function(messagetype) {
            return MessageTypeService.save({messagetypeCode : messagetype.code},messagetype);
        }
	}
}]);

messagetypeServices.factory('MessageTypeService', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/messagetype/:messagetypeCode',  {messagetypeCode : '@messagetypeCode'}, {
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
