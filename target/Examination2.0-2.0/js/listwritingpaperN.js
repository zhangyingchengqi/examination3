//根据学期显示班级 	
	function createSelectOption(s){
 		var selectedvalue=s;
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
 		var selectedvalue="S1";
 		
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
 	
 	
 	//移到焦点除变颜色
 	function overChangeColor(myColor,myId){
 			$("#"+myId).mouseover(
 					function(){
 						$("#"+myId).css("color",myColor);
 						alert(myColor);
 					}
 				);
 	}
 	//离开焦点除变颜色
 	function outChangeColor(myColor,myId){
 			$("#"+myId).mouseout(
 					function(){
 						$("#"+myId).css("color",myColor);
 					}
 				);
 	}
 	//首页，上一页，下一页，尾页，点击时触发该函数
 	function skipToPageNum(status){
 		
 		//总页数
 		var totalPage=parseInt($.trim($("#tabTotalPage").text()));
 			//alert(totalPage);
 			if(totalPage==""){
 				displayRows=0;
 			}
 		//每页显示几条	
 		var displayRows=$.trim($("#displayRows").val());
 			if(displayRows==""){
 				displayRows=10;
 				$("#displayRows").val(displayRows);
 			}
 		//第几页		
 		var pageNume=$.trim($("#pageNume").val());
 			if(pageNume==""){
 				pageNume=1;
 				$("#pageNume").val(pageNume);
 			}else if(pageNume==0){
 				pageNume=1;
 			}
 		//当前第几页	
 		var currentNume=$.trim($("#tabCurrentPage").text());
 			if(currentNume==""){
 				currentNume=1;
 				$("#tabCurrentPage").val(currentNume);
 			}else if(currentNume==0){
 				currentNume=1;
 			}	
 		//班级名称	
 		var examClassName=$.trim($("#examClassName").val());
 			if(examClassName=="--请选择--"){
 				examClassName="";
 			}
 		if(status=="up"){	//上一页
 			if(currentNume==1){
 				pageNume=currentNume;
 				return;
 			}else{
 				//$("#upPage").animate({color:'red'});
 				pageNume--;
 				$("#pageNume").val(pageNume);
 			}
 		}else if(status=="down"){
 			if(currentNume==totalPage||currentNume+""==totalPage+""){
 				pageNume=currentNume;
 				return;
 			}else{
 				//$("#downPage").animate({color:'red'});
 				pageNume++;
 				$("#pageNume").val(pageNume);
 			}
 		}else if(status=="first"){
 			if(currentNume==1){
 				pageNume=currentNume;
 				return;
 			}else{
 				//overChangeColor("firstPage","blue")
 				pageNume=1;
 				$("#pageNume").val(pageNume);
 			}
 		}else if(status=="last"){
 			if(currentNume==totalPage){
 				pageNume=currentNume;
 				return;
 			}else{
 				//overChangeColor("lastPage","blue")
 				pageNume=totalPage;
 				$("#pageNume").val(pageNume);
 			}
 		}	
 		if(pageNume>=1&&pageNume<=totalPage){
 			$.ajax({
 				url:"/Examination2.0/dataarraylist_showWritingPaperPages.action",
 				type:"post",
 				datatype:"json",
 				data:{"displayRows":displayRows,"pageNume":pageNume,"examClassName":examClassName},
 				success:showPageInfo
 			});
 		}
 				/* $("#lastPage").css("color","black");	
 				$("#firstPage").css("color","black");
 				$("#upPage").css("color","black");
 				$("#downPage").css("color","black"); */
 	}
 	//点击搜索和GO时触发此函数
 	$(function()
 	{
 		$("#search,#searchGo").click(function()
 		{	
 			//总页数
 			var totalPage=parseInt($.trim($("#tabTotalPage").text()));
 			if(totalPage==""){
 				displayRows=0;
 			}
 			//每页显示几条
 			var displayRows=$.trim($("#displayRows").val());
 			if(displayRows==""){
 				displayRows=10;
 				$("#displayRows").val(displayRows);
 			}
 			//第几页
 			var pageNume=$.trim($("#pageNume").val());
 			if(pageNume==""){
 				pageNume=1;
 				$("#pageNume").val(pageNume);
 			}else if(pageNume==0){
 				pageNume=1;
 			}else if(pageNume>=totalPage){
 				pageNume=totalPage;
 				$("#pageNume").val(totalPage);
 			}
 			//班级名称	
 			var examClassName=$.trim($("#examClassName").val());
 			if(examClassName=="--请选择--"){
 				examClassName="";
 			}
 			$.ajax({
 				url:"/Examination2.0/dataarraylist_showWritingPaperPages.action",
 				type:"post",
 				datatype:"json",
 				data:{"displayRows":displayRows,"pageNume":pageNume,"examClassName":examClassName},
 				success:showPageInfo
 			});
 		});
 	});
 	//拼页面
 	function showPageInfo(data)
 	{		
 					var pageinfos = eval("(" + data + ")");	
 					var pageStr="";
 					pageStr=pageStr+'<tr><th bordercolor="#7EA6DC">编号</th><th bordercolor="#7EA6DC">密码</th><th bordercolor="#7EA6DC">考试日期</th><th bordercolor="#7EA6DC">班级</th><th bordercolor="#7EA6DC">试卷名称</th><th bordercolor="#7EA6DC">状态</th><th bordercolor="#7EA6DC">操作</th></tr>';
 					if(pageinfos.responseCode==0){
 						var pageSize=pageinfos.obj.pageSize;
 						var currentPage=pageinfos.obj.currentPage;
 						var totalsCount=pageinfos.obj.totalsCount;
 						var totalsPage=pageinfos.obj.totalsPage;
 						//alert("pagesize:"+pageSize+"\t"+"totalspage:"+totalsPage);				
 						$.each(pageinfos.obj.result, function(i,pageinfo){
 						//alert(pageinfo.examSubject);
 							pageStr+='<tr bgcolor="#EDECEB" onmouseover="this.bgColor=\'#93BBDF\';" onmouseout="this.bgColor=\'#EDECEB\';" align="left" id="tr_'+pageinfo.id+'">';
 							pageStr+='<td align="left">'+pageinfo.id+'</td>';
 							pageStr+='<td align="center">'+pageinfo.paperPwd+'</td>';
 							pageStr+='<td align="center">'+pageinfo.examDate+'</td>';
 							pageStr+='<td align="center">'+pageinfo.examineeClass+'</td>';
 							pageStr+='<td align="left"> <input type="text" style="color:#000099;cursor:pointer"onClick="toShowWritingPaperPage(\''+pageinfo.id+'\')" value="'+pageinfo.paperName+'" title="'+pageinfo.paperName+'" class="noneborder" size="23" readonly="readonly" /> </td>';
 							var statusStr="";
 							if(pageinfo.paperStatus==4){
 								statusStr='考试';
 							}else if(pageinfo.paperStatus==1){
 								statusStr='未考';
 							}else if(pageinfo.paperStatus==3){
 								statusStr='已评';
 							}else if(pageinfo.paperStatus==2){
 								statusStr='未评';
 							}
 							//alert("statusStr"+statusStr);
 							pageStr+='<td align="center" >'+statusStr+'</td>';
 							if(pageinfo.paperStatus==3){
 								pageStr+='<td align="center" id="tdOP_'+pageinfo.paperStatus+''+pageinfo.id+'"><a href="#" onclick="javascript:showwrittiongpapergrade(\''+pageinfo.id+'\',\''+pageinfo.paperName+'\',\''+pageinfo.examineeClass+'\',\''+pageinfo.examSubject+'\',\''+pageinfo.examDate+'\',\''+pageinfo.scorePercent+'\',\''+pageinfo.avgScore+'\',\''+pageinfo.maxScore+'\',\''+pageinfo.minScore+'\',\''+pageinfo.questionInfo+'\')" title="查看已评试卷的评卷结果">查看</a>   &nbsp;<a href="#" onClick="javascript:deleteThisPaper(\''+pageinfo.id+'\')"title="删除此卷">删除</a>&nbsp; <a href="#" id="reExam_${rowNumber.index}" onClick="reExamThisWp(\''+pageinfo.id+'\')" title="对本班重考此试卷，全班原有的考试成绩将会全部丢失">重考</a></td>';												
 							}else if(pageinfo.paperStatus==1){
 								pageStr+='<td align="center"><a href="#" onClick="updateThisPaperStatus(\''+pageinfo.id+'\',\''+4+'\')" title="开考此卷，也可以在“考试安排”菜单中开考">开考</a>&nbsp; <a href="#" onClick="javascript:deleteThisPaper(\''+pageinfo.id+'\')"title="删除此卷">删除</a> </td>';													
 							}else if(pageinfo.paperStatus==4){
 								pageStr+='<td align="center"><a href="#" onclick="javascript:toShowWritingPaperPage(\''+pageinfo.id+'\')" title="查看考试试卷">查看</a>&nbsp; <a href="#" onClick="javascript:deleteThisPaper(\''+pageinfo.id+'\')"title="删除此卷">删除</a></td>';	
 							}else if(pageinfo.paperStatus==2){
 								pageStr+='<td align="center"> <a  href="#" onClick="gradeThisPaper(\''+pageinfo.id+'\')" title="自动评卷">评卷</a>&nbsp; <a href="#" onClick="javascript:deleteThisPaper(\''+pageinfo.id+'\')"title="删除此卷">删除</a</td>';	
 							}
 						});	
 					}	
 					if(totalsCount!=0){
 						$("#strPrompt").html("查出"+totalsCount+"记录");
 					}else{
 						$("#strPrompt").html("没此记录");
 					}
 					$("#tabCurrentPage").html(currentPage);
 					$("#tabTotalPage").html(totalsPage);
 					$("#tbPapershowInfo").html(pageStr);	
 					
 	}	
 		//把id存到localstorage或cookie中
 		function toShowWritingPaperPage(wpid){
 			if (window.localStorage) {
 	   			 localStorage.setItem("wpid", wpid);
 			} else {
 	    		 Cookie.write("wpid", wpid);	
 			}
 			window.location.href='/Examination2.0/back/grade/showwritingpaper.html';
 		}
 		//显示班级考试情况（带图表显示的）
 		function showwrittiongpapergrade(wpid,paperName,examineeClass,examSubject,examDate,scorePercent,avgScore,maxScore,minScore,questionInfo){
 			if (window.localStorage) {
 				//alert(wpid);
	   			 localStorage.setItem("writtingPaper_wpid", wpid);
	   			 localStorage.setItem("writtingPaper_paperName", paperName);
	   			 localStorage.setItem("writtingPaper_examineeClass", examineeClass);
	   			 localStorage.setItem("writtingPaper_examSubject", examSubject);
	   			 localStorage.setItem("writtingPaper_examDate", examDate);
	   			 localStorage.setItem("writtingPaper_scorePercent", scorePercent);
	   			 localStorage.setItem("writtingPaper_avgScore", avgScore);
	   			 localStorage.setItem("writtingPaper_maxScore", maxScore);
	   			 localStorage.setItem("writtingPaper_minScore", minScore);
	   			 localStorage.setItem("writtingPaper_questionInfo", questionInfo);
			} else {
	    		 Cookie.write("writtingPaper_wpid", wpid);                   
	    		 Cookie.write("writtingPaper_paperName", paperName);         
	    		 Cookie.write("writtingPaper_examineeClass", examineeClass); 
	    		 Cookie.write("writtingPaper_examSubject", examSubject);     
	    		 Cookie.write("writtingPaper_examDate", examDate); 
	    		 Cookie.write("writtingPaper_scorePercent", scorePercent);  
	    		 Cookie.write("writtingPaper_avgScore", avgScore);     
	    		 Cookie.write("writtingPaper_maxScore", maxScore);
	    		 Cookie.write("writtingPaper_minScore", minScore);
	    		 Cookie.write("writtingPaper_questionInfo", questionInfo);
			}
 			window.location.href='/Examination2.0/back/grade/showwritingpapergrade.html';
 		}
 		function deleteThisPaper(wpid){
 			//alert("delId"+delId);
 			//delId=wpid;
 			$.ajax({
 				url:"/Examination2.0/writingPaper_delWritingPaperById.action",
 				type:"post",
 				datatype:"json",
 				data:{"wpid":wpid},
 				success:showDelInfo
 			});
 		};
 		//根据id来删除考卷
 		function showDelInfo(data){
 			var returninfos = eval("(" + data + ")");
 			if(returninfos.responseCode==0){
 				//总页数
 	 			var totalPage=parseInt($.trim($("#tabTotalPage").text()));
 	 			if(totalPage==""){
 	 				displayRows=0;
 	 			}
 	 			//每页显示几条
 	 			var displayRows=$.trim($("#displayRows").val());
 	 			if(displayRows==""){
 	 				displayRows=10;
 	 				$("#displayRows").val(displayRows);
 	 			}
 	 			//第几页
 	 			var pageNume=$.trim($("#pageNume").val());
 	 			if(pageNume==""){
 	 				pageNume=1;
 	 				$("#pageNume").val(pageNume);
 	 			}else if(pageNume==0){
 	 				pageNume=1;
 	 			}
 	 			//班级名称	
 	 			var examClassName=$.trim($("#examClassName").val());
 	 			if(examClassName=="--请选择--"){
 	 				examClassName="";
 	 			}
 	 			$.ajax({
 	 				url:"/Examination2.0/dataarraylist_showWritingPaperPages.action",
 	 				type:"post",
 	 				datatype:"json",
 	 				data:{"displayRows":displayRows,"pageNume":pageNume,"examClassName":examClassName},
 	 				success:showPageInfo
 	 			});
 			}else{
 				alert("删除失败！");
 			}
 		}
 		//更改评卷信息及其更改后的状态
 		function gradeThisPaper(wpid){
 			$.ajax({
 				url:"/Examination2.0/writingPaper_gradeThisWritingPaper.action",
 				type:"post",
 				datatype:"json",
 				data:{"wpid":wpid},
 				success:showUpdateExamWritingPaperInfo
 			});
 		}
 	
		//重考试卷
		function reExamThisWp(wpid){
			$.ajax({
				url:"/Examination2.0/writingPaper_reExamThisWritingPaper.action",
				type:"post",
				datatype:"json",
				data:{"wpid":wpid},
				success:showUpdateExamWritingPaperInfo
			});
		}
 		
 		
 		//根据id来更新试卷的状态
 		function updateThisPaperStatus(wpid,toPaperStatu){
 			$.ajax({
 				url:"/Examination2.0/writingPaper_updatePaperStatuById.action",
 				type:"post",
 				datatype:"json",
 				data:{"wpid":wpid,"paperStatus":toPaperStatu},
 				success:showUpdateExamWritingPaperInfo
 			});
 		}
 		function showUpdateExamWritingPaperInfo(data){
 			var returninfos = eval("(" + data + ")");
 			if(returninfos.responseCode==0){
 				//总页数
 	 			var totalPage=parseInt($.trim($("#tabTotalPage").text()));
 	 			if(totalPage==""){
 	 				displayRows=0;
 	 			}
 	 			//每页显示几条
 	 			var displayRows=$.trim($("#displayRows").val());
 	 			if(displayRows==""){
 	 				displayRows=10;
 	 				$("#displayRows").val(displayRows);
 	 			}
 	 			//第几页
 	 			var pageNume=$.trim($("#pageNume").val());
 	 			if(pageNume==""){
 	 				pageNume=1;
 	 				$("#pageNume").val(pageNume);
 	 			}else if(pageNume==0){
 	 				pageNume=1;
 	 			}
 	 			//班级名称	
 	 			var examClassName=$.trim($("#examClassName").val());
 	 			if(examClassName=="--请选择--"){
 	 				examClassName="";
 	 			}
 	 			$.ajax({
 	 				url:"/Examination2.0/dataarraylist_showWritingPaperPages.action",
 	 				type:"post",
 	 				datatype:"json",
 	 				data:{"displayRows":displayRows,"pageNume":pageNume,"examClassName":examClassName},
 	 				success:showPageInfo
 	 			});
 			}else{
 				alert("状态改变失败！");
 			}
 		}