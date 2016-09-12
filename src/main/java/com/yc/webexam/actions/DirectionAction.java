package com.yc.webexam.actions;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;




import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import com.yc.biz.DirectionBiz;
import com.yc.biz.EditionBiz;
import com.yc.biz.impl.EditionBizImpl;
import com.yc.po.Direction;
import com.yc.po.Edition;

public class DirectionAction  extends ActionSupport implements ServletResponseAware,SessionAware{
	/**
	 * 
	 */

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DirectionAction.class);

	private HttpServletResponse  response;
	private Map<String,Object> session;
	
	private PrintWriter out;
	
	@Resource(name="directionBiz")
	private DirectionBiz directionBiz;
	
	private String result;
	
	private Direction direction=new Direction();
	 
	public void setEditionBiz(DirectionBiz directionBiz) {
		this.directionBiz = directionBiz;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	
	//显示版本
	public void direction(){
		try {
			out=response.getWriter();
		List<Direction> currentUse=directionBiz.searchAllDirection();
		for (int i = 0; i < currentUse.size(); i++) {
			//currentUse.get(i).setEditions(null);
		}
		
		result = JSON.toJSONString(currentUse); 
		
		out.print(result);
		out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//更改当前正在使用的版本
	public void changeCurrentUse(){
		try {
			out=response.getWriter();
			HttpServletRequest req = ServletActionContext.getRequest();
			int did=Integer.parseInt(req.getParameter("did"));
			
			directionBiz.updateCurrentUse(did);
			out.print(1);
		} catch (Exception e) {
			e.printStackTrace();
			out.print(0);
		} 
		
		out.flush();
		out.close();
	}
	
	//添加版本
	public void addDirection() throws IOException{
		
		out=response.getWriter();
		HttpServletRequest req = ServletActionContext.getRequest();
		String  dname = new String(req.getParameter("dname").getBytes("iso-8859-1"),"utf-8");
        String  cname = new String(req.getParameter("cname").getBytes("iso-8859-1"),"utf-8");
        String  remark = new String(req.getParameter("remark").getBytes("iso-8859-1"),"utf-8");
		
		direction.setDname(dname);
        direction.setClassname(cname);
		direction.setRemark(remark);
		direction.setCurrentUse(0);
		
		int results=(Integer) directionBiz.addDirection(direction);
		
		if(results>0){
			out.print(1);
		}else{
			out.print(0);
		}
		out.flush();
		out.close();
	}
	
	//更新当前版本
	public void updateDirection(){
		HttpServletRequest req = ServletActionContext.getRequest();
		try {
			out = response.getWriter();
			
			Integer did = Integer.parseInt(req.getParameter("did"));
	        String  dname = new String(req.getParameter("dname").getBytes("iso-8859-1"),"utf-8");
	        String  cname = new String(req.getParameter("cname").getBytes("iso-8859-1"),"utf-8");
			Integer currentUse=Integer.parseInt(req.getParameter("currentUse"));
			
			direction.setDid(did);
			direction.setDname(dname);
	        direction.setClassname(cname);
			direction.setCurrentUse(currentUse);
						
			directionBiz.updateDirection(direction);
			out.print(1);
			
		} catch (Exception e) {
			e.printStackTrace();
			out.print(0);
		}	 
		out.flush();
		out.close();
	}
	
	//删除版本
	public void deleteEdition(){
		HttpServletRequest req = ServletActionContext.getRequest();
		Integer did = Integer.parseInt(req.getParameter("did"));
		
		direction.setDid(did);
		
		try {
			out = response.getWriter();
			directionBiz.deleteDirection(direction);
			out.print(1);
		} catch (Exception e) {
			e.printStackTrace();
			out.print(0);
		}
		
		out.flush();
		out.close();
	}
}
