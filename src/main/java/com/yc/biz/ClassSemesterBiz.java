package com.yc.biz;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

import com.yc.po.ClassSemester;
import com.yc.po.ExamineeClass;


public interface ClassSemesterBiz {

    /**
     * 新增班级的学期信息
     * @param examineeClass
     */
    public void addClassSemester(ClassSemester classSemester);

    /**
     * 更新班级的学期信息
     * @param examineeClass
     * @return
     */
    public void updateClassSemester(ClassSemester classSemester);


    /**
     *根据班级查学期信息
     * @param uname：教员姓名
     * @return
     */
    public List<ClassSemester> findClassSemesterByEC(String ECid);
    
}
