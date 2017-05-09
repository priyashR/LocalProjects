(function(){
    'use strict';

    angular
        .module('pangea')
        .factory('DefaultChargeService',DefaultChargeService);

    DefaultChargeService.$inject = ['$resource','config'];

    function DefaultChargeService($resource, config) {
    	return $resource(config.apiUrl + '/defaultCharge/:defaultChargeId',  {defaultChargeId : '@defaultChargeId'}, {});
    }

})();