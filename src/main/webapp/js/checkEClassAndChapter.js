var did = "";
var eid = "";
var sid = "";
var semester = "S1";
var snumber = "0";
var classN = "";
function changeDirection() {
    did = $("#direction").val();
    var str = did.split("-");
    did = str[0];
    classN = str[1];
    getedition(did);
    getClassByDid()
}
function getedition(did) {
    $.post("/Examination2.0/course_edition.action", {
        did : did
    }, function(json) {
        var strJSON = json;
        var obj = eval(strJSON);// 转换后的JSON对象
        $("#version").html("");
        $("#version").append('<option value="0">');
        for (var i = 0; i < obj.length; i++) {
            $("#version").append(
                    "<option value='" + obj[i].id + "-" + obj[i].semesternum
                            + "'>" + obj[i].editionName + "</option>");
        }
    });
}
function changeEdition() {
    eid = $("#version").val();
    var str = eid.split("-");
    eid = str[0];
    snumber = str[1];
    getSemester(snumber);
    getchapter(eid);
    getClassByEid()
//生成科目列表
    createSelectOption(eid,semester);
    findPointBySubject()
}
// 改变学期
function changeSemester() {
    semester = $("#semester").val();
    checkSemester(semester);
  //生成科目列表
    createSelectOption(eid,semester);
    findPointBySubject()
}

// 得到学期信息
function getSemester(snumber) {
    $("#semester").html("");
    for (var i = 1; i <= snumber; i++) {
        $("#semester").append(
                "<option value='S" + i + "'>S" + i + "</option>");
    }
}

var sid = "0";
function changeSubject() {
    sid = $("#subject").val();
    getchapter(sid);
    findPointBySubject(sid)
}
function getchapter(sid) {
    $.post("/Examination2.0/chapter_findChapterBySubject.action", {
        sid : sid
    }, function(json) {
        var obj = ($.parseJSON(json)).obj;
        $("#chapter").html("");
        $("#chapter").append('<option value="0">');
        for (var i = 0; i < obj.length; i++) {
            $("#chapter").append(
                    "<option value='" + obj[i].id + "'>" + obj[i].chapterName
                            + "</option>");
        }
    });
}

// 通过Did获取班级
function getClassByDid() {
    did = $("#direction").val();
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
// 通过Eid获取班级
function getClassByEid() {
    eid = $("#version").val();
    var str = eid.split("-");
    eid = str[0];
    snumber = str[1];
    $.post("/Examination2.0/examineeclass_getClassByEdition.action", {
        eid : eid,
    }, function(json) {
        var obj = eval(json);// 转换后的JSON对象
        showClass(obj);
    });
}

function showClass(obj) {
    $("#classes").html("<option>--请选择--</option>");
    if (obj == null) {
        $("#classes").append("<option>--请选择--</option>");
    }
    for (var i = 0; i < obj.length; i++) {
        $("#classes").append(
                "<option value='" + obj[i].id + "'>" + obj[i].className
                        + "</option>");
    }
}

/*//选择学期
function checkSemester(s){
    semester=s;
    version=$('#version').val();
  //生成科目列表
    createSelectOption(version,semester);
    //createClassOption()
    findPointBySubject();
}
//选择版本
function changeVersion(){
    semester=$('#semester').val();
    version=$('#version').val();
    createSelectOption(version,semester);
    findPointBySubject()
}*/