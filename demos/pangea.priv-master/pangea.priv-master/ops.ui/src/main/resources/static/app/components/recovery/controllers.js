var recControllers = angular.module('RecoveryControllers', ['ui.bootstrap', 'FinServices']);

recControllers.controller('RecoveryMainCtrl', ['$scope', 'FinancialRecoveryService', function($scope, FinancialRecoveryService) {
	$scope.tabs = [
	         {heading: "Charge Details", uri: "/app/components/recovery/partials/charge-info.html", open:true},
			 {heading: "Period Details", uri: "/app/components/recovery/partials/charge-period.html", open:false},
			 {heading: "Charge Schedule", uri: "/app/components/recovery/partials/charge-schedule.html", open:false}
	];
	
	FinancialRecoveryService.recoveryScheduleFuture($rootScope.deals.deal.id).$promise.then(function(data) {
		 $scope.charges = data;
		 $scope.selectedCharge = $scope.charges[0];
		 $scope.selectedPeriod = $scope.selectedCharge.period[0];
		 angular.forEach($scope.charges, function(value, key) {
			 value.selected = false;
			 angular.forEach(value.period, function (period, key) {
				 period.selected = false;
			 });
		 });
		 $scope.selectedCharge.selected = true;
		 $scope.selectedPeriod.selected = true;
	});
	
	$scope.selectCharge = function(index) {
		$scope.selectedCharge.selected = false;
		$scope.selectedCharge = $scope.charges[index];
		$scope.selectedCharge.selected = true;
		$scope.selectedPeriod.selected = false;
		$scope.selectedPeriod = $scope.selectedCharge.period[0];
		$scope.selectedPeriod.selected = true;
	}

	$scope.selectPeriod = function(period) {
		$scope.selectedPeriod.selected = false;
		$scope.selectedPeriod = period;
		$scope.selectedPeriod.selected = true;
	}

	$scope.getDueDate = function(selectedCharge,period){
	    if (selectCharge){}
	}
	
}]);




