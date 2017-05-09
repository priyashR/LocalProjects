/**
 * Services for charges
 */

var chargeServices = angular.module('ChargeServices', ['ngResource', 'configService']);

chargeServices.factory('ChargeLookupService', function() {
	return {
		allChargeCodes: function() {
			var _allChargeCodes =  [
						{"id": 1, "description": "Commission"},
						{"id": 2, "description": "Principle Amount"},
						{"id": 3, "description": "Recovery"}
					]
			return _allChargeCodes;
		},
		allChargeTypes: function(){
			return [
			        {"id":1, description: "Fixed Amount"},
			        {"id":2, description: "Fixed Percent"},
			        {"id":3, description: "By Period"}
			        ]
		},
//		initNavigation : function() {
//			
//			$rootScope.deals.charge.add = {};
//			$rootScope.deals.charge.add.tabs =[{
//				"name" : "charge-add",
//				"path" : "/deal/charge/add",
//				"heading" : "Optional Charges",
//				"icon" : "glyphicon-briefcase",
//				"uri" : "/app/components/charges/partials/add-charges/deal-charges-optional.html",
//				"disabled" : function(){return $scope.chargeType == 'Optional'}
//			}
//			]
//		}
	
	
			
	}
});

//	chargesCommonServices.factory('chargesNavService', [ '$rootScope', function($rootScope) {
//		return {
//			initNavigation : function() {
//				
//			$rootScope.deals.charge.add = {};
//			$rootScope.deals.charge.add.tabs =[{
//				"name" : "charge-add",
//				"path" : "/deal/charge/add",
//				"heading" : "Optional Charges",
//				"icon" : "glyphicon-briefcase",
//				"uri" : "/app/components/charges/partials/add-charges/deal-charges-optional.html",
//				"disabled" : function(){return $scope.chargeType == 'Optional'}
//			}
//			]
//		}
//	}
		
chargeServices.factory('ProductCategoryType', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/categories', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);
