(function(){
    'use strict';

    angular
        .module('pangea')
        .factory('ChargeStructureService',ChargeStructureService);

    ChargeStructureService.$inject = ['$resource', 'config'];


    function ChargeStructureService($resource, config) {
        return $resource(config.apiUrl + '/chargestructure/:chargeStructureId',  {chargeStructureId : '@chargeStructureId'}, {});
    };

})();