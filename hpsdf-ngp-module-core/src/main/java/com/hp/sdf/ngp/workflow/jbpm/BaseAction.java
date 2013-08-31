package com.hp.sdf.ngp.workflow.jbpm;

import java.text.SimpleDateFormat;

import com.hp.sdf.ngp.model.AssetLifecycleAction;

/**
 * function as passed-role configuration<br>
 * <custom expr="#{managerApproveAction}" name="managerApprove"><br>
 * <field name="passRoles"><string value="Tester,Developer"/></field><br>
 * <transition name="toGrant" to="grantPrivilege" g="-23,-18"/><br>
 * </custom>
 */
public abstract class BaseAction {

	protected SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");

	protected String passRoles = "";// for userLifecycleAction, do_not_pass as
	// default

	protected Boolean passThisState = false;// for assetLifecycleAction,
	// do_not_pass as default

	protected Boolean logNeeded = true;// no_need_log as default

	protected String desc = "";

	/**
	 * role index in the passRoles config<br>
	 * 
	 * @param role
	 * @return true->pass
	 */
	protected boolean passThisRole(String role) {
		if (null == role)
			return false;

		return passRoles.indexOf(role) > -1;
	}

	/**
	 * set description for each action,used as description for this action while
	 * log to db like this format<br>
	 * {ownerid||submiterid} {desc} {date}
	 */
	protected abstract void setDesc();

	protected void setDesc(AssetLifecycleAction cycleAction, String stepRest) {
		if (null == cycleAction.getDescription())
			cycleAction.setDescription(stepRest);
		else
			cycleAction.setDescription(cycleAction.getDescription() + stepRest);
	}

}
