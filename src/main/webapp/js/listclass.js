$(function(){
	var semester="S1";
	$("#semesterS1").prop('checked',true);
	//getclass(semester);
	$.post("/Examination2.0/examineeclass_showClassList.action",{semester:semester},function(json){
        var obj=$.parseJSON(json);
        showClassInfo(obj);
    });
});


function showClassInfo(obj){ 
    if(obj == null){
        $("#message").text("查询到  0 个班级");
        $("#showlist").html("");
    }else{
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
    }
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


function getclass(semester){
    $('#table_class').datagrid({
            url : '/Examination2.0/examineeclass_showClassList.action',
            fit : true,
            border : true,
            fitColumns : true,
            pagination : true,
            singleSelect : true,
            sortOrder : 'asc', //排序规则
            pageList : [ 5, 10, 15, 20, 30, 50, 100 ], //每页多少条
            pageSize : 5, //默认每页多少条
            rownumbers : true, //排序ID
            singleSelect : true,
            
            columns : [ [
                    {   
                        filed : 'id',
                        title : '编号',
                        checkbox : true,
                    },
                    {
                        field : 'className',
                        title : '班级名称',
                        width : 100,
                        align : 'center',
                    },
                    {
                        field : 'remark',
                        title : '班级描述',
                        width : 100,
                        align : 'center',
                        sortable : true
                    //可排序
                    },
                    {
                        field : 'studentCount',
                        title : '考生数量',
                        width : 150,
                        align : 'center',
                        editor : {
                            type : 'validatebox',
                        }
                    },
                    {
                        field : 'createDate',
                        title : '开班日期',
                        width : 150,
                        align : 'center',
                    },
                    {
                        field : 'overDate',
                        title : '结束日期',
                        width : 150,
                        align : 'center',
                    },
                    {
                        field : 'chapterCount',
                        title : '信息',
                        width : 150,
                        align : 'center',
                        formatter : function(value, index) {
                            return "  <a href='#'  title='查看班级考生信息' onclick='saverow("+ index + ")'>考生信息</a>";
                        }
                    },
                    {
                        field : 'action',
                        title : '操作',
                        width : 150,
                        align : 'center',
                        formatter : function(value, index) {
                            
                        }

                    } ] ]
        });
}
