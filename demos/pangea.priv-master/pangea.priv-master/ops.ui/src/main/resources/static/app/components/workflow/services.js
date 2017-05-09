(function(){
	'use strict';

	angular
		.module('pangea')
		.factory('WorkflowService', WorkflowService)
		.factory('ActivitiService', ActivitiService);
	
	WorkflowService.$inject = ['ActivitiService', 'config'];
	ActivitiService.$inject = ['$resource', 'config'];
	
	function WorkflowService(ActivitiService, config) {
		var manager = {	
			get : function(id, handler) {
				return ActivitiService.runtimeTasks.get({taskId : id}, handler);
			},
			attachmentUrl: function(attachment) {
				return config.workflowServicesUrl + '/attachments/' + attachment.id;
			},
			listNewTasks: function(handler) {
				return ActivitiService.runtimeTasks.query({unassigned : true}, handler);
			},
			listTasks: function(_status, _size, _startIndex,_searchFilter, handler) {
				return ActivitiService.internalQueryTasks.list({status : _status, size : _size, startIndex : _startIndex,searchFilter:_searchFilter}, handler);
			},
			listAttachments: function(task, handler) {
				return ActivitiService.processAttachments.query({processId : task.processInstanceId}, handler);			
			},
			escelations:  ActivitiService.escelations,
			readVariable: function(task, variableName, handler) {
				return ActivitiService.taskVariables.get({taskId : task.id, variable : variableName}, handler, handler);
			},
			readVariables: function(task, handler) {
				//JHP - inject the task into the result.
				var promise = ActivitiService.taskVariables.query({taskId : task.id}, handler, handler)
				promise.$promise.then(function(result){
					result.task = task;
					return result;
				});
				return promise;
			},
			readForm: function(task, handler) {
				return ActivitiService.formTasks.query({taskId : task.id}, handler);
			},
			selectVariable: function(variables, name) {
				for (var index in variables) {
					if (variables[index].name === name) {
						return variables[index];
					}
				}
			},
			claim: function(task, handler) {
				var payload = { "action" : "claim", "assignee" : auth.profile.username};
				ActivitiService.runtimeTasks.claim({taskId: task.id}, payload);
			},
			forceClaim: function(task, handler) {
				var payload = { "action" : "claim", "assignee" : null};
				ActivitiService.runtimeTasks.claim({taskId: task.id}, payload, function() {
					payload = { "action" : "claim", "assignee" : auth.profile.username};
					ActivitiService.runtimeTasks.claim({taskId: task.id}, payload, function(_result) {
						handler.call(this, task);
					});
				});
			},
			complete: function(task, status, handler) {
				var payload = {
						  "action" : "complete",
						  "assignee" : auth.profile.username,
						  "variables" : [{"name" : "status", "value" : status, "type" : "string"}]
					};
				ActivitiService.runtimeTasks.complete({taskId: task.id}, payload).$promise.then(handler);			
			},
			completeAndClaimNext: function(task, status, handler) {
				manager.complete(task, status, function() {
					var results = ActivitiService.queryTasks.list({processInstanceId:task.processInstanceId}, function() {
						if (results.data.length > 0) {
							var _task = results.data[0];
							var payload = { "action" : "claim", "assignee" : auth.profile.username};
							ActivitiService.runtimeTasks.claim({taskId: _task.id}, payload).$promise.then(function() {
								handler.call(this, _task);
							});
						}
					});
				});
			},
			update : function(task, payload, handler) {
				return ActivitiService.runtimeTasks.update({taskId : task.id}, payload);
			},
			updateAttachment : function(attachment, handler) {
				return ActivitiService.updateAttachments.save({id : attachment.id}, attachment, handler);
			},
			updateVariables : function(task, payload) {
				return ActivitiService.taskVariables.update({taskId : task.id}, payload);
			},
			startProcess : function() {
				ActivitiService.processStarter.create();
			},
			startProcessSwift : function() {
				ActivitiService.processSwiftStarter.create();
			},
			readUserSettings : function() {
				return ActivitiService.userSettings.get();
			}
		};

		return manager;
	};
	
	function ActivitiService($resource, config) {
		var service = {
				runtimeTasks: $resource(config.workflowServicesUrl + '/runtime/tasks/:taskId', {taskId:'@id'}, {
					query : { method : 'GET', isArray : false, withCredentials : true },
					get : { method : 'GET', isArray : false, withCredentials : true },
					claim : { method : 'POST', withCredentials : true },
					complete : { method : 'POST', withCredentials : true },
					update : { method : 'PUT', withCredentials : true }
				}),
				queryTasks: $resource(config.workflowServicesUrl + '/query/tasks', {}, {
					list : { method : 'POST', isArray : false, withCredentials : true }
				}),
				taskVariables: $resource(config.workflowServicesUrl + '/runtime/tasks/:taskId/variables/:variable', {taskId : '@id', variable : '@variable'}, {
					get : { method : 'GET', isArray : false, withCredentials : true },
					query : { method : 'GET', isArray : true, withCredentials : true },
					update : { method : 'PUT', isArray : true, withCredentials : true }
				}),
				taskAttachments: $resource(config.workflowServicesUrl + '/runtime/tasks/:taskId/attachments', {taskId : '@id'}, {
					query : { method : 'GET', isArray : true, withCredentials : true }
				}),
				processAttachments: $resource(config.workflowServicesUrl + '/processes/:processId/attachments', {processId : '@id'}, {
					query : { method : 'GET', isArray : true, withCredentials : true }
				}),
				updateAttachments: $resource(config.workflowServicesUrl + '/attachments/:id', {id : '@id'}, {
					save : { method : 'POST', withCredentials : true }
				}),
				internalQueryTasks: $resource(config.workflowServicesUrl + '/custom/query/tasks', {}, {
					list : { method : 'GET', isArray : false, withCredentials : true }
				}),
				formTasks: $resource(config.workflowServicesUrl + '/form/form-data', {taskId : '@id'}, {
					query : { method : 'GET', isArray : false, withCredentials : true, params : {taskId : '@id'} }
				}),
				escelations: $resource(config.workflowServicesUrl + '/process/:id/escalations', {id : '@id'}, {}),
				processStarter: $resource(config.workflowServicesUrl + '/custom/dummy/case', {}, {
					create : { method : 'POST', isArray : false, withCredentials : true }
				}),
				processSwiftStarter: $resource(config.workflowServicesUrl + '/custom/dummy/swift', {}, {
					create : { method : 'POST', isArray : false, withCredentials : true }
				}),
				variableHistory: $resource(config.workflowServicesUrl + '/history/historic-variable-instances', {}, {
					query : { method : 'GET', isArray : false, withCredentials : true, params : { processInstanceId : '@processInstanceId', variableName : '@variableName' } }
				}),
				userSettings: $resource(config.workflowServicesUrl + '/user/settings', {}, { })
		}
		
		return service;
	};
	
})();

