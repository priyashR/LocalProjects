var pangea = angular.module(
	'pangea', [
	    'ngResource',
	    'ngSanitize',
	    'xeditable',
	    'ngMessages',
		'ui.bootstrap', 
		'configService', 
		'commonControllers', 
		'commonServices',
		'ngSanitize', 
		'ngRoute',
		'angularMoment',
		'DealControllers', 
		'DocumentControllers', 
		'DealReviewControllers', 
		'PartyControllers', 
		'customer.app', 
		'RecoveryControllers',
		'ChargeControllers',
		'DealChecksControllers',
		'DealFinalizationControllers',
		'authModuleService',
		'LocalStorageModule',
		'TemplateControllers',
        'TemplateServices',
		'ClauseMasterControllers',
		'ClauseMasterServices',
		'PlaceholderControllers',
		'PlaceholderServices',
		'LimitControllers',
		'LimitServices',
		'MessageTypeControllers',
		'MessageTypeServices',
		'CustomerMasterControllers',
		'CustomerMasterServices'
		]
	);

pangea.config(function (localStorageServiceProvider){
	localStorageServiceProvider
    .setPrefix('myApp')
    .setStorageType('sessionStorage')
    .setNotify(true, true)
});
pangea.run(function($rootScope,$filter, lookupDataService,DealLookupService, CaseLookupService, TemplateService, LimitService, $sce,$window, $templateCache, leftNavService, navService, navServiceTop, $route,config,$routeParams,DealService, $http, bootstrap,$location,Authentication,localStorageService,WorkflowService) {

    $rootScope.$on('$routeChangeSuccess', function() {
        //document.title = config.appName;
        $routeParams
		var title = $rootScope.APPLICATION_TITLE;
        if ($route.current && $route.current.$$route){
            title = title + " - " + $route.current.$$route.tab;
		}
        document.title =  title ;
    });

	if(bootstrap.AuthInit && bootstrap.AuthInit._error){
		displayMessage(bootstrap.AuthInit._error,"Error:");
	}
	$scope.docking  = 'col-lg-12';
	$scope.post = {};
	$rootScope.login = function(){
		Authentication.login($scope.post);
	}
	$rootScope.logout = Authentication.logout;
	$rootScope.userprofile = auth.profile;
	
	$rootScope.isLoggedIn = function(){
		return auth.loggedIn;
	}
	$rootScope.isBasicLoggedIn = function(){
		return auth.loggedIn || auth.developerTesting===undefined || auth.developerTesting.toLowerCase() == "false";
	}
	
	$rootScope.$on("$routeChangeStart", function(event, next, current) { 
		if(current){
			null;
		}
		if (!$rootScope.isLoggedIn() && !(next.originalPath === "/")){
			$location.path("/");
		}
    });
	
	$rootScope.APPLICATION_TITLE = config.appName;
	$rootScope.APPLICATION_VERSION = config.appVersion;
	$rootScope.ENVIRONMENT = config.env;
	$rootScope.BUILD_NUMBER = config.buildNumber;
	$rootScope.BUILD_ID_JENKINS = config.jenBuildId;
	$rootScope.BUILD_NUMBER_JENKINS = config.jenBuildNum;
	$rootScope.GIT_COMMIT = config.gitCommit;

	$rootScope.dateFormat = "dd-MMMM-yyyy";

	if ($rootScope.isLoggedIn()){
//		$rootScope.allCommunicationTypes = lookupDataService.allCommunicationTypes();
//		$rootScope.allCustomerTypes = lookupDataService.allCustomerTypes();
//		$rootScope.allIdentificationTypes = lookupDataService.allIdentificationTypes();
//		$rootScope.allCountries = lookupDataService.allCountries();
//		$rootScope.allGenderTypes = lookupDataService.allGenderTypes();
//		$rootScope.allIndustryTypes = lookupDataService.allIndustryTypes();
//		$rootScope.allSectorTypes = lookupDataService.allSectorTypes();
//		$rootScope.allProvinces = lookupDataService.allProvinces();
//		$rootScope.allLanguages = lookupDataService.allLanguages();
//		$rootScope.allPartyCategories = lookupDataService.allPartyCategories();
//		$rootScope.allBankTypes = lookupDataService.allBankTypes();
//		$rootScope.allStatuses = lookupDataService.allStatuses();
//		$rootScope.allPartyTypes = lookupDataService.allPartyTypes();
//		$rootScope.allClearingSystems = lookupDataService.allClearingSystems();
//		$rootScope.allClassifications = lookupDataService.allClassifications();
//		$rootScope.dealerrors = '';
		
//		$rootScope.allCommunicationTypes = lookupDataService.allCommunicationTypes();
//		$rootScope.allCustomerTypes = lookupDataService.allCustomerTypes();
//		$rootScope.allIdentificationTypes = lookupDataService.allIdentificationTypes();
//		$rootScope.allCountries = lookupDataService.allCountries();
//		$rootScope.allGenderTypes = lookupDataService.allGenderTypes();
//		$rootScope.allIndustryTypes = lookupDataService.allIndustryTypes();
//		$rootScope.allSectorTypes = lookupDataService.allSectorTypes();
//		$rootScope.allProvinces = lookupDataService.allProvinces();
//
//        $rootScope.allProductTypes = DealLookupService.allProductTypes();
        $rootScope.allProducts = DealLookupService.allProducts();
//        //JHP - MOVED TO Charges Init Section - $rootScope.allPostingClassificationCodes = DealLookupService.allPostingClassificationCode();
//        $rootScope.allProductClassificationTypes = DealLookupService.allProductClassificationTypes();
        $rootScope.allSteps = lookupDataService.allSteps();
//        $rootScope.allMessageTypes = TemplateService.messageTypes();
//        $rootScope.allDeliveryMethods = lookupDataService.allDeliveryMethods();
//        $rootScope.allSectionTypes = TemplateService.getSectionTypes();
//		$rootScope.allLimitPeriod = LimitService.limitPeriod();
//		$rootScope.allLimitStatus = LimitService.limitStatuses();
//		$rootScope.allLimitOwner = LimitService.limitOwner();
//		$rootScope.allProducts = LimitService.allProducts();
//    	$rootScope.allAddressTypes = lookupDataService.allAddressTypes();
//		$rootScope.allContactTypes = lookupDataService.allContactTypes();
//		$rootScope.allBanks = lookupDataService.allBanks();
//		$rootScope.allBranches = lookupDataService.allBranches();
//		$rootScope.allCurrencyTypes = lookupDataService.allCurrencyTypes();
//		$rootScope.allAccountTypes = lookupDataService.allAccountTypes();
//		$rootScope.allSanctionStatuses = lookupDataService.allSanctionStatuses();
//		$rootScope.allConditionFunctions = lookupDataService.allConditionFunctions();
//		$rootScope.allChargeCodes = lookupDataService.allChargeCodes();
//		$rootScope.allChargeTypes = lookupDataService.allChargeTypes();
//		$rootScope.allRecoveryTypes = lookupDataService.allRecoveryTypes();
//		$rootScope.allPeriods = lookupDataService.allPeriods();
//		$rootScope.allBaseCodes = lookupDataService.allBaseCodes();
//		$rootScope.allBands = lookupDataService.allBands();
//		$rootScope.allCalcPeriods = lookupDataService.allCalcPeriods();
//		$rootScope.allTaxes = lookupDataService.allTaxes();
//		$rootScope.allChargeStructureStatuses = lookupDataService.allChargeStructureStatuses();
//		$rootScope.allProductGroups = lookupDataService.allProductGroups();
//		$rootScope.allLimitLevel = LimitService.limitLevels();
		
//		navServiceTop.initNavigation();
//		navService.initNavigation();
		
	}
	
	leftNavService.initNavigation();

	$rootScope.isActiveTab = function(tab) {
		return $route.current.tab === tab;
	}
	
	$rootScope.isActiveNav = function(nav) {
		if ($routeParams.nav){
			return nav.name===$routeParams.nav;
		}else{
			return $rootScope.activeNav === nav;	
		}
		
	}

	$rootScope.dockCheck = false;


	$rootScope.selectNav = function(nav) {
	    if(nav.hidden && nav.hidden() || nav.disabled && nav.disabled()){
	        return;
	    }

		$rootScope.activeNav = nav;
		$routeParams.nav = undefined;
		$routeParams.dealRef = undefined;
	};
	
	$rootScope.setDocState = function(state){
		
		var DOC_STATE="DOC_STATE";
		var DOC_STATE_CLASS=DOC_STATE+"_CLASS";
		
		localStorageService.remove(DOC_STATE);
		localStorageService.remove(DOC_STATE_CLASS);
		
		localStorageService.set(DOC_STATE, state);
		
		switch(state){
		case 0: //
			localStorageService.set(DOC_STATE_CLASS, "{sidebar-left:'hidden',content:'col-xs-12',sidebar-right:'hidden'}");
			break;
		case 1:
			localStorageService.set(DOC_STATE_CLASS, "{sidebar-left:'col-xs-4',content:'col-xs-8',sidebar-right:'hidden'}");
			break;
		case 2:
			localStorageService.set(DOC_STATE_CLASS, "{sidebar-left:'col-xs-4',content:'col-xs-4',sidebar-right:'col-xs-4'}");
		}
		
	}
	
	//JHP - setting default value
	$rootScope.setDocState(0);
		
	$rootScope.getDocSize = function(panel){
		var DOC_STATE="DOC_STATE";
		var DOC_STATE_CLASS=DOC_STATE+"_CLASS";
		var panel_class="hidden";
		var doc_state_class = localStorageService.get(DOC_STATE_CLASS);
		
		if (!doc_state_class){
			if(panel==='content'){
				panel_class="col-xs-12";
			}
		}else{
			panel_class = doc_state_class[panel];
			if(!panel_class){
				if(panel==='content'){
					panel_class="col-xs-12";
				}else{
					panel_class="hidden";
				}
			}
		}
		
		return panel_class;
		
	}
	
	$rootScope.processTabs = [];
    $rootScope.processTabsPlaceHolder = [];

	$rootScope.getContent = function() {
		
		if(auth && !auth.loggedIn){
			return auth.authz.createLoginUrl();
		}
		
		if ($rootScope.processTabsPlaceHolder !== $rootScope[$route.current.tab].tabs) {
            $rootScope.processTabsPlaceHolder = $rootScope[$route.current.tab].tabs;
			$rootScope.processTabs = angular.copy($rootScope.processTabsPlaceHolder);
			if($rootScope.processTabsPlaceHolder.hideLeftNav) {
                $rootScope.processTabs.hideLeftNav = $rootScope.processTabsPlaceHolder.hideLeftNav;
            }
			$rootScope.activeNav = undefined;
		}
		
		if (!$rootScope.activeNav || $routeParams.nav) {
			
			if ($routeParams.nav){
		
				angular.forEach($rootScope.processTabs,function(data){
					if ($routeParams.nav===data.name){
						$rootScope.activeNav = data;
					}
				});
			
			}
			
			if (!$rootScope.activeNav){
				$rootScope.activeNav = $rootScope.processTabs[0];
			}
		}
		
		var resultUri;
		if (isFunction($rootScope.activeNav.uri)) {
			resultUri = $rootScope.activeNav.uri();
		} else {
			resultUri = $rootScope.activeNav.uri;
		} 
		return resultUri;
	}
	
	$rootScope.themes = themes;
	$rootScope.$on("$includeContentLoaded", function(event, templateName){
		if (templateName==="theme-switcher.html" || templateName==="menu-recursive.html"){
			initThemeSwitcher(activeTheme);
		}
	});
	
	$rootScope.initDeal = function(callback,noInitCallback){
		if($routeParams.dealRef){
			if(!$rootScope.deals.deal || ($rootScope.deals.deal.reference!==$routeParams.dealRef)){
				DealService.initDeal($routeParams.dealRef).then(function(data){
					return callback(data);
				});
				return true;
			}
		}else{
			if (noInitCallback){
				return noInitCallback();
			}
		}
		
			
		return false;
	}
	
	$rootScope.forceNav = function(nav){
		$routeParams.nav = nav;
	}
	
	$rootScope.resetDealNav = function(){
		$routeParams.nav = "deal";
        $rootScope.activeNav = undefined;
        $rootScope.processTabs = [];
        $rootScope.processTabsPlaceHolder = [];
        $rootScope.deals.deal = undefined;
	}

	 
	
	$rootScope.previewAttachment = function($event,attachment) { 
		$rootScope.dockCheck = true;
		if($event){
			$event.stopPropagation();
		}
		
		if ($rootScope.viewAttachment){
			//$rootScope.viewAttachment = undefined;
		}else{
			var promise = $rootScope.getPdf(attachment);
			attachment.Taskid = $routeParams.taskId;
			promise.success(function(uri){
				$rootScope.viewAttachment={};
				$rootScope.viewAttachment.uri =  $sce.trustAsResourceUrl(uri);	
				localStorageService.set('dockAttachment', attachment);
			});
		}
		
		if ( $scope.docking == 'col-lg-12'){
			$scope.changeLayout( true );
		};	
			//var myLayout = $rootScope.initGoldenLayout();
			$rootScope.$broadcast('reinitLayout', $rootScope);
			
			var attachment = localStorageService.get('dockAttachment');
			if(attachment && $routeParams.taskId == attachment.id ){

				attachment.url = WorkflowService.attachmentUrl(attachment);

				$rootScope.viewAttachment.uri = $sce.trustAsResourceUrl(attachment.url);

				$rootScope.previewAttachment(null,attachment);
			}

			// $scope.myLayout = myLayout ;

		}
	
	$rootScope.removeAttachment = function(){
		$scope.changeLayout( false );
		if($rootScope.viewAttachment && $rootScope.viewAttachment.uri){
			$rootScope.viewAttachment.uri = "";
			
		}
		$rootScope.dockCheck = false;
	}

	$rootScope.getAttachmentUrl = function(){
		if($rootScope.viewAttachment && $rootScope.viewAttachment.uri){
			return $rootScope.viewAttachment.uri;
		}else{
			//return "";
		}
	}


	$rootScope.initGoldenLayout = function(){
		 var AngularModuleComponent = function( container, state ) {

				var elem = angular.element($('body')); 
				var injector = elem.injector();
				var $compile = injector.get('$compile');
				var $rootScope = injector.get('$rootScope');
				  
				//create a new scope for the panel
				var $panelScope = $rootScope.$new();
				  
				//add panel and state to new scope
				$panelScope.container = container;
				$panelScope.state = state;
				  
				//get the template
				var html = $( '#' + state.templateId ).html(),
						element = container.getElement();
				  
				//apply/compile template
				element.html(html); 
				$compile(element.contents())($panelScope)
			};

			var config = {
				content:[{
					type: 'component',
					title: 'Docker',
					componentName: 'template',
					componentState: {
						module: 'pangea',
						templateId: 'dockingPanel',
						selectedUserIndex: 2
					}
					
				}]
			};
				
			myLayout = new GoldenLayout( config,  document.getElementById( 'layout-container') );			
				
			myLayout.registerComponent( 'template', AngularModuleComponent );
			myLayout.init();

			return myLayout;
	}

	if($scope.myLayout != undefined){
		$scope.myLayout.on( 'stateChanged', function(){ 
		    var state = JSON.stringify( myLayout.toConfig() );
		    localStorage.setItem( 'savedState', state );
		});
	}



	$rootScope.getAttachment = function(attachment){

		attachment = localStorageService.get('dockAttachment');
		if(attachment && $routeParams.taskId == attachment.Taskid ){

			attachment.url = WorkflowService.attachmentUrl(attachment);

			attachment.url = $sce.trustAsResourceUrl(attachment.url);
			$rootScope.previewAttachment(null,attachment);
		} else {
			$rootScope.viewAttachment = undefined
		}		
	}
	
	$rootScope.getPdf = function(attachment){
		var promise = $http.get(attachment.url.$$unwrapTrustedValue(), {
		    responseType: 'blob',
		    transformResponse: function(data) {
		        // don't forget to inject $window

		        return ($window.URL || $window.webkitURL).createObjectURL(data);
		    }
		});
		return promise;
	}

	$scope.changeLayout = function(undock){
		if( undock ){
			$scope.docking  = 'col-lg-9';
		} else {
			$scope.docking  = 'col-lg-12';
		}
		
	}
	
	$rootScope.refreshAttachments = function(task) {
		$rootScope.attachments = localStorageService.get('tastAttachments');
		var result = WorkflowService.listAttachments(task, function() {
			$rootScope.attachments = result;
			for (index in $rootScope.attachments) {
				var attachment = $rootScope.attachments[index];
				if (!attachment.url) {
					attachment.url = WorkflowService.attachmentUrl(attachment);
				}
				attachment.url = $sce.trustAsResourceUrl(attachment.url);
			}
			localStorageService.set('tastAttachments', $rootScope.attachments);
		});
	};
	
	$rootScope.selectAttachment = function(attachment) {
		$rootScope.selectedAttachment = angular.copy(attachment);
	};
	
	$rootScope.getAttachments = function(){
		return  $rootScope.attachments;
	}
	
	$rootScope.initInputChannel = function(task){
		var _form = WorkflowService.readForm(task, function() {
	
			var properties = _form.formProperties;
			var caseProperties = [];
			
			for(index in properties){
			    if (properties[index].value != 'false'){
			    	caseProperties.push(properties[index]);
			    }
			}
		
			localStorageService.set('entryChanelForm', caseProperties);
	
		});
		return _form.$promise;
		
	}

    $rootScope.arrayFilter = function(arr,criteria,returnattr,comparator){
        var result = $filter('filter')(arr, criteria,comparator);
        if (returnattr && result.length) {
           result = result[0][returnattr];
        }
        return (result.length==0?'':result);
    }
    
    $rootScope.activeCrumb = {};
    
    $rootScope.selectTabClass = function(tab){
    	if(tab.selected ){
            return  "active"
        }
        if(tab.default ){
            return  "active"
        }
        if(!tab.disabled){
            return  "disabled"
        }

    }
    
    $rootScope.status = {
    		date1: false, 
    		openDate1 : function(event){
    			$rootScope.status.date1 = true;
    		},
    		date2:false,
    		openDate2 : function(event){
    			$rootScope.status.date2 = true;
    		},
    		date3:false,
    		openDate3 : function(event){
    			$rootScope.status.date3 = true;
    		},
    		format:'yyyy-MM-dd'
    };
        
    $rootScope.getCrumbContent = function() {
        var resultUri;
        if (isFunction($rootScope.activeCrumb.uri)) {
            resultUri = $rootScope.activeCrumb.uri();
        } else {
            resultUri = $rootScope.activeCrumb.uri;
        }
        return resultUri;
    }

    $rootScope.selectCrumb = function(tab){
        return;
    }

    $rootScope.switchTab = function(tab){
    	$rootScope.activeCrumb.selected = false;
        if (!$rootScope.activeCrumb.default){
        	$rootScope.activeCrumb.disabled = true;
        }
        $rootScope.activeCrumb = tab;
        $rootScope.activeCrumb.disabled = false;
        $rootScope.activeCrumb.selected = true;
    }
    

    $rootScope.parse = function(str) {
       var args = [].slice.call(arguments, 1),
           i = 0;

       return str.replace(/%s/g, function() {
           return args[i++];
       });
    }
    
    $rootScope.findOne = function(arr,id){
		return $filter('filter')(arr, {'id':id})[0];
	}
	 $rootScope.initMenus = function(){
	    $('ul.dropdown-menu [data-toggle=dropdown]').off('click');
	    $('ul.dropdown-menu [data-toggle=dropdown]').on('click', function(event) {
            event.preventDefault();
            event.stopPropagation();
            $(this).parent().siblings().removeClass('open');
            $(this).parent().toggleClass('open');
        });
    }

    $rootScope.$on("$includeContentLoaded", function(event, templateName){
         $rootScope.initMenus();
	});

});

angular.element(document).ready(function ($rootScope, $scope) {
	initThemeSwitcher(activeTheme);
	
});


angular.element(document).ready(function ($rootScope, $scope) {
   console.log( $rootScope.fitPageholder )

});




pangea.config(function($routeProvider,$httpProvider,config, bootstrap) {
	
	var bootstrapConfig = bootstrap.Config;
	
	config.apiUrl = bootstrapConfig.baseServicesUrl;
	config.workflowServicesUrl = bootstrapConfig.baseWorkFlowUrl;
	
	// versioning 
	config.appVersion = bootstrapConfig.applicationVersion;
	config.appName = bootstrapConfig.applicationTitle;
	config.buildNumber = bootstrapConfig.buildNumber;
	config.env = bootstrapConfig.env;
	config.jenBuildId = bootstrapConfig.jenBuildId;
	config.jenBuildNum = bootstrapConfig.jenBuildNum;
	config.gitCommit = bootstrapConfig.gitCommit;

	$routeProvider

	// route for the home page
	.when('/customer', {
		templateUrl : 'app/components/config/partials/customer/main.html',
		controller : 'CustomerMasterControllers',
		tab : "customer",
	}).when('/chargeCode', {
		templateUrl : 'app/components/config/partials/chargecode/chargecode-main.html',
		controller : 'ChargeCodeController',
		tab : "chargeCode"
	}).when('/defaultCharge', {
			templateUrl : 'app/components/config/partials/main.html',
			controller : 'DefaultChargeController',
			tab : "defaultCharge"
	}).when('/chargestructure', {
		templateUrl : 'app/components/config/partials/main.html',
		controller : 'ChargeStructureController',
		tab : "chargeStructure"
	}).when('/pricing', {
		templateUrl : 'view.html',
		controller : 'emptyController',
		tab : "pricing"
	}).when('/deals', {
		templateUrl : 'view.html',
		controller : 'DealMainCtrl',
		tab : "deals"
	}).when('/deals/:dealRef', {
		templateUrl : 'view.html',
		controller : 'DealMainCtrl',
		tab : "deals",
		init:true
	}).when('/deals/:dealRef/:nav', {
		templateUrl : 'view.html',
		controller : 'DealMainCtrl',
		tab : "deals",
		init : true
	}).when('/deal/task/:taskId', {
		templateUrl : 'view.html',
		controller : 'DealMainCtrl',
		tab : "deals",
		init : true
	}).when('/deal/task/:taskId/:navi', {
        templateUrl : 'view.html',
        controller : 'DealMainCtrl',
        tab : "deals",
        init : true
    }).when('/limit', {
		templateUrl : 'app/components/config/partials/limit/limit-main.html',
		controller : 'LimitController',
		tab : "limit"
	}).when('/charges-setup', {
		templateUrl : 'app/components/charges/partials/charges-setup-main.html',
		controller : 'ChargeMainController',
		tab : "charge"
	}).when('/template', {
        templateUrl : 'app/components/config/partials/main.html',
        controller : 'TemplateController',
        tab : "template"
    }).when('/schedule', {
		templateUrl : 'app/components/recovery/partials/charge-main.html',
		controller : 'RecoveryMainCtrl',
		tab : "schedule"
	}).when('/tasks', {
		templateUrl : 'app/components/workflow/task-dashboard.html',
		controller : 'TaskDashboardController',
		tab : "tasks"
	}).when('/tasks/:id', {
		templateUrl : 'app/components/workflow/task-main.html',
		controller : 'TaskMainController',
		tab : "tasks"
	}).when('/case/', {
		templateUrl : 'view.html',
		controller : 'caseController',
		tab : "case"	
	}).when('/case/tasks/:taskId', {
		templateUrl : 'view.html',
		controller : 'caseController',
		tab : "case"	
	}).when('/maker-checker/:dealRef', {
		templateUrl : 'view.html',
		controller : 'DealReviewCtrl',
		tab : "makerchecker",
		init:true	
	}).when('/maker-checker/task/:taskId', {
		templateUrl : 'view.html',
		controller : 'DealReviewCtrl',
		tab : "makerchecker",
		init:true	
	}).when('/dealchecks/task/:taskId', {
		templateUrl : 'view.html',
		controller : 'DealChecksCtrl',
		tab : "dealchecker",
		init:true		
	}).when('/deal/final/:dealId', {
		templateUrl : 'app/components/deal/partials/deal-complete.html',
		controller : 'DealFinalCtrl',
		tab : "deal"	
	}).when('/clause-master', {
        templateUrl : 'app/components/config/partials/main.html',
        controller : 'ClauseMasterController',
        tab : "clausemaster"
    }).when('/placeholder', {
        templateUrl : 'app/components/config/partials/main.html',
        controller : 'PlaceholderController',
        tab : "placeholder"
    }).when('/messagetype', {
        templateUrl : 'app/components/config/partials/main.html',
        controller : 'MessageTypeController',
        tab : "messagetype"
    }).otherwise({
		redirectTo : '/tasks'
	});
	
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

});

pangea.filter('percentage', [ '$filter', function($filter) {
	return function(input, decimals) {
		return $filter('number')(input * 100, decimals) + '%';
	};
} ]);

pangea.filter("itemDateRangeFilter", function() {
  return function(items) {
        if(!items){
            return [];
        }
        var dealDate = new Date();
        var result = [];
        for (var i=0; i<items.length; i++){
            var startDate = new Date(items[i].start),
                endDate = new Date(items[i].end);
            if (startDate > dealDate && endDate > dealDate)  {
                result.push(items[i]);
            }
        }
        return result;
  };
});

pangea.directive('dockingPanel', function ($rootScope) {
	// Runs during compile
	return {
		restrict: 'EA', 
		link: function(scope){
			scope.reinitLayout = function(  ){
				scope.myLayout = $rootScope.initGoldenLayout();
				scope.myLayout.on( 'itemDestroyed', function( tab ){ 
			    	
					$scope.changeLayout( false );
					$scope.$apply()
				});
			}

			scope.$on('reinitLayout', function( ){
		       scope.reinitLayout( )
		    })

		   
		},
		controller: ["$scope", "$rootScope", function($scope, $rootScope) {
			
			//var myLayout = $rootScope.initGoldenLayout();
			$scope.myLayout = $rootScope.initGoldenLayout();

			$scope.myLayout.on( 'itemDestroyed', function( tab ){ 
				$scope.changeLayout( false );
				$scope.$apply()
			});

			$scope.myLayout.on( 'stateChanged', function(){ 
			    var state = JSON.stringify( myLayout.toConfig() );
			    localStorage.setItem( 'savedState', state );
			});


			$scope.myLayout.on( 'windowOpened', function(ev){ 
			    //console.log("ev")
			});

		}]
	};
});

pangea.directive('includeReplace', function () {
    return {
        require: 'ngInclude',
        link: function (scope, el, attrs) {
        	if (attrs){
        		null;
        	}
            el.replaceWith(el.children());
        }
    };
});

pangea.directive('datepickerPopup', function (){
    return {
        restrict: 'EAC',
        require: 'ngModel',
        link: function(scope, elem, attrs, ngModel) {
        	ngModel.$formatters.push(function (input) {
        		if(input == null)return null;	
                var transformedInput = new Date(input);

                if (transformedInput != input) {
                	ngModel.$setViewValue(transformedInput);
                	ngModel.$render();
                }

                return transformedInput;
            });
        	
//        	ngModel.$parsers.push(function toModel(date) {
//        		if (Object.prototype.toString.call(date) === '[object Date]') {
//        			return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
//        		}else return date;
//            });
        }
    }
});

pangea.directive('loading',   function ($http,$rootScope){
	return {
	    restrict: 'A',
	    link: function (scope, elm, attrs)
	    {
	    	if(elm || attrs){
	    		null;
	    	}
	    	var ajaxNotify;
	        scope.isLoading = function () {
	            return $http.pendingRequests.length;
	        };

	        scope.$watch(scope.isLoading, function (v)
	        {
	            if(v){
	            	if($rootScope.block && !ajaxNotify){
		            	ajaxNotify = $.notify({
		            		'icon': 'assets/images/ajax-loader.gif',
							'title': "<strong>Loading</strong> ",
							'message': 'Loading...'
						}, {
							'type': 'info',
							'placement':{'align': 'center','from': "bottom"},
							'allow_dismiss': false,
							'icon_type': 'image',
							'allow_duplicates':false,
							'delay':0,
							'offset': window.innerHeight/2
						});
	            	}
	            	$("#ajax_loader").show();
	            }else{
	            	if (ajaxNotify){
	            		$rootScope.block = undefined;
	            		ajaxNotify.close();
	            		ajaxNotify = undefined;
	            	}
	            	 $("#ajax_loader").hide();
	            }
	        });
	    }
	};
});

function isFunction(functionToCheck) {
	var getType = {};
	return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
}

deferredBootstrapper.bootstrap({
	element : document.body,
	module : 'pangea',
	injectorModules : ['configService','authModuleService'],
	resolve : {
		bootstrap : [ 'Config','Authentication', function(Config,Authentication) {
			var configPromise = Config.fetch().$promise;
			var configData;
			var authPromise = configPromise.then(function(configDatap){
				deferredBootstrapper.developerTesting = configDatap.developerTesting;
				configData = configDatap; 
				return Authentication.initAuth(configData)
			});
			var finalPromise = authPromise.then(function(authData){
				return {'Config':configData,'AuthInit':authData};
			}).catch(function(authData){
				var AuthInit = {};
				AuthInit._error = authData;
				return {'Config':configData,'AuthInit':AuthInit};
			});
			
			return finalPromise;
		}]
	},
	onError : function(error){
		if(error){
			null;
		}
	},
	continueOnError : true
});

var displayMessage = function(message,title,type){
	if (!type)type='danger';
	if (!title)title="";
	$.notify({
		'title': "<strong>"+title+"</strong> ",
		'message':message
	}, {
		'type': type,
		'placement':{'align': 'center'}
	});
}

String.prototype.splice = function(idx, rem, s) {
    return (this.slice(0, idx) + s + this.slice(idx + Math.abs(rem)));
};

var fromCurrency = function(inputValue){
	
	if (!inputValue){
		return inputValue;
	}
	
    var inputVal = inputValue.toString();
    
    //stripping commas
    inputVal = inputVal.replace(/,/g,'');

    //clearing left side zeros
    while (inputVal.charAt(0) == '0') {
        inputVal = inputVal.substr(1);
    }

    //stripping non numerics
    inputVal = inputVal.replace(/[^\d.\',']/g, '');

    return inputVal;
}

var formatCurrency = function(inputValue){
	
	if (!inputValue){
		return inputValue;
	}
	
    var inputVal = inputValue;
    
    //clean up just in case
    inputVal = fromCurrency(inputVal);

    //clearing left side zeros
    while (inputVal.charAt(0) == '0') {
       inputVal = inputVal.substr(1);
    }

    inputVal = inputVal.replace(/[^\d.\',']/g, '');

    var point = inputVal.indexOf(".");
    if (point >= 0) {
       inputVal = inputVal.slice(0, point + 3);
    }

    var decimalSplit = inputVal.split(".");
    var intPart = decimalSplit[0];
    var decPart = decimalSplit[1];

    intPart = intPart.replace(/[^\d]/g, '');
    if (intPart.length > 3) {
       var intDiv = Math.floor(intPart.length / 3);
       while (intDiv > 0) {
           var lastComma = intPart.indexOf(",");
           if (lastComma < 0) {
               lastComma = intPart.length;
           }

           if (lastComma - 3 > 0) {
               intPart = intPart.splice(lastComma - 3, 0, ",");
           }
           intDiv--;
       }
    }

    if (decPart === undefined) {
       decPart = "";
    }
    else {
       decPart = "." + decPart;
    }
    var res = intPart + decPart;
   
    return res;
};

pangea.directive('currencyInput', function() {
   return {
       restrict: 'A',
       scope: {},
       require: '?ngModel',
       link: function(scope, element, attrs, ngModelController) {
    	   
            $(element).bind('keyup', function(e) {
            	 element.val(formatCurrency(element.val()));
            });
           
	        ngModelController.$parsers.push(function(data) {
	          // convert data from view format to model format
	          return fromCurrency(data)
		    });
		    
	        ngModelController.$formatters.push(function(data) {
	          // convert data from model format to view format
	          return formatCurrency(data);
	        });

       }
   };
});

function getInputSelection(el) {
        var start = 0, end = 0, normalizedValue, range,
            textInputRange, len, endRange;

        if (typeof el.selectionStart == "number" && typeof el.selectionEnd == "number") {
            start = el.selectionStart;
            end = el.selectionEnd;
        } else {
            range = document.selection.createRange();

            if (range && range.parentElement() == el) {
                len = el.value.length;
                normalizedValue = el.value.replace(/\r\n/g, "\n");

                // Create a working TextRange that lives only in the input
                textInputRange = el.createTextRange();
                textInputRange.moveToBookmark(range.getBookmark());

                // Check if the start and end of the selection are at the very end
                // of the input, since moveStart/moveEnd doesn't return what we want
                // in those cases
                endRange = el.createTextRange();
                endRange.collapse(false);

                if (textInputRange.compareEndPoints("StartToEnd", endRange) > -1) {
                    start = end = len;
                } else {
                    start = -textInputRange.moveStart("character", -len);
                    start += normalizedValue.slice(0, start).split("\n").length - 1;

                    if (textInputRange.compareEndPoints("EndToEnd", endRange) > -1) {
                        end = len;
                    } else {
                        end = -textInputRange.moveEnd("character", -len);
                        end += normalizedValue.slice(0, end).split("\n").length - 1;
                    }
                }
            }
        }

        return {
            start: start,
            end: end
        };
    }

function replaceSelectedText(el, text) {
    var sel = getInputSelection(el), val = el.value;
    el.value = val.slice(0, sel.start) + text + val.slice(sel.end);
}

pangea.service('angularDateInterceptor', function() {
	var iso8601 = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?Z$/;

	this.response = function (response) {
		convertToDate(response.data);
		return response;
	};

	this.request = function(request) {
		convertToIso8601String(request.data);
		return request;
	};

	function isIso8601(value) {
		return angular.isString(value) && iso8601.test(value);
	}

	function convertToDate(input) {
		if (!angular.isObject(input)) {
			return input;
		}

		angular.forEach(input, function (value, key) {
			if (isIso8601(value)) {
				input[key] = new Date(value);
			} else if (angular.isObject(value)) {
				convertToDate(value);
			}
		});
	}

	function convertToIso8601String(input) {
		if(!angular.isObject(input)) {
			return input;
		}

		angular.forEach(input, function(value, key) {
			if(!String(key).startsWith("$")) {
				if (value instanceof Date) {
					input[key] = value.toISOString();
				} else if (angular.isObject(value)) {
					convertToIso8601String(value);
				}
			}
		})

	}
});

pangea.config(function($httpProvider) {
	$httpProvider.interceptors.push('angularDateInterceptor');
});



