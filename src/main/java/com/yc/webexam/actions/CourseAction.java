package com.yc.webexam.actions;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import com.yc.biz.EditionBiz;
import com.yc.biz.impl.EditionBizImpl;
import com.yc.po.Direction;
import com.yc.po.Edition;

public class CourseAction  extends ActionSupport implements ServletResponseAware,SessionAware{
	/**
	 * 
	 */

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CourseAction.class);

	private HttpServletResponse  response;
	private Map<String,Object> session;
	
	private PrintWriter out;
	
	@Resource(name="editionBiz")
	private EditionBiz editionBiz;
	
	private String result;
	
	private Edition edition=new Edition();
	 
	public void setEditionBiz(EditionBiz editionBiz) {
		this.editionBiz = editionBiz;
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
	public void edition(){
		try {
			out=response.getWriter();
		String did = ServletActionContext.getRequest().getParameter("did");
		List<Edition> currentUse=editionBiz.getEditionByDid(did);
		if(currentUse != null && currentUse.size() > 0){
		    for (int i = 0; i < currentUse.size(); i++) {
	            currentUse.get(i).setLabQuestions(null);
	            currentUse.get(i).setSubjects(null);
	        }
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
			int  id=Integer.parseInt(req.getParameter("id"));
			
			editionBiz.updateCurrentUse(id);
			out.print(1);
		} catch (Exception e) {
			e.printStackTrace();
			out.print(0);
		} 
		
		out.flush();
		out.close();
	}
	
	//添加版本
	public void addEdition() throws IOException{
		
		out=response.getWriter();
		HttpServletRequest req = ServletActionContext.getRequest();
		String  editionName=new String(req.getParameter("editionName").getBytes("iso-8859-1"),"UTF-8");
        Integer semesternum=Integer.parseInt(req.getParameter("semesternum"));
		int did=Integer.parseInt(req.getParameter("did"));
		
		Direction direction = new Direction();
		direction.setDid(did);
		
		edition.setEditionName(editionName);
		edition.setSemesternum(semesternum);
		edition.setDirection(direction);
		edition.setCurrentUse(0);
		
		int results=(Integer) editionBiz.addEdition(edition);
		
		if(results>0){
			out.print(1);
		}else{
			out.print(0);
		}
		
		
		out.flush();
		out.close();
		
	}
	
	public void upgradeCurrent(){
	    HttpServletRequest req = ServletActionContext.getRequest();
	    String  editionName = null;
	    try {
            editionName=new String(req.getParameter("oldename").getBytes("iso-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Integer semesternum=Integer.parseInt(req.getParameter("oldsnum"));
        int did=Integer.parseInt(req.getParameter("did"));
        
        String ename = "";
        //editionName
	}
	
	
	//更新当前版本
	public void updateCurrent(){
		HttpServletRequest req = ServletActionContext.getRequest();
		try {
			out = response.getWriter();
			
			Integer editionId = Integer.parseInt(req.getParameter("editionId"));
			String  editionName=req.getParameter("editionName");
	        Integer semesternum=Integer.parseInt(req.getParameter("semesternum"));
			Integer currentUse=Integer.parseInt(req.getParameter("currentUse"));
			
			edition.setId(editionId);
			edition.setEditionName(editionName);
	        edition.setSemesternum(semesternum);
			edition.setCurrentUse(currentUse);
						
			editionBiz.updateEdition(edition);
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
		Integer editionId = Integer.parseInt(req.getParameter("editionId"));
		
		edition.setId(editionId);
		
		try {
			out = response.getWriter();
			editionBiz.deleteEdition(edition);
			out.print(1);
		} catch (Exception e) {
			e.printStackTrace();
			out.print(0);
		}
		
		out.flush();
		out.close();
	}
}
