var dealchecksControllers = angular.module('DealChecksControllers', ['ngResource','ngRoute','customer.services.app','commonServices']);


dealchecksControllers.controller('DealChecksCtrl', ['$scope','$location', '$window', '$rootScope','DealLookupService','DealCustomer','lookupDataService','Deal','ProductPreChecks','WorkflowService','CaseService','$routeParams',
                                                   function($scope,$location,$window, $rootScope,DealLookupService,DealCustomer,lookupDataService,Deal,ProductPreChecks,WorkflowService,CaseService,$routeParams) {
   		

    
  //#########################################################################
  //DUPLICATED FROM CASE CONTROLLER   ^^^^^^^^^

        $scope.case.instance={};
        $scope.customers = {};
        $scope.customer = {};
        $scope.deal = {};
        $scope.breadCrumbTabs = $rootScope.case.breadCrumbTabs;
        $scope.activeCrumb = {};
        $scope.search={};
        $scope.precheckLog = [];

    	$scope.isPrecheckOk = function(){
    		if(!$scope.deal.preChecks){
    			$scope.deal.preChecks = [];
    		}
    		angular.forEach($scope.preChecks, function(preCheck,key){
    			angular.forEach(preCheck, function(pre){
    				var indexOf = this.indexOf(pre.description);
    				if(indexOf<0 && (!$scope.validateSinglePreCheck(pre))){
    					this.push(pre.description);
    					$scope.managePrecheck(false,pre);
    				}else if(indexOf>=0 && ($scope.validateSinglePreCheck(pre))){
    					this.splice(indexOf,1);
    					$scope.managePrecheck(true,pre);
    				}
    			},this);
    		}, $scope.precheckLog);
    		
    		return $scope.precheckLog.length;
    	};
    	
    	$scope.managePrecheck = function(isValid,pre){
    		var indexOf = $scope.deal.preChecks.indexOf(pre);
    		if (isValid && indexOf<0){
    			$scope.deal.preChecks.push(pre);
    		}else if (indexOf>=0 && !isValid){
    			$scope.deal.preChecks.splice(indexOf,1);
    		}
    	}
    	
    	$scope.validateSinglePreCheck = function(pre){
    		return pre.selectedvalue && (!pre.passOption || pre.selectedvalue === pre.passOption.code);
    	}
    	
    	$scope.preChecks = {};
    	$scope.initPreChecks = function(){
    		if ($scope.deal.productTypeCode && $scope.deal.step){
    			ProductPreChecks.query({productTypeId:$scope.deal.productTypeCode,stepTypeCode:$scope.deal.step,phaseCode:'approval:0'}).$promise.then(function(preCheckData){
    					var productStepPreChecks = preCheckData;
    					var context = {};
    					context.lastGroup = undefined;
    					context.preChecks = [];
    					angular.forEach(productStepPreChecks, function(preCheck, key) {
    						if (context.lastGroup !== preCheck.relativeGroup && context.preChecks.length>0){
    							$scope.preChecks[context.lastGroup]=context.preChecks;
    							context.preChecks = [];
    						}
    						context.lastGroup = preCheck.relativeGroup
    						$scope.mapPreCheck(preCheck);
    						context.preChecks.push(preCheck);
    					},context);
    					$scope.preChecks[context.lastGroup]=context.preChecks;
    					
    			});
    		}
    	}
    	
    	$scope.getDisabled = function(pre){
    		return pre.disabled;
    	}

    	$scope.mapPreCheck = function(preCheck){
    		if ($scope.deal.preChecks){
    			angular.forEach($scope.deal.preChecks,function(pCheck){
    				if (pCheck.id === preCheck.id){
    					preCheck.selectedvalue = pCheck.selectedvalue;
    					return true;
    				}
    			});
    		}
    	}

    	$scope.complete = function() {
    		if ($scope.release.valid) {
    			WorkflowService.complete($scope.task, 'completed', function() {
               		$location.path("/deal/final/"+$scope.case.dealId);
                 	});
//            	var _status = WorkflowService.readVariable($scope.task, 'status', function() {
//            		var status = 'review'; //set to review from maker
//            		var path = '/tasks';
//            		var tab = 'review';
//            		if (_status.value == 'review') { //checker review successful so task can be completed
//            			status = 'completed';
//            			tab = 'wip';
//            		}
//            	});    			
    		} else {
    			WorkflowService.complete($scope.task, 'unsuccessful', function() {
    	              $location.path("/tasks").search({t : 'review'});
    	    	  });    			
    		}
    	};
    	
        $scope.initChecks = function(){
           
        	$scope.release = {valid : true};
        	
      	  if ($routeParams.taskId) {
  			var task = WorkflowService.get($routeParams.taskId, function() {
  				$scope.task = task;
  	        	var _res = WorkflowService.readVariable($scope.task, 'caseId', function() {
  	                var _caseId = _res.value;

  	                CaseService.cases.get({caseId : _caseId}, function (data) {
  	                  $scope.deal = data;
  	                  $scope.case = data;
  	                  $scope.initPreChecks();
  	                });
  	        	});
  			});
      	  }
            
            //});

            
          }

	} 
]);



