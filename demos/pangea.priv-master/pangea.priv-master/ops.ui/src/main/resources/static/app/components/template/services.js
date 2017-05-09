var service = angular.module('TemplateServices', ['ngResource', 'configService']);

service.factory('TemplateService', function(MessageType, DocumentSaveService,DocumentsService, TermService, SectionTypeService, DeliveryMethodService, ProductService, MessageTypeSectionTypeService) {
	return {
		messageTypes: function() {
			return MessageType.query();
		},
		documents: function(document) {
            return DocumentsService.query();
        },
		saveDocument: function(document) {
            return DocumentSaveService.save(document);
        },
        getSectionTypes: function() {
            return SectionTypeService.query();
        },
        deliveryMethods: function() {
            return DeliveryMethodService.query();
        },
        products: function() {
            return ProductService.query();
        },
        terms: function() {
             return TermService.query();
        },
        messageTypeSections : function(messageType) {
            MessageTypeSectionTypeService.query(messageType);
        }
	};
});

service.factory('TermService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/terms', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

service.factory('SectionTypeService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/sectiontypes', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

service.factory('ProductService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/products', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

service.factory('DeliveryMethodService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/deliveryMethod', {}, {
		query : {
			method : 'GET',
			isArray : true
		}
	});
} ]);

service.factory('MessageType', [ '$resource', 'config', function($resource, config) {
    return $resource(config.apiUrl + '/messageTypes', {}, {
        query : {
            method : 'GET',
            isArray : true
        }
	});
} ]);

service.factory('DocumentSaveService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/document', {}, {
		save : {
			method : 'POST',
			params : {documentDto : '@document'}
		}
	});
} ]);

service.factory('DocumentsService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/documents', {}, {
		query : {
			method : 'GET',
            isArray : true,
            withCredentials : true
		}
	});
} ]);

service.factory('MessageTypeSectionTypeService', [ '$resource', 'config', function($resource, config) {
	return $resource(config.apiUrl + '/messageTypeSectionTypes/:messageType', {messageType : '@messageType'}, {
		query : {
			method : 'GET',
            isArray : true
		}
	});
} ]);


