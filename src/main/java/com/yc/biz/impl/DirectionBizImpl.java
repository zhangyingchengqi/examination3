package com.yc.biz.impl;


import java.util.ArrayList;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.stereotype.Service;

import com.yc.biz.DirectionBiz;
import com.yc.biz.EditionBiz;
import com.yc.dao.BaseDao;
import com.yc.po.Direction;
import com.yc.po.Edition;
import com.yc.webexam.actions.CourseAction;

@Service("directionBiz")
public class DirectionBizImpl implements DirectionBiz{

	private BaseDao baseDao;

	private static final Logger logger = Logger.getLogger(CourseAction.class);
	
	@Resource(name = "baseDao")
	public void setBaseDao(BaseDao baseDao)
	{
		this.baseDao = baseDao;
	}


	@Override
	public String getDirectionName()
	{
		Edition edition = (Edition) baseDao.get(Edition.class, 1);
		// return edition.getEditionName();
		return edition.toString();

	}



	@Override
	public List<Direction> searchAllDirection() {
		List<Direction> directions = (List<Direction>) baseDao.search("from Direction");
		return directions;
	}


	@Override
	public void updateDirection(Direction direction) {
		baseDao.update(direction);		
	}


	@Override
	public void deleteDirection(Direction direction) {
		baseDao.del(direction);
	}


	@Override
	public Serializable addDirection(Direction direction) {
		Serializable result=baseDao.add(direction);
		return result;
	}

	@Override
	public List searchDirection()
	{
		String sql = "select dname from Direction ";
		List<String> editionArr = (List<String>) baseDao.search(sql);
		if (editionArr != null && editionArr.size() > 0)
		{
			return editionArr;
		}
		else
		{
			return null;
		}
	}

	@Override
	public int getDirectionId(String dname)
	{
		int did = 0;
		String sql = "select id from Direction where dname=?";
		String[] params = new String[] { dname };
		List<Integer> list = (List<Integer>) baseDao.search(sql, params);
		if (list != null && list.size() > 0)
		{
			did = list.get(0);
		}
		return did;
	}


	@Override
	public List<Direction> getAllDirection() {
		String sql="from Direction";
		List <Direction> list=null;
		list=baseDao.search(sql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}


	@Override
	public String searchDirectionName(int id) {
		String dname="";
		String sql="select dname from Direction where did=?";
		int[] params = new int[] { id };
		List<String> list=baseDao.searchs(sql, params);
		
		if (list != null && list.size() > 0)
		{
			dname = list.get(0);
		}
		return dname;
		
	}


	@Override
	public void updateCurrentUse(int did) {
		Map<String,Object> params=new HashMap<String,Object>();
		String sql="update Direction set currentUse=0";
		baseDao.executeHql(sql, null);
		String hql="update Direction set currentUse=1 where did=:did";
		params.put("did", did);
		baseDao.executeHql(hql, params);
	}

}
