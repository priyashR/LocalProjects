var docServices = angular.module('DocServices', ['ngResource', 'configService']);

docServices.factory('DocumentService', function(AdviceTemplateService, DealDocumentService,DealDocumentTemplateService, DocumentStatusService, ContentService, DocumentTypesService, ContentCategoryService,WorkflowService,LinkingService,DealDocumentPreviewService, DealTemplateService, GetDocumentService,DealDocumentNSTemplateService, TemplateEscalationService ) {
	return {
		loadDocuments : function() {
		    return GetDocumentService.query();
		},
        loadDealDocumentTemplates : function(dealId) {
            return DealDocumentTemplateService.query({dealId : dealId});
        },
        loadDealNonStandardTemplates : function(dealId){
            return DealDocumentNSTemplateService.query({dealId : dealId});
        },
        linkDealDocumentTemplate : function(dealId,doc){
            return DealDocumentTemplateService.save({dealId : dealId,docId:doc.id},doc);
        },
        createDealNonStandardTemplate : function(dealId,messageTypeCode){
            return DealDocumentNSTemplateService.save({dealId : dealId,messageTypeCode:messageTypeCode},{})
        },
		documents : function(id, handler) {
			return DealDocumentService.query({dealId : id}, handler);
		},
		saveDocument : function(dealId_, doc, handler) {
			return DealDocumentService.save({dealId : dealId_}, doc, handler);
		},
		linkDocument : function(dealId_, doc, handler) {
			return DealDocumentService.save({dealId : dealId_,contentId:doc.contentId}, doc, handler);
		},
		addDocument : function(document, handler) {
            return DealTemplateService.save(document, handler);
        },
		linkDocuments : function(taskId, dealId_, handler) {
			var task = WorkflowService.get(taskId, function() {
				var _attachments = WorkflowService.listAttachments(task, function() {
					var request = {"documents": []};
					for (var index = 0; index < _attachments.length; index++) {
						var doc = {};
						var attachment = _attachments[index];
						doc.description = attachment.description;
						doc.source = attachment.name;
						doc.url = WorkflowService.attachmentUrl(attachment);
						doc.categoryId = attachment.typeId;
						doc.mediaType = attachment.mediaType;
						request.documents.push(doc);
					}
					
					LinkingService.execute({dealId : dealId_}, request, handler);
				});				
			});
		},
		searchByProduct: function(productTypeId_, status_, description_, handler) {
			return AdviceTemplateService.query({productTypeId : productTypeId_, status : status_, description : description_}, handler);
		},
		searchByCode: function(code_, status_) {
			return AdviceTemplateService.query({code : code_, status : status_});
		},
		searchByCustomer: function(customer_, status_,handler) {
			return AdviceTemplateService.query({customer : customer_, status : status_}, handler);
		},
		listStatuses: function(handler) {
			return DocumentStatusService.get(handler);
		},
		listTypes: function(handler) {
			return DocumentTypesService.get(handler);
		},
		listCategories: function(handler) {
			return ContentCategoryService.get(handler);
		},
		uploadUrl : function() {
			return ContentService.uploadUrl();
		},
		contentUrl : function(id) {
			return ContentService.contentUrl(id);
		},
		previewUrl : function(dealId,docId){
			return DealDocumentPreviewService.previewUrl(dealId,docId);
		},
		previewUrlFromST : function(dealId,docId){
            return DealDocumentPreviewService.previewUrlFromST(dealId,docId);
        },
        listTemplateEscalations : function(dealId){
            return TemplateEscalationService.query(dealId);
        },
	};

});

docServices.factory('AdviceTemplateService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/advices/templates', {}, {
		query : {
			method : 'GET',
			isArray : true,
			params : {
				productTypeId : '@productTypeId',
				status : '@status',
				description : '@description',
				code : '@code',
				customer : '@customer'
			}
		}
	});
} ]);

docServices.factory('DealTemplateService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/document', {}, {
		save : {
			method : 'POST',
			params : {documentDto : '@document'}
		}
	});
} ]);

docServices.factory('DealDocumentService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/deal/:dealId/documents/:contentId', {dealId : '@id', contentId : '@contentId'}, {
		query : {
			method : 'GET',
			isArray : true
		},
		save : {
			method : 'POST'
		}
	});
} ]);

docServices.factory('DealDocumentPreviewService', [ '$resource', 'config', function($resource, config) {
	return {	
		previewUrl: function(dealId,docId) {
			return config.apiUrl + '/deal/' + dealId + '/documentst/' + docId + '/preview?raw=true';
		},
		previewUrlFromST:  function(dealId,docId) {
            return config.apiUrl + '/deal/' + dealId + '/documentst/' + docId + '/preview';
        }
	};
} ]);

docServices.factory('DocumentTypesService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/deal/documents/types', {}, {
		get : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

docServices.factory('DocumentStatusService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/deal/documents/statuses', {}, {
		get : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

docServices.factory('ContentService',[ '$resource', 'config', function($resource, config) {
	return {	
		contentUrl: function(id, handler) {
			return config.apiUrl + '/content/' + id;
		},
		uploadUrl : function() {
			return config.apiUrl + '/content/upload';
		}
	};
} ]);


docServices.factory('LinkingService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/content/link/:dealId', {dealId:'@id'}, {
		execute : {
			method : 'POST',
			isArray : true
		}
	});
} ]);

docServices.factory('ContentCategoryService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/content/categories', {}, {
		get : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

docServices.factory('GetDocumentService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/documents', {}, {
		get : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

docServices.factory('DealDocumentTemplateService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/documents', {}, {
		get : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

docServices.factory('DealDocumentTemplateService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/deal/:dealId/documentTemplate/:docId', {dealId:'@id',docId:'@docId'}, {});
} ]);

docServices.factory('DealDocumentNSTemplateService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/deal/:dealId/messageType/:messageTypeCode', {dealId:'@id',messageTypeCode:'@messageTypeCode'}, {});
} ]);

docServices.factory('TemplateEscalationService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/deal/:dealId/template/escalations', {dealId:'@id'}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);
