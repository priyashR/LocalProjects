var app = angular.module('myapp', [] );

//object to be shared between controllers
app.factory('shared', function(){
  return {};
});

app.controller('userlistController', function( $scope, shared, $timeout) {

		var selectedUser = {};

		$scope.users = [
			{ name: 'Jackson Turner', street: '217 Tawny End', img: 'men_1.jpg' },
			{ name: 'Megan Perry', street: '77 Burning Ramp', img: 'women_1.jpg' },
			{ name: 'Ryan Harris', street: '12 Hazy Apple Route', img: 'men_2.jpg' },
			{ name: 'Jennifer Edwards', street: '33 Maple Drive', img: 'women_2.jpg' },
			{ name: 'Noah Jenkins', street: '423 Indian Pond Cape', img: 'men_3.jpg' }
		];

		$timeout(function(){
			$scope.select( $scope.users[ $scope.state.selectedUserIndex ] );
		});
  
     shared['list'] = true;
     $scope.shared = shared;

		$scope.select = function( user ) {
			selectedUser.isSelected = false;
			user.isSelected = true;
			selectedUser = user;
			$scope.container.extendState({ selectedUserIndex: $scope.users.indexOf( user ) });
			$scope.container.layoutManager.eventHub.emit( 'userSelected', user );
		};
	});


app.controller('userdetailsController', function( $scope, shared) {
	$scope.user = $scope.state.user || null;

 shared['details'] = true;
 $scope.shared = shared;

	$scope.container.layoutManager.eventHub.on( 'userSelected', function( user ){
		$scope.user = user;
		$scope.container.extendState({ user: user });
		$scope.$apply();
	});
});


var AngularModuleComponent = function( container, state ) {

  var injector = angular.element($('body')).injector();
  var $compile = injector.get('$compile');
  var $rootScope = injector.get('$rootScope');
  
  //create a new scope for the panel
  var $panelScope = $rootScope.$new();
  
  //add panel and state to new scope
  $panelScope.container = container;
  $panelScope.state = state;
  
  //get the template
	var html = $( '#' + state.templateId ).html(),
		element = container.getElement();
  
  //apply/compile template
	element.html(html); 
  $compile(element.contents())($panelScope)
};

var myLayout = new GoldenLayout({
	content:[{
		type: 'row',
		content: [{
			width: 20,
			title: 'Registered Users',
			type: 'component',
			componentName: 'angularModule',
			componentState: {
				module: 'myapp',
				templateId: 'userlistTemplate',
				selectedUserIndex: 2
			}
		},{
			type: 'component',
			title: 'Selected User',
			componentName: 'angularModule',
			componentState: {
				module: 'myapp',
				templateId: 'userDetailTemplate'
			}
		}]
	}]
});

myLayout.registerComponent( 'angularModule', AngularModuleComponent );
myLayout.init();