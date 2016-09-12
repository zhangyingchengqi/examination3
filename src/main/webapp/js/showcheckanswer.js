$(function() {
    $.post("/Examination2.0/direction_direction.action", function(json) {
        var strJSON = json;
        var obj = eval(strJSON);// 转换后的JSON对象
        var result = '';
        for (var i = 0; i < obj.length; i++) {
            result += '<option value ="' + obj[i].did + "-" + obj[i].classname
                    + '">' + obj[i].dname + '</option>';
        }
        $("#direction").append(result);
    });
    
    $.ajax({
        url:"/Examination2.0/systemUser_showSystemUserInfo.action",
        type:"post",
        datatype:"json",
        success:showUserNameInfo
    });
});

    /* xh */
    var did = "";
    var sid = "";
    var semester = "0";
    var snumber = "0";
    var classN = "";
    //改变方向
    function changeDirection() {
        did = $("#direction").val();
        var str = did.split("-");
        did = str[0];
        classN = str[1];
        $.post("/Examination2.0/examineeclass_getCNumByDid.action", {
            did : did
        }, function(json) {
            var obj = eval(json);// 转换后的JSON对象
            $("#examclassid").html("<option>--请选择--</option>");
            if (obj == null) {
                $("#examclassid").append("<option>--请选择--</option>");
            }
            for ( var i = 0; i < obj.length; i++) {
                $("#examclassid").append(
                        "<option value='" + obj[i].id + "'>" + obj[i].className
                                + "</option>");
            }
        });
    }

function changeClass(){
    var cid = $("#examclassid option:checked").text();
    alert(cid);
    $.post("/Examination2.0/examineeclass_showExamineeList.action", {
        className : cid
    }, function(json) {
        var obj = eval(json);// 转换后的JSON对象
        $("#examineeName").html("<option>--请选择--</option>");
        if (obj == null) {
            $("#examineeName").append("<option>--请选择--</option>");
        }
        for ( var i = 0; i < obj.length; i++) {
            $("#examineeName").append(
                    "<option value='" + obj[i].eid + "'>" + obj[i].name
                            + "</option>");
        }
    });
}

var systemUser;
function showUserNameInfo(data){
    var uId= window.localStorage? localStorage.getItem("systemUser_uId"): Cookie.read("systemUser_uId");
    var datainfos= eval("(" + data + ")");
    var optionstr="<option value='0'>";
    if(datainfos.responseCode==0){
        if(datainfos.obj!=null){
            systemUser = datainfos.obj;
            $.each(datainfos.obj, function(i,datainfo){
                if(uId==datainfo[0]){
                    optionstr+="<option value='"+datainfo[0]+"' >"+datainfo[1]+"</option>";
                }else{
                    optionstr+="<option value='"+datainfo[0]+"' >"+datainfo[1]+"</option>";  
                }                                           
            }); 
        }
        $("#systemName").html(optionstr);
    }else{
        alert("查询用户名失败！");
    }
}


function changeExaminee(){
    var cid = $("#examclassid").val();
    var eid = $("#examineeName").val();
    var checkDate = $("#checkDate").val();
    if(eid == 0){
        return;
    }
    $.post("/Examination2.0/checkAnswer_showCheckAnswerByEid.action", {
        cid : cid,eid : eid,startDate : checkDate
    },function(json) {
        var strJSON = json;
        var obj = eval(strJSON);// 转换后的JSON对象
        
        cleartable();
        showTable(obj);
    });
}


function changeSystem(){
    var cid = $("#examclassid").val();
    var eid = $("#examineeName").val();
    var sid = $("#systemName").val();
    var checkDate = $("#checkDate").val();

    cleartable();
    if(eid == "--请选择--"){
        $.post("/Examination2.0/checkAnswer_showCheckAnswerBySid.action", {
            cid : cid,sid : sid,startDate : checkDate
        },function(json) {
            var strJSON = json;
            var obj = eval(strJSON);// 转换后的JSON对象

            var min = obj[0];
            var max = obj[1];
            var avg = obj[2];
            
            $("#span1").html("最低分");
            $("#span2").html("最高分");
            $("#span3").html("平均分");

            for (var i = 0; i < 26; i++) {
                $("#answer"+(i+1)+"_1").html(min[i]);
                $("#answer"+(i+1)+"_2").html(max[i]);
                $("#answer"+(i+1)+"_3").html(avg[i]);
            }
            $("#answer27_1").html("");
            $("#answer27_2").html("");
            $("#answer27_3").html("");
            $("#answer28_1").html("");
            $("#answer28_2").html("");
            $("#answer28_3").html("");
        });
    }else{
        $.post("/Examination2.0/checkAnswer_showCheckAnswerByEidandSid.action", {
            eid : eid,sid : sid,startDate : checkDate
        },function(json) {
            var strJSON = json;
            var obj = eval(strJSON);// 转换后的JSON对象
            
            showTable(obj);
        });
    }
    
}

function showTable(obj){
    for(var i = 0; i < obj.length; i++){
        var answer = obj[i];
        for(var j =0; j < systemUser.length; j++){
            if(systemUser[j][0] == answer[28]){
                $("#span"+(i+1)).html(systemUser[j][1]);
            }
        }
        for (var x = 0; x < 28; x++) {
            $("#answer"+(x+1)+"_"+(i+1)).html(obj[i][x]);
        }
    }
}

function cleartable(){
    $("#span1").html("");
    $("#span2").html("");
    $("#span3").html("");
    for (var x = 0; x < 28; x++) {
        for(var i = 0; i< 3; i++){
            $("#answer"+(x+1)+"_"+(i+1)).html("");
        }
    }
}