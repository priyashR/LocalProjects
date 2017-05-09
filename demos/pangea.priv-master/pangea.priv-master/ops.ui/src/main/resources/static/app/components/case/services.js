(function() {
	'use strict';

	angular
		.module('pangea')
		.factory('CaseLookupService', CaseLookupService)
		.factory('CaseService', CaseService);
	
		CaseLookupService.$inject = ['$resource', 'config'];
		CaseService.$inject = ['$resource', 'config'];
		
		function CaseLookupService($resource, config, ProductService) {
			return {
				allProductTypes : function() {
					return ProductService.productTypes.query();
				},
				allProducts : function() {
					return ProductService.products.query();
				},
				allProductClassificationTypes : function() {
					return ProductService.productClassificationTypes.query();
				}
			};
		};
		
		function CaseService($resource, config) {
			var service = {
					cases : $resource(config.apiUrl + '/case/:caseId', { caseId : '@id' }, {}),
					escalations : $resource(config.apiUrl + '/case/:caseId/escalations', { caseId : '@id' }, {}),
					caseCustomers : $resource(config.apiUrl + '/case/:caseId/customer', { caseId : '@id' }, {}),
					prechecks : $resource(config.apiUrl + '/case/:caseId/prechecks', { caseId : '@id' }, {})
			};
			
			return service;
		};
		
})();
