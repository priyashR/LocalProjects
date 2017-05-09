var limitControllers = angular.module('LimitControllers',['ui.bootstrap', 'ngSanitize','xeditable','ngRoute','ngMessages','LocalStorageModule', 'customer.services.app']);

limitControllers.controller('LimitController', ['$scope', 'LimitService', 'searchTradeCustomerServices',
    function ($scope, LimitService, searchTradeCustomerServices) {

        $scope.data = {};
        $scope.data.update = false;
        $scope.data.limits = [];
        $scope.data.tradeLimits = [];
        $scope.data.productLimits = [];
        $scope.data.periodLimits = [];
        $scope.data.utilisations = [];
        $scope.data.activeLimit = {};
        $scope.activeCrumb = {};
        $scope.customers = searchTradeCustomerServices.query({
            tradename : ''
        });
        $scope.tabs = [
                     	{"heading": "Credit Limits", "default":true , "uri": "/app/components/config/partials/limit/limits.html"},
                     	{"heading": "Limit", "uri": "/app/components/config/partials/limit/limit-form.html"},
                     	{"heading": "Utilisation", "uri": "/app/components/config/partials/limit/limit-utilisation.html"}
                  	];
        $scope.init = function() {
                var promise = LimitService.limits();
                 promise.$promise
                            .then(function(response){
                                $scope.data.limits = response;
                                $scope.data.periodLimits = response;
                                $scope.data.productLimits = response;
                                if(!$scope.activeCrumb.uri){
                                    $scope.activeCrumb = $scope.tabs[0];
                                    $scope.activeCrumb.selected = true;
                                }
                            })
                            .catch(function(response){

                            });
        };

        $scope.selectTabClass = function(tab){

                    if(tab.selected ){
                        return  "active"
                    }
                    if(!tab.disabled){
                        return  "disabled"
                    }

        };

        $scope.getCrumbContent = function() {
                    var resultUri;
                    if (isFunction($scope.activeCrumb.uri)) {
                        resultUri = $scope.activeCrumb.uri();
                    } else {
                        resultUri = $scope.activeCrumb.uri;
                    }
                    return resultUri;
        };

        $scope.selectCrumb = function(tab){
                    return;
        };

        $scope.switchTab = function(index){
            $scope.activeCrumb.selected = false;
            $scope.activeCrumb.disabled = true;
            $scope.activeCrumb = $scope.tabs[index];
            $scope.activeCrumbIndex = index;
            $scope.activeCrumb.disabled = false;
            $scope.activeCrumb.selected = true;
        };

        $scope.editLimit = function(limit){
                $scope.data.activeLimit = limit;
                $scope.data.activeLimit.startDate = new Date(limit.startDate);
                $scope.data.activeLimit.endDate = new Date(limit.endDate);
                $scope.data.update = true;
                $scope.switchTab(1);
        };

        $scope.addLimit = function () {
            $scope.data.activeLimit = {};
            $scope.data.update = false;
            $scope.switchTab(1);
        };

        $scope.saveLimit = function () {
            LimitService.saveCreditLimit($scope.data.activeLimit).$promise.then(function(result){
                $scope.data.activeLimit = result;
                $scope.init();
                $scope.switchTab(0);
            });
        };

        $scope.cancelLimit = function () {
            $scope.data.activeLimit = {};
            $scope.init();
            $scope.switchTab(0);
        };

        $scope.getUtilisations = function (customerId) {
            LimitService.getLimitUtilisations(customerId).$promise.then(function (result) {
                $scope.data.utilisations = result;
                $scope.switchTab(2);
            })
        };
        $scope.init();
    }
]);