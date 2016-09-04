package com.yc.webexam.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ModelDriven;
import com.yc.biz.ChapterBiz;
import com.yc.biz.EditionBiz;
import com.yc.biz.SubjectBiz;
import com.yc.po.Chapter;
import com.yc.po.Edition;
import com.yc.po.Subject;
import com.yc.utils.JsonUtil;
import com.yc.vo.ChapterPage;
import com.yc.vo.SubjectPage;

public class ChapterAction extends BaseAction implements ServletResponseAware, ModelDriven<ChapterPage> {
    private static final Logger logger = Logger.getLogger(SubjectAction.class);
    private HttpServletResponse response;

    @Resource(name = "subjectBiz")
    private SubjectBiz subjectBiz;

    @Resource(name = "editionBiz")
    private EditionBiz editionBiz;

    @Resource(name = "chapterBiz")
    private ChapterBiz chapterBiz;

    private String jsonStr;

    private PrintWriter out;

    private ChapterPage chapterPage = new ChapterPage();

    public ChapterPage getChapterPage() {
        return chapterPage;
    }

    HttpServletRequest req = ServletActionContext.getRequest();

    public void showAllChapter() {
        Integer page = Integer.parseInt(req.getParameter("page"));
        Integer rows = Integer.parseInt(req.getParameter("rows"));

        List<Chapter> chapter;
        List<ChapterPage> chapters;
        try {
            chapter = chapterBiz.getChapterList(chapterPage, page, rows);
            Map<String, Object> map = new HashMap<String, Object>();
            chapters = new ArrayList<ChapterPage>();
            if (chapter != null && chapter.size() > 0) {
                for (int i = 0; i < chapter.size(); i++) {
                    ChapterPage chapterPage = new ChapterPage();
                    chapterPage.setId(chapter.get(i).getId());
                    chapterPage.setEditionName(chapter.get(i).getSubject().getEdition().getEditionName());
                    chapterPage.setSemester(chapter.get(i).getSubject().getSemester());
                    chapterPage.setSubjectName(chapter.get(i).getSubject().getSubjectName());
                    chapterPage.setRemark(chapter.get(i).getRemark());
                    chapterPage.setChapterName(chapter.get(i).getChapterName());
                    chapters.add(chapterPage);
                }
                 
                List<Chapter> chapter111 = chapterBiz.getChapterList(chapterPage, null, null);
                int total = chapter111.size();
                map.put("total", total);
                map.put("rows", chapters);
            } else {
                map = new HashMap<String, Object>();
                map.put("total", 0);
                map.put("rows", chapter);
            }

            jsonStr = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd");
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = super.writeJson(1, "出现异常");
        } finally {
            try {
                JsonUtil.jsonOut(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void findChapterBySubject(){
        String jsonStr="";
        try {
            List<Chapter> list=chapterBiz.findChapter(Integer.parseInt(req.getParameter("sid")));
            List<Chapter> list2 = new ArrayList<Chapter>();
            for (Chapter chapter : list) {
                chapter.setSubject(null);
            }
            jsonStr=super.writeJson(0, list);
        } catch (Exception e) {
            jsonStr=super.writeJson(1, "修改失败！");
            logger.error(e);
        }finally{
            try {
                JsonUtil.jsonOut(jsonStr);
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    public void subjectName() throws IOException {
        try {
            String id = req.getParameter("sid");
            String semester = req.getParameter("semester");
            out = response.getWriter();
            List<Subject> subject = (List<Subject>) subjectBiz.getSubjectsById(id,semester);

            Edition edition = new Edition();
            if (subject != null && subject.size() > 0) {
                for (int i = 0; i < subject.size(); i++) {
                    edition.setEditionName(subject.get(i).getEdition().getEditionName());
                    subject.get(i).setChapters(null);
                    subject.get(i).setEdition(edition);
                }
            }
            if (subject != null && subject.size() > 0) { // SerializerFeature.DisableCircularReferenceDetect
                                                         // 不能循环引用
                jsonStr = JSON.toJSONString(subject, SerializerFeature.DisableCircularReferenceDetect);
                out.print(jsonStr);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateChapter() throws IOException {
        int cid = chapterPage.getId();
        String newChapterName = new String(chapterPage.getChapterName().getBytes("iso-8859-1"), "UTF-8");
        String chapterRemark = new String(chapterPage.getRemark().getBytes("iso-8859-1"), "UTF-8");

        Chapter chapter = chapterBiz.findChapterById(cid);
        chapter.setId(cid);
        chapter.setChapterName(newChapterName);
        chapter.setRemark(chapterRemark);

        try {
            int result = chapterBiz.updateChapter(chapter);
            if (result > 0) {
                jsonStr = super.writeJson(0, "修改成功");
            } else {
                jsonStr = super.writeJson(1, "修改失败");
            }
        } catch (Exception e) {
            jsonStr = super.writeJson(1, "修改出现异常：" + e.toString());
        }
        JsonUtil.jsonOut(jsonStr);

    }

    public void deleteChapter() {
        int cid = chapterPage.getId();
        Chapter chapter = chapterBiz.findChapterById(cid);
        int subjectId = chapter.getSubject().getId();
        try {
            int rs = chapterBiz.deleteChapter(cid);
            if (rs > 0) {
                int count = chapterBiz.getChapterCount(subjectId); // 根据科目id获取章节数
                int res = subjectBiz.updateChapterCount(count, subjectId);
                if (res > 0) {
                    jsonStr = super.writeJson(0, null);
                } else {
                    jsonStr = super.writeJson(1, "删除失败");
                }
            } else {
                jsonStr = super.writeJson(1, "删除失败,或因为该章节不能删除");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = super.writeJson(1, "删除出现异常" + e.toString());
        } finally {
            try {
                JsonUtil.jsonOut(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void addChapter() throws IOException {
        int subjectId = Integer.parseInt(req.getParameter("subjectName"));
        String chapterName = req.getParameter("chapterName");
        String remark = req.getParameter("remark");

        //int subjectId = subjectBiz.getSubjectId(subjectName);
        Subject subject = new Subject();
        subject.setId(subjectId);

        Chapter chapter = new Chapter();
        chapter.setSubject(subject);
        chapter.setChapterName(chapterName);
        chapter.setRemark(remark);
        try {
            int result = chapterBiz.addChapter(chapter);

            if (result > 0) {
                int count = chapterBiz.getChapterCount(subjectId); // 根据科目id获取章节数
                int rs = subjectBiz.updateChapterCount(count, subjectId);
                if (rs > 0) {
                    jsonStr = super.writeJson(0, null);
                } else {
                    jsonStr = super.writeJson(1, "添加失败");
                }

            } else {
                jsonStr = super.writeJson(1, "添加失败");
            }
        } catch (Exception e) {
            jsonStr = super.writeJson(1, "添加出现异常，请联系管理员");
            e.printStackTrace();
        }
        JsonUtil.jsonOut(jsonStr);

    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;

    }

    @Override
    public ChapterPage getModel() {
        return chapterPage;
    }
}
