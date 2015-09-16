package com.hivier.model;

import com.google.gson.annotations.Expose;
import com.sf.common.reflection.annotations.pbdb_alias;
import com.sf.common.reflection.annotations.pbdb_ignore;

import java.util.Date;

/*  */
@pbdb_alias("hivier_email_info")
public class HivierEmailInfo {
    @pbdb_ignore
    @Expose
    private Integer id;//remark:;length:10; not null,default:null
    @Expose
    private String email;
    @pbdb_alias("e_name")
    @Expose
    private String eName;
    @pbdb_alias("e_phone")
    @Expose
    private String ePhone;
    @pbdb_alias("i_status")
    private Integer iStatus;//remark:状态 1 新建 2 审核 3 上线 4 下线;length:10; not null,default:1

    @pbdb_alias("create_time")
    @Expose
    private Date createTime;//remark:;length:19; not null,default:null
    @pbdb_ignore
    @pbdb_alias("update_time")
    private Date updateTime;//remark:;length:19; not null,default:CURRENT_TIMESTAMP

    public HivierEmailInfo() {
    }


    @Override
    public String toString() {
        return "HivierEmailInfo{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", eName='" + eName + '\'' +
                ", ePhone='" + ePhone + '\'' +
                ", iStatus=" + iStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getePhone() {
        return ePhone;
    }

    public void setePhone(String ePhone) {
        this.ePhone = ePhone;
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}