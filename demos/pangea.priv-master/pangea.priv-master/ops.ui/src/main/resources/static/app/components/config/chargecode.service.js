(function(){
    'use strict';

    angular
        .module('pangea')
        .factory('ChargeCodeService',ChargeCodeService);

    ChargeCodeService.$inject = ['$resource', 'config'];

    function ChargeCodeService($resource, config) {
        return $resource(config.apiUrl + '/chargecode/:chargeCodeId',  {chargeCodeId : '@chargeCodeId'}, {});
    };

})();