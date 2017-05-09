var customerMasterServices = angular.module('CustomerMasterServices', ['ngResource', 'configService']);

customerMasterServices.factory('CustomerMasterFactory', ['CustomersService',
    function(CustomersService) {
	return {
		customers : function() {
			return CustomersService.query();
		},
		customer : function(customerId) {
			return CustomersService.get({customerId : customerId});
		},
        savecustomer : function(customer) {
            return CustomersService.save({customerId : customer.id},customer);
        },
        saveaddress : function(address) {
            return AddressesService.save({addressId : address.id},address);
        }
	}
}]);

customerMasterServices.factory('CustomersService', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/customers/:customerId',  {customerId : '@customerId'}, {
        query : {
            method : 'GET',
            isArray : true
        },
    	save : {
        	method : 'POST'
        }

    });
}]);

