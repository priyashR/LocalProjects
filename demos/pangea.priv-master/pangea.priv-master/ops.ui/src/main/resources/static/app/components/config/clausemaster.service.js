var clauseMasterServices = angular.module('ClauseMasterServices', ['ngResource', 'configService']);

clauseMasterServices.factory('ClauseMasterFactory', ['TermsService','TermsClauseService','ClauseService',
    function(TermsService,TermsClauseService,ClauseService) {
	return {
		terms : function() {
			return TermsService.query();
		},
		term : function(termId) {
			return TermsService.get({termId : termId});
		},
        saveterm : function(term) {
            return TermsService.save({termId : term.id},term);
        },
        termclauses : function(termId) {
            return TermsClauseService.query({termId:termId});
        },
        clauses : function(clauseId) {
            return ClauseService.query();
        },
        clause : function(clauseId) {
            return ClauseService.get({clauseId : clauseId});
        }
	}
}]);

clauseMasterServices.factory('TermsService', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/terms/:termId',  {termId : '@termId'}, {
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

clauseMasterServices.factory('TermsClauseService', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/terms/:termId/clause',  {termId : '@termId'}, {
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

clauseMasterServices.factory('ClauseService', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/terms/:termId',  {termId : '@termId'}, {
        query : {
            method : 'GET',
            isArray : true
        },
    	save : {
        	method : 'POST'
        }
    });
}]);