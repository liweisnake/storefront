package com.hp.sdf.ngp.dao;


import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.model.UserProfile;

@Component
public class UserProfileDAO extends JpaDao<String, UserProfile> {
	
}