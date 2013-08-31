package com.hp.sdf.ngp.workflow.jbpm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.api.ProcessInstance;
import org.jbpm.api.task.Task;

public class JbpmHelper {
	/**
	 * @param deployName
	 * @param userId
	 * @return
	 */
	public static String genInstanceId(String deployName, String userId,
			String targetRole) {
		if (deployName == null || null == userId)
			return null;
		return deployName + "_" + targetRole + "_" + userId;
	}

	/**
	 * @param instanceId
	 * @return
	 */
	public static String getUserIdByInstance(String instanceId) {
		if (instanceId == null || instanceId.equals(""))
			return null;

		return instanceId.substring(instanceId.lastIndexOf("_"));
	}

	/**
	 * check if one userRole promotion is ended <br>
	 * if ended ,delete the instance in the execution table<br>
	 * called from UserInfoPanel for checking display finish or waiting info for
	 * each rolePromotion flow
	 * 
	 * @param deployName
	 * @param userId
	 * @param targetRole
	 * @return true->finished <br>
	 *         false->processing<br>
	 *         null->no request<br>
	 */
	public static Boolean checkPromotionStatus(String deployName,
			String userId, String targetRole) {
		String insId = genInstanceId(deployName, userId, targetRole);
		return checkPromotionStatus(insId);
	}

	public static Boolean checkPromotionStatus(String insId) {
		ProcessInstance ins = JbpmServiceHolder.executionService
				.createProcessInstanceQuery().processInstanceKey(insId)
				.uniqueResult();
		if (ins == null) {
			return null;
		} else if (ins.isEnded()) {
			JbpmServiceHolder.executionService
					.deleteProcessInstanceCascade(insId);
			return true;
		} else
			return false;
	}

	/**
	 * get promotion tasks for the manager
	 * 
	 * @param managerName
	 * @return
	 */
	public static List<Task> getPromotons(String managerName) {
		return JbpmServiceHolder.taskService.findPersonalTasks(managerName
				+ "_" + "userPromotion");
	}

	/**
	 * get PromotionRequest param
	 * 
	 * @param taskId
	 * @return
	 */
	public static PromotionRequest getPromotonsotionRequest(String taskId) {
		return (PromotionRequest) JbpmServiceHolder.taskService.getVariable(
				taskId, "request");
	}

	/**
	 * approve the promotion task
	 * 
	 * @param taskId
	 * @param approved
	 *            {support 'true' yet }
	 */
	public static void approvePromotion(String taskId, Boolean approved) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("approveResult", approved);
		JbpmServiceHolder.taskService.setVariables(taskId, variables);
		JbpmServiceHolder.taskService.completeTask(taskId);
	}
}
