package com.hp.sdf.ngp.banner.dao;

import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.banner.model.RotatingBanner;
import com.hp.sdf.ngp.dao.JpaDao;

@Component
public class RotatingBannerDAO extends JpaDao<Long,RotatingBanner>{

}