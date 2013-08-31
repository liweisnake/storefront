package com.hp.sdf.ngp.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.model.Asset;

@Component
public class AssetDAO extends JpaDao<Long, Asset> {

	public List complexFind() {
		Criteria c = createCriteria();

		c.createAlias("assetCategoryRelations", "assetCategoryRelations")
				.createAlias("assetCategoryRelations.category", "category")
				.add(
						Restrictions.eq("category.name", "game"));
		c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List list = c.list();
		return list;
	}

}