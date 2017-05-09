(function() {
	'use strict';

	angular.module('pangea')
			.factory('Deal', Deal)
	 		.factory('DealService', DealService)
	 		.factory('DealCustomer', DealCustomer)
	 		.factory('DealLookupService', DealLookupService)
	 		.factory('LimitEscalations', LimitEscalations)
	 		.factory('searchTradeCustomerServices', searchTradeCustomerServices)
	 		.factory('DealNotesService', DealNotesService);

	Deal.$inject = ['$resource', 'config'];
	DealService.$inject = ['$resource', 'config'];
	DealCustomer.$inject = ['$resource', 'config'];
	DealLookupService.$inject = ['$resource', 'config','ProductService'];
	DealService.$inject = ['$resource', 'config'];
	DealCustomer.$inject = ['$resource', 'config'];
	LimitEscalations.$inject = ['$resource', 'config'];
	searchTradeCustomerServices.$inject = ['$resource', 'config'];
	
	function Deal($resource, config) {
		return $resource(config.apiUrl + '/deal/:dealId', {
			dealId : '@id'
		}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	}

	function DealService($resource, config, $rootScope, Deal) {
		return {
			initDeal : function(dealRef) {
				var dealByRef = $resource(config.apiUrl
						+ '/deal/reference/:reference', {
					reference : '@reference'
				});
				return dealByRef.get({
					reference : dealRef
				}).$promise.then(function(data) {
					$rootScope.deals.deal = data;
					return $rootScope.deals.deal;
				}, function(reason) {
					$rootScope.deals.error = reason;
					throw ($rootScope.deals.error);
				});

			},
			dealCharge : $resource(config.apiUrl + '/charges/debitcredits', {id:'@id',isOverride:'@isOverride'}, {	
					post :{
						method : 'POST',
						isArray : true,
						params: {id:'@id'},
					}
			})
		};
	}

	function DealCustomer($resource, config) {
		return $resource(config.apiUrl + '/deal/:dealId/customer/:customerId',
				{
					dealId : '@dealId',
					customerId : '@customerId'
				}, {
					query : {
						method : 'GET',
						isArray : true
					}
				});
	}

	function DealLookupService($resource, config, ProductService) {
		return {
			allCurrencyTypes : function() {
				return $resource(config.apiUrl + '/currencies', {}, {}).query();
			},
			allExpiryTypes : function() {
				return $resource(config.apiUrl + '/deal/expiryTypes', {}, {}).query();
			},
			allGuaranteeRuleTypes : function() {
				return $resource(config.apiUrl + '/deal/rules', {}, {}).query();
			},
			allProductTypes : function() {
				return ProductService.productTypes.query();
			},
			allProducts : function() {
				return ProductService.products.query();
			},
			allProductClassificationTypes : function() {
				return ProductService.productClassificationTypes.query();
			},
			allGuaranteeWordingTypes : function() {
				return $resource(config.apiUrl + '/deal/wordingTypes', {}, {}).query();
			},
			allDeliveryMethods : function() {
				return $resource(config.apiUrl + '/deliveryMethod', {}, {}).query();
			},
			allTarrifTypes : function() {
				return $resource('app/components/deal/tarrifTypes.json', {}, {}).query();
			},
			allChargesWhen : function() {
				return $resource(config.apiUrl + '/chargewhen', {}, {}).query();
			},
			allChargeCrDrType : function() {
				return $resource(config.apiUrl + '/chargecrdrtypes', {}, {}).query();
			},
			getChargeDrCr : function(dealId, customerId) {
				var dealCharges = $resource(config.apiUrl + '/charges/debitcredits/calculate', { id : '@id'}, 
						{
							calculate : {
								method : 'POST',
								isArray : true,
								params : {
									id : '@id'
								},
						}
				}).calculate({
					id : dealId
				});
				return dealCharges;
			},
			allRateCode : function() {
				return $resource(config.apiUrl + '/ratecode', {}, {}).query();
			},
			getSettlementCharges : function(dealId) {
				var settlementCharges = $resource(config.apiUrl + '/charges/settlements', {
					id : '@id'
				}, {
					query : {
						method : 'GET',
						isArray : true,
						params : {
							id : '@id'
						},
					},
					post : {
						method : 'POST',
						isArray : true,
						params : {
							id : '@id'
						},
					}
				}).query({
					id : dealId
				});
				return settlementCharges;
			},
			allPostingClassificationCode : function() {
				return $resource(config.apiUrl + '/postingClassificationCode', {}, {}).query();
			},
			allCustomerAccount : function(dealId) {
				return $resource(config.apiUrl + '/customer/accounts', {
					id : '@id'
				}, {
					query : {
						method : 'GET',
						isArray : true,
						params : {
							id : '@id'
						},

					}
				}).query({
					id : dealId
				});

			},
			getSettlementPostingCodes : function(dealId) {
				return $resource(config.apiUrl + '/charges/settlement/partycodes', {
					id : '@id'
				}, {
					query : {
						method : 'GET',
						isArray : true,
						params : {
							id : '@id'
						},

					}
				}).query({
					id : dealId
				});

			},
			allSettlementCurrencyTypes : function() {
				return $resource(config.apiUrl + '/settlementCurrencies', {}, {}).query();
			},
			allChargeCodes : function() {
				return $resource(config.apiUrl + '/chargecode/all', {}, {}).query();
			},
			allChargeTypes : function() {
				return $resource(config.apiUrl + '/chargetypes', {}, {}).query();
			},
			allPeriods : function() {
				return $resource(config.apiUrl + '/periods', {}, {}).query();
			},
			allRecoveryTypes : function() {
				return $resource(config.apiUrl + '/recoveryTypes', {}, {}).query();
			},
			getFinancialPosting : function(dealId) {
				return  $resource(config.apiUrl + '/deal/financialPosting', {
					id : '@id'
				}, {
					post : {
						method : 'POST',
						isArray : true,
						params : {
							id : '@id'
						},
					}
				}).post({
					id : dealId
				});
			},
			allYesNos : function() {
				return $resource(config.apiUrl + '/yesNos', {}, {}).query();
			},
			allNoticePeriods : function() {
				return $resource(config.apiUrl + '/noticePeriods', {}, {}).query();
			},
			allEscapeCauses : function() {
				return $resource(config.apiUrl + '/escapeCauses', {}, {}).query();
			},
			allDeliveryTypes : function() {
				return $resource(config.apiUrl + '/deliveryTypes', {}, {}).query();
			},
			allCourierTypes : function() {
				return $resource(config.apiUrl + '/courierTypes', {}, {}).query();
			},
			allBGISubTypes : function() {
				return $resource(config.apiUrl + '/bgiSubTypes', {}, {}).query();
			},
			applicableSubApplicants : function() {
				return $resource(config.apiUrl + '/applicableSubApplicants', {}, {}).query();
			},
			allCashCoverMethods : function() {
				return $resource(config.apiUrl + '/cashCoverMethods', {}, {}).query();
			},
			allCashCoverBasis : function() {
				return $resource(config.apiUrl + '/cashCoverBasis', {}, {}).query();
			},
			allFurtherIdentification : function() {
				return $resource(config.apiUrl + '/furtherIdentification', {}, {}).query();
			},
			allTerms : function() {
				return $resource(config.apiUrl + '/terms', {}, {}).query();
			},
			allSectionTypes : function() {
				return $resource(config.apiUrl + '/sectiontypes', {}, {}).query();
			},
			allExpirylocations : function() {
				return $resource(config.apiUrl + '/expiryLocations', {}, {}).query();
			},			
			productParties : function(productTypeId, stepTypeCode) {
				return $resource(config.apiUrl + '/product/party/types', {}, {}).query({
					productTypeId : productTypeId,
					stepTypeCode : stepTypeCode
				});
			}
		}
	}

	function DefaultChargeStructure($resource, config) {
		return $resource(config.apiUrl + '/chargestructure/:dealId', {
			dealId : '@dealId'
		}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	}

	function searchTradeCustomerServices($resource, config) {
		return $resource(config.apiUrl + '/searchTradeCustomers', {
			tradename : '@tradename'
		}, {
			query : {
				method : 'GET',
				isArray : true,
				params : {
					tradename : '@tradename'
				}
			}
		});
	}

	function DealPartiesCode($resource, config) {
		return $resource(config.apiUrl + '/dealPartiesCode', {}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	}

	function CustomerAccounts($resource, config) {
		return $resource(config.apiUrl + '/customer/accounts', {
			id : '@id'
		}, {
			query : {
				method : 'GET',
				isArray : true,
				params : {
					id : '@id'
				},

			}
		});
	}

	function DeliveryMethod($resource, config) {
		return $resource(config.apiUrl + '/deliveryMethod', {}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	}

	function DealNotesService($resource, config) {
		return $resource(config.apiUrl + '/deal/:dealId/notes/:noteId', {
			dealId : '@dealId',
			noteId : '@noteId'
		}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	}

	function LimitEscalations($resource, config) {
		return $resource(config.apiUrl + '/deal/:dealId/limits/escalations', {
			dealid : '@dealid'
		}, {
			query : {
				method : 'GET',
				isArray : true
			}
		});
	}


})();
