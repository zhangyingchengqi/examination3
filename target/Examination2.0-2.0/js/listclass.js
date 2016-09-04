$(function(){
	
	var semester=$("#semesterS1").attr('value');
	$("#semesterS1").prop('checked',true);
	changeSemester(semester);

});


function changeSemester(semester){ 
//	$("#semesterS1").attr("checked",true);
//	var semester=$("#semesterS1").attr('value');
	$.post("/Examination2.0/examineeclass_showClassList.action",{semester:semester},function(json){
		var obj=$.parseJSON(json);
		$("#message").text("查询到 "+obj.length+" 个班级");
		$("#showlist").html("");
		for( var i=0;i<obj.length;i++){
			$("#showlist").append("<tr bgcolor='#EDECEB' onMouseOver=this.bgColor='#93BBDF';  onMouseOut=this.bgColor='#EDECEB';  align='center' > "+
	                  "<td align='center'>"+obj[i].className+"</td>"+
	                  
	                 " <td align='left' title=''> "+obj[i].remark+"  </td>"+
	                  "<td align='center'>"+obj[i].studentCount+"</td>"+
	                  "<td align='center'>"+obj[i].createDate+"</td>"+
	                 " <td align='center'>"+obj[i].overDate+"</td>"+
	                 " <td align='center'>"+
	                  "  <a href='listexaminee.html?op=show&semester="+semester+"&className="+obj[i].className+"'  title='查看班级考生信息'>考生信息</a>"+
	                 " </td>"+
                  	"	<td><a href='updateclass.html?id="+obj[i].id+",&remark="+obj[i].remark+",&semester="
                  	+obj[i].semester+",&studentCount="+obj[i].studentCount+",&createDate="
                  	+obj[i].createDate+",&overDate="+obj[i].overDate+",&className="+obj[i].className+",'>修改</a>&nbsp;<a href='#'  onClick=deleteClass('"+obj[i].studentCount+"','"+obj[i].id+"');>删除</a></td>"+
               " </tr>");
		}
	});
}

function deleteClass(count,id){
	
	if(!confirm("确定要删除？")){
		return false;
	}
	if(count!=0 ||count!="0"){
		alert("该班级下还有考生，不能删除");
		return false;
	}
	
	$.post("/Examination2.0/examineeclass_deleteClass.action",{id:id},function(data){
		var obj=$.parseJSON(data);
		if(obj.responseCode==0){
			alert("删除成功");
			location.reload(); 
		}
	});
	
	
	
	
}

