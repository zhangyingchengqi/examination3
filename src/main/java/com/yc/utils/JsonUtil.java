package com.yc.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;




import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;



public class JsonUtil {
	
	/**
	 * 把json字符串传到页面中去
	 * @param jsonStr json字符串
	 * @throws IOException 先抛出,待处理
	 * @author fanhaohao
	 */
	public static void jsonOut(String jsonStr) throws IOException{
		HttpServletResponse response=ServletActionContext.getResponse();
		PrintWriter out=response.getWriter();
		out.print(jsonStr);
		out.flush();
		out.close();
	}
	
	public static void writeJson(Object obj){
		try {
			String json=JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd",SerializerFeature.DisableCircularReferenceDetect);
			//json=JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
