//根据学期显示班级 
	var selectedvalue="";
	function createSelectOption(s){
 		selectedvalue=s;
 		$.getJSON("/Examination2.0/writingPaper_getExamineeClassName.action",{"semester":selectedvalue}, function(data){
				var examineeClassList= eval("("+data+")");
				var optionstr="<option>--请选择--</option>";	
				if(examineeClassList!=null){
					$.each(examineeClassList, function(i,examineeClass){
  					optionstr+="<option value='"+examineeClass.className+"' name='className'>"+examineeClass.className+"</option>";	  														
					});	
				}																
				$("#examClassName").html(optionstr);
  				});  				  		
 	}
 	$(function(){
 		selectedvalue="S1";
 		
 		$.getJSON("/Examination2.0/writingPaper_getExamineeClassName.action",{"semester":selectedvalue}, function(data){
				var examineeClassList= eval("("+data+")");
				var optionstr="<option>--请选择--</option>";																
				if(examineeClassList!=null){
					$.each(examineeClassList, function(i,examineeClass){
  					optionstr+="<option value='"+examineeClass.className+"' name='className'>"+examineeClass.className+"</option>";	  														
					});	
				}			
				$("#examClassName").html(optionstr);
  				});  			
 	});
 	//显示课程名字下拉框调用后台
 	function changeClassName(className){
 		var semester=selectedvalue;
 		//alert("className:"+className);
 		if(className!="--请选择--"&&className!=""){
 			$.ajax({
 				url:"/Examination2.0/pointPaper_findSubjectInfo.action",
 				type:"post",
 				datatype:"json",
 				data:{"className":className,"semester":semester},
 				success:showSubjectName
 				});
 		}else{
 			$("#subjectName").html("<option>--请选择--</option>");
 		}
 		
		
 	}
 	//显示课程名字下拉框的页面实现
 	function showSubjectName(data){
 		var subjectNames = eval("(" + data + ")");
 		if(subjectNames.responseCode==0){
 			var optionstr="<option>--请选择--</option>";
 			if(subjectNames.obj!=null&&subjectNames.obj.length>0){
 				for(var i=0;i<subjectNames.obj.length;i++){
 					optionstr+="<option value='"+subjectNames.obj[i][0]+"' name='className'>"+subjectNames.obj[i][1]+"</option>";	
 				}
 				$("#subjectName").html(optionstr);
 			}else{
 				$("#subjectName").html("<option>--请选择--</option>");
 			}
 			
 		}else{
 			$("#subjectName").html("<option>--请选择--</option>");
 			
 		}
 	}
 	//根据课程id和班级名称来查找知识点统计的信息（向后台请求数据）
 	function findPointPaper(){
 		var className=$.trim($("#examClassName").val());
 		var subjectId=$.trim($("#subjectName").val());
 		if(className=="--请选择--"){
 			className=="";
 		}
 		if(subjectId!="--请选择--"&&className!=""){
	 		$.ajax({
				url:"/Examination2.0/pointPaper_findPointPaperInfo.action",
				type:"post",
				datatype:"json",
				data:{"className":className,"subjectId":subjectId},
				success:showPointPaper
				});
	 	}else{
	 		$("#subjectNameMsg").text("*必选！");
	 		var str=" <table width='680' border='1' align='center' cellpadding='1' bordercolor='#FFFFFF' cellspacing='0'>";
	 		str+="<td colspan='6' style='color:red'>对不起,没有符合查询条件的测评试卷!</td>";
	 		$("#paperanswerinfo").html(str);
	 	}
 	}
 	//验证课程是否已选
 	$(document).ready(function(){
 		$('#subjectName').change(function(){
 			var subjectId=$.trim($("#subjectName").val());
 			if(subjectId!="--请选择--"){
 				$("#subjectNameMsg").text("*");
 			}else{
 				$("#subjectNameMsg").text("*必选！");
 			}
 		})
 		}) 
 	//根据课程id和班级名称来查找知识点统计的信息（向页面显示）
 	function showPointPaper(data){
 		var pointPapers = eval("(" + data + ")");
 		if(pointPapers.responseCode==0){
 			var str=" <table width='680' border='1' align='center' cellpadding='1' bordercolor='#FFFFFF' cellspacing='0'>";
				if(pointPapers.obj==null&&pointPapers.obj.length==0){
					str+="<td colspan='6' style='color:red'>对不起,没有符合查询条件的测评试卷!</td>";
				}else{
					var str=" <table width='680' border='1' align='center' cellpadding='1' bordercolor='#FFFFFF' cellspacing='0'>";
					$.each(pointPapers.obj,function(idx,pointPaper){
						var index=idx+1;
						str+="<tr id=\""+index+"\" bgcolor=\"#EDECEB\" onMouseOver=\"this.bgColor='#93BBDF';\" onMouseOut=\"this.bgColor='#EDECEB';\" align=\"center\">";
						str+="<td align=\"center\" width=\"12%\">"+index+"</td>";
						str+="<td width=\"25%\">"+pointPaper.pname+"</td>";
						str+="<td width=\"15%\">"+pointPaper.examineeClass.className+"</td>";
						str+="<td align=\"center\" width=\"17%\">"+pointPaper.subject.subjectName+"</td>";
						str+="<td align=\"center\" width=\"15%\">"+pointPaper.pdate+"</td>";
						str+="<td align=\"center\" width=\"15%\">";
						str+="<a href=\"javascript:showPointTotal('"+pointPaper.pid+"','"+pointPaper.examineeClass.className+"','"+pointPaper.pdate+"','"+pointPaper.pstatus+"','"+pointPaper.subject.subjectName+"','"+pointPaper.pname+"','"+pointPaper.descript +"','"+pointPaper.ptitle+"')\" title=\"查看统计\">[查看统计]</a>&nbsp;&nbsp;";
						str+="</td></tr>"
					});
				}
				str+="</table>";
				$("#paperanswerinfo").html(str);
 		}else{
 			alert("没有该条件对应的测评信息!");
 		}
 	}
 	//把该页的部分信息存到localStorage或cookie中下一个页面取
 	function showPointTotal(pid,className,pdate,pstatus,subjectName,pname,descript,ptitle){
 		var subjectId=$.trim($("#subjectName").val());
 		if (window.localStorage) {
   			localStorage.setItem("pointPaper_pid", pid);
   			localStorage.setItem("pointPaper_className", className);
   			localStorage.setItem("pointPaper_pdate", pdate);
   			localStorage.setItem("pointPaper_pstatus", pstatus);
   			localStorage.setItem("pointPaper_subjectName", subjectName);
   			localStorage.setItem("pointPaper_pname", pname);
   			localStorage.setItem("pointPaper_descript", descript);
   			localStorage.setItem("pointPaper_ptitle", ptitle);
   			localStorage.setItem("pointPaper_subjectId",subjectId);
		} else {
    		 Cookie.write("pointPaper_pid", pid);               
    		 Cookie.write("pointPaper_className", className);   
    		 Cookie.write("pointPaper_pdate", pdate);           
    		 Cookie.write("pointPaper_pstatus", pstatus);        
    		 Cookie.write("pointPaper_subjectName", subjectName);
    		 Cookie.write("pointPaper_pname", pname);            
    		 Cookie.write("pointPaper_descript", descript); 
    		 Cookie.write("pointPaper_ptitle", ptitle);  
    		 Cookie.write("pointPaper_subjectId",subjectId); 
		}                                                        
 		window.location.href='/Examination2.0/back/grade/showpointtotal.html';
 	}
 	