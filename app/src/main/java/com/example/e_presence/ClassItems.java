package com.example.e_presence;

public class ClassItems {
    private String className;
     private String subjectName;
     private long cid;

    public ClassItems(String className, String subjectName, long cid) {
        this.className = className;
        this.subjectName = subjectName;
        this.cid = cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getCid() {
        return cid;
    }



    public String getClassName() {
        return className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
