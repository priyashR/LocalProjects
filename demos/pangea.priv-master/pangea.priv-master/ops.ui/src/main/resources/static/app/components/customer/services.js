
var customerServicesApp = angular.module('customer.services.app', ['ui.bootstrap','configService', 'commonControllers','commonServices','customer.model.app','ngSanitize','ngRoute']);

customerServicesApp.factory('CustomerResource', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/customer', {}, {
		post :{
			method: 'POST',
			isArray : true,
			params : {
				customer : '@customer'
			}
		},
		query : {
			method : 'PUT',
			isArray : true,
			params : { id : '@id'}
		}
		
	});
} ]);

customerServicesApp.factory('customerServices', function(CustomerResource) {
	return {
		saveCustomer: function(scope) {
			return CustomerResource.post(scope.customer);
			
		}
	}
}
);
customerServicesApp.factory('searchCustomerServices', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/searchCIFCustomers', {tradename: '@tradename' }, {
		query : {
			method : 'GET',
			isArray : true,
			params : {tradename : '@tradename'}
		}
		});
} ]);

customerServicesApp.factory('searchTradeCustomerServices', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/searchTradeCustomers/case', {criteria: '@criteria' }, {
		query : {
			method : 'POST',
			isArray : true,
			params : {criteria : '@criteria'}
		}
		});
} ]);

(function(){
	'use strict';

    angular.module('pangea')
        .factory('CustomerService', CustomerService);

    CustomerService.$inject = [ '$resource', 'config' ];

    function CustomerService($resource, config) {
        return {
            customerSearch: $resource(config.apiUrl + '/customers/search', {criteria: '@criteria'}, {
                search: {
                    method: 'POST',
                    isArray: true,
                    params: {criteria: '@criteria'}
                }
            }),
			customerAccounts: $resource(config.apiUrl + '/customer/:id/accounts', {id: '@id'}, {
				get: {
					method: 'GET',
					isArray: true,
					params: {id: '@id'}
				}
			})
        };
    }

})();