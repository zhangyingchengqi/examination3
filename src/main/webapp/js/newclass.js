var val;
$(function(){
	
	val=$("#semesterS1").attr('value');
	$("#semesterS1").prop('checked',true);
})

function reset(){
	$("#semesterS1").checked=true;
	$("#className").val("");
	$("#openTime").val("");
	$("#overTime").val("");
	$("#textarea").val("");
}
var check;
function checkClassName(){
	var className=$("#className").val();
	$.post("/Examination2.0/examineeclass_checkClassName.action",{className:className},function(data){
		var obj=$.parseJSON(data);
		if(obj.responseCode==0){
			$("#classTip").html("该班级已经存在，请重新输入班级编号");
			check=null;
			return;
		}else{
			check="1";
			return;
		}
	});
}

function submitForm(){
	var semester="S1";
	var eid=$("#editionName").val();
	var str = eid.split("-");
	eid = str[0];
	var snumber = str[1];
	var className=$("#className").val();
	var createDate=$("#openTime").val();
	var overDate=$("#overTime").val();
	var remark=$("#textarea").val();
	   
	if(eid=="" || eid==null){
	        alert("班级不能为空");
	        return;
	 }
	if(className=="" || className==null){
		alert("班级不能为空");
		return;
	}
	
	/*checkClassName();
	if(check==null){
		alert("请重新输入班级编号");
		return;
	}*/
	
	if(createDate=="" || createDate==null){
		alert("开班时间不能为空");
		return;
	}
	
	
	$.post("/Examination2.0/examineeclass_addClass.action",{snumber:snumber,eid:eid,semester:semester,className:className,
		createDate:createDate,overDate:overDate,remark:remark},
			function(json){
			var obj=$.parseJSON(json);
			if(obj.responseCode==0){
				$("#strPromp").text("班级添加成功");
				reset();
			}
			else if(obj.responseCode==1){
				$("#strPromp").val(obj.errorMessage);
			}
	});
}