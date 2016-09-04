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
    
    showClassName("S1")
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
    getclassName(did,true);
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
        getclassName(sid,false);
    });
}

function getclassName(id ,flag){
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
    $("#className").html("");
    if (obj == null) {
        $("#className").append("<option>--班级名称--</option>");
    }
    for ( var i = 0; i < obj.length; i++) {
        $("#className").append(
                "<option value='" + i + "'>" + obj[i].className
                        + "</option>");
    }
}



function showClassName(semester) {
	$.post("/Examination2.0/examineeclass_showClassList.action", {
		semester : semester
	}, function(json) {
		var obj = $.parseJSON(json);
		showClass(obj);
	});
}

// 添加考生
function addExamClass() {

	var className = $("#className").find("option:selected").text().trim();
	var examineeNames = $("#examineeNames").val().trim();
	// 判断字符或者字母
	if (/^[\n\u4e00-\u9fa5]+$/.test(examineeNames)) {

		var pwd1 = $("#password1").val().trim();
		var pwd2 = $("#password2").val().trim();
		var pwd = "";
		if (className == "" || examineeNames == "" || pwd1 == "") {
			$("#strSpan").text("输入的内容不完整");
			return;
		}
		if (pwd1 != pwd2) {
			$("#strSpan").text("两次密码不一致");
			return;
		} else {
			pwd = pwd1;
		}

		$.post("/Examination2.0/examineeclass_addExamClass.action", {
			className : className,
			examineeNames : examineeNames,
			pwd : pwd
		}, function(json) {
			var obj = $.parseJSON(json);
			if (obj.responseCode == 0) {
				$("#strSpan").text("添加考生成功");
				reset();
			} else if (obj.responseCode == 1) {
				$("#strSpan").text("添加考生失败，请联系管理员");
				$("#strSpan").val(obj.errorMessage);
			}
		});
	}else{
		$("#strSpan").text("考生们必须是汉字");
		return;
	}
}
// 改变radio的选项
function changeSemester(semester) {
	showClassName(semester);
}
function judgePwd() {
	var pwd1 = $("#password1").val();
	var pwd2 = $("#password2").val();
	if (pwd1 != pwd2) {
		alert("两次密码不一致");
	}
}
// 重置
function reset() {
	$("#semesterS1").checked = true;
	$("#examineeNames").val("");
	$("#password1").val("");
	$("#password2").val("");
}