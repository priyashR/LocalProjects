var clauseMasterControllers = angular.module('PlaceholderControllers',['ui.bootstrap', 'ngSanitize','xeditable','ngRoute','ngMessages','LocalStorageModule']);

clauseMasterControllers.controller('PlaceholderController', ['$scope','$uibModal','PlaceholderFactory',
    function($scope,$uibModal,PlaceholderFactory){


    $scope.success = null;
    $scope.activeCrumb = {};
    $scope.data = {};
    $scope.data.activePlaceholder = {};
    $scope.data.placeholders = [];
    $scope.tabs = [
             	{"heading": "Placeholders", "default":true , "uri": "/app/components/config/partials/templateplaceholder/placeholders.html"},
             	{"heading": "Placeholder", "uri": "/app/components/config/partials/templateplaceholder/placeholder.html"},
          	];
    $scope.dataTypes = ["String","int","Long"];

    $scope.init = function(){
        var promise = PlaceholderFactory.placeholders();

        promise.$promise
            .then(function(response){
                $scope.data.placeholders = response;
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

    $scope.editPlaceholder = function(placeholders){
        $scope.data.activePlaceholder = placeholders;
        $scope.switchTab(1);
    }

    $scope.cancelEditPlaceholder = function(){
        $scope.data.activePlaceholder = {};
        $scope.switchTab(0);
    }

    $scope.savePlaceholder = function(){
        PlaceholderFactory.saveplaceholder($scope.data.activePlaceholder).$promise.then(function(result){
           $scope.data.activePlaceholder = result;
           $scope.switchTab($scope.activeCrumbIndex-1);
           $scope.init();
        });
    }

    $scope.addPlaceholder = function(){
        $scope.data.activePlaceholder = {};
        $scope.switchTab(1);
    }

      $scope.openmodal = function (placeholder) {

      var ModalControler = function($uibModalInstance, $scope,placeholder){

         $scope.nodeIndex = 0;
         $scope.nodeMap = {};
         $scope.payloadData = [];

         $scope.initTreeView = function(){

             var traversRecursiveBuildKeys = function(payload, data,parentIndex){
                 for (var key in data) {
                   $scope.nodeIndex++
                   if (key == '$promise'){continue;}
                   if (key == '$resolved'){continue;}
                   if (data[key] && {}.toString.call(data[key]) === '[object Function]'){continue;}
                   console.log(key);
                   console.log(data[key]);

                   payload.push({'id':$scope.nodeIndex,'text': key,'parent':parentIndex ,'href': '#'+key, 'tags': [key],'nodes': []});
                   payloadItem = payload[payload.length-1];
                   $scope.nodeMap[$scope.nodeIndex]=payloadItem;

                     if (data[key] instanceof Array) {
                        data[key].forEach(function(currentValue,index,arr){
                          traversRecursiveBuildKeys(this,currentValue,$scope.nodeIndex);
                       },payloadItem.nodes);
                     }else if (data[key] instanceof Object) {
                        traversRecursiveBuildKeys(payloadItem.nodes,data[key],$scope.nodeIndex);
                     }

                 }
             }

             var getObjects = function(obj, key, val) {
                 var objects = [];
                 for (var i in obj) {
                     if (!obj.hasOwnProperty(i)) continue;
                     if (typeof obj[i] == 'object') {
                         objects = objects.concat(getObjects(obj[i], key, val));
                     } else if (i == key && obj[key] == val) {
                         objects.push(obj);
                     }
                 }
                 return objects;
             }



             var promise = PlaceholderFactory.dataContext();
             promise.$promise.then(function(result){
                $scope.payloadData = [];
                traversRecursiveBuildKeys($scope.payloadData,result);
                $scope.treeViewDataContext = true;
                 $('#treeViewDataContext').treeview({
                    levels: 1,
                    data: $scope.payloadData
                  });

             });

         }

         var buildNodePattern = function(node,result){
             if (result){
                result = "." + result;
             }else{
                 result='';
             }
             result = node.text + result;
             if (node.parent){
                 parentNode =  $scope.nodeMap[node.parent];
                 result = buildNodePattern(parentNode,result);
             }
             return result;
         }

       $scope.okModal = function () {

         var node = $('#treeViewDataContext').data().treeview.getSelected();
         var nodePattern = undefined;
         $scope.nodePattern = buildNodePattern(node[0],nodePattern);
         $uibModalInstance.close($scope.nodePattern);
       };

       $scope.cancelModal = function () {
         $uibModalInstance.dismiss('cancel');
       };

     }

          var modalInstance = $uibModal.open({
            animation: $scope.animationsEnabled,
            templateUrl: 'app/components/config/partials/templateplaceholder/placeholder-pattern-select.html',
            controller: ModalControler,
            resolve: {
                placeholder: function(){return placeholder;}
            }
          });

          modalInstance.result.then(function (selectedPattern) {
            placeholder.resolvePattern = selectedPattern;
          });

      };


    $scope.init();

}]);
