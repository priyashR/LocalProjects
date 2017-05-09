'use strict';
var app = angular.module('TemplateControllers', ['ui.bootstrap', 'configService', 'commonControllers',
    'commonServices', 'ngSanitize', 'ngRoute', 'TemplateServices']);

app.controller('TemplateController', ['$scope','$rootScope', 'TemplateService', function($scope,$rootScope, TemplateService) {

    $scope.sections = [];
    $scope.document = {};

    $scope.sectionTypes = $rootScope.allSectionTypes;
    $scope.deliveryMethods = $rootScope.allDeliveryMethods;
    $scope.products =  $rootScope.allProducts;
    $scope.messageTypes = $rootScope.allMessageTypes;

    $scope.getSectionType = function(sectionTypeIn){
       var sectionTypes = [];

       $.each($scope.sectionTypes,function(){
            var sectionType = this;
            var found = false;
            if ($scope.document && $scope.document.sections){
                $.each($scope.document.sections,function(){
                    var section = this;
                    if (sectionType.code == section.sectionType){
                        found = true;
                        return false;
                    }
                });
                if (!found || sectionTypeIn==sectionType.code){
                    sectionTypes.push(sectionType);
                }
            }else{
                sectionTypes.push(sectionType);
            }
       });

       return sectionTypes;
    }

    $scope.sectionTypeFilter = function(){
       return function(sectionType){
           var found = false;
           var selectedMessageType = $rootScope.arrayFilter($scope.messageTypes,{code:$scope.document.messageType},undefined,true)
           if(selectedMessageType.length>0){
               var messageSections = selectedMessageType[0].messageSectionDTOList;
               for(var index in messageSections){
                   if (messageSections[index].sectionType == sectionType.code){
                    found = true;
                    break;
                   }
               }
           }
           return found;
       }
    }

    $scope.addSection = function(){
        if (!$scope.sections) {
            $scope.sections = [];
        }
        $scope.sections.push({});
    };

    $scope.removeSection = function(index){
        if ($scope.sections) {
            $scope.sections.splice(index, 1);
        }
    };

    $scope.saveTerm = function() {
        if (!$scope.section.sectionClauses) {
          $scope.section.sectionClauses = [];
        }
        var selectedClause = angular.copy($scope.section.selectedClause);

        selectedClause.sectionId = $scope.section.id;
        selectedClause.clauseId =  selectedClause.id;
        selectedClause.id = null;
        $scope.section.sectionClauses.push(selectedClause);
    }

    $scope.selectTerm = function(section, index) {
        $scope.section = section;
        $scope.section.sequence = index;
        $scope.terms = TemplateService.terms();
    }

    $scope.saveDocument = function() {
        $scope.document.sections = $scope.sections;
        var doPush = true;
        if ($scope.document.id){
           doPush = false;
        }
        TemplateService.saveDocument($scope.document).$promise.then(function(data) {
            if(doPush){
                $scope.data.documents.push(data);
            }
            $scope.document = data;
            $scope.switchTab(0);

            $scope.success = true;
        }, function(data) {
           $scope.success = true;
        });
    }

    $scope.reset = function() {
        $scope.success = false;
    }

    $scope.tabs = [
        {"heading": "Document Templates", "default":true , "uri": "app/components/template/partials/templates.html"},
        {"heading": "Document Template", "uri": "app/components/template/partials/template-config.html"},
    ];
    $scope.activeCrumb = {};
    $scope.data = {};
    $scope.data.documents = [];

    $scope.init = function(){

        var promise = TemplateService.documents();

        promise.$promise
            .then(function(response){
                $scope.data.documents = response;
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

    $scope.editDocument = function(document){
        $scope.document = document;
        $scope.sections = document.sections;
        $scope.switchTab(1);
    }

    $scope.cancelEditDocument = function(){
        $scope.document = {};
        $scope.switchTab(0);
    }

    $scope.addDocument = function(){
        $scope.document = {};
        $scope.switchTab(1);
    }

    $scope.init();

    $scope.isValid = function() {
         return $scope.document.messageType && $scope.document.deliveryMethod && $scope.document.productId && $scope.document.name;
    }

}]);