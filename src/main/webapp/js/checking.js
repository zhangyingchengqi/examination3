var semester;
$(function() {
    $.post("/Examination2.0/direction_direction.action", function(json) {
        var strJSON = json;
        var obj = eval(strJSON);// 转换后的JSON对象
        var result = '';
        for (var i = 0; i < obj.length; i++) {
            result += '<option value ="' + obj[i].did + "-" + obj[i].classname
                    + '">' + obj[i].dname + '</option>';
        }
        $("#directionName").append(result);
    });
    
    
	semester = "S1";
	//设置时间选中   上午、下午、晚上
	var myDate = new Date();
	var hours=myDate.getHours();
	if(hours<12&&hours>7){
		$("#dateTime").find("option[value='上午']").attr("selected",true);
	}else if(hours>=12&&hours<18){
		$("#dateTime").find("option[value='下午']").attr("selected",true);
	}else{
		$("#dateTime").find("option[value='晚上']").attr("selected",true);
	}
	
	showClassName(semester);
	
	userName=localStorage.getItem("systemUser_userName");
	$("#systemUser_userName").html("考勤教员："+userName);
	$("#checkDate").val(changeTime(new Date()));
})

/* xh */
var did = "";
var sid = "";
var semester = "0";
var snumber = "0";
var classN = "";
//改变方向
function changeDirection() {
    did = $("#directionName").val();
    var str = did.split("-");
    did = str[0];
    classN = str[1];
    getedition(did);
}
//改变版本
function changeSubject() {
    sid = $("#editionName").val();
    var str = sid.split("-");
    sid = str[0];
    snumber = str[1];
    getchapter(sid, semester);
}

//获取版本
function getedition(did) {
    $.post("/Examination2.0/course_edition.action", {
        did : did
    }, function(json) {
        var strJSON = json;
        var obj = eval(strJSON);// 转换后的JSON对象
        $("#editionName").html("");
        $("#editionName").append('<option value="0">');
        for (var i = 0; i < obj.length; i++) {
            $("#editionName").append(
                    "<option value='" + obj[i].id + "-" + obj[i].semesternum
                            + "-" + obj[i].semesternum + "'>"
                            + obj[i].editionName + "</option>");
        }
    });
    getlassName(did,true);
}

//获取章节
function getchapter(sid, semester, flag) {
    $.post("/Examination2.0/chapter_subjectName.action", {
        sid : sid,
        semester : semester
    }, function(json) {
        var obj = eval(json);// 转换后的JSON对象

        $("#subjectName").html("");
        $("#subjectName").append('<option value="0">');
            $("#semester").html("");
            for (var i = 1; i <= snumber; i++) {
                $("#semester").append( "<option value='S" + i + "'>S" + i + "</option>");
            }
        getlassName(sid,false);
    });
}

//获取班级
function getClassByDid() {
    did = $("#directionName").val();
    var str = did.split("-");
    did = str[0];
    classN = str[1];
    getedition(did);
    $.post("/Examination2.0/examineeclass_getCNumByDid.action", {
        did : did
    }, function(json) {
        var obj = eval(json);// 转换后的JSON对象
        showClass(obj);
    });
}

function getlassName(id ,flag){
    if(flag){
        $.post("/Examination2.0/examineeclass_getCNumByDid.action", {
            did : id
        }, function(json) {
            var obj = eval(json);// 转换后的JSON对象
            showClass(obj);
        });
    }else{
        $.post("/Examination2.0/examineeclass_getClassByEdition.action", {
            eid : id,
        }, function(json) {
            var obj = eval(json);// 转换后的JSON对象
            showClass(obj);
        });
    }
}
function showClass(obj){
    $("#examineeClass").html("<option>--请选择--</option>");
    if (obj == null) {
        $("#examineeClass").append("<option>--请选择--</option>");
    }
    for ( var i = 0; i < obj.length; i++) {
        $("#examineeClass").append(
                "<option value='" + obj[i].id + "'>" + obj[i].className
                        + "</option>");
    }
}



var userName="";
var index=0; //考勤人数

function checkSemesters(semester){
	showClassName(semester);
}

// 显示讲解班级
function showClassName(semester) {
	$.post("/Examination2.0/examineeclass_showClassList.action", {
		semester : semester
	}, function(json) {
		var obj = $.parseJSON(json);
		showClass(obj);
	});
}

function showcheckingresultinfos(){
	
}

function showAllCheckingInfo(){
	var cid = $("#examineeClass").find("option:selected").val(); //班级编号
	var startDate=	$("#startDate").val();
	var endDate  =  $("#endDate").val();
	var dateTime =  $("#dateTime").find("option:selected").text().trim();
//	alert(cid+"\t"+startDate+"\t"+endDate+"\t"+dateTime+"\t"+userName);
	if(cid=="--请选择--"  ||  cid=="0"){
		alert("请选择班级");
		return;  
	}
	
	if(dateTime=="--请选择--"){
		dateTime=null;
	}
//	alert(startDate);
//	alert(endDate);
	$.post("/Examination2.0/checking_showAllCheckingInfo.action",
			
			{cid:cid,startDate:startDate,endDate:endDate,dateTime:dateTime},function(data){
				var obj=$.parseJSON(data);
				if(obj!=null  &&  obj.length>0){
					str = "<table id='mytable' width='800px' border='1' cellpadding='1'  bgcolor='#EDECEB' bordercolor='#FFFFFF' cellspacing='0'>";
					
					for(var i=0;i<obj.length;i++){
						if(obj[i].checkRemark==""||obj[i].checkRemark==null){
							obj[i].checkRemark="无";
						}
						str+="<tr height='23' id='"+(i+1)+"' bgcolor='#EDECEB' onmouseover=this.bgColor='#93BBDF'; onmouseout=this.bgColor='#EDECEB';>";
						str+="<td width=15% align='center'>"+(i+1)+"</td>";
						str+="<td width=15% align='center'>"+obj[i].examineeClass.className+"</td>";
						str+="<td width=15%>"+obj[i].systemUser.userName+"</td>";
						str+="<td align='center' width='25%'>"+changeTime(obj[i].checkDate)+"</td>";
						str+="<td align='center' width='15%'>"+obj[i].checkTime+"</td>";			"semester="+semester+"&className="+obj[i].examineeClass.className+"&userName="+obj[i].systemUser.userName
						str+="<td align='center' width='15%'><font style='color:blue;font-size:12px'><a href=\"changecheckinginfo.html?semester="
							+obj[i].semester+"&className="+obj[i].examineeClass.className+"&userName="
							+obj[i].systemUser.userName
							+"&checkDate="+changeTime(obj[i].checkDate).trim()+"&checkTime="+obj[i].checkTime+"&checkRemark="+obj[i].checkRemark+"&checkId="+obj[i].checkId+"\">&nbsp;查看</a></font></td>"
						str+="</td></tr>";
					}
//					utcToDate(new Date(obj[i].checkDate)).substring(9)
					str += "</table>";
					
					$("#findCheckingResultInfo").html(str);
				}else{
					alert("该班级在该时段类没有考勤记录");
					$("#findCheckingResultInfo").html("");
				}
			});
	
}

function changeClazz(){
	$.ajaxSettings.async = false;
	var className = $("#examineeClass").find("option:selected").text(); //班级编号
	var mydate=$("#checkDate").val();   //考勤日期
	var mytime=$("#dateTime").val();  //考勤时段
//	$("#btnView").attr("disabled",true);
	$("#btnView").removeAttr("disabled");
	var str;
	if(className==""||className==null){
		return;
	}
	var statusInfo=["已到","迟到","病假","请假","旷课","早退"];
	$.getJSON("/Examination2.0/examineeclass_showExamineeList.action",{className:className},function(json){
		if(json!=null  ||  json!=""){
			str="<table width='800px' border='1' cellpadding='1' bordercolor='#FFFFFF' cellspacing='0'>";
			for(var i=0;i<json.length;i++){
				index=i+1;
				str+="<tr height='25px' id=\""+index+"\" bgcolor=\"#EDECEB\" onmouseover=\"this.bgColor='#93BBDF';\" onmouseout=\"this.bgColor='#EDECEB';\">";
				str+="<td width=\"8%\" align=\"center\">"+index+"</td>";
				str+="<td width=\"12%\" align=\"center\">"+json[i].name+"</td><td width=\"50%\" align=\"center\">";
				str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+json[i].name+",1\" checked=\"checked\"/>已到&nbsp;&nbsp;&nbsp;";
				str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+json[i].name+",2\"/>迟到&nbsp;&nbsp;&nbsp;";
				str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+json[i].name+",3\"/>病假&nbsp;&nbsp;&nbsp;";
				str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+json[i].name+",4\"/>请假&nbsp;&nbsp;&nbsp;";
				str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+json[i].name+",5\"/>旷课&nbsp;&nbsp;&nbsp;";
				str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+json[i].name+",6\"/>早退&nbsp;&nbsp;&nbsp;";
				str+="</td><td><input type=\"text\" size=\"30\" id=\"remarkInfo"+i+"\"/></td></tr>";
			}
			str+="</table>";
			$("#checkClassInfo").html(str);
		}
		
	});
}
//提交
function CheckingResultInfo(){
	var cid=$("#examineeClass").val();  //考勤班级
	//alert(cid);
	var date=$("#checkDate").val();   //考勤日期
	var time=$("#dateTime").val();  //考勤时段
	var remark=$("#remarkInfo").val();  //备注
	var semesterNa=$("#semester").find("option:selected").val();  //学期
	
	var resultInfo="";  //考勤结果
	var count=0; //考勤总数
	var sturemark="";   //学生备注信息
	alert(semesterNa);
	
	if(cid<=0){
		alert("请选择考勤班级");
		return false;
	}

	if(date==""||date==null){
		alert("请选择考勤日期");
		return false;
	}
	if(semesterNa==""||semesterNa==null){
        alert("请选择考勤学期");
        return false;
    }
    
	$("#checkClassInfo input[type=radio]" ).each(function(){
		if(this.checked){
			//count+=1;
			sturemark="#remarkInfo"+count;
			sturemark=$(sturemark).val();
			if(sturemark==null||sturemark==""){
				resultInfo+=$(this).val()+"- |";
			}else{
				resultInfo+=$(this).val()+"-"+sturemark+"|";
			}
			count+=1;
		}
	});
	if(index>count){
		alert("您还有未考完的学生记录，请全部考完后再提交");
		return;
	}
	$.post("/Examination2.0/checking_subChecking.action",
			{className:cid,checkDate:date,checkTime:time,checkRemark:remark,semester:semesterNa,checkResult:resultInfo,userName:userName},function(json){
				var obj=$.parseJSON(json);
		if(obj.responseCode==1){
			alert(obj.errorMessage);
		}else{
			alert("提交成功");
			$("#remarkInfo").val("");
		}
	});
}


function changeClasses(){
	var cid = $("#examineeClass").find("option:selected").val().trim(); //班级编号
	var mydate=$("#checkDate").val();   //考勤日期
	var mytime=$("#dateTime").val();  //考勤时段
	var str;
	if(cid==""||cid==null){
		return;
	}
	var statusInfo=["已到","迟到","病假","请假","旷课","早退"];
	$.getJSON("/Examination2.0/checking_showcheckingClass.action",{cid:cid,mydate:mydate,mytime:mytime},function(data){
		if(data.dataInfo==""||data.dataInfo==null){
			document.myform.btnView.disabled=true;
			str="<span class=\"fontColor\" style=\"color:red;font-weight:bold;font-size:20px;\">对不起，本班您已经考勤，如需修改或查询请选择学员管理->考勤记录</span>";
		}else{
			document.myform.btnView.disabled=false;
			str="<table width='800px' border='1' cellpadding='1' bordercolor='#FFFFFF' cellspacing='0'>";
			if(data.dataInfo[0].name=="SBSBSB"){
				$("#remarkInfo").val("");
				document.myform.btnView.value="提交";
				$.each(data.dataInfo,function(index,item){
					if(index!=0&&index!="0"){
						str+="<tr height='25px' id=\""+index+"\" bgcolor=\"#EDECEB\" onmouseover=\"this.bgColor='#93BBDF';\" onmouseout=\"this.bgColor='#EDECEB';\">";
						str+="<td width=\"8%\" align=\"center\">"+index+"</td>";
						str+="<td width=\"12%\" align=\"center\">"+item.name+"</td><td width=\"50%\" align=\"center\">";
						str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+item.name+",1\" checked=\"checked\"/>已到&nbsp;&nbsp;&nbsp;";
						str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+item.name+",2\"/>迟到&nbsp;&nbsp;&nbsp;";
						str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+item.name+",3\"/>病假&nbsp;&nbsp;&nbsp;";
						str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+item.name+",4\"/>请假&nbsp;&nbsp;&nbsp;";
						str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+item.name+",5\"/>旷课&nbsp;&nbsp;&nbsp;";
						str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+item.name+",6\"/>早退&nbsp;&nbsp;&nbsp;";
						str+="</td><td><input type=\"text\" size=\"35\" id=\"remarkInfo"+index+"\"/></td></tr>";
					}
				});
			}else{
				document.myform.btnView.value="提交修改";
				$("#remarkInfo").val(data.dataInfo[0].name);
				$.each(data.dataInfo,function(index,item){
					if(index!=0&&index!="0"){
						str+="<tr height='25px' id=\""+index+"\" bgcolor=\"#EDECEB\" onmouseover=\"this.bgColor='#93BBDF';\" onmouseout=\"this.bgColor='#EDECEB';\">";
						str+="<td width=\"8%\" align=\"center\">"+index+"</td>";
						str+="<td width=\"12%\" align=\"center\">"+item.sname+"</td><td width=\"50%\" align=\"center\">";
						for(var i=1;i<=6;i+=1){
							if(item.subid==i){
								str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+item.sname+","+i+"\" checked=\"checked\"/>"+statusInfo[i-1]+"&nbsp;&nbsp;&nbsp;";
							}else{
								str+="<input type=\"radio\" name=\"status"+index+"\" id=\"status"+index+"\" value=\""+item.sname+","+i+"\"/>"+statusInfo[i-1]+"&nbsp;&nbsp;&nbsp;";
							}
						}
						str+="</td><td><input type=\"text\" size=\"35\" id=\"remarkInfo"+index+"\" value=\""+item.pcontent+"\"/></td></tr>";
					}
				});
			}
			str+="</table>";
		}
		$("#checkClassInfo").html(str);
	});
}

function changeTime(time)
{ 
	if(time==null){
		var now = new Date();
	}else{
		var now =new Date(time);
	}
    
   
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
   
//    var hh = now.getHours();            //时
//    var mm = now.getMinutes();          //分
   
    var clock = year + "-";
   
    if(month < 10)
        clock += "0";
   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
       
    clock += day + " ";
   
//    if(hh < 10)
//        clock += "0";
       
//    clock += hh + ":";
//    if (mm < 10) clock += '0'; 
//    clock += mm; 
    return(clock); 
} 