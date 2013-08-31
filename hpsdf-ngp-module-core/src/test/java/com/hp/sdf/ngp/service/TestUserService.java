/*
 * Copyright (c) 2009 Hewlett-Packard Company, All Rights Reserved.
 *
 * RESTRICTED RIGHTS LEGEND Use, duplication, or disclosure by the U.S.
 * Government is subject to restrictions as set forth in sub-paragraph
 * (c)(1)(ii) of the Rights in Technical Data and Computer Software
 * clause in DFARS 252.227-7013.
 *
 * Hewlett-Packard Company
 * 3000 Hanover Street
 * Palo Alto, CA 94304 U.S.A.
 * Rights for non-DOD U.S. Government Departments and Agencies are as
 * set forth in FAR 52.227-19(c)(1,2).
 */
package com.hp.sdf.ngp.service;

import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hp.sdf.ngp.DBEnablerTestBase;
import com.hp.sdf.ngp.api.search.SearchExpression;
import com.hp.sdf.ngp.api.search.Condition.StringComparer;
import com.hp.sdf.ngp.eav.EavRepository;
import com.hp.sdf.ngp.eav.model.AttributeValue;
import com.hp.sdf.ngp.model.RoleCategory;
import com.hp.sdf.ngp.model.UserLifecycleAction;
import com.hp.sdf.ngp.model.UserProfile;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionOwneridCondition;
import com.hp.sdf.ngp.search.condition.userlifecycleaction.UserLifecycleActionUseridCondition;
import com.hp.sdf.ngp.search.condition.userprofile.UserProfileUseridCondition;
import com.hp.sdf.ngp.search.engine.SearchExpressionImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:META-INF/applicationContext-test.xml" })
public class TestUserService extends DBEnablerTestBase {

	@Resource
	private UserService userService;
	
	@Resource
	private InfoService infoService;

    @Resource
    private EavRepository eavRepository;

	@Override
	public String dataSetFileName() {
		return "/data_init.xml";
	}
//
//	@Test
//	public void testAssignRole() throws Exception {
//		String userName = "levi";
//		long roleId = 4L;
//		userService.assignRole(userName, roleId);
//		ITable tableValue = databaseTester.getConnection().createQueryTable(
//				"result",
//				"select * from userrolecategory where userrolecategory.USERID = '"
//						+ userName + "' and userrolecategory.ROLEID = 4");
//		Assert.assertTrue(tableValue.getRowCount() == 1);
//	}
//
//
//	@Test
//	public void testSaveRole() throws Exception {
//		RoleCategory role = new RoleCategory();
//		role.setRoleName("female");
//		userService.saveRole(role);
//
//		ITable tableValue = databaseTester.getConnection().createQueryTable(
//				"result",
//				"select * from rolecategory where rolecategory.roleName = 'female'");
//		Assert.assertTrue(tableValue.getRowCount() == 1);
//	}
//
	@Test
	public void testSaveUser() throws Exception {
		UserProfile userProfile = new UserProfile("levi1", "wei.li20@hp.com");
		userService.saveUser(userProfile);
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from userprofile where userprofile.userid = 'levi1'");
		Assert.assertTrue(tableValue.getRowCount() == 1);
	}
	
	@Test
	public void testSaveUserWithPassword() throws Exception {
		UserProfile user = new UserProfile("levi1", "wei.li20@hp.com");
		user.setPassword("passwd");
		userService.saveUser(user);
		userService.updatePassword(user.getUserid(), "passwd");
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from userprofile where userprofile.userid = 'levi1'");
		
		Assert.assertTrue(tableValue.getRowCount() == 1);
		
		List<AttributeValue> values = eavRepository.getEntityAttributes(user.getEntityId(), "SYNCFLG");
		Float syncFlg = (Float) values.get(0).getValue();
		Assert.assertEquals(syncFlg, new Float(0.0f));
		
		values = eavRepository.getEntityAttributes(user.getEntityId(), "TEMPPASSWORD");
		String pwd = (String) values.get(0).getValue();
		Assert.assertEquals(pwd, "passwd");
	}
//
//	@Test
//	public void testDeleteUser() throws Exception {
//		userService.deleteUser("zhenyu");
//		ITable tableValue = databaseTester.getConnection().createQueryTable(
//				"result",
//				"select * from userprofile where userprofile.userid = 'zhenyu'");
//		Assert.assertTrue(tableValue.getRowCount() == 0);
//	}
//
//	@Test
//	public void testGetCountrys() {
//		List<Country> list = infoService.getCountrys();
//		Assert.assertTrue(list.size() == 4);
//	}
//
//	@Test
//	public void testGetLanguages() {
//		List<Language> list = infoService.getLanguages();
//		Assert.assertTrue(list.size() == 4);
//	}
//
//	@Test
//	public void testGetRole() {
//		RoleCategory role = userService.getRole(1L);
//		Assert.assertTrue(role.getRoleName().equals("Developer"));
//	}
//
//	@Test
//	public void testGetRoleByName() {
//		RoleCategory role = userService.getRoleByName("Tester");
////		System.out.println(role.size());
//		Assert.assertTrue(role != null);
//	}
//
//	@Test
//	public void testGetRoleCategoryByUserId() {
//		List<RoleCategory> roles = userService.getRoleCategoryByUserId("levi");
//		Assert.assertTrue(roles.size() == 1);
//	}
//
//	@Test
//	public void testGetUser() {
//		UserProfile up = userService.getUser("levi");
//		Assert.assertTrue(up.getEmail().endsWith("wei.li20@hp.com"));
//	}
//
//	
//
//	@Test
//	public void testUpdateRole() throws Exception {
//		RoleCategory role = roleDao.findById(2L);
//		role.setRoleName("female");
//		userService.updateRole(role);
//		ITable tableValue = databaseTester
//				.getConnection()
//				.createQueryTable("result",
//						"select * from rolecategory where rolecategory.rolename = 'female'");
//		Assert.assertTrue(tableValue.getRowCount() > 0);
//	}
//
//	@Test
//	public void testUpdateUser() throws Exception {
//		UserProfile up = userDao.findById("levi");
//		up.setEmail("test@hp.com");
//		userService.updateUser(up);
//		ITable tableValue = databaseTester
//				.getConnection()
//				.createQueryTable("result",
//						"select * from userprofile where userprofile.email = 'test@hp.com'");
//		Assert.assertTrue(tableValue.getRowCount() > 0);
//	}
//
//	
//	
//	
//	
//	@Test
//	public void testAssignRole2() throws Exception {
//		String userName = "levi";
//		String roleName = "aaa";
//		userService.assignRole(userName, roleName);
//		ITable tableValue = databaseTester.getConnection().createQueryTable(
//				"result",
//				"select * from rolecategory where rolecategory.ROLENAME = '"
//						+ roleName + "'");
//		Assert.assertTrue(tableValue.getRowCount() == 1);
//	}
//	
//	@Test
//	public void testRemoveRole() throws Exception{
//		userService.removeRole("levi", 1L);
//		ITable tableValue = databaseTester.getConnection().createQueryTable(
//				"result",
//				"select * from userrolecategory where userrolecategory.ID = 1");
//		Assert.assertTrue(tableValue.getRowCount() == 0);
//	}
//	
//	@Test
//	public void testGetAllUserLifecycleAction() throws Exception{
//		List<UserLifecycleAction> userLifecycleAction=infoService.getAllUserLifecycleAction(0, Integer.MAX_VALUE);
//		Assert.assertTrue(userLifecycleAction.size() == 2);
//	}
//	
//	@Test
//	public void testGetAllUserLifecycleActionCount() throws Exception{
//		long result=infoService.getAllUserLifecycleActionCount();
//		Assert.assertTrue(result == 2);
//	}
//	
	@Test
	public void testGetUserLifecycleAction() throws Exception{
		SearchExpression searchExpression=new SearchExpressionImpl();
		searchExpression.addCondition(new UserLifecycleActionUseridCondition("1", StringComparer.EQUAL, false, false));
		searchExpression.addCondition(new UserLifecycleActionOwneridCondition("levi", StringComparer.EQUAL, false, false));
		List<UserLifecycleAction> userLifecycleAction=infoService.getUserLifecycleAction(searchExpression);
		Assert.assertTrue(userLifecycleAction.size() == 1);
	}
	
	@Test
	public void testGetUserLifecycleActionByUserIdAndRoleName() throws Exception{
		UserLifecycleAction userLifecycleAction=infoService.getUserLifecycleActionByUserIdAndRoleName("1", "user", "developer");
		Assert.assertTrue(userLifecycleAction!=null);
	}
	
	@Test
	public void testRemoveUserLifecycleAction() throws Exception{
		infoService.removeUserLifecycleAction(1L);
		
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from userlifecycleaction where id = 1");
		Assert.assertTrue(tableValue.getRowCount() == 0);
	}
	
	@Test
	public void testSaveOrUpdateUserLifecycleAction() throws Exception{
		UserLifecycleAction userLifecycleAction=new UserLifecycleAction();
		userLifecycleAction.setEvent("test");
		userLifecycleAction.setPreRole("user");
		userLifecycleAction.setPostRole("user");
		userLifecycleAction.setOwnerid("liuchao");
		userLifecycleAction.setUserid("liuchao");
		userLifecycleAction.setSubmitterid("liuchao");
		infoService.saveOrUpdateUserLifecycleAction(userLifecycleAction);
		
		ITable tableValue = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from userlifecycleaction where ownerid= 'liuchao'");
		Assert.assertTrue(tableValue.getRowCount() == 1);
		
		userLifecycleAction.setId(2L);
		
		infoService.saveOrUpdateUserLifecycleAction(userLifecycleAction);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from userlifecycleaction where id=2 and ownerid= 'liuchao'");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}
	
	@Test
	public void testSearchUserProfile() throws Exception{
		SearchExpressionImpl searchExpression=new SearchExpressionImpl();
		searchExpression.addCondition(new UserProfileUseridCondition("levi",StringComparer.EQUAL,false,false));
		
		List<UserProfile> userProfiles=userService.searchUserProfile(searchExpression);
		
		Assert.assertTrue(userProfiles.size() == 1);
	}
	
	@Test
	public void testCountUserProfile() throws Exception{
		SearchExpressionImpl searchExpression=new SearchExpressionImpl();
		searchExpression.addCondition(new UserProfileUseridCondition("levi",StringComparer.EQUAL,false,false));
		int count=userService.countUserProfile(searchExpression);
		
		Assert.assertTrue(count == 1);
	}
	
	
	@Test
	public void testGetAllRoles(){
		List<RoleCategory> roles=userService.getAllRoles();
		Assert.assertTrue(roles.size()==6);
	}
	
	@Test
	public void testDisableUser() throws Exception{
		this.userService.disableUser("levi", true);
		ITable tableValue2 = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from userprofile where userid='levi' and disabled= 1");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
		this.userService.disableUser("levi", false);
		tableValue2 = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from userprofile where userid='levi' and disabled= 1");
		Assert.assertTrue(tableValue2.getRowCount() == 0);
		tableValue2 = databaseTester.getConnection().createQueryTable(
				"result",
				"select * from userprofile where userid='levi' and disabled=0");
		Assert.assertTrue(tableValue2.getRowCount() == 1);
	}
	
}

// $Id$