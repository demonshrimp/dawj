package com.demonshrimp.dawj.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.model.dao.BaseDao;
import com.demonshrimp.dawj.model.entity.BaseEntity;
import com.demonshrimp.dawj.service.BaseService;

import pers.ksy.common.ArrayUtil;
import pers.ksy.common.StringUtil;
import pers.ksy.common.model.Page;
import pers.ksy.common.orm.Order;
import pers.ksy.common.orm.QueryCondition;

@Transactional
public abstract class BaseServiceImpl<T extends BaseEntity, ID extends Serializable> implements BaseService<T, ID> {
	abstract protected BaseDao<T, ID> getDao();
	
	public T get(ID id) {
		return getDao().get(id);
	}
	public T load(ID id){
		T t =  getDao().load(id);
		if(t==null)
			throw new ServiceException("无法根据id="+id+" 获得一个实体！");
		return t;
	}

	public T save(T t) {
		t.setCreateTime(new Date());
		return getDao().save(t);
	}

	public Serializable saveAny(Serializable t) {
		return getDao().saveAny(t);
	}
	
	public T update(T entity) {
		return getDao().update(entity);
	}

	public T update(T entity, String... fieldNames) {
		entity.setLastModifyTime(new Date());
		String[] fields = new String[] { "lastModifyTime" };
		if (null != fieldNames) {
			fields = ArrayUtil.addAll(fields, fieldNames);
		}
		return getDao().update(entity, fieldNames);
	}

	public T update(T entity, boolean inclusive, String... fieldNames) {
		entity.setLastModifyTime(new Date());
		if (!inclusive) {
			String[] fields = new String[] { "id", "createTime" };
			if (null != fieldNames) {
				fields = ArrayUtil.addAll(fields, fieldNames);
			}
			fieldNames = fields;
		}
		return getDao().update(entity, inclusive, fieldNames);
	}

	public T saveOrUpdate(T entity) {
		return getDao().saveOrUpdate(entity);
	}
	 

	public T saveOrUpdate(T entity, String... fieldNames) {
		if (StringUtil.notEmpty(entity.getId())) {
			update(entity, fieldNames);
		} else {
			save(entity);
		}
		return entity;
	}

	public T delete(T entity) {
		return getDao().delete(entity);
	}

	public T deleteById(ID id) {
		return getDao().deleteById(id);
	}

	public void refresh(Object entity) {
		getDao().refresh(entity);
	}

	public Page<T> findByPage(int pageIndex, int pageSize, Order[] orders) {
		return getDao().findByPage(pageIndex, pageSize, orders);
	}

	public Page<T> findByPage(int pageIndex, int pageSize, Map<String, Object> eqConditions,
			Map<String, String> likeConditions, Order[] orders) {
		return getDao().findByPage(pageIndex, pageSize, eqConditions, likeConditions, orders);
	}

	public Page<T> findByPage(QueryCondition qc, int pageIndex, int pageSize) {
		return getDao().findByPage(qc, pageIndex, pageSize);
	}

	public QueryCondition getQC() {
		return getDao().getQC();
	}

	public List<T> findAll() {
		return getDao().findAll();
	}

	public List<T> findAll(String[] joins) {
		return getDao().findAll(joins);
	}

	public List<T> findByProperty(String propName, Object propVal) {
		return getDao().findByProperty(propName, propVal);
	}

	public List<T> findByProperty(String propName, Object propVal, String[] joins) {
		return getDao().findByProperty(propName, propVal, joins);
	}

	public List<T> findByProperty(String[] propNames, Object[] propVals, String[] joins) {
		return getDao().findByProperty(propNames, propVals, joins);
	}

	public T getByProperty(String propName, Object propVal) {
		return getDao().getByProperty(propName, propVal);
	}

	public T getByProperty(String propName, Object propVal, String[] joins) {
		return getDao().getByProperty(propName, propVal, joins);
	}

	public int updatePropertiesById(Map<String, Object> updateSet, ID id) {
		return getDao().updatePropertiesById(updateSet, id);
	}

	public int updateProperties(Map<String, Object> updateSet, Map<String, Object> updateWhere) {
		return getDao().updateProperties(updateSet, updateWhere);
	}

	public List listByQC(QueryCondition qc) {
		return getDao().listByQC(qc);
	}

	public List listByQC(QueryCondition qc, int first, int max) {
		return getDao().listByQC(qc, first, max);
	}

	public Object uniqueByQC(QueryCondition qc) {
		return getDao().uniqueByQC(qc);
	}

	public Long countByQC(QueryCondition qc) {
		return getDao().countByQC(qc);
	}

	public Long countByProperties(String[] propNames, Object[] propVals) {
		return getDao().countByProperties(propNames, propVals);
	}

	public void deleteAll() {
		getDao().deleteAll();
	}

	@Override
	public List<T> findList(QueryCondition qc) {
		return getDao().listByQC(qc);
	}

	@Override
	public List<T> findList(QueryCondition qc, int first, int max) {
		return getDao().listByQC(qc, first, max);
	}

	@Override
	public Object findOne(QueryCondition qc) {
		return getDao().uniqueByQC(qc);
	}

	@Override
	public Long count(QueryCondition qc) {
		return getDao().countByQC(qc);
	}
	
	@Override
	public int deleteByHQL(String hqlStr) {
		return getDao().deleteByHQL(hqlStr);		
	}
	
	public int deleteBySQL(String sql){
		return getDao().deleteBySQL(sql);
	}

	public int update(QueryCondition qc, Map<String, Object> properties) {
		return getDao().update(qc, properties);
	}

}
