(function(){
    'use strict';

    angular
        .module('pangea')
        .controller('DefaultChargeController',DefaultChargeController);

    DefaultChargeController.$inject = ['$scope','DefaultChargeService'];

    function DefaultChargeController($scope,DefaultChargeService){

        $scope.tabs = [
            {"heading": "Default Charges", "default":true , "uri": "/app/components/config/partials/defaultcharge/default.charges.html","disabled":true},
            {"heading": "Default Charge", "uri": "/app/components/config/partials/defaultcharge/default.charge.html", "disabled":false}
        ];
        $scope.data = {};
        $scope.data.activeDefaultCharge = {};

        $scope.init = function(){
            DefaultChargeService.query().$promise.then(function(response){
                $scope.data.defaultCharges = response;
                $scope.switchTab($scope.tabs[0]);
            });
        }

        $scope.addDefaultCharge = function(){
            $scope.data.activeDefaultCharge = {};
            $scope.switchTab($scope.tabs[1]);
        }

        $scope.editDefaultCharge = function(defaultCharge){
            $scope.data.activeDefaultCharge = defaultCharge;
            $scope.data.backupDefaultCharge = angular.copy(defaultCharge);
            $scope.switchTab($scope.tabs[1]);
        }

        $scope.cancelEditDefaultCharge = function(){
            angular.copy($scope.data.backupDefaultCharge,$scope.data.activeDefaultCharge);
            $scope.switchTab($scope.tabs[0]);
        }

        $scope.saveDefaultCharge = function(){
            var isNew = (!$scope.data.activeDefaultCharge.id);
            DefaultChargeService.save($scope.data.activeDefaultCharge).$promise.then(function(response){
                $scope.data.activeDefaultCharge = response;
                $scope.switchTab($scope.tabs[0]);
                if(isNew)$scope.data.defaultCharges.push($scope.data.activeDefaultCharge);
            });
        }

        $scope.init();
    }

})();