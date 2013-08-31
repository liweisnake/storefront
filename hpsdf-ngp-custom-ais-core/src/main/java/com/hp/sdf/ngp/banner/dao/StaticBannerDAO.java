package com.hp.sdf.ngp.banner.dao;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.banner.model.StaticBanner;
import com.hp.sdf.ngp.dao.JpaDao;

@Component
public class StaticBannerDAO extends JpaDao<Long,StaticBanner>{

}