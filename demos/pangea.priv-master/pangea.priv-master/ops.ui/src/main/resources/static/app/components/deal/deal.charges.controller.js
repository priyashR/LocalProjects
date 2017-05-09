(function(){
    'use strict';

    angular
        .module('pangea')
        .controller('DealChargesController',DealChargesController);

    DealChargesController.$inject = ['$scope','$rootScope','$sce','$filter','$routeParams','$window','$uibModal','$route','localStorageService','DealLookupService','DealChargesServiceFactory','lookupDataService'];

    function DealChargesController($scope,$rootScope,$sce,$filter, $routeParams,$window,$uibModal,$route,localStorageService,DealLookupService,DealChargesServiceFactory,lookupDataService) {

        $scope.selectedCalculationMethodObj = {};

        $scope.selectTabClass = function(tab){
            if(tab.default ){
                return  "active"
            }
            if(!tab.disabled){
                return  "disabled"
            }
        }

        $scope.saveAllDealCharges = function(){
            return $scope.saveDealChargesWithSettlement($scope.deal.dealCharge,false,true);
        }

        $scope.selectChargesContent = function(crumb) {
                //TODO - JHP - To be removed  - Save charges for now
                if (crumb.name === "charge-settlement") {
                    $scope.saveAllDealCharges().then(function (data) {
                        $scope.setContent(crumb);
                    });
                }else{
                    $scope.setContent(crumb);
                }
        };

        $scope.setContent = function(crumb){
            $scope.activeChargesCrumb = crumb;
            $scope.getChargesContent()
            $scope.removeActiveTab($scope.deals.charge.tabs);
            crumb.disabled = true;
            crumb.default  = true;
        }

        $scope.getChargesContent = function() {

            var resultUri;
            if (isFunction($scope.activeChargesCrumb.uri)) {
                resultUri = $scope.activeChargesCrumb.uri();
            } else {
                resultUri = $scope.activeChargesCrumb.uri;
            }

            if(resultUri === undefined){
                //$(".case-content-box ul li").removeClass('active')
                resultUri = $scope.deals.charge.tabs[0].uri
            }

            return resultUri;
        }

        $scope.chargeTemplates = [
            {name: 'Optional Charges', url: 'app/components/charges/partials/add-charges/deal-charges-optional.html'},
            {name: 'Customer Specific Charges', url: 'app/components/charges/partials/add-charges/deal-customer-specific-charge.html'}
        ];

        $scope.initDealChargeRefData = function(){
            $scope.allCustomerAccounts = DealLookupService.allCustomerAccount($scope.deal.customerId); //TODO-JHP Needs to move to Customer Service
            $scope.allOptionalCharges = DealChargesServiceFactory.getAllOptionalDebitCredits($scope.deal.id);
            $scope.allCalculationMethods = DealChargesServiceFactory.getCalculationMethods($scope.deal.id);
            $scope.allChargeTypes = lookupDataService.allChargeTypes();
            $scope.allPeriods = lookupDataService.allPeriods();
            $scope.allBaseCodes =  lookupDataService.allBaseCodes();
            $scope.allRecoveryTypes = lookupDataService.allRecoveryTypes();
            $scope.allPostingClassificationCodes = DealChargesServiceFactory.allPostingClassificationCode();
            $scope.allSettlementCurrencyTypes = DealChargesServiceFactory.allSettlementCurrencyTypes();
        }

        $scope.resetControlerVars = function(){
            $scope.deal.selectedCharge = {};
            $scope.newDealCharge = {};
            $scope.deal.selectedSettlement = {};
            $scope.deal.selectedPosting = {};
            $scope.chargeTypeTemplate = $scope.chargeTemplates[0];
        }

        $scope.initDealCharges = function(){

            $scope.resetControlerVars();

            if ($scope.deal.principalAmount && $scope.deal.principalCurrencyCode){
                //TODO - JHP - Change to GET rather than CALCULATE
                $scope.deal.dealCharge = DealChargesServiceFactory.getDealCharges($scope.deal.id);
                //$scope.deal.dealCharge = DealChargesServiceFactory.getChargeDrCr($scope.deal.id);
            }

        }

        $scope.initSettlements = function(){
            $scope.settlementPartyCodes  = DealChargesServiceFactory.getAllSettlementPartyCodes($scope.deal.id);
            //TODO - JHP - Get befor save - REST 2 align required
            //$scope.deal.dealCharge = DealChargesServiceFactory.getSettlementCharges($scope.deal.id);
            $scope.deal.dealCharge = DealChargesServiceFactory.saveSettlementCharges($scope.deal.id,$scope.deal.dealCharge);
        }

        $scope.initFinancialPosting = function(){
            $scope.deal.financialPosting  = DealChargesServiceFactory.getAllDealFinancialPosting($scope.deal.id);
        }

        $scope.initDealChargesController = function(){

            $scope.initDealChargeRefData();
            $scope.initDealCharges();

        }

        $scope.removeActiveTab = function(tabs){
            angular.forEach(tabs, function(value) {
                value.default = false
            });
        }

        $scope.selectedCharge = function(selectedCharge){
            $scope.optionalCharge  = selectedCharge;
        }

        $scope.saveOptionalDealCharge = function(optionalCharge){
            var optionalCharges = [];
            optionalCharges[0] = optionalCharge;
            $scope.saveDealCharge(optionalCharges,true);

            $scope.initDealChargesController();
            return true;
        }

        //TODO - JHP Should be seperated as per REST 2 compl
        $scope.saveDealChargesWithSettlement = function(dealCharges,isOverride,initialise){
            $rootScope.block = true;
            return DealChargesServiceFactory.saveDealCharges($scope.deal.id,isOverride,dealCharges).$promise.then(function(data){
                if(initialise){
                    $scope.deal.dealCharge = data;
                }
            });
        }

        $scope.saveDealCharge = function(dealCharge,isOverride){
            var dealChargeResult = DealChargesServiceFactory.saveDealCharge($scope.deal.id,isOverride,dealCharge).$promise.then(function(data){
                angular.copy(data,dealCharge);
            });
            return dealChargeResult;
        }

        $scope.editCharge = function(charge){
            $scope.deal.selectedCharge = null;
            //JHP - Create a backup copy of the charge
            $scope.deal.selectedCharge = angular.copy(charge);
        }

        $scope.resetEditCharge = function(charge){
            //JHP - reset from backup copy of the charge
            angular.copy($scope.deal.selectedCharge,charge);
            $scope.deal.selectedCharge = {};
            $scope.newDealCharge = {};
            charge.mode = null;
        }

        $scope.chargeTypeTemplate = $scope.chargeTemplates[0];
        $scope.swapTemplate = function(chargeType){
            if(chargeType === 'optional'){
                $scope.chargeTypeTemplate = $scope.chargeTemplates[0];
            }

            if(chargeType === 'customer-specific'){
                $scope.chargeTypeTemplate = $scope.chargeTemplates[1];
            }

            return $scope.chargeTypeTemplate;
        }

        $scope.initDealChargeStructure = function(){
            $scope.chargeStructure = {};
            $scope.chargeStructure.effectiveDate = new Date();
        }

        $scope.selectedDealCalculationMethod = function(selectedCalculationMethod){
            if(selectedCalculationMethod != null){
                $scope.calculationMethod = selectedCalculationMethod;
                $scope.calculationMethod.effectiveDate = new Date();
            }
        }

        $scope.saveDealCalculationMethod = function(calculationMethod){

            calculationMethod.customerId = $scope.customer.id;
            calculationMethod.expiryDate = '2066-06-06'; //TODO - JHP: Need to correct this on server side

            var result = DealChargesServiceFactory.saveCalculationMethod($scope.deal.id,calculationMethod)

            result.$promise.then(function(data){
                $scope.initDealChargesController();
            });

        }

        $scope.editSettlement = function(settlement){
            $scope.deal.selectedSettlement = {};
            $scope.deal.selectedSettlement = angular.copy(settlement);

        }

        $scope.saveSettlement = function(dealChargeSettlement){
            var dealChargeSettlementResult = DealChargesServiceFactory.saveDealCharge($scope.deal.id,false,dealChargeSettlement);

            dealChargeSettlementResult.$promise.then(function(data){
                angular.copy(data,dealChargeSettlement);
            });

            return dealChargeSettlement;
        }

        $scope.resetEditSettlement = function(dealChargeSettlement){
            angular.copy($scope.deal.selectedSettlement,dealChargeSettlement);
            $scope.deal.selectedSettlement = {};
        }

        $scope.debitRateIsEditable = function(settlement){
            return (!(settlement.debitRateCodeCode === 'SPOT_RATE' || settlement.debitRateCodeCode === 'Spot Rate'));
        }

        $scope.creditRateIsEditable = function(settlement){
            return (!(settlement.creditRateCodeCode === 'SPOT_RATE'|| settlement.debitRateCodeCode === 'Spot Rate'));
        }

        $scope.isCreditPartyIBA = function(settlement){
            return settlement.creditPostingClassification === 'IBA';
        }

        $scope.editAccPosting = function(posting){
            $scope.deal.selectedPosting = {};
            $scope.deal.selectedPosting = angular.copy(posting);
        }

        $scope.saveAccPosting = function(dealChargePosting){
            De,alCharge.post({"id":$scope.deal.id,"isOverride":false},[dealChargePosting]).$promise.then(function(data){
                $scope.resetEditCharge(dealChargePosting);
            });

        }

        $scope.resetEditAccPosting = function(posting){
            angular.copy(posting,$scope.deal.selectedPosting);
            $scope.deal.selectedPosting = {};
        }

        $scope.openAddChargesModal =function(){
            var modalInstance = $uibModal.open({
                animation: $scope.animationsEnabled,
                templateUrl: '/app/components/charges/partials/add-charges/deal-add-charge-main.html',
                scope: $scope,
                controller: function($uibModalInstance, $scope){

                    $scope.radioModel = 'Optional';

                    $scope.okModal = function () {
                        $uibModalInstance.close();
                    };

                    $scope.cancelModal = function () {
                        $uibModalInstance.dismiss('cancel');
                    };
                },
                resolve: {
                    items: function () {
                        return $scope.modalitems;
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
            });
        }


        /*
         GROUPED TEMPLATE LINKS
         */
        $scope.getChargeTableTemplate = function(charge){
            if(charge.description === "New Charge" || charge.mode === "Add Mode"){
                return 'add-charge';
            }else if(charge.id === $scope.deal.selectedCharge.id){
                return '/app/components/deal/partials/deal-default-charge-edit.html';
            }else{
                return '/app/components/deal/partials/deal-default-charge-display.html';
            }
        }

        $scope.getSettlementChargeTableTemplate = function(settlement){
            var urlpath = "app/components/deal/partials/charges/settlement/"
            if(settlement.id === $scope.deal.selectedSettlement.id){
                return urlpath + 'deal-charge-settlement-edit.html';
            }else{
                return urlpath + 'deal-charge-settlement-display.html';
            }
        }

        $scope.getArrearChargeTableTemplate = function(settlement){
            var urlpath = "app/components/deal/partials/charges/arrear/"
            if(settlement.id === $scope.deal.selectedSettlement.id){
                return urlpath + 'deal-charge-settlement-edit.html';
            }else{
                return urlpath + 'deal-charge-settlement-display.html';
            }
        }

        $scope.getPostingChargeTableTemplate = function(settlement){
            var urlpath = "app/components/deal/partials/charges/postings/"
            if(settlement.id === $scope.deal.selectedPosting.id){
                return urlpath + 'deal-charge-posting-edit.html';
            }else{
                return urlpath + 'deal-charge-posting-display.html';
            }
        }


        /*THIS IS WHERE IT ALL STARTS HAPPENING*/
        $scope.initDealChargesController();



    }


})();