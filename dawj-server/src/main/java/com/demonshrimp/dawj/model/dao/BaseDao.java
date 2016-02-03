package com.demonshrimp.dawj.model.dao;

import java.io.Serializable;
import java.util.Map;

import pers.ksy.common.orm.QueryCondition;
import pers.ksy.common.orm.hibernate4.HibernateBaseDAO;

public interface BaseDao<T, ID extends Serializable> extends HibernateBaseDAO<T, ID> {


}