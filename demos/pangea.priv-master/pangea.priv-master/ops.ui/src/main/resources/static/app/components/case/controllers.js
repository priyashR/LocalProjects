(function(){
	'use strict';
	
	angular
		.module('pangea')
		.controller('caseController', CaseController);
	
	CaseController.$inject = ['$scope', '$sce', '$routeParams','$location', '$rootScope','$http','lookupDataService','CustomerService','searchTradeCustomerServices','CaseLookupService','Deal','CaseService','WorkflowService','ProductPreChecks','localStorageService']
	
	function CaseController($scope,$sce,$routeParams,$location,$rootScope,$http,lookupDataService,CustomerService,searchTradeCustomerServices,CaseLookupService,Deal,CaseService,WorkflowService,ProductPreChecks,localStorageService) {

		/**
		 * JHP - This is a very important sequence of assignment,
		 * any subsequent assignments to the local copies of deal, and customer
		 * should be done using angular.copy to avoid losing the reference.
		 * I am working on a solution to replace the excessive use of $rootScope
		*/
		//################################
		$scope.case = {};
		$scope.customer = {};
		//################################
		
		$scope.precheckLog = [];
		$scope.isCaseOnTask = false;
		$scope.attachments = {};
		$scope.customers = {};
		$scope.activeCrumb = {};
		$scope.search={};
		$scope.preChecks = {};

		$scope.breadCrumbTabs = angular.copy($rootScope.case.breadCrumbTabs);
		$scope.state = $scope.breadCrumbTabs[0].state;

		$scope.identifyCrumb = function(){
			if ($scope.case.productTypeCode && $scope.case.stepCode){
				$scope.selectCrumb($scope.breadCrumbTabs[2]);
			}
			if ($scope.case.preChecks && $scope.case.preChecks.length>0){
				$scope.selectCrumb($scope.breadCrumbTabs[3]);
			}
		};
		
		/**
		 * Save the AppCase
		 */
		$scope.save = function() {
//			CaseService.cases.get({"caseId" : $scope.case.id}, function (data) {
//				$scope.case=angular.copy(data);
//			})
			;
		}
		
		/**
		 *Move to next tab
		 */
		$scope.nextPage = function(){
			localStorageService
			if ($scope.activeCrumb.uri){
				for (var i=0;i<$scope.breadCrumbTabs.length;i++){
					if ($scope.isActiveCrumb($scope.breadCrumbTabs[i]) && (i < $scope.breadCrumbTabs.length-1) ){
						$scope.activeCrumb = $scope.breadCrumbTabs[i+1];
						$scope.activeCrumb.default = true;
						$scope.state = $scope.activeCrumb.state;
						//WorkflowService.updateVariables($scope.task, [{
						//	"name": "state",
						//	"type": "string",
						//	"scope": "global",
						//	"value": $scope.state
						//}]);
						$rootScope.block=true;
						break;
					}
				}
			}else{
				$scope.selectCrumb($scope.breadCrumbTabs[0]);
			}
		};
		
		/**
		 *Move to previous tab
		 */
		$scope.backPage = function(){
			localStorageService
			if ($scope.activeCrumb.uri){
				for (var i=0;i<$scope.breadCrumbTabs.length;i++){
					if ($scope.isActiveCrumb($scope.breadCrumbTabs[i]) && (i > 0) ){
						$scope.activeCrumb = $scope.breadCrumbTabs[i-1];
						$scope.state = $scope.activeCrumb.state;
						//WorkflowService.updateVariables($scope.task, [{
						//	"name": "state",
						//	"type": "string",
						//	"scope": "global",
						//	"value": $scope.state
						//}]);
						$rootScope.block=true;
						break;
					}
				}
			}else{
				$scope.selectCrumb($scope.breadCrumbTabs[0]);
			}
		};
		
		if ($routeParams.taskId) {
			var task = WorkflowService.get($routeParams.taskId, function() {
				
				$scope.task = task;
				
				$scope.initChanelFrom(task);
				$scope.refreshAttachments(task);
				
				var variables = WorkflowService.readVariables(task, function() {
					var _caseId = WorkflowService.selectVariable(variables, 'caseId');
					if (_caseId) {
						$scope.selectCrumb($scope.breadCrumbTabs[1]);
						CaseService.cases.get({caseId : _caseId.value}, function (data) {
							
							$scope.case=angular.copy(data);
							$scope.isCaseOnTask = true;
							$scope.initPreChecks();
							CaseService.caseCustomers.get({caseId: _caseId.value}, function(data) {
								$scope.customer = angular.copy(data);
								$scope.customers = [ $scope.customer ];
							});

							if ($location.search().processToDeal == "true") {
								$scope.processToDeal();
							}else{
								$scope.identifyCrumb();
							}
						});
						
					} else {
						var _cusId = WorkflowService.selectVariable(variables, 'customerId');
						if (_cusId) {
							//TODO EVT: There is no service to retrieve a customer by id, will finish when there is time
						}
					}
				});
				
			});
			
		}
	
		$scope.initChanelFrom = function(task){
			$rootScope.initInputChannel(task).then(function(data){
				$scope.caseProperties = localStorageService.get('entryChanelForm');
			});
		};

		$scope.isNewDeal = function(){
			return $scope.case.stepCode === 'ISS';
		};
		
		$scope.isExistingDeal = function(){
			return $scope.case.stepCode === 'AMD';
		};
		
		$scope.isActiveCrumb = function(crumb) {
			return $scope.activeCrumb === crumb;	
		};
	
		$scope.selectTabClass = function(tab, type){ 
			if(tab.default){
				return  "active" 
			}

			if( !tab.default && type == 'next'){
				return  "active" 
			}
			if(!tab.disabled){
				return  "disabled" 
			}
			
		};
		
		$scope.selectCrumb = function(crumb) {
			
		    $scope.removeActiveTab($scope.breadCrumbTabs);
			crumb.disabled = true;
			crumb.default  = true;
	
			$scope.activeCrumb = crumb;
			$scope.getCrumbContent()
			$scope.state = crumb.state;
			
		};
	
		$scope.removeActiveTab = function(tabs){
			angular.forEach(tabs, function(value) {
				value.default = false
			});
	
		};
		
		$scope.getCrumbContent = function() {
			
			var resultUri;
			
			if (!$scope.activeCrumb.uri){
				$scope.nextPage();
			}
			
			if (isFunction($scope.activeCrumb.uri)) {
				resultUri = $scope.activeCrumb.uri();
			} else {
				resultUri = $scope.activeCrumb.uri;
			} 
			
			return resultUri;
		};
		
		$scope.isKeyInResult = function(key){
			return !!(key !== 'addresses' && key !== 'contacts' && key !== 'idType' && key !== 'customerAccounts');

		};
		
		$scope.getCustomers = function(){
			return $scope.customers;
		};
		
		$scope.search = function() {
			var param = {};
			param[$scope.search.criteria]=$scope.search.value;
			
            CustomerService.customerSearch.search(param, function(response) {
                $scope.customers = response;
            });
		};
	
		$scope.resetForm = function() {
			$scope.searchForm = {};
			$scope.customers = {};
			$scope.searchResult = false;
		};
		
		$scope.saveAndProgress = function(){
			$scope.saveCase().then(function(data){
				$scope.nextPage();
			});
		};
		
		$scope.saveAndProgressToDeal = function(processToDeal){
			$rootScope.block=true;
			$scope.saveCase(true).then(function(data){
				$scope.processToDeal();
			});
		};
		
		$scope.selectCaseCustomer = function(customer) {
			$scope.customer = angular.copy(customer, $scope.customer);
	
			//TODO-JHP assumed contact, need revise
			//if($scope.customer.contacts.length){
			//	$scope.customer.defaultContact = $scope.customer.contacts[0];
			//	$scope.customer.defaultContact.name=$scope.customer.name;
			//	$scope.customer.defaultContact.method = 'email';
			//}

			$scope.saveAndProgress();
			
		};
		
		$scope.productTypeSelected = function(val) {
			angular.forEach($scope.allProductTypes, function(value, key) {
				if (val === value.id) {
					$scope.case.productClassificationTypeCode = value.productClassification.id;
					return true;
				}
			});
		};

		$scope.combine = function(result) {
			var combo;
			if(result == null) {
				return '';
			}
			if(result.surname && result.surname !== '') {
				combo = result.name + ' ' + result.surname;
			}
			else {
				combo = result.name;
			}
			if(result.tradeName && result.tradeName !== '') {
				combo = combo + ' (' + result.tradeName + ')';
			}
			return combo;
		};

		$scope.customerSearchFilter = function(val) {
			var param = {
				'domain': 'Trade',
				'combination': val
			};

			return CustomerService.customerSearch.search(param).$promise;
		};

		$scope.selectCustomer = function($item, $model, $label) {
			var param = {};
			//param[$scope.search.criteria]=$item.name;
			if($item.surname && $item.surname !== '') {
				param['combination'] = $item.surname;
			}
			else if($item.tradeName && $item.tradeName !== '') {
				param['combination'] = $item.tradeName;
			}
			else {
				param['combination'] = $item.name;
			}
			param['domain'] = 'Trade';

			CustomerService.customerSearch.search(param, function(response) {
				$scope.customers = response;
				angular.forEach($scope.customers, function(customer) {
					CustomerService.customerAccounts.get({id: customer.id}, function(response) {
						customer.accounts = response;
					})
				});
			});
		};

		$scope.suggest = function(val) {
			return Deal.query({"reference": val}).$promise;
		};

		$scope.select = function($item, $model, $label) {
			$location.path( "/deals/" + $item.reference );	
		};
		
		$scope.saveCase = function(processToDeal){
			//JHP - Need to correct DTO

			$scope.case.relatedCustomerId = $scope.customer.id;
			return CaseService.cases.save($scope.case).$promise
				.then(function(data){
					$scope.case =  angular.copy(data,$scope.case);

					if($scope.customer && $scope.customer.idType) {
						$scope.customer.idTypeCode = $scope.customer.idType.id;
					}
					CaseService.caseCustomers.save({"caseId":$scope.case.id}, $scope.customer).$promise
						.then(function(data) {
							$scope.customer = angular.copy(data, $scope.customer);
						});

					if (processToDeal) {
						CaseService.escalations.query({"caseId":$scope.case.id}).$promise
							.then(function(escalations) {
								var payload = {
									"escalations": []
								};

								if (escalations.length > 0) {
									for (var i = 0; i < escalations.length; i++) {
										var esc = {};
										esc.name = escalations[i].code;
										esc.due = '';
										payload.escalations.push(esc);
									}

									WorkflowService.escelations.save({"id":$scope.task.processInstanceId}, payload,
										function(data) {
											CaseService.prechecks.save({"caseId":$scope.case.id}, {});
										});
								}
							});

					}
				
				$scope.initPreChecks();
				if ($scope.task && !$scope.isCaseOnTask) {
					var payload = [{
						"name" : "caseId",
						"scope" : "global",
						"value" : data.id,
						"type" : "integer"
					}];
					payload.push({
						"name" : "customerName",
						"scope" : "global",
						"value" : $scope.customer.name,
						"type" : "string"
					});
					payload.push({
						"name" : "customerId",
						"scope" : "global",
						"value" : $scope.customer.idNumber,
						"type" : "string"
					});
					payload.push({
						"name" : "customerRM",
						"scope" : "global",
						"value" : $scope.customer.rmName,
						"type" : "string"
					});
					
					WorkflowService.updateVariables($scope.task, payload).$promise
						.then(function() {
							$scope.isCaseOnTask = true;
							if (processToDeal){
								$scope.processToDeal();
						}
					});
					
				} 
			});
		};
		
		 $scope.getSequentialPreCheckIndex = function(groupIndex,preCheckIndex) {
			    var total = 0;
			    for(var i = 0; i < groupIndex; i += 1) {
			    	total += $scope.preChecks[Object.keys($scope.preChecks)[i]].length;
			    }
			    return total + preCheckIndex;
		};
		
		$scope.processToDeal = function(){
			$rootScope.resetDealNav();
			$rootScope.deals.tabs[0].disabled = function () {
				return false;
			};
			if ($scope.task) {
				var variables = WorkflowService.readVariables(task, function() {
					var _caseId = WorkflowService.selectVariable(variables, 'caseId');
					if (_caseId.value) {
						CaseService.cases.get({caseId : _caseId.value}, function (data) {
							
							$scope.case = angular.copy(data,$scope.case);
							$rootScope.deals.deal = angular.copy($scope.case);
							$rootScope.deals.deal.id = undefined;
							$rootScope.deals.deal.caseId = $scope.case.id;
							$rootScope.deals.customer = $scope.customer;
	
							CaseService.caseCustomers.get({caseId : _caseId.value}, function(data) {
								$scope.customer = data;
								var _status = WorkflowService.selectVariable(variables, 'status');
								if (_status.value === 'case') { //Move process to the next step and claim it
									WorkflowService.completeAndClaimNext(task, 'application', function(result) {
										$location.path( "/deal/task/" + result.id);
									});
								} else { //Same task
									$location.path( "/deal/task/" + task.id);
								}
								$rootScope.deals.deal.customerId = data.id;
	
							});
						});
					}
				});
									
			} else {
				//JHP - No reference number yet so only use in-memory data
				$rootScope.deals.deal = $scope.case;
				$rootScope.deals.deal.id = undefined;
				$rootScope.deals.deal.caseId = $scope.case.id;
				$rootScope.deals.customer = $scope.customer;
			
				$location.path( "/deals" );		
			}
			
		};
		
	
		$scope.isPrecheckOk = function(){

			if(!$scope.case.preChecks) {
				$scope.case.preChecks = [];
			}
			angular.forEach($scope.preChecks, function(preCheck,key){
				angular.forEach(preCheck, function(pre){
					var indexOf = this.indexOf(pre.description);
					var isValid = $scope.validateSinglePreCheck(pre);
					if(indexOf<0 && (!isValid)){
						this.push(pre.description);
					}else if(indexOf>=0 && (isValid)){
						this.splice(indexOf,1);
					}
					$scope.managePrecheck(pre);
				},this);
			}, $scope.precheckLog);
			
			return $scope.precheckLog.length;
		};
		
		$scope.indexOfPrecheck = function(arr,pre){
			for(var i=0; i<arr.length; ++i)
			{
			    if(arr[i].id === pre.id)
			        return i;
			}
			return -1;
		};
		
		$scope.managePrecheck = function(pre){
			var indexOf = $scope.indexOfPrecheck($scope.case.preChecks,pre);
			if (pre.selectedvalue){
				if (indexOf<0){
					$scope.case.preChecks.push(pre);
				}else{
					$scope.case.preChecks[indexOf].selectedvalue=pre.selectedvalue;
				}
			}
		};
		
		$scope.validateSinglePreCheck = function(pre){
			var result = 0;
			if (pre.mandatoryCorrect){
				if(pre.selectedvalue){
					if(!pre.passOption || pre.selectedvalue === pre.passOption.code){
						result = 1;
					}
				}
			}else{
				if (pre.mandatory && pre.selectedvalue){
					result = 2;
					if(!pre.passOption || pre.selectedvalue === pre.passOption.code){
						result = 1;
					}
				}
			}
			return result;
		};

		$scope.initPreChecks = function () {
			if ($scope.case.productTypeCode && $scope.case.stepCode) {
				ProductPreChecks.query({
					productTypeId: $scope.case.productTypeCode,
					stepTypeCode: $scope.case.stepCode,
					phaseCode: $scope.task.processDefinitionId
				}).$promise
					.then(function (preCheckData) {
						var productStepPreChecks = preCheckData;
						var context = {};
						context.lastGroup = undefined;
						context.preChecks = [];
						angular.forEach(productStepPreChecks, function (preCheck, key) {
							if (context.lastGroup !== preCheck.relativeGroup && context.preChecks.length > 0) {
								$scope.preChecks[context.lastGroup] = context.preChecks;
								context.preChecks = $scope.preChecks[preCheck.relativeGroup];
								if (!context.preChecks) {
									context.preChecks = [];
								}
							}
							context.lastGroup = preCheck.relativeGroup;
							$scope.mapPreCheck(preCheck);
							context.preChecks.push(preCheck);
						}, context);
						//$scope.
						$scope.preChecks[context.lastGroup] = context.preChecks;

					});
			}
		};

		$scope.getDisabled = function(pre) {
			return pre.disabled;
		};
	
		$scope.mapPreCheck = function(preCheck){
			if ($scope.case.preChecks){
				angular.forEach($scope.case.preChecks,function(pCheck){
					if (pCheck.id === preCheck.id){
						preCheck.selectedvalue = pCheck.selectedvalue;
						return true;
					}
				});
			}
		};
		
		//$scope.allGuaranteeWordingTypes = lookupDataService.allGuaranteeWordingTypes();
	
		$scope.allwithEngagements = [{code:true,description:'With Engagement'},
			{code:false,description:'Without Engagement'}];

		//$scope.allPriority = lookupDataService.allPriority();

	}

})();
