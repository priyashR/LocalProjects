'use strict';
var customerApp = angular.module('customer.app',
		[ 'ui.bootstrap', 'configService', 'commonControllers',
				'commonServices', 'customer.model.app',
				'customer.services.app', 'ngSanitize', 'ngRoute' ]);

customerApp.controller("customerController", function($scope,$rootScope, $http,
		customerServices,searchCustomerServices, $uibModal, $log) {

	$scope.message = 'Please choose a customer type';

	$scope.processForm = function() {
		customerServices.saveCustomer($scope);
	};
	$scope.selected = {};
	$scope.query = {};
	$scope.customers = {};

	$scope.search = function() {
		searchCustomerServices.query({
			tradename : $scope.query.tradeName
		}, function(response) {
			$scope.customers = response;
			$scope.searchQuery = angular.copy($scope.query.tradeName);
			$scope.searchResult = true;
		});
	};

	$scope.resetForm = function() {
		$scope.searchForm = {};
		$scope.query.tradeName = '';
		$scope.customers = {};
		$scope.searchReasult = false;
	};
	
	$scope.select = function(customer){
		$scope.customer = customer;
	};
	
});

