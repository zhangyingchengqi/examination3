$(function() {
	var semester = $("#semesterS1").attr('value');
	showClassName(semester);

});

function showClassName(semester) {
	$.post("/Examination2.0/examineeclass_showClassList.action", {
		semester : semester
	}, function(json) {
		var obj = $.parseJSON(json);
		$("#className").html("");
		if (obj == null) {
			$("#className").append("<option>--班级名称--</option>");
		}
		for ( var i = 0; i < obj.length; i++) {
			$("#className").append(
					"<option value='" + i + "'>" + obj[i].className
							+ "</option>");
		}
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