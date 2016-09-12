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
import com.yc.biz.CheckAnswerBiz;
import com.yc.biz.CheckingBiz;
import com.yc.biz.ExamineeClassBiz;
import com.yc.biz.SystemUserBiz;
import com.yc.biz.TempBiz;
import com.yc.po.ADailyTalk;
import com.yc.po.CheckAnswer;
import com.yc.po.Checking;
import com.yc.po.Examinee;
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
public class CheckAnswerAction extends BaseAction {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CheckAnswerAction.class);

    private PrintWriter out;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
    private PageUtil pageUtil = new PageUtil();
    private int totalInfo = 0;
    private int countInfo = 10;
    private int tempsum = 0;
    private String resultListInfo = "";

    private CheckAnswer checkAnswer;

    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private HttpSession mysession = request.getSession();

    @Resource(name = "checkAnswerBiz")
    private CheckAnswerBiz checkAnswerBiz;
    @Resource(name = "examineeClassBiz")
    private ExamineeClassBiz examineeClassBiz;
    @Resource(name = "systemUserBiz")
    private SystemUserBiz systemUserBiz;
    @Resource(name = "tempBiz")
    private TempBiz tempBiz;

    JSONArray json = null;
    JSONObject jb = null;
    String jsonStr = null;

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
     * 提交评教结果
     * 
     * @throws IOException
     */
    @SuppressWarnings("null")
    public void subCheckAnswer() throws IOException {

        request = ServletActionContext.getRequest();
        Integer sys_id = Integer.parseInt(request.getParameter("sys_id"));
        String checkDate = (String) request.getParameter("checkDate");
        String answer = (String) request.getParameter("answer");

        SystemUser systemUser = systemUserBiz.findSystemUserByName(sys_id).get(0);

        if (checkDate == null || "".equals(checkDate)) {
            checkDate = (new Date()).toString();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date a = null;
        try {
            a = sdf.parse(checkDate);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        
        Examinee exam = (Examinee) mysession.getAttribute("Examinee");
        
        String startDate = checkDate;
        String endDate = null; // 查询结束时间

        if (startDate == null || "".equals(startDate)) {
            startDate = sdf.format(new Date());
        }
        String[] strs = startDate.split("-");
        startDate = strs[0] + "-" + strs[1] + "-01";
        endDate = strs[0] + "-" + strs[1] + "-28";

        try {
            Date da1 = sdf.parse(startDate);
            Date b = sdf.parse(endDate);
            boolean flag = a.before(b);
            if (!flag) {
                String c = "";
                c = startDate;
                startDate = endDate;
                endDate = c;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        List<CheckAnswer> checkAnswers = null;
        checkAnswers =  checkAnswerBiz.findCheckAnswerByEidAndDate((exam.getId()+""), (sys_id+""), startDate, endDate);
        
        
        CheckAnswer checkAnswer = new CheckAnswer();
        checkAnswer.setAnswer(answer);
        checkAnswer.setCheckDate(a);
        checkAnswer.setEcid(exam.getExamineeClass().getId()+"");
        checkAnswer.setExaminee(exam);
        checkAnswer.setSystemUser(systemUser);
        checkAnswer.setRemark("学员评教");
        checkAnswer.setStatus(0 + "");

        try {
            System.out.println(checkAnswers);
            if(checkAnswers != null){
                jsonStr = super.writeJson(1, "本月您已经对本教师进行了评教，不能重复操作");
            }else{
                int result = checkAnswerBiz.addCheckAnswer(checkAnswer);
                if (result > 0) {
                    jsonStr = super.writeJson(0, "操作成功");
                } else {
                    jsonStr = super.writeJson(1, "提交评教结果失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = super.writeJson(1, "添加出现异常，请联系管理员");
        } finally {
            JsonUtil.jsonOut(jsonStr);
        }

    }

    // 通过班级和教师查评教结果
    public void showCheckAnswerBySid() throws Exception {
        String cid = request.getParameter("cid").toString().trim(); // 班级
        String sid = request.getParameter("sid").toString().trim(); // 教师
        String startDate = request.getParameter("startDate").toString().trim(); // 查询开始时间
        String endDate = null; // 查询结束时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        if (startDate == null || "".equals(startDate)) {
            startDate = sdf.format(new Date());
        }
        String[] strs = startDate.split("-");
        startDate = strs[0] + "-" + strs[1] + "-01";
        endDate = strs[0] + "-" + strs[1] + "-28";

        Date a = sdf.parse(startDate);
        Date b = sdf.parse(endDate);
        boolean flag = a.before(b);
        if (!flag) {
            String c = "";
            c = startDate;
            startDate = endDate;
            endDate = c;
        }

        List<CheckAnswer> checkAnswer = null;

        checkAnswer = checkAnswerBiz.findCheckAnswerByCid(cid, sid, startDate, endDate);

        // int
        // 用于存答案的统计数据
        int[] avg = new int[26];
        int[] min = new int[26];
        int[] max = new int[26];
        for (int i = 0; i < 26; i++) {
            min[i] = 10;
            max[i] = 0;
        }

        try {
            if (checkAnswer != null && checkAnswer.size() > 0) {
                for (int i = 0; i < checkAnswer.size(); i++) {
                    CheckAnswer ca = checkAnswer.get(i);
                    String answer = ca.getAnswer();
                    String[] str = answer.split("\\-\\|\\-");
                    for (int x = 0; x < 26; x++) {
                        if (str[x] == null || "".equals(str[x])) {
                            str[x] = 0 + "";
                        }
                        int grade = Integer.parseInt(str[x]);
                        avg[x] += grade;
                        if (min[x] > grade) {
                            min[x] = grade;
                        }
                        if (max[x] < grade) {
                            max[x] = grade;
                        }
                    }
                }
                for (int i = 0; i < 26; i++) {
                    avg[i] = avg[i] / checkAnswer.size();
                }

                List<int[]> lists = new ArrayList<int[]>();
                lists.add(min);
                lists.add(max);
                lists.add(avg);
                jsonStr = JSON.toJSONString(lists, SerializerFeature.DisableCircularReferenceDetect);
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

    // 通过班级和学员查评教结果
    public void showCheckAnswerByEid() throws Exception {
        String cid = request.getParameter("cid").toString().trim(); // 班级
        String eid = request.getParameter("eid").toString().trim(); // 教师
        String startDate = request.getParameter("startDate").toString().trim(); // 查询开始时间
        String endDate = null; // 查询结束时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        if (startDate == null || "".equals(startDate)) {
            startDate = sdf.format(new Date());
        }
        String[] strs = startDate.split("-");
        startDate = strs[0] + "-" + strs[1] + "-01";
        endDate = strs[0] + "-" + strs[1] + "-28";

        Date a = sdf.parse(startDate);
        Date b = sdf.parse(endDate);
        boolean flag = a.before(b);
        if (!flag) {
            String c = "";
            c = startDate;
            startDate = endDate;
            endDate = c;
        }

        List<CheckAnswer> checkAnswer = null;

        checkAnswer = (List<CheckAnswer>) checkAnswerBiz.findCheckAnswerByEid(cid, eid, startDate, endDate);

        List<String[]> lists = new ArrayList<String[]>();
        try {
            if (checkAnswer != null && checkAnswer.size() > 0) {
                for (int i = 0; i < checkAnswer.size(); i++) {
                    CheckAnswer ca = checkAnswer.get(i);
                    String answer = ca.getAnswer();
                    String[] str = answer.split("\\-\\|\\-");
                    for (int x = 0; x < 26; x++) {
                        if (str[x] == null || "".equals(str[x])) {
                            str[x] = 0 + "";
                        }
                    }
                    str[28] = ca.getSystemUser().getId()+"";
                    lists.add(str);
                }

                jsonStr = JSON.toJSONString(lists, SerializerFeature.DisableCircularReferenceDetect);
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
    
 // 通过教师和学员查评教结果
    public void showCheckAnswerByEidandSid() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        
        String sid = request.getParameter("sid").toString().trim(); // 教师
        String eid = request.getParameter("eid").toString().trim(); // 学生
        String startDate = request.getParameter("startDate").toString().trim(); // 查询开始时间
        String endDate = sdf.format(new Date()); // 查询结束时间

        if (startDate == null || "".equals(startDate)) {
            startDate = sdf.format(new Date());
        }
        String[] strs = startDate.split("-");
        startDate = strs[0] + "-" + strs[1] + "-01";
        
        String[] strs1 = endDate.split("-");
        endDate = strs1[0] + "-" + strs1[1] + "-28";

        Date a = sdf.parse(startDate);
        Date b = sdf.parse(endDate);
        boolean flag = a.before(b);
        if (!flag) {
            String c = "";
            c = startDate;
            startDate = endDate;
            endDate = c;
        }

        List<CheckAnswer> checkAnswer =  checkAnswerBiz.findCheckAnswerByEidAndDate(eid, sid, startDate, endDate);

        List<String[]> lists = new ArrayList<String[]>();
        try {
            if (checkAnswer != null && checkAnswer.size() > 0) {
                for (int i = 0; i < checkAnswer.size(); i++) {
                    CheckAnswer ca = checkAnswer.get(i);
                    String answer = ca.getAnswer();
                    String[] str = answer.split("\\-\\|\\-");
                    for (int x = 0; x < 26; x++) {
                        if (str[x] == null || "".equals(str[x])) {
                            str[x] = 0 + "";
                        }
                    }
                    str[28] = ca.getSystemUser().getId()+"";
                    lists.add(str);
                }

                jsonStr = JSON.toJSONString(lists, SerializerFeature.DisableCircularReferenceDetect);
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
    
}
