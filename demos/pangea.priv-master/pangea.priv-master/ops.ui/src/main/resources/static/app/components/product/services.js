(function() {
	'use strict';
	
	angular
		.module('pangea')
		.factory('ProductService', ProductService)
		.factory('ProductPreChecks', ProductPreChecks)
		.factory('ProductTerms', ProductTerms)
		.factory('RoleProductPreChecks', RoleProductPreChecks);
		
		ProductService.$inject = [ '$resource', 'config'];
		ProductPreChecks.$inject = [ '$resource', 'config'];
		ProductTerms.$inject = [ '$resource', 'config'];
		RoleProductPreChecks.$inject = [ '$resource', 'config'];
		
		function ProductService($resource, config) {
			var service = {
					products : $resource(config.apiUrl + '/products', {}, {	query : {method : 'GET', isArray : true} }),
					productTypes : $resource(config.apiUrl + '/product/types', {}, { query : {method : 'GET', isArray : true} }),
					productClassificationTypes : $resource(config.apiUrl + '/product/classifications', {}, { query : {method : 'GET', isArray : true} })
			};
			return service;
		};
		
		function ProductPreChecks($resource, config) {
			var service = $resource(config.apiUrl + '/product/prechecks', {
				productTypeId : '@productTypeId',
				stepTypeCode : '@stepTypeCode'} , {
					'query' : {
							method : 'GET',
							isArray : true,
						}
					});
			return service;
		};
		
		function ProductTerms($resource, config) {
			var service = $resource(config.apiUrl + '/product/terms', {
				productTypeId : '@productTypeId',
				stepTypeCode : '@stepTypeCode'}, {
					'query' : {
						method : 'GET',
						isArray : true,
					}
				});
			return service;
		};
		
		function RoleProductPreChecks($resource, config) {
			var service = $resource(config.apiUrl + '/product/roleBasedPrechecks', {
				productTypeId : '@productTypeId',
				stepTypeCode : '@stepTypeCode'}, {
					'query' : {
						method : 'GET',
						isArray : true,
					}
				});
			return service;
		};
})();