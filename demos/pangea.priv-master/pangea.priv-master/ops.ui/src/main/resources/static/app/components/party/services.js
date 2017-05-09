var partyServices = angular.module('PartyServices', [ 'ngResource', 'configService' ]);

partyServices.factory('PartyService', [ '$resource', 'config', function($resource, config) {
	return {
		party : $resource(config.apiUrl + '/deal/:dealid/party/:id', {
			dealid : '@dealid',
			id : '@id'
			}, 
			{
				'query' : {
					method : 'GET',
					isArray : true
				}
			},
				{
				'remove' : {
					method : 'DELETE',
					isArray : true,
				}
		}),
		init : $resource(config.apiUrl + '/deal/:dealid/party/new', {type:'@type'}, {
			get : {
				method : 'GET',
				params:{type:'@type'},
				isArray : false
			}
		}),
		
		productParties : $resource(config.apiUrl + '/product/party/types', {
			productTypeId : '@productTypeId',
			stepTypeCode : '@stepTypeCode'
		}, {
			'query' : {
				method : 'GET',
				isArray : true,
			}
		}),
		escalations : $resource(config.apiUrl + '/deal/:dealid/party/escalations', {
			dealid : '@dealid'
		}, {
			'query' : {
				method : 'GET',
				isArray : true,
			}
		}),
		configs : $resource(config.apiUrl + '/deal/:dealid/party/configs', {
			dealid : '@dealid'
		}, {
			'query' : {
				method : 'GET',
				isArray : true,
			}
		}),

	};
} ]);

partyServices.factory('SearchPartiesService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/party', {name: '@name',type:'@type' }, {
		query : {
			method : 'GET',
			isArray : true,
			params : {name : '@name',type:'@type'}
		}
		});
} ]);

partyServices.factory('DealPartyTypeService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/deal/party/types', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
		});
} ]);

partyServices.factory('SearchCriteriaParties', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/searchPartiesCriteria', {criteria: '@criteria' }, {
		query : {
			method : 'POST',
			isArray : true,
			params : {criteria : '@criteria',}
		}
		});
} ]);

partyServices.factory('DealSanctionService', [ '$resource', 'config', function($resource, config) {
	 return $resource(config.apiUrl + '/deal/:dealId/sanction/:sanctionId',  {dealId : '@dealId', sanctionId:'@sanctionId' }, {
	        query : {
	            method : 'GET',
	            isArray : false
	        },
	    	save : {
	        	method : 'POST'
	        }

	    });
	
}]);
