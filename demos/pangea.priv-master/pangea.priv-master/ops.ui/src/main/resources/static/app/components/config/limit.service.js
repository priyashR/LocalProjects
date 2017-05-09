var limitServices = angular.module('LimitServices', ['ngResource', 'configService']);

limitServices.factory('LimitService', ['CreditLimitService', 'LimitPeriodService', 'LimitLevelsService', 'CreditLimitStatusService', 'CreditLimitOwnerService', 'ProductsService', 'CreditLimitUtilisationsService', 
    function(CreditLimitService, LimitPeriodService,LimitLevelsService, CreditLimitStatusService, CreditLimitOwnerService, ProductsService, CreditLimitUtilisationsService, CurrencyType) {
	return {
		limits : function () {
			return CreditLimitService.query();
		},
        saveCreditLimit : function (limit) {
            return CreditLimitService.save(limit);
        },
        limitPeriod : function () {
            return LimitPeriodService.query();
        },
        limitStatuses : function () {
            return CreditLimitStatusService.query();
        },
        limitOwner : function () {
            return CreditLimitOwnerService.query();
        },
        allProducts : function () {
            return ProductsService.query();
        },
        getLimitUtilisations : function (customerId) {
            return CreditLimitUtilisationsService.query({
                    customerId : customerId
            });
        },
        limitLevels : function() {
            return LimitLevelsService.query();
        },
        
        allCurrencyTypes : function() {
			return CurrencyType.query();
		}
		
	}
}]);

limitServices.factory('CreditLimitService', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/limit',{}, {
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

limitServices.factory ('LimitPeriodService', ['$resource', 'config', function ($resource, config) {
    return $resource(config.apiUrl + '/limitPeriods', {}, {
        query : {
            method : 'GET',
            isArray : true
        }
    })
}]);

limitServices.factory ('LimitLevelsService', ['$resource', 'config', function ($resource, config) {
    return $resource(config.apiUrl + '/limitLevels', {}, {
        query : {
            method : 'GET',
            isArray : true
        }
    })
}]);

//limitServices.factory ('LimitLevelsService', ['$resource', 'config', function ($resource, config) {
//    return $resource(config.apiUrl + '/limitLevels', {}, {
//        query : {
//            method : 'GET',
//            isArray : true
//        }
//    })
//}]);


limitServices.factory('CurrencyType', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/currencies', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);


limitServices.factory ('CreditLimitStatusService', ['$resource', 'config', function ($resource, config) {
    return $resource(config.apiUrl + '/creditLimitStatuses', {}, {
        query : {
            method : 'GET',
            isArray : true
        }
    })
}]);

limitServices.factory ('CreditLimitOwnerService', ['$resource', 'config', function ($resource, config) {
    return $resource(config.apiUrl + '/creditLimitOwners', {}, {
        query : {
            method : 'GET',
            isArray : true
        }
    })
}]);

limitServices.factory ('ProductsService', ['$resource', 'config', function ($resource, config) {
    return $resource(config.apiUrl + '/products', {}, {
        query : {
            method : 'GET',
            isArray : true
        }
    })
}]);

limitServices.factory ('CreditLimitUtilisationsService', ['$resource', 'config', function ($resource, config) {
    return $resource(config.apiUrl + '/limitUtilisations/:customerId', {customerId : '@customerId'}, {
        query : {
            method : 'GET',
            isArray : true,
            params : {customerId : '@customerId'}
        }
    })
}]);
