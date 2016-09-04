package com.yc.vo;

import java.io.Serializable;

public class ListClassPage implements Serializable {
		private Integer id;
		private String className;
		private String semester;
		private String createDate;
		private String overDate;
		private String remark;
		private int studentCount;
		private String name;
		
		private String oldname;
		
		private String pwd;
		
		private String examineeNames;
		private String eid;
		private String snumber;
		
		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}

		public String getExamineeNames() {
			return examineeNames;
		}

		public void setExamineeNames(String examineeNames) {
			this.examineeNames = examineeNames;
		}

		public String getOldname() {
			return oldname;
		}
		
		public void setOldname(String oldname) {
			this.oldname = oldname;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
		}
		public String getSemester() {
			return semester;
		}
		public void setSemester(String semester) {
			this.semester = semester;
		}
		public String getCreateDate() {
			return createDate;
		}
		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}
		public String getOverDate() {
			return overDate;
		}
		public void setOverDate(String overDate) {
			this.overDate = overDate;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public int getStudentCount() {
			return studentCount;
		}
		public void setStudentCount(int studentCount) {
			this.studentCount = studentCount;
		}

        public String getEid() {
            return eid;
        }

        public void setEid(String eid) {
            this.eid = eid;
        }

        public String getSnumber() {
            return snumber;
        }

        public void setSnumber(String snumber) {
            this.snumber = snumber;
        }
        
}
