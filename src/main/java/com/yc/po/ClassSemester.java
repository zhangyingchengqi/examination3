package com.yc.po;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Table(name="ClassSemester")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassSemester implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id ;//考试班级信息表
	@Column(length=50,nullable = false)
	private String semester ;//学期
	@Column(length=50)
	private String starttime;//开始时间
	@Column(length=50)
	private String endtime ;//结束时间
	@Column(length=255)
	private String remark;//标记 
	
	@ManyToOne
    @JoinColumn(name="eid",nullable=false)
    private ExamineeClass examineeClass;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ExamineeClass getExamineeClass() {
        return examineeClass;
    }

    public void setExamineeClass(ExamineeClass examineeClass) {
        this.examineeClass = examineeClass;
    }

    @Override
    public String toString() {
        return "ClassSemester [id=" + id + ", semester=" + semester + ", starttime=" + starttime + ", endtime=" + endtime + ", remark=" + remark
                + ", examineeClass=" + examineeClass + "]";
    }
}
