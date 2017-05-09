var dealControllers = angular.module('DealControllers', ['ui.bootstrap', 'ngSanitize', 'xeditable','ngRoute','ngMessages','LocalStorageModule','CustomerMasterServices']);

dealControllers.controller('DealMainCtrl', ['$scope','$sce','$rootScope','$filter','$routeParams','$window','lookupDataService','Deal','DealService', 'DealCustomer', 'DealLookupService','searchTradeCustomerServices', 'WorkflowService', '$route','CaseService','CalculationMethod', '$uibModal','LimitEscalations','localStorageService','DealChargesServiceFactory','CustomerMasterFactory',
   function($scope,$sce, $rootScope, $filter, $routeParams,$window,lookupDataService, Deal,DealService, DealCustomer, DealLookupService,searchTradeCustomerServices,WorkflowService,$route,CaseService,CalculationMethod, $uibModal,LimitEscalations,localStorageService,DealChargesServiceFactory,CustomerMasterFactory) {

	/* DATE Picker */
	$scope.formats = ['yyyy-MM-dd','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$scope.format = $scope.formats[1];
	$scope.readOnly = false;

	$scope.getReadOnly = function(){
		return $scope.readOnly;
	};

	$scope.status = {
		opened: false
	};
	$scope.open = function($event) {
	    $scope.status.opened = true;
	};
	
	$scope.status2 = {
		opened: false
	};
	$scope.open2 = function($event) {
		  $scope.status2.opened = true;
	};

	$scope.status3 = {
    		opened: false
    	};
    	$scope.open3 = function($event) {
    		  $scope.status3.opened = true;
    	};
		
	$scope.setDate = function(year, month, day) {
		$scope.deal.counterGuaranteeExpiryDate = new Date(year, month, day);
	};
	
	/*  Deal - Customer Navigation   */
	
	$scope.selected = {};
	$scope.query = {};
	$scope.customers = {};
	$scope.currentTabIndex = 0;
	$scope.documentIndex = 0;
	$scope.partyIndex = 0;



	$scope.isKeyInResult = function(key){
		if(key!=='addresses' && key!=='contacts' && key!=='idType' && key!=='customerAccounts'){
			return true;
		}
		return false;
	};
	
	$scope.getCustomers = function(){
		return $scope.customers;
	};

	$scope.search = function() {
		searchTradeCustomerServices.query({
			tradename : $scope.query.tradeName
		}, function(response) {
			$scope.customers = response;
			$scope.searchQuery = angular.copy($scope.query.tradeName);
			$scope.searchResult = true;
		});
	};

	$scope.showContract = function(){
		
		$scope.contract_docs = false
	}	

	$scope.editGoodsDesc = function(){
		$scope.goods_desc_input = false;
		$scope.goods_desc_default = true
	}	

	$scope.resetForm = function() {
		$scope.searchForm = {};
		$scope.query.tradeName = '';
		$scope.customers = {};
		$scope.searchResult = false;
	};
	
	$scope.customerTabs = [ 
      	{"heading": "Details", "uri": "/app/components/deal/partials/customer/deal-customer.html"},
      	{"heading": "Contact", "uri": "/app/components/deal/partials/customer/deal-customer-contact.html"},
   		{"heading": "Accounts", "uri": "/app/components/deal/partials/customer/deal-customer-accounts.html"},
   	];
	
	/* Deal - Deal Navigation */

	$scope.initNewDeal = function(){
		$rootScope.deals.deal = {};
		$scope.deal = $rootScope.deals.deal;
        $scope.firstSave();
	}

    $scope.firstSave = function(){
        if($scope.deal && !$scope.deal.id){
            Deal.save($scope.deal).$promise.then(function (data) {
                $rootScope.deals.deal = data;
                $scope.initDealScope();
                if ($scope.task) {
                    var variables = WorkflowService.readVariables($scope.task, function () {
                        var _dealId = WorkflowService.selectVariable(variables, 'dealId');
                        if (!_dealId) {
                            var payload = [{
                                "name": "dealId",
                                "scope": "global",
                                "value": $scope.deal.id,
                                "type": "integer"
                            }];
                            WorkflowService.updateVariables($scope.task, payload);
                        }
                    });
                }
                
            })
        }
    }

    $scope.setNewDefaults = function(){
            if($scope.deal && !$scope.deal.id){
                $scope.deal.applicableSubApplicant = 'Not Applicable';
                $scope.deal.counterGuarantee = 'No';
                $scope.deal.cashCover = 'No';
                $scope.deal.scheduledReduction = 'No';
            }
        }

	$scope.initDeal = function(){
		if ($routeParams.taskId) {
			var task = WorkflowService.get($routeParams.taskId, function() {
				
				$scope.task = task;
				
				$scope.initChanelFrom(task);
				$scope.refreshAttachments(task); 
				
				var variables = WorkflowService.readVariables(task, function() {
					var _dealId = WorkflowService.selectVariable(variables, 'dealId');
					if (_dealId) {
						Deal.get({dealId: _dealId.value}).$promise.then(function (data){
							$rootScope.deals.deal = angular.copy(data);
							$scope.initDealScope();
							$scope.initDealCustomer();
						    $scope.productParties= DealLookupService.productParties($scope.deal.productTypeCode,$scope.deal.stepCode);
							$scope.loadTabs();
                            $scope.getNavTab();
						});						
					} else {
						if($rootScope.deals.deal){
							$scope.loadTabs();
							$scope.initDealScope();
							$scope.productParties= DealLookupService.productParties($scope.deal.productTypeCode,$scope.deal.stepCode);
							if ($scope.deal && $scope.deal.id){
								$scope.initDealCustomer();
                                $scope.getNavTab();
							} else if ($rootScope.deals.deal.customer){
								$scope.customer = $rootScope.deals.deal.customer;
							}
						}else{
							displayMessage('Deal not yet available for this task',"Warning:","warning");
							$window.location.href = '#/case/tasks/'+$routeParams.taskId;
						}
						
					}
				});
			});
		} else {
            if ($routeParams.dealRef){
            	//TODO - This is a temp solution for M-visit bug fix, and a more permanent one needs to be established
				$scope.readOnly = true;
                $rootScope.processTabs.splice(0,4);
                $rootScope.processTabs[0].heading="";
                $rootScope.processTabs[0].disabled=function(){return true};
                $rootScope.processTabs.hideLeftNav = true;
                $rootScope.deals.deal = undefined;

                $routeParams.navi = "review";
                $scope.getNavTab();


            }else{

				if ($rootScope.deals.deal){
					$scope.initDealScope();
				}

				if ($scope.deal && $scope.deal.id){
					$scope.initDealCustomer();
					$scope.productParties= DealLookupService.productParties($scope.deal.productTypeCode,$scope.deal.stepCode);
					$scope.getNavTab();
					$scope.loadTabs();
				} else if ($rootScope.deals.deal.customer){
					$scope.customer = $rootScope.deals.deal.customer;
				}
			}
		}
	}

	$scope.getNavTab = function(){
        if ($routeParams.navi) {
            $routeParams.nav = $routeParams.navi;
        }
	}
	
	$scope.loadTabs = function() {
		if ($scope.deals.deal.productCode == '101' || $scope.deals.deal.productCode == '111' || $scope.deals.deal.productCode == '112') {
			$scope.dealStepsTabs = [
	    		{"heading": "Product", "uri": "/app/components/deal/partials/deal-product.html", "disabled" : true, "default": true},
	    		{"heading": "Expiry Information", "uri": "/app/components/deal/partials/deal-expiry.html", "disabled" : false, "default": false},
                {"heading": "Financial", "uri": "/app/components/deal/partials/deal-financials.html", "disabled" : false, "default": false},
	    		{"heading": "Further Delivery Instructions", "uri": "/app/components/deal/partials/bgi/deal-delivery-bgi.html", "disabled" : false, "default": false},
            ];
		} else {
			$scope.dealStepsTabs = [
        		{"heading": "Product", "uri": "/app/components/deal/partials/deal-product.html", "disabled" : true, "default": true},
        		{"heading": "Expiry Information", "uri": "/app/components/deal/partials/deal-expiry.html", "disabled" : false, "default": false},
                {"heading": "Financial", "uri": "/app/components/deal/partials/deal-financials.html", "disabled" : false, "default": false},
            ];
		}
	}
	
	$scope.initChanelFrom = function(task){
		$scope.caseProperties = localStorageService.get('entryChanelForm');
		if(!$scope.caseProperties){
			$rootScope.initInputChannel(task).then(function(data){
				$scope.caseProperties = localStorageService.get('entryChanelForm');
			});
		}
	}
	
	$scope.initDealScope = function(){
		$scope.deal = $rootScope.deals.deal;
		$scope.setNewDefaults();
        $scope.firstSave(); //TODO JHP - Need to investigate this
	}
	
	$scope.initDealCustomer = function(){
        var result = CustomerMasterFactory.customer($rootScope.deals.deal.customerId).$promise.then(function(data)
        {
            $rootScope.deals.deal.customer = data;
            $scope.customer = $rootScope.deals.deal.customer;
        })
	}
	
	$scope.tabs = [ 
	   	{"heading": "Deal", "uri": "/app/components/deal/partials/deal-detail.html"},
		{"heading": "Product", "uri": "/app/components/deal/partials/deal-product.html"},
		{"heading": "Financial", "uri": "/app/components/deal/partials/deal-financials.html"},
	];

	$scope.dealStepsTabs = [
		{"heading": "Product", "uri": "/app/components/deal/partials/deal-product.html", "disabled" : true, "default": true},
		{"heading": "Financial", "uri": "/app/components/deal/partials/deal-financials.html", "disabled" : false, "default": false},
		{"heading": "General", "uri": "/app/components/deal/partials/bgi/deal-product-bgi.html", "disabled" : false, "default": false},
	];

	$scope.activeCrumb = {};
	$scope.activeChargesCrumb = {};
	
	$scope.isActiveCrumb = function(crumb) { 
		if(crumb.active == 'true'){
			return true
		}
		return $scope.activeCrumb === crumb;	
	}

	$scope.isActiveChargeTab = function(crumb) { 
		return crumb.disabled ? false : true ;	
		return $scope.activeChargesCrumb === crumb;	
	}

	$scope.selectTabClass = function(tab){ 
		if(tab.default ){
			return  "active" 
		}
		if(!tab.disabled){
			return  "disabled" 
		}
	}

	$scope.saveToReview = function(){
		$scope.settlementverify = true;
		$rootScope.deals.tabs[4].disabled = function(){return false}
		$routeParams.nav = 'review';
	}

	$scope.isDisabledTab = function(crumb){
		return crumb.disabled ? true : false ;		
	}
	
	$scope.selectCrumb = function(crumb) {
		$scope.currentTabIndex = $scope.dealStepsTabs.indexOf(crumb);
	    $scope.removeActiveTab($scope.dealStepsTabs);
		crumb.disabled = true;
		crumb.default  = true;
		$scope.activeCrumb = crumb;
		$scope.getCrumbContent()
	};
	
	$scope.getCrumbContent = function() {
		var resultUri;
		if (isFunction($scope.activeCrumb.uri)) {
			resultUri = $scope.activeCrumb.uri();
		} else {
			resultUri = $scope.activeCrumb.uri;
		}
		if (resultUri === undefined){
			resultUri = $scope.dealStepsTabs[0].uri
		}
		return resultUri;
	}

	$scope.removeActiveTab = function(tabs){
		angular.forEach(tabs, function(value) {
			value.default = false
		});

	}

    $scope.validate = function(){
	    if (!$scope.deal.balance || !$scope.deal.principalAmount || $scope.deal.balance !== $scope.deal.principalAmount){
		    $scope.deal.balance = $scope.deal.principalAmount; //TODO JHP - should be done backend
	    }
    }


       $scope.navigate = function() {

        if($scope.readOnly){
            $scope.processWizNavigation();
            return;
		}else {

            $scope.validate();
            Deal.save($scope.deal).$promise.then(function (data) {
                $rootScope.deals.deal = data;
                $scope.initDealScope();

                $rootScope.deals.tabs[0].disabled = function () {
                    return false
                }
                $scope.updateCase();
                if ($scope.task) {
                    var variables = WorkflowService.readVariables($scope.task, function () {
                        var _dealId = WorkflowService.selectVariable(variables, 'dealId');
                        if (!_dealId) {
                            var payload = [{
                                "name": "dealId",
                                "scope": "global",
                                "value": $scope.deal.id,
                                "type": "integer"
                            }];
                            WorkflowService.updateVariables($scope.task, payload);
                        }
                    });
                }

                $scope.processWizNavigation();

                $("html, body").animate({scrollTop: 0}, "slow");
            });
        }
		$scope.status.opened = false;
		$scope.status2.opened = false;
		$scope.status3.opened = false;
	}

   $scope.processWizNavigation = function(){
	   if ($scope.currentTabIndex === $scope.dealStepsTabs.length - 1) {
		   if (!$routeParams.nav) {
			   $routeParams.nav = "deal";
		   }
		   if ($routeParams.nav === "deal"){
			   $scope.forceNav("parties");
		   }
	   } else {
		   $scope.selectCrumb($scope.dealStepsTabs[++$scope.currentTabIndex]);
	   }
   }

	$scope.escalateFinancials = function() {
		LimitEscalations.query({dealId : $scope.deal.id}).$promise.then(function(data) {
			var payload = {
				    "escalations": []
				};

				if (data.length > 0) {
					for (var i = 0; i < data.length; i++) {
						var esc = {};
						esc.name = data[i].code;
						esc.due = '';
						payload.escalations.push(esc);
					}

					WorkflowService.escelations.save({"id":$scope.task.processInstanceId}, payload);
				}

		});
	}

	$scope.updateCase = function(){
		if ($rootScope.case.instance && !$rootScope.case.instance.dealId){
			$rootScope.case.instance.dealId = $scope.deal.id; 
			CaseService.cases.save($rootScope.case.instance).$promise.then(function(data){
				$rootScope.case.instance=data;
			});
		}
	}
	
	$scope.edit = function() {
		rowform.$show();
	};
	
	$scope.tabPreProcessor = function(tab){
		if(tab.heading === 'Settlement of charges'){
			$scope.saveAllDealCharges();
		}
	}
	
	$scope.productSelected = function(val) {
		$scope.deal.productClassificationTypeCode = val;
	};
	
	$scope.productTypeSelected = function(val) {
		angular.forEach($scope.allProductTypes, function(value, key) {
			if (value && $scope.deal.productClassificationTypeCode && val === value.id) {
				$scope.deal.productClassificationTypeCode = value.productClassification.id;
				return true;
			}
		});
	};

	$scope.allCurrencyTypes = DealLookupService.allCurrencyTypes();
	$scope.allExpiryTypes = DealLookupService.allExpiryTypes();
	$scope.allGuaranteeRules = DealLookupService.allGuaranteeRuleTypes();
	$scope.allGuaranteeWordingTypes = DealLookupService.allGuaranteeWordingTypes();
	$scope.allDeliveryMethods = DealLookupService.allDeliveryMethods();
	$scope.allChargesWhen = DealLookupService.allChargesWhen();
	$scope.allChargeCrDrType = DealLookupService.allChargeCrDrType();
	$scope.allRateCodes = DealLookupService.allRateCode();
    $scope.applicableSubApplicants = DealLookupService.applicableSubApplicants();
    $scope.allYesNos = DealLookupService.allYesNos();
    $scope.allNoticePeriods = DealLookupService.allNoticePeriods();
    $scope.allEscapeCauses = DealLookupService.allEscapeCauses();
    $scope.allDeliveryTypes = DealLookupService.allDeliveryTypes();
    $scope.allCourierTypes = DealLookupService.allCourierTypes();
    $scope.allBGISubTypes = DealLookupService.allBGISubTypes();
    $scope.allCashCoverBasis = DealLookupService.allCashCoverBasis();
    $scope.allCashCoverMethods = DealLookupService.allCashCoverMethods();
    $scope.allFurtherIdentification= DealLookupService.allFurtherIdentification();
    $scope.allExpirylocations= DealLookupService.allExpirylocations();

	/*INIT The Deal*/
	$rootScope.initDeal($scope.initDeal,$scope.initDeal);

    $scope.counterGuaranteeShow = false;
    $scope.cashCoverShow = false;
    $scope.cashCoverAmountSelected = false;
    $scope.escapeClause = false;
    $scope.scheduledReductionYes = false;

	$scope.$watch('deal.counterGuarantee', function() {
        if($scope.deal != null){
            $scope.counterGuaranteeShow = ($scope.deal.counterGuarantee ==='Yes');
        }
    });

    $scope.$watch('deal.cashCover', function() {
        if($scope.deal != null){
            $scope.cashCoverShow = ($scope.deal.cashCover ==='Yes');
        }
    });

    $scope.$watch('deal.cashCoverMethod', function() {
        if($scope.deal != null){
            $scope.cashCoverAmountSelected = ($scope.deal.cashCoverMethod ==='Amount');
        }
    });

    $scope.$watch('deal.escapeCause', function() {
        if($scope.deal != null){
            $scope.escapeClause = ($scope.deal.escapeCause ==='Required');
        }
    });

    $scope.$watch('deal.principalCurrencyCode', function() {
        if($scope.deal != null){
            $scope.deal.cashCoverCurrency = $scope.deal.principalCurrencyCode;
        }
    });

    $scope.$watch('deal.scheduledReduction', function() {
        if($scope.deal != null){
            $scope.scheduledReductionYes = ($scope.deal.scheduledReduction==='Yes');
        }
    });

    $scope.$watch('deal.cashCoverPercentage', function() {
        $scope.recalculateCashCover();
    });

    $scope.$watch('deal.principalAmount', function() {
        $scope.recalculateCashCover();
    });

    $scope.recalculateCashCover = function(){
        if($scope.deal != null && $scope.deal.cashCoverMethod ==='Percentage'){
            $scope.deal.cashCoverAmount = ($scope.deal.cashCoverPercentage * $scope.deal.principalAmount)/100;
        }
    }

    $scope.$watch('deal.expiryTypeCode', function() {
        if($scope.deal != null){
            var startdate = moment.utc($scope.deal.issueDate);
            if($scope.deal.expiryTypeCode ==='01'){
                $scope.deal.reviewDate = startdate.add(36, 'months').toDate();
                angular.forEach($scope.allEscapeCauses, function(value, key) {
                    if (value === 'Required') {
                        $scope.deal.escapeCause = value;
                    }
                });
            }else if($scope.deal.expiryTypeCode ==='02'){
                $scope.deal.reviewDate = startdate.add(12, 'months').toDate();
                 angular.forEach($scope.allEscapeCauses, function(value, key) {
                     if (value === 'Not Required') {
                         $scope.deal.escapeCause = value;
                     }
                 });
            }else{
                $scope.deal.reviewDate = undefined;
            }
        }
    });

} ]);