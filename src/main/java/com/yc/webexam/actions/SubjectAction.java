package com.yc.webexam.actions;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ModelDriven;
import com.yc.biz.EditionBiz;
import com.yc.biz.SubjectBiz;
import com.yc.po.Edition;
import com.yc.po.Subject;
import com.yc.utils.JsonProtocol;
import com.yc.utils.JsonUtil;
import com.yc.vo.ChapterPage;
import com.yc.vo.SubjectPage;

public class SubjectAction extends BaseAction implements ServletResponseAware,SessionAware ,ModelDriven<SubjectPage>{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SubjectAction.class);

	private HttpServletResponse response;
	
	@Resource(name="subjectBiz")
	private SubjectBiz subjectBiz;
	
	@Resource(name="editionBiz")
	private EditionBiz editionBiz;
	
	private String semester;
	
	
	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}


	HttpServletRequest req = ServletActionContext.getRequest();
	
	private String jsonStr=null;
	
	private SubjectPage sb=new SubjectPage();
	JsonProtocol jsonProtocol=new JsonProtocol();
	
	public void setSubjectBiz(SubjectBiz subjectBiz) {
		this.subjectBiz = subjectBiz;
	}
	
	public void setEditionBiz(EditionBiz editionBiz) {
		this.editionBiz = editionBiz;
	}


	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response=response;
	}
	
	public void setSb(SubjectPage sb) {
		this.sb = sb;
	}
	
	public SubjectPage getSb() {
		return sb;
	}
	
	
	public void showSubject(){
			
			List<SubjectPage> subjects=null;
			try {
				List<Subject> allSubject=subjectBiz.getAllSubject(sb,sb.getPage(),sb.getRows());
				Map<String,Object> map=new HashMap<String,Object>();
				subjects = new ArrayList<SubjectPage>();
				if(allSubject!=null && allSubject.size()>0){
					for (int i = 0; i < allSubject.size(); i++) {
						SubjectPage subjectPage = new SubjectPage();
						subjectPage.setId(allSubject.get(i).getId());
						subjectPage.setEditionName(allSubject.get(i).getEdition().getEditionName());
						subjectPage.setSemester(allSubject.get(i).getSemester());
						subjectPage.setSubjectName(allSubject.get(i).getSubjectName());
						subjectPage.setChapterCount(allSubject.get(i).getChapterCount());
						subjects.add(subjectPage);
						
					}
					int total=subjectBiz.getAllSubject(sb,null,null).size();
					map.put("total",total);
					map.put("rows",subjects);
					
				}else{
					map.put("total",0);
					map.put("rows",allSubject);
				}
				
				jsonStr=JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd");
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					JsonUtil.jsonOut(jsonStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	//更新科目
	public void updateSubject(){

		int sid=Integer.parseInt(req.getParameter("sid"));
		String subjectName=req.getParameter("newSubName");
		
		try {
			subjectName=new String(subjectName.getBytes("iso-8859-1"),"UTF-8");
			
			Subject subject=subjectBiz.findSubjectById(sid);
			subject.setId(sid);
			subject.setSubjectName(subjectName);
		
		
			int result=subjectBiz.updateSubject(subject);
			if(result>0){
				jsonStr=super.writeJson(0, null);
			}else{
				jsonStr=super.writeJson(1, null);
			}
		
		
			JsonUtil.jsonOut(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//删除科目
	public void deleteSubject(){

		int sid=Integer.parseInt(req.getParameter("sid"));
		String subjectName=req.getParameter("subName");
		
			try {
				int count=subjectBiz.getChapterCount(subjectName);
				if(count<=0){
					int result=subjectBiz.deleteSubject(sid);
					
					if(result>0){
						jsonStr=super.writeJson(0, null);
					}else{
						jsonStr=super.writeJson(1, null);
					}
					
				}else{
					JsonProtocol jsonProtocol=new JsonProtocol();
					//jsonProtocol.setObj("删除失败");
					jsonStr=super.writeJson(-1, null);
				}
				
				
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				jsonStr=super.writeJson(-1, null);
				try {
					JsonUtil.jsonOut(jsonStr);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
		
	}
	
	public void addSubject(){
		int editionId=Integer.parseInt(sb.getEditionName());
		String semester=sb.getSemester();
		String subjectName=sb.getSubjectName();
		/*try {
            subjectName=new String(subjectName.getBytes("iso-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }*/
		
		
		Edition edition=new Edition();
		edition.setId(editionId);
	
		Subject sb=new Subject();
		sb.setEdition(edition);
		sb.setSubjectName(subjectName);
		sb.setSemester(semester);
		sb.setChapterCount(0);		
		
		try {
			int result=subjectBiz.addSubject(sb);
			
			if(result>0){
				jsonStr=super.writeJson(0, null);
			}else{
				jsonStr=super.writeJson(1,"添加失败");
			}
		} catch (Exception e) {
			jsonStr=super.writeJson(1,"添加出现异常，请联系管理员");
			e.printStackTrace();
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	/**
	 * 根据学期来查询课程名称
	 * @author fanhaohao
	 */
	public void findSubjectBySemester(){
		String jsonStr="";
		try {
			List<String> list=subjectBiz.findSubjectNameBySemester(req.getParameter("semester"));
			jsonStr=super.writeJson(0, list);
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "修改失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}


	@Override
	public SubjectPage getModel() {
		return sb;
	}

}
