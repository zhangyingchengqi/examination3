$(function(){
	var result=null;
	result=getQueryString();
	if(result==null  || result==""){
		var semester=$("#semesterS1").attr('value');
		$("#semesterS1").prop('checked',true);
		showClassName(semester);
	}else{
		$.ajaxSettings.async = false;
		var arr=result.toString().split(",");
		var op=arr[0].split("=")[1];
		var semester=arr[1].split("=")[1];
		var className=arr[2].split("=")[1];
		$("#semester"+semester).prop('checked',true);
		showClassName(semester);
		$("#className option").each(function(){
		     if($(this).text() == className){
		        $(this).attr("selected",true);
		        return;
		     }
		 });
//		window.history.back(-1);
		$("#back").show();
		serchExaminee(className);
		
	}
	
});

function backPage(){
	window.history.back(-1);
}
var len=0;
function getQueryString(){

    var result = location.search.match(new RegExp("[\?\&][^\?\&]+=[^\?\&]+","g")); 

    if(result == null){

        return "";

    }

    for(var i = 0; i < result.length; i++){

        result[i] = result[i].substring(1);

    }

    return result;

}


function showClassName(semester){
	$.post("/Examination2.0/examineeclass_showClassList.action",{semester:semester},function(json){
		var obj=$.parseJSON(json);
		len=obj.length;
		$("#className").html("");
		if(obj==null){
			$("#className").append("<option>--班级名称--</option>");
		}
		for( var i=0;i<obj.length;i++){
			$("#className").append("<option value='"+i+"'>"+obj[i].className+"</option>");
		}
	});
}

function changeSemester(semester){
	showClassName(semester);
}

function serchExaminee(className){
	if(className==null  ||  className==""){
		className=$("#className").find("option:selected").text().trim();
	}
	
	
	$.post("/Examination2.0/examineeclass_showExamineeList.action",{className:className},function(json){
		var obj=$.parseJSON(json);
		$("#listexaminee").show();
		$("#message").text("查询到 "+obj.length+" 个学生");
		$("#examineerows").html("");
		for(var i=0;i<obj.length;i++){
			$("#examineerows").append("<tr bgcolor='#EDECEB' onMouseOver=this.bgColor='#93BBDF';"+
										"onMouseOut=this.bgColor='#EDECEB'; align='center' "+
										"id='"+obj[i].name+"'>"+
										"<td align='left'>"+(i+1)+"</td>"+
										"<td align='left' id='className"+(i+1)+"'>"+obj[i].className+"</td>"+
										"<td align='left'><input type='text' "+
										"value='"+obj[i].name+"' class='text4' size='18' maxlength='18' "+
										"name='name' id='examinee_"+(i+1)+"'>"+
										"</td>"+
										"<td align='center' width='80'><a href='#' onClick=updateExaminee('"+(i+1)+"','"+obj[i].name+"')>修改</a>"+
										"<a href='#' onClick=deleteExaminee('"+obj[i].name+"')>删除</a>"+
										"</td>"+
										"</tr>");
		}
	});
}

function updateExaminee(id,oldname){
	
	if(confirm("确定要修改？")){
		var name=$("#examinee_"+id).val();
		className=$("#className"+id).text();
		if(name==oldname){
			return;
		}
		
		if(name==null  ||name==""){
			alert("考生姓名不能为空");
			return;
		}
		
		$.post("/Examination2.0/examineeclass_updateExaminee.action",{oldname:oldname,name:name,className:className},function(json){
			var obj=$.parseJSON(json);
			if(obj.responseCode==0){
				alert("修改成功");
				location.reload(); 
			}else{
				alert(obj.errorMessage);
			}
		});
	}
}


function deleteExaminee(name){
	var className=$("#className").find("option:selected").text().trim();
	if(!confirm("确定要删除？")){
		return false;
	}
	$.post("/Examination2.0/examineeclass_deleteExaminee.action",{name:name,className:className},function(json){
		var obj=$.parseJSON(json);
		if(obj.responseCode==0){
			alert("删除成功");
			serchExaminee();
		}
	});
}



