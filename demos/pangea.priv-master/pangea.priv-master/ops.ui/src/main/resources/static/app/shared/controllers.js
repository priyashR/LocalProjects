var commonControllers = angular.module('commonControllers', ['configService']);


//This is just an example of how to use it in a controller
commonControllers.controller('lookupDataCtrl', ['$scope', 'lookupDataService', function($scope, LookupDataService) {
}]);

commonControllers.controller('emptyController', ['$scope', function($scope) {
	
	

}]);

commonControllers.filter('productTypebyProductCode', function() {
	return function(productTypes,productCode) {
		var out = [];
		angular.forEach(productTypes, function(productType) {
			if(productType.product.id === productCode){
				out.push(productType);
			}
		});
		
		return out;
	}
});

commonControllers.filter('productTypebyProductClassification', function() {
	return function(productTypes,productCode) {
		var out = [];
		angular.forEach(productTypes, function(productType) {
			if(productType.productClassification.id === productCode){
				out.push(productType);
			}
		});

		return out;
	}
});

commonControllers.filter('productClassbyProductType', function() {
	return function(productClassifications,params) {
		var out = [];
		var productTypeCrit;
		angular.forEach(params.productTypes, function(productType) {
			if(productType.id === params.productTypeCode){
				productTypeCrit = productType;
			}
		});
		angular.forEach(productClassifications, function(productClassification) {
			if(productTypeCrit && productClassification.id === productTypeCrit.productClassification.id){
				out.push(productClassification);
			}
		});
		
		return out;
	}
});

commonControllers.directive('dynamicController', function($compile) {
	  return  {
	    transclude: 'element',
	    scope: {
	      'dynamicController': '=' 
	    },
	    link: function(scope, element, attr, ctrl, transclude) {
	      var el = null;
	      scope.$watch('dynamicController',function() {
	        if (el) {
	          el.remove();
	          el = null;
	        }
	        transclude(function(clone) {
	          clone.attr('ng-controller', scope.dynamicController);
	          clone.removeAttr('dynamic-controller');
	          el = $compile(clone[0])(scope.$parent)
	          element.after(el);
	        });
	      });
	    }
	  }
	});
