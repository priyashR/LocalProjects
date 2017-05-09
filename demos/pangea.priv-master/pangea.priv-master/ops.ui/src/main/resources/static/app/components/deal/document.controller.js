var documentControllers = angular.module('DocumentControllers', ['ui.bootstrap', 'ngRoute','ngFileUpload','MessageTypeServices']);

documentControllers.controller('DocumentMainCtrl', ['$scope','$rootScope','$filter', '$sce', '$timeout','$routeParams', 'DocumentService', 'DealLookupService', 'Upload','ProductTerms','MessageTypeService','Deal','WorkflowService',
   function($scope,$rootScope,$filter,$sce,$timeout,$routeParams,DocumentService,DealLookupService,Upload,ProductTerms,MessageTypeService,Deal,WorkflowService) {

        $scope.documentSteps = [
            {"heading": "Delivery Information", "uri": "/app/components/deal/partials/document/document-delivery-type.html", "disabled" : true, "default": true},
            {"heading": "Template", "uri": "/app/components/deal/partials/document/deal-template.html", "disabled" : false, "default": false},
            {"heading": "Terms and conditions", "uri": "/app/components/deal/partials/document/document-terms-and-conditions.html", "disabled" : false, "default": false},
            {"heading": "Contracts", "uri": "/app/components/deal/partials/document/document-contract.html", "disabled" : false, "default": false},
        ];

   	    $scope.sections = [];
        $scope.document = {};

        //JHP - @Deprecated
        $scope.sections.push({"type": "Introduction"});
        $scope.sections.push({"type": "Guarantee Details"});
        $scope.sections.push({"type": "Counter Guarantee"});
        $scope.sections.push({"type": "Special Instructions"});
        $scope.sections.push({"type": "Sanction Screening Clause"});
        $scope.sections.push({"type": "Signatures"});

   		$scope.allNSTOptions = [{"code":"MessageType","description":"Document Builder"},{"code":"Upload","description":"Upload Document"}];

        $scope.getSectionTypeDescription = function(sectionType){
            return $rootScope.arrayFilter($rootScope.allSectionTypes,{code:sectionType},'description');
        }

   		$scope.docsverify = false;

	    //JHP - @Deprecated - Almost
		$scope.selectContractWording = function(wording_type){
			if(wording_type === '01'){
				$scope.contract_search  	= true;
				$scope.upload_docs			= false;
			}

			if(wording_type === '02'){
				$scope.contract_search  	    = false;
				$scope.contract_search_results  = false;
				$scope.upload_docs				= true;
				$scope.launchUpload({code : 'C'});//default to a contract document
			}
		}

        //JHP - @Deprecated
		$scope.searchType = 'SEARCH_BY_TEMPLATE';
			
		$scope.supporting_docs = {};

		$scope.updateDeliveryMethod = function() {
			
			if (!$scope.allDeliveryMethods) {
				$scope.allDeliveryMethods = DealLookupService.allDeliveryMethods();
			}

			var deliveryMethods = $filter('filter')($scope.allDeliveryMethods, {'code':$scope.deal.deliveryTypeCode}, true);
			
			if (deliveryMethods.length && deliveryMethods.length === 1) {
				$scope.deliveryTypeDescription = deliveryMethods[0].description;
			}
			
		}
		
		$scope.loadDocuments = function() {
			
			$scope.selectContractWording($rootScope.deals.deal.standardTemplateCode);
			$scope.updateDeliveryMethod();

			$scope.loadDealDocumentTemplate();
			$scope.loadDealNonStandardTemplate()

			if (!$rootScope.deals.deal.documentStatusCode){
				$rootScope.deals.deal.documentStatusCode="01";
			}
			
			var res_ = DocumentService.documents($rootScope.deals.deal.id, function() {

				$scope.initTerms();

                //Check if application for has been attached from workflow
				var found = false;
				for(var i in res_){
				    var dealDoc = res_[i];
					if    (dealDoc.statusCode === "04"){
						found = true;
					}
                    // Check if CONTRACT has been attached
					if (dealDoc.typeCode==='C'){
					    $scope.contractIsAttached = true;
					}
				}
				if ((!found && $routeParams.taskId)) {
				    //TODO - JHP this should happen in backend
                    //Attached application to deal if not yet attached
					DocumentService.linkDocuments($routeParams.taskId, $rootScope.deals.deal.id, function() {
						var _docs = DocumentService.documents($rootScope.deals.deal.id, function() {
							$scope.contract_documents = _docs;
						});
					});
				} else {
					$scope.contract_documents = res_;
				}
			});
			
			var statuses_ = DocumentService.listStatuses(function() {
				$scope.statusTypes = statuses_;
			});
			
			var types_ = DocumentService.listTypes(function() {
				$scope.documentTypes = types_;
			});
			
		};
		
		$scope.viewContent = function(doc) {
			
			var attachment={};
			var url;
			if(doc.previewst){
			    url = DocumentService.previewUrlFromST(doc.dealId,doc.docId);
			}else if(doc.preview){
				url = DocumentService.previewUrl(doc.dealId,doc.docId);
			}else{
				url = DocumentService.contentUrl(doc.contentId);
			}
			
			attachment.url = $sce.trustAsResourceUrl(url);
		
			var promise = $rootScope.getPdf(attachment);
			
			promise.success(function(uri){
				doc.url = $sce.trustAsResourceUrl(uri);
				$scope.selectedDocument = doc;	
			});
			
			return promise;
				
		};

		$scope.getCurrentDealTemplate = function(){
            var doc;
            if($scope.dealNonStandardTemplates && $scope.dealNonStandardTemplates.length>0){
                doc = $scope.dealNonStandardTemplates[0];
            }else if($scope.dealDocumentTemplates && $scope.dealDocumentTemplates.length>0){
                doc = $scope.dealDocumentTemplates[0]
            }
            return doc;
		}
		
        $scope.viewPreviewST = function(doc){
            if (!doc){
                doc = $scope.getCurrentDealTemplate();
            }
            $rootScope.block = true;

             var document = {
                "dealId": $rootScope.deals.deal.id,
                "docId": doc.id
             };

            document.previewst = true;

            $scope.viewContent(document).then(function(){
                $('#previewModal').modal('show');
            });
        }

		$scope.viewRawPreview = function(doc){
			$rootScope.block = true;

             var document = {
                "dealId": $rootScope.deals.deal.id,
                "docId": doc.id
             };

            document.preview = true;

            $scope.viewContent(document).then(function(){
                $('#previewModal').modal('show');
            });
		}

		$scope.selectDocumentTemplate = function(doc) {
			$rootScope.block = true;

			var response = DocumentService.linkDealDocumentTemplate($rootScope.deals.deal.id, doc);

			response.$promise.then(function(result){
                $scope.contract_search_results = false;
                $scope.document_search_results = {};
                $scope.loadDealDocumentTemplate();

			});
			return response;

		};


		$scope.proceedToSettlement = function(){
			$scope.docsverify = true;
			$rootScope.deals.tabs[3].disabled = function(){return false}
			$routeParams.nav = 'settlement';
		}

		$scope.contractDocumentsExists = function(){
			var documents = $scope.contract_documents;
			var exists = false;
			for(var index in documents){
				if (documents[index]){
					var document = documents[index];
					if (document.typeCode==='C'){
						exists = true;
					}
				}
			}
			return exists;
		}
		
		$scope.saveDocument = function(document) {
			var result = DocumentService.saveDocument($rootScope.deals.deal.id, document);
			result.$promise.then(function() {
				var docs_ = DocumentService.documents($rootScope.deals.deal.id, function() {
					$scope.contract_documents = docs_;
				});
			});
			return result;
		};
		$scope.linkDocument = function(document) {
			DocumentService.linkDocument($rootScope.deals.deal.id, document, function() {
				var docs_ = DocumentService.documents($rootScope.deals.deal.id, function() {
					$scope.contract_documents = docs_;
				});				
			});			
		};

        $scope.saveTerm = function() {

            if (!$scope.section.sectionClauses) {
              $scope.section.sectionClauses = [];
            }
            var selectedClause = angular.copy($scope.section.selectedClause);

            selectedClause.sectionId = $scope.section.id;
            selectedClause.clauseId =  selectedClause.id;
            selectedClause.id =null;

            $scope.section.sectionClauses.push(selectedClause);


        }

		$scope.selectTerm = function(section, index) {
		    $scope.section = section;
		    $scope.section.sequence = index;
		    $scope.terms = DealLookupService.allTerms();
		}

		$scope.generateTemplate = function() {
		    $scope.document.sections = $scope.sections;
		    if (($scope.dealNonStandardTemplates || $scope.dealDocumentTemplates) && !$scope.contractIsAttached){
		        var doc = $scope.getCurrentDealTemplate();
		        if (doc){
		            return $scope.selectDocumentTemplate(doc).$promise;
		        }
		    }
		}

		$scope.saveThenNextDocumentTab = function(){
		   if (!$scope.contractIsAttached){
                var result = $scope.generateTemplate()
                if (result){
                    result.then(function(){
                        var doc = $scope.getCurrentDealTemplate();
                        doc.typeCode='C'
                        $scope.saveDocument(doc).$promise.then(function(){
                            var docs_ = DocumentService.documents($rootScope.deals.deal.id, function() {
                                $scope.contract_documents = docs_;
                                $scope.nextDocumentTab();
                            });
                        });
                    });
                }else{
                    var doc = $scope.getCurrentDealTemplate();
                    doc.typeCode='C'
                    $scope.saveDocument(doc).$promise.then(function(){
                        var docs_ = DocumentService.documents($rootScope.deals.deal.id, function() {
                            $scope.contract_documents = docs_;
                            $scope.nextDocumentTab();
                        });
                    });
                }
		    }else{
		        $scope.nextDocumentTab();
		    }
		}

		$scope.saveThenViewPreviewST = function(){
            var result = $scope.generateTemplate()
            if (result){
                result.then(function(){
                    $scope.viewPreviewST();
                });
            }else{
                viewPreviewST();
            }
        }

		$scope.launchUpload = function(type) {
			$scope.uploadType = type;
			$scope.f = {};
			$scope.errFile = {};
		};
		
		$scope.uploadFile = function(file, errFiles) {
			file.description = $scope.f.description;
	        $scope.f = file;
	        $scope.errFile = errFiles && errFiles[0];
			
            file.upload = Upload.upload({
                url: DocumentService.uploadUrl(),
                //fields:, //TODO: EVT: Add fields to store this doc
                data: {file: file}
            });

            file.upload.then(function (response) {
            	var contentId = response.data;
    			var document = {
    					"id": null,
    					"dealId": $rootScope.deals.deal.id,
    					"contentId": contentId,
    					"statusCode": $rootScope.deals.deal.documentStatusCode,
    					"description": file.description,
    					"typeCode": $scope.uploadType.code};
    			
    			$scope.linkDocument(document);
            }, function (response) {
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 * 
                                         evt.loaded / evt.total));
            });
			
		};

		$scope.getDocuments = function() {
             DocumentService.loadDocuments().$promise.then(function(data) {
                $scope.document_search_results = data;
                $scope.contract_search_results = true;
             });
		}

		$scope.searchDocs = function() {
			$scope.contract_search_results = true;
			$scope.document_search_results = {};
			if ($scope.searchType === 'SEARCH_BY_TEMPLATE') {
				DocumentService.searchByCode($scope.searchValue, $rootScope.deals.deal.documentStatusCode).$promise.then(function(_results) {
					$scope.document_search_results = _results;
				});
			} else if ($scope.searchType === 'SEARCH_BY_PRODUCT') {
				DocumentService.searchByProduct($rootScope.deals.deal.productTypeCode, $rootScope.deals.deal.documentStatusCode, $scope.searchValue, function(_results) {
					$scope.document_search_results = _results;
				});
				
			} else if ($scope.searchType === 'SEARCH_BY_CUSTOMER') {
				DocumentService.searchByCustomer($scope.searchValue, $rootScope.deals.deal.documentStatusCode, function(_results) {
					$scope.document_search_results = _results;
				});
				
			}
		};

		$scope.getShowUploads = function(){
		    var result = $scope.deal.nonStandardTemplateProcess=='Upload';
		    result = result && $scope.isConfigurable();
		    return result;
		}
		$scope.getShowBuilder = function(){
		    var result = $scope.deal.nonStandardTemplateProcess=='MessageType';
		    result = result && $scope.isConfigurable();
		    return result;
		}
		$scope.isConfigurable = function(){
            var result = !$scope.dealNonStandardTemplates && !$scope.dealDocumentTemplates ;
            result = result && !$scope.contractIsAttached;
            return result;
        }

		$scope.saveDeliveryMethod = function() {
			$scope.updateDeliveryMethod();
		}
		$scope.terms = [];
		$scope.initTerms = function(){
			if ($scope.deal.productTypeCode && $scope.deal.step){
				
				var pt = ProductTerms.query({productTypeId:$scope.deal.productTypeCode,stepTypeCode:$scope.deal.step})
				
				pt.$promise.then(function(termData){
					$scope.deal.terms = termData;
					$scope.terms = $scope.deal.terms;
				});
			}
		}

		$scope.loadDealDocumentTemplate = function(){
		    var result = DocumentService.loadDealDocumentTemplates($rootScope.deals.deal.id);
		    result.$promise.then(function(result){
		        if(result.length){
		            $scope.dealDocumentTemplates = result;
		        }
		    });
		}

		$scope.getDealTemplates = function(){
		    if($scope.deal.standardTemplateCode=="01"){
		        return $scope.dealDocumentTemplates;
		    }
		    if($scope.deal.standardTemplateCode=="02"){
		        return $scope.dealNonStandardTemplates;
		    }

		}

        $scope.loadDealNonStandardTemplate = function(){
            var result = DocumentService.loadDealNonStandardTemplates($rootScope.deals.deal.id);
            result.$promise.then(function(result){
                if(result.length){
                    $scope.dealNonStandardTemplates = result;
                }
            });
        }

        $scope.addNST = function(){
            var response = DocumentService.createDealNonStandardTemplate($rootScope.deals.deal.id, $scope.deal.messageType);

            response.$promise.then(function(result){
                $scope.loadDealNonStandardTemplate();
                $scope.escalateToLegal();
            });
        }


        $scope.selectTabClass = function(tab){
            if(tab.default ){
                return  "active"
            }
            if(!tab.disabled){
                return  "disabled"
            }

        }

        $scope.nextDocumentTab = function() {

            if ($scope.documentIndex === $scope.documentSteps.length - 1) {
                $rootScope.deals.tabs[3].disabled = function(){return false}
                $routeParams.nav = "settlement";

            } else {
                if ($scope.documentIndex === 0){
                    //JHP - needed to save deal to ensure first tab changes are save
                    Deal.save($scope.deal);
                }
                $scope.selectDocumentContent($scope.documentSteps[++$scope.documentIndex]);
            }

        }

        $scope.selectDocumentContent = function(crumb) {
            $scope.documentIndex = $scope.documentSteps.indexOf(crumb);
            $scope.removeActiveTab($scope.documentSteps);
            crumb.disabled = true;
            crumb.default  = true;
            $scope.activeDocumentCrumb = crumb;
            $scope.getDocumentContent();
        };

        $scope.getDocumentContent = function() {
            var resultUri;
            if (!$scope.activeDocumentCrumb) {
                $scope.activeDocumentCrumb = $scope.documentSteps[0];
            }
            if (isFunction($scope.activeDocumentCrumb.uri)) {
                resultUri = $scope.activeDocumentCrumb.uri();
            } else {
                resultUri = $scope.activeDocumentCrumb.uri;
            }
            return resultUri;
        }

        $scope.escalateToLegal = function() {
            DocumentService.listTemplateEscalations({dealId : $scope.deal.id}).$promise.then(function(data) {
                var payload = {
                    "escalations": []
                };

                if (data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        var esc = {};
                        esc.name = data[i].code;
                        esc.due = '';
                        payload.escalations.push(esc);
                    }

                    WorkflowService.escelations.save({"id":$scope.task.processInstanceId}, payload);
                }

            });
        }

		/*INIT The Deal*/
		$rootScope.initDeal($scope.loadDocuments,$scope.loadDocuments);
		
}]);




