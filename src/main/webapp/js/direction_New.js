

$(function(){
	$.post("/Examination2.0/direction_direction.action", function(json){
		var strJSON=json;
		var obj = eval(strJSON);//转换后的JSON对象
		//alert();
		for(var i=0;i<obj.length;i++){
			$("#showDriection").append("<tr bgcolor='#EDECEB'  onmouseover=this.bgColor='#93BBDF'; onmouseout=this.bgColor='#EDECEB';> "
									+"<td align='center' id='did'"+obj[i].did+">"+(i+1)
									+"</td>"
									+"<td align='center' >"
									+"	<input name='txtDname' id='dname"+obj[i].did+"'"
									+"		type='text' class='text4'  size='15' value="+obj[i].dname 
									+"		maxlength='15' />"
									+"</td>"
									+"<td align='center' >"
                                    +"  <input name='classname' id='cname"+obj[i].did+"'"
                                    +"      type='text' class='text4'  size='15' value="+obj[i].classname 
                                    +"      maxlength='15' />"
                                    +"</td>"
									+"<td align='center' >"
                                    +"  <input name='txtDname' id='remark"+obj[i].did+"'"
                                    +"      type='text' class='text4'  size='15' value="+obj[i].remark 
                                    +"      maxlength='15' />"
                                    +"</td>"
									+"<td align='center'>  "
								+"		<input type='radio' name='currentUse' id='currentUse"+obj[i].did+"' value="+obj[i].currentUse+"   onclick=changeCurrentUse('"+obj[i].did+"',this.value)>"
								+"	</td><td align='center'>"
								+"		<input name='act' type='button' class='inp_L1' "
								+"			value='修 改' id='"+obj[i].did+"' onclick=chkUpdate("+ obj[i].did +",'dname"+obj[i].did +",'cname"+obj[i].did+"','"+obj[i].dname+"','"+obj[i].remark+"')"
								+"			onmouseover=this.className='inp_L2'  "
								+"			onmouseout=this.className='inp_L1' /> "
								+"	</td>"
								+"	<td align='center'>"
								+"		<input name='act' type='button' class='inp_L1' "
								+"			value='删 除' id='"+obj[i].did+"' onclick='return chkForm(this)' "
								+"			onmouseover=this.className='inp_L2' "
								+"			onmouseout=this.className='inp_L1' />"
								+"	</td></tr> ");
			
			if($("#currentUse"+obj[i].did).attr('value')==1){
				$("#currentUse"+obj[i].did).attr("checked",true);
			}
		}
	});
});


//修改按钮
function chkUpdate(dId, direction, cname, Odname,  Oremark) {
	var did=dId;
	var dname=$("#"+direction).val();
    var cname=$("#"+cname).val();
	var remark=$("#remark"+did).val();
    var currentUse=$("#currentUse"+did).val();
	if(dname==null  ||  dname==""){
	    alert('方向名不能为空');
        /*$.messager.show({
            title:'提示',
            msg:'方向名不能为空',
            timeout:3000,
            showType:'slide'
            });*/
		$("#"+direction).focus();
		return;
	}
	if(cname==null  ||  cname==""){
        alert('班级命名规则不能为空');
        /*$.messager.show({
            title:'提示',
            msg:'方向名不能为空',
            timeout:3000,
            showType:'slide'
            });*/
        $("#"+direction).focus();
        return;
    }
	if(dname==Odname){
        alert('您所要修改的方向名称没有改变');
        /*$.messager.show({
            title:'提示',
            msg:'您所要修改的方向名称没有改变',
            timeout:3000,
            showType:'slide'
            });*/
		return;
	}
	if(remark==Oremark){
        alert('您所要修改的方向备注没有改变');
        /*$.messager.show({
            title:'提示',
            msg:'您所要修改的方向备注没有改变',
            timeout:3000,
            showType:'slide'
            });*/
        return;
    }
	$.post("/Examination2.0/direction_updateDirection.action",{did:did,currentUse:currentUse,cname:cname,dname:dname,remark:remark},function(data){
		if(data==1){
	        alert('方向修改成功');
			/*$.messager.show({
                title:'提示',
                msg:'方向修改成功',
                timeout:3000,
                showType:'slide'
                });*/
		}else{
	        alert('方向修改失败');
			/*$.messager.show({
                title:'提示',
                msg:'方向修改失败',
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
	var dname=$("#txtNewDriectionName").val();
    var cname=$("#classname").val();
    var remark=$("#txtNewDriectionRemark").val();
	
	if(submitButton.value=="增 加") {
		if(dname=="" || dname==null) {
			alert("新方向名称不能为空");
			$("#txtNewDirectionName").focus();
			return false;
		}	
        if(remark=="" || remark==null) {
            alert("新方向备注不能为空");
            $("#txtNewDirectionRemark").focus();
            return false;
        }   
		$.getJSON("/Examination2.0/direction_addDirection.action",{dname:dname,cname:cname,remark:remark},function(data){
			if(data==1){
		        alert('方向添加成功');
				/*$.messager.show({
                    title:'提示',
                    msg:'方向添加成功',
                    timeout:3000,
                    showType:'slide'
                    });*/
				$("#txtNewDirectionName").val("");
                $("#txtNewDirectionRemark").val("");
				location.reload(); 
			}else if(data==0){
		        alert('方向添加失败');
				/*$.messager.show({
	                title:'提示',
	                msg:'方向添加失败',
	                timeout:3000,
	                showType:'slide'
	                });*/
			}else{
		        alert('方向版本异常，请与管理员联系');
				/*$.messager.show({
	                title:'提示',
	                msg:'方向版本异常，请与管理员联系',
	                timeout:3000,
	                showType:'slide'
	                });*/
			}
		});
		
	}
	//删除按钮
	else if(submitButton.value=="删 除") {
//		var editionId=$("#editionId").text();
		did=id;
		if(!confirm("真的要删除此方向吗？")) {
			return false;
		}
		$.post("/Examination2.0/direction_deleteDirection.action",{did:did},function(data){
			if(data==1){
		        alert('已删除此方向');
				/*$.messager.show({
                    title:'提示',
                    msg:'已删除此方向',
                    timeout:3000,
                    showType:'slide'
                    });*/
				location.reload(); 
			}else{
		        alert('此方向下还有版本存在，不能删除');
			    /*$.messager.show({
                    title:'提示',
                    msg:'此方向下还有版本存在，不能删除',
                    timeout:3000,
                    showType:'slide'
                    });*/
			}
		});
	}
	return true;
}


//后台提交改变版本号
function changeCurrentUse(did,currentUse) {
	if(currentUse==1){
		return;
	}else{
		$.post("/Examination2.0/direction_changeCurrentUse.action",
				{did:did},function(data){
					if(data==1){
						location.reload(); 
					}
				});
	}
}