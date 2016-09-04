package com.yc.biz;


import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;



import com.yc.po.Direction;
import com.yc.po.Edition;



import com.yc.po.Edition;
import com.yc.po.Subject;




import java.util.List;



public interface DirectionBiz {
	/**
	 * <p>
	 * Title:版本号DAO类 
	 * 
	 * @version 1.0
	 */
	public String getDirectionName();
	/**
	 * 得到所有的版本名称,存入集合中返回
	 * 
	 * @return ArrayList 版本名称集合
	 * @throws Exception
	 */
	public List searchDirection();
	/**
	 * 通过版本号得到版本ID
	 * 
	 * @param editionName
	 *          String
	 * @return int
	 * @throws Exception
	 */
	public int getDirectionId(String directionName);
	/**
	 * 得到所有的Edition放入ArrayList中，每个是一个Edition对象
	 */
	public List<Direction> getAllDirection();
	
	public List<Direction> searchAllDirection();
	
	public void updateDirection(Direction direction);
	
	public void deleteDirection(Direction direction);
	
	public Serializable addDirection(Direction direction);
	
	public String searchDirectionName(int id);
	
	public void updateCurrentUse(int id);
}
