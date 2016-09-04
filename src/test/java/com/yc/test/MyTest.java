package com.yc.test;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yc.biz.ExamineeBiz;
import com.yc.biz.ExamineeClassBiz;
import com.yc.biz.WritingPaperBiz;
import com.yc.biz.WritingQuestionBiz;
import com.yc.po.Examinee;
import com.yc.po.WritingPaper;

public class MyTest {
//	@Test
//	public void testA() {
//		System.out.println("test a method ");
//	}
//
//	@Test
//	public void testB() {
//		System.out.println("test b method ");
//	}
//	
	@Ignore
	//@Test
	public void test1fpc(){
		ApplicationContext ac=new ClassPathXmlApplicationContext(new String[]{"applicationContext-actions.xml","applicationContext-dao.xml"}   );
//		ADailyTalkBiz eb=(ADailyTalkBiz) ac.getBean("aDailyTalkBiz");
//		System.out.println(eb.findADailyTalkBypages(6,"付鹏程",0,5));
//		ExamineeBiz  eb=(ExamineeBiz)ac.getBean("examineeBiz");
//		System.out.println(eb.addExaminee("邓望", "a",9));
		
//		Session session = ((SessionFactory)ac.getBean("sessionFactory")).openSession();
//		System.out.println(session.isConnected());
//		System.out.println(session.isOpen());
//		Query query = session.createQuery("from Examinee e where e.examineeClass.className='YC_20'");
//		System.out.println(query);
//		List<Examinee> es = query.list();
//		System.out.println(es);
		ExamineeClassBiz eb=(ExamineeClassBiz)ac.getBean("examineeClassBiz");
		System.out.println(eb.searchAllExamineeClassName());	
//		
	}
	
	
	@Test
	public  void testWriting(){
		ApplicationContext ac=new ClassPathXmlApplicationContext(new String[]{"applicationContext-actions.xml","applicationContext-dao.xml"}   );
		WritingPaperBiz eb=(WritingPaperBiz)ac.getBean("writingPaperBiz");
		WritingPaper wp=new WritingPaper();
		wp.setId("1S110220160709100054");
		wp.setQuestionId("535,675,");
		eb.updateWritingPaperById(wp);
		//WritingQuestionBiz eb=(WritingQuestionBiz)ac.getBean("writingQuestionBiz");
		//System.out.println(eb.getAnswers("535,675,"));
		
	}

}
