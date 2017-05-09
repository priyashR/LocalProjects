var dealReviewControllers = angular.module('DealReviewControllers', ['ngAnimate', 'ui.bootstrap','ngRoute','PartyServices','DocServices']);

dealReviewControllers.controller(
    'DealReviewCtrl',
    [
      '$scope',
      '$rootScope',
      '$window', 
      '$location',
      '$routeParams',
      '$filter',
      '$sce',
      '$document',
      'FinancialRecoveryService',
      'DealLookupService',
      'Deal',
      'DealCustomer',
      'WorkflowService',
      'PartyService',
      'DocumentService',
      'DealNotesService',
      'Review',
      '$uibModal',
        'DealChargesServiceFactory',
      

   function($scope,$rootScope,  $window, $location, $routeParams, $filter, $sce, $document,  FinancialRecoveryService,DealLookupService,Deal,DealCustomer,WorkflowService,PartyService,DocumentService,DealNotesService,Review,$uibModal,DealChargesServiceFactory) {
      $scope.valueToPush         = {}



       $scope.approveamount = true
       $scope.expirydate = true
       $scope.checkapproveamount = false;
       $scope.checkexpirydate = false;
       
      $scope.verifyFinanceAmount = function(amount){

        if(parseInt(amount) === parseInt($scope.items[1].subitems[0].desc)){
           $scope.checkapproveamount = !$scope.checkapproveamount;
           $scope.items[1].subitems[0].approved = true;
        } else {
            $scope.items[1].subitems[0].approved = false;
        }
      }

      //expiry date datepicker
     
       $scope.dateOptions = {
        
        formatYear: 'yy',
        maxDate: new Date(2020, 5, 22),
        minDate: new Date(),
        startingDay: 1
      };

      $scope.format = 'yyyy/MM/dd';
      $scope.popup1 = {
        opened: false
      };

      $scope.open1 = function() {
        $scope.popup1.opened = true;
      };

      $scope.expiryDate = { value: null };
      
      $scope.verifyExpiryDate = function(expirydate){
    	  var comp = $filter('date')(expirydate, $scope.format);
    	if(comp === $scope.items[0].subitems[5].desc){
           $scope.checkexpirydate = !$scope.checkexpirydate;
           $scope.items[0].subitems[5].approved = true;
//           angular.element("#expiry_date").html(expirydate_val)
//           scope.verificationresults.push('expiry_date', true);
        } else {
//           scope.verificationresults.push('expiry_date', false);
        	$scope.items[0].subitems[5].approved = false;
        }
      }

      $scope.getItemPanelClass = function(item){
          return $scope.validateItemApproval(item) ? "panel-success" : "panel-danger";
      }

      $scope.validateItemApproval = function(value){
          if (!value.approved) {
              if (!value.subitems || value.subitems.length === 0) {
                  return false;
              } else {
                  var len = value.subitems.length;
                  for (var j = 0; j < len; j++) {
                      var subvalue = value.subitems[j];
                      if (!subvalue.approved &&
                    		  !angular.isUndefined(value.subitems[j].subitemsvalues) &&
                    		  value.subitems[j].subitemsvalues != null &&
                    		  value.subitems[j].subitemsvalues.length > 0) {
                          return false;
                      }
                  }
              }
          }
          return true;
      }
      
      $scope.submitDealReview = function(){
       
    		$scope.sanitizeItems();

    		var valid = true;
          
          for(var index = 0; index < $scope.items.length; index++) {
          	var item = $scope.items[index];
              if (!$scope.validateItemApproval(item)){
                  break;
              }
          }

          var note =   {
          	    "id": $scope.id,
          	    "dealId": $scope.deal.id,
          	    "note": angular.toJson($scope.items, true),
          	    "visible": false,
          	    "typeCode": "A"
          	  }
         	DealNotesService.save({dealId : $scope.deal.id, noteId : $scope.noteid}, note, function(data) {
         		$scope.noteid = data;
         	});

          if(valid === false){
          	WorkflowService.complete($scope.task, 'amend', function() {
                $location.path("/tasks").search({t : 'review'});
      	  });
          } else {
      		$location.path('/dealchecks/task/' + $scope.task.id);    		
          }
      }


      $scope.getPostingChargeTableTemplate = function(settlement){
        var urlpath = "app/components/deal/partials/deal-charge-posting-review.html"      
        return urlpath;
      }

      $scope.getPostingChargeTableMakerTemplate = function(settlement){
        var urlpath = "app/components/deal/partials/deal-charge-posting-maker.html"      
        return urlpath;
      }


      $scope.tabs = [
        {heading: "Charge Details", uri: "/app/components/recovery/partials/charge-info.html", open:true},
        {heading: "Period Details", uri: "/app/components/recovery/partials/charge-period.html", open:false}
        /*,
        {heading: "Charge Schedule", uri: "/app/components/recovery/partials/charge-schedule.html", open:false}*/
      ];

      $scope.getSettlementChargeTableMakerTemplate = function(settlement){
        return  "app/components/deal/partials/deal-charge-settlement-review.html"
        
      }

      $scope.openmodal = function (task) {
          $rootScope.removeAttachment();
          $scope.dealRef = $rootScope.deals.deal.reference;

          var modalInstance = $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: '/app/components/deal/partials/deal_release_modal.html',
            controller: function($uibModalInstance, $scope){
              $scope.okModal = function () {
            	  WorkflowService.complete(task, 'review', function() {
                      
                      var deal = $rootScope.deals.deal;
                      var startdate = moment(deal.issueDate,'YYYY/MM/DD');
                      var currentdate = moment();
                      var diff = startdate.diff(currentdate, 'days');
                      if (diff >= 5) {
              			var payload = {
            				    "escalations": []
            				};
              			
              			var esc = {};
						esc.name = data[i].code;
						esc.due = '';
						
              			payload.escalations.push(esc);
              			
              			WorkflowService.escelations.save({"id":$scope.task.processInstanceId}, payload);
            			                    	  
                      }
                  
                      $uibModalInstance.close();
                      $location.path('/tasks').search({t : 'release'});

            	  });
              };

              $scope.cancelModal = function () {
                $uibModalInstance.dismiss('cancel');
              };
            },
            resolve: {
              items: function () {
                return $scope.modalitems;
              }
            }
          });

          modalInstance.result.then(function (selectedItem) {
            $scope.selected = selectedItem;
          });
      };
      

      $scope.selectCharge = function(index) {
        $scope.selectedCharge.selected = false;
        $scope.selectedCharge = $scope.charges[index];
        $scope.selectedCharge.selected = true;
        $scope.selectedPeriod.selected = false;
        $scope.selectedPeriod = $scope.selectedCharge.period[0];
        $scope.selectedPeriod.selected = true;
      }

      $scope.selectPeriod = function(period) {
        $scope.selectedPeriod.selected = false;
        $scope.selectedPeriod = period;
        $scope.selectedPeriod.selected = true;
      }

      $scope.oneAtATime = true;
      $scope.status = {
        isFirstOpen: true,
        isFirstDisabled: false
      };

      $scope.getChargeReviewTableTemplate = function(charge){
        return '/app/components/deal/partials/deal-default-charge-review-display.html';
      }
       $scope.getChargeReviewTableMakerTemplate = function(charge){
        return '/app/components/deal/partials/deal-default-charge-review-maker-display.html';
      }

      $scope.editDeal =  function (){
        var absUrl = $location.absUrl();
        $window.location.href = absUrl + '/deal';
      }


//TODO - JHP  - WE NEED TO ABSTRACT THESE FUNCTIONS - REDUNDANCY
//#########################################################################

  $scope.chargeTemplates = [{
        name: 'Optional Charges',
        url: 'app/components/charges/partials/add-charges/deal-charges-optional.html'},
        {
        name: 'Customer Specific Charges',
        url: 'app/components/charges/partials/add-charges/deal-customer-specific-charge.html'
    }];

$scope.initDealCharges = function(){
    $scope.deal.dealCharge = $scope.getDealCharges($scope.deal.id);  //DealCharge.query({id:1},$scope.deal);
    $scope.deal.selectedCharge = {};
    $scope.newDealCharge = {};
    $scope.deal.selectedSettlement = {};
    $scope.deal.selectedPosting = {};
    $scope.chargeTypeTemplate = $scope.chargeTemplates[0];
        $scope.allCustomerAccounts = DealLookupService.allCustomerAccount($rootScope.deals.customer.id);
        $scope.deal.settlementPartyCodes  = $scope.getSettlementPartyCodes($rootScope.deals.deal.id);

       FinancialRecoveryService.recoveryScheduleFuture($rootScope.deals.deal.id).$promise.then(function(data) {
            $scope.charges = data;
            $scope.selectedCharge = $scope.charges[0];
            if (!angular.isUndefined($scope.selectedCharge)) {
            	$scope.selectedPeriod = $scope.selectedCharge.period[0];
            }
            angular.forEach($scope.charges, function(value, key) {
              value.selected = false;
              angular.forEach(value.period, function (period, key) {
                period.selected = false;
              });
            });
            if (!angular.isUndefined($scope.selectedCharge)) {
            	$scope.selectedCharge.selected = true;
            	$scope.selectedPeriod.selected = true;
            }
         });
         
  }      

  $scope.initDealCustomer = function(){
    return DealCustomer.get({dealId:$rootScope.deals.deal.id},function(data){
      if (!$rootScope.deals.customer || ($rootScope.deals.customer && $rootScope.deals.customer.id !== data.id)){
        $rootScope.deals.customer = data;
        $scope.customer = $rootScope.deals.customer;
      }
      
    }).$promise;
  }
  
  $scope.rpad = function(value, maxlen, char) {
	  return value + Array(1 - value.length + maxlen).join(char || " ");
  }
  
  $scope.initDocuments = function(deal) {
	  var _docs = DocumentService.documents(deal.id, function() {
		 var _docTypes = DocumentService.listTypes( function() {
		 	 for (var docTypeIndex = 0; docTypeIndex < _docTypes.length; docTypeIndex++) {
				 
				 var currDocType = _docTypes[docTypeIndex];				 
				 $scope.items.push({name: currDocType.description+" Documents",  
			               modelname  : currDocType.code+"_documents", 
			               uri: "/app/components/deal/partials/document/documents.html",
			               maker_uri: "/app/components/deal/partials/document/maker-documents.html",
			               category:"documentation",
			               subcategory: currDocType.code+"_documents",
			               subitems: [
			                 ]});				 
				 
				 var docItems = $filter('filter')($scope.items, {subcategory: currDocType.code+"_documents"}, true);
				 var typeDocs = $filter('filter')(_docs, {typeCode: currDocType.code});
				 
				 if ((typeDocs.length > 0) && (docItems[0].subitems.length > 0)) {
					 for (var i = 0; i < typeDocs.length; i++) {
						 var doc = typeDocs[i];
						 var itemfound = false;
						 
						 for (var j = 0; j < docItems[0].subitems.length; j++) {
							 var item = docItems[0].subitems[j];
							 if (item.id === doc.id) {
								 itemfound = true;
								 item.name = doc.description;
								 item.link = DocumentService.contentUrl(doc.contentId);
							 }
						 }
						 if (!itemfound) {
							 docItems[0].subitems.push({
								 id : doc.id,
								 name : doc.description,
								 link : DocumentService.contentUrl(doc.contentId),
								 type : 'N/A'
							 });
						 }
						 
					 }
				 } else {
					angular.forEach(typeDocs, function(doc) {
						 docItems[0].subitems.push({
							 id : doc.id,
							 name : doc.description,
							 link : DocumentService.contentUrl(doc.contentId),
							 type : 'N/A'
						 });
					});
				 }			 
			 }
		 });
	  });  	
  }
  
  $scope.initDealInfoItems = function(deal) {
	  $scope.items.push({
          name: "Product Information",
          subitems: [
              {
                  name: "Product",
                  desc: 'N/A',
                  modelname: "product",
                  category:"deal_information",
                  subcategory: "product"
              },
              {
                  name: "Product Type",
                  desc: 'N/A',
                  modelname: "product_type",
                  category:"deal_information",
                  subcategory: "product"
              },
              {
                  name: "Product Classification",
                  desc: 'N/A',
                  modelname: "product_classification",
                  category:"deal_information",
                  subcategory: "product"
              },
              {
                  name: "Goods Description",
                  desc: 'N/A',
                  modelname: "goods_desc",
                  category:"deal_information",
                  subcategory: "product"
              },
              {
                  name: "Expiry Type",
                  desc: 'N/A',
                  modelname: "expiry_type",
                  category:"deal_information",
                  subcategory: "product"
              },
              {
                  name: "Expiry Date",
                  desc: $filter('date')(deal.expiryDate, $scope.format),
                  modelname: "expiry_date",
                  category:"deal_information",
                  subcategory: "product"
              },
              {
                  name: "Location of Expiry",
                  desc: deal.expiryLocation,
                  modelname: "expiry_location",
                  category:"deal_information",
                  subcategory: "product"
              },
              {
                  name: "Expiry Event Detail",
                  desc: deal.expiryEventDetails,
                  modelname: "expiry_event_detail",
                  category:"deal_information",
                  subcategory: "product"
              },
              {
                  name: "Review Date",
                  desc: deal.reviewDate,
                  modelname: "review_date",
                  category:"deal_information",
                  subcategory: "product"
              }
          ]
      },
      {
          name: "Financial Information",
          subitems: [
              {
                  name: "Amount",
                  desc: deal.principalAmount,
                  modelname: "amount",
                  category:"deal_information",
                  subcategory: "financial"
              },
              {
                  name: "Currency Type",
                  desc: deal.principalCurrencyCode,
                  modelname: "currency_type",
                  category:"deal_information",
                  subcategory: "financial"
              }
           
          ]
      });
	  
	  DealLookupService.allProducts().$promise.then(function(data) {
		  $scope.updateItem(data, {id: deal.productCode}, 'Product Information', 'product');
	  });
	  
	  DealLookupService.allProductTypes().$promise.then(function(data) {
		  $scope.updateItem(data, {id: deal.productTypeCode}, 'Product Information', 'product_type');
	  });
	  
	  DealLookupService.allProductClassificationTypes().$promise.then(function(data) {
		  $scope.updateItem(data, {id: deal.productClassificationTypeCode}, 'Product Information', 'product_classification');
	  });

	  DealLookupService.allExpiryTypes().$promise.then(function(data) {
		  $scope.updateItem(data, {code: deal.expiryTypeCode}, 'Product Information', 'expiry_type');
	  });	  
  }
  

  $scope.getDealCharges = function(id) {
    var result =  DealChargesServiceFactory.getDealCharges($scope.deal.id);
    result.$promise.then(function (data) {
        $scope.deal.dealCharge = data;
        return data;
    })
    //DealLookupService.getChargeDrCr(id).$promise.then(function (data) {
    //  $scope.deal.dealCharge = data;
    //  return data;
    //});
  }

  $scope.initDeal = function() {

	  if ($routeParams.taskId) {
			var task = WorkflowService.get($routeParams.taskId, function() {
				$scope.task = task;
				var variables = WorkflowService.readVariables(task, function() {
					var _dealId = WorkflowService.selectVariable(variables, 'dealId');
					if (_dealId) {
						Deal.get({dealId: _dealId.value}).$promise.then(function (data){
							$scope.deal = angular.copy(data);
							$rootScope.deals.deal = $scope.deal;
							$scope.initDealCustomer().then(function(data){
								$scope.initDealCharges();
								$scope.initItems($scope.deal);
								$scope.initDealInfoItems($scope.deal);
							});
							
						});						
					} else {
						$scope.deal = $rootScope.deals.deal;
						if ($scope.deal && $scope.deal.id){
							$scope.initDealCustomer().then(function(data){
								$scope.initDealCharges();
								$scope.initItems($scope.deal);
								$scope.initDealInfoItems($scope.deal);
							});
						} else if ($rootScope.deals.customer){
							$scope.customer = $rootScope.deals.customer;
						}
						
					}
				});
			});
		} else {

          if ($routeParams.dealRef){
              //TODO - This is a temp solution for M-visit bug fix, and a more permanent one needs to be established
              $scope.readOnly = true;
          }

		    if ($rootScope.deals.deal){
		      $scope.deal = $rootScope.deals.deal;
		      if ($scope.deal.id){
		        $scope.initDealCustomer().then(function(data){
		          $scope.initDealCharges();
				  $scope.initItems($scope.deal);
				  $scope.initDealInfoItems($scope.deal);
		        });
		      }else if ($rootScope.deals.customer){
		        $scope.customer = $rootScope.deals.customer;
		      }
		    }
		}
  }

  $scope.getReadOnly = function(){
      return $scope.readOnly;
  }
  
  $scope.updateItem = function(data, criteria, itemName, subitemModelName) {

	  angular.forEach($scope.items, function(value) {
		 if (value.name === itemName) {
			 angular.forEach(value.subitems, function(value) {
				if (value.modelname === subitemModelName) {
					  var types = $filter('filter')(data, criteria, true);
					  value.desc = types[0].description;
					  return;
				} 
			 });
		 } 
	  });
  }
  
  $scope.viewContent = function(doc) {
		if (!doc.processed) {
		    doc.link = $sce.trustAsResourceUrl(doc.link);
		    doc.processed = true;
		}
		$scope.selectedDocument = doc;
	};

	$scope.initItems = function(deal) {
        $scope.items = [
          {
              name: "Applicable Charges",
              uri: "/app/components/deal/partials/deal-default-charges-review.html",
              maker_uri: "/app/components/deal/partials/deal-default-charges-maker-review.html",
          },
          {
              name: "Recovery Schedule",
              modelname  : "recovery_schedule",
              subitems: [
                {heading: "Charge Details", uri: "/app/components/recovery/partials/charge-info.html", open:true},
                {heading: "Period Details", uri: "/app/components/recovery/partials/charge-period.html", open:false}
              ]
          }
        ];

        $scope.initItemOpenStatus();

		DealNotesService.query({dealId : deal.id, type : 'A'}, function(data) {
			if (data.length > 0) {
				$scope.items = angular.fromJson(data[0].note);
				$scope.noteid = data[0].id;
			} else {
				$scope.noteid = 0;//force a new one
			}
		      
			var review = new Review("review", deal.id);
			review.loadParties(function(data) {
				$scope.items.push(review.PartyItem);
			});
		      $scope.initDocuments(deal);
		});

	}

   $scope.initItemOpenStatus = function(){
       $scope.items[0].status={open:true};
       for (i = 1; i < $scope.items.length; i++) {
           $scope.items[i].status={open:false};
       }
   }

   $scope.groupClick = function($event,item){
       //avoid panel-body click from closing panel
       if ($($event.currentTarget).hasClass("panel-body")) {
           $event.stopPropagation();
           return;
       }
       item.status.open = !item.status.open;
   }
	
	$scope.sanitizeItems = function() {
		var items = $scope.items;
		var i = items.length;
		while (i--) {
			if (items[i].name === 'Applicable Charges') {
				null;
			} else if (items[i].subitems && items[i].subitems.length > 0) {
				var j = items[i].subitems.length;
				while (j--) {
					var subitem = items[i].subitems[j];
					if (subitem.subitemsvalues && subitem.subitemsvalues.length === 0) {
						items[i].subitems.splice(j, 1);
					}
				}
			} else {
				items.splice(i, 1);
			}			
		}
	}

      $scope.getSettlementPartyCodes = function(id){
        DealLookupService.getSettlementPostingCodes(id).$promise.then(function (data) {
            $scope.settlementPartyCodes = data;
            return data;
        });;
    }

  //#########################################################################
  //TODO - JHP  - WE NEED TO ABSTRACT THESE FUNCTIONS - REDUNDANCY   ^^^^^^^^^

  /*INIT The Deal*/
  $rootScope.initDeal($scope.initDeal,$scope.initDeal);

		
	} 
]);




dealReviewControllers.directive('approveItem', function(){
    
    return {
      restrict: 'CA',
      replace: false,
      link: function(scope, elem, attributes){
        elem.on("click", function(){
           var itemrow = $(this).parents(".itemcheck");

            if($(this).prop('checked')){

                $(this).parents(".form-group").css("background-color", "#f7f7f7")
                itemrow.find(".btn-warning").hide();

            } else {
                $(this).parents(".form-group").css("background-color", "transparent")
                itemrow.find(".btn-warning").show();
                $scope.results = false;
            }
            
        })
          
      }
  };
})


dealReviewControllers.directive('queryItem', function($compile){
    
    return {
      restrict: 'CA',
      replace: false,
     
      link: function(scope, elem, attributes){
        var newElem = $compile("<textarea class='col-xs-7 mr10'></textarea><input type='button' value='CANCEL' cancel-query class='btn btn-small btn-default mr10'/><input type='button' value='SAVE' save-item class='btn btn-small btn-warning'/>")(scope)
        
         elem.on("click", function(){
            var row = $(this).parent('.itemcheck');
            var modelname = row.find(".modelname").val(); 
             row.html(newElem).append('<input type="hidden" class="modelname" value="'+modelname+'" />');
           
         })
      },
      controller: function($scope, $element){
        $scope.cancelquery = function(){
            var newElems = $compile('<input type="checkbox" approve-item /> Approve <button class="btn btn-small btn-warning ml10" type="submit" query-item >Query</button>')($scope)
            $('.itemcheck').html( newElems )
          
        }        
      }
  };
})

dealReviewControllers.directive('saveItem', function($compile){ 
   return {
      restrict: 'CA',
      replace: false,
     
      link: function(scope, elem, attributes){
        elem.on("click", function(){
          var row = $(this).parent('.itemcheck');
          var modelname = row.find(".modelname").val();
          var index = scope.verificationresults.indexOf(modelname);


           if (index !== -1) {
                scope.verificationresults[index] = modelname;
            } else {
              scope.verificationresults.push(modelname, false);
            }
            row.html( 'QUERY SAVED' );
          
        })
      },
    }
})

dealReviewControllers.directive('cancelQuery', function($compile){ 
   return {
      restrict: 'CA',
      replace: false,
     
      link: function(scope, elem, attributes){
        elem.on("click", function(){
          var newElems = $compile('<input type="checkbox" approve-item /> Approve <button class="btn btn-small btn-warning ml10" type="submit" query-item >Query</button>')($scope)
            $(this).parents('.itemcheck').html( newElems )
        })
      },
    }
})

