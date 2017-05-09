(function() {
	'use strict';

	angular.module('pangea')
			.factory("Review", ReviewFactory)
			.factory("FinancialInsitution", FinancialInsitutionFactory)
			.factory("Party", PartyFactory)
			.factory("Item", ItemFactory)
			.factory("SubItem", SubItemFactory);
	
	ReviewFactory.$inject =  ['DealPartyTypeService', 'PartyService', 'FinancialInsitution', 'Party', 'Item', 'SubItem'];
	PartyFactory.$inject = ['SubItem'];
	FinancialInsitutionFactory.$inject = ['SubItem'];
	
	function ReviewFactory(DealPartyTypeService,PartyService,FinancialInsitution,Party,Item,SubItem) {
		
	    function Review(name, dealId) {
	    	this.name = name;
	    	this.dealId = dealId;
	    	this.PartyItem = null;
			this.dealPartyTypes = null;

			var _self = this;
			DealPartyTypeService.query().$promise.then(function(data) {
				var dealPartyTypesDictionary = {};
				angular.forEach(data, function(value) {
					dealPartyTypesDictionary[value.code] = value.description;
				});
				_self.dealPartyTypes = dealPartyTypesDictionary;
			});
			
	    }		
	    
    	Review.prototype.loadParties = function(handler) {
    		var self = this;
    		
    		PartyService.party.query({dealid:this.dealId}).$promise.then(function(data) {
    			self.PartyItem = new Item("Parties");
    			
		    	  angular.forEach(data, function(value) {
		    		  var party = {};
		    	      party.type = value.dealPartyTypeCode;	  
		    		  party.fullname = value.name;
		    		  
		    		  if(value.legalEntityName != null){
		    			  party.fullname = value.legalEntityName;
		    		  }
		    		  
		    		  if (value.address) {
		    	       	party.primaryaddress = value.address;
		    	      } else {
		    	    	  party.primaryaddress = {addressline1 : '', addressline2 : '', city : '', zipCode : ''};
		    	      }
		    	        
		    	      if (value.contact) {
		    	      	var contact = value.contact;
		    	      	party.primarymobile = contact.mobileNumber;
		    	       	party.primaryphone = contact.officeNumber;
		    	      }
		    	        
		    	      if (value.accounts && value.accounts.length > 0) {
		    	        var account = value.accounts[0];
		    	        party.primaryaccountno = account.accountno;
		    	      }
		    	      
		    	      party.swiftKey = value.swiftKey;
		    	      
		    	      var subItem = null;
		    	      if (party.type == '03' || party.type == '04') {
		    	    	  subItem = new FinancialInsitution(party.fullname, party.swiftKey, party.primaryaddress.addressline1, party.primaryaddress.addressline2, party.primaryaddress.city, party.primaryaddress.zipCode);
		    	      } else {
		    	    	  subItem = new Party(party.fullname, party.swiftKey, party.primaryaddress.addressline1, party.primaryaddress.addressline2, party.primaryaddress.city, party.primaryaddress.zipCode);
		    	      }
		    	      
		    	      self.PartyItem.add(subItem.toSubItem(self.dealPartyTypes[party.type], "party", "customer_information", "customer_details"));
		    	  });
		    	handler.call(self);
    		});
    	}
    	
    	Review.prototype.toItems = function() {
    		var items = new Array();
    		this.items.push(this.PartyItem);
    		return items;
    	}

    	return Review;		
	}
	
	function FinancialInsitutionFactory(SubItem) {
		
	    function FinancialInstitution(name, swiftAddress, physicalAddressLine1, physicalAddressLine2, city, zipCode) {
	    	this.name = name;
	    	this.swiftAddress = swiftAddress;
	    	this.physicalAddressLine1 = physicalAddressLine1;
	    	this.physicalAddressLine2 = physicalAddressLine2;
	    	this.city = city;
	    	this.zipCode = zipCode;
	    	
	    }

    	FinancialInstitution.prototype.toSubItem = function(subitemname,modelname,category,subcategory) {
    		var subItem = new SubItem(subitemname,modelname,category,subcategory);
    		subItem.add("Bank Name", this.name.substring(0,25));
    		subItem.add("Swift Address", this.swiftAddress);
    		subItem.add("Address", this.physicalAddressLine1);
    		subItem.add("", this.physicalAddressLine2);
    		subItem.add("", this.city);
    		subItem.add("", this.zipCode);
    		subItem.add("", "");
    		subItem.add("", "");

    		return subItem;
    	} 

    	return FinancialInstitution;
	}

	function PartyFactory(SubItem) {
	    function Party(name, addressLine1, addressLine2, city, zipCode, phone, mobile, accountNo) {
	    	this.name = name;
	    	this.addressLine1 = addressLine1;
	    	this.addressLine2 = addressLine2;
	    	this.city = city;
	    	this.zipCode = zipCode;
	    	this.phone = phone;
	    	this.mobile = mobile;
	    	this.accountNo = accountNo;
	    	
	    }
	    
    	Party.prototype.toSubItem = function(subitemname,modelname,category,subcategory) {
    		var subItem = new SubItem(subitemname,modelname,category,subcategory);
    		
    		subItem.add("Name", this.name.substring(0,25));
    		subItem.add("Address", this.addressLine1);
    		subItem.add("", this.addressLine2);
    		subItem.add("", this.city);
    		subItem.add("", this.zipCode);
    		subItem.add("Telephone", this.phone);
    		subItem.add("Cellphone", this.mobile);
    		subItem.add("Account No.", this.accountNo);
    		
    		return subItem;
    	}
    	
    	return Party;
	}
    
	function ItemFactory(SubItem) {
	    function Item(name) {
	    	this.name = name;
	    	this.subitems = [];	    	
	    }
    	Item.prototype.add = function(subItem) {
    		this.subitems.push(subItem);
    	}
    	return Item;
	}

	function SubItemFactory() {
	    function SubItem(subitemname,modelname,category,subcategory) {
	    	this.subitemname = subitemname;
	    	this.modelname = modelname;
	    	this.category = category;
	    	this.subcategory = subcategory;
	    	this.subitemsvalues = [];
	    	
	    }
		
    	SubItem.prototype.add = function(name, value) {
    		this.subitemsvalues.push({ "name" : name, "value" : value})
    	}
    	
    	return SubItem;
	}
	

})();