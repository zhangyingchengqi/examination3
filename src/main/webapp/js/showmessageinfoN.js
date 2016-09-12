$(function(){
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
    
        selectedvalue="S1";
        //根据学期显示课程名称（默认的学期尾S1）
        $.ajax({
            url:"/Examination2.0/subject_findSubjectBySemester.action",
            type:"post",
            datatype:"json",
            data:{"semester":selectedvalue},
            success:showSubjectName
            }); 
        //根据学期显示班级 （默认的学期尾S1）
        /*$.ajax({
            url:"/Examination2.0/writingPaper_getExamineeClassNameAndClassId.action",
            type:"post",
            datatype:"json",
            data:{"semester":selectedvalue},
            success:showAllExamineeClass
            }); 
        $("#examineeName").html("<option>--请选择--</option>");*/
    });	

/* xh */
var did = "";
var eid = "";
var semester = "S1";
var snumber = "0";
var classN = "";
//改变方向
function changeDirection() {
    did = $("#directionName").val();
    var str = did.split("-");
    did = str[0];
    classN = str[1];
    getedition(did);
    getClassByDid()
}
//改变版本
function changeEdition() {
    eid = $("#editionName").val();
    var str = eid.split("-");
    eid = str[0];
    snumber = str[1];
    getSemester(snumber);
    getClassByEid()
    createSubject(eid, "S1");
}
//改变学期
function changeSemester() {
    semester = $("#semesterName").val();
    createSubject(eid, semester);
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
                    "<option value='" + obj[i].id + "-" + obj[i].semesternum+ "'>"
                            + obj[i].editionName + "</option>");
        }
    });
    
}
//得到学期信息
function getSemester(snumber){
    $("#semesterName").html("");
    for (var i = 1; i <= snumber; i++) {
        $("#semesterName").append(
                "<option value='S" + i + "'>S" + i + "</option>");
    }
}

//通过Did获取班级
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
//通过Eid获取班级
function getClassByEid() {
  eid = $("#editionName").val();
  var str = eid.split("-");
  eid = str[0];
  snumber = str[1];
  $.post("/Examination2.0/examineeclass_getClassByEdition.action", {
      eid : eid,
  }, function(json) {
      var obj = eval(json);// 转换后的JSON对象
      showClass(obj);
  });
}

function showClass(obj) {
  $("#examclassName").html("<option>--请选择--</option>");
  if (obj == null) {
      $("#examclassName").append("<option>--请选择--</option>");
  }
  for (var i = 0; i < obj.length; i++) {
      $("#examclassName").append(
              "<option value='" + obj[i].className + "'>" + obj[i].className
                      + "</option>");
  }
}

function createSubject(eid, semester){
    $.post("/Examination2.0/initData_subject.action", {editionId:eid,semester:semester},function(json){
        var obj = $.parseJSON(json).obj;// 转换后的JSON对象
        $("#subjectOption").html("");
        $("#subjectOption").append('<option value="0">');
         for(var i=0; i<obj.length;i++){
            $("#subjectOption").append(
            "<option value='"+obj[i].id+"'>"+ obj[i].subjectName+ "</option>"); 
         }  
    });
}



    var selectedvalue="";
	function createSelectOption(s){
 		selectedvalue=s;
 		//根据学期显示课程名称
 		/*$.ajax({
			url:"/Examination2.0/subject_findSubjectBySemester.action",
			type:"post",
			datatype:"json",
			data:{"semester":selectedvalue},
			success:showSubjectName
			});
 		//根据学期显示班级 
 		$.ajax({
			url:"/Examination2.0/writingPaper_getExamineeClassNameAndClassId.action",
			type:"post",
			datatype:"json",
			data:{"semester":selectedvalue},
			success:showAllExamineeClass
			});
 		$("#examineeName").html("<option>--请选择--</option>");*/
	}

 	
	//根据学期显示课程名称函数
 	function showSubjectName(data){
 		var subjects = eval("(" + data + ")");
 		var optionstr='<option selected="selected" value="-1">--请选择--</option>';
		if(subjects.responseCode==0&&subjects.obj.length>0){
			$.each(subjects.obj, function(i,subject){
				optionstr+='<option value="'+subject[0]+'">'+subject[1]+'</option>';	  														
			});	
		}																
		$("#subjectOption").html(optionstr);
	}
 	

 	//把拼好的班级加到班级下拉框中
	/*function showAllExamineeClass(data){
		var examieeClassNames = eval("(" + data + ")");
		if(examieeClassNames.responseCode==0){			
			var examineeClassStr="<option>--请选择--</option>";
			for(var i=0;i<examieeClassNames.obj.length;i++){
 					examineeClassStr+="<option value='"+examieeClassNames.obj[i][0]+"'>"+examieeClassNames.obj[i][0]+"</option>"
			}
			$("#examclassName").html(examineeClassStr);	
		
		}
	}*/
	//根据班级名称来查学生的姓名
	function showStuNameByClassName(className){
		var className=$.trim(className);
		if(className!=""&&className!="--请选择--"){
			$.ajax({
				url:"/Examination2.0/dataarraylist_showExamineeNames1.action",
				type:"post",
				datatype:"json",
				data:{"examClassName":className},
				success:showExamineeNameOption
			});
		}else{
			var optionstr="<option>--请选择--</option>";
			$("#examineeName").html(optionstr);
		}
	}
	function showExamineeNameOption(data){
		var classNameInfos = eval("(" + data + ")");
		var optionstr="<option>--请选择--</option>";
		if(classNameInfos.responseCode==0){
			for(var i=0;i<classNameInfos.obj.length;i++){
				optionstr+="<option value='"+classNameInfos.obj[i]+"' name='stuName'>"+classNameInfos.obj[i]+"</option>";	  
			}
		}
		$("#examineeName").html(optionstr);
	}
	//根据学生名称，课程id,班级班级名称来显示显示测评留言信息
	function showMessagesInfo(){
		var subjectid=$("#subjectOption").val();
		var className=$.trim($("#examclassName").val());
		var stuName=$.trim($("#examineeName").val());
		if(stuName=="--请选择--"){
			stuName="";
		}
		if(className=="--请选择--"){
			className="";
		}
		$.ajax({
			url:"/Examination2.0/pointAnswer_findPointAnswerInfo.action",
			type:"post",
			datatype:"json",
			data:{"examClassName":className,"subjectid":subjectid,"stuName":stuName},
			success:showPointAnswerInfo
		});	
		
	}
	function showPointAnswerInfo(data){
		var messageinfos = eval("(" + data + ")");
		var str="<div class=\"showMessageInfoDiv\"><table width='680' border='1' align='center' cellpadding='1' bordercolor='#FFFFFF' cellspacing='0'>";
		if(messageinfos.responseCode==0){
			if(messageinfos.obj!=null){
				$.each(messageinfos.obj,function(index,item){ 
					str+="<tr><td><div class=\"messageInfoStyle\"><h4>"+item.name;
					str+="&nbsp;&nbsp;在&nbsp;&nbsp;"+item.pointPaper.pname+"&nbsp;&nbsp;课程中说：</h4><span>"+item.idea;
					str+="</span><br /></div></td></tr>";
				});
			}
		}	
		str+="</table></div>";
		$("#findMessagesInfo").html(str);
	}