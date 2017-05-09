var partyControllers = angular.module('PartyControllers', ['ui.bootstrap','PartyServices']);

partyControllers.config( function($controllerProvider){}); 

partyControllers.controller('PartyCtrl', function($filter, $http, $compile, limitToFilter, $rootScope,$scope,$uibModal, PartyService, SearchPartiesService, DealPartyTypeService, SearchCriteriaParties, $routeParams, DealSanctionService, WorkflowService) {
	
	$scope.partySteps = [
	                     {"heading":"Main Parties", "uri": "/app/components/party/partials/party.html","disabled" : true, "default": true, "active": true},
	                     {"heading":"Other", "uri": "/app/components/party/partials/other.html","disabled" : false, "default": false, "active": false},
	                     {"heading":"Sanction", "uri": "/app/components/party/partials/sanction.html","disabled" : false, "default": false, "active": false}
	                    ];
	
	$scope.rescreeningDatePopup = {opened:false};
	$scope.rescreeningDatePopup.open = function($event) {
		  $scope.rescreeningDatePopup.opened = true;
	};
	
	/** Party Tabs **/
	$scope.nextToSanctions = function( index ) {
		$scope.selectedStep.code = "05";
		$scope.viewAddParty = false;
		if($scope.allparties){
			DealSanctionService.query({dealId:$scope.deals.deal.id}).$promise.then(function(data){
				$scope.dealSanction = data;
				$scope.selectPartyContent($scope.partySteps[ index ]);
			});
		}
	}
	
	$scope.proceedToDocumentation =  function(){
		if(!$scope.dealSanction.id){
			$scope.dealSanction.deal = angular.copy($scope.deals.deal.id);
		}

		//TODO JHP - This needs to be fixed to save the dealparty, and sanctions
		// DealSanctionService.save({dealId:$scope.deals.deal.id, sanctionId:$scope.dealSanction.id},$scope.dealSanction).$promise.then(function (data) {
		// 		$scope.dealSanction = data;
        //
		// 		PartyService.escalations.query({dealid:$rootScope.deals.deal.id}).$promise.then(function(data) {
		// 			var payload = {
		// 				    "escalations": []
		// 				};
		//
		// 				if (data.length > 0) {
		// 					for (var i = 0; i < data.length; i++) {
		// 						var esc = {};
		// 						esc.name = data[i].code;
		// 						esc.due = '';
		// 						payload.escalations.push(esc);
		// 					}
		//
		// 					WorkflowService.escelations.save({"id":$scope.task.processInstanceId}, payload);
		// 				}
        //
		// 		});
		//
        //
		// 	});

        $rootScope.deals.tabs[1].disabled = function(){return false}
        $rootScope.deals.tabs[2].disabled = function(){return false}
        $routeParams.nav = 'documents';
		
	}

	
	
	$scope.selectPartyContent = function(crumb) {
        $scope.partyIndex = $scope.partySteps.indexOf(crumb);
        $scope.removeActiveTab();
        if( crumb.active == false){
        	crumb.active = true;
        }
        crumb.disabled = true;
        crumb.default  = true;
        $scope.activePartyCrumb = crumb;
        $scope.getPartyContent();
    };


    $scope.removeActiveTab = function(){
    	$.each( $scope.partySteps , function(index, val) {
    		if(val.active) {
    			val.active = false;
    		} 

    		if(val.disabled) {
    			val.disabled = false;
    		} 

    	});
    	
    }
    
    $scope.getPartyContent = function() {
        var resultUri;
        if (!$scope.activePartyCrumb) {
            $scope.activePartyCrumb = $scope.partySteps[0];
            $scope.activePartyCrumb.disabled = true;
            $scope.activePartyCrumb.default = true;
        }
        if (isFunction($scope.activePartyCrumb.uri)) {
            resultUri = $scope.activePartyCrumb.uri();
        } else {
            resultUri = $scope.activePartyCrumb.uri;
        }
        return resultUri;
    }
    
	$scope.baseRelativePath = "app/components/party/partials";

	$scope.allparties = false

    $scope.allowedTypes = [];


    $scope.resetMaxPartyTypes = function(){
         $scope.allowedTypes = [{max:99,description:'Beneficiary'},{max:99,description:'Advising Bank'}];
    }
	
	// TODO - JHP - This can be abstracted to rootScope and used across all MDM
	$scope.resolvePartyType = function(party){
		var result = $filter('filter')($rootScope.allPartyTypes, {code:party.partyTypeCode})[0];
		return result.description;

	}
	
	$scope.steps = [];
	$scope.dealparties = [];
	$scope.stepitems  = []
	$scope.beneficiaries = [];
	$scope.searchParty  			= false;
	$scope.addParty  				= false;
	$scope.addMultipleParties		= false;	
	$scope.editDealParty 			= false;
	$scope.viewParty				= false;
	$scope.viewAddParty				= false;
	$scope.nextbtn					= false;
	$scope.nextstage				= false;
	

	$scope.accordionurl 				= $scope.baseRelativePath + '/templates/accordion.html'
	$scope.getSteps = function(){
		return $scope.steps;
	}	
	
	$scope.isContact = function(party){
	if (!party) return false;
		return party.partyTypeCode === 'CONTACT';
	}
	$scope.isFI = function(party){
		if (!party) return false;
		return party.dealPartyTypeCode === '04';
	}	
	
	$scope.isOther = function(party) {
		if (!party) return false;
		return party.dealPartyTypeCode === '05';
	}
	$scope.isBeneficiary = function(party) {
		if (!party) return false;
		return party.dealPartyTypeCode === '02';
	}
	
	$scope.isAdvising = function(party) {
		if (!party) return false;
		return party.dealPartyTypeCode === '03';
	}
	$scope.isCustomer = function(party) {
		if (!party) return false;
		return party.customer === true;
	}
	
	
	
	$scope.nextStep = undefined;
	
	$scope.initStepFromPartyType = function(partyType){
		if( (partyType.code==='03') || (partyType.code==='06')) {
			return {code: partyType.code, description: partyType.description, completed : false, party : null, edit : false, add : false, search : true, showCommands : true, mode : undefined};
		}else {
			return {code: partyType.code, description: partyType.description,heading:"Add" + partyType.description, completed : false, party : null, edit : false, add : true, search : true, showCommands : true, mode : undefined};
		}	
	}	
	$scope.initStepFromPartyAndPartyType = function(party,partyType){
		// if party type is equal to Customer or addvin
		if ((party.dealPartyTypeCode ==='03')  || (party.dealPartyTypeCode ==='06')){
			return {code: partyType.code, description: partyType.description, completed : true, 'party' : party, edit : false, add : false, search : true, showCommands : true, mode : undefined};
		}
		else  {
			return {code: partyType.code, description: partyType.description, completed : true, 'party' : party, edit : party.customer? false : true, add : false, search : false, showCommands : true, mode : undefined, logged : false};
		}
	}

	$scope.getActiveParty = function( parties ){ 
		$.each( parties , function(index, val) {
			if( val.active ){
				$scope.activeParties = getPartyByCode( val.code ); 
				configActiveParty( parties[index] );
			}

			
		});
		
	}


	function getOthers( data ){
		$scope.otherParties 			= [];
		$scope.sanctionParties 			= [];

		if( data.length > 0){
			$.each( data , function(index, val) {
				if( val.partyTypeCode == 'OTHER' ){
					$scope.otherParties.push(val)
				}

				if( val.partyTypeCode == 'SANCTION' ){
					$scope.sanctionParties.push(val)
				}

				
			});
		}
	}


	$scope.otherSearchResults = function(val) {
		
		var param = {};
		param["partyTypeCode"]= $scope.setPartyTypeCode();
		param['legalEntityName']  = val;
		return SearchCriteriaParties.query(param).$promise.then(
			function(response){
				var results = []
				if( response.length > 0){
					$.each( response , function(index, val) {
						results.push({
							"id":val.id,
							"name":val.name,
							"tradeName":val.tradeName,
							"legalEntityName":val.legalEntityName,
							"partyTypeCode":val.partyTypeCode,
						})
					});
					return results;
				}
			}
		);
	};

	$scope.partySearchResults = function(val) {
		
		var param = {};
		param["partyTypeCode"]= $scope.setPartyTypeCode();
		param['legalEntityName']  = val;
		return SearchCriteriaParties.query(param).$promise.then(
			function(response){
				var results = []
				if( response.length > 0){
					$.each( response , function(index, val) {
						results.push({
							"id":val.id,
							"name":val.name,
							"tradeName":val.tradeName,
							"legalEntityName":val.legalEntityName,
							"partyTypeCode":val.partyTypeCode,
						})
					});
					return results;
				}
			}
		);

		/*return SearchPartiesService.query(param).$promise.then(
			function(response){
				var results = []
				if( response.length > 0){
					$.each( response , function(index, val) {
						results.push({
							"id":val.id,
							"name":val.name,
							"tradeName":val.tradeName,
							"legalEntityName":val.legalEntityName,
							"partyTypeCode":val.partyTypeCode,
						})
					});
					return results;
				}
			}
		);
		*/
	};


	$scope.searchPartyResults = function( param ){
		var results = []
		SearchCriteriaParties.query(param, function(response) {
			$scope.searchResults = response;
			$scope.searchQuery = angular.copy($scope.search);
			$scope.searchResult = true;
			if( response.length > 0){
				$.each( response , function(index, val) {
					results.push({
						"id":val.id,
						"name":val.name,
						"tradeName":val.tradeName,
						"legalEntityName":val.legalEntityName,
						"partyTypeCode":val.partyTypeCode,
					})
				});
			}
			return results;
		});		
	}


	function getActivePartyIndex( parties ){
		var curIndex
		$.each(parties, function(index, val) {
			 if( val.active == true){
			 	curIndex = index
			 	return false;
			 }
		});
		return curIndex;
	}

	$scope.selectCustomer = function($item, $model, $label){
		var param = {};
		param[$scope.search.criteria]=$item.name; 

		searchTradeCustomerServices.query(param, function(response) { 
			$scope.customers = response;
		});
	}

	$scope.selectBeneficiary = function( $item, $model, $label ){ 
		$scope.activeParties  = getPartyByCode( $item.code ); 
		$scope.getTemplate;
		$scope.viewParty = true;
	}

	$scope.addSelectParty = function( $item ){
		$scope.getselectedparty = $item;
	}

	$scope.addSelectOtherParty = function( $item, type ){
		$item.partyTypeCode = type;
		$scope.getselectedparty = $item;
	}


	angular.element(document).ready(function ($rootScope, $scope) {

	    $('body').on('click', '.trigger', function(event) {
	    	  $(this).parents('.accordionbox__trigger').next().slideToggle("slow");return false; 
	    });
		 
	});


	function configActiveParty( party ){   
		$scope.ActivePartyName = party.description;
		$scope.selectedStep = party;
		$scope.currentStep = party;
		if( party.allowSearch ){
		 	$scope.searchParty = true;
		}

		if( party.allowAdd ){
		 	$scope.addParty = true;
		}

		if( party.allowedMultiple ){
		 	$scope.addMultipleParties = true
		}

		if( party.allowEdit ){
		 	$scope.editDealParty = true
		}

		if( party.notEditable ){
		 	$scope.nextbtn = true;
		}

		if( !jQuery.isEmptyObject( $scope.activeParties ) ){
			$scope.viewParty = true;
			$scope.nextbtn = true;
		}
	}



	function getPartyByCode( codeid ) {		
		var party = [];
		$.each( $scope.parties, function(index, val) {
			 if( val.dealPartyTypeCode == codeid ){ 
			 	party.push($scope.populatedparties[index])
			 } 
		});	
		
		return party;			
	}

	
	$scope.getPopulatedParties= function( data ){
		$scope.populatedparties = data;
		getOthers( data );
	}

	$scope.GetAllParties = function( data ){
		var results = [];
		var check = [];
		if ( data != undefined ){
    		
    		$.each(data, function(index, val) { 
    			if( val.code != undefined && val.description != undefined){
    				var check = [];
	    			check['code'] = val.code;
	    			check['description'] = val.description;
	    			results.push( check );
    			}
    		});
    	}
		$scope.stepitems = results; 
	}	
	
	$scope.refreshParties = function() {
        $scope.resetMaxPartyTypes();
		$scope.searchResult = false;
		//$scope.currentStep = undefined;
		//$scope.nextStep = undefined;
		if ($routeParams.taskId) {
			var task = WorkflowService.get($routeParams.taskId, function() {
				$scope.task = task;
			});
		}

		
		PartyService.party.query({dealid:$rootScope.deals.deal.id}).$promise.then(function(data) {
			$scope.parties = data;  
			$scope.getPopulatedParties( data );
			$scope.steps = [];
			$scope.completedPercentage = 0;
			var completedSteps = 0;
			
			DealPartyTypeService.query().$promise.then( function(data) {
				$scope.globalDealPartyTypes = data;
				$scope.GetAllParties( data )
			});

			if( $scope.dealparties.length <= 0){
				PartyService.configs.query({dealid:$rootScope.deals.deal.id}).$promise.then(function(data) { 
						$scope.dealparties = [];
						$.each(data, function(index, val) {
							$scope.dealparties.push({ 
								allowAdd: val.allowAdd,
								allowEdit: val.allowEdit,
								allowView: val.allowView,
								allowSearch: val.allowSearch,
								allowDelete: val.allowDelete,
								allowedMultiple: val.allowedMultiple,
								code: val.code,
								description: val.partTypeDescription,
								partySearchLookup: val.partySearchLookup,
								partySearchLookup: val.partySearchLookup,
							 	active: false, 
							 	tab: false})
						});
						$scope.dealparties[0]['active'] = true;
						$scope.dealparties[0]['tab'] 	= true;			

					$scope.getActiveParty( $scope.dealparties );
				})
			} else {
				$scope.getActiveParty( $scope.dealparties );
			}
			
		    PartyService.productParties.query({productTypeId:$rootScope.deals.deal.productTypeCode,stepTypeCode:$rootScope.deals.deal.stepCode}).$promise.then( function(data) {
				$scope.dealPartyTypes = data;  

				angular.forEach($scope.dealPartyTypes, function(partyType, key) {
					
						var step = undefined;
						$scope.parties = $scope.parties.sort(function(a, b) {
                            return a.dealPartyTypeCode - b.dealPartyTypeCode;
                        });
                        var keepGoing = true;
						angular.forEach($scope.parties, function(party, pkey) {
							if (!step && (partyType.code === party.dealPartyTypeCode && !party.linked)) {
								party.linked = true;
								step = $scope.initStepFromPartyAndPartyType(party,partyType);
								completedSteps += 1;
						        $scope.addMore(step,partyType);
								$scope.steps.push(step);
								step = undefined;
								keepGoing = false;
							}
						});
						
						if (keepGoing && !step) {
							step = $scope.initStepFromPartyType(partyType);
							if (!step.completed && !$scope.nextStep) {
                                $scope.nextStep = step;
                            }
                            $scope.addMore(step,partyType);

						    $scope.steps.push(step);
						}
					
				});
				
				//JHP - Ensure the parties not associated with the default partyList if added
				angular.forEach($scope.parties, function(party, pkey) {
					if (!party.linked) {
						angular.forEach($scope.globalDealPartyTypes, function(partyType, key) {
							if (partyType.code === party.dealPartyTypeCode){
								party.linked = true;
								var step = $scope.initStepFromPartyAndPartyType(party,partyType);
								completedSteps += 1;
								$scope.steps.push(step);
							}
						});
					}
				});
				
				$scope.completedPercentage = (completedSteps / $scope.steps.length) * 100;
				
				
				if((completedSteps / $scope.steps.length) === 1 ){
					$scope.allparties = true;
				}
				if (!$scope.nextStep) {
					$scope.nextStep = {code: "05", description: "Other", heading: "Add More Parties", completed : false, party : null, edit : false, add : true, search : true, showCommands : true, mode : "none"};
					PartyService.init.get({dealid:$rootScope.deals.deal.id,type:'OTHER'}).$promise.then(function(data) {
						$scope.nextStep.party = data;
					});
					
					$scope.steps.push($scope.nextStep);
				}
			});
		});
	};



    $scope.addMore = function(step,partyType){
        angular.forEach($scope.allowedTypes,function(value,key){
            if(value.description === partyType.description){
                angular.forEach($scope.parties, function(party, pkey) {
                    if (partyType.code === party.dealPartyTypeCode){
                        value.max -= 1;
                    }
                });

                if(value.max > 0){
                    step.more = true;
                }

            }
        });
    };

	$scope.getHeading = function (step) {
		if (step){
			if (step.mode === "add") { 
				return "Add " + step.description;
			} else if (step.mode === "edit") {
				return "Edit " + step.description;
			} else if (step.mode === "search") {
				return "Search " + step.description;
			}
			return step.description;
		}
		return null;
	};
	
	$scope.isTypeDisabled = function(party){
		return party.id;
	}
	
	$scope.saveParty = function(){
		$scope.currentStep.party.dealPartyTypeCode = $scope.currentStep.code;
		$scope.currentStep.party.partyTypeCode = $scope.setPartyTypeCode();
		PartyService.party.save({dealid:$rootScope.deals.deal.id},$scope.currentStep.party,function(party){
		
			$scope.refreshParties();
		
		});
	}

	$scope.stepConfig = function( step ){
		var config = {}
		$.each( $scope.dealparties , function(index, val) {
			 if(val.code === step.dealPartyTypeCode || val.code === step.code){
			 	config['allowAdd'] = val.allowAdd;
			 	config['allowDelete'] = val.allowDelete;
			 	config['allowEdit'] = val.allowEdit;
			 	config['allowSearch'] = val.allowSearch;
			 	config['allowedMultiple'] = val.allowedMultiple;			 	
			 	
			 }
		});

		return config;
	}
	
	$rootScope.getTemplate = function( step, type ) {  
		var config = $scope.stepConfig( step ); 
		var template;
		if( step ){ 
			if( type == 'view' || type == undefined ){
				template = $scope.getViewTemplate(step);
			}

			if( type == 'add' && config.allowAdd){
				template = $scope.getEditTemplate(step);
			}

			if( type == 'edit' && config.allowEdit ){ 
				template = $scope.getEditTemplate(step);
				$scope.nextbtn = true;
			}
			
			return template 
		}
				
    };


    
    $scope.getViewTemplate = function(step) { 
    	$scope.step = step;
    	if ($scope.isFI(step)) {
    		return $scope.baseRelativePath + '/templates/fiView.html'; 
    	} else if ($scope.isContact(step)) {
    		return $scope.baseRelativePath + '/templates/contactView.html';
    	} else if ($scope.isCustomer(step)) {
    		return $scope.baseRelativePath + '/templates/customerView.html';
    	} else if ($scope.isBeneficiary(step)) {
    		return $scope.baseRelativePath + '/templates/beneficiaryView.html';
    	}
    	return $scope.baseRelativePath + '/templates/partyView.html';
    }
    
    $scope.getEditTemplate = function(step) { 
    	var template;
    	
    	if ($scope.isContact(step.party)) {
    		template = $scope.baseRelativePath + '/templates/contactEdit.html';
    	}
    	if (step.dealPartyTypeCode === '02' || step.code === '02') { 
    		template = $scope.baseRelativePath + '/templates/beneficiaryEdit.html';
    		//$scope.$apply()
    	}
    	if (step.dealPartyTypeCode === '03' || step.code === '03') { 
    		template = $scope.baseRelativePath + '/templates/advisingBankEdit.html';
    	}

    	if (step.dealPartyTypeCode === '05' || step.code === '05') { 
    		template = $scope.baseRelativePath + '/templates/agentOtherEdit.html';
    	}

    	if (step.dealPartyTypeCode === '07' || step.code === '07') { 
    		template = $scope.baseRelativePath + '/templates/agentOtherEdit.html';
    	}
    	
    	return template ;
    }

    $scope.toggleMode = function (mode, step) {
    	
    	if (step.mode === mode){
    		$scope.resetStep(step);
    	}else{
	    	step.mode = mode;
	    	$scope.selectedStep = step;
	    	$scope.currentStep = step;
    	}
    };

    $scope.addNewParty = function( step ){
    	$scope.viewAddParty	= true;
    	$scope.dealparty = {};
    	$scope.getNewTemplate = $scope.getTemplate( step, 'add');
    }

    $scope.addOtherParty = function( type ){
    	$scope.viewAddParty = true;
    	$scope.otherpartyType = type;
    	$scope.getNewTemplate = $scope.baseRelativePath + '/templates/other-sanctions.html';
    }
   

    $scope.selectTabClass = function( status ){   
    	if(status.active  == true){
    		return "active";
    	} 

    	if(status.tab  != true){
    		return "inactive";
    	} 
	}

	$scope.selectCrumb = function ( party ) {
		$scope.resetValues()
		
		
		$.each( $scope.dealparties , function(index, val) {
			if( val.code == party.code ){
				val.active = true;
				val.tab = true;

			} else {
				val.active = false;
				val.tab = false;

			}
		});

		$scope.getActiveParty($scope.dealparties); 

	}

	$scope.resetValues = function(){
		$scope.viewParty = false;
		$scope.viewAddParty = false;
		$scope.criteria.partyName = "";
		$scope.editDealParty = false;
		$scope.addParty  	= false;
		$scope.searchParty = false;
		$scope.nextbtn = false;
	}

	$scope.nextParty = function( parties ){ 
		$scope.resetValues();
		
		$.each( parties , function(index, val) {
			if( val.active ){ 
				var newindex = index +0; 
				$scope.dealparties[index]['active'] = false;
				$scope.dealparties[parseInt(index)+1]['active'] = true;
				$scope.dealparties[parseInt(index)+1]['tab'] = true;
				$scope.dealparties[index]['tab'] 	= true;
				$scope.getActiveParty($scope.dealparties);
				if( $scope.dealparties[parseInt(index)+2] == undefined ){
					$scope.nextstage = true;
					$scope.allparties = true;
				} else {
					$scope.nextstage = false;
					$scope.allparties = false;
				}
				return false; 
			}
		});

		
	}



    $scope.insertNew = function(aStep, index){
        var type = {code:aStep.code,description:aStep.description};
        var newStep = $scope.initStepFromPartyType(type);
        $scope.steps.splice(index+1,0,newStep);
    };
    
    $scope.resetStep = function(step){
		step.mode = undefined;
    	$scope.selectedStep = undefined;
    	$scope.currentStep = undefined;
    }
    
    $scope.editParty = function (party){
    	$scope.party = party;
    }
    
    $scope.cancel = function (){
    	if ($scope.party === party){ 
    		$scope.party = undefined;
    	}
    }
    
    $scope.removeParty = function(party){
    	PartyService.party.delete(
			{dealid:$rootScope.deals.deal.id,id:party.id},
			function(){
				$scope.parties = $scope.refreshParties();
			});
    }

    $scope.criteria = {partyName : "", partyType: "", searchBeneficiaryName:""};
    
    $scope.resetParties = function() {
    	$scope.criteria = {partyName : "", partyType: "", searchBeneficiaryName:""};
		$scope.searchResults = [];
		$scope.search.value = "";
		$scope.search.criteria = "";
		$scope.searchQuery = angular.copy($scope.criteria);
		$scope.searchResult = false;
    };
    
    $scope.setPartyTypeCode = function(){
    	if ($scope.selectedStep.code==="01"){ // applicant
			 return "FI";

		}
		if ($scope.selectedStep.code==="02"){  // Beneficiary
		    return "CORPORATE";

		}
		if ($scope.selectedStep.code==="03"){ // advising Bank 
		   return "FI";

		}
		if ($scope.selectedStep.code==="04"){  // Issuing bank
			return "FI";
		}
		if ($scope.selectedStep.code==="05"){
			return "OTHER";
		}

		if ($scope.selectedStep.code==="07"){
			return "AGENT";
		}
    }
    
	$scope.search = function() {
		$scope.criteria.partyTypeCode = $scope.setPartyTypeCode();
		SearchPartiesService.query({partyName: $scope.criteria.partyName,partyType:$scope.criteria.partyTypeCode}, function(response) {
			$scope.searchResults = response;
			$scope.searchQuery = angular.copy($scope.criteria);
			$scope.searchResult = true;
		});
	};


	$scope.partySearchFilter = function(val){
		$scope.criteria.partyTypeCode = "CORPORATE";
		 $scope.partyResults = [];
		
		var results  = []
		SearchPartiesService.query({partyName: val,partyType:$scope.criteria.partyTypeCode}, function(response) {
			//$scope.searchResults = response; 
			$.each(response, function(index, val) {
				var item 	= {};
				item.id  	= val.id;
				item.name 	= val.name;
				results.push(item);
			});
						
			//$scope.searchQuery = angular.copy($scope.criteria);
			$scope.searchResult = true;
			$scope.partyResults = results;
		});

	}

	$scope.selectParty = function($party){ 
		$scope.currentStep.party = angular.copy(party);
		$scope.searchResult = false;
		$scope.saveParty();	
	}


	
	$scope.getCurrentStep = function( step, steps ){
		
	}

	$scope.getParties = function(items, desc){
		var results = [];
		if(items != undefined){
			if(items.length > 0){
				$.each(items, function(index, val) { 
					if (val.description == desc){
						results.push(val.party)
					}
				});
			}
		}
		
		return results;
	}
	

	$scope.checkStep = function(items){  
		$scope.results = [];
		
		/*
		
		results.push( items[0] );

		for (var i=1; i < items.length; i++) {
			for ( var x = 0; x < results.length; x++){
				check.push(results[x].description);		       
			}
			if( check.indexOf(items[i].description) == -1 ){
				 results.push( items[i] );
			}
    	}*/
    	if ( $scope.globalDealPartyTypes != undefined ){
    		
    		$.each($scope.globalDealPartyTypes, function(index, val) {
    			var check = [];
    			check['code'] = val.code;
    			check['description'] = val.description;
    			results.push( check );
    			
    		});
    	}
    	
		return results;
	}
	
	
	$scope.searchParties = function() {
		var param = {};
		var partyTypeCode = "partyTypeCode";
		
		param[partyTypeCode]= $scope.setPartyTypeCode();
		param[$scope.search.criteria]=$scope.search.value;
		SearchCriteriaParties.query(param, function(response) {
			$scope.searchResults = response;
			$scope.searchQuery = angular.copy($scope.search);
			$scope.searchResult = true;
		});
	};
	
	$scope.isBeneficairyOtherparty = function(party){
        if ($scope.selectedStep.code === '02')  {
            return true;
        }
  	}
	
	$scope.isIssuingParty = function(party){
	    if ($scope.selectedStep.code === '04') {
	        return true;
	    }
	}


	$scope.openConfirmModal = function () {
	//	var parentElem = parentSelector ? angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
		var modalInstance = $uibModal.open({
			animation: true,
			ariaLabelledBy: 'modal-title',
			ariaDescribedBy: 'modal-body',
			templateUrl: $scope.baseRelativePath + '/templates/confirmDeleteModal.html',
			size: 'md',
			
			
		} );
	}


	$scope.getCountry = function( countryid ){
		var country
		$.each( $rootScope.allCountries , function(index, val) {
			if( val.code  == countryid ){
				country = val.description;
			}	
		});
		return country;
	}


	$scope.getSectorType = function( sectorid ){
		var sector; 
		$.each( $rootScope.allSectorTypes , function(index, val) {
			if( val.id  == sectorid ){
				sector = val.description;
			}	
		});
		return sector;
	}

	$scope.getIndustryType = function( industryid ){
		var industry; 
		$.each( $rootScope.allIndustryTypes , function(index, val) {
			if( val.id  == industryid ){
				sector = val.description;
			}	
		});
		return industry;
	}

	$scope.getSanctionCode = function( id ){
		var item; 
		$.each( $rootScope.allSanctionStatuses , function(index, val) {
			if( val.code  == id ){
				item = val.description;
			}	
		});
		return item;
	}

	$scope.deletePartyItem = function( itemid, $event ){
		PartyService.party.remove({dealid:$rootScope.deals.deal.id, id: itemid },$scope.currentStep.party,function(party){
			$scope.refreshParties();
		});

		$( $event.currentTarget ).parents('.activeparties').remove()
	}
	  
	
	$scope.select = function(searchResult) {
		$scope.currentStep.party = angular.copy(searchResult);
		$scope.currentStep.party.partyId = angular.copy($scope.currentStep.party.id);
		$scope.currentStep.party.id = null;
		$scope.searchResult = false;
		$scope.saveParty();		
	};


	/**NEW STUFF ADDED BY JAMES ON 31ST MAY 2016*/
	$scope.linktype = '';
	$scope.linkTitle = '';
	
	$scope.selectMode = function(type, mode){ 
		if(type !== 'undefined'){	
			$scope.linktype =	$scope.getLink(type)
		}		
		
	}

	$scope.getLink = function (type){
		if(type === 'addCustomer'  ){
			$scope.linkTitle = 'Add Corporate'
			 return $scope.baseRelativePath + '/templates/addEditCorporateView.html'; 
		}
		if(type === 'editCustomer' ){
			$scope.linkTitle = 'Edit Corporate'
			return $scope.baseRelativePath + '/templates/addEditCorporateView.html'; 
		}
		if(type === 'searchCustomer' ){
			$scope.linkTitle = 'Search Corporate'
			return $scope.baseRelativePath + '/templates/searchView.html'; 
		}
	}


	$scope.addSearchParty = function( party, event ){
		$scope.update(party);
		$(event.target).parents(".input-group").find('.form-control').val("")
	}	
	
	$scope.update = function(party) {
		
		if( $scope.otherpartyType ){
			party.partyTypeCode 	= $scope.otherpartyType;
			party.legalEntityName 	= party.name
		}
		$scope.currentStep.party = angular.copy(party);
		$scope.viewAddParty	= false;
		$scope.saveParty();		
		$scope.searchparty = "";
		
	};
	
	
	if (!$rootScope.initDeal($scope.refreshParties))$scope.refreshParties();
	
});


partyControllers.directive('partyAction', function($compile, $templateRequest){
  return {
    restrict: 'A',
    scope: {
    	dealParty: "@",
    	partyType: "@"
    },
   
    link: function(scope, element, attrs){
    	
      	element.on('click', function(event){
      		var _this = this;
      		var targetdiv =  $( this ).parents('.accordionbox__trigger').next();
	    	if( $( targetdiv ).css('display') == 'none') {
	    		targetdiv.slideToggle("slow");
	    	}

	    	scope.step = {};
	    	if( scope.partyType == 'view' ){
	    		scope.step = JSON.parse( scope.dealParty );
	    		var templatelink = scope.$root.getTemplate( scope.step, scope.partyType);
	    	} else {
	    		scope.step.party = JSON.parse( scope.dealParty );
	    		var templatelink = scope.$root.getTemplate( scope.step.party, scope.partyType);
	    	}
      		
      		
      		
      		$templateRequest( templatelink ).then(function(html){

		        var template = angular.element(html);
		      	$compile(template)(scope);
		      	var pageElement = $(_this).parents(".activeparties").find(".hook");
        	 	pageElement.empty()
			  	pageElement.append( template )        	  
		   });      		
      	})
    }
  }
});


partyControllers.directive('updateStep', function( $compile, $templateRequest ){
  return {
    scope: false,
    link: function(scope, element, attrs){ 
    	$(element).click(function(event) { 
    		var _this = this;
    		scope.$parent.$parent.update( scope.step.party );
    		var templatelink = scope.$root.getTemplate( scope.step.party, 'view');
    		$templateRequest( templatelink ).then(function(html){
    			scope.step = angular.copy( scope.step.party )
		        var template = angular.element(html);
		      	$compile(template)(scope);
		      	var pageElement = $(_this).parents(".activeparties").find(".hook");
        	 	pageElement.empty()
			  	pageElement.append( template )        	  
		   });  
    	});
    }
  }
});


partyControllers.directive('cancelAction', function(){
  return {
    scope: false,
    link: function(scope, element, attrs){ 
    	$(element).click(function(event) { 
    		scope.$parent.$parent.$parent.viewAddParty	= false;
    		scope.$apply()
    	});
    	
    }
  }
});

