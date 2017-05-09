var customerModelApp = angular.module('customer.model.app', ['ui.bootstrap','configService', 'commonControllers','commonServices','ngSanitize']);

customerModelApp.run(['$rootScope','lookupDataService','$sce','$templateCache', function($rootScope,lookupDataService,$sce,$templateCache) {	
	$scope = $rootScope;
//	$scope.allCommunicationTypes = lookupDataService.allCommunicationTypes();
//	$scope.allCustomerTypes = lookupDataService.allCustomerTypes();
//	$scope.allIdentificationTypes = lookupDataService.allIdentificationTypes();
//	$scope.allCountries = lookupDataService.allCountries();
//	$scope.allGenderTypes = lookupDataService.allGenderTypes();
//	$scope.allIndustryTypes = lookupDataService.allIndustryTypes();
//	$scope.allSectorTypes = lookupDataService.allSectorTypes();
//	$scope.allProvinces = lookupDataService.allProvinces();
    
	$scope.selectedCustomerType = null;
	
	$scope.goBack = function() {
	    	  window.history.back();
	};
	    
	$scope.triggerSubmit = function() {
		$scope.customerForm.$setSubmitted();
	}
	
	$scope.change = function(){
		$scope.customerForm.customerName.$validate();
	}
	
	 $scope.customerContact = {"id": null, "partyId":0 , "contactType":null ,"contactPerson":null,  "mobileNumber":null , "officeNumber":null , "email":null , "alternativeEmail":null};
		
	 $scope.customerAddress = {"id": null, "partyId":0 , "addressLine1":null , "addressLine2":null , "suburb":null , "city":null , "state":null, 
							"country":null , "zipCode":null , "addressType":null
	};
		
		
	 
	 $scope.customer =  {"id":null, "CIFKey":0, "customerType":null , "name":null , "surname":null , "idType":null , 
             "idNumber":null , "passportNumber":null , "passportCountry": null , "tempResPermitNumber": null , "dateOfBirth" : null , "gender" : null , 
             
             "legalEntityName":null , "tradeName":null , "registrationNumber":null , "registrationDate":null , 
             "countryOfResidence":null , "countryOfNationality":null , "preferredCommunicationLanguage": null , "institutionalSector" : 0 , 
             "industrialClassification" : 0 , "address": [{"id":0 , "partyId":0 , "addressLine1":null , "addressLine2":null , "suburb":null , "city":null , "state":null , 
															"country":null , "zipCode":null , "addressType":"physical"
     														},
     													  {"id":0 , "partyId":0 , "addressLine1":null , "addressLine2":null , "suburb":null , "city":null , "state":null , 
     														"country":null , "zipCode":null , "addressType":"postal"
     													  }],
             "contacts": [$scope.customerContact],
             "customerStatus" : null, "kycStatus" : null , "barclaysCustomer" : false 
	 };
	 
	 $scope.CIFcustomer = {"id":null,"name":null,"surname":null,"idNumber":null,"passportNumber"
		 					:null,"tradeName":null,"registrationNumber":null,"kycStatus":null,"addressLine1":null,"addressLine2":null,"suburb":null,"city":null,
		 					"state":null,"country":null,"zipCode":null,"cifkey":null};
		 					
}]);