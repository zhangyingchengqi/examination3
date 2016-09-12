$(function(){
    
	var result=null;
	result=getQueryString();

	$.ajaxSettings.async = false;
	var arr=result.toString().split(",");
	var op=arr[0].split("=")[1];
	var id=arr[1].split("=")[1];
	
	serchExaminee(id);

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
    alert(result);
    return result;
}

function updateExaminee(id,oldname){

    var uid=$('#uid').val();
    var name=$('#name').val();
    var realname = $('#realname').val();
    var age = $('#age').val();
    var sex = $('input:radio:checked').val();
    var idcard = $('#idcard').val();
    var wechat = $('#wechat').val();
    var qq = $('#qq').val();
    var phone = $('#phone').val();
    var address =$('#address').val();
    var school = $('#school').val();
    var grade = $('#grade').val();
    var Professional = $('#Professional').val();
    var bedroom =$('#bedroom').val();
    
	if(confirm("确定要修改？")){
		$.post("/Examination2.0/login_updateInfo.action",{uid:uid,name:name,realname:realname,age:age,sex:sex,idcard:idcard,wechat:wechat,qq:qq,phone:phone,address:address,school:school,grade:grade,Professional:Professional,bedroom:bedroom},function(json){
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


function serchExaminee(id){
    alert(id);
    $.post("/Examination2.0/examineeclass_getExamById.action",{id:id},function(json){
        var obj=$.parseJSON(json);
        alert(obj.obj);
        if(obj.responseCode==0){
            $("#uid").val(obj.obj.id);
            $("#name").val(obj.obj.name);
            $("#realname").val(obj.obj.realname);
            $("#age").val(obj.obj.age);
            if(obj.obj.sex == "1"){
                $('input[name="sex"][value="nan"]').attr('checked', true);
            }else{
                $('input[name="sex"][value="nv"]').attr('checked', true);
            }
            
            $("#idcard").val(obj.obj.idcard);
            $("#wechat").val(obj.obj.wechat);
            $("#qq").val(obj.obj.qq);
            $("#phone").val(obj.obj.phone);
            $("#address").val(obj.obj.address);
            $("#school").val(obj.obj.school);
            $("#grade").val(obj.obj.grade);
            $("#Professional").val(obj.obj.professional);
            $("#bedroom").val(obj.obj.bedroom);
            
        }else{
            alert(obj.errorMessage);
        }
    });
}


