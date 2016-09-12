$(function() {
    $.post("/Examination2.0/direction_direction.action", function(json) {
        var strJSON = json;
        var obj = eval(strJSON);// 转换后的JSON对象
        var result = '';
        for (var i = 0; i < obj.length; i++) {
            result += '<option value ="' + obj[i].did + "-" + obj[i].classname
                    + '">' + obj[i].dname + '</option>';
        }
        $("#directionName").append(result);
        
        getedition(1);
    });
})

/* xh */
var did = "";
var sid = "";
var semester = "0";
var snumber = "0";
var classN = "";
//改变方向
function changeDirection() {
    did = $("#directionName").val();
    var str = did.split("-");
    did = str[0];
    classN = str[1];
    getedition(did);
}
//改变版本
function changeSubject() {
    sid = $("#editionName").val();
    var str = sid.split("-");
    sid = str[0];
    snumber = str[1];
    getchapter(sid, semester, true);
}
//改变改变学期
function changeSemester() {
    semester = $("#semesterName").val();
    getchapter(sid, semester, false);
}
//获取版本
function getedition(did) {
    $.post("/Examination2.0/course_edition.action", {
        did : did
    }, function(json) {
        var strJSON = json;
        var obj = eval(strJSON);// 转换后的JSON对象
        $("#editionName").html("");
        $("#editionName").append('<option value="0">');
        for (var i = 0; i < obj.length; i++) {
            $("#editionName").append(
                    "<option value='" + obj[i].id + "-" + obj[i].semesternum
                            + "-" + obj[i].semesternum + "'>"
                            + obj[i].editionName + "</option>");
        }
    });
}

//获取章节
function getchapter(sid, semester, flag) {
    $.post("/Examination2.0/chapter_subjectName.action", {
        sid : sid,
        semester : semester
    }, function(json) {
        var obj = eval(json);// 转换后的JSON对象
        if (flag) {
            $("#semesterName").html("");
            for (var i = 1; i <= snumber; i++) {
                $("#semesterName").append(
                        "<option value='S" + i + "'>S" + i + "</option>");
            }
        }
        $("#subjectName").html("");
        $("#subjectName").append('<option value="0">');
        for (var i = 0; i < obj.length; i++) {
            $("#subjectName").append(
                    "<option value='" + obj[i].id + "'>" + obj[i].subjectName
                            + "</option>");
        }
        
        showClass();
    });
}

//通过DID获取班级学期数
function getECNum() {
    $.post("/Examination2.0/examineeclass_getCNumByDid.action", {
        did : did
    }, function(json) {
        var obj = eval(json);// 转换后的JSON对象
        var num = obj.length;
        var newClassName = classN + (num + 1);
        $("#className").val(newClassName);
    });
}
//通过Did获取班级
function getClassByDid() {
    did = $("#directionName").val();
    var str = did.split("-");
    did = str[0];
    classN = str[1];
    getedition(did);
    $.post("/Examination2.0/examineeclass_getCNumByDid.action", {
        did : did
    }, function(json) {
        var obj = eval(json);// 转换后的JSON对象
        showClass(obj);
    });
}
//通过Eid获取班级
function getClassByEid() {
    sid = $("#editionName").val();
    var str = sid.split("-");
    sid = str[0];
    snumber = str[1];
    $.post("/Examination2.0/examineeclass_getClassByEdition.action", {
        eid : sid,
    }, function(json) {
        var obj = eval(json);// 转换后的JSON对象
        showClass(obj);
    });
}