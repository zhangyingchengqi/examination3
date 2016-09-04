package com.yc.filter;



import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class StudentLoginFilter extends HttpServlet implements Filter {
	
	@Override
	public void doFilter(ServletRequest sRequest, ServletResponse sResponse,        
            FilterChain filterChain) throws IOException, ServletException{  
          
        HttpServletRequest request = (HttpServletRequest) sRequest;        
        HttpServletResponse response = (HttpServletResponse) sResponse; 
        //response.setCharacterEncoding("utf-8");
        //request.setCharacterEncoding("utf-8");
        //PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();        
        //String contextPath=request.getContextPath();
        String uname=(String) session.getAttribute("userName");
        String identity=(String) session.getAttribute("identity");
        System.out.println(identity+"-----------"+uname);
        if(identity==null||"".equals(identity)){
        	response.sendRedirect("/login.html");   
            return; 
        }
        if(identity.equals("2")){
        	response.sendRedirect("/login.html");   
            return; 
        }
        if(uname==null||"".equals(uname)){
        	//out.print("<script>alert('对不起，您还没有登录，不能进行此操作，请您先登录！');location.href='http://localhost:8080/Examination2.0';</script>");
        	//out.flush();out.close();
        	response.sendRedirect("/login.html");   
            return; 
        }
        filterChain.doFilter(sRequest, sResponse);      
    }    

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	

}
