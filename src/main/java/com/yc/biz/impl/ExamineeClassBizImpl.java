package com.yc.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yc.biz.ExamineeClassBiz;
import com.yc.dao.BaseDao;
import com.yc.po.ExamineeClass;

@Service("examineeClassBiz")
public class ExamineeClassBizImpl implements ExamineeClassBiz {
	@Resource(name="baseDao")
	private BaseDao baseDao;
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public int getClassIdOfname(String className) {
		String[] params=new String[]{className};
		int classID = 0;
		String sql = "select e.id from ExamineeClass e where e.className=?";
		List list=(List) this.baseDao.search(sql,params);
		if( list!=null&&list.size()>0 ){
			classID= (Integer)  (      list.get(0) );
		}		
		return classID;
	}

	@Override
	public int searchExamineeCount(String className) {
		//未实现   数据库中没有studentCount字段
//		int studentCount = 0;
//		String sql="select studentCount from ExamineeClass where className =?";
//		String[] params=new String[]{className};
//		List<Integer> list=(List<Integer>) baseDao.search(sql, params);
//		if(list!=null&&list.size()>0){
//			studentCount=list.get(0);
//		}
//		return studentCount;
		return 0;
	}

	@Override
	public List<String> searchExamineeClassName(String semester) {
		List<String> list = new ArrayList<String>();
		//按开班时间的降序排序，后开的班显示在前面
		String sql="select className from ExamineeClass where semester = ? order by createDate desc";
		String[] params=new String[]{semester};
		list=(List<String>) baseDao.search(sql, params);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	public List<ExamineeClass> findExamineeClassBySemester(String semester) {
		String[] params=new String[]{semester};
		String sql = "from ExamineeClass e where e.semester=? order by id desc";
		List<ExamineeClass> list=(List<ExamineeClass>) this.baseDao.search(sql,params);
		if( list!=null&&list.size()>0 ){	
			return list;
		}	
		return null;
	}
	public List<String> findExamineeClassAndClassIdBySemester(String semester) {
		String[] params=new String[]{semester};
		String sql = "select e.className,e.id from ExamineeClass e where e.semester=? order by createDate desc";
		List<String> list=(List<String>) this.baseDao.search(sql,params);
		if( list!=null&&list.size()>0 ){	
			return list;
		}	
		return null;
	}

	@Override
	public List<String> searchAllExamineeClassName() {
		List<String> list = new ArrayList<String>();
		//按开班时间的降序排序，后开的班显示在前面
		String sql="select className from ExamineeClass  order by createDate desc";
		list=(List<String>) baseDao.search(sql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}
	@Override
	public List<String> searchAllExamineeClassNameAndId() {
		List<String> list = new ArrayList<String>();
		//按开班时间的降序排序，后开的班显示在前面
		String sql="select e.id,e.className from ExamineeClass e order by e.createDate desc";
		list=(List<String>) baseDao.search(sql);
		if(list!=null&&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public ExamineeClass findExamineeClassById(Integer cid) {
		String sql="from ExamineeClass where id=?";
		String[] params=new String[]{cid+""};
		List<ExamineeClass> list=baseDao.search(sql, params);
		if(list!=null&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ExamineeClass> findAllExamineeClass() {
		String sql="from ExamineeClass order by id desc";
		String[] params=new String[]{};
		List<ExamineeClass> list=baseDao.search(sql, params);
		if(list!=null&list.size()>0){
			return list;
		}
		return null;
	}

	@Override
	public List<ExamineeClass> findAllClass() {
		String sql="from ExamineeClass";
		List<ExamineeClass> list=this.baseDao.findByProperty(sql, null, null, null);
		return list;
	}


	/**
	 * 通过ID找出对应的班级名称
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public String getClassNameById(int id) {
		String[] params=new String[]{id+""};
		String sql = "select className from ExamineeClass where id=?";
		List<String> list=(List<String>) this.baseDao.search(sql,params);
		return list.get(0);
	}

	@Override
    public ExamineeClass getIdByName(String name) {
        String[] params=new String[]{name};
        String sql = "from ExamineeClass where className=?";
        List<ExamineeClass> list=(List<ExamineeClass>) this.baseDao.search(sql,params);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

	@Override
	public void updateExamineeClass(ExamineeClass examineeClass) {
		baseDao.update(examineeClass);
		
	}

	@Override
	public void addExamineeClass(ExamineeClass examineeClass) {
		baseDao.add(examineeClass);
		
	}

	@Override
	public int searchExamineeCount(Integer classId) {
		if(classId!=null){
			String hql=" select count(*)  from Examinee  where classId ="+classId;
			int count=baseDao.searchCount(hql);
			
			return count;
		}
		
		
		return 0;
	}
	@Override
	public Integer deleteExaminee(Integer classId){
		String hql= "  delete  from  ExamineeClass where id="+classId;
		
		Integer result=baseDao.executeSql(hql, null);
		
		if(result>0){
			return 1;
		}else{
			return 0;
		}
	}

    @Override
    public List<ExamineeClass> findExamineeClassByEid(String eid) {
        String[] params=new String[]{eid};
        String sql = "from ExamineeClass e where e.eid=? order by id desc";
        List<ExamineeClass> list=(List<ExamineeClass>) this.baseDao.search(sql,params);
        if( list!=null&&list.size()>0 ){    
            return list;
        }   
        return null;
    }
    
    @Override
    public int findExamineeClassByDid(String did) {
        String[] params=new String[]{did};
        int classID = 0;
        String sql = "from examineeclass ec left join edition e on ec.eid = e.id where e.did= ?";
        List list=(List) this.baseDao.search(sql,params);
        if( list!=null&&list.size()>0 ){
            classID= list.size();
        }       
        return classID;
    }

}
