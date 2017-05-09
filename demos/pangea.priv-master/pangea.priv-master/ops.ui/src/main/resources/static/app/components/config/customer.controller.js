var customerControllers = angular.module('CustomerMasterControllers',['ui.bootstrap', 'ngSanitize','xeditable','ngRoute','ngMessages','LocalStorageModule','CustomerMasterServices']);

customerControllers.controller('CustomerMasterControllers', ['$scope','CustomerMasterFactory',
                                                       function($scope,CustomerMasterFactory){
	$scope.customers = [];
	
	$scope.success = null;
    $scope.activeCrumb = {};
    $scope.data = {};
    $scope.data.activeCustomer = {};
    $scope.tabs = [
             	{"heading": "Customers", "default":true , "uri": "/app/components/config/partials/customer/customers.html"},
             	{"heading": "Customer", "uri": "/app/components/config/partials/customer/customer.html"},
             	{"heading": "Address", "uri": "/app/components/config/partials/customer/address.html"},
             	{"heading": "Contact", "uri": "/app/components/config/partials/customer/contact.html"},
             	{"heading": "Account", "uri": "/app/components/config/partials/customer/account.html"}
            ];
    
    
    $scope.init = function(){
        var promise = CustomerMasterFactory.customers();

        promise.$promise
            .then(function(response){
                $scope.data.customers = response;
                if(!$scope.activeCrumb.uri){
                    $scope.activeCrumb = $scope.tabs[0];
                    $scope.activeCrumb.selected = true;
                }
            })
            .catch(function(response){

            });

    }

    $scope.selectTabClass = function(tab){

    	if(tab.selected ){
            return  "active"
        }
        if(!tab.disabled){
            return  "disabled"
        }

    }

    $scope.getCrumbContent = function() {
        var resultUri;
        if (isFunction($scope.activeCrumb.uri)) {
            resultUri = $scope.activeCrumb.uri();
        } else {
            resultUri = $scope.activeCrumb.uri;
        }
        return resultUri;
    }

    $scope.selectCrumb = function(tab){
        return;
    }

    $scope.switchTab = function(index){
        $scope.activeCrumb.selected = false;
        if (!$scope.activeCrumb.default){
            $scope.activeCrumb.disabled = true;
        }
        $scope.activeCrumb = $scope.tabs[index];
        $scope.activeCrumb.disabled = false;
        $scope.activeCrumb.selected = true;
    }

    $scope.editCustomer = function(customer){
        $scope.data.activeCustomer = customer;
        $scope.data.backupCustomer = angular.copy(customer);
        $scope.switchTab(1);
    }

    $scope.cancelEditCustomer = function(){
        angular.copy($scope.data.backupCustomer,$scope.data.activeCustomer);
        $scope.switchTab(0);
    }

    $scope.saveCustomer = function(){
        isNew = !$scope.data.activeCustomer.id;
    	CustomerMasterFactory.savecustomer($scope.data.activeCustomer).$promise.then(function(result){
           $scope.data.activeCustomer = result;
          
        });
    	$scope.switchTab(0);
    	if(isNew)$scope.data.customers.push($scope.data.activeCustomer);
    }

    $scope.addCustomer = function(){
        $scope.data.activeCustomer = {};
        $scope.switchTab(1);
    }

    $scope.init();

    $scope.status = {
    		openBirthDate: false
    };
    $scope.openBirthDate = function(event){
    	$scope.status.openBirthDate = true;
    }
    
    $scope.editAddress = function(address){
        $scope.data.backupAddress = angular.copy(address);
    	$scope.data.activeCustomer.activeAddress = address;
        $scope.switchTab(2);
    }

    $scope.cancelEditAddress = function(){
        angular.copy($scope.data.backupAddress,$scope.data.activeCustomer.activeAddress);
        $scope.switchTab(1);
    }

    $scope.saveAddress = function(){
    	if(!$scope.data.activeCustomer.activeAddress.id){
    		$scope.data.activeCustomer.activeAddress.customer = {id:$scope.data.activeCustomer.id};
    		$scope.data.activeCustomer.addresses.push({id:null,customer:$scope.data.activeCustomer.id,address:$scope.data.activeCustomer.activeAddress});
    	}
    	CustomerMasterFactory.savecustomer($scope.data.activeCustomer).$promise.then(function(result){
           $scope.data.activeCustomer = result;
           
           $scope.data.backupCustomer = angular.copy(result);
           $scope.switchTab(1);
           
        });
    	
    	
    }
    
    $scope.addAddress = function(){
        $scope.data.activeCustomer.activeAddress = {};
        $scope.switchTab(2);
    }
    
    $scope.editContact = function(contact){
    	$scope.data.backupContact = angular.copy(contact);
        $scope.data.activeCustomer.activeContact = contact;
        $scope.switchTab(3);
    }

    $scope.cancelEditContact = function(){
        angular.copy($scope.data.backupContact, $scope.data.activeCustomer.activeContact);
    	$scope.switchTab(1);
    }

    $scope.saveContact = function(){
    	if(!$scope.data.activeCustomer.activeContact.id){
    		$scope.data.activeCustomer.activeContact.customer = {id:$scope.data.activeCustomer.id};
    		$scope.data.activeCustomer.contacts.push($scope.data.activeCustomer.activeContact);
    	}
    	CustomerMasterFactory.savecustomer($scope.data.activeCustomer).$promise.then(function(result){
           $scope.data.activeCustomer = result;
           $scope.data.backupCustomer = angular.copy(result);
          
        });
    	$scope.switchTab(1);
    	
    }
    
    $scope.addContact = function(){
        $scope.data.activeCustomer.activeContact = {};
        $scope.switchTab(3);
    }
	
    $scope.editAccount = function(account){
    	$scope.data.backupAccount = angular.copy(account);
        $scope.data.activeCustomer.activeAccount = account;
        $scope.data.activeCustomer.activeAccount.bank = $scope.findOne($scope.allBanks,$scope.data.backupAccount.bank);
        $scope.data.activeCustomer.activeAccount.branch = $scope.findOne($scope.allBranches,$scope.data.backupAccount.branch); 
        $scope.switchTab(4);
    }

    $scope.cancelEditAccount = function(){
        angular.copy($scope.data.backupAccount, $scope.data.activeCustomer.activeAccount);
    	$scope.switchTab(1);
    }

    $scope.saveAccount = function(){
    	if(!$scope.data.activeCustomer.activeAccount.id){
    		$scope.data.activeCustomer.activeAccount.customer = {id:$scope.data.activeCustomer.id};
    		$scope.data.activeCustomer.customerAccounts.push($scope.data.activeCustomer.activeAccount);
    	}
    	if($scope.data.activeCustomer.activeAccount.branch)$scope.data.activeCustomer.activeAccount.branch = angular.copy($scope.data.activeCustomer.activeAccount.branch.id);
    	if($scope.data.activeCustomer.activeAccount.bank)$scope.data.activeCustomer.activeAccount.bank = angular.copy($scope.data.activeCustomer.activeAccount.bank.id);
    	
    	CustomerMasterFactory.savecustomer($scope.data.activeCustomer).$promise.then(function(result){
           $scope.data.activeCustomer = result;
           $scope.data.backupCustomer = angular.copy(result);
           
        });
    	$scope.switchTab(1);
    	
    }
    
    $scope.addAccount = function(){
        $scope.data.activeCustomer.activeAccount = {};
        $scope.switchTab(4);
    }
 }]);
