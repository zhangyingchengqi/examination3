package com.yc.webexam.actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ModelDriven;
import com.yc.biz.ChapterBiz;
import com.yc.biz.ExamineeClassBiz;
import com.yc.biz.PointAnswerBiz;
import com.yc.biz.PointInfoBiz;
import com.yc.biz.PointPaperBiz;
import com.yc.biz.SubjectBiz;
import com.yc.po.Chapter;
import com.yc.po.ExamineeClass;
import com.yc.po.PointPaper;
import com.yc.po.Subject;
import com.yc.utils.JsonProtocol;
import com.yc.utils.JsonUtil;
import com.yc.vo.DataGaidModel;
import com.yc.vo.PointPaperModel;
import com.yc.vo.WritingQuestionPaper;

public class AssessmentPaperAction extends BaseAction implements ModelDriven<PointPaperModel>{
	private static final Logger logger = Logger.getLogger(LoginAction.class);
	private PointPaperModel pointPaperModel=new PointPaperModel();
	private PointInfoBiz pointInfobiz;
	@Resource(name = "pointInfoBiz")
	public void setPointInfobiz(PointInfoBiz pointInfobiz) {
		this.pointInfobiz = pointInfobiz;
	}
	private PointPaperBiz pointPaperBiz;
	@Resource(name = "pointPaperBiz")
	public void setPointPaperbiz(PointPaperBiz pointPaperbiz) {
		this.pointPaperBiz = pointPaperbiz;
	}
	private SubjectBiz subjectBiz;
	@Resource(name = "subjectBiz")
	public void setSubjectBiz(SubjectBiz subjectBiz) {
		this.subjectBiz = subjectBiz;
	}
	private ExamineeClassBiz examineeClassBiz;
	@Resource(name = "examineeClassBiz")
	public void setExamineeClassBiz(ExamineeClassBiz examineeClassBiz) {
		this.examineeClassBiz = examineeClassBiz;
	}
	private PointAnswerBiz pointAnswerBiz;
	@Resource(name = "pointAnswerBiz")
	public void setPointAnswerBiz(PointAnswerBiz pointAnswerBiz) {
		this.pointAnswerBiz = pointAnswerBiz;
	}
	//添加试卷
	public void newPointPaper(){
		String jsonStr="";
		try {
			Subject sb=subjectBiz.findSubjectById(pointPaperModel.getSid());
			ExamineeClass ec=examineeClassBiz.findExamineeClassById(pointPaperModel.getCid());
			PointPaper po=new PointPaper();
			po.setPname(pointPaperModel.getPname());
			po.setPdate(pointPaperModel.getExamDate());
			po.setPstatus(pointPaperModel.getPstatus());
			po.setPaperPwd(pointPaperModel.getPaperPwd());
			po.setPtitle(pointPaperModel.getPtitle());
			po.setDescript(pointPaperModel.getDescript());
			po.setSubject(sb);
			po.setExamineeClass(ec);
			pointPaperBiz.addPointPaper(po);
			jsonStr=super.writeJson(0, "出卷成功！");
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "出卷失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	
		//更新试卷
		public void updatePointPaper(){
			String jsonStr="";
			try {
				Subject sb=subjectBiz.findSubjectById(pointPaperModel.getSid());
				ExamineeClass ec=examineeClassBiz.findExamineeClassById(pointPaperModel.getCid());
				PointPaper po=new PointPaper();
				po=pointPaperBiz.findPointPaperById(pointPaperModel.getPid());
				po.setPname(pointPaperModel.getPname());
				po.setPdate(pointPaperModel.getExamDate());
				po.setPstatus(pointPaperModel.getPstatus());
				po.setPaperPwd(pointPaperModel.getPaperPwd());
				po.setPtitle(pointPaperModel.getPtitle());
				po.setDescript(pointPaperModel.getDescript());
				po.setSubject(sb);
				po.setExamineeClass(ec);
				pointPaperBiz.updatePointPaper(po);
				jsonStr=super.writeJson(0, "编辑成功！");
			} catch (Exception e) {
				jsonStr=super.writeJson(1, "编辑失败！请稍后再试");
				logger.error(e);
			}finally{
				try {
					JsonUtil.jsonOut(jsonStr);
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	//按条件查询试卷
	public void findPointPaper(){
		String jsonStr="";
		String date="";
		List<PointPaper> pointPaper=new ArrayList<PointPaper>();
		List<PointPaperModel> list=new ArrayList<PointPaperModel>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(pointPaperModel.getExamDate()!=null){
			date=sdf.format(pointPaperModel.getExamDate());
		}
		try {
			if(pointPaperModel.getPid()!=null){
				pointPaper.add(pointPaperBiz.findPointPaperById(pointPaperModel.getPid()));
			}else{
				pointPaper=pointPaperBiz.findPointPaper(pointPaperModel.getSid(), pointPaperModel.getCid(),date);
			}
			for(int i=0;i<pointPaper.size();i++){
				PointPaperModel pm=new PointPaperModel();
				pm.setClassName(pointPaper.get(i).getExamineeClass().getClassName());
				pm.setExamDate(pointPaper.get(i).getPdate());
				pm.setPname(pointPaper.get(i).getPname());
				pm.setPstatus(pointPaper.get(i).getPstatus());
				pm.setPid(pointPaper.get(i).getPid());
				pm.setSubjectName(pointPaper.get(i).getSubject().getSubjectName());
				pm.setPaperPwd(pointPaper.get(i).getPaperPwd());
				pm.setCid(pointPaper.get(i).getExamineeClass().getId());
				pm.setPtitle(pointPaper.get(i).getPtitle());
				pm.setSid(pointPaper.get(i).getSubject().getId());
				pm.setDescript(pointPaper.get(i).getDescript());
				pm.setDate(sdf.format(pointPaper.get(i).getPdate()));
				list.add(pm);
			}
			JsonProtocol jsonProtocol = null;
			jsonProtocol = new JsonProtocol(0, null, list);
			jsonStr = JSON.toJSONString(jsonProtocol, SerializerFeature.DisableCircularReferenceDetect); 
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "查询失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		
	}
	
	//查询所有试卷
		public void findAllPointPaper(){
			String jsonStr="";
			try {
				List<PointPaper> pointPaper=new ArrayList<PointPaper>();
				List<PointPaperModel> list=new ArrayList<PointPaperModel>();
				DataGaidModel dgm=new DataGaidModel();
				dgm=pointPaperBiz.getAllPointPaper(pointPaperModel);
				pointPaper=dgm.getRows();
				
				for(int i=0;i<pointPaper.size();i++){
					PointPaperModel pm=new PointPaperModel();
					pm.setClassName(pointPaper.get(i).getExamineeClass().getClassName());
					pm.setExamDate(pointPaper.get(i).getPdate());
					pm.setPname(pointPaper.get(i).getPname());
					//pm.setPstatus(pointPaper.get(i).getPstatus());
					if(pointPaper.get(i).getPstatus()==1){
						pm.setPstatusname("未考");
					}else if(pointPaper.get(i).getPstatus()==2){
						pm.setPstatusname("开评");
					}else if(pointPaper.get(i).getPstatus()==3){
						pm.setPstatusname("已评");
					}
					pm.setPid(pointPaper.get(i).getPid());
					list.add(pm);
				}
				dgm.setRows(list);
				jsonStr=JSON.toJSONStringWithDateFormat(dgm, "yyyy-MM-dd",SerializerFeature.DisableCircularReferenceDetect);
			} catch (Exception e) {
				DataGaidModel dgm=new DataGaidModel();
				List<PointPaperModel> list=new ArrayList<PointPaperModel>();
				dgm.setRows(list);
				dgm.setTotal((long) 0);
				jsonStr=JSON.toJSONStringWithDateFormat(dgm, "yyyy-MM-dd",SerializerFeature.DisableCircularReferenceDetect);
			}finally{
				try {
					JsonUtil.jsonOut(jsonStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	//修改试题状态
	public void  changePointPaperStatus(){
		String jsonStr="";
		try {
			PointPaper po=new PointPaper();
			po=pointPaperBiz.findPointPaperById(pointPaperModel.getPid());
			po.setPstatus(pointPaperModel.getPstatus());
			pointPaperBiz.updatePointPaperStatus(po);
			jsonStr=super.writeJson(0, "开考成功！");
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "开考失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		
	}
	//删除试卷
	public void delPointPaper(){
		String jsonStr="";
		try {
			PointPaper po=new PointPaper();
			po.setPid(pointPaperModel.getPid());
			
			pointPaperBiz.delPointPaperById(po);
			jsonStr=super.writeJson(0, "删除成功！");
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "删除失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	//显示试卷题目
	public void showPointInfo(){
		String jsonStr="";
		try {
			String title=pointPaperModel.getPtitle().substring(0, pointPaperModel.getPtitle().length()-1);
			String[] ptitles=title.split(",");
			List<String> list=new ArrayList<String>();
			String pointName="";
			for(int i=0;i<ptitles.length;i++){
				pointName=pointInfobiz.findPointInfoById(Integer.parseInt( ptitles[i]));
				list.add(pointName);
			}
			jsonStr=super.writeJson(0, list);
		} catch (NumberFormatException e) {
			jsonStr=super.writeJson(1, "失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	//统计参加测评的人数
	public void studentCount(){
		String jsonStr="";
		try {
			int count=0;
			count=pointAnswerBiz.getStudentCount(pointPaperModel.getPid());
			jsonStr=super.writeJson(0, count);
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "失败！");
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
	public PointPaperModel getModel() {
		return this.pointPaperModel;
	}
	
	
}
