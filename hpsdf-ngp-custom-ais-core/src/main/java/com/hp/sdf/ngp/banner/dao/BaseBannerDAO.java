package com.hp.sdf.ngp.banner.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import org.hibernate.Hibernate;
import org.hibernate.type.NullableType;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.banner.QueryParam;
import com.hp.sdf.ngp.banner.model.BaseBanner;
import com.hp.sdf.ngp.dao.JpaDao;

@Component
public class BaseBannerDAO extends JpaDao<Long, BaseBanner> {

	public List<BaseBanner> find(final QueryParam qp, BaseBanner filter) {
		return buildFindQuery(qp, filter, false).list();
	}

	protected Query buildFindQuery(QueryParam qp, BaseBanner filter,
			boolean count) {
		HibernateContactFinderQueryBuilder builder = new HibernateContactFinderQueryBuilder();
		builder.setQueryParam(qp);
		builder.setFilter(filter);
		builder.setCount(count);
		Query query = getSession().createQuery(builder.buildHql());
		query.setParameters(builder.getParameters(), builder.getTypes());
		if (!count && qp != null) {
			query.setFirstResult(qp.getFirst()).setMaxResults(qp.getCount());
		}
		return query;
	}

	private class HibernateContactFinderQueryBuilder {
		private List<String> parameters;
		private List<NullableType> types;
		private boolean count;
		private BaseBanner filter = new BaseBanner();
		private QueryParam queryParam;

		public String buildHql() {
			parameters = new ArrayList<String>();
			types = new ArrayList<NullableType>();
			StringBuilder hql = new StringBuilder();
			addCountClause(hql);
			hql.append("from BaseBanner target where 1=1 ");
			addMatchingCondition(hql, filter.getName(), "name");
			Integer value = -1;
			if (filter.getStauts() == null
					|| !filter.getStauts().endsWith("all")) {
				if (filter.getBannerStatus() != null) {
					value = filter.getBannerStatus().ordinal();
				}
				addEqualCondition(hql, value, "bannerStatus");
			}
			
			value = -1;
			if (filter.getType() == null
					|| !filter.getType().endsWith("all")) {
				if (filter.getBannerType() != null) {
					value = filter.getBannerType().ordinal();
				}
				addEqualCondition(hql, value, "bannerType");
			}

			addOrderByClause(hql);
			return hql.toString();
		}

		private void addEqualCondition(StringBuilder hql, Integer value,
				String name) {
			if (value != -1) {
				hql.append("and target.");
				hql.append(name);
				hql.append(" = (?)");
				parameters.add(value.toString());
				types.add(Hibernate.STRING);
			}

		}

		private void addCountClause(StringBuilder hql) {
			if (count) {
				hql.append("select count(*) ");
			}
		}

		private void addMatchingCondition(StringBuilder hql, String value,
				String name) {
			if (value != null) {
				hql.append("and upper(target.");
				hql.append(name);
				hql.append(") like (?)");
				parameters.add("%" + value.toUpperCase() + "%");
				types.add(Hibernate.STRING);
			}
		}

		private void addOrderByClause(StringBuilder hql) {
			if (!count && queryParam != null && queryParam.hasSort()) {
				hql.append(" order by upper(target.");
				hql.append(queryParam.getSort());
				hql.append(") ");
				hql.append(queryParam.isSortAsc() ? "asc" : "desc");
			}
		}

		public void setQueryParam(QueryParam queryParam) {
			this.queryParam = queryParam;
		}

		public void setFilter(BaseBanner filter) {
			if (filter == null) {
				throw new IllegalArgumentException("Null value not allowed.");
			}
			this.filter = filter;
		}

		public void setCount(boolean count) {
			this.count = count;
		}

		public String[] getParameters() {
			return parameters.toArray(new String[0]);
		}

		public NullableType[] getTypes() {
			return types.toArray(new NullableType[0]);
		}
	}
}