var config = angular.module('configService', ['ngResource']);

//TODO - JHP - The defaults should be reconsidered.
/*
 * These are the defaults
 * and are overwritten in app.js pangea.config
 */
config.constant('config', {
    appName: 'Pangea Prototype',
    appVersion: '0.1-alpha',
    buildNumber: '0.1.0',
    apiUrl: 'http://localhost:8081',
    workflowServicesUrl: 'http://localhost:9090'
});

config.factory('Config', [ '$resource', function($resource) {
	return $resource('/config', {}, {
		fetch : {
			method : 'GET',
			isArray : false
		}
	});
} ]);
