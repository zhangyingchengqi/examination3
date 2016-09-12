$(function() {
	var semester ="S1";
	showClassName(semester);

})
var userName = "";
var index = 0; // 考勤人数

function checkSemesters(semester) {
	showClassName(semester);
}
var id = "";

// 改变讲解班级显示学员姓名
function changeExamineeClasses(value) {
	var className = $("#examineeClass").find("option:selected").text().trim();
	if (className == "--请选择--") {
		$("#examineeName").html("<option>--请选择--</option>");
		return;
	}
	$("#examineeName").html("<option>--请选择--</option>");
	$.post("/Examination2.0/examineeclass_showExamineeList.action", {
		className : className
	}, function(json) {
		var obj = $.parseJSON(json);
		for ( var i = 0; i < obj.length; i++) {
			$("#examineeName").append(
					"<option value='" + obj[i].id + "'>" + obj[i].name
							+ "&nbsp;&nbsp;&nbsp;&nbsp;</option>");
		}
	});
}

// 显示讲解班级
function showClassName(semester) {
	$.post("/Examination2.0/examineeclass_showClassList.action", {
		semester : semester
	}, function(json) {
		var obj = $.parseJSON(json);
		$("#examineeClass").html("<option>--请选择--</option>");
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


//重置
function reset(){
	$("#startDate").val("");
	$("#endDate").val("");
	$("#dateTime").val("0");
	$("#examineeName").val("--请选择--");
	$("#examineeClass").val("0");
}

// 组合查询考勤记录
function findCheckingInfoByAll() {
	var semester = $("input[name=semesterName]:checked").text().trim(); // 学期
	var cid = $("#examineeClass").val(); // 班级编号
	var stuname = $("#examineeName").find("option:selected").text().trim(); // 学生姓名
	var startdate = $("#startDate").val(); // 查询开始时间
	var enddate = $("#endDate").val(); // 查询结束时间
	var datetime = $("#dateTime").val(); // 查询时间段

	if (cid == "--请选择--" || cid <= 0) {
		alert("请选择您要查询的班级");
		return;
	}
	if (stuname == "--请选择--") {
		stuname = null;
	}

	$.post("/Examination2.0/checking_findResult.action",
					{
						// op : 'findCheckingInfoByAll',
						semester : semester,
						cid : cid,
						stuname : stuname,
						startdate : startdate,
						enddate : enddate,
						datetime : datetime
					},
					function(data) {
						var obj = $.parseJSON(data);
						if (obj.responseCode == 1) {
							alert(obj.errorMessage);
							return;
						}
						var stuInfo = obj.obj;
						var str = "";
						var count = 0;
						var count_1 = 0;
						var count_2 = 0;
						var count_3 = 0;
						var count_4 = 0;
						str += "<table width='1000px' border='1px' cellpadding='1' bordercolor='#FFFFFF' cellspacing='1' >"

						if (stuInfo != null && stuInfo.length > 0) {
							str += "<tr><th bordercolor=\"#7EA6DC\" width='40%'>日&nbsp;&nbsp;期</th><th bordercolor=\"#7EA6DC\" width='30%'>状&nbsp;&nbsp;态</th><th bordercolor=\"#7EA6DC\" width='30%'>备&nbsp;&nbsp;注</th></tr>";
							for ( var i = 0; i < stuInfo.length; i++) {
								var sss="";
								var ssss="";
								if(stuInfo[i].subid==1){
									ssss="上午";
								}else if(stuInfo[i].subid==2){
									ssss="下午";
								}else if(stuInfo[i].subid==3){
									ssss="晚上";
								}
								if(stuInfo[i].pointid==1){
									sss="已到";
								}else if(stuInfo[i].pointid==2){
									sss="迟到"
								}else if(stuInfo[i].pointid==3){
									sss="病假"
								}else if(stuInfo[i].pointid==4){
									sss="请假"
								}else if(stuInfo[i].pointid==5){
									sss="旷课"
								}else if(stuInfo[i].pointid==6){
									sss="早退"
								}
								str += "<tr  align=\"center\"  bgColor=\"#EDECEB\" onmouseover=\"this.bgColor='#93BBDF'\" onmouseout=\"this.bgColor='#EDECEB';\" height=\"25\">";
								str += "<td bordercolor=\"#7EA6DC\">"
										+ stuInfo[i].pcontent + "&nbsp;&nbsp;"
										+ ssss + "</td>";
								if (stuInfo[i].pointid == 3
										|| stuInfo[i].pointid == 4) {
									str += "<td bordercolor=\"#7EA6DC\" style='color:green'>"
											+ sss + "</td>";
									count_1++; // 请假
								} else if (stuInfo[i].pointid == 2
										|| stuInfo[i].pointid == 5
										|| stuInfo[i].pointid == 6) {
									str += "<td bordercolor=\"#7EA6DC\" style='color:red'>"
											+ sss + "</td>";
									if (stuInfo[i].pointid == 2) {
										count_2++; // 迟到
									}

									if (stuInfo[i].pointid == 5) {
										count_3++; // 旷课
									}

									if (stuInfo[i].pointid == 6) {
										count_4++; // 早退
									}

								} else {
									str += "<td bordercolor=\"#7EA6DC\">"
											+ sss + "</td>";
									count++; // 已到
								}
								if (stuInfo[i].subname == null
										|| stuInfo[i].subname.toString()
												.trim() == "") {
									str += "<td bordercolor=\"#7EA6DC\">&nbsp;</td></tr>";
								} else {
									str += "<td bordercolor=\"#7EA6DC\">"
											+ stuInfo[i].subname
											+ "</td></tr>";
								}
							}
							str += "</tr></table>";

							$("#findCheckingResultInfo").html(str);
							var str2 = "";
							str2 += "<tr align=\"center\" height=\"40\"><td colspan=\"3\" style=\"font-weight:bold;font-size:18px;\">";
							str2 += stuInfo[0].sname + "&nbsp;&nbsp;考勤总数为："
									+ stuInfo.length
									+ "次&nbsp;&nbsp;&nbsp;&nbsp;";
							str2 += "旷课：<span style=\"color:green;font-weight:bold;font-size:18px;\">"
									+ (count_3 + count_4)
									+ "次</span>&nbsp;&nbsp;&nbsp;&nbsp;";

							str2 += "请假：<span style=\"color:green;font-weight:bold;font-size:18px;\">"
									+ count_1
									+ "次</span>&nbsp;&nbsp;&nbsp;&nbsp;";

							str2 += "迟到：<span style=\"color:green;font-weight:bold;font-size:18px;\">"
									+ count_2
									+ "次</span>&nbsp;&nbsp;&nbsp;&nbsp;";
							str2 += "已到：<span style=\"font-weight:bold;font-size:18px;\">"
									+ count
									+ "次</span>&nbsp;&nbsp;&nbsp;&nbsp;";

							str2 += "</td></tr>";

							$("#findCheckingResultInfo").append(str2);
						} else {
							str += "<tr height=\"25\"><th width=\"8%\" bordercolor=\"#7EA6DC\">日&nbsp;&nbsp;期</th>";
							if (obj.checkingStuInfo != null
									&& obj.checkingStuInfo.length > 0) {
								for ( var i = 0; i < obj.checkingStuInfo.length; i++) {
									str += "<th width=\""
											+ (900 / obj.checkingStuInfo.length)
											+ "px\" id=\"student"
											+ (i + 1)
											+ "\" bordercolor=\"#7EA6DC\" \">"
											+ obj.checkingStuInfo[i] + "</th>";
								}
							}

						}
						if (obj.checkingDateTime != null
								&& obj.checkingDateTime.length > 0) {
							var flag;
							var statu = new Array();
							for ( var i = 0; i < obj.checkingDateTime.length; i++) {
								if (obj.checkingDateTime[i][2] == 1) {
									statu[0] = "上午";
								}
								if (obj.checkingDateTime[i][2] == 2) {
									statu[1] = "下午";
								}
								if (obj.checkingDateTime[i][2] == 3) {
									statu[2] = "晚上";
								}

								str += "<tr height=\"25\"  bgColor=\"#EDECEB\" align=\"center\"><td width=\"8%\" id=\"stuBydateInfo"
										+ (i + 1)
										+ "\" bordercolor=\"#7EA6DC\" \">"
										+ obj.checkingDateTime[i][1]
										+ " "
										+ statu[obj.checkingDateTime[i][2] - 1]
										+ "</td>";
								if (obj.allInfo != null
										&& obj.allInfo.length > 0) {
									for ( var j = 0; j < obj.checkingStuInfo.length; j++) {
										flag = true;
										var status = new Array();
										for ( var k = 0; k < obj.allInfo.length; k++) {
											//obj.allInfo[k][6]   到勤状态
											if (obj.allInfo[k][6] == 1) {
												status[0] = "√";
											}
											if (obj.allInfo[k][6] == 2) {
												status[1] = "〇";
											}
											if (obj.allInfo[k][6] == 3) {
												status[2] = "病假";
											}
											if (obj.allInfo[k][6] == 4) {
												status[3] = "请假";
											}
											if (obj.allInfo[k][6] == 5) {
												status[4] = "×";
											}
											if (obj.allInfo[k][6] == 6) {
												status[5] = "早退";
											}

											if (obj.checkingStuInfo[j] == obj.allInfo[k][1]
													&& obj.allInfo[k][7] == obj.checkingDateTime[i][1]
													&& obj.allInfo[k][4] == obj.checkingDateTime[i][2]) {
												flag = false;
												if (obj.allInfo[k][6] == 3
														|| obj.allInfo[k][6] == 4) {
													str += "<td id=\"stuInfo"
															+ (k + 1)
															+ "\" \" bordercolor=\"#7EA6DC\" style='color:green'>"
															+ status[obj.allInfo[k][6] - 1]
															+ "</td>";
												} else if (obj.allInfo[k][6] == 2
														|| obj.allInfo[k][6] == 5
														|| obj.allInfo[k][6] == 6) {
													str += "<td id=\"stuInfo"
															+ (k + 1)
															+ "\" \" bordercolor=\"#7EA6DC\" style='color:red'>"
															+ status[obj.allInfo[k][6] - 1]
															+ "</td>";
												} else {
													str += "<td id=\"stuInfo"
															+ (k + 1)
															+ "\" \" bordercolor=\"#7EA6DC\">"
															+ status[obj.allInfo[k][6] - 1]
															+ "</td>";
												}
												break;
											}
										}
										if (flag) {
											str += "<td bordercolor=\"#7EA6DC\">&nbsp;</td>";
										}
									}
								}
								str += "</tr>";
							}
						}
						str += "</tr></table>";
						$("#findCheckingResultInfo").html(str);

						if(obj.warnInfo!=null &&obj.warnInfo.length){
							var status2="<br /><font color=\"red\">以下人员迟到次数已达3次：";
							var flag1=false;
							var status5="<br /><font color=\"red\">以下人员旷课次数已达3次：";
							var flag2=false;
							var status6="<br /><font color=\"red\">以下人员早退次数已达3次：";
							var flag3=false;
							var str2="";
							
							str2+="<br/><br /><font color=\"red\"><b>警告：</b></font><br />";
							for(var i=0;i<obj.warnInfo.length;i++){
								if(obj.warnInfo[i][1]==2){
									flag1=true;
									status2+=obj.warnInfo[i][2]+"  ";
								}else if(obj.warnInfo[i][1]==5){
									flag2=true;
									status5+=obj.warnInfo[i][2]+"  ";
								}else{
									flag3=true;
									status6+=obj.warnInfo[i][2]+"  ";
								}
							}
							if(flag1){
								str2+=status2+"</font>";
								flag1=false;
							}
							if(flag2){
								str2+=status5+"</font>";
								flag2=false;
							}
							if(flag3){
								str2+=status6+"</font>";
								flag3=false;
							}
						}
						
						$("#warnInfo").html(str2);
					});
}

// 统计鼠标放上
function totalMyCheckingInfo(name, val, e) {
	// 这里可得到鼠标X坐标
	var pointX = e.pageX;
	// 这里可以得到鼠标Y坐标
	var pointY = e.pageY;
	$("#tip").css({
		left : pointX,
		top : pointY
	}).show();
	var strInfo = "";
	if (id == val) {
		return;
	}
	$.post("/Examination2.0/checking_totalResultByStuname.action", {
		sname : name,
		val : val
	}, function(json) {
		var obj = $.parseJSON(json);
		if (obj.responseCode == 0) {
			var list = obj.obj.list;
			var suminfo = obj.obj.suminfo;
			var status = new Array();
			for ( var i = 0; i < list.length; i++) {
				if (list[i][0])
					if (list[i][0] == 1) {
						status[0] = "已到";
					}
				if (list[i][0] == 2) {
					status[1] = "迟到";
				}
				if (list[i][0] == 3) {
					status[2] = "病假";
				}
				if (list[i][0] == 4) {
					status[3] = "请假";
				}
				if (list[i][0] == 5) {
					status[4] = "旷课";
				}
				if (list[i][0] == 6) {
					status[5] = "早退";
				}

				if (list[i][0] == 3 || list[i][0] == 4) {
					strInfo += status[list[i][0] - 1]
							+ "：<font color=\"green\">" + list[i][1]
							+ " 次</font>&nbsp;&nbsp;&nbsp;&nbsp;";
				} else if (list[i][0] == 2 || list[i][0] == 5
						|| list[i][0] == 6) {
					strInfo += status[list[i][0] - 1] + "：<font color=\"red\">"
							+ list[i][1] + " 次</font>&nbsp;&nbsp;&nbsp;&nbsp;";
				} else {
					strInfo += status[list[i][0] - 1] + "：<font>" + list[i][1]
							+ " 次</font>&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}
			strInfo += "<br/><br/><font color=\"red\"><b>总计：" + suminfo
					+ " 次</b></font>"
			$("#tip").html(strInfo);
		} else {
			$("#tip").html(obj.errorMessage);
		}
	});

}

// 统计鼠标移开时
function totalMyCheckingout(val) {
	$("#tip").hide();
}

// 日期鼠标放上时
function totalDateCheckingInfo(val, ppid, e) {
	// 这里可得到鼠标X坐标
	var pointX = e.pageX;
	// 这里可以得到鼠标Y坐标
	var pointY = e.pageY;
	$("#tip").css({
		left : pointX,
		top : pointY
	}).show();
	if (id == val) {
		return;
	}
	$.post("/Examination2.0/checking_totalResultByDate.action", {
		ppid : ppid
	}, function(data) {
		var remark = $.parseJSON(data);
		if (remark.responseCode == 0) {
			$("#tip").html(remark.obj);
		} else {
			$("#tip").html(remark.errorMessage);
		}

	});
	// var tip = document.getElementById("tip");
	// tip.style.pixelTop = event.clientY
	// tip.style.pixelLeft = event.clientX;
	// document.getElementById(val).className = "tronmouseover";
	// $.post("totalCheckingInfoServlet.do?t=" + new Date(), {
	// op : 'totalResultByDate',
	// ppid : ppid
	// }, function(data) {
	// tip.innerHTML = data;
	// tip.style.display = "";
	// });
}

// 日期鼠标移开时
function totalDateCheckingout(val) {
	$("#tip").hide();
}

// 状态鼠标放上时
function checkingInfoByPointOver(val, ppid, sname, e) {
	// 这里可得到鼠标X坐标
	var pointX = e.pageX;
	// 这里可以得到鼠标Y坐标
	var pointY = e.pageY;
	$("#tip").css({
		left : pointX,
		top : pointY
	}).show();
	if (id == val) {
		return;
	}
	$.post("/Examination2.0/checking_totalResultByStatus.action", {
		ppid : ppid,
		sname : sname
	}, function(data) {
		var remark = $.parseJSON(data);
		if (remark.responseCode == 0) {
			$("#tip").html(remark.obj);
		} else {
			$("#tip").html(remark.errorMessage);
		}

	});

	// var tip = document.getElementById("tip");
	// tip.style.pixelTop = event.clientY
	// tip.style.pixelLeft = event.clientX;
	// document.getElementById(val).className = "tronmouseover";
	// $.post("totalCheckingInfoServlet.do?t=" + new Date(), {
	// op : 'totalResultByStatus',
	// ppid : ppid,
	// sname : sname
	// }, function(data) {
	// tip.innerHTML = data;
	// tip.style.display = "";
	// });
}

// 状态鼠标移开时
function checkingInfoByPointOut(val) {
	document.getElementById(val).className = "classmouseout1";
	document.getElementById("tip").style.display = "none";
}

// 学生端考勤记录查询
function findCheckingInfoByMe() {
	var semester = $('#semesterName').val();
	var startDate = $('#startDate').val();
	var endDate = $('#endDate').val();
	var dateTime = $('#dateTime').val();

	$.post("/Examination2.0/checking_findResultStu.action", {
		semester : semester,
		startDate : startDate,
		endDate : endDate,
		dateTime : dateTime
	}, function(data) {
		$("#showresultInfo").html($(data));
	});
}