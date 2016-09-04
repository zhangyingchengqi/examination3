/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/8/23 9:40:40                           */
/*==============================================================*/


drop table if exists pointanswer;
drop table if exists pointpaper;
drop table if exists adailytalk;
drop table if exists examinee;
drop table if exists pointinfo;
drop table if exists chapter;
drop table if exists checking;
drop table if exists examineeclass;
drop table if exists subject;
drop table if exists systemuser;
drop table if exists authority;
drop table if exists labquestion;
drop table if exists edition;
drop table if exists labquestiontype;
drop table if exists direction;
drop table if exists check_constraints;
drop table if exists labanswer;
drop table if exists labpaper;
drop table if exists sysdiagrams;
drop table if exists temp;
drop table if exists writinganswer;
drop table if exists vw_writingquestioninfo;
drop table if exists writingpaper;
drop table if exists writingquestion;


/*==============================================================*/
/* Table: authority                                             */
-- 权限表：  id 权限名 权限范围
/*==============================================================*/
create table authority
(
   id                   int(10) not null auto_increment,
   authorityName        varchar(50) not null,
   bound                varchar(200),
   primary key (id)
);

/*==============================================================*/
/* Table: systemuser                                            */
-- 管理员：  id   用户名  密码   职位   备注 
/*==============================================================*/
create table systemuser
(
   id                   int(10) not null auto_increment,
   userName             varchar(50) not null,
   password             varchar(64) not null,
   authorities          varchar(50) not null,
   remark               varchar(64),
   primary key (id)
);

/*==============================================================*/
/* Table: examineeclass                                         */
-- 班级表：  id   班级名  学期  创建日期   结束日期   备注
/*==============================================================*/
create table examineeclass
(
   Id                   int(10) not null auto_increment,
   className            varchar(50) not null,
   semester             varchar(50) not null,
   createDate           varchar(50),
   overDate             varchar(50),
   remark               varchar(255),
   primary key (Id)
);

/*==============================================================*/
/* Table: checking                                              */
-- 考察表:  id  学期  考察人id   班级id  考察日期  考察时间  考察结果   备注   状态   标记   描述
/*==============================================================*/
create table checking
(
   checkId              int(10) not null auto_increment,
   semester             varchar(50) not null,
   uid                  int(10) not null,
   cid                  int(10) not null,
   checkDate            date not null,
   checkTime            varchar(50)  not null,
   checkResult          text not null,
   checkRemark          text,
   checkStatus          int(10),
   checkFlag            text,
   checkDescript        text,
   primary key (checkId)
);

/*==============================================================*/
/* Table: direction                                             */
-- 方向表:  id  方向名  备注   是否使用
/*==============================================================*/
create table direction
(
   did                  int(10) not null auto_increment,
   dname                varchar(50) not null,
   remark               varchar(50),
   currentUse           int(10),
   primary key (did)
);

/*==============================================================*/
/* Table: edition                                               */
-- 版本表：  id  版本名  是否当前使用
/*==============================================================*/
create table edition
(
   id                   int(10) not null auto_increment,
   did                  int(10),
   editionName          varchar(50) not null,
   currentUse           int(10),
   primary key (id)
);

/*==============================================================*/
/* Table: labquestiontype                                       */
--  机试题目类型   id   技术代码    学期
/*==============================================================*/
create table labquestiontype
(
   id                   int(10) not null auto_increment,
   skillCode            varchar(50) not null,
   semester             varchar(50) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: labquestion                                           */
-- 机试题目表：  id  版本id  学期  问题标题  问题  图像   技术点   难点   备注 
/*==============================================================*/
create table labquestion
(
   id                   int(10) not null auto_increment,
   editionId            int(10) not null,
   semester             varchar(50) not null,
   questionTitle        varchar(255) not null,
   question             text not null,
   image                text,
   skillCodeId          int(10) not null,
   difficulty           int(10),
   remark               varchar(200),
   primary key (id)
);

/*==============================================================*/
/* Table: subject                                               */
-- 课程表： id   课程名  章节数  学期  版本id
/*==============================================================*/
create table subject
(
   Id                   int(10) not null auto_increment,
   subjectName          varchar(100) not null,
   chapterCount         int(10) not null,
   semester             varchar(50) not null,
   editionId            int(10),
   primary key (Id)
);

/*==============================================================*/
/* Table: chapter                                               */
-- 章节表：   id  章节名  课程id  备注
/*==============================================================*/
create table chapter
(
   id                  int(10) not null auto_increment,
   chapterName          varchar(50) not null,
   subjectId            int(10) not null,
   remark               varchar(50),
   primary key (id)
);

/*==============================================================*/
/* Table: pointinfo                                             */
-- 知识点信息表：  id   课程id  内容  状态  备注  标记
/*==============================================================*/
create table pointinfo
(
   pid                  int(10) not null auto_increment,
   cid                  int(10),
   pcontent             varchar(200) not null,
   status               int(10),
   remark               varchar(200),
   flag                 varchar(255),
   primary key (pid)
);

/*==============================================================*/
/* Table: pointpaper                                            */
-- 知识试题：  id  课程id  班级id   知识点名  标题  时间  密码   状态  描述  备注  标记 
/*==============================================================*/
create table pointpaper
(
   pid                  int(10) not null auto_increment,
   sid                  int(10),
   cid                  int(10),
   pname                varchar(100) not null,
   ptitle               text not null,
   pdate                date,
   paperPwd             varchar(50) not null,
   pstatus              varchar(50),
   descript             text,
   remark               varchar(50),
   flag                 varchar(255),
   primary key (pid)
);

/*==============================================================*/
/* Table: pointanswer                                           */
-- 知识点答案：  id   知识点id  名字  班级  答案    意见  备注  状态   aname  claddid 
/*==============================================================*/
create table pointanswer
(
   opid                 int(10) not null auto_increment,
   pid                  int(10),
   name                 varchar(50),
   classId              int(10),
   answer               text,
   idea                 text,
   remark               varchar(200),
   status               varchar(255),
   aname                longtext,
   claddid              longtext,
   primary key (opid)
);

/*==============================================================*/
/* Table: examinee                                              */
-- 考生表：  id  名字  密码  班级id
/*==============================================================*/
create table examinee
(
   id                   int(11) not null auto_increment,
   classId              int(10) not null,
   realname             varchar(50),
   name                 varchar(50) not null,
   password             varchar(64) not null,
   age                  int(10),
   sex                  int(2),
   idcard               varchar(50),
   wechat               varchar(50),
   qq                   varchar(50),
   phone                varchar(50),
   address              varchar(100),
   primary key (id)
);

/*==============================================================*/
/* Table: adailytalk                                            */
-- 每日一讲表：  id  评价  讲课课件  描述  知识点  名字  备注   讲课时间   状态   班级id  
/*==============================================================*/
create table adailytalk
(
   id                   int(11) not null auto_increment,
   assessment           varchar(1000),
   dateinfo             tinyblob,
   descript             longtext,
   knowledge            longtext not null,
   name                 varchar(50),
   remark               varchar(200),
   speakdate            datetime not null,
   status               int(11) not null,
   cid                  int(10) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: check_constraints                                     */
-- 考察约束表：  约束目录   约束概要   约束名   内容条款
/*==============================================================*/
create table check_constraints
(
   CONSTRAINT_CATALOG   varchar(128),
   CONSTRAINT_SCHEMA    varchar(128),
   CONSTRAINT_NAME      varchar(128) not null,
   CHECK_CLAUSE         text
);

/*==============================================================*/
/* Table: labpaper                                              */
--   机试试卷表 ：  id  班级  试题名  试题密码  试题状态  questionId  skillCodeId   
-- 考试时间   所用时间   分数  平均分  最大分  最小分  备注  录入人  疑问 
/*==============================================================*/
create table labpaper
(
   id                   varchar(50) not null,
   examineeClass        varchar(50) not null,
   paperName            varchar(50) not null,
   paperPwd             varchar(50) not null,
   paperStatus          int(10) not null,
   questionId           int(10) not null,
   skillCodeId          int(10),
   examDate             timestamp not null,
   timesConsume         int(10) not null,
   scorePercent         float,
   avgScore             float,
   maxScore             float,
   minScore             float,
   remark               varchar(255),
   operator             varchar(50),
   questioned           int(10) not null,
   primary key (id)
);

/*==============================================================*/
/* Table: labanswer                                             */
-- 机试答案表：  id  试题id   考生名  课称名  内容  分数   评价
/*==============================================================*/
create table labanswer
(
   id                   int(10) not null auto_increment,
   paperId              varchar(50),
   examineeName         varchar(20),
   projectName          varchar(50),
   code                 text,
   score                float,
   comment              varchar(255),
   primary key (id)
);

/*==============================================================*/
/* Table: sysdiagrams                                           */
-- 公司管理层表： 名字  负责人id  diagram_id   版本  描述
/*==============================================================*/
create table sysdiagrams
(
   name                 varchar(128) not null,
   principal_id         int(10) not null,
   diagram_id           int(10) not null,
   version              int(10),
   definition           blob,
   primary key (diagram_id)
);


/*==============================================================*/
/* Table: temp                                                  */
-- 临时表： 
/*==============================================================*/
create table temp
(
   ppid                 int(10),
   sname                varchar(50),
   classid              int(10),
   className            varchar(50),
   subid                int(10),
   subname              varchar(100),
   pointid              int(10),
   pcontent             varchar(200),
   grade                float
);

/*==============================================================*/
/* Table: vw_writingquestioninfo                                */
-- 笔试题目信息    id  学期  版本名  问题类型   课程名  章节名   难点  问题   选项一  选项二  选项三  选项四  答案   备注  图片 
/*==============================================================*/
create table vw_writingquestioninfo
(
   id                   int(10) not null,
   semester             varchar(50) not null,
   editionName          varchar(50) not null,
   questionType         varchar(2) not null,
   subjectName          varchar(100) not null,
   chapterName          varchar(50) not null,
   difficulty           int(10),
   question             text not null,
   optionA              text not null,
   optionB              text not null,
   optionC              text not null,
   optionD              text not null,
   answer               varchar(50),
   remark               text,
   image                text,
   primary key (id)
);

/*==============================================================*/
/* Table: writingpaper                                          */
-- 笔试试卷   id  班级  paperName  paperPwd  考查的课程  试题状态  问题id  问题内容  问题数  考查时间 
--  用时  分数  批改   平均分  最高分  最低分  正确率  备注   记录员
/*==============================================================*/
create table writingpaper
(
   id                   varchar(50) not null,
   examineeClass        varchar(50) not null,
   paperName            varchar(50) not null,
   paperPwd             varchar(50) not null,
   examSubject          varchar(200) not null,
   paperStatus          int(10) not null,
   questionId           text not null,
   questionInfo         text not null,
   countOfQuestion      int(10) not null,
   examDate             timestamp not null,
   timesConsume         int(10) not null,
   scorePercent         varchar(255),
   questionCorrect      text,
   avgScore             float,
   maxScore             float,
   minScore             float,
   correctRate          varchar(255),
   remark               varchar(255),
   operator             varchar(50),
   primary key (id)
);

/*==============================================================*/
/* Table: writingquestion                                       */
-- 笔试题目表  id   版本id  学期  课程名  章节名 问题类型  问题   选项一  选项二  选项三  选项四  图片  答案  难点 备注 
/*==============================================================*/
create table writingquestion
(
   id                   int(10) not null auto_increment,
   editionId            int(10) not null,
   semester             varchar(50) not null,
   subjectId            int(10) not null,
   chapterId            int(10) not null,
   questionType         varchar(2) not null,
   question             text not null,
   optionA              text not null,
   optionB              text not null,
   optionC              text not null,
   optionD              text not null,
   image                text,
   answer               varchar(50),
   difficulty           int(10),
   remark               text,
   primary key (id)
);

/*==============================================================*/
/* Table: writinganswer                                         */
-- 笔试题答案： id  试题id  考生名 答案  分数   正确率  剩余时间
/*==============================================================*/
create table writinganswer
(
   id                   int(10) not null auto_increment,
   paperId              varchar(50) not null,
   examineeName         varchar(50) not null,
   answer               varchar(255),
   score                float,
   correctRate          varchar(255),
   spareTime            int(10),
   primary key (id)
);

alter table adailytalk add constraint FK_Reference_5 foreign key (cid)
      references examineeclass (Id) on delete restrict on update restrict;

alter table chapter add constraint FK_Reference_9 foreign key (subjectId)
      references subject (Id) on delete restrict on update restrict;

alter table checking add constraint FK_Reference_6 foreign key (uid)
      references systemuser (id) on delete restrict on update restrict;

alter table checking add constraint FK_Reference_7 foreign key (cid)
      references examineeclass (Id) on delete restrict on update restrict;

alter table edition add constraint FK_Reference_18 foreign key (did)
      references direction (did) on delete restrict on update restrict;

alter table examinee add constraint FK_Reference_1 foreign key (classId)
      references examineeclass (Id) on delete restrict on update restrict;

alter table labanswer add constraint FK_Reference_2 foreign key (paperId)
      references labpaper (id) on delete restrict on update restrict;

alter table labquestion add constraint FK_Reference_10 foreign key (editionId)
      references edition (id) on delete restrict on update restrict;

alter table labquestion add constraint FK_Reference_3 foreign key (skillCodeId)
      references labquestiontype (id) on delete restrict on update restrict;

alter table pointanswer add constraint FK_Reference_12 foreign key (pid)
      references pointpaper (pid) on delete restrict on update restrict;

alter table pointinfo add constraint FK_Reference_17 foreign key (cid)
      references chapter (cid) on delete restrict on update restrict;

alter table pointpaper add constraint FK_Reference_13 foreign key (sid)
      references subject (Id) on delete restrict on update restrict;

alter table pointpaper add constraint FK_Reference_4 foreign key (cid)
      references examineeclass (Id) on delete restrict on update restrict;

alter table subject add constraint FK_Reference_19 foreign key (editionId)
      references edition (id) on delete restrict on update restrict;

alter table writinganswer add constraint FK_Reference_15 foreign key (paperId)
      references writingpaper (id) on delete restrict on update restrict;

      
      
      select count(*) from examineeclass where eid in (select id from edition where did= 1)