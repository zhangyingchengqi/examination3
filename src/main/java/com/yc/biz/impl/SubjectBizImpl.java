package com.yc.biz.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yc.biz.SubjectBiz;
import com.yc.dao.BaseDao;
import com.yc.po.Chapter;
import com.yc.po.Edition;
import com.yc.po.PointPaper;
import com.yc.po.Subject;
import com.yc.vo.SubjectPage;
@Service("subjectBiz")
public class SubjectBizImpl implements SubjectBiz {

	
	private BaseDao baseDao;
	@Resource(name = "baseDao")
	public void setBaseDao(BaseDao baseDao)
	{
		this.baseDao = baseDao;
	}
	
	
	@Override
	public String findSubjectName(int pid) {
		String sql="select subjectName from Subject where id=?";
		String[] params = new String[]{pid+""};
		List<String> list=baseDao.search(sql, params);
		return list!=null&&list.size()>0?list.get(0):null;
	}

	@Override
	public List<Subject> findSubjectByEndAnswer(int cid, String sid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List searchSubject(String semester) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateSubject(Subject subject) {
		try {
			baseDao.update(subject);
			return 1;
		} catch (Exception e) {
			return 0;
		}
		
		
	}

	@Override
	public int addSubject(Subject subject) {
		Serializable result=baseDao.add(subject);
		return (Integer) result;
	}

	@Override
	public List searchSubject(String semester, int editionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Subject> findSubject(String semester, int editionId) {
		List<Subject> list = new ArrayList();
		String sql="from Subject where semester=? and editionId=?";
		String[] params = new String[] { semester, editionId + "" };
		list=baseDao.search(sql, params);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public int getSubjectId(String subjectName) {
		String hql="  from Subject  sb  where  sb.subjectName= '"+subjectName +"'";
		List<Subject> subject=baseDao.search(hql);
		return subject.get(0).getId();
	}

	@Override
	public int getChapterCount(String subjectName) {
		
		return 0;
	}

	@Override
	public int getSubjectCount(int editionId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Subject> getSubjects() {
		String hql="from Subject ";
		List<Subject> subject=baseDao.search(hql);
		return subject;
	}

    @Override
    public List<Subject> getSubjectsById(String editionId,String semester) {
        String sql;
        String[] params;
        if(semester == null || "".equals(semester) || semester == "0" || "0".equals(semester)){
            sql="from Subject where editionId=?";
            params = new String[] { editionId };
        }else{
            sql="from Subject where editionId=? and semester=?";
            semester="S"+semester;
            params = new String[] { editionId,semester };
        }
        
        List<Subject> subject=baseDao.search(sql,params);
        if(subject!=null&&subject.size()>0){
            return subject;
        }
        return null;
    }


	@Override
	public int deleteSubject(int subjectId) {
		
		Subject subject=new Subject();
		subject.setId(subjectId);
		try {
			baseDao.del(subject);
			return 1;
		} catch (Exception e) {
			return 0;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override    //select s.id,s.subjectName,s.chapterCount,s.semester,e.id ,e.editionName  from Subject s,Edition e where e.id=s.editionId 
	public List<Subject> getAllSubject(SubjectPage subjectPage,Integer page, Integer rows) {
//		List<Subject> subject=(List<Subject>) baseDao.searchByPage(pageStart, pageSize, " from Subject");
//		return subject;
		List<Subject> subject=null;
		String hql="from Subject s";
		Map<String,Object> param=new HashMap<String,Object>();
		if(subjectPage!=null){
			hql+="  where  ";
			
			if(subjectPage.getEditionName()!=null && !"".equals(subjectPage.getEditionName().trim())){
				hql+=" s.edition.editionName like :editionName and ";
				param.put("editionName", "%"+subjectPage.getEditionName().trim()+"%");
			}
			
			if(subjectPage.getSemester()!=null && !"".equals(subjectPage.getSemester().trim())){
				hql+="  s.semester like :semester  and ";
				param.put("semester", "%"+subjectPage.getSemester().trim()+"%");
			}
			
			if(subjectPage.getSubjectName()!=null && !"".equals(subjectPage.getSubjectName().trim())){
				hql+=" s.subjectName like :subjectName and ";
				param.put("subjectName", "%"+subjectPage.getSubjectName().trim()+"%");
			}
			
			hql+=" 1=1";
		}
		
		if(page==null  ||  rows==null){
			subject = (List<Subject>) baseDao.searchByPro(hql, null, null,param);
		}else{
		
		int startPage=(page-1)*rows;
			subject = (List<Subject>) baseDao.searchByPro(hql, startPage, rows,param);
		}
		return subject;
	}

	@Override
	public int updateChapterCount(int chapterCount, int id) {
		String sql = "update Subject set chapterCount=:chapterCount where id=:id";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("chapterCount", chapterCount);
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

	public Subject findSubjectById(int subjectId)
	{
		String sql="from Subject where id=?";
		String[] params =new String[]{subjectId+""};
		List<Subject> list=baseDao.search(sql, params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Subject> getSubjectTotal(String semester, int cid) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getAllSubjectCount() {
		String hql = "select count(*)  from Subject ";
		
		int count=baseDao.searchCount(hql);
		return count;
	}


	@Override
	public List<String> getSubjectNamesBySemesterAndClassName(String semester,
			String className) {
		String[] params = new String[] { className,semester };
		String hql="select distinct p.subject.id ,p.subject.subjectName from  PointPaper p  where p.examineeClass.className=? and p.examineeClass.semester=?";
		List<String> list=this.baseDao.search(hql, params);
		if(list!=null&&list.size()>0){
		return list;
		}
		return null;
	}


	@Override
	public List<PointPaper> getPointPaperByIdAndClassName(Integer subjectId,
			String className) {
		String[] params = new String[] { className,subjectId+"" };
		String hql="from  PointPaper p where p.examineeClass.className=? and p.subject.id=?";
		List<PointPaper> list=this.baseDao.search(hql, params);
		if(list!=null&&list.size()>0){
			return list;
			}
		return null;
	}


	@Override
	public List<String> findSubjectNameBySemester(String semester) {
		String[] params = new String[] {semester };
		String hql="select s.id,s.subjectName from  Subject s where s.semester=?";
		List<String> list=this.baseDao.search(hql, params);
		if(list!=null&&list.size()>0){
			return list;
			}
		return null;
	}

}
