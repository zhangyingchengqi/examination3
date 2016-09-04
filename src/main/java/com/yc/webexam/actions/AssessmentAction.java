package com.yc.webexam.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.ModelDriven;
import com.yc.biz.ChapterBiz;
import com.yc.biz.PointInfoBiz;
import com.yc.biz.SubjectBiz;
import com.yc.po.Chapter;
import com.yc.po.PointInfo;
import com.yc.po.Subject;
import com.yc.utils.JsonUtil;
import com.yc.vo.DataGaidModel;
import com.yc.vo.PointInfoModel;
import com.yc.vo.PointPaperModel;

public class AssessmentAction extends BaseAction implements ModelDriven<PointInfoModel>{
	private PointInfoModel pointInfoModel=new PointInfoModel();
			
	private static final Logger logger = Logger.getLogger(AssessmentAction.class);
	
	@Resource(name = "chapterBiz")
    private ChapterBiz chapterBiz;
	private PointInfoBiz pointInfoBiz;
	@Resource(name = "pointInfoBiz")
	public void setPointInfoBiz(PointInfoBiz pointInfoBiz) {
		this.pointInfoBiz = pointInfoBiz;
	}
	
	private SubjectBiz subjectBiz;
	@Resource(name = "subjectBiz")
	public void setSubjectBiz(SubjectBiz subjectBiz) {
		this.subjectBiz = subjectBiz;
	}
	
	public void initPointInfos(){
		String jsonStr="";
		try {
			List<PointInfo> pointInfos=pointInfoBiz.getPointInfo(pointInfoModel.getCid());
			for(int i=0;i<pointInfos.size();i++){
				pointInfos.get(i).setChapter(null);
			}
			jsonStr=super.writeJson(0,pointInfos);
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "查询失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	public void getPointBySid(){
        String jsonStr="";
        try {
            List<Chapter> chapters =  chapterBiz.findChapter(pointInfoModel.getCid());
            //List<PointInfo> pointInfos= new ArrayList<PointInfo>();
            if(chapters.size() > 0 || chapters != null){
                List<PointInfo> pointInfos=pointInfoBiz.findPointAllInfoByCids(chapters);
                
                for(int i=0;i<pointInfos.size();i++){
                    pointInfos.get(i).setChapter(null);
                }
                jsonStr=super.writeJson(0,pointInfos);
            }
        } catch (Exception e) {
            jsonStr=super.writeJson(1, "查询失败！");
            logger.error(e);
        }finally{
            try {
                JsonUtil.jsonOut(jsonStr);
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
	
	public void initPointBySid(){
        
        String jsonStr="";
        try {
            DataGaidModel dgm=new DataGaidModel();
            dgm=pointInfoBiz.getPointInfo(pointInfoModel);
            if(dgm.getRows().size()>0&&dgm.getRows()!=null){
                for(int i=0;i<dgm.getRows().size();i++){
                    ((PointInfo) dgm.getRows().get(i)).setChapter(null);
                }
            }
            jsonStr=JSON.toJSONStringWithDateFormat(dgm, "yyyy-MM-dd");
        } catch (Exception e) {
            DataGaidModel dgm=new DataGaidModel();
            List<PointInfoModel> list=new ArrayList<PointInfoModel>();
            dgm.setRows(list);
            dgm.setTotal((long) 0);
            jsonStr=JSON.toJSONStringWithDateFormat(dgm, "yyyy-MM-dd");
        }finally{
            try {
                JsonUtil.jsonOut(jsonStr);
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }
	
	public void initPointInfo(){
		
		String jsonStr="";
		try {
			DataGaidModel dgm=new DataGaidModel();
			dgm=pointInfoBiz.getPointInfo(pointInfoModel);
			if(dgm.getRows().size()>0&&dgm.getRows()!=null){
				for(int i=0;i<dgm.getRows().size();i++){
					((PointInfo) dgm.getRows().get(i)).setChapter(null);
				}
			}
			jsonStr=JSON.toJSONStringWithDateFormat(dgm, "yyyy-MM-dd");
		} catch (Exception e) {
			DataGaidModel dgm=new DataGaidModel();
			List<PointInfoModel> list=new ArrayList<PointInfoModel>();
			dgm.setRows(list);
			dgm.setTotal((long) 0);
			jsonStr=JSON.toJSONStringWithDateFormat(dgm, "yyyy-MM-dd");
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	public void addPointInfo(){
		String jsonStr="";
		try {
		    Chapter chapter=chapterBiz.findChapterById(pointInfoModel.getCid());
			PointInfo po=new PointInfo();
			po.setPcontent(pointInfoModel.getPcontent());
			po.setChapter(chapter);
			po.setStatus(1);
			pointInfoBiz.addPointInfo(po);
			jsonStr=super.writeJson(0, "添加成功");
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "添加失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
		
	}
	public void delPointInfo(){
		String jsonStr="";
		try {
		    Chapter chapter=chapterBiz.findChapterById(pointInfoModel.getCid());
			PointInfo po=new PointInfo();
			po.setPid(pointInfoModel.getPid());
			po.setChapter(chapter);
			pointInfoBiz.delPointInfoById(po);
			jsonStr=super.writeJson(0, "删除成功");
		} catch (Exception e) {
			jsonStr=super.writeJson(1, "删除失败！");
			logger.error(e);
		}finally{
			try {
				JsonUtil.jsonOut(jsonStr);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	public void updatePointInfo(){
		String jsonStr="";
		try {
		    Chapter chapter=chapterBiz.findChapterById(pointInfoModel.getCid());
			PointInfo po=new PointInfo();
			po.setPid(pointInfoModel.getPid());
			po.setPcontent(pointInfoModel.getPcontent());
			po.setChapter(chapter);
			po.setStatus(1);
			pointInfoBiz.updatePointInfo(po);
			jsonStr=super.writeJson(0, "修改成功");
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
	@Override
	public PointInfoModel getModel() {
		return this.pointInfoModel;
	}
	
}
