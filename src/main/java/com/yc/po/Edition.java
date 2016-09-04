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
import org.springframework.context.annotation.Lazy;

@Entity
@Table(name="Edition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Edition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(length=50,nullable = false)
	private String editionName;//版本名

    @Column(nullable = false)
    private Integer semesternum;//学期数
	
	@Column(nullable = false)
	private Integer currentUse;//使用状态  0.未使用  1.正在使用
	
	
	@ManyToOne
    @JoinColumn(name="did",nullable=false)
    private Direction direction;  //方向
	
	@OneToMany(mappedBy="edition",fetch=FetchType.EAGER)
	private Set<Subject> subjects;
	@OneToMany(mappedBy="edition",fetch=FetchType.EAGER )
	private Set<LabQuestion> labQuestions;
	 
	public Integer getCurrentUse() {
		return currentUse;
	}

	public void setCurrentUse(Integer currentUse) {
		this.currentUse = currentUse;
	}

	public Set<LabQuestion> getLabQuestions() {
		return labQuestions;
	}

	public void setLabQuestions(Set<LabQuestion> labQuestions) {
		this.labQuestions = labQuestions;
	}

	public Set<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEditionName() {
		return editionName;
	}

	public void setEditionName(String editionName) {
		this.editionName = editionName;
	}

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Integer getSemesternum() {
        return semesternum;
    }

    public void setSemesternum(Integer semesternum) {
        this.semesternum = semesternum;
    }
	
}
