package com.yc.webexam.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.yc.biz.ExamineeBiz;
import com.yc.biz.ExamineeClassBiz;
import com.yc.biz.SystemUserBiz;

public class ValidateuserAction extends BaseAction implements ServletResponseAware
{
	private static final long serialVersionUID = -5236216596902268720L;
	private ExamineeClassBiz examineeClassBiz;
	private SystemUserBiz systemUserBiz;
	private ExamineeBiz examineeBiz;
	private String jspPage;
	private String authCode;// 输入完验证码时，检验验证码是否正确
	private String thisclass;// 输入班级完成后检查是否存在此班级名称
	private String name;// 输入用户名完成后检查是否存在此用户名称
	private String password;
	private String status;
	private HttpServletResponse response;
	private PrintWriter out;
	HttpServletRequest request = ServletActionContext.getRequest();

	@Resource(name = "examineeClassBiz")
	public void setExamineeClassBiz(ExamineeClassBiz examineeClassBiz)
	{
		this.examineeClassBiz = examineeClassBiz;
	}

	@Resource(name = "systemUserBiz")
	public void setSystemUserBiz(SystemUserBiz systemUserBiz)
	{
		this.systemUserBiz = systemUserBiz;
	}

	@Resource(name = "examineeBiz")
	public void setExamineeBiz(ExamineeBiz examineeBiz)
	{
		this.examineeBiz = examineeBiz;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getStatus()
	{
		return status;
	}

	public void setJspPage(String jspPage)
	{
		this.jspPage = jspPage;
	}

	public void setAuthCode(String authCode)
	{
		this.authCode = authCode;
	}

	public void setThisclass(String thisclass)
	{
		this.thisclass = thisclass;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0)
	{
		this.response = arg0;

	}

	public void validate()
	{
		try
		{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=utf-8");

			out = response.getWriter();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if ("examineeClass".equals(jspPage))
		{
			// 当验证的是班级时
			int classId = this.examineeClassBiz.getClassIdOfname(thisclass);
			if (classId == 0)
			{
				out.print("输入的班级名称不存在!");
			}
			else
			{
				out.print("0");
			}
		}
		else if ("systemUser".equals(jspPage))
		{
			// 验证系统用户时
			try
			{
				name = URLDecoder.decode(name, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			if (this.systemUserBiz.isExist(name))
			{
				out.print("0");
			}
			else
			{
				out.print("输入的用户名不存在");
			}
		}
		else if ("examineeName".equals(jspPage))
		{
			// 验证考生时
			int classId = this.examineeClassBiz.getClassIdOfname(thisclass);

			try
			{
				name = URLDecoder.decode(name, "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}

			boolean flag = examineeBiz.isClassExaminee(name, classId);

			if (flag)
			{
				out.print("0");
			}
			else
			{
				out.print("不存在此班级的考生名称!");
			}

		}
		else if ("userPassword".equals(jspPage))
		{
			// 验证系统用户密码时
			if (systemUserBiz.isExist(name, password))
			{
				out.print("0");
			}
			else
			{
				out.print("输入的密码不正确!");
			}

		}
		else if ("userPassword".equals(jspPage))
		{
			// 验证考生密码时
			int classId = this.examineeClassBiz.getClassIdOfname(thisclass);
			if (examineeBiz.isClassExaminee(name, classId, password))
			{
				out.print("0");
			}
			else
			{
				out.print("输入的密码不正确!");
			}
		}

		// 验证验证码
		else if ("authCode".equals(jspPage))
		{
			// 大小写不敏感
			String code = (String) session.get("SESSION_SECURITY_CODE");// 获得真实的验证码
			if (!code.equalsIgnoreCase(authCode))
			{
				out.print("输入的验证码不正确!");
			}
			else
			{
				out.print("0");
			}
		}

		out.flush();
		out.close();

	}

}
