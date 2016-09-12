package com.yc.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.hibernate.annotations.Cache;


import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Range;
@Entity
@Table(name="checkanswer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CheckAnswer implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length=50,nullable=false)
	private String ecid;  //班级
	
	
	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	private Date checkDate;  //评教日期
	
	@Column(length=4000)
	private String answer;    //结果
	@Column(length=400)
	private String remark;   //备注
	@Column()
	private String status; //状态

	
	@ManyToOne
	@JoinColumn(name="sys_id",nullable=false)
	private SystemUser systemUser;
	 
	@ManyToOne
	@JoinColumn(name="exa_id",nullable=false)
	private Examinee examinee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEcid() {
        return ecid;
    }

    public void setEcid(String ecid) {
        this.ecid = ecid;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }

    public Examinee getExaminee() {
        return examinee;
    }

    public void setExaminee(Examinee examinee) {
        this.examinee = examinee;
    }

    @Override
    public String toString() {
        return "CheckAnswer [id=" + id + ", ecid=" + ecid + ", checkDate=" + checkDate + ", answer=" + answer + ", remark=" + remark + ", status=" + status
                + ", systemUser=" + systemUser + ", examinee=" + examinee + "]";
    }
}
