package com.yc.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yc.biz.LabAnswerBiz;
import com.yc.dao.BaseDao;
import com.yc.po.LabAnswer;

@Service("labAnswerBiz")
public class LabAnswerBizImpl implements LabAnswerBiz {

	private BaseDao baseDao;

	@Resource(name = "baseDao")
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public int addLabAnswer(LabAnswer labAnswer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String searchProjectName(String paperId, String examineeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String searchCode(String paperId, String examineeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int exeUpdateAnswer(LabAnswer labAnswer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int searchMaxScore(String paperId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int searchMinScore(String paperId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int searchAvgScore(String paperId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isExamOfExaminee(String paperId, String examineeName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int insertAnswer(LabAnswer la) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List searchLabPaperExaminee(String paperId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LabAnswer searchExamineeLabPaper(String paperId, String examineeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String searchScorePercent(String paperId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteLabAnswerByExamineeName(String examineeName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<LabAnswer> getExamineeAllLabAnswer(String examineeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExistProjectName(String paperId, String examineeName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteLabAnswer(String paperId, String examineeName) {
		// TODO Auto-generated method stub
		return 0;
	}

}
