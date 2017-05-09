var messagetypeControllers = angular.module('MessageTypeControllers',['ui.bootstrap', 'ngSanitize','xeditable','ngRoute','ngMessages','LocalStorageModule']);

messagetypeControllers.controller('MessageTypeController', ['$scope','$rootScope','$uibModal','$filter','MessageTypeFactory',
    function($scope, $rootScope,$uibModal,$filter,MessageTypeFactory){

    $scope.success = null;
    $scope.activeCrumb = {};
    $scope.data = {};
    $scope.data.activeMessageType = {};
    $scope.data.activeMessageType.messageType = {};
    $scope.data.messageTypes = [];
    $scope.tabs = [
             	{"heading": "Message Types", "default":true , "uri": "/app/components/config/partials/messagetype/messagetypes.html"},
             	{"heading": "Message Type and Section Types", "uri": "/app/components/config/partials/messagetype/messagetype.html"},
             	{"heading": "Section Type Link", "uri": "/app/components/config/partials/messagetype/sectiontype.html"},
          	];
    $scope.products =  $rootScope.allProducts;

    $scope.init = function(){
        var promise = MessageTypeFactory.messagetypes();

        promise.$promise
            .then(function(response){
                $scope.data.messageTypes = response;
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

    $scope.editMessageType = function(messagetype){
        $scope.data.activeMessageType = messagetype;
        $scope.switchTab(1);
    }

    $scope.cancelEditMessageType = function(){
        $scope.data.activeMessageType = {};
        $scope.switchTab(0);
    }

    $scope.saveMessageType = function(){
        if($scope.data.activeMessageType.activeMessageSection && $scope.data.activeMessageType.activeMessageSection.sectionType){
            if(!$scope.data.activeMessageType.messageSectionDTOList){
                $scope.data.activeMessageType.messageSectionDTOList = [];
            }
            $scope.data.activeMessageType.messageSectionDTOList.push($scope.data.activeMessageType.activeMessageSection);
            $scope.data.activeMessageType.activeMessageSection = {};
        }
        MessageTypeFactory.savemessagetype($scope.data.activeMessageType).$promise.then(function(result){
           $scope.data.activeMessageType = result;
           $scope.switchTab($scope.activeCrumbIndex-1);
           $scope.init();
        });
    }

    $scope.addMessageType = function(){
        $scope.data.activeMessageType = {};
        $scope.switchTab(1);
    }

    $scope.editMessageSection = function(messageSection){
        $scope.data.activeMessageType.activeMessageSection = messageSection;
        $scope.switchTab(2);
    }

    $scope.cancelMessageSection = function(){
        $scope.data.activeMessageType.activeMessageSection = {};
        $scope.switchTab(1);
    }

    $scope.addMessageSection = function(){
        $scope.data.activeMessageType.activeMessageSection = {};
        $scope.switchTab(2);
    }

    $scope.init();

}]);
