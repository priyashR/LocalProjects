var commonServices = angular.module('commonServices', ['ngResource', 'configService','ngCookies']);

commonServices.factory('resolveNodeTree', ['$rootScope',function($rootScope) {
    return {
        resolve: function(nodes) {
           for (var i=0;i<nodes.length;i++){
				var nav = nodes[i];
				for (var key in nav) {
					if (nav.hasOwnProperty(key) && nav[key] && nav[key].indexOf) {
						if(nav[key].indexOf("return")>=0 || nav[key].indexOf("return function()")>=0 || nav[key].indexOf("function()")>=0){
							nav[key] = new Function('$rootScope',nav[key])($rootScope);
						}
					}
				}
			}
        }
    };
}]);

commonServices.factory('navServiceTop', [ '$rootScope','NavigationConfig', 'resolveNodeTree',
    function($rootScope,NavigationConfig,resolveNodeTree) {

	return {initNavigation : function() {
		NavigationConfig.query({menuGroup:'top-config'}).$promise.then(function(data){
			$rootScope.adminMenuTabs = data; 
			resolveNodeTree.resolve($rootScope.adminMenuTabs,$rootScope);
		})
	}};

}]);

commonServices.factory('navService', [ '$rootScope','NavigationConfig', 'resolveNodeTree',
           function($rootScope,NavigationConfig,resolveNodeTree) {

	return {initNavigation : function() {
		NavigationConfig.query({menuGroup:'menu-config'}).$promise.then(function(data){
			$rootScope.menuTabs = data; 
			resolveNodeTree.resolve($rootScope.menuTabs,$rootScope);
		})
	}};

}]);

commonServices.factory('leftNavService', [ '$rootScope', function($rootScope) {
	return {
		initNavigation : function() {
			
			$rootScope.tasks = {};
			
			$rootScope.customer = {};
			$rootScope.customer.tabs = [ {
				"name" : "search",
				"path" : "/customer/search",
				"heading" : "Customer search",
				"icon" : "glyphicon-search",
				"uri" : "app/components/customer/customerSearch.html",
				"active" : "active"
			}, {
				"name" : "detail",
				"path" : "/customer/data",
				"heading" : "Retrieve data",
				"icon" : "glyphicon-download-alt",
				"uri" : "app/components/deal/partials/deal-customer.html"
			}, {
				"name" : "contact",
				"path" : "/customer/contact",
				"heading" : "Contact details",
				"icon" : "glyphicon-earphone",
				"uri" : "app/components/deal/partials/deal-product.html"
			}, {
				"name" : "account",
				"path" : "/customer/account",
				"heading" : "Account",
				"icon" : "glyphicon-credit-card",
				"uri" : "app/components/deal/partials/deal-financials.html"
			}, {
				"name" : "rm",
				"path" : "/customer/rm",
				"heading" : "Relationship manager",
				"icon" : "glyphicon-user",
				"uri" : "app/components/deal/partials/deal-advices.html"
			}, {
				"name" : "review",
				"path" : "/customer/review",
				"heading" : "Review",
				"icon" : "glyphicon-ok-sign",
				"uri" : "app/components/deal/partials/deal-charges.html"
			}];
			
			$rootScope.pricing = {};
			$rootScope.pricing.tabs = [ {
				"name" : "p1",
				"path" : "/pricing",
				"heading" : "Pricing",
				"icon" : "glyphicon-download-alt",
				"uri" : "",
				"active" : "active"
			}, {
				"name" : "p2",
				"path" : "/pricing/page2",
				"heading" : "Pricing pg2",
				"icon" : "glyphicon-search",
				"uri" : ""
			}, {
				"name" : "p3",
				"path" : "/pricing/page3",
				"heading" : "Pricing pg3",
				"icon" : "glyphicon-search",
				"uri" : ""
			}  ];
			
			$rootScope.deals = {};
			$rootScope.deals.tabs = [{
				"name" : "deal",
				"path" : "/deal",
				"heading" : "Deal information",
				"icon" : "glyphicon-download-alt",
				"uri" : "app/components/deal/partials/deal-main.html",
				"disabled" : function(){ return $scope.deals.customer ? false : true}
			}, {
				"name" : "parties",
				"path" : "/deal/parties",
				"heading" : "Other parties information",
				"icon" : "glyphicon-earphone",
				"uri" : "/app/components/party/partials/party-main.html",
				//"disabled" : function(){ return ($scope.deals.deal && $scope.deals.deal.id) ? false : true},
				"disabled" : function(){ return ($scope.allparties) ? false : true}
			}, {
				"name" : "documents",
				"path" : "/deal/documentation",
				"heading" : "Documentation",
				"icon" : "glyphicon-user",
				"uri" : "/app/components/deal/partials/document/document-main.html",
				"disabled" : function(){ return ($scope.docsverify) ? false : true}
			}, {
				"name" : "settlement",
				"path" : "/deal/settlement",
				"heading" : "Settlement of charges",
				"icon" : "glyphicon-credit-card",
				//"uri" : "/app/components/deal/partials/deal-default-charges-main.html",
				//"uri" : "/app/components/deal/partials/charges/deal-settlement-charges-main.html",
                "uri" : "/app/components/deal/partials/charges/deal-charges-main.html",
				"disabled" : function(){return ( $scope.settlementverify) ? false : true }
			/*}, {
				"name" : "schedule",
				"path" : "/deal/schedule",
				"heading" : "Recovery Schedule",
				"icon" : "glyphicon-credit-card",
				"uri" : "app/components/recovery/partials/charge-main.html",
				"disabled" : function(){ return ($scope.deals.deal && $scope.deals.deal.id) ? false : true}*/
			}, {
				"name" : "review",
				"path" : "/deal/review",
				"heading" : "Review",
				"icon" : "glyphicon-briefcase",
				"uri" : "/app/components/deal/partials/deal-review.html",
				"disabled" : function(){ return ($scope.reviewverify) ? false : true}
			}];
			$rootScope.deals.charge = {};
			$rootScope.deals.charge.tabs =[{
				"name" : "charge",
				"path" : "/deal/charge",
				"heading" : "Default Charges",
				"disabled" : true,
				"uri" : "/app/components/deal/partials/charges/default/deal-default-charges.html",
				"default" : true,

			},{
				"name" : "charge-settlement",
				"path" : "/deal/charge-settlement",
				"heading" : "Settlement of charges",
				"disabled" : false,
				"uri" : "/app/components/deal/partials/charges/settlement/deal-charge-settlement.html",
				"default" : false
			},{
                "name" : "charge-arrears",
                "path" : "/deal/charge-arrears",
                "heading" : "Arrear charges",
                "disabled" : false,
                "uri" : "/app/components/deal/partials/charges/arrear/deal-charge-settlement-arrear.html",
                "default" : false
             },{
				"name" : "charge-posting",
				"path" : "/deal/charge-posting",
				"heading" : "Accounting",
				"disabled" : false,
				"uri" : "/app/components/deal/partials/charges/postings/deal-charge-posting.html",
				"default" : false
			}
			];
		
						
			$rootScope.case = {};
			$rootScope.case.tabs = [{
				"path" : "/case",
				"heading" : "Case Management",
				"icon" : "glyphicon-user",
				"uri" : "app/components/case/case.html"
			}];
			$rootScope.case.tabs.hideLeftNav = true;
			
			$rootScope.case.breadCrumbTabs = [
			{
				"path" : "/case/caseInformation",
				"heading" : "Case Information",
				"uri" : "app/components/case/partials/case-information.html",
				"state" : "case-information",
				"disabled" : false,
				"default" : true
			},{
				"path" : "/case/caseReview",
				"heading" : "Case Review",
				"uri" : "app/components/case/partials/case-review.html",
				"state" : "case-review",
				"disabled" : true,
				"default" : false
			},{
				"path" : "/case/caseFFPChecklist",
				"heading" : "Case Fit For Process Checklist",
				"uri" : "app/components/case/partials/case-ffp-checklist.html",
				"state" : "case-ffp",
				"disabled" : false,
				"default" : false
			},{
				"path" : "/case/caseExecutionChecklist",
				"heading" : "Case Execution Checklist",
				"uri" : "app/components/case/partials/case-execution-checklist.html",
				"state" : "case-execution",
				"disabled" : false,
				"default" : false
			}];
			
			$rootScope.limit = {};
			$rootScope.charge = {};
			$rootScope.makerchecker = {};
			$rootScope.makerchecker.tabs = [{
				"path" : "/case",
				"heading" : "Checker Review",
				"icon" : "glyphicon-check",
				"uri" : "app/components/deal/partials/makerchecker.html"
			}];

			$rootScope.dealchecker = {};
			$rootScope.dealchecker.tabs = [{
				"path" : "/case",
				"heading" : "Deal Review",
				"icon" : "glyphicon-check",
				"uri" : "app/components/case/partials/case-application-information-review.html"
			}];
			

		}
	};

} ]);


commonServices.factory('NavigationConfig', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/navigationNodes/:menuGroup', {menuGroup:'@menuGroup'}, {
		
	});
} ]);

commonServices.factory('lookupDataService', function($resource, config, CommunicationType, GenderType, IdentificationType, Country, CustomerType, IndustryType, Language, Province, Region, SectorType,PartyCategory,BankTypes,Status,PartyTypes,ClearingSystems,Classifications,Steps,AddressTypes,ContactTypes,Banks,CurrencyTypes,AccountTypes,Branches, SanctionStatuses, ConditionFunctions,ChargeTypes,RecoveryTypes,Periods,BaseCodes,Bands,CalcPeriods,Taxes,ChargeStructureStatuses,ProductGroups,ChargeCodeService) {
	return {
		allCommunicationTypes: function() {
			return CommunicationType.query();
		},
		allGenderTypes: function() {
			return GenderType.query();
		},
		allIdentificationTypes: function() {
			return IdentificationType.query();
		},
		allCountries: function() {
			return Country.query();
		},
		allCustomerTypes: function() {
			return CustomerType.query();
		},
		allIndustryTypes: function() {
			return IndustryType.query();
		},
		allLanguages: function() {
			return Language.query();
		},
		allProvinces: function() {
			return Province.query();
		},
		allRegions: function() {
			return Region.query();
		},
		allSectorTypes: function() {
			return SectorType.query();
		},
		allPartyCategories: function(){
			return PartyCategory.query();
		},
		allBankTypes: function(){
			return BankTypes.query();
		},
		allStatuses: function(){
			return Status.query();
		},
		allPartyTypes : function() {
			return PartyTypes.query();
		},
		allClearingSystems : function() {
			return ClearingSystems.query();
		},
		allClassifications : function() {
			return Classifications.query();
		},
		allSteps : function() {
			return Steps.query();
		},
		allAddressTypes : function() {
			return AddressTypes.query();
		},
		allContactTypes : function() {
			return ContactTypes.query();
		},
		allBanks : function(){
			return Banks.query();
		},
		allCurrencyTypes : function(){
			return CurrencyTypes.query();
		},
		allAccountTypes : function(){
			return AccountTypes.query();
		},
		allBranches : function(){
			return Branches.query();
		},
		allSanctionStatuses : function(){
			return SanctionStatuses.query();
		},
		allConditionFunctions : function(){
			return ConditionFunctions.query();
		},
		allChargeCodes : function(){
			return ChargeCodeService.query();
		},
		allChargeTypes : function(){
			return ChargeTypes.query();
		},
		allRecoveryTypes : function(){
			return RecoveryTypes.query();
		},
		allPeriods : function(){
			return Periods.query();
		},
		allBaseCodes : function(){
			return BaseCodes.query();
		},
		allBands : function(){
			return Bands.query();
		},
		allCalcPeriods : function(){
			return CalcPeriods.query();
		},
		allTaxes : function(){
			return Taxes.query();
		},
		allChargeStructureStatuses : function(){
			return ChargeStructureStatuses.query();
		},
		allProductGroups : function(){
			return ProductGroups.query();
		},
		allGuaranteeWordingTypes : function() {
			return $resource(config.apiUrl + '/deal/wordingTypes', {}, {}).query();
		},
		allDeliveryMethods : function() {
			return $resource(config.apiUrl + '/deliveryMethod', {}, {}).query();
		},
		allPriority : function() {
			return $resource(config.apiUrl + '/priority', {}, {}).query();
		}
	}
});

commonServices.factory('CommunicationType', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/communicationTypes', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('GenderType', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/genders', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('IdentificationType', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/identificationTypes', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Country', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/countries', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('CustomerType', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/customerTypes', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('IndustryType', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/industries', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Language', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/languages', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Province', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/provinces', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Region', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/regions', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('SectorType', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/sectors', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('PartyCategory', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/partycategories', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('BankTypes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/banktypes', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Status', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/statuses', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('PartyTypes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/partytypes', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('ClearingSystems', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/clearingsystems', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Classifications', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/classifications', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Steps', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/step', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('AddressTypes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/addresstype', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('ContactTypes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/contacttype', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Banks', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/bank', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('CurrencyTypes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/currencies', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('AccountTypes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/accountTypes', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Branches', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/branches', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('SanctionStatuses', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/sanctionStatuses', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('ConditionFunctions', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/conditionFunctionsLookup', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('ChargeTypes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/chargetypes', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('RecoveryTypes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/recoveryTypes', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Periods', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/periods', {}, {});
} ]);

commonServices.factory('BaseCodes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/baseCodes', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Bands', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/bands', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('CalcPeriods', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/calcperiod', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('Taxes', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/taxesLookup', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('ChargeStructureStatuses', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/chargeStructureStatusesLookup', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

commonServices.factory('ProductGroups', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/productGroupsLookup', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		}
	});
} ]);




