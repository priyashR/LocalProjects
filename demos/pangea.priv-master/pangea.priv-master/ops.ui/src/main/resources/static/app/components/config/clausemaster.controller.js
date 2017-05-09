var clauseMasterControllers = angular.module('ClauseMasterControllers',['ui.bootstrap', 'ngSanitize','xeditable','ngRoute','ngMessages','LocalStorageModule']);

clauseMasterControllers.controller('ClauseMasterController', ['$scope','$uibModal','$rootScope','$filter','ClauseMasterFactory','DealLookupService','PlaceholderFactory',
    function($scope,$uibModal,$rootScope,$filter,ClauseMasterFactory,DealLookupService,PlaceholderFactory){

    $scope.terms = [];
    $scope.success = null;
    $scope.activeCrumb = {};
    $scope.data = {};
    $scope.data.activeTerm = {};
    $scope.tabs = [
             	{"heading": "Terms", "default":true , "uri": "/app/components/config/partials/clausemaster/terms.html"},
             	{"heading": "Term and Clauses", "uri": "/app/components/config/partials/clausemaster/termwithclauses.html"},
             	{"heading": "Clause", "uri": "/app/components/config/partials/clausemaster/clause.html"},
          	];

    $scope.init = function(){

        var promise = ClauseMasterFactory.terms();

        promise.$promise
            .then(function(response){
                $scope.data.terms = response;
                if(!$scope.activeCrumb.uri){
                    $scope.switchTab(0);
                }
            })
            .catch(function(response){

            });

    }

    $scope.selectTabClass = function(tab){

        if(tab.selected ){
            return  "active"
        }
        if(tab.disabled){
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
        $scope.activeCrumb.disabled = true;
        $scope.activeCrumb = $scope.tabs[index];
        $scope.activeCrumbIndex = index;
        $scope.activeCrumb.disabled = false;
        $scope.activeCrumb.selected = true;
    }

    $scope.editTerm = function(term){
        $scope.data.activeTerm = term;
        $scope.data.activeTerm.activeClause = {};
        $scope.switchTab(1);
    }

    $scope.cancelEditTerm = function(){
        $scope.data.activeTerm = {};
        $scope.switchTab(0);
    }

    $scope.editClause = function(clause){
        $scope.data.activeTerm.activeClause = clause;
        $scope.switchTab(2);
    }

    $scope.cancelEditClause = function(){
        $scope.data.activeTerm.activeClause = {};
        $scope.switchTab(1);
    }

    $scope.saveTerm = function(){
        if($scope.data.activeTerm.activeClause.description){
            if(!$scope.data.activeTerm.clauses){
                $scope.data.activeTerm.clauses = [];
            }
            $scope.data.activeTerm.clauses.push($scope.data.activeTerm.activeClause);
            $scope.data.activeTerm.activeClause = {};
        }
        ClauseMasterFactory.saveterm($scope.data.activeTerm).$promise.then(function(result){
           $scope.data.activeTerm = result;
           $scope.data.activeTerm.activeClause = {};
           $scope.switchTab($scope.activeCrumbIndex-1);
        });
    }

    $scope.addTerm = function(){
        $scope.data.activeTerm = {};
        $scope.data.clauses = [];
        $scope.switchTab(1);
    }

    $scope.addClause = function(){
        $scope.data.activeTerm.activeClause = {};
        $scope.switchTab(2);
    }

    $scope.getAllPlaceHolders = function(){
        var promise = PlaceholderFactory.placeholders();
        return promise.$promise;
    }

    $scope.getProductType = function(clause){
        var productType = $rootScope.arrayFilter($rootScope.allProductTypes,{id:clause.productType},undefined,true);
        if (productType.length==0){
            productType="";
        }else{
            productType=productType[0].description+'('+productType[0].product.description+')';
        }
        return productType;
    }

    $scope.openModalSelect = function (textSelector) {

          var ModalSelectController = function($uibModalInstance, $scope,textSelector,placeHolders){

             placeHolders().then(function(response){
                $scope.data = {};
                $scope.data.placeholders = response;
              })

            $scope.selectRow = function(placeholder){
               $scope.selectedPlaceholder = placeholder;
               $scope.selectedPatternCode = placeholder.code;
            };

            $scope.isSelected = function(placeholder){
                return placeholder === $scope.selectedPlaceholder;
            }

           $scope.okModal = function () {
             $uibModalInstance.close($scope.selectedPatternCode);
           };

           $scope.cancelModal = function () {
             $uibModalInstance.dismiss('cancel');
           };

         }

          var modalInstance = $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'app/components/config/partials/templateplaceholder/placeholder-select.html',
            controller: ModalSelectController,
            resolve: {
                textSelector: function() {return textSelector;},
                placeHolders: function() {return $scope.getAllPlaceHolders;}
            }
          });

          modalInstance.result.then(function (selectedPatternCode) {
               var el = $(textSelector)[0];
               replaceSelectedText(el, " ${"+selectedPatternCode+"} ");
               $scope.data.activeTerm.activeClause.text = el.value;
          });

        };

    $scope.init();

}]);

