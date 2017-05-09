(function(){
    'use strict';

    angular
        .module('pangea')
        .controller('ChargeCodeController',ChargeCodeController);

    ChargeCodeController.$inject = ['$scope','$rootScope','ChargeCodeService','lookupDataService'];

    function ChargeCodeController($scope, $rootScope, ChargeCodeService,lookupDataService){

         $scope.tabs = [
                        {"heading": "Charge Codes", "default":true , "uri": "/app/components/config/partials/chargecode/chargecodes.html","disabled":true},
                        {"heading": "Charge Code", "uri": "/app/components/config/partials/chargecode/chargecode.html", "disabled":false}
         ];
        $scope.data = {};
        $scope.data.activeChargeCode = {};

        $scope.init = function(){
            ChargeCodeService.query().$promise.then(function(response){
                    $scope.data.chargeCodes = response;
                    $scope.switchTab($scope.tabs[0]);
            });

        }

        $scope.addChargeCode = function(){
            $scope.data.activeChargeCode = {};
            $scope.switchTab($scope.tabs[1]);
        }

        $scope.editChargeCode = function(chargeCode){
            $scope.data.activeChargeCode = chargeCode;
            $scope.data.backupChargeCode = angular.copy(chargeCode);
            $scope.switchTab($scope.tabs[1]);
        }

        $scope.cancelEditChargeCode = function(){
            angular.copy($scope.data.backupChargeCode,$scope.data.activeChargeCode);
            $scope.switchTab($scope.tabs[0]);
        }

        $scope.saveChargeCode = function(){

            var isNew = (!$scope.data.activeChargeCode.code);
            var result = ChargeCodeService.save($scope.data.activeChargeCode)

            result.$promise.then(function(response){
                $scope.data.activeChargeCode = response;
                $scope.switchTab($scope.tabs[0]);
                if(isNew)$scope.data.chargeCodes.push($scope.data.activeChargeCode);
                $rootScope.allChargeCodes = lookupDataService.allChargeCodes();
            });

        }

        $scope.init();

    };

})();
