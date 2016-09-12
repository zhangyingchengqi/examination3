package com.yc.po;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;



import javax.persistence.Column;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.persistence.Table;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.JoinColumn;

@Entity
@Table(name="Examinee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Examinee implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id@GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	private Integer id; 
	
	@ManyToOne	
    @JoinColumn(name="classId")
	private ExamineeClass examineeClass;
	
	@Column(length=50)
	private String name;

	@Column(length=64,nullable=false)
	private String password ;//学生密码

    @Column(length=50)
    private String realname ;//真实名
    
    @Column
    private Integer age ;  //年龄
    @Column
    private Integer sex ;
    @Column
    private String idcard ;
    @Column(length=50)
    private String wechat ;
    @Column(length=50)
    private String qq ;
    @Column(length=50)
    private String phone ;
    @Column(length=100)
    private String address ;
    @Column(length=50)
    private String school ;
    @Column(length=50)
    private String grade ;
    @Column(length=50)
    private String Professional ;
    @Column(length=50)
    private String bedroom ;

	
	 @OneToMany(cascade=CascadeType.ALL)
	    @JoinColumns ({
	        @JoinColumn(name="name", referencedColumnName = "name"),
	        @JoinColumn(name="classId", referencedColumnName = "classId")
	 })
	 private Set<PointAnswer> pointAnswers= new HashSet<PointAnswer>();


	public Set<PointAnswer> getPointAnswers() {
		return pointAnswers;
	}


	public void setPointAnswers(Set<PointAnswer> pointAnswers) {
		this.pointAnswers = pointAnswers;
	}


	public ExamineeClass getExamineeClass() {
		return examineeClass;
	}


	public void setExamineeClass(ExamineeClass examineeClass) {
		this.examineeClass = examineeClass;
	}

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((examineeClass == null) ? 0 : examineeClass.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((pointAnswers == null) ? 0 : pointAnswers.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Examinee other = (Examinee) obj;
		if (examineeClass == null) {
			if (other.examineeClass != null)
				return false;
		} else if (!examineeClass.equals(other.examineeClass))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (pointAnswers == null) {
			if (other.pointAnswers != null)
				return false;
		} else if (!pointAnswers.equals(other.pointAnswers))
			return false;
		return true;
	}


	public Examinee() {
		super();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getSchool() {
        return school;
    }


    public void setSchool(String school) {
        this.school = school;
    }


    public String getGrade() {
        return grade;
    }


    public void setGrade(String grade) {
        this.grade = grade;
    }


    public String getProfessional() {
        return Professional;
    }


    public void setProfessional(String professional) {
        Professional = professional;
    }


    public String getBedroom() {
        return bedroom;
    }


    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }


    @Override
    public String toString() {
        return "Examinee [id=" + id + ", examineeClass=" + examineeClass + ", name=" + name + ", password=" + password + ", realname=" + realname + ", age="
                + age + ", sex=" + sex + ", idcard=" + idcard + ", wechat=" + wechat + ", qq=" + qq + ", phone=" + phone + ", address=" + address + ", school="
                + school + ", grade=" + grade + ", Professional=" + Professional + ", bedroom=" + bedroom + ", pointAnswers=" + pointAnswers + "]";
    }

}

