package com.yc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.yc.vo.Page;

public interface BaseDao<T>
{

	/**
	 * 向当前Session添加对象，并返回一个序列化对象
	 * 
	 * @param object
	 *            要添加的对象，也是即将序列化的对象
	 * @return 序列化对象
	 */
	public Serializable add(T t);
	
	public void addList(List<T> ts);

	/**
	 * 从Session中删除反射类中序列化号为id的对象
	 * 
	 * @param clss
	 * @param id
	 */
	public void del(T t);

	/**
	 * 从Session中更新指定类object
	 * 
	 * @param object
	 */
	public void update(T t);
	
	public void saveOrUpdate(T t);

	/**
	 * 根据反射类从Session中得到该类序列化号为id的对象
	 * 
	 * @param clss
	 * @param id
	 * @return
	 */
	public T get(Class<T> clss, Serializable id);

	/**
	 * 根据HQL语句查询
	 * 
	 * @param hql
	 * @param params
	 * @return
	 */
	public List<T> search(String hql, String... params);
	
	public List<T> searchs(String hql, int... params);
	/**
	 * 根据HQL语句查询
	 * 
	 * @param hql
	 * @param list
	 * @return
	 */
	public List<T> searchByList(String hql, List list,String listName);

	/**
	 * 分页查询
	 * 
	 * @param pageStart
	 *            起始位置
	 * @param pageSize
	 *            页面大小
	 * @param hqlString
	 *            hql语句
	 * @return List
	 */
	public List<T> searchByPage(int pageStart, int pageSize, String hqlString);
	/**
	 * 分页查询,用page来存查出的结果
	 * @param queryHql
	 * @param queryCountHql
	 * @param page 一个javabean 用来存结果
	 * @param params
	 */
	public void showPage(String queryHql, String queryCountHql, Page<T> page, String... params);
	
	public Integer executeHql(String hql,Map<String,Object> params);
	 
	public Integer executeSql(String sql,Map<String,Object> params);

	 /*
	  * 查询数据的条数
	  */
	 public int searchCount(String  hql);

	 
	 
	 public List<T> findByProperty(String hql, Map<String, Object> param,Integer offset, Integer length);


	 /**
		 * 专用于每日一讲中文件的上传
		 * @param f：要上传的文件
		 * @param id：对应的知识点编号
		 * @return
		 */
	 public int uploadKnowledgeInfo(byte[] bt,int id,String filetype);
	 
	 public List<T> excutePro(String hql, Object... params);

	 /**
	  * easyui 表格分页
	  * @param hql ：hql语句
	  * @param param ：参数
	  * @param offset ：开始条数
	  * @param length：
	  * @param sort ：排序字段名·
	  * @param order ：排序方式
	  * @return
	  */
	 public List<T> findByProperty(String hql,Map<String, Object> param, Integer offset,
				Integer length, String sort, String order);

	 public List<T> searchByPro(String hql,Integer offset,Integer length, Map<String,Object> params);
	 
	 public List<T> searchByfpc(String hql, Integer offset,Integer length,String... params);

}
