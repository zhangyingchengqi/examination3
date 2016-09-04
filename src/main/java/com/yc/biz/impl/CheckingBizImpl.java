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

import com.yc.biz.CheckingBiz;
import com.yc.dao.BaseDao;
import com.yc.po.ADailyTalk;
import com.yc.po.Checking;

@Service("checkingBiz")
public class CheckingBizImpl implements CheckingBiz {
	
	private BaseDao baseDao;
	
	private Map<String, Object> param=new HashMap<String,Object>();

	@Resource(name = "baseDao")
	public void setBaseDao(BaseDao baseDao)
	{
		this.baseDao = baseDao;
	}

	@Override
	public int addCheckingInfo(Checking ck) {
		int result= (Integer) baseDao.add(ck);
		return result;
	}

	@Override
	public int updateCheckingInfo(Checking ck) {
		try {
			this.baseDao.update(ck);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	@Override
	public Checking findNewCheckingId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Checking findNewCheckingIdes(int checkId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Checking findCheckingInfoById(int checkId) {
		String sql=" from Checking c  where c.checkId= "+checkId;
		List<Checking> list=this.baseDao.search(sql);
		if(list!=null &&  list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}

	@Override
	public List<Checking> findCheckingByAll(String semester, int cid,
			String stuname, String startdate, String enddate, String datetime) {
		
		return null;
	}

	@Override
	public List<Integer> findCheckingAllCheckId(String semester, int cid,
			String stuname, String startdate, String enddate, String datetime)  {
		String sql="";
		try {
			sql="select checkId from Checking where checkStatus=1";
			if(semester!=null&&!"".equalsIgnoreCase(semester)){
				sql+=" and semester like '%"+semester+"%'";
			}
			if(cid>0){
				sql+=" and cid="+cid;
			}
			if(stuname!=null&&!"".equalsIgnoreCase(stuname)&&!"0".equalsIgnoreCase(stuname)){
				sql+=" and checkResult like '%"+stuname+"%'";
			}
			if(startdate!=null&&!"".equalsIgnoreCase(startdate)){
//				Date startdates=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startdate);
				sql+=" and checkDate>=  '"+startdate  +"'";  	// DATEDIFF('"+startdate+"',checkDate)>=0";
			}
			if(enddate!=null&&!"".equalsIgnoreCase(enddate)){
//				Date enddates=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(enddate);
				sql+=" and   checkDate <= '"+enddate +"'";	 //DATEDIFF(checkDate,'"+enddate+"')>=0";
			}
			if(datetime!=null&&!"".equalsIgnoreCase(datetime)&&!"0".equalsIgnoreCase(datetime)){
				sql+=" and rtrim(checkTime) like '"+datetime+"'";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return this.baseDao.search(sql);
	}

	/**
	 * 根据跟定的条件查询考勤信息（学生端）
	 * @param semester：学期
	 * @param startDate：查询开始时间
	 * @param endDate：查询结束时间
	 * @param dateTime：查询时间段
	 * @param uname：学生姓名
	 * @return
	 */
	@Override
	public List<Checking> findCheckingResullByStudentName(String semester,
			String startDate, String endDate, String time, String uname) {
		String sql="";
		List<Checking> list=null;
		if(semester==null||"".equals(semester)){
			if(time==null  ||  "".equals(time)){
				if(startDate == null || "".equals(startDate) && endDate == null  || "".equals(endDate)){
					sql=" from Checking c where c.checkResult like ?  ";
					String	param[]={"%"+uname+"%"};
					list=this.baseDao.search(sql, param);
				} else {
					sql=" from Checking c where c.checkResult like ?   and  checkDate BETWEEN ?  AND ?";
					String	param[]={"%"+uname+"%",startDate,endDate};
					list=this.baseDao.search(sql, param);
				}
			}else{
				if(startDate == null || "".equals(startDate) && endDate == null  || "".equals(endDate)){
					sql=" from Checking c where c.checkResult like ?   and  checkTime=? ";
					String	param[]={"%"+uname+"%",time};
					list=this.baseDao.search(sql, param);
				}else{
					sql=" from Checking c where c.checkResult like ? and checkTime=? and  checkDate BETWEEN ?  AND ?";
					String	param[]={"%"+uname+"%",time,startDate,endDate};
					list=this.baseDao.search(sql, param);
				}
				
			}
		}else{
			if(time==null  ||  "".equals(time)){
				if(startDate == null || "".equals(startDate) && endDate == null  || "".equals(endDate)){
					sql=" from Checking c where c.checkResult like ? and c.semester=? ";
					String	param[]={"%"+uname+"%",semester};
					list=this.baseDao.search(sql, param);
				} else {
					sql=" from Checking c where c.checkResult like ? and c.semester=?  and  checkDate BETWEEN ?  AND ?";
					String	param[]={"%"+uname+"%",semester,startDate,endDate};
					list=this.baseDao.search(sql, param);
				}
			}else{
				if(startDate == null || "".equals(startDate) && endDate == null  || "".equals(endDate)){
					sql=" from Checking c where c.checkResult like ?   and  checkTime=? and c.semester=?";
					String	param[]={"%"+uname+"%",time,semester};
					list=this.baseDao.search(sql, param);
				}else{
					sql=" from Checking c where c.checkResult like ? and checkTime=? and c.semester=? and  checkDate BETWEEN ?  AND ?";
					String	param[]={"%"+uname+"%",time,semester,startDate,endDate};
					list=this.baseDao.search(sql, param);
				}
				
			}
		}
		return list;
	}

	@Override
	public List<Checking> showCheckingResult(String semester, int cid,
			String startdate, String enddate, String datetime) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Checking findCheckingByClassIdAndDateTime(int cid, Date date,
			String time) {
		String sql=" from Checking c where c.examineeClass.id=? and c.checkDate=?  and c.checkTime=?";
		
		String param[]={cid+"",new SimpleDateFormat("yyyy-MM-dd").format(date),time};
//		param.put("id", cid);
//		param.put("checkDate", date);
//		param.put("checkTime", time);
		List<Checking> list=this.baseDao.search(sql, param);
		if(list!=null  &&  list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}

	@Override
	public List<Checking> findCheckingByClassIdAndUidAndTime(int cid,String time,
			String startDate,String endDate) {
		String sql="";
		List<Checking> list=null;
		
		if(time==null  ||  "".equals(time)){
			if(startDate == null || "".equals(startDate) && endDate == null  || "".equals(endDate)){
				sql=" from Checking c where c.examineeClass.id=?  order by checkDate DESC";
				String	param[]={cid+""};
				list=this.baseDao.search(sql, param);
			} else {
				sql=" from Checking c where c.examineeClass.id=?   and  checkDate BETWEEN ?  AND ? order by checkDate DESC";
				String	param[]={cid+"",startDate,endDate};
				list=this.baseDao.search(sql, param);
			}
		}else{
			if(startDate == null || "".equals(startDate) && endDate == null  || "".equals(endDate)){
				sql=" from Checking c where c.examineeClass.id=?   and  checkTime=?  order by checkDate DESC";
				String	param[]={cid+"",time};
				list=this.baseDao.search(sql, param);
			}else{
				sql=" from Checking c where c.examineeClass.id=? and checkTime=? and  checkDate BETWEEN ?  AND ? order by checkDate DESC";
				String	param[]={cid+"",time,startDate,endDate};
				list=this.baseDao.search(sql, param);
			}
			
		}
		

		return list;
	}

	@Override
	public List<Checking> findCheckByCheckid(int checkid) {
		String sql="from Checking ck  where checkStatus=1 and checkId =?";
		String param[]={checkid+""};
		List<Checking> list=this.baseDao.search(sql, param);
		if(list!=null  &&  list.size()>0){
			return list;
		}else{
			return null;
		}
	}

}
