package com.yc.biz;

import java.util.Date;
import java.util.List;

import com.yc.po.CheckAnswer;
import com.yc.po.Checking;

public interface CheckAnswerBiz {

	/**
	 * 添加评教信息
	 * @param ck：要添加的评教记录对象
	 * @return
	 */
	public int addCheckAnswer(CheckAnswer checkAnswer);

	/**
	 * 根据评教编号查询评教记录
	 * @param checkId：要查询的评教编号
	 * @return
	 */
	public CheckAnswer findCheckAnswerById(int checkId);


	public List<CheckAnswer> findCheckAnswerByCid(String cid,String sys_id,String startdate,String enddate);
	
	public List<CheckAnswer> findCheckAnswerByEid(String cid,String eid,String startdate,String enddate);

    public List<CheckAnswer> findCheckAnswerByEidAndDate(String eid,String sys_id,String startdate,String enddate);

}
