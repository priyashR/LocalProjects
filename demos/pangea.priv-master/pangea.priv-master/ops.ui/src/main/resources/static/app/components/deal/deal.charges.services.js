(function(){
    'use strict';

    angular
        .module('pangea')
        .factory('DealChargesServiceFactory',DealChargesServiceFactory)
        .factory('CalculateDealCharge',CalculateDealCharge)
        .factory('DealCharges',DealCharges)
        .factory('DealCharge',DealCharge)
        .factory('CalculationMethod',CalculationMethod)
        .factory('OptionalDebitCredit',OptionalDebitCredit)
        .factory('SettlementPostingCodes',SettlementPostingCodes)
        .factory('DealFinancialPosting',DealFinancialPosting)
        .factory('PostingClassificationCode',PostingClassificationCode)
        .factory('SettlementCurrencyTypes',SettlementCurrencyTypes)
        .factory('SettlementCharges',SettlementCharges);

    DealChargesServiceFactory.$inject =  [ '$resource','config','$rootScope','CalculateDealCharge','DealCharges','CalculationMethod','OptionalDebitCredit','SettlementPostingCodes','DealFinancialPosting','DealCharge','PostingClassificationCode','SettlementCurrencyTypes','SettlementCharges'];
    CalculateDealCharge.$inject = ['$resource', 'config'];
    DealCharges.$inject = ['$resource', 'config'];
    DealCharge.$inject = ['$resource', 'config'];
    CalculationMethod.$inject = ['$resource', 'config'];
    OptionalDebitCredit.$inject = ['$resource', 'config'];
    SettlementPostingCodes.$inject = ['$resource', 'config'];
    DealFinancialPosting.$inject = ['$resource', 'config'];
    PostingClassificationCode.$inject = ['$resource', 'config'];
    SettlementCurrencyTypes.$inject = ['$resource', 'config'];
    SettlementCharges.$inject = ['$resource', 'config'];

    function DealChargesServiceFactory($resource,config,$rootScope,CalculateDealCharge,DealCharges,CalculationMethod,OptionalDebitCredit,SettlementPostingCodes,DealFinancialPosting,DealCharge,PostingClassificationCode,SettlementCurrencyTypes,SettlementCharges) {
        return {
            getChargeDrCr: function(dealId){
                return CalculateDealCharge.calculate({id:dealId});
            },
            getDealCharges: function(dealId){
                return DealCharges.query({'dealId':dealId});
            },
            saveDealCharges: function(dealId,isOverride,dealCharges){
                return DealCharges.save({"dealId":dealId,"isOverride":isOverride},dealCharges);
            },
            saveDealCharge: function(dealId,isOverride,dealCharge){
                return DealCharge.save({"dealId":dealId,"isOverride":isOverride},dealCharge);
            },
            getCalculationMethods: function(dealId){
                return CalculationMethod.query({'dealId':dealId});
            },
            saveCalculationMethod: function(dealId,calculationMethod){
                return CalculationMethod.save({'dealId':dealId},calculationMethod);
            },
            getAllOptionalDebitCredits: function(dealId){
                return OptionalDebitCredit.query({'dealId':dealId});
            },
            getAllSettlementPartyCodes: function(dealId){
                return SettlementPostingCodes.query({'dealId':dealId});
            },
            getAllDealFinancialPosting: function(dealId){
                return DealFinancialPosting.save({'dealId':dealId});
            },
            allPostingClassificationCode : function(){
                return PostingClassificationCode.query();
            },
            allSettlementCurrencyTypes: function() {
                return SettlementCurrencyTypes.query();
            },
            getSettlementCharges: function(dealId){
                return SettlementCharges.query({id:dealId});
            },
            saveSettlementCharges: function(dealId,dealCharges){
                //TODO - JHP - Service to ignore dealcharges until data injection risk is assessed.
                return SettlementCharges.save({id:dealId},dealCharges);
            }
        }
    }

    function CalculateDealCharge($resource, config) {
        return $resource(config.apiUrl + '/charges/debitcredits/calculate', {id:'@id'}, {
            calculate : {
                method : 'POST',
                isArray : true,
                params: {id:'@id'}
            }
        });

    }

   function DealCharge($resource, config) {
        return $resource(config.apiUrl + '/deal/:dealId/charge', {dealId:'@dealId',isOverride:'@isOverride'}, {});
    }

    function DealCharges($resource, config) {
        return $resource(config.apiUrl + '/deal/:dealId/charges', {dealId:'@dealId'}, {
            save : {
                method: 'POST',
                isArray: true
            }
        });
    }

    function CalculationMethod($resource, config) {
        return $resource(config.apiUrl + '/deal/:dealId/calculationmethod', {id:'@dealId'}, {});
    }

    function OptionalDebitCredit($resource, config) {
        return $resource(config.apiUrl + '/deal/:dealId/charges/optional', {dealId:'@dealId'}, {});
    }

    function SettlementPostingCodes($resource, config) {
        return $resource(config.apiUrl + '/deal/:dealId/charges/partycodes', {dealId:'@dealId'}, {});
    }

    function DealFinancialPosting($resource, config) {
        return $resource(config.apiUrl + '/deal/financialPosting', {dealId:'@dealId'}, {
            save : {
                method: 'POST',
                isArray: true
            }
        });
    }

    function PostingClassificationCode($resource, config) {
        return $resource(config.apiUrl + '/postingClassificationCode', {}, {});
    }

    function SettlementCurrencyTypes($resource, config) {
        return $resource(config.apiUrl + '/settlementCurrencies', {}, {});
    }

    function SettlementCharges($resource, config) {

        return  $resource(config.apiUrl + '/deal/:id/charges/settlements',{id:'@id'}, {
            save :{
                method : 'POST',
                isArray : true
            }
        });


    } ;


})();

