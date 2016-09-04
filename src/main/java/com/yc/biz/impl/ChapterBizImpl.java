package com.yc.biz.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yc.biz.ChapterBiz;
import com.yc.dao.BaseDao;
import com.yc.po.Chapter;
import com.yc.po.Subject;
import com.yc.vo.ChapterPage;

@Service("chapterBiz")
public class ChapterBizImpl implements ChapterBiz {
	@Resource(name = "baseDao")
	private BaseDao baseDao;

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public List<Chapter> getChapterList(ChapterPage chapterPage,Integer page, Integer rows) {
		List<Chapter> chapter = null;
		
		String hql="from Chapter c";
		Map<String,Object> param=new HashMap<String,Object>();
		if(chapterPage!=null){
			hql+="  where  ";
			if(chapterPage.getChapterName()!=null && !"".equals(chapterPage.getChapterName().trim())){
				hql+="  c.chapterName like :chapterName   and";
				param.put("chapterName", "%"+chapterPage.getChapterName().trim()+"%");
			}
			
			if(chapterPage.getEditionName()!=null && !"".equals(chapterPage.getEditionName().trim())){
				hql+=" c.subject.edition.editionName like :editionName and ";
				param.put("editionName", "%"+chapterPage.getEditionName().trim()+"%");
			}
			
			if(chapterPage.getSemester()!=null && !"".equals(chapterPage.getSemester().trim())){
				hql+="  c.subject.semester like :semester  and ";
				param.put("semester", "%"+chapterPage.getSemester().trim()+"%");
			}
			
			if(chapterPage.getSubjectName()!=null && !"".equals(chapterPage.getSubjectName().trim())){
				hql+=" c.subject.subjectName like :subjectName and ";
				param.put("subjectName", "%"+chapterPage.getSubjectName().trim()+"%");
			}
			
			hql+=" 1=1";
		}


		if(page==null  ||  rows==null){
			chapter = (List<Chapter>) baseDao.searchByPro(hql, null, null,param);
		}else{
    		int startPage=(page-1)*rows;
    			chapter = (List<Chapter>) baseDao.searchByPro(hql, startPage, rows,param);
		}
		return chapter;
	}

	@Override
	public int getAllChapterCount(ChapterPage chapterPage) {
		String hql = "select count(*)  from Chapter c";

		int count = baseDao.searchCount(hql);
		return count;
	}

	@Override
	public List<String> searchChapter(int subjectId) {
		List<String> list = new ArrayList<String>();
		String sql = "select chapterName from Chapter where subjectId=?";
		String[] params = new String[] { subjectId + "" };
		list = (List<String>) baseDao.search(sql, params);
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	@Override
	public int getChapterId(int subjectId, String chapterName) {
		Number chapterID = 0;
		String sql = "select id from Chapter where subjectId =? and chapterName =?";
		String[] params = new String[] { subjectId + "", chapterName };
		List<Number> list = (List<Number>) baseDao.search(sql, params);
		if (list != null && list.size() > 0) {
			chapterID = list.get(0);
		}
		return (Integer) chapterID;
	}

	@Override
	public String getChapterName(int chapterId) {
		String sql = "select chapterName from Chapter where id=?";
		String[] params = new String[] { chapterId + "" };
		List<String> list = baseDao.search(sql, params);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public int getQuestionAmount(int chapterId) {
		long count = 0;
		String sql = "select count(*) from WritingQuestion where chapterId=?";
		String[] params = new String[] { chapterId + "" };
		List<Long> list = (List<Long>) baseDao.search(sql, params);
		if (list != null && list.size() > 0) {
			count = list.get(0);
		}
		return (int) count;
	}

	@Override
	public int getChapterCount(int subjectId) {
		String sql = "select count(*)  from Chapter c where c.subject.id= "
				+ subjectId;
		List<Long> list = baseDao.search(sql);
		long result = 0;
		if (list != null && list.size() > 0) {

			result = list.get(0);
			return (int) result;
		} else {
			return 0;
		}

	}

	@Override
	public int updateChapter(Chapter chapter) {
		try {
			baseDao.update(chapter);
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public int deleteChapter(int id) {
		String sql = "delete from Chapter where id=:id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		try {
			baseDao.executeHql(sql, params);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int addChapter(Chapter chapter) {
		Serializable result = baseDao.add(chapter);
		return (Integer) result;
	}

	@Override
	public int getSubjectId(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Chapter findChapterById(int chapterId) {
		String sql = "from Chapter where id= " + chapterId;
		List<Chapter> chapter = baseDao.search(sql);
		return chapter.get(0);
	}

	@Override
	public List<Chapter> findChapter(int subjectId) {
		String sql = "from Chapter where subjectId=?";
		String[] params = new String[] { subjectId + "" };
		List<Chapter> list = baseDao.search(sql, params);
		return list != null && list.size() > 0 ? list : null;
	}

}
