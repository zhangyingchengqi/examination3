

$(function(){
    $.post("/Examination2.0/direction_direction.action", function(json){
        var strJSON=json;
        var obj = eval(strJSON);//转换后的JSON对象
        var result ='';
        for(var i=0;i<obj.length;i++){
            result += '<option value ="'+obj[i].did+'">'+obj[i].dname+'</option>';
        }
        $("#direction").append(result);
    });
});


var did = "";

function changeDirection(){
    did = $("#direction").val();
    getedition(did);
}

function getedition(did){
    $.post("/Examination2.0/course_edition.action", {did:did},function(json){
        var strJSON=json;
        var obj = eval(strJSON);//转换后的JSON对象

        //alert();
        $("#showEdition").html("");
        for(var i=0;i<obj.length;i++){
            $("#showEdition").append("<tr bgcolor='#EDECEB'  onmouseover=this.bgColor='#93BBDF'; onmouseout=this.bgColor='#EDECEB';> "
                                    +"<td align='center' id='editionId'"+obj[i].id+">"+(i+1)
                                    +"</td>"
                                    +"<td align='center' >"
                                    +"  <input name='txtEditionName' id='editionName"+obj[i].id+"'"
                                    +"      type='text' class='text4'  size='15' value="+obj[i].editionName 
                                    +"      maxlength='15' />"
                                    +"</td>"
                                    +"<td align='center' >"
                                    +"  <input name='txtEditionName' id='semesternum"+obj[i].id+"'"
                                    +"      type='text' class='text4'  size='15' value="+obj[i].semesternum 
                                    +"      maxlength='15' />"
                                    +"</td>"
                                    +"<td align='center'>  "
                                +"      <input type='radio' name='currentUse' id='currentUse"+obj[i].id+"' value="+obj[i].currentUse+"   onclick=changeCurrentUse('"+obj[i].id+"',this.value)>"
                                +"  </td><td align='center'>"
                                +"      <input name='act' type='button' class='inp_L1' "
                                +"          value='修 改' id='"+obj[i].id+"' onclick=chkUpdate("+ obj[i].id +",'editionName"+obj[i].id+",'semesternum"+obj[i].id+"','"+obj[i].editionName+"')"
                                +"          onmouseover=this.className='inp_L2'  "
                                +"          onmouseout=this.className='inp_L1' /> "
                                +"  </td>"
                                +"  <td align='center'>"
                                +"      <input name='act' type='button' class='inp_L1' "
                                +"          value='删 除' id='"+obj[i].id+"' onclick='return chkForm(this)' "
                                +"          onmouseover=this.className='inp_L2' "
                                +"          onmouseout=this.className='inp_L1' />"
                                +"  </td></tr> ");
            
            if($("#currentUse"+obj[i].id).attr('value')==1){
                $("#currentUse"+obj[i].id).attr("checked",true);
            }
        }
    });
}

//修改按钮
function chkUpdate(id,edition,snum,OeditionName) {
	
	
	var editionId=id;
	var editionName=$("#"+edition).val();
    var semesternum=$("#"+snum).val();
	var currentUse=$("#currentUse"+id).val();
	if(editionName==null  ||  editionName==""){
        alert('版本名不能为空');
        /*$.messager.show({
            title:'提示',
            msg:'版本名不能为空',
            timeout:3000,
            showType:'slide'
            });*/
		$("#"+edition).focus();
		return;
	}
	if(semesternum==null  ||  semesternum==""){
        alert('学期数不能为空');
        /*$.messager.show({
            title:'提示',
            msg:'版本名不能为空',
            timeout:3000,
            showType:'slide'
            });*/
        $("#"+edition).focus();
        return;
    }
	
	if(editionName==OeditionName){
        alert('您所要修改的版本名称没有改变');
        /*$.messager.show({
            title:'提示',
            msg:'您所要修改的版本名称没有改变',
            timeout:3000,
            showType:'slide'
            });*/
		return;
	}
	
	$.post("/Examination2.0/course_updateCurrent.action",{editionId:editionId,currentUse:currentUse,editionName:editionName,semesternum:semesternum},function(data){
		if(data==1){
            alert('版本号修改成功');
	        /*$.messager.show({
	            title:'提示',
	            msg:'版本号修改成功',
	            timeout:3000,
	            showType:'slide'
	            });*/
		}else{
            alert('版本号修改失败');
	        /*$.messager.show({
	            title:'提示',
	            msg:'版本号修改失败',
	            timeout:3000,
	            showType:'slide'
	            });*/
		}
	});
}

//增加和删除按钮
function chkForm(submitButton) {
	//添加按钮
	var id = submitButton.id;
	var editionName=$("#txtNewEditionName").val();
    var semesternum=$("#semesternum").val();
	
	if(submitButton.value=="增 加") {
		if(editionName=="" || editionName==null) {
            alert('新版本名称不能为空');
	        /*$.messager.show({
	            title:'提示',
	            msg:'新版本名称不能为空',
	            timeout:3000,
	            showType:'slide'
	            });*/
			$("#txtNewEditionName").focus();
			return false;
		}	
		if(semesternum=="" || semesternum==null) {
            alert('学期数不能为空');
            /*$.messager.show({
                title:'提示',
                msg:'新版本名称不能为空',
                timeout:3000,
                showType:'slide'
                });*/
            $("#semesternum").focus();
            return false;
        }   
		$.getJSON("/Examination2.0/course_addEdition.action",{editionName:editionName,did:did,semesternum:semesternum},function(data){
			if(data==1){
                alert('版本添加成功');
		        /*jQuery.messager.show({
		            title:'提示',
		            msg:'版本添加成功',
		            timeout:3000,
		            showType:'slide'
		            });*/
				$("#txtNewEditionName").val("");
                $("#semesternum").val("");
				location.reload(); 
			}else if(data==0){
                alert('版本添加失败');
			    /*jQuery.messager.show({
		            title:'提示',
		            msg:'版本添加失败',
		            timeout:3000,
		            showType:'slide'
		            });*/
			}else{
                alert('添加版本异常，请与管理员联系');
			    /*jQuery.messager.show({
		            title:'提示',
		            msg:'添加版本异常，请与管理员联系',
		            timeout:3000,
		            showType:'slide'
		            });*/
			}
		});
		
	}
	//删除按钮
	else if(submitButton.value=="删 除") {
		
//		var editionId=$("#editionId").text();
		editionId=id;
		if(!confirm("真的要删除此版本号吗？")) {
			return false;
		}
		$.post("/Examination2.0/course_deleteEdition.action",{editionId:editionId},function(data){
			if(data==1){
			    alert('已删除版本号');
		        /*$.messager.show({
		            title:'提示',
		            msg:'已删除版本号',
		            timeout:3000,
		            showType:'slide'
		            });*/
				location.reload(); 
			}else{
		        /*$.messager.show({
		            title:'提示',
		            msg:'此版本下还有课程存在，不能删除',
		            timeout:3000,
		            showType:'slide'
		            });*/
                alert('此版本下还有课程存在，不能删除');
			}
		});
	}
	return true;
}


//后台提交改变版本号
function changeCurrentUse(id,currentUse) {
	if(currentUse==1){
		return;
	}else{
		$.post("/Examination2.0/course_changeCurrentUse.action",
				{id:id},function(data){
					if(data==1){
						location.reload(); 
					}
				});
	}
}