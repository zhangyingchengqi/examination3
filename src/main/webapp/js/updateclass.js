var val;
$(function(){
	
	var result=getQueryString();
	var arr=result.toString().split(",");
	var id=arr[0].split("=")[1];
	var semester=arr[4].split("=")[1];
	var createDate=arr[8].split("=")[1];
	var endDat=arr[10].split("=")[1];
	var className=arr[12].split("=")[1];
	var remark=arr[2].split("=")[1];
	$("#id").val(id);
	$("#semester"+semester).attr("checked",true);
	$("#className").val(className);
	$("#openTime").val(createDate);
	$("#overTime").val(endDat);
	$("#textarea").val(decodeURIComponent(remark));

	$.post("/Examination2.0/examineeclass_getCSByEid.action",{eid:id},function(json){
	    var strJSON = json;
        var obj = eval(strJSON);// 转换后的JSON对象
            //var obj=$.parseJSON(json);
            alert(obj);
            if(obj==null || obj.length==0){
                alert("获取班级学期信息失败");
                javascript:history.go(-1);
            }else{
                $("#CStable").html("");
                for(var i = 0; i < obj.length; i++){
                    $("#CStable").append('<tr><td height="26" class="fontColor">学期名:</td>'
                            +'<td class="STYLE4"><input  style="width:30px;" name="CSsemester" type="text"  readonly="readonly" id="CSsemester'+obj[i].id+'" value="'+obj[i].semester+'" /></td>'
                            +'<td height="26" class="fontColor">开始时间:</td><td class="STYLE4"><label> '
                            +'<input type="text" style="width:100px;" id="starttime'+obj[i].id+'" onclick="fPopCalendar(event,this,this)" onfocus="this.select()" value="'+obj[i].starttime+'"/> </label></td>'
                            +'<td height="26"><span class="fontColor">结束时间:</span></td><td ><span class="STYLE4">'
                            +'<input type="text" id="endtime'+obj[i].id+'" style="width:100px;"  onclick="fPopCalendar(event,this,this)" onfocus="this.select()" value="'+obj[i].endtime+'"/></input> </span></td>'
                            +'<td height="26"><span class="fontColor">描述:</span></td>'
                            +'<td ><span class="STYLE4"> <input type="text" id="remark'+obj[i].id+'" style="width:100px;" value="'+obj[i].remark+'"/></input> </span></td>'
                            +'<td align="center" height="26"><input name="update" type="button" value="修 改" onClick="updateCS('+obj[i].id+')" /> <label></label>'
                            +'</td></tr>');
                }
            }
    });
});
   
   
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
   
   function submitForm(){
	var semester=$('input:radio[name="semester"]:checked').val();
	var id=$("#id").val();
	var className=$("#className").val();
	var createDate=$("#openTime").val();
	var overDate=$("#overTime").val();
	var remark=$("#textarea").val();
	
	$.post("/Examination2.0/examineeclass_updateClass.action",{id:id,semester:semester,className:className,
		createDate:createDate,overDate:overDate,remark:remark},
			function(json){
			var obj=$.parseJSON(json);
			if(obj.responseCode==0){
				//$("#strPromp").text("班级信息更新成功");
				alert("班级信息更新成功");
				javascript:history.go(-1);
			}
			else if(obj.responseCode==1){
				$("#strPromp").val(obj.errorMessage);
			}
	});
   }
   
   function updateCS(id){
       var id=id;
       var createDate=$("#starttime"+id).val();
       var overDate=$("#endtime"+id).val();
       var remark=$("#remark"+id).val();
       
       $.post("/Examination2.0/examineeclass_updateClassSemester.action",{id:id,createDate:createDate,overDate:overDate,remark:remark},
               function(json){
               var obj=$.parseJSON(json);
               if(obj.responseCode==0){
                   alert("班级信息更新成功");
                   javascript:history.go(-1);
               }
               else if(obj.responseCode==1){
                   $("#strPromp").val(obj.errorMessage);
               }
       });
      }