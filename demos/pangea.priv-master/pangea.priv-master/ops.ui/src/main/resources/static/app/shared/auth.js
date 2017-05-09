var authModule = angular.module('authModuleService', ['ngResource','ngCookies']);

var auth = {};
var blocklocallogin = false;
var configData;

function logout(){
	if(auth.developerTesting && auth.developerTesting.toLowerCase() == "true"){
		authenticated = false;
		auth.loggedIn = authenticated;
		auth.profile = {};
		window.location.reload();
	}else{
		var options = {};
		options.redirectUri = window.location.reload();
		auth.logoutUrl = auth.authz.createLogoutUrl(options);// + "/realms/" + auth.authz.realm + "/protocol/openid-connect/logout?redirect_uri=http://localhost:8080";
		window.location = auth.logoutUrl;
	}
}

authModule.factory("Authentication",function($http,$cookieStore){

return {

    logout: function(){
        $cookieStore.remove('profile');
        logout();
    },

    login: function(post){
        try{
        	if(auth.developerTesting && auth.developerTesting.toLowerCase() == "true"){ 
        		if(post.username && post.password){
					auth.profile.username= post.username.toLowerCase();
					auth.profile.authToken= 'Basic ' + btoa(post.username + ':' + post.password)
				    $http({method: 'GET', url: configData.baseServicesUrl + '/pangea',withCredentials: true}).then(function(response, status, headers, config) {
        			    authenticated = true;					
        			    auth.loggedIn = authenticated;auth.profile.username= post.username.toLowerCase();
						auth.profile.authToken= 'Basic ' + btoa(post.username.toLowerCase() + ':' + post.password)
                        $cookieStore.put('profile', auth.profile);
        		        window.location.reload();
        			},function(response, status, headers, config){
                        auth.profile={};
        			});
        		}
        	}else{
    		    auth.authz.login().success(function () {
    		        auth.loggedIn = true;
    		        auth.authz = keycloakAuth;
                    var options = {};
                    options.redirectUri = window.location.reload();
                    auth.logoutUrl = auth.authz.createLoginUrl(options);// + "/realms/" + auth.authz.realm + "/protocol/openid-connect/logout?redirect_uri=http://localhost:8080";
    		    }).error(function () {
    		    });
        	}
        }catch(err){
        	displayMessage('Login redirect failed',"Error:");
        }
    },


	initAuth: function (data) {
		configData = data;
		var deferred = $q.defer();
		try{
			if (data.developerTesting && data.developerTesting.toLowerCase() == "true"){
                auth.profile = $cookieStore.get('profile') || {};
                if(auth.profile.username){
                    auth.username = auth.profile.username;   
                    authenticated = true;
                    auth.loggedIn = authenticated;
                }
				if (auth.loggedIn){
					if (auth.username.toUpperCase()==="JOHN" || auth.username.toUpperCase()==="VERONICA"){
						auth.profile.firstName = auth.username.charAt(0).toUpperCase() + auth.username.slice(1).toLowerCase();
						auth.profile.lastName = 'Makes';
					}
					if (auth.username.toUpperCase()==="JACK" || auth.username.toUpperCase()==="BETTY"){
						auth.profile.firstName = auth.username.charAt(0).toUpperCase() + auth.username.slice(1).toLowerCase();
						auth.profile.lastName = 'Checks';
					}
				}else{
				    authenticated = false;
					auth.profile = {};
			        auth.loggedIn = authenticated;
			        auth.authz = {};
				}
				auth.developerTesting = data.developerTesting;
		     	deferred.resolve();
			}else{
				var keycloakAuth = new Keycloak(data.keycloak);
			    keycloakAuth.init({ onLoad: 'check-sso' }).success(function (authenticated) {
			        auth.loggedIn = authenticated;
			        auth.authz = keycloakAuth;
			        if(authenticated){
			        	 auth.authz.loadUserProfile().success(function(profile){
			        		auth.profile = profile;
			             	auth.username = profile.username;
			             	deferred.resolve();
			             });
			        } else {
			        	deferred.resolve();
			        }
			    }).error(function () {
			    	deferred.reject('Failed to init Authentication');
			    });
		    
			    keycloakAuth.onAuthSuccess = function() { null; };
			    keycloakAuth.onAuthRefreshSuccess = function() { null; };
			}
			
		}catch(err) {
			deferred.reject('Authentication provider seems To be missing - GO Speak To Jannie');
		}
	
	    return deferred.promise;
	    
	}};
});


authModule.factory('Auth', function() {
    return auth;
});

authModule.factory('authInterceptor', function($q, Auth) {
    return {
        request: function (config) {
            var deferred = $q.defer();
            if(Auth.profile && Auth.profile.authToken){
        		config.headers.Authorization = Auth.profile.authToken;
        		deferred.resolve(config);
        	}else if (Auth && Auth.authz && Auth.authz.token) {
                Auth.authz.updateToken(5).success(function() {
                    config.headers = config.headers || {};
                    config.headers.Authorization = 'Bearer ' + Auth.authz.token;

                    deferred.resolve(config);
                }).error(function() {
                        deferred.reject('Failed to refresh token');
                    });
                
            }else{
            	deferred.resolve(config);
            }
            return deferred.promise;
        }
    };
});

authModule.factory('errorInterceptor', function($q) {
    return {
    	'response': function(response) {
    	      // do something on success
    	      return response;
    	    },

    	    // optional method
    	   'responseError': function(response) {
                if (response.status === 401) {
                    displayMessage('Invalid session login required');
                    if(auth.loggedIn=true){
                       logout();
                    }
               } else if (response.status === 403) {
            	   displayMessage("Forbidden");
               } else if (response.status === 404) {
                   displayMessage("Not found");
               } else if (response.status === 412) {
                   
               } else if (response.status === -1) {
            	   displayMessage("Authentication failed!");
               } else if (response.status) {
                   displayMessage("An unexpected server error has occurred");
               }
               return $q.reject(response);
           }
    }
});

authModule.config(function($httpProvider) {
    $httpProvider.interceptors.push('errorInterceptor');
    $httpProvider.interceptors.push('authInterceptor');
});