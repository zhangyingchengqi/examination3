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
            var obj=$.parseJSON(json);
            alert(obj);
            if(obj==null || obj.length==0){
                alert("获取班级学期信息失败");
                javascript:history.go(-1);
            }else{
                $("CStable").html("");
                for(var i = 0; i < obj.length; i++){
                    $("CStable").append('<tr><td width="10%" height="26" class="fontColor">学期名:</td>'
                            +'<td width="10%" class="STYLE4"><input name="CSsemester" type="text"  readonly="readonly" id="CSsemester" value="'+obj[i].semester+'" /></td>'
                            +'<td width="12%" height="26" class="fontColor">开始时间:</td><td width="16%" class="STYLE4"><label> '
                            +'<input type="text" id="starttime" onclick="fPopCalendar(event,this,this)" onfocus="this.select()" value="'+obj[i].starttime+'"/> </label></td>'
                            +'<td width="12%" height="26"><span class="fontColor">结束时间:</span></td><td width="16%"><span class="STYLE4">'
                            +'<input type="text" id="endtime" onclick="fPopCalendar(event,this,this)" onfocus="this.select()" value="'+obj[i].endtime+'"/></input> </span></td>'
                            +'<td width="6%" height="26"><span class="fontColor">描述:</span></td>'
                            +'<td width="18%"><span class="STYLE4"> <input type="text" id="remark" value="'+obj[i].remark+'"/></input> </span></td>'
                            +'<td align="center" valign="top"><br> <input name="update" type="button" value="修 改" onClick="updateCS('+obj[i].id+')" /> <label></label>'
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
       var createDate=$("#starttime").val();
       var overDate=$("#endtime").val();
       var remark=$("#remark").val();
       
       $.post("/Examination2.0/examineeclass_updateClassSemester.action",{id:id,createDate:createDate,overDate:overDate,remark:remark},
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