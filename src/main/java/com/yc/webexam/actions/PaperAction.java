package com.yc.webexam.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;



import com.opensymphony.xwork2.ModelDriven;
import com.yc.biz.EditionBiz;
import com.yc.biz.ExamineeClassBiz;
import com.yc.biz.SubjectBiz;
import com.yc.biz.WritingPaperBiz;
import com.yc.biz.WritingQuestionBiz;
import com.yc.po.WritingPaper;
import com.yc.utils.ExamUtil;
import com.yc.utils.JsonUtil;
import com.yc.vo.WritingPaperModel;

public class PaperAction extends BaseAction implements ModelDriven<WritingPaperModel>
		 {
	private WritingPaperModel writingPaperModel=new WritingPaperModel();
	private static final Logger logger = Logger.getLogger(PaperAction.class);
	
	private ServletContext s = ServletActionContext.getServletContext();
	@Resource(name="writingPaperBiz")
	private WritingPaperBiz writingPaperBiz;
	public void setWritingPaperBiz(WritingPaperBiz writingPaperBiz) {
		this.writingPaperBiz = writingPaperBiz;
	}
	@Resource(name="editionBiz")
	private EditionBiz  editionBiz ;
	public void setEditionBiz(EditionBiz editionBiz) {
		this.editionBiz = editionBiz;
	}
	@Resource(name="examineeClassBiz")
	private ExamineeClassBiz  examineeClassBiz ;
	public void setExamineeClassBiz(ExamineeClassBiz examineeClassBiz) {
		this.examineeClassBiz = examineeClassBiz;
	}
	@Resource(name="subjectBiz")
	private SubjectBiz subjectBiz ;
	public void setSubjectBiz(SubjectBiz subjectBiz) {
		this.subjectBiz = subjectBiz;
	}
	@Resource(name="writingQuestionBiz")
	private WritingQuestionBiz  writingQuestionBiz ;
	public void setWritingQuestionBiz(WritingQuestionBiz writingQuestionBiz) {
		this.writingQuestionBiz = writingQuestionBiz;
	}
	
	/*public void searchDifficultyRate(){
		String questionInfo=writingPaperModel.getQuestionInfo();//取出试卷试题信息
		String strAr[] =questionInfo.split("MMM");
		String strQ[] = strAr[0].split(":");
		int subjID = subjectBiz.getSubjectId(strQ[0]);
		String chapterN[] = strQ[1].split(";");
		// String chapterN[] = strQ[1].split(",");
		@SuppressWarnings("unused")
		List chapterIDandCountList = new ArrayList();
		for (int i = 0; i < chapterN.length; i++) {
			int chapterId = chapterBiz.getChapterId(subjID,
					chapterN[i].split(",")[0]);
		}
		writingQuestionBiz.getDifficultyRate(writingPaperModel.getSemester(), subjID, chapterId, difficulty);
	}*/
	

	/*public void addWritingPaper(){
		String jsonStr="";
		try {
			WritingPaper wp=new WritingPaper();//试题对象
			String questionInfo=writingPaperModel.getQuestionInfo();//取出试卷试题信息
			List alQuestions=new ArrayList();
			//生成试卷编号
			String strAr[] =questionInfo.split("MMM"); // 拆分出题条件
			String paperId="";//试卷编号
			// 连接生成试卷的题目编号
			String strQuestionID = "";
			//查出符合要求的试题
			if (strAr.length == 1) {
				paperId=this.createWritingPaperId(writingPaperModel.getEdition(), writingPaperModel.getSemester(), writingPaperModel.getExamSubject(),
						 writingPaperModel.getExamineeClass());
				alQuestions=writingPaperBiz.getQuestionIds(writingPaperModel.getExamineeClass(), writingPaperModel.getExamSubject());
				strQuestionID = "";
				// 连接生成试卷的题目编号
				strQuestionID = writingQuestionBiz.getQuestionIdsOf(strAr[0], writingPaperModel.getSemester(),
						writingPaperModel.getCountOfQuestion(), writingPaperModel.getRate1(), writingPaperModel.getRate2(), writingPaperModel.getRate3(), alQuestions);
			}else{
				paperId=this.createWritingPaperId(writingPaperModel.getEdition(), writingPaperModel.getSemester(), "0",
						 writingPaperModel.getExamineeClass());
				alQuestions=writingPaperBiz.getQuestionIds(writingPaperModel.getExamineeClass(), "综合");
				strQuestionID = "";
				for (int i = 0; i < strAr.length; i++) {
					String ss= writingQuestionBiz.getQuestionIdsOf(strAr[i],  writingPaperModel.getSemester(),
							writingPaperModel.getCountOfQuestion(),writingPaperModel.getRate1(),writingPaperModel.getRate2(),writingPaperModel.getRate3(), alQuestions);
					if (ss.equals("0")) {
						strQuestionID = ss;
						break;
					} else {
						strQuestionID += ss;
					}
				}
			}
			
			if (strQuestionID.equals("0")) {
				jsonStr=super.writeJson(1, "数据库中符合条件的题目不够数!请重新再出题.");
			
			} else{
				wp.setId(paperId);
				wp.setExamineeClass(writingPaperModel.getExamineeClass());
				wp.setExamSubject(writingPaperModel.getExamSubject());
				wp.setPaperName(writingPaperModel.getPaperName());
				wp.setPaperPwd(writingPaperModel.getPaperPwd());
				wp.setPaperStatus(1);
				wp.setCountOfQuestion(writingPaperModel.getCountOfQuestion());
				wp.setExamDate(writingPaperModel.getExamDate());
				wp.setQuestionId(strQuestionID);
				wp.setTimesConsume(writingPaperModel.getTimesConsume());
				wp.setRemark(writingPaperModel.getRemark());
				wp.setQuestionCorrect(writingQuestionBiz.getAnswers(strQuestionID));
				wp.setQuestionInfo(writingPaperModel.getQuestionInfo());
				String userName=(String) session.get("userName");
				wp.setOperator(userName);
				// 添加试卷
				writingPaperBiz.addWritingPaper(wp);
				jsonStr=super.writeJson(0, "试卷生成成功");
			}
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "试卷生成出现异常，请联系管理员或稍候再试。");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	
	}*/
	
	/**
	 * 添加考试试卷
	 */
	public void addWritingPaper(){
		String jsonStr="";
		try {
			WritingPaper wp=new WritingPaper();//试题对象
			String questionInfo=writingPaperModel.getQuestionInfo();//取出试卷试题信息
			//生成试卷编号
			String strAr[] =questionInfo.split("MMM"); // 拆分出题条件
			String paperId="";//试卷编号
			// 连接生成试卷的题目编号
			String strQuestionID = "";
			//查出符合要求的试题
			if (strAr.length == 1) {
				paperId=this.createWritingPaperId(writingPaperModel.getEdition(), writingPaperModel.getSemester(), writingPaperModel.getExamSubject(),
						 writingPaperModel.getExamineeClass());
				strQuestionID = "";
				// 连接生成试卷的题目编号
				strQuestionID = writingQuestionBiz.getQuestionIdsOf(strAr[0], writingPaperModel.getSemester(),
						writingPaperModel.getCountOfQuestion(), writingPaperModel.getRate1(), writingPaperModel.getRate2(), writingPaperModel.getRate3());
			}else{
				paperId=this.createWritingPaperId(writingPaperModel.getEdition(), writingPaperModel.getSemester(), "0",
						 writingPaperModel.getExamineeClass());
				strQuestionID = "";
				for (int i = 0; i < strAr.length; i++) {
					String ss= writingQuestionBiz.getQuestionIdsOf(strAr[i],  writingPaperModel.getSemester(),
							writingPaperModel.getCountOfQuestion(),writingPaperModel.getRate1(),writingPaperModel.getRate2(),writingPaperModel.getRate3());
					if (ss.equals("0")) {
						strQuestionID = ss;
						break;
					} else {
						strQuestionID += ss;
					}
				}
			}
			
			if (strQuestionID.equals("0")) {
				jsonStr=super.writeJson(1, "数据库中符合条件的题目不够数!请重新再出题.");
			
			} else{
				wp.setId(paperId);
				wp.setExamineeClass(writingPaperModel.getExamineeClass());
				wp.setExamSubject(writingPaperModel.getExamSubject());
				wp.setPaperName(writingPaperModel.getPaperName());
				wp.setPaperPwd(writingPaperModel.getPaperPwd());
				wp.setPaperStatus(1);
				wp.setCountOfQuestion(writingPaperModel.getCountOfQuestion());
				wp.setExamDate(writingPaperModel.getExamDate());
				wp.setQuestionId(strQuestionID);
				wp.setTimesConsume(writingPaperModel.getTimesConsume());
				wp.setRemark(writingPaperModel.getRemark());
				wp.setQuestionCorrect(writingQuestionBiz.getAnswers(strQuestionID));
				wp.setQuestionInfo(writingPaperModel.getQuestionInfo());
				String userName=(String) session.get("userName");
				wp.setOperator(userName);
				// 添加试卷
				writingPaperBiz.addWritingPaper(wp);
				jsonStr=super.writeJson(0, "试卷生成成功");
			}
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "试卷生成出现异常，请联系管理员或稍候再试。");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	
	}
	
	
	/**
	 * 根据版本,学期,班级,科目(综合科:0)和当前时间生成试卷编号
	 * 
	 * @param version
	 *            String 版本
	 * @param semester
	 *            String 学期
	 * @param examClass
	 *            String 班级
	 * @return String
	 */
	private  String createWritingPaperId(String version, String semester,
			String subject, String examClass) {
		String paperID = ""; // 考卷ID
		String vid=Integer.toString(editionBiz.getEditionId(version));
		String eid=Integer.toString(examineeClassBiz.getClassIdOfname(examClass));
		String sid;
		if(subject.endsWith("0")){
			sid=0+"";
		}else{
			sid=Integer.toString(subjectBiz.getSubjectId(subject));
		}
		// 根据系统时间生成年月日时分组成的字符串
		String nowT = ExamUtil.getNowDate("yyyyMMddhhmmss");
		// 版本，学期，考试班级，科目编号(综合试卷的科目为:0)生成考卷ID
		paperID = vid + semester + eid + sid + nowT;
		return paperID;
	}


	@Override
	public WritingPaperModel getModel() {
		return this.writingPaperModel;
	}

}