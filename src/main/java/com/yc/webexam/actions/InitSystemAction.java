package com.yc.webexam.actions;

import java.util.HashMap;
import javax.annotation.Resource;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import org.springframework.stereotype.Controller;

import com.yc.biz.EditionBiz;

@Controller("initSystemAction")
public class InitSystemAction extends BaseAction
{
	private static final long serialVersionUID = -3651155643108111565L;
	private EditionBiz editionBiz;

	@Resource(name = "editionBiz")
	public void setEditionBiz(EditionBiz editionBiz)
	{
		this.editionBiz = editionBiz;
	}

	public String execute()
	{
		HashMap<Integer, String> paperStatus = new HashMap<Integer, String>();
		paperStatus.put(1, "未考"); // 试卷未考
		paperStatus.put(2, "未评"); // 试卷未评
		paperStatus.put(3, "已评"); // 试卷已评
		paperStatus.put(4, "考试"); // 试卷正在考试

		session.put("paperStatus", paperStatus);

		// 相应的默认版本号从数据库中得到，默认的版本名
		String currentUse = editionBiz.getEditionName();
 

		session.put("editionName", currentUse);
		return "success";
	}
	
	

}
