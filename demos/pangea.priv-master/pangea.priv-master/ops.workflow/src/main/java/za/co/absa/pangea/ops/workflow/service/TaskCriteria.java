package za.co.absa.pangea.ops.workflow.service;

import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.task.NativeTaskQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskCriteria {

	private Map<String, List<String>> processVariables;
	private List<String> searchVariables;
	private Map<String, List<String>> excludeVariables;
	private List<String> containedExecutionVariables;
	private List<String> excludedExecutionVariables;
	private List<String> groups;
	private boolean count;
	private int limit;
	private int offset;
	
	protected TaskCriteria() {
		groups = new ArrayList<>();
		processVariables = new HashMap<>();
		searchVariables = new ArrayList<>();
		excludeVariables = new HashMap<>();
		containedExecutionVariables = new ArrayList<>();
		excludedExecutionVariables = new ArrayList<>();
	}
	
	protected TaskCriteria addGroup(String group) {
		this.groups.add(group);
		return this;
	}
	
	protected TaskCriteria addProcessVariable(String name, String value) {
		List<String> values = this.processVariables.get(name);
		if (values == null) {
			values = new ArrayList<>();
		}
		values.add(value);
		this.processVariables.put(name, values);
		return this;
	}
	
	protected TaskCriteria excludeVariable(String name, String value) {
		List<String> values = this.excludeVariables.get(name);
		if (values == null) {
			values = new ArrayList<>();
		}
		values.add(value);
		this.excludeVariables.put(name, values);
		return this;
	}

	protected TaskCriteria containsExecutionVariable(String name) {
		this.containedExecutionVariables.add(name);
		return this;
	}
	
	protected TaskCriteria excludeExecutionVariable(String variableName) {
		this.excludedExecutionVariables.add(variableName);
		return this;
	}
	
	protected TaskCriteria addSearchValue(String value) {
		this.searchVariables.add(value);
		return this;
	}
	
	protected TaskCriteria count(boolean count) {
		this.count = count;
		return this;
	}
	
	protected TaskCriteria limit(int limit) {
		this.limit = limit;
		return this;
	}
	
	protected TaskCriteria offset(int offset) {
		this.offset = offset;
		return this;
	}
	
	public NativeTaskQuery buildQuery(ManagementService managementService, TaskService taskService) {
		NativeTaskQuery nativeQuery = taskService.createNativeTaskQuery();
		
		String taskTable = managementService.getTableName(TaskEntity.class);
		
		String identityLinkTable = managementService.getTableName(IdentityLinkEntity.class);
		
		String variableTable = managementService.getTableName(VariableInstanceEntity.class);

		String from = "select distinct _TASK.* ";
		if (count) {
			from = "select count(distinct _TASK.*) ";
		}
		
		String sql = from + createQueryBody(taskTable, identityLinkTable, variableTable);
	
		if (!count) {
			sql = sql +
		             " order by _TASK.CREATE_TIME_ desc ";
		}
		
		if (!count && limit > 0) {
			sql = sql + " LIMIT " +  limit + " OFFSET " + offset;
		}
		
		nativeQuery.sql(sql);

		return nativeQuery;
	}
	
	protected String createQueryBody(String taskTable, String identityLinkTable, String variableTable) {
		StringBuilder result = new StringBuilder();
		result.append("  from " + taskTable + " _TASK");
		if (this.groups != null && !this.groups.isEmpty()) {
			result.append(", " + identityLinkTable + " _LINK ");
		}
		result.append(", " + variableTable +  " _VAR ");
		result.append(" where ");
		if (this.groups != null && !this.groups.isEmpty()) {
			result.append(" _TASK.ID_ = _LINK.TASK_ID_ ");
			result.append(" AND _LINK.TYPE_ = 'candidate' ");
			result.append("   and _LINK.GROUP_ID_ IN " + createDelimitedString(this.groups));
		}
		if (!processVariables.isEmpty()) {
			result.append(" AND _VAR.PROC_INST_ID_ = _TASK.PROC_INST_ID_ ");
			result.append(variablesMatching(processVariables,"_VAR",false));
		}
		if (!searchVariables.isEmpty()) {
			result.append(" and exists(select 1 from  ACT_RU_VARIABLE _VAR3	where _VAR3.PROC_INST_ID_ = _TASK.PROC_INST_ID_ ");
			result.append(variablesLike(searchVariables,"_VAR3",true));
			result.append(" ) ");
		}
		if (!excludeVariables.isEmpty()) {
			result.append(" AND ");
			result.append(variablesNotMatching(variableTable));
		}
		if (!containedExecutionVariables.isEmpty()) {
			result.append(" AND ");
			result.append(executionVariablesContaining(variableTable));
		}
		if (!excludedExecutionVariables.isEmpty()) {
			result.append(" AND ");
			result.append(excludeExecutionVariables(variableTable));
		}
		return  result.toString();
	}
	
	private String variablesMatching(Map<String,List<String>> map,String tableAlias,boolean useLike) {
		if (map.isEmpty()) {
			return "";
		}
		StringBuilder result = new StringBuilder(" /*JL4UE*/ AND (");
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			result.append("(");
			if (useLike){
				result.append(matchLikeVariableClause(tableAlias, entry.getKey(), entry.getValue()));
			}else{
				result.append(matchVariableClause(tableAlias, entry.getKey(), entry.getValue()));
			}
			result.append(")");
			result.append(" OR ");
		}
		result.replace(result.length() - 4, result.length(), "");
		result.append(")");
		return result.toString();
	}

	private String variablesLike(List<String> variables,String tableAlias,boolean useLike) {
		if (variables.isEmpty()) {
			return "";
		}
		StringBuilder result = new StringBuilder(" AND (");
		for (String var : variables) {
			result.append("(");
			result.append(tableAlias + ".TEXT_ LIKE '%" + var + "%'");
			result.append(")");
			result.append(" OR ");
		}
		result.replace(result.length() - 4, result.length(), "");
		result.append(")");
		return result.toString();
	}

	private String variablesNotMatching(String variableTable) {
		if (excludeVariables.isEmpty()) {
			return "";
		}
		StringBuilder result = new StringBuilder("NOT EXISTS (SELECT 1 FROM " + variableTable + " _VAR2 WHERE _VAR2.PROC_INST_ID_ = _TASK.PROC_INST_ID_ and (");
		for (Map.Entry<String, List<String>> entry : excludeVariables.entrySet()) {
			result.append("(");
			result.append(matchVariableClause("_VAR2", entry.getKey(), entry.getValue()));
			result.append(")");
			result.append(" OR ");
		}
		result.replace(result.length() - 5, result.length(), "");
		result.append(")))");
		return result.toString();
	}
	
	private String matchLikeVariableClause(String alias, String varName, List<String> values) {
		StringBuilder res = new StringBuilder();
		boolean isFirst = true; 
		for (String value : values) {
			if (!isFirst){
				res.append(" OR ");
			}
			isFirst = false;
			res.append(alias + ".NAME_ = '" + varName + "' and " + alias + ".TEXT_ LIKE ");
			res.append("'%");
			res.append(value);
			res.append("%'");
		}
		return  res.toString();
	}
	
	private String matchVariableClause(String alias, String varName, List<String> values) {
		return alias + ".NAME_ = '" + varName + "' and " + alias + ".TEXT_ IN " + createDelimitedString(values);
	}
	
	private String executionVariablesContaining(String variableTable) {
		StringBuilder result = new StringBuilder("EXISTS (SELECT 1 FROM " + variableTable + " _VAR2 WHERE _VAR2.EXECUTION_ID_ = _TASK.EXECUTION_ID_ AND (");
		for (String varname : containedExecutionVariables) {
			result.append("(");
			result.append("_VAR2.NAME_ = '");
			result.append(varname);
			result.append("')");
			result.append(" OR ");
		}
		result.replace(result.length() - 4, result.length(), "");
		result.append("))");
		return result.toString();
	}
	
	private String excludeExecutionVariables(String variableTable) {
		StringBuilder result = new StringBuilder("NOT EXISTS (SELECT 1 FROM " + variableTable + " _VAR2 WHERE _VAR2.EXECUTION_ID_ = _TASK.EXECUTION_ID_ AND (");
		for (String varname : excludedExecutionVariables) {
			result.append("(");
			result.append("_VAR2.NAME_ = '");
			result.append(varname);
			result.append("')");
			result.append(" OR ");
		}
		result.replace(result.length() - 4, result.length(), "");
		result.append("))");
		return result.toString();
	}

	private String createDelimitedLikeString(List<String> list) {
		StringBuilder res = new StringBuilder();
		res.append("(");
		for (String value : list) {
			res.append("'");
			res.append(value);
			res.append("',");
		}
		res.deleteCharAt(res.length() - 1);
		res.append(")");
		return res.toString();
	}
	
	private String createDelimitedString(List<String> list) {
		StringBuilder res = new StringBuilder();
		res.append("(");
		for (String value : list) {
			res.append("'");
			res.append(value);
			res.append("',");
		}
		res.deleteCharAt(res.length() - 1);
		res.append(")");
		return res.toString();
	}
	
	public void addGroups(List<String> groups2) {
		this.groups.addAll(groups2);
	}
	
}
