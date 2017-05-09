(function(){
    'use strict';

    angular
        .module('pangea')
        .controller('ChargeStructureController',ChargeStructureController);

    ChargeStructureController.$inject = ['$scope','$uibModal','ChargeStructureService','CustomerMasterFactory'];

    function ChargeStructureController($scope,$uibModal,ChargeStructureService,CustomerMasterFactory){

        $scope.tabs = [
            {"heading": "Charge Structures", "default":true , "uri": "/app/components/config/partials/chargestructure/charge.structures.html","disabled":true},
            {"heading": "Charge Structure", "uri": "/app/components/config/partials/chargestructure/charge.structure.html", "disabled":false}
        ];

        $scope.data = {};
        $scope.data.activeChargeStructure = {};
        $scope.errors = {};

        $scope.init = function(){

            ChargeStructureService.query().$promise.then(function(response){
                $scope.data.chargeStructures = response;
                $scope.switchTab($scope.tabs[0]);
            });

        }

        $scope.addChargeStructure = function(){
            $scope.data.activeChargeStructure = {};
            $scope.switchTab($scope.tabs[1]);
        }

        $scope.editChargeStructure = function(chargeStructure){
            $scope.data.activeChargeStructure = chargeStructure;
            $scope.data.backupChargeStructure = angular.copy(chargeStructure);
            $scope.switchTab($scope.tabs[1]);
        }

        $scope.cancelEditChargeStructure = function(){
            angular.copy($scope.data.backupChargeStructure,$scope.data.activeChargeStructure);
            $scope.switchTab($scope.tabs[0]);
            $scope.errors = {};
        }

        $scope.saveChargeStructure = function(){
            var isNew = (!$scope.data.activeChargeStructure.id);
            ChargeStructureService.save($scope.data.activeChargeStructure).$promise.then(function(response){
                $scope.data.activeChargeStructure = response;
                $scope.switchTab($scope.tabs[0]);
                if(isNew)$scope.data.chargeStructures.push($scope.data.activeChargeStructure);
            }).catch(function(response){
                response.data.forEach(function(error){
                    switch(error.code){
                        case 'CHARGE_CODE_ACTIVE_UNIQUE_PER_TENANT':
                            $scope.errors['chargeCode']=1;
                            $scope.errors['bankId']=1;
                            displayMessage(error.message);
                            break;
                        case 'CHARGE_CODE_ACTIVE_UNIQUE_PER_CUSTOMER':
                            $scope.errors['chargeCode']=1;
                            $scope.errors['customerId']=1;
                            displayMessage(error.message);
                            break;
                        default:
                            displayMessage(error.message);
                    }
                });
            });
        }

        $scope.openModalSelect = function (textSelector) {

            var ModalSelectController = function($uibModalInstance, $scope,textSelector,customerId){

                $scope.initCustomerSelect = function(){
                    $scope.data = {};
                    $scope.selectedCustomerId = customerId;
                    CustomerMasterFactory.customers().$promise.then(function(response){
                        $scope.data.customers = response;
                    });
                }

                $scope.selectRow = function(customer){
                    $scope.selectedCustomerId = customer.id;
                };

                $scope.isSelected = function(customer){
                    return customer.id === $scope.selectedCustomerId;
                }

                $scope.okModal = function () {
                    $uibModalInstance.close($scope.selectedCustomerId);
                };

                $scope.cancelModal = function () {
                    $uibModalInstance.dismiss('cancel');
                };

            }

            var modalInstance = $uibModal.open({
                animation: $scope.animationsEnabled,
                templateUrl: 'app/components/config/partials/chargestructure/customer.select.html',
                controller: ModalSelectController,
                resolve: {
                    textSelector: function() {return textSelector;},
                    customerId: function() {return $scope.data.activeChargeStructure.customerId;}
                }
            });

            modalInstance.result.then(function (selectedCustomerId) {
                $scope.data.activeChargeStructure.customerId = selectedCustomerId;
            });

        };

        $scope.init();

    };

})();

