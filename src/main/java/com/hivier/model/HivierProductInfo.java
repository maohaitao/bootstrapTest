package com.hivier.model;

import com.google.gson.annotations.Expose;
import com.sf.common.reflection.annotations.pbdb_alias;
import com.sf.common.reflection.annotations.pbdb_ignore;

import java.util.Date;

/*  */
@pbdb_alias("hivier_product_info")
public class HivierProductInfo {
    @pbdb_ignore
    @Expose
    private Integer id;//remark:;length:10; not null,default:null
    @Expose
    private String icon;//remark:原始图片;length:255
    @pbdb_alias("p_name")
    @Expose
    private String pName;//remark:'商品名称';length:255; not null,default:null
    @pbdb_alias("p_url")
    @Expose
    private String pUrl;//remark:地址;length:255; not null,default:null
    @pbdb_alias("i_status")
    private Integer iStatus;//remark:状态 1 新建 2 审核 3 上线 4 下线;length:10; not null,default:1
    @Expose
    private double price;//remark:价格,1tydia;length:10; not null,default:1
    private String other;//remark:其他信息;length:255
    @pbdb_alias("create_time")
    @Expose
    private Date createTime;//remark:;length:19; not null,default:null
    @pbdb_ignore
    @pbdb_alias("update_time")
    private Date updateTime;//remark:;length:19; not null,default:CURRENT_TIMESTAMP

    public HivierProductInfo() {
    }

    @Override
    public String toString() {
        return "DiaryImageInfo{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", pName='" + pName + '\'' +
                ", pUrl='" + pUrl + '\'' +
                ", iStatus=" + iStatus +
                ", price=" + price +
                ", other='" + other + '\'' +
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public Integer getiStatus() {
        return iStatus;
    }

    public void setiStatus(Integer iStatus) {
        this.iStatus = iStatus;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
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