var val;
$(function() {
	var semester = $("#semesterS1").attr('value');
	$("#semesterS1").prop('checked', true);
	showClassName(semester);
	val="S1";
	 $("#semesterS1").click(function(){
		 	val=$('input:radio[name="semester"]:checked').val();
		 });
	  
	  $("#semesterS2").click(function(){
		 	val=$('input:radio[name="semester"]:checked').val();
		 });
	  
	  $("#semesterS3").click(function(){
		 	val=$('input:radio[name="semester"]:checked').val();
		 });
})

function checkSemesters(semester) {
	showClassName(semester);
}
// 显示讲解班级
function showClassName(semester) {
	$.post("/Examination2.0/examineeclass_showClassList.action", {
		semester : semester
	}, function(json) {
		var obj = $.parseJSON(json);
		$("#examineeClass").html("<option>--请选择--</option>");
		$("#examineeName").html("<option>--请选择--</option>");
		if (obj == null) {
			$("#examineeClass").append("<option>--请选择--</option>");
		}
		for ( var i = 0; i < obj.length; i++) {
			$("#examineeClass").append(
					"<option value='" + obj[i].id + "'>" + obj[i].className
							+ "</option>");
		}
	});
}
// 改变讲解班级显示学员姓名
function changeExamineeClasses(value) {
	var className = $("#examineeClass").find("option:selected").text().trim();
	if(className==null  ||  className=="--请选择--"){
		$("#historyInfobtn").attr("disabled",true);
		return;
	}
	$("#historyInfobtn").removeAttr("disabled");
	$.post("/Examination2.0/examineeclass_showExamineeList.action", {
		className : className
	}, function(json) {
		var obj = $.parseJSON(json);
		$("#examineeName").html("");
		for ( var i = 0; i < obj.length; i++) {
			$("#examineeName").append(
					"<option value='" + i + "'>" + obj[i].name
							+ "&nbsp;&nbsp;&nbsp;&nbsp;</option>");
		}
	});
	showAdailyTalk();
}
// 添加
function addADailyTalkInfo() {
	var className = $("#examineeClass").find("option:selected").text().trim(); // 班级编号
	var name = $("#examineeName").find("option:selected").text().trim(); // 学生姓名
	var knowledge = $("#knowledgeInfo").val(); // 新技术内容

	if (className == "--请选择--") {
		alert("请选择要添加的班级");
		return;
	}

	if (name == "--请选择--") {
		alert("请选择讲解的学生");
		return;
	}

	if (knowledgeInfo == "" || knowledge == null || knowledge == "null") {
		alert("请输入要添加的新技术内容");
		return;
	}
	$("#historyInfobtn").removeAttr("disabled");

	$.getJSON("/Examination2.0/checking_dailyTalk.action", {
		className : className,
		name : name,
		knowledge : knowledge
	}, function(data) {
		if (data.responseCode == 0) {
			showAdailyTalk();
		}

		
	});
}

function resetInfo() {

}

function showAdailyTalk() {
	var str="";
	$.ajaxSettings.async = false;
	var className = $("#examineeClass").find("option:selected").text().trim(); // 班级编号
	str = "<table id='mytable' width='680' border='1' cellpadding='1'  bgcolor='#EDECEB' bordercolor='#FFFFFF' cellspacing='0'>";
	var status = 1;
	$.getJSON("/Examination2.0/checking_showDailyTalk.action",{status:status,className:className},function(data) {
		if(data.responseCode==0){
			
			for(var i=0;i<data.obj.length;i++){
				str+="<tr height='23' id='"+(i+1)+"' bgcolor='#EDECEB' onmouseover=this.bgColor='#93BBDF'; onmouseout=this.bgColor='#EDECEB';>";
				str+="<td width=10% align='center'>"+(i+1)+"</td>";
				str+="<td width=15% align='center'>"+data.obj[i].name+"</td>";
				str+="<td width=60%>"+decodeURIComponent(data.obj[i].knowledge)+"</td>";
				str+="<td align='center'><a href=javascript:speakKnowledgeInfo('"+data.obj[i].name+"',"+data.obj[i].id+")>&nbsp;[开讲]</a>";
				str+="<a href=javascript:delKnowledgeInfo("+(i+1)+","+data.obj[i].id+")>&nbsp;[删除]</a>";
				str+="</td></tr>";
			}
			
		}
	});
	str += "</table>";

	$("#showKnowledgeInfoDiv").html(str);
	$("#knowledgeInfo").val("");
}

//删除
function delKnowledgeInfo(flag,id){
	if(confirm("您确定要删除此安排吗?")){
		$.getJSON("/Examination2.0/checking_delSpeak.action",{id:id},function(data) {
			if(data.responseCode==0){
				alert("操作成功");
				showAdailyTalk();
			}
		});
	}
	
}

//开讲
function speakKnowledgeInfo(name,id){
	location.href="commitAdailytalk.html?name="+name+"&id="+id;
}
//历史记录
function showHistoryInfoInfo(){
	var cid=$("#examineeClass").val();  //班级编号
	var className = $("#examineeClass").find("option:selected").text().trim(); // 班级编号
	if(cid==0||cid=="0"){
		alert("请选择查询的班级");
		return;
	}
	location.href='showhistoryadailytalk.html?id='+cid+'&semester='+val+'&className='+className;

}


