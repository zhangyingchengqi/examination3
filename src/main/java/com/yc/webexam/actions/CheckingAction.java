package com.yc.webexam.actions;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

//import org.newboy.net.UrlUtils;
import org.springframework.context.annotation.Scope;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yc.biz.ADailyTalkBiz;
import com.yc.biz.CheckingBiz;
import com.yc.biz.ExamineeClassBiz;
import com.yc.biz.SystemUserBiz;
import com.yc.biz.TempBiz;
import com.yc.po.ADailyTalk;
import com.yc.po.Checking;
import com.yc.po.ExamineeClass;
import com.yc.po.SystemUser;
import com.yc.po.Temp;
import com.yc.utils.JsonUtil;
import com.yc.utils.PageUtil;
import com.yc.utils.UTFUtil;
import com.yc.vo.AdailyTalkPage;
import com.yc.vo.CheckingPage;
import com.yc.vo.DataGaidModel;

@SuppressWarnings("serial")
@Scope("prototype")
public class CheckingAction extends BaseAction implements ServletRequestAware,
		ServletResponseAware, ModelDriven<AdailyTalkPage> {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CheckingAction.class);

	private PrintWriter out;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	private PageUtil pageUtil = new PageUtil();
	private int totalInfo = 0;
	private int countInfo = 10;
	private int tempsum = 0;
	private String resultListInfo = "";
	private static List<ADailyTalk> aDailyTalkInfoHistory = null;
	
	
	private File image; //上传的文件
    private String imageFileName; //文件名称
    private String imageContentType; //文件类型
    
    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }
	
	
	private List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
	private Map<String, Object> map = new HashMap<String, Object>();

	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();

	private HttpSession mysession = request.getSession();

	@Resource(name = "checkingBiz")
	private CheckingBiz checkingBiz;
	@Resource(name = "examineeClassBiz")
	private ExamineeClassBiz examineeClassBiz;
	@Resource(name = "aDailyTalkBiz")
	private ADailyTalkBiz aDailyTalkBiz;
	@Resource(name = "systemUserBiz")
	private SystemUserBiz systemUserBiz;
	@Resource(name = "tempBiz")
	private TempBiz  tempBiz;

	private AdailyTalkPage adailyTalkPage = new AdailyTalkPage();

	public AdailyTalkPage getAdailyTalkPage() {
		return adailyTalkPage;
	}

	JSONArray json = null;
	JSONObject jb = null;
	String jsonStr = null;

	public void setaDailyTalkBiz(ADailyTalkBiz aDailyTalkBiz) {
		this.aDailyTalkBiz = aDailyTalkBiz;
	}

	public void setCheckingBiz(CheckingBiz checkingBiz) {
		this.checkingBiz = checkingBiz;
	}

	public void setSystemUserBiz(SystemUserBiz systemUserBiz) {
		this.systemUserBiz = systemUserBiz;
	}

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
	
	/**
	 * 查看
	 */
	@SuppressWarnings("static-access")
	public void showcheckingresultinfos(){
		String checkId=request.getParameter("checkId").toString().trim();
		if(checkId!=null&&!"".equals(checkId)){
			
			try {
				Checking checking=checkingBiz.findCheckingInfoById(Integer.parseInt(checkId));
				if(checking!=null){
					checking.setExamineeClass(null);
					checking.setSystemUser(null);
					jsonStr=super.writeJson(0, checking);
				}else{
					jsonStr=super.writeJson(1, "查询出错");
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				jsonStr=super.writeJson(1, "查询出现异常");
			}finally{
				try {
					JsonUtil.jsonOut(jsonStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 提交修改结果
	 */
	public void subchangeChecking(){
		int checkId=adailyTalkPage.getId();
		Date checkDate=adailyTalkPage.getCheckDate();
		String checkTime=adailyTalkPage.getCheckTime();
		String checkRemark=adailyTalkPage.getCheckRemark();
		String checkResult=adailyTalkPage.getCheckResult();
		
		try {
			Checking checking=checkingBiz.findCheckingInfoById(checkId);
			
			checking.setCheckId(checkId);
			checking.setCheckDate(checkDate);
			checking.setCheckTime(checkTime);
			checking.setCheckRemark(checkRemark);
			checking.setCheckResult(checkResult);
			
			int result=checkingBiz.updateCheckingInfo(checking);
			if(result>0){
				jsonStr=super.writeJson(0, null);
			}else{
				jsonStr=super.writeJson(1, "更新失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "更新出现异常");
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	/**
	 * 提交考勤结果
	 * 
	 * @throws IOException
	 */
	public void subChecking() throws IOException {
		int className = Integer.parseInt(adailyTalkPage.getClassName());
		Date checkDate = adailyTalkPage.getCheckDate();
		String checkTime = adailyTalkPage.getCheckTime();
		String checkRemark = adailyTalkPage.getCheckRemark();
		String semester = adailyTalkPage.getSemester();
		String userName = adailyTalkPage.getUserName();
		String checkResult = adailyTalkPage.getCheckResult();
		Checking ck = checkingBiz.findCheckingByClassIdAndDateTime(className,
				checkDate, checkTime);
		try {
			if (ck != null) {
				jsonStr = super.writeJson(1, "该时间段考勤已经提交，请不要重复考勤");
				return;
			} else {

				List<SystemUser> user = systemUserBiz
						.findSystemUserByName(userName);
				int uid = user.get(0).getId();

				Checking checking = new Checking();
				checking.setSemester(semester);
				checking.setCheckDate(checkDate);
				checking.setCheckTime(checkTime);
				checking.setCheckRemark(checkRemark);
				checking.setCheckResult(checkResult);
				checking.setCheckFlag(null);
				checking.setCheckStatus(1);
				checking.setCheckFlag(null);
				checking.setCheckDescript(null);
				SystemUser systemUser = new SystemUser();
				systemUser.setId(uid);
				checking.setSystemUser(systemUser);
				ExamineeClass examineeClass = new ExamineeClass();
				examineeClass.setId(className);
				checking.setExamineeClass(examineeClass);

				int result = checkingBiz.addCheckingInfo(checking);
				if (result > 0) {
					jsonStr = super.writeJson(0, null);
				} else {
					jsonStr = super.writeJson(1, "提交考勤结果失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr = super.writeJson(1, "添加出现异常，请联系管理员");
		} finally {
			JsonUtil.jsonOut(jsonStr);
		}

	}

	public void showAllCheckingInfo() throws Exception {
		String cid = request.getParameter("cid").toString().trim(); // 班级
		String startDate = request.getParameter("startDate").toString().trim(); // 查询开始时间
		String endDate = request.getParameter("endDate").toString().trim(); // 查询结束时间
		String dateTime = request.getParameter("dateTime").toString().trim(); // 查询时间段
		
		if(startDate==null||"".equals(startDate)){
			startDate="1911-1-1";
		}
		if(endDate==null||"".equals(endDate)){
			endDate="2050-12-30";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date a = sdf.parse(startDate);
		Date b = sdf.parse(endDate);
		boolean flag = a.before(b);
		if(!flag){
			String c="";
			c=startDate;
			startDate=endDate;
			endDate=c;
		}
		
		
//		String userName = request.getParameter("userName").toString().trim();
//		List<SystemUser> user = systemUserBiz.findSystemUserByName(userName);
		List<Checking> checking = null;

		
			
		checking= checkingBiz.findCheckingByClassIdAndUidAndTime(Integer.parseInt(cid), dateTime, startDate, endDate);
		
			try {
			if (checking != null && checking.size() > 0) {
				for (int i = 0; i < checking.size(); i++) {
					SystemUser systemUser = new SystemUser();
					systemUser.setUserName(checking.get(i).getSystemUser()
							.getUserName());
					checking.get(i).setSystemUser(systemUser);
					ExamineeClass examineeClass = new ExamineeClass();
					examineeClass.setClassName(checking.get(i)
							.getExamineeClass().getClassName());
					checking.get(i).setExamineeClass(examineeClass);
					checking.get(i).setCheckDate(
							new SimpleDateFormat("yyyy-MM-dd").parse(checking
									.get(i).getCheckDate().toString()));
				}
				jsonStr = JSON.toJSONString(checking,
						SerializerFeature.DisableCircularReferenceDetect);
			} else {
				jsonStr = super.writeJson(1, "查询失败");
			}
		} catch (Exception e) {
			jsonStr = super.writeJson(1, "查询出现的异常");
			e.printStackTrace();
		} finally {
			JsonUtil.jsonOut(jsonStr);
		}
	}
	
	/**
	 * 学生端查询考勤
	 */
	public void findResultStu() throws Exception{
		String semester = request.getParameter("semester").toString().trim(); // 学期编号
		String startDate = request.getParameter("startDate").toString().trim(); // 查询开始时间
		String endDate = request.getParameter("endDate").toString().trim(); // 查询结束时间
		String dateTime = request.getParameter("dateTime").toString().trim(); // 查询时间段
		
		if(semester=="0"||"0".equals(semester)){
			semester="";
		}
		if(dateTime=="0"||"0".equals(dateTime)){
			dateTime="";
		}
		if(startDate==null||"".equals(startDate)){
			startDate="1911-1-1";
		}
		if(endDate==null||"".equals(endDate)){
			endDate="2050-12-30";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date a = sdf.parse(startDate);
		Date b = sdf.parse(endDate);
		boolean flag = a.before(b);
		if(!flag){
			String c="";
			c=startDate;
			startDate=endDate;
			endDate=c;
		}
		
		String uname = mysession.getAttribute("userName").toString().trim(); // 当前学生姓名

		// 查出满足条件的考勤记录
		List<Checking> list = checkingBiz.findCheckingResullByStudentName(
				semester, startDate, endDate, dateTime, uname);

		// 准备结果页面
		StringBuffer stf = new StringBuffer(
				"<table width=\"860\" border=\"1\" cellpadding=\"1\" cellspacing=\"1\" bordercolor=\"#FFFFFF\" >");
		StringBuffer line = new StringBuffer();
		int count1 = 0;
		int count2 = 0;
		int count3 = 0;
		int count4 = 0;
		int count5 = 0;
		int count6 = 0;
		int sum = 0;
		String result = "";

		// 如果有数据，则拼接
		if (list != null && list.size() > 0) {
			// 拼接表格的th部分
			if (list.size() >= 4) {
				stf.append("<tr><th bordercolor=\"#7EA6DC\">日&nbsp;&nbsp;期</th><th bordercolor=\"#7EA6DC\">状&nbsp;&nbsp;态</th>");
				stf.append("<th bordercolor=\"#7EA6DC\">日&nbsp;&nbsp;期</th><th bordercolor=\"#7EA6DC\">状&nbsp;&nbsp;态</th>");
				stf.append("<th bordercolor=\"#7EA6DC\">日&nbsp;&nbsp;期</th><th bordercolor=\"#7EA6DC\">状&nbsp;&nbsp;态</th>");
				stf.append("<th bordercolor=\"#7EA6DC\">日&nbsp;&nbsp;期</th><th bordercolor=\"#7EA6DC\">状&nbsp;&nbsp;态</th></tr>");
			} else {
				stf.append("<tr>");
				for (int i = 0; i < list.size(); i++) {
					stf.append("<th bordercolor=\"#7EA6DC\">日&nbsp;&nbsp;期</th><th bordercolor=\"#7EA6DC\">状&nbsp;&nbsp;态</th>");
				}
				stf.append("</tr>");
			}
			// 拼接表格的td部分
			stf.append("<tr align=\"center\" onmouseover=\"this.bgColor='#93BBDF'\" onmouseout=\"this.bgColor='#EDECEB';\" height=\"25\">");
			for (Checking ck : list) {
				sum++;
				line.append("<td align=\"center\" bordercolor=\"#7EA6DC\">"
						+ ck.getCheckDate() + " " + ck.getCheckTime() + "</td>");
				// 获取该学生的考勤状态
				result = ck.getCheckResult();
				result = result.substring(result.indexOf("|" + uname + ",")
						+ uname.length() + 2, result.indexOf("|" + uname + ",")
						+ uname.length() + 3);

				if (Integer.parseInt(result) == 2) {
					count2++;
					line.append("<td align=\"center\" bordercolor=\"#7EA6DC\" style=\"color:red\">迟到</td>");
				} else if (Integer.parseInt(result) == 3) {
					count3++;
					line.append("<td align=\"center\" bordercolor=\"#7EA6DC\" style=\"color:green\">病假</td>");
				} else if (Integer.parseInt(result) == 4) {
					count4++;
					line.append("<td align=\"center\" bordercolor=\"#7EA6DC\" style=\"color:red\">请假</td>");
				} else if (Integer.parseInt(result) == 5) {
					count5++;
					line.append("<td  align=\"center\" bordercolor=\"#7EA6DC\" style=\"color:red\">旷课</td>");
				} else if (Integer.parseInt(result) == 6) {
					count6++;
					line.append("<td  align=\"center\" bordercolor=\"#7EA6DC\" style=\"color:red\">早退</td>");
				} else {
					count1++;
					line.append("<td align=\"center\" bordercolor=\"#7EA6DC\">已到</td>");
				}
				if (sum % 4 == 0) {
					stf.append(line).append("</tr>");
					line = new StringBuffer();
					stf.append("<tr align=\"center\" onmouseover=\"this.bgColor='#93BBDF'\" onmouseout=\"this.bgColor='#EDECEB';\" height=\"25\">");
				}
			}
			stf.append(line).append("</tr>");
			if (list.size() >= 4) {
				stf.append("<tr eight=\"40\" style=\"color:red;font-weight:bold;font-size:18px;\"><td colspan=\"8\">");
			} else {
				stf.append("<tr height=\"40\" style=\"font-weight:bold;font-size:18px;\"><td colspan=\""
						+ (sum * 2) + "\">");
			}
			stf.append("考勤总数：" + sum + "次&nbsp;&nbsp;已到：" + count1
					+ "次&nbsp;&nbsp迟到：<font color=\"red\">" + count2
					+ "次</font>&nbsp;&nbsp" + "病假：<font color=\"green\">"
					+ count3 + "次</font>&nbsp;&nbsp;请假：<font color=\"green\">"
					+ count4 + "次</font>&nbsp;&nbsp;"
					+ "旷课：<font color=\"red\">" + count5
					+ "次</font>&nbsp;&nbsp;早退：<font color=\"red\">" + count6
					+ "次</font>&nbsp;&nbsp;</td></tr>");
		} else { // 如果没有记录，提示
			stf.append("<tr><td><br/><br/><span class=\"fontColor\" style=\"color:red;font-weight:bold;font-size:18px;\">提示：对不起,没有找到您要的数据!</span></td></tr>");
		}
		stf.append("</table>");
		//this.writeJson(0, stf.toString());
		out.print(stf.toString());
		out.flush();
	}
	//根据查询组合条件统计(教师端)  的弹出层处理


	//根据学生姓名统计（弹层显示）
	public void totalResultByStuname(){
			String sname=request.getParameter("sname").toString().trim();  //学生姓名
			
			try {
				if(sname!=null&&!"".equals(sname)){
					List<Temp> list=tempBiz.getTotalStatusBysname(sname);
					int suminfo=tempBiz.getCheckingTotalInfo(sname);
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("list", list);
					map.put("suminfo", suminfo);
					jsonStr=super.writeJson(0, map);
				}else{
					jsonStr=super.writeJson(1, "对不起,信息统计失败!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				jsonStr=super.writeJson(1, "对不起,信息统计异常!");
			}finally{
				try {
					JsonUtil.jsonOut(jsonStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	//日期弹层
	public  void totalResultByDate(){
		String ppid=request.getParameter("ppid").toString().trim();  //考勤编号
		try {
			if(ppid!=null&&!"".equals(ppid)){
				StringBuffer stf=new StringBuffer("<b>备注：</b><br /><br />");
				Checking ck=checkingBiz.findCheckingInfoById(Integer.parseInt(ppid));
				if(ck!=null&&ck.getCheckRemark()!=null&&!"".equalsIgnoreCase(ck.getCheckRemark().trim())){
					stf.append(ck.getCheckRemark());
				}else{
					stf.append("暂无");
				}
				
				jsonStr=super.writeJson(0, stf);
			}else{
				jsonStr=super.writeJson(1, "对不起，显示备注失败");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "对不起，显示备注异常");
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//状态的备注信息（弹层）
	public  void totalResultByStatus(){
		String ppid=request.getParameter("ppid").toString().trim();  //考勤编号
		String sname=request.getParameter("sname").toString().trim();  //学生姓名

		try {
			if(ppid!=null&&!"".equals(ppid)&&sname!=null&&!"".equals(sname)&&!"".equals(sname)){
				StringBuffer stf=new StringBuffer("<b>备注：</b><br /><br />");
				Temp tp=tempBiz.getSingleCheckingResultInfo(Integer.parseInt(ppid),sname);

				if(tp!=null&&tp.getPcontent().trim()!=null&&!"".equalsIgnoreCase(tp.getPcontent().trim())){
					stf.append(tp.getPcontent());
				}else{
					stf.append("暂无");
				}
				
				jsonStr=super.writeJson(0, stf);
			}else{
				jsonStr=super.writeJson(1, "对不起，显示备注失败");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	
	//根据查询组合条件统计(教师端)
	public void findResult() throws ParseException {
		String semester=request.getParameter("semester").toString().trim();  //学期编号
		String cid=request.getParameter("cid").toString().trim();  //班级编号
		String stuname=request.getParameter("stuname").toString().trim();  //学生姓名
		String startdate=request.getParameter("startdate").toString().trim();  //查询开始时间
		String enddate=request.getParameter("enddate").toString().trim();  //查询结束时间
		String datetime=request.getParameter("datetime").toString().trim();  //查询时间段
		
		if(startdate==null||"".equals(startdate)){
			startdate="1911-1-1";
		}
		if(enddate==null||"".equals(enddate)){
			enddate="2050-12-30";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date a = sdf.parse(startdate);
		Date b = sdf.parse(enddate);
		boolean flag = a.before(b);
		if(!flag){
			String c="";
			c=startdate;
			startdate=enddate;
			enddate=c;
		}
		
		
		List<Integer> checkides=null;
		try {
			checkides=checkingBiz.findCheckingAllCheckId(semester,Integer.parseInt(cid), stuname, startdate, enddate, datetime);
			//如果查到了记录，则执行存储过程
			if(checkides!=null&&checkides.size()>0){
				int result = tempBiz.exectueDelTemp(); // 清空临时表中的记录
				if (result >= 0) {
					for (Integer str : checkides) {
						int ctime=0;	//考勤时间段   1.上午   2.下午  3.晚上
						String sname="";  //学生姓名
						int status=0;   //考勤状态
						String remark="";  //备注
						List<Checking> list=checkingBiz.findCheckByCheckid(str);
						Date checkDate=list.get(0).getCheckDate();
						String checkTime=list.get(0).getCheckTime();
						String checkResult=list.get(0).getCheckResult();
						Integer ccid=list.get(0).getExamineeClass().getId();
						list.get(0).getExamineeClass().getClassName();
						String className=list.get(0).getExamineeClass().getClassName();
						int flags=1;
						while(flags>0){
							if("上午".equals(checkTime.trim())){
								ctime=1;
							}else if("下午".equals(checkTime.trim())){
								ctime=2;
							}else{
								ctime=3;
							}
							sname=checkResult.substring(0,checkResult.indexOf(",", 0));
							status=Integer.parseInt(checkResult.substring(checkResult.indexOf(",", 0)+1,checkResult.indexOf("-", 0)));
							remark=checkResult.substring(checkResult.indexOf("-", 0)+1,checkResult.indexOf("|", 0));
							Temp t=new Temp();
							t.setPpid(str);
							t.setSname(sname);
							t.setClassid(ccid);
							t.setClassName(className);
							t.setSubid(ctime);
							t.setSubname(remark);
							t.setPointid(status);
							t.setPcontent(sdf.format(checkDate));
							t.setGrade(Float.parseFloat(0+""));
							//insert into temp values(@checkId,@sname,@cid,@className,@status,cast(@checkDate as varchar(20)),@ctime,@remark,0)
							tempBiz.addTemp(t);
							if(checkResult.substring(checkResult.indexOf("|", 0)+1,checkResult.length()).indexOf("|",0)>1){
								checkResult=checkResult.substring(checkResult.indexOf("|", 0)+1,checkResult.length());
							}else{
								flags=0;
							}
						}
						// 执行存储过程
						tempBiz.exectueCheckingProc(str);
					}

					// 根据条件获取考勤信息
					jsonStr = readyCheckingJson(Integer.parseInt(cid), stuname);

				} else {
					jsonStr = super.writeJson(1, "暂无您要查询的考勤记录");
				}
			}else{
				jsonStr=super.writeJson(1, "暂无您要查询的考勤记录");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "查询出现异常");
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private String readyCheckingJson(int cid, String stuname) {
		List<Map<String,Object>> list=null;
		Map<String,Object> map=null;
		try {
			if(stuname!=null  &&  !"".equalsIgnoreCase(stuname) && !"0".equals(stuname)){
				
				//根据学生姓名，查询该学生的考勤信息
				List<Temp> singleCheckingInfo=tempBiz.getSingleCheckingInfo(stuname);
				if(singleCheckingInfo!=null&&singleCheckingInfo.size()>0){
					jsonStr=super.writeJson(0, singleCheckingInfo);
				}else{
					jsonStr=super.writeJson(1, "暂无您要查询的考勤记录");
				}
			}else{
				//根据班级查询这个班的考勤学生姓名
				List<Temp> checkingStuInfo=tempBiz.getAllStuname(cid);
				list=new ArrayList<Map<String,Object>>();
				map=new HashMap<String, Object>();
				if(checkingStuInfo!=null&&checkingStuInfo.size()>0){
					map.put("checkingStuInfo", checkingStuInfo);
				}
				//获取考勤日期
				List<Temp> checkingDateTime=tempBiz.getAllDateTime(cid);
				if(checkingDateTime!=null  &&  checkingDateTime.size()>0){
					map.put("checkingDateTime", checkingDateTime);
				}
				
				//获取考勤记录  已到  请假 病假 等标记
				List<Temp> allInfo=tempBiz.getAllCheckingInfo(cid);
				if(allInfo!=null  &&  allInfo.size()>0){
					map.put("allInfo", allInfo);
				}
				//获取警告信息
				List<Temp> warnInfo=tempBiz.getWarnInfo(cid);
				if(warnInfo!=null  &&  warnInfo.size()>0){
					map.put("warnInfo", warnInfo);
				}
				
				
				jsonStr=JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
//				jsonStr=super.writeJson(0, allInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr=super.writeJson(1, "查询出现异常");
		}
		
		return jsonStr;
		
		
	}

	/**
	 * 每日一讲后台
	 * 
	 * @throws IOException
	 */
	public void dailyTalk() throws Exception {
		String name =new String( adailyTalkPage.getName().getBytes("iso-8859-1"),"UTF-8");
		String className = adailyTalkPage.getClassName();
		String knowledge = adailyTalkPage.getKnowledge();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		String time=df.format(new Date());// new Date()为获取当前系统时间
		knowledge=new String(knowledge.getBytes("iso-8859-1"),"UTF-8");
		int cid = aDailyTalkBiz.getCid(className);

		ADailyTalk adilyTalk = new ADailyTalk();
		adilyTalk.setName(name);
		adilyTalk.setKnowledge(knowledge);
		adilyTalk.setStatus(1);
		adilyTalk.setSpeakdate(df.parse(time));
		adilyTalk.setAssessment("等待开讲");
		ExamineeClass examineeClass = new ExamineeClass();
		examineeClass.setId(cid);
		adilyTalk.setExamineeClass(examineeClass);

		try {
			int result = (Integer) aDailyTalkBiz.addADailyTalk(adilyTalk);

			if (result > 0) {
				jsonStr = super.writeJson(0, null);
			} else {
				jsonStr = super.writeJson(1, "添加演讲内容失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr = super.writeJson(1, "添加出现异常，请联系管理员");
		} finally {
			JsonUtil.jsonOut(jsonStr);
		}

	}

	/**
	 * 删除
	 * 
	 * @throws IOException
	 */

	public void delSpeak() throws IOException {
		int id = adailyTalkPage.getId();

		try {
			int result = aDailyTalkBiz.delADailyTalk(id);

			if (result > 0) {
				jsonStr = super.writeJson(0, null);
			} else {
				jsonStr = super.writeJson(1, "该操作不成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr = super.writeJson(1, "出现异常");
		} finally {
			JsonUtil.jsonOut(jsonStr);
		}
	}

	/**
	 * 显示历史的每日一讲
	 * 
	 * @throws IOException
	 */

	public void showHistory() throws IOException {
		int status = adailyTalkPage.getStatus();
		String className = adailyTalkPage.getClassName();
		String name=UTFUtil.Utf8Util(adailyTalkPage.getName());
		List<ADailyTalk> aDailyTalk=null;
		try {
			int cid = aDailyTalkBiz.getCid(className);
			if(name==null  ||  "".equals(name)){
				aDailyTalk = aDailyTalkBiz
						.findHistoryADailyTalkByCid(cid, status, null, null);
			}else{
				aDailyTalk = aDailyTalkBiz.findADailyTalkByCidHistoryStudent(cid, name);
			}
			
			

			if (aDailyTalk != null && aDailyTalk.size() > 0) {
				for (int i = 0; i < aDailyTalk.size(); i++) {
					aDailyTalk.get(i).setExamineeClass(null);
				}
				jsonStr = super.writeJson(0, aDailyTalk);
			} else {
				jsonStr = super.writeJson(1, "暂无相关每日一讲信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr = super.writeJson(1, "出现异常");
		} finally {
			JsonUtil.jsonOut(jsonStr);
		}
	}

	/**
	 * 显示还未开讲的
	 * 
	 * @throws IOException
	 */
	public void showDailyTalk() throws IOException {
		int status = adailyTalkPage.getStatus();
		String className = adailyTalkPage.getClassName().trim();

		try {
			int cid = aDailyTalkBiz.getCid(className);

			List<ADailyTalk> aDailyTalk = aDailyTalkBiz.findADailyTalkByCid(cid, status);

			if (aDailyTalk != null && aDailyTalk.size() > 0) {
				for (int i = 0; i < aDailyTalk.size(); i++) {
					aDailyTalk.get(i).setExamineeClass(null);
				}
				jsonStr = super.writeJson(0, aDailyTalk);
			} else {
				jsonStr = super.writeJson(1, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr = super.writeJson(1, "出现异常");
		} finally {
			JsonUtil.jsonOut(jsonStr);
		}

	}

	/**
	 * 开讲
	 * 
	 * @throws IOException
	 */
	public void startSpeak() throws IOException {
		int id = adailyTalkPage.getId();
		String assessment=adailyTalkPage.getAssessment();

		try {
			int result = aDailyTalkBiz.updateADailyTalkStatus(id,assessment, 2);
			if (result > 0) {
				jsonStr = super.writeJson(0, null);
			} else {
				jsonStr = super.writeJson(1, "该操作不成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonStr = super.writeJson(1, "出现异常");
		} finally {
			JsonUtil.jsonOut(jsonStr);
		}
	}

	/**
	 * 每日一讲
	 * 
	 * @return
	 */
	public void talk() {
		String uname = mysession.getAttribute("userName").toString().trim(); // 当前学生姓名
		// String
		// cid=mysession.getAttribute("loginUserclassId").toString().trim();
		// //班级编号
		String examName = mysession.getAttribute("examClass").toString().trim(); // 班级编号
		int cid = aDailyTalkBiz.getCid(examName);
		int pageNo = super.getPage();
		int row=super.getRows();
		AdailyTalkPage at=new AdailyTalkPage();
		DataGaidModel dgm=new DataGaidModel();
		
		List<AdailyTalkPage> AdailyTalkModel=new ArrayList<AdailyTalkPage>();

		List<ADailyTalk> aDailyTalkInfoHistory = aDailyTalkBiz.findSize(cid);

		totalInfo = aDailyTalkInfoHistory.size();
		dgm.setTotal(Long.parseLong(totalInfo+""));
		
		List<ADailyTalk> list = aDailyTalkBiz.findADailyTalkBypages(cid, uname,	pageNo, row);
		for(int i=0;i<list.size();i++){
			AdailyTalkPage atp=new AdailyTalkPage();
			
			String a=list.get(i).getSpeakdate().toString();
			String b=a.split(" ")[0];
			atp.setId(list.get(i).getId());
			atp.setName(list.get(i).getName());
			atp.setKnowledge(list.get(i).getKnowledge());
			atp.setAssessment(list.get(i).getAssessment());
			atp.setSpeakdate(java.sql.Date.valueOf(b));
			atp.setStatus(list.get(i).getStatus());
			atp.setUname(uname);
			if("1".equals(list.get(i).getStatus()+"")||list.get(i).getStatus()==1){
				atp.setSta("资料准备中");
			}else if("2".equals(list.get(i).getStatus()+"")||list.get(i).getStatus()==2){
				atp.setSta("资料未上传");
			}else if("3".equals(list.get(i).getStatus()+"")||list.get(i).getStatus()==3){
				atp.setSta("可以下载");
			}
			AdailyTalkModel.add(atp);
			//dgm.setRows(AdailyTalkModel);
		}
		dgm.setRows(AdailyTalkModel);

		String json = JSON.toJSONStringWithDateFormat(dgm, "yyyy-MM-dd");
		// String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.PrettyFormat);
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		try {
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void talkByFpc(){
		String uname = mysession.getAttribute("userName").toString().trim(); // 当前学生姓名
		// String
		// cid=mysession.getAttribute("loginUserclassId").toString().trim();
		// //班级编号
		String examName = mysession.getAttribute("examClass").toString().trim(); // 班级编号
		int cid = aDailyTalkBiz.getCid(examName);
		List<ADailyTalk> stuNameList = aDailyTalkBiz
				.findADailyTalkStudentName(cid);
		map.put("stuNameList", stuNameList);
		listMap.add(map);
		String json = this.writeJson(0, listMap);
		out.print(json);
		out.flush();
	}
	

	// 查询
	public void find() throws ParseException {
		String uname = mysession.getAttribute("userName").toString().trim(); // 当前学生姓名
		String examName = mysession.getAttribute("examClass").toString().trim(); // 班级编号
		
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		
		if(startDate==null||"".equals(startDate)){
			startDate="1911-1-1";
		}
		if(endDate==null||"".equals(endDate)){
			endDate="2050-12-30";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date aa = sdf.parse(startDate);
		Date bb = sdf.parse(endDate);
		boolean flag = aa.before(bb);
		if(!flag){
			String c="";
			c=startDate;
			startDate=endDate;
			endDate=c;
		}
		
		String stuname=UTFUtil.Utf8Util(request.getParameter("stuname"));
		if("0".equals(stuname)){
			stuname=null;
		}
		int cid = aDailyTalkBiz.getCid(examName);
		int pageNo = super.getPage();
		int row=super.getRows();
		AdailyTalkPage at=new AdailyTalkPage();
		DataGaidModel dgm=new DataGaidModel();
		
		List<AdailyTalkPage> AdailyTalkModel=new ArrayList<AdailyTalkPage>();
//
//		List<ADailyTalk> aDailyTalkInfoHistory = aDailyTalkBiz.findSize(cid);
//
//		totalInfo = aDailyTalkInfoHistory.size();
//		dgm.setTotal(Long.parseLong(totalInfo+""));
		
		List<ADailyTalk> list = aDailyTalkBiz.findADailyTalkBytime(cid, uname,startDate,endDate,stuname,	pageNo, row);
		totalInfo = aDailyTalkBiz.findADailyTalkSize(cid, uname, startDate, endDate, stuname).size();
		dgm.setTotal(Long.parseLong(totalInfo+""));
		for(int i=0;i<list.size();i++){
			AdailyTalkPage atp=new AdailyTalkPage();
			
			String a=list.get(i).getSpeakdate().toString();
			String b=a.split(" ")[0];
			atp.setId(list.get(i).getId());
			atp.setName(list.get(i).getName());
			atp.setKnowledge(list.get(i).getKnowledge());
			atp.setAssessment(list.get(i).getAssessment());
			atp.setSpeakdate(java.sql.Date.valueOf(b));
			atp.setStatus(list.get(i).getStatus());
			atp.setUname(uname);
			if("1".equals(list.get(i).getStatus()+"")||list.get(i).getStatus()==1){
				atp.setSta("资料准备中");
			}else if("2".equals(list.get(i).getStatus()+"")||list.get(i).getStatus()==2){
				atp.setSta("资料未上传");
			}else if("3".equals(list.get(i).getStatus()+"")||list.get(i).getStatus()==3){
				atp.setSta("可以下载");
			}
			AdailyTalkModel.add(atp);
			//dgm.setRows(AdailyTalkModel);
		}
		dgm.setRows(AdailyTalkModel);

		String json = JSON.toJSONStringWithDateFormat(dgm, "yyyy-MM-dd");
		// String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.PrettyFormat);
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		try {
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 下载
	public void download() {
		String id = request.getParameter("id").toString(); // 新技术编号
		// String path=request.getParameter("path").toString(); //文件路径
		List<ADailyTalk> list = null;
		
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		list = aDailyTalkBiz.downloadKnowledgeInfos(Integer.parseInt(id));
		File downFile;
		if (list != null && list.size() > 0) {
			// String moduleDir =
			// this.getServletContext().getRealPath(this.filepath);
			String moduleDir = request.getScheme() + "://"
					+ request.getLocalAddr() + ":" + request.getServerPort()
					+ request.getContextPath();
			//org.newboy.io.FileUtils.newFolder(moduleDir);
			//http://127.0.0.1:80/WebExamination/upload/fpc201405112053.rar
			String local = "http://" + request.getLocalAddr();
			local += ":" + request.getLocalPort() + request.getContextPath()
					+ "/upload/" + list.get(0).getRemark();
			//C:/Program Files (x86)/apache-tomcat-7.0.42/webapps/WebExamination/upload
			downFile = new File(local);
			map.put("local", local);
			list1.add(map);
			String json=this.writeJson(0, list1);
			out.println(json);
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 获取每日一讲的知识点的名字
	 */
	public void getKnow(){
		String id = request.getParameter("id").toString();
		String know=aDailyTalkBiz.findADailyTalkDetail(Integer.parseInt(id));
		out.println(know);
		out.flush();
		out.close();
	}
	
	

	// 上传
//	public void upload() {
//		String id = null; // 新技术编号
//		byte[] bt = null;
//		String fileName = null;
//		int result = 0;
//		
//
//		// 处理表单其它数据
//		String path=request.getParameter("filename");
//		id=request.getParameter("id");
//		fileName = request.getParameter("filename");
//		// 得到上传文件的相关信息，可省
//		fileName = UrlUtils.formatParam(fileName);
//		// 只取后面一段
//		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1,
//				fileName.length());
//		String filetype = fileName.substring(fileName.lastIndexOf("."));
//		fileName = fileName.substring(fileName.lastIndexOf("\\") + 1,
//				fileName.lastIndexOf("."))
//				+ filetype;
//
//		// String moduleDir =
//		// HttpServlet.getServletContext().getRealPath(this.filepath);
//		File uploadedFile = new File(path);
//		try {
//			FileInputStream fis = new FileInputStream(path);
//			bt = new byte[fis.available()];
//			fis.read(bt);
//			fis.close();
//			result = aDailyTalkBiz.uploadKnowledgeInfos(bt, Integer.parseInt(id),fileName);
//			if (result > 0) {
//				List<ADailyTalk> listResult = (List<ADailyTalk>) mysession
//						.getAttribute("saDailyTalkListInfo");
//				if (listResult != null && listResult.size() > 0) {
//					for (ADailyTalk at : listResult) {
//						if (at.getId() == Integer.parseInt(id)) {
//							at.setStatus(3);
//						}
//					}
//				}
//				mysession.setAttribute("saDailyTalkListInfo", listResult);
//				out.println("<script>alert('上传成功!');window.close(); </script>");
//			} else {
//				out.println("<script>alert('上传失败!');window.close();</script>");
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		out.flush();
//		out.close();
//		try {
//			String realpath = ServletActionContext.getServletContext().getRealPath("/upload");
//			//D:\apache-tomcat-6.0.18\webapps\struts2_upload\images
//			System.out.println("realpath: "+realpath);
//			if (image != null) {
//			    File savefile = new File(new File(realpath), imageFileName);
//			    if (!savefile.getParentFile().exists())
//			        savefile.getParentFile().mkdirs();
//			    FileUtils.copyFile(image, savefile);
//			    ActionContext.getContext().put("message", "文件上传成功");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}

	
	
	
	@Override
	public AdailyTalkPage getModel() {
		return adailyTalkPage;
	}
}
