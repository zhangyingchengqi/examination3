package com.yc.webexam.actions;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.yc.biz.LabPaperBiz;
import com.yc.biz.WritingPaperBiz;
import com.yc.utils.ExamUtil;
import com.yc.utils.ValueUtil;

public class ExamArrangementAction extends BaseAction implements
		ServletRequestAware, ServletResponseAware {
	private static final Logger logger = Logger
			.getLogger(ExamArrangementAction.class);
	private WritingPaperBiz writingPaperBiz;
	private LabPaperBiz labPaperBiz;
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private PrintWriter out;
	private ArrayList arr = new ArrayList();

	@Resource(name = "writingPaperBiz")
	public void setWritingPaperBiz(WritingPaperBiz writingPaperBiz) {
		this.writingPaperBiz = writingPaperBiz;
	}

	@Resource(name = "labPaperBiz")
	public void setLabPaperBiz(LabPaperBiz labPaperBiz) {
		this.labPaperBiz = labPaperBiz;
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
			this.session = this.request.getSession();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
	}

	public String examType() {
		String paperType = request.getParameter("type");
		String strClass = ValueUtil.formatRequestStr(request.getSession()
				.getAttribute("examClass"));
		// 当请求的笔试卷时
		if (paperType == null || paperType.equals("writ")) {
			paperType = "writ";
			// 找已经安排了考试的试卷和未评的试卷，只显示这两种
			ArrayList arrIsTesting = (ArrayList) writingPaperBiz
					.searchWritingPaper(ExamUtil.PAPER_STATUS_IS_TESTING,
							strClass);
			// 如果找不到，则找未评的试卷（避免后来的同学还没考，已经有人交了卷的时候，因为试卷状态发生了变化，所以找不到试卷了）
			ArrayList arrNotView = (ArrayList) writingPaperBiz
					.searchWritingPaper(ExamUtil.PAPER_STATUS_NOT_VIEW,
							strClass);
			arrIsTesting.addAll(arrNotView);
			arr = arrIsTesting;
		}
		// 当请求的是机试卷时
		else if (paperType.equals("lab")) {
			// 这里没有改，后面要改
			arr = (ArrayList) labPaperBiz.searchLabPaper(
					ExamUtil.PAPER_STATUS_IS_TESTING, strClass);
		}
		// 把查询到的信息存入请求对象中，再转向到页面
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		map.put("arr", arr);
		map.put("paperType", paperType);
		list.add(map);
		String dataInfo=writeJson(0,list);
		out.print(dataInfo);
		out.flush();
		out.close();
		
		// request.setAttribute("arr", arr);
		// request.setAttribute("paperType", paperType);
		return "examhomepageseccuss";
	}
}
