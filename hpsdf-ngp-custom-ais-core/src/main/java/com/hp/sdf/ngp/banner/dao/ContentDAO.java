package com.hp.sdf.ngp.banner.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.banner.BannerType;
import com.hp.sdf.ngp.banner.model.Content;
import com.hp.sdf.ngp.dao.JpaDao;

@Component
public class ContentDAO extends JpaDao<Long,Content>{

	public List<Content> findByBanner(Long bannerId, BannerType bannerType){
		Criterion criterion1 = Restrictions.eq("referenceId", bannerId);
		Criterion criterion2 = Restrictions.eq("bannerType", bannerType);
		Criterion[] criterions = new Criterion[]{criterion1,criterion2};
		List<Content> list = super.simpleFind(0, Integer.MAX_VALUE, criterions);
		return list;
	}
	
}