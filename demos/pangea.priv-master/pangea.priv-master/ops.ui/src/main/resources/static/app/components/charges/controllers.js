/**
 * Controllers for Charges 
 */

'use strict';
var chargeControllersApp = angular.module('ChargeControllers',
		[ 'ui.bootstrap', 'configService', 'commonControllers',
				'commonServices',
				
				'ChargeServices', 'ngSanitize', 'ngRoute' ]);

chargeControllersApp.controller('ChargeMainController', ['$scope', 'ChargeLookupService', function($scope, ChargeLookupService) {
	
	$scope.tabs = [ 
   	{"heading": "NewCharge", "uri": "/app/components/charges/partials/charges-setup-new-charge.html"}
	];
	
	$scope.init = function() {
		$scope.chargeType.template = $scope.templates[0];
		
	};

//	
	$scope.templates = [{
        name: 'Optional Charges',
        url: '/app/components/charges/partials/add-charges/deal-charges-optional.html'},
        {
        name: 'Customer Specific Charges',
        url: '/app/components/charges/partials/add-charges/deal-customer-specific-charge.htm'
    }];



	
	$scope.allChargeCodes = ChargeLookupService.allChargeCodes();
	$scope.allChargeTypes = ChargeLookupService.allChargeTypes();

	$scope.chargeType = {};
	$scope.chargeType.template = $scope.templates[0];
	$scope.chargeType.swapTemplate = function(){
		if($scope.chargeType === 'optional'){
			$scope.chargeType.template = $scope.templates[0];
		}
		
		if($scope.chargeType === 'customer-specific'){
			$scope.chargeType.template = $scope.templates[1];
		}
	}


	
} ]);