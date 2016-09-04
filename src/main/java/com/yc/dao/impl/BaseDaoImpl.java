package com.yc.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.yc.dao.BaseDao;
import com.yc.vo.Page;

/**
 * 利用的是原生Hibernate API操作数据库
 * 
 * @author dsp
 */

@Repository("baseDao")
public class BaseDaoImpl<T> implements BaseDao<T>
{

	private SessionFactory sessionFactory;

	@Resource(name = "sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	public Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	public Serializable add(T t)
	{
		Serializable serial = getSession().save(t);
		return serial;
	}
	
	public void addList(List<T> ts)
    {
            
        Session session = sessionFactory.openSession();  
        Transaction tx = session.beginTransaction();  
        for (T t : ts) {
            session.save(t);
        }
        tx.commit();  
    }

	public void del(T t)
	{
		getSession().delete(t);
	}

	public void update(T t)
	{
		getSession().update(t);
	}
	
	public void saveOrUpdate(T t)
	{
		getSession().saveOrUpdate(t);
	}

	public T get(Class<T> clss, Serializable id)
	{
		T t = (T) getSession().get(clss, id);
		return t;
	}

	public List<T> search(String hql, String... params)
	{
		Query query = getSession().createQuery(hql);
		
		if (params.length > 0)
		{
			for (int i = 0; i < params.length; ++i)
			{
				query.setString(i, params[i]);
			}
		}
		return query.list();
	}
	
	public List<T> searchByPro(String hql,Integer offset,Integer length, Map<String,Object> params)
	{
		Query query = getSession().createQuery(hql);
		if(params!=null&&params.size()>0){
			for(Map.Entry<String, Object> entry:params.entrySet()){
				query.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}
		if(offset!=null) {
			query.setFirstResult(offset).setMaxResults(length);
		}
		return query.list();
	}
	
	public List<T> searchByfpc(String hql,Integer offset,Integer length, String... params)
	{
		Query query = getSession().createQuery(hql);
		if (params.length > 0)
		{
			for (int i = 0; i < params.length; ++i)
			{
				query.setString(i, params[i]);
			}
		}
		if(offset!=null) {
			query.setFirstResult(offset).setMaxResults(length);
		}
		return query.list();
	}
	
	@Override
	public List<T> searchs(String hql, int... params) {
		Query query = getSession().createQuery(hql);
		if (params.length > 0)
		{
			for (int i = 0; i < params.length; ++i)
			{
				query.setInteger(i, params[i]);
			}
		}
		return query.list();
	}

	
	public List<T> searchByList(String hql, List list,String listName)
	{
		Query query = getSession().createQuery(hql);
		if (list.size() > 0)
		{
			query.setParameterList(listName, list);
		}
		return query.list();
	}
	@Override
	public List<T> searchByPage(int pageStart, int pageSize, String hqlString)
	{
		Query query = getSession().createQuery(hqlString);
		query.setFirstResult(pageStart);
		query.setMaxResults(pageSize);
		return query.list();
	}

	@Override
	public void showPage(String queryHql, String queryCountHql, Page<T> page, String... params)
	{

		Query queryAll = getSession().createQuery(queryHql);
		if (params.length > 0)
		{
			for (int i = 0; i < params.length; ++i)
			{
				String param = params[i];
				queryAll.setString(i, param);
			}
		}
		page.setResult(queryAll.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list());
		Query queryAllCount = getSession().createQuery(queryCountHql);
		if (params.length > 0)
		{
			for (int i = 0; i < params.length; ++i)
			{
				String param = params[i];
				queryAllCount.setString(i, param);
			}
		}
		page.setTotalsCount(Integer.parseInt(queryAllCount.setMaxResults(1).uniqueResult().toString()));
	}
	
	public Integer executeHql(String hql,Map<String,Object> params){
		Query q=this.getSession().createQuery(hql);
		if(params!=null&&params.size()>0){
			for(Map.Entry<String, Object> entry:params.entrySet()){
				q.setParameter(entry.getKey(), entry.getValue());
			}
		}
		int a = q.executeUpdate();
		return a;
	}
	
	public Integer executeSql(String sql,Map<String,Object> params){
		Query q=this.getSession().createSQLQuery(sql);
		if(params!=null&&params.size()>0){
			for(Map.Entry<String, Object> entry:params.entrySet()){
				q.setParameter(entry.getKey(), entry.getValue());
			}
		}
		int a = q.executeUpdate();
		return a;
	}

	@Override
	public int searchCount(String hql) {
        Query query =  getSession().createQuery(hql);
        int count = ((Long) query.iterate().next()).intValue();
		return count;
	}
	
	
	public List<T> findByProperty(String hql, Map<String, Object> param,
			Integer offset, Integer length) {
		Query queryObject = getSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (Map.Entry<String, Object> entry : param.entrySet()) {
				queryObject.setParameter(entry.getKey(), entry.getValue()
						.toString());
			}
		}
		if (offset != null && length != null) {
			queryObject.setFirstResult(offset).setMaxResults(length);
		}

		return queryObject.list();
	}

	@Override
	public int uploadKnowledgeInfo(byte[] bt, int id, String filetype) {
		String sql="update ADailyTalk set datainfo=:bt,remark=:filetype where id=:id";
		Map<String, Object> param=new HashMap<String,Object>();
		param.put("bt", bt);
		param.put("filetype", filetype);
		param.put("id", id);
		Query q=this.getSession().createQuery(sql);
		for(Map.Entry<String, Object> entry:param.entrySet()){
			q.setParameter(entry.getKey(), entry.getValue());
		}
		return q.executeUpdate();
	}

	@Override
	public List<T> excutePro(String hql, Object... params) {
		SQLQuery query = getSession().createSQLQuery(hql); 
		 
		if (params.length > 0)
		{
			for (int i = 0; i < params.length; ++i)
			{
				Object param = params[i];
				query.setString(i, (String)param);
			}
		}
		query.executeUpdate();
		 return null;
	}

	public List<T> findByProperty(String hql,Map<String, Object> param, Integer offset,
			Integer length, String sort, String order) {
		Query queryObject=getSession().createQuery(hql);
		if(param!=null && param.size()>0){
			for(Map.Entry<String, Object> entry:param.entrySet()){
				queryObject.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}
		if(offset!=null){
			queryObject.setFirstResult(offset).setMaxResults(length);
		}
		
		if(sort!=null && order!=null){
			hql+=" order by "+sort+" "+order;
		}
		List list=queryObject.list();
		return queryObject.list();
	}
}
