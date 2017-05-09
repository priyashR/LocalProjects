(function() {

	angular
		.module('pangea')
		.controller('TaskDashboardController', TaskDashboardController)
		.controller('TaskMainController', TaskMainController);
	
	TaskDashboardController.$inject = ['$scope','$rootScope', '$location','$routeParams', '$window', 'ActivitiService', 'WorkflowService', 'Deal'];
	TaskMainController.$inject = ['$scope', '$location', '$sce', '$routeParams', 'ActivitiService', 'WorkflowService'];
	
	function TaskDashboardController($scope,$rootScope, $location,$routeParams, $window, ActivitiService, WorkflowService, Deal) {
		$scope.taskTabs = [ 
            {"heading": "New", "status" : "new"},
            {"heading": "Work in progress", "status" : "wip"},
            {"heading": "Complete", "status" : "completed"}
	    ];
		
		angular.element($window).bind('resize', function() {
			$scope.filteredTasks = [];
			$scope.searchFilter = '';
			$scope.maxSize = 10;
			$scope.currentPage = 1;
			$scope.numPerPage = Math.max(Math.floor((window.innerHeight-340)/46), 1);	
			$scope.showTab($scope.selectedTab, $scope.selectedTab.pageIndex);
		});
		
		$scope.initView = function() {
			var paramValue = $location.search().t; 
			var found = false;
			if (paramValue) {
				$scope.showTabByParam(paramValue);
			} else {
				WorkflowService.readUserSettings().$promise.then(function(data) {
					$scope.showTabByParam(data.defaultTab);
				});
			}
		};
		
		$scope.showTabByParam = function(param) {
			angular.forEach($scope.taskTabs, function(tab, key) {
				if (tab.status === param) {
					$scope.showTab(tab);
					found = true;
				}
			});
			if (!found) {
				$scope.showTab($scope.taskTabs[0]);
			}		
		}
		
		$scope.showTab = function(tab, index) {
			$scope.filteredTasks = [];
			$rootScope.block=true;
			
			$scope.selectedTab = tab;
			var status = tab.status;
			index = tab.pageIndex;
			if (!index) {
				index = 1;
			}
				
			$scope.loadTasks(status,index,$scope.searchFilter);

		}
		
		$scope.isSelected = function(tab) {
			return tab === $scope.selectedTab;
		};
		
		$scope.claim = function(task, status) {
			$rootScope.resetDealNav();
			if (status === 'new') {
				WorkflowService.completeAndClaimNext(task, 'case', function(result) {
					$scope.resolveView(result);
				});
			} else {
				WorkflowService.forceClaim(task, function(result) {
					$scope.resolveView(result);
				});
			}
			
		};
		
		$scope.view = function(process) {
            $rootScope.resetDealNav();
			ActivitiService.variableHistory.query({}, {processInstanceId : process.id, variableName : 'dealId'}, function(data) {
				var id = data.data[0].variable.value;
				Deal.get({dealId : id}, function(data) {
					$location.path('/deals/' + data.reference);
				});
				
			});
		}
		
		$scope.resolveView = function(task) {
			var status = WorkflowService.readVariable(task, 'status', function() {
				if (status && (status.value === 'new' || status.value === 'application' || status.value === 'case' || status.value === 'amend')) {
					WorkflowService.readVariables(task, function(variables) {
						var _dealId = WorkflowService.selectVariable(variables, 'dealId');
						if (_dealId) { 
							$location.path( "/deal/task/" + task.id );
						} else if (status.value === 'application') { //a case that hasn't been converted to a deal
							$location.path( "/case/tasks/" + task.id).search({processToDeal: 'true'});
						} else {
							$location.path( "/case/tasks/" + task.id );						
						}
					});
				} else if (status && status.value === 'review') {
					WorkflowService.readVariables(task, function() {
						$location.path("/maker-checker/task/" + task.id);
					});
				} else {
					$location.path( "/tasks/" + task.id );
				}
			});
			
		};
		
		$scope.range = function(min, max, step) {
		    step = step || 1;
		    var input = [];
		    for (var i = min; i <= max; i += step) {
		        input.push(i);
		    }
		    return input;
		};
		
		$scope.startProcess = function() {
			WorkflowService.startProcess();
			$scope.showTab($scope.selectedTab, $scope.selectedTab.pageIndex);
		}
		
		$scope.startProcessSwift = function() {
			WorkflowService.startProcessSwift();
			$scope.showTab($scope.selectedTab, $scope.selectedTab.pageIndex);
		}
		
		$scope.selectedTabTotal = function(){
			if ($scope.totalItems != $scope.selectedTab.total){
				console.log("$scope.totalItems:"+$scope.totalItems);
				console.log("$scope.selectedTab.total:"+$scope.selectedTab.total);
			}
			return $scope.selectedTab.total;
		}
		

		$scope.searchTaskCustomers = function(task){
			//JHP - Need to implement Customer on Case search
			return false;
		}
		
		$scope.getTaskRef = function(task){
			var ref = task.processInstanceId;
			if (task.id != task.processInstanceId){
				ref = ref + ":" + task.id;
			}
			return ref;
		}
		
		$scope.loadTasks = function(status,index,searchFilter){
			var result = WorkflowService.listTasks(status, $scope.numPerPage, (index - 1) * $scope.numPerPage,searchFilter, function() {
				$scope.tasks = result.data;
				$scope.selectedTab.pageIndex = index;
				$scope.selectedTab.total = result.total;
				
				$scope.filteredTasks = angular.copy($scope.tasks);
			});
			return result.$promise;
		}
		
		$scope.filteredTasks = [];
		$scope.searchFilter = '';
		$scope.maxSize = 10;
		$scope.currentPage = 1;
		$scope.numPerPage = Math.floor((window.innerHeight-340)/46);
		
		$scope.paginate = function (value) {
			var begin, end, index;
			begin = ($scope.currentPage - 1) * $scope.numPerPage;
			end = begin + $scope.numPerPage;
			index = $scope.filteredTasks.indexOf(value);
			return (begin <= index && index < end);
		};
			  
		$scope.filter = function(){
//			if ($scope.searchFilter.length >= 3)
				$scope.showTab($scope.selectedTab, 1);
		}
	}
	
	function TaskMainController($scope, $location, $sce, $routeParams, ActivitiService, WorkflowService) {
		ActivitiService.runtimeTasks.get({taskId : $routeParams.id}, function(data) {
			$scope.task = data;

			ActivitiService.taskAttachments.query({taskId : $routeParams.id}, function(att) {
				$scope.attachments = att;
				
				ActivitiService.processAttachments.query({processId : $scope.task.processInstanceId}, function(procAtt) {
					$scope.attachments = procAtt;
				});
			});
		});
		
		ActivitiService.formTasks.query({taskId : $routeParams.id}, function(data) {
			$scope.form = data;
		});
		
		$scope.viewAttachment = function(attachment) {
			$scope.selectedAttachment = attachment;
		};
		
		$scope.startCase = function(task) {
			WorkflowService.complete(task, "application");
			$location.path("/tasks");
		};
		
		$scope.sendToQuery = function(task) {
			WorkflowService.complete(task, "query");
			$location.path("/tasks");
		};
	};	
	
})();
