package com.yc.biz;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.util.List;

import com.yc.po.ExamineeClass;


public interface ExamineeClassBiz {

	/**
	 * 通过班级名称得到班级编号
	 * 
	 * @param className
	 *          String 班级名称
	 * @return int 班级编号
	 * @throws Exception
	 */
	public int getClassIdOfname(String className);
	/**
	 * 通过班级名称查询出对应的人数
	 * 
	 * @param className
	 *          String 班级名称
	 * @return int 查到的人数存入整型变量返回
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public int searchExamineeCount(String className);
	/**
	 * 通过学期查询出对应的考生班级
	 * 
	 * @param semester
	 *          String 学期名称
	 * @return ArrayList 查到的班级存入集合返回
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public List<String> searchExamineeClassName(String semester);
	/**
	 *根据教员姓名查询教员信息
	 * @param uname：教员姓名
	 * @return
	 */
	public List<ExamineeClass> findExamineeClassBySemester(String semester);

    public List<ExamineeClass> findExamineeClassByEid(String eid);
    
	public int findExamineeClassByDid(String did);
	/**

	 * 查询所有班级的信息
	 * @return
	 */
	public List<ExamineeClass> findAllExamineeClass();
	/**

	 *根据教员姓名查询班级的名字和id
	 * @param semester：学期
	 * @return
	 */
	public List<String> findExamineeClassAndClassIdBySemester(String semester);
	/**

	 * 查询所以班级的名字
	 * @param semester
	 * @return
	 */
	public List<String> searchAllExamineeClassName();



	
	/**
	 * 查找所有的ExamineeClass
	 * @return
	 */
	public List<ExamineeClass> findAllClass();
	
	/**
	 * 通过ID找出对应的班级名称
	 * 
	 * @param id
	 * @return
	 */
	public String getClassNameById(int id);
	public ExamineeClass getIdByName(String Name);
	/**
	 * 根据班级ID  查出班级信息
	 * @param id
	 * @return
	 */
	public ExamineeClass findExamineeClassById(Integer id);
	/**
	 * 更新班级信息
	 * @param examineeClass
	 * @return
	 */
	public void updateExamineeClass(ExamineeClass examineeClass);
	/**
	 * 新增班级
	 * @param examineeClass
	 */
	public void addExamineeClass(ExamineeClass examineeClass);
	/**
	 * 通过班级编号查询出考生的数量
	 * classId  班级的id
	 * @param classId
	 * @return
	 */
	public int searchExamineeCount(Integer  classId);
	/**
	 * 查询所有的班级的名称和它的id
	 * @return
	 */
	public List<String> searchAllExamineeClassNameAndId();
	
	/**
	 * 根据班级编号删除班级
	 */
	public Integer deleteExaminee(Integer id);

}
