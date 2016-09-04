	$(function(){
					//从浏览器中取id（从localstorage或cookie中取）
					var wpid= window.localStorage? localStorage.getItem("wpid"): Cookie.read("wpid");
					var waid= window.localStorage? localStorage.getItem("waid"): Cookie.read("waid");
					//alert("wpid:"+wpid);
					//alert("waid:"+waid);
					//根据id查找试卷信息
					$.ajax({
						url:"/Examination2.0/dataarraylist_showAnswerPagesDetail.action",
						type:"post",
						datatype:"json",
						data:{"wpid":wpid,"waid":waid},
						success:showPageInfoDetail
						});
					
				});
	var rightAnswers="";
	//进入显示试卷页面的初始化数据
	function showPageInfoDetail(data){
		var pageInfoDetails = eval("(" + data + ")");
		if(pageInfoDetails.responseCode==0){		
			var paperId=pageInfoDetails.obj.writingPaper.id;
			var paperName=pageInfoDetails.obj.writingPaper.paperName;
			var examineeClass=pageInfoDetails.obj.writingPaper.examineeClass;
			var stuName=pageInfoDetails.obj.examineeName;
			var countOfQuestion=pageInfoDetails.obj.writingPaper.countOfQuestion;
			var timesConsume=pageInfoDetails.obj.writingPaper.timesConsume;
			rightAnswers=pageInfoDetails.obj.answer;
			$("#paperId").text(paperId);
			$("#paperName").text(paperName);
			$("#examineeClass").text(examineeClass);
			$("#stuName").text(stuName);
			$("#countOfQuestion").text(countOfQuestion);
			$("#timesConsume").text(timesConsume);
			questionId=pageInfoDetails.obj.writingPaper.questionId;
			//alert(questionId.charAt(questionId.length-1));
			if( questionId.charAt(questionId.length-1)==","){
			  questionId=questionId.substring(0,questionId.length-1)
			}else{
				questionId=questionId;
			}
			questionId="["+questionId+"]";
			//alert("questionId:"+questionId);
			 //根据ids查询考试题
			$.ajax({
			url:"/Examination2.0/dataarraylist_findQuestionByids.action",
			type:"post",
			datatype:"json",
			data:{"questionId":questionId},
			success:showAllQuestion
			});	
		}
		//显示拼接好的试题信息
		function showAllQuestion(data){
			var questionCount=0;
			var questionStr="";
			var allQuestion=eval("(" + data + ")");
			$.each(allQuestion.obj, function(i,question){
				//alert("question:"+question.question)
				questionStr+='<input type="hidden" name="answer_'+(i+1)+'" id="answer_'+(i+1)+'" value="'+question.answer+'">';
				questionStr+='<table width="100%" border="0" cellspacing="0" cellpadding="0" id="questionTable'+(i+1)+'"> <tr><td width="8%"><p align="center">&nbsp;</p></td><td height="27" colspan="2">&nbsp;</td></tr>';
				questionStr+='<tr><td height="4%" valign="top">&nbsp;<b>'+(i+1)+'.</b></td><td height="10%" colspan="2" rowspan="2"><a name="Food_'+(i+1)+'"> </a><span class="style14"> </span><textarea name="text_'+(i+1)+'" cols="80%" rows="4"readonly="readonly" class="txaNoneBorder">'+question.question+'</textarea>&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;</td></tr>';
				questionStr+='<tr><td height="100%">&nbsp;</td></tr>';
				if(question.image!=null){
					questionStr+='<tr><td></td><td colspan="2">&nbsp;<span class="fontColor"> 图片说明:</span><br><div align="left"><img src="" alt=""><br /></div></td></tr>';
				}
				questionStr+='<tr><td rowspan="4">&nbsp;</td><td width="3%" valign="top">A.</td><td width="89%"><textarea name="textA_'+(i+1)+'" class="txaNoneBorder" cols=""readonly="readonly">'+question.optionA+'</textarea></td></tr>';
				questionStr+='<tr><td width="3%" valign="top">B.</td><td ><textarea name="textB_'+(i+1)+'" class="txaNoneBorder" cols=""readonly="readonly">'+question.optionB+'</textarea></td></tr>';	
				questionStr+='<tr><td width="3%" valign="top">C.</td><td ><textarea name="textC_'+(i+1)+'" class="txaNoneBorder" cols=""readonly="readonly">'+question.optionC+'</textarea></td></tr>';
				questionStr+='<tr><td width="3%" valign="top">D.</td><td ><textarea name="textD_'+(i+1)+'" class="txaNoneBorder" cols=""readonly="readonly">'+question.optionD+'</textarea></td></tr>';
				questionStr+='<tr><td height="39"></td><td height="39" colspan="2"><span class="fontColor">&nbsp;&nbsp; 正确答案：</span><span id="rightAnswer_'+(i+1)+'">'+question.answer+'</span><br><span>&nbsp;&nbsp;&nbsp;你的答案：</span><span id="myAnswer_'+(i+1)+'"></span> &nbsp;&nbsp;&nbsp;<a href="#top"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; TOP</a></td></tr>';
				questionStr+='</table>';
				questionCount++;	
			});	
			$("#questionInfoTab").html(questionStr);
			//给试卷加考生的做题答案
			//alert(rightAnswers);
			var rightAnswersArr=rightAnswers.split(';');
			for(var j=1;j<=questionCount;j++){
				var rightAnswer=$.trim($("#rightAnswer_"+j).text());
				if(rightAnswer==rightAnswersArr[j-1].split(',')[1]){
					$("#myAnswer_"+j).text(rightAnswer);
				}else{
					$("#myAnswer_"+j).text(rightAnswersArr[j-1].split(',')[1]);
					$("#myAnswer_"+j).css("color","red");
				}
			}
			//把试卷信息表中的题的数量更新
			$("#questionAmount").text(questionCount);
			//显示侧栏第几题	
			showtitleInfoStr(questionCount,rightAnswers);	
			
		}
		//显示侧栏第几题	
		function showtitleInfoStr(countOfQuestion,rightAnswers){
				var titleInfoStr="";
				var rightAnswersArr=rightAnswers.split(';');
				for(var i=1;i<=countOfQuestion;i++){
					titleInfoStr+='&nbsp;&nbsp;&nbsp;&nbsp;<a href="#Food_'+i+'"> 第 '+i+' 题 </a><span id="span'+i+'"> '+rightAnswersArr[i-1].split(',')[1]+'</span><br>';			
				}
				$("#divQuestionCounts").html(titleInfoStr);	
		}
		
}