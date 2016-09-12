package com.yc.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yc.biz.ClassSemesterBiz;
import com.yc.dao.BaseDao;
import com.yc.po.ClassSemester;
import com.yc.po.ExamineeClass;
import com.yc.webexam.actions.CourseAction;

@Service("classSemesterBiz")
public class ClassSemesterBizImpl implements ClassSemesterBiz {
    private static final Logger logger = Logger.getLogger(CourseAction.class);
    @Resource(name="baseDao")
    private BaseDao baseDao;
    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
    
    @Override
    public void addClassSemester(ClassSemester classSemester) {
        baseDao.add(classSemester);
    }

    @Override
    public void updateClassSemester(ClassSemester classSemester) {
        baseDao.update(classSemester);
    }

    @Override
    public List<ClassSemester> findClassSemesterByEC(String eid) {
        System.out.println("----------"+eid);
        String[] params=new String[]{eid};
        String sql = "from ClassSemester cs where cs.examineeClass.id=? order by id";
        
        List<ClassSemester> list=baseDao.search(sql,params);
        if( list!=null&&list.size()>0 ){    
            return list;
        }   
        return null;
    }

    @Override
    public ClassSemester findClassSemesterById(String id) {
        String[] params=new String[]{id};
        String sql = "from ClassSemester cs where cs.id=?";
        
        List<ClassSemester> list=baseDao.search(sql,params);
        if( list!=null&&list.size()>0 ){    
            return list.get(0);
        }   
        return null;
    }

}
