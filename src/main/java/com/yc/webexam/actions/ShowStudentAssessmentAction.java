package com.yc.webexam.actions;

import javax.annotation.Resource;

import com.yc.biz.PointPaperBiz;

public class ShowStudentAssessmentAction extends BaseAction {
	private int classId;//班级编号

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}
	
	private PointPaperBiz pointPaperBiz;
	@Resource(name = "pointPaperBiz")
	public void setPointPaperbiz(PointPaperBiz pointPaperbiz) {
		this.pointPaperBiz = pointPaperbiz;
	}
	//根据班级编号查出参加测评的学生名
	public void showStudent(){
		
	}
}
