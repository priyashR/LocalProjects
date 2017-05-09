var finServices = angular.module('FinServices', ['ngResource', 'configService']);

finServices.factory('FinancialRecoveryService', [ '$resource','DealRecoverySchedule', function($resource,DealRecoverySchedule) {
return {
		recoverySchedule: function(dealId) {
			return DealRecoverySchedule.query({'dealId':dealId});
		},
		recoveryScheduleFuture: function(dealId) {
        	return DealRecoverySchedule.query({'dealId':dealId,includeCurrent:'false'});
        }
	};
}]);

finServices.factory('DealRecoverySchedule', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl+'/deal/:dealId/recoverySchedule', {dealId:'@dealId'}, {});
} ]);


