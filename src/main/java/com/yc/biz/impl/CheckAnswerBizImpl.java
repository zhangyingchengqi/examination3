package com.yc.biz.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yc.biz.CheckAnswerBiz;
import com.yc.biz.CheckingBiz;
import com.yc.dao.BaseDao;
import com.yc.po.ADailyTalk;
import com.yc.po.CheckAnswer;
import com.yc.po.Checking;

@Service("checkAnswerBiz")
public class CheckAnswerBizImpl implements CheckAnswerBiz {
	
	private BaseDao baseDao;
	
	private Map<String, Object> param=new HashMap<String,Object>();

	@Resource(name = "baseDao")
	public void setBaseDao(BaseDao baseDao)
	{
		this.baseDao = baseDao;
	}


    @Override
    public int addCheckAnswer(CheckAnswer checkAnswer) {
        int result= (Integer) baseDao.add(checkAnswer);
        return result;
    }

    @Override
    public CheckAnswer findCheckAnswerById(int checkId) {
        String sql=" from CheckAnswer ca  where ca.Id= "+checkId;
        List<CheckAnswer> list=this.baseDao.search(sql);
        if(list!=null &&  list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<CheckAnswer> findCheckAnswerByCid(String cid, String sys_id, String startdate, String enddate) {
        String sql="";
        List<CheckAnswer> list=null;

        sql=" from CheckAnswer c where c.ecid=? and c.systemUser.id=? and checkDate BETWEEN ? AND ? order by Id DESC";
        String  param[]={cid,sys_id,startdate,enddate};
        list=this.baseDao.search(sql, param);
        
        return list;
    }

    @Override
    public List<CheckAnswer> findCheckAnswerByEid(String cid, String eid, String startdate, String enddate) {
        String sql="";
        List<CheckAnswer> list=null;

        sql=" from CheckAnswer c where c.ecid=? and c.examinee.id=? and checkDate BETWEEN ? AND ? order by Id DESC";
        String  param[]={cid,eid,startdate,enddate};
        list=this.baseDao.search(sql, param);
        
        return list;
    }
    
    @Override
    public List<CheckAnswer> findCheckAnswerByEidAndDate(String eid, String sys_id, String startdate, String enddate) {
        String sql="";
        List<CheckAnswer> list=null;

        sql=" from CheckAnswer c where c.examinee.id=? and c.systemUser.id=? and checkDate BETWEEN ? AND ? order by Id DESC";
        String  param[]={eid,sys_id,startdate,enddate};
        list=this.baseDao.search(sql, param);
        if(list.size() > 0){
            return list;
        }
        return null;
    }

}
