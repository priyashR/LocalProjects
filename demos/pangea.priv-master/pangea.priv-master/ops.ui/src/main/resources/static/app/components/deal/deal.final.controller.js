var dealchecksControllers = angular.module('DealFinalizationControllers', ['ngResource','ngRoute']);


dealchecksControllers.controller('DealFinalCtrl', ['$scope','$location', '$window', '$rootScope','$routeParams','$timeout','Deal','DealLookupService',
                                                   function($scope,$location,$window, $rootScope,$routeParams,$timeout,Deal,DealLookupService) {

	$scope.init = function() {
		$timeout(function() {
			$scope.swiftStatus = 'ACK Received';
		}, 10000);        
    	  if ($routeParams.dealId) {
			Deal.get({dealId: $routeParams.dealId}).$promise.then(function (data){
				
				$rootScope.deals.deal = angular.copy(data);
                $scope.deal = data;
	            $scope.deal.financialPosting  = $scope.getFinacialPosting($scope.deal.id);
				
			});	
    	  }
		
	}
	
	$scope.getFinacialPosting = function(id){
		   DealLookupService.getFinancialPosting(id).$promise.then(function (data) {
		   $scope.deal.financialPosting = data;
		  			return data;
		  		});;
		  	}
	
}
]);