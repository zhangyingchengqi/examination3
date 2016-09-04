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
});
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
    getlassName(sid,false);
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
    $("#examClass").html("<option>--请选择--</option>");
    if (obj == null) {
        $("#examClass").append("<option>--请选择--</option>");
    }
    for ( var i = 0; i < obj.length; i++) {
        $("#examClass").append(
                "<option value='" + obj[i].id + "'>" + obj[i].className
                        + "</option>");
    }
}










/**
 * 显示班级名称列表
 */
function listClass(semester){
	$.post("/Examination2.0/examineeclass_showClassList.action",{semester:semester},function(json){
		var obj=$.parseJSON(json);
		$("#examClass").html("");
		if(obj==null){
			$("#examClass").append("<option>--班级名称--</option>");
		}
		for( var i=0;i<obj.length;i++){
			$("#examClass").append("<option value='"+i+"'>"+obj[i].className+"&nbsp;&nbsp;&nbsp;&nbsp;</option>");
		}
		
		listExaminee();
	});
}
/**
 * 显示考生姓名列表
 */

function listExaminee(){
	var className=$("#examClass").find("option:selected").text().trim();
	$.post("/Examination2.0/examineeclass_showExamineeList.action",{className:className},function(json){
		var obj=$.parseJSON(json);
		$("#examineeName").html("");
		for( var i=0;i<obj.length;i++){
			$("#examineeName").append("<option value='"+i+"'>"+obj[i].name+"&nbsp;&nbsp;&nbsp;&nbsp;</option>");
		}
	});
}
/**
 *更新所有密码
 */
function updateAllPwd(){
	var className=$("#examClass").find("option:selected").text().trim();
	var pwd=$("#newPwd1").val();
	if(pwd==""){
		alert("密码不能为空");
	}
	
	$.post("/Examination2.0/examineeclass_updateAllExaminee.action",{className:className,pwd:pwd},function(json){
		var obj=$.parseJSON(json);
		if(obj.responseCode==0){
			$("#strSpan").text("修改全班密码成功");
		}else{
			$("#strSpan").text(obj.errorMessage);
		}
	});
	
}

/**
 * 更新单个密码
 */

function updatePwd(){
	var className=$("#examClass").find("option:selected").text().trim();
	var name=$("#examineeName").find("option:selected").text().trim();
	var pwd=$("#newPwd1").val();
	if(pwd==""){
		alert("密码不能为空");
	}
	
	$.post("/Examination2.0/examineeclass_updatepassword.action",{className:className,name:name,pwd:pwd},function(json){
		var obj=$.parseJSON(json);
		if(obj.responseCode==0){
			$("#strSpan").text("修改单个密码成功");
		}else{
			$("#strSpan").text(obj.errorMessage);
		}
	});
	
}
