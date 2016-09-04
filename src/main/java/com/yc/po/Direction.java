package com.yc.po;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="Direction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Direction implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer did;
	@Column(length=50,nullable = false)
	private String dname;
    @Column(length=50,nullable = false)
    private String remark;
    @Column(length=50,nullable = false)
    private String classname;
	
	@Column(nullable = false)
	private Integer currentUse;//使用状态  0.未使用  1.正在使用
	
	
	
    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCurrentUse() {
        return currentUse;
    }

    public void setCurrentUse(Integer currentUse) {
        this.currentUse = currentUse;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return "Direction [did=" + did + ", dname=" + dname + ", remark=" + remark + ", classname=" + classname + ", currentUse=" + currentUse + "]";
    }
}
