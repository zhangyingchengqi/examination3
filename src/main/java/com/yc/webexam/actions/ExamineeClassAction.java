package com.yc.webexam.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ModelDriven;
import com.yc.biz.ClassSemesterBiz;
import com.yc.biz.EditionBiz;
import com.yc.biz.ExamineeBiz;
import com.yc.biz.ExamineeClassBiz;
import com.yc.po.ClassSemester;
import com.yc.po.Edition;
import com.yc.po.Examinee;
import com.yc.po.ExamineeClass;
import com.yc.utils.Encrypt;
import com.yc.utils.JsonUtil;
import com.yc.vo.ListClassPage;

public class ExamineeClassAction extends BaseAction implements ModelDriven<ListClassPage>,ServletRequestAware,
ServletResponseAware {
    private static final long serialVersionUID = 1L;

    @Resource(name="examineeBiz")
	private ExamineeBiz examineeBiz;
	
	@Resource(name="examineeClassBiz")
	private ExamineeClassBiz examineeClassBiz;

    @Resource(name="classSemesterBiz")
    private ClassSemesterBiz classSemesterBiz;
    
    @Resource(name="editionBiz")
    private EditionBiz editionBiz;
	
	private String jsonStr=null;
	
	private ListClassPage  listClassPage=new ListClassPage();
	
	private PrintWriter out;
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();

	private HttpSession mysession = request.getSession();
	public void setServletResponse(HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		this.response = response;
		try {
			this.out = this.response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setServletRequest(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("utf-8");
			this.request = request;
			this.mysession = this.request.getSession();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
	}
	
	
	public ListClassPage getListClassPage() {
		return listClassPage;
	}
	
	public void getClassByEdition(){
        String eid=request.getParameter("eid");
        List<ListClassPage> list = new ArrayList<ListClassPage>();
        try {
            List<ExamineeClass> eClass = examineeClassBiz.findExamineeClassByEid(eid);
            if(eClass != null && eClass.size()>0){
                int i = 0;
                for (ExamineeClass examineeClass : eClass) {
                    examineeClass.setaDailyTalks(null);
                    examineeClass.setCheckings(null);
                    examineeClass.setPointPapers(null);
                    
                    ListClassPage listClassPage=new ListClassPage();
                    BeanUtils.copyProperties(examineeClass,listClassPage);
                    list.add(i,listClassPage);
                    int studentCount=examineeClassBiz.searchExamineeCount(examineeClass.getId());
                    list.get(i).setStudentCount(studentCount);
                    i++;
                }
            }
            jsonStr = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JsonUtil.jsonOut(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public void getCNumByDid(){
	    String did=request.getParameter("did");
	    try {
	        List<Edition> editions = editionBiz.getEditionByDid(did);
	        List<ListClassPage> list = new ArrayList<ListClassPage>();
            int i = 0;
            if(editions!=null && editions.size() >0){
                for (Edition edition : editions) {
                    List<ExamineeClass> eClass = examineeClassBiz.findExamineeClassByEid(edition.getId()+"");
                    if(eClass!=null && eClass.size()>0){
                        for (ExamineeClass examineeClass : eClass) {
                            examineeClass.setaDailyTalks(null);
                            examineeClass.setCheckings(null);
                            examineeClass.setPointPapers(null);

                            ListClassPage listClassPage=new ListClassPage();
                            BeanUtils.copyProperties(examineeClass,listClassPage);
                            list.add(i,listClassPage);
                            int studentCount=examineeClassBiz.searchExamineeCount(examineeClass.getId());
                            list.get(i).setStudentCount(studentCount);
                            i++;
                        }
                    }
                }
            }
	        jsonStr = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JsonUtil.jsonOut(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public void getCSByEid(){
        String eid=request.getParameter("eid");
        try {
            List<ClassSemester> css = classSemesterBiz.findClassSemesterByEC(eid);
            
            jsonStr = JSON.toJSONString(css, SerializerFeature.DisableCircularReferenceDetect); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JsonUtil.jsonOut(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	public void updateClass(){
		//int id=listClassPage.getId();
		String id=request.getParameter("id");
		String semester=request.getParameter("semester").toString().trim();
		String className=request.getParameter("className").toString().trim();
		String createDate=request.getParameter("createDate").toString().trim();
		String overDate=request.getParameter("overDate").toString().trim();
		String remark=request.getParameter("remark").toString().trim();
		try {
			ExamineeClass  examineeClass=examineeClassBiz.findExamineeClassById(Integer.parseInt(id));
			examineeClass.setClassName(className);
			examineeClass.setCreateDate(createDate);
			examineeClass.setOverDate(overDate);
			examineeClass.setRemark(remark);
			examineeClass.setSemester(semester);
			examineeClassBiz.updateExamineeClass(examineeClass);
			
			jsonStr=super.writeJson(0, null);
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "更新出现异常");
		}
		try {
			JsonUtil.jsonOut(jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//录入考生
	public void addExamClass(){
		String pwd=Encrypt.md5AndSha2(listClassPage.getPwd());
		String exaimineeNames=listClassPage.getExamineeNames();
		String className=listClassPage.getClassName();
		int classId=examineeClassBiz.getClassIdOfname(className);
		System.out.println("------------"+exaimineeNames+"---"+pwd+"----"+className+"----"+classId);
//		ExamineePK pk=new ExamineePK();
//		ExamineeClass examineeClass=new ExamineeClass();
//		Examinee examinee=new Examinee();
		String examinees[]=exaimineeNames.split("\n");
		for (int i = 0; i < examinees.length; i++) {
			String examineeName = examinees[i].toString().trim();
			
			if (!"".equals(examineeName)) {
	            int j = 1;
	            String examineeN = examineeName;
	            while(examineeBiz.isClassExaminee(examineeN, classId)){
	                examineeN = examineeName+j;
	                j++;
	            }
	            if(j > 1){
	                examineeName = examineeName + (j-1);
	            }
	            
				int count=examineeBiz.addExaminee(examineeName, pwd, classId);
				if(count>0){
					jsonStr=super.writeJson(0, count);
				}else{
					jsonStr=super.writeJson(1, "添加失败");
				}
				try {
					JsonUtil.jsonOut(jsonStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//检查班级是否存在
	public void checkClassName(){
		try {
		    
			String className=listClassPage.getClassName();
			Integer id=examineeClassBiz.getClassIdOfname(className);
			if(id>0  ){
				jsonStr=super.writeJson(0, null);
			}else{
				jsonStr=super.writeJson(1, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "校验出现异常");
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//新增班级
	public void addClass(){
		ExamineeClass  examineeClass=new ExamineeClass();
		BeanUtils.copyProperties(listClassPage, examineeClass);
		int snum = Integer.parseInt(listClassPage.getSnumber());
		String cName = listClassPage.getClassName();
		try {
		    Integer id=examineeClassBiz.getClassIdOfname(cName);
		    if(examineeClassBiz.getIdByName(cName) == null){
		        examineeClassBiz.addExamineeClass(examineeClass);
		        ExamineeClass ec = examineeClassBiz.getIdByName(cName);
		        ClassSemester classSemester = new ClassSemester();
                classSemester.setSemester("S1");
                classSemester.setExamineeClass(ec);
                classSemester.setStarttime(listClassPage.getCreateDate());
                classSemester.setEndtime(listClassPage.getOverDate());
                classSemesterBiz.addClassSemester(classSemester);
		        for (int i = 2; i <= snum; i++) {
		            ClassSemester classS = new ClassSemester();
		            classS.setSemester("S"+i);
		            classS.setExamineeClass(ec);
		            classSemesterBiz.addClassSemester(classS);
                }
	            
	            jsonStr=super.writeJson(0, null);
		    }else{
		        jsonStr=super.writeJson(1, "班级名已存在");
		    }
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "新增班级出现异常"+e.toString());
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//更新班级的学期
    public void updateClassSemester(){
        String id=request.getParameter("id");
        String createDate=request.getParameter("createDate").toString().trim();
        String overDate=request.getParameter("overDate").toString().trim();
        String remark=request.getParameter("remark").toString().trim();
        
        ClassSemester classSemester = new ClassSemester();
        classSemester.setId(Integer.parseInt(id));
        classSemester.setStarttime(createDate);
        classSemester.setEndtime(overDate);
        classSemester.setRemark(remark);
        try {
            classSemesterBiz.updateClassSemester(classSemester);
        
            jsonStr=super.writeJson(0, "班级学期信息更新成功");
        } catch (Exception e) {
            jsonStr=super.writeJson(1, "新增班级出现异常"+e.toString());
        }finally{
            try {
                JsonUtil.jsonOut(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //获取班级的学期信息
    public void getClassSemester(){
        String eid=request.getParameter("eid");
        try {
            List<ClassSemester> list = classSemesterBiz.findClassSemesterByEC(eid);
        
            jsonStr = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect); 
        } catch (Exception e) {
            jsonStr=super.writeJson(1, "获取班级的学期信息出现异常"+e.toString());
        }finally{
            try {
                JsonUtil.jsonOut(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
	
	
	//修改全班考生的密码
		public void updateAllExaminee() throws IOException{
			String className=listClassPage.getClassName();
			String pwd=Encrypt.md5AndSha2(listClassPage.getPwd());
			
			try {
				List<Examinee>  examinees=examineeBiz.getAllExaminee(className);
				if(examinees!=null  &&  examinees.size()>0){
					for(Examinee examinee:  examinees){
						examinee.setPassword(pwd);
						int result=examineeBiz.updateExaminee(examinee);
						if(result>0){
							jsonStr=super.writeJson(0, null);
						}else{
							jsonStr=super.writeJson(1, "修改全班密码失败");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				jsonStr=super.writeJson(1, "修改出现异常"+e.toString());
			}finally{
				JsonUtil.jsonOut(jsonStr);
			}
			
			
		}
		
	//删除考生
	public void deleteExaminee(){
		String className=listClassPage.getClassName();
		String examineeName=listClassPage.getName();
		try {
			int classId=examineeClassBiz.getClassIdOfname(className);
			int result=examineeBiz.deletelistexam(classId, examineeName);
			if(result>0){
				jsonStr=super.writeJson(0, null);
			}else{
				jsonStr=super.writeJson(1, "删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "删除出现异常"+e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//修改考生密码
	public void updatepassword() throws IOException{
		String className=listClassPage.getClassName();
		String newpwd=Encrypt.md5AndSha2(listClassPage.getPwd());
		String examineeName=listClassPage.getName();
		
		try {
			Examinee examinee=examineeBiz.getExaminee(examineeName, null, className);
			examinee.setPassword(newpwd);
			int result=examineeBiz.updateExaminee(examinee);
			
			if(result>0){
				jsonStr=super.writeJson(0, null);
			}else{
				jsonStr=super.writeJson(1, "修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "修改出现异常"+e.toString());
		}finally{
			JsonUtil.jsonOut(jsonStr);
		}
		
	}
	
	
	//修改考生姓名
	public void updateExaminee(){
		String name=listClassPage.getName();
		String className=listClassPage.getClassName();
		String oldname=listClassPage.getOldname();
		
		Examinee examinee=examineeBiz.getExaminee(oldname, null, className);
		examinee.setName(name);
		//根据班级名查到班级编号
		try {
			//根据班级编号和学生旧姓名查出单个学生的信息
			//ExamineePK  examineePK=examinee.getPk();
			int result=examineeBiz.updateExaminee(examinee);
			if(result>0){
				jsonStr=super.writeJson(0, null);
			}else{
				jsonStr=super.writeJson(1, "修改失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "修改出现异常"+e.toString());
			
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//根据班级编号显示同一班级的学生信息
	public void showExamineeList(){
		String className=listClassPage.getClassName();

		try {
			List<String> examinee=examineeBiz.findAllStuNameByClassName(className);
			List<ListClassPage> list = new ArrayList<ListClassPage>();
			if(examinee!=null  &&  examinee.size()>0){
				for(int i=0;i<examinee.size();i++){
					ListClassPage  listClassPage=new ListClassPage();
					listClassPage.setName(examinee.get(i));
					listClassPage.setClassName(className);
					list.add(i,listClassPage);
				}
			}

			
			jsonStr = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect); 
			System.out.println(jsonStr+"-----------------------------------------------------------");
			JsonUtil.jsonOut(jsonStr);
		} catch (IOException e) {
			jsonStr = super.writeJson(0, "查询出现异常"+e.toString());
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}

	//显示班级列表
	public void showClassList(){
		
		try {
			String semester=listClassPage.getSemester();
		    //String semester="S1";
			List<ExamineeClass> examineeClass= examineeClassBiz.findExamineeClassBySemester(semester);
			List<ListClassPage> list = new ArrayList<ListClassPage>();
			if(examineeClass!=null  &&  examineeClass.size()>0){
				for(int i=0;i<examineeClass.size();i++){
					ListClassPage listClassPage=new ListClassPage();
					BeanUtils.copyProperties(examineeClass.get(i),listClassPage);
					list.add(i,listClassPage);
					int studentCount=examineeClassBiz.searchExamineeCount(examineeClass.get(i).getId());
					list.get(i).setStudentCount(studentCount);
				}
				System.out.println(list);
				jsonStr = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect); 
				JsonUtil.jsonOut(jsonStr);
			}else{
				jsonStr=super.writeJson(0, "查询数据库失败");
				JsonUtil.jsonOut(jsonStr);
			}
		} catch (Exception e) {
			jsonStr=super.writeJson(0, "出现异常："+e.toString()+"请与管理员联系");
			e.printStackTrace();
		} 
		
	}
	
	//删除班级
	public void deleteClass(){
		try {
			Integer id=listClassPage.getId();
			Integer result=examineeClassBiz.deleteExaminee(id);
			if(result>0){
				jsonStr=super.writeJson(0, null);
			}else{
				jsonStr=super.writeJson(1, "删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "删除出现异常"+e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	@Override
	public ListClassPage getModel() {
		return listClassPage;
	}
	
	
}
