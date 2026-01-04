package com.zxtx.hummer.system.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 注意：本内容仅限于JUST有限公司内部传阅，禁止外泄以及用于其他的商业目
 * @Title: ProjectsDO.java
 * @Package com.just.comm.project.domain
 * @Description:
 * @author: wangding
 * @date: 2018年6月26日 下午1:49:50
 */
public class ProjectsDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer projectId;
    //项目标识
    private Integer projectCode;
    //项目名称(1牦牛出行2牦牛车主3牦牛司机)
    private String projectName;
    //设备（1.Android/2.iOS）
    private Integer device;
    //Build号
    private Integer buildCode;
    //版本号
    private String versionCode;
    //上线时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;
    //状态（1未开启2当前版本3已过期）
    private Integer status;
    //下载地址
    private String downloadUrl;
    //更新说明
    private String readme;
    //是否强制更新（1是2否）
    private Integer forcedUpdating;
    //创建时间
    private Date creatTime;
    //创建人
    private String creator;
    private String idSuffix;
    private String userType;

    public String getIdSuffix() {
        return idSuffix;
    }

    public void setIdSuffix(String idSuffix) {
        this.idSuffix = idSuffix;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * 设置：主键
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取：主键
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置：项目标识
     */
    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 获取：项目标识
     */
    public Integer getProjectCode() {
        return projectCode;
    }

    /**
     * 设置：项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 获取：项目名称
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * 设置：设备（1.Android/2.iOS）
     */
    public void setDevice(Integer device) {
        this.device = device;
    }

    /**
     * 获取：设备（1.Android/2.iOS）
     */
    public Integer getDevice() {
        return device;
    }

    /**
     * 设置：Build号
     */
    public void setBuildCode(Integer buildCode) {
        this.buildCode = buildCode;
    }

    /**
     * 获取：Build号
     */
    public Integer getBuildCode() {
        return buildCode;
    }

    /**
     * 设置：版本号
     */
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 获取：版本号
     */
    public String getVersionCode() {
        return versionCode;
    }

    /**
     * 设置：上线时间
     */
    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    /**
     * 获取：上线时间
     */
    public Date getOnlineTime() {
        return onlineTime;
    }

    /**
     * 设置：状态（1未开启2当前版本3已过期）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取：状态（1未开启2当前版本3已过期）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置：下载地址
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * 获取：下载地址
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * 设置：更新说明
     */
    public void setReadme(String readme) {
        this.readme = readme;
    }

    /**
     * 获取：更新说明
     */
    public String getReadme() {
        return readme;
    }

    /**
     * 设置：是否强制更新（1是2否）
     */
    public void setForcedUpdating(Integer forcedUpdating) {
        this.forcedUpdating = forcedUpdating;
    }

    /**
     * 获取：是否强制更新（1是2否）
     */
    public Integer getForcedUpdating() {
        return forcedUpdating;
    }

    /**
     * 设置：创建时间
     */
    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreatTime() {
        return creatTime;
    }

    /**
     * 设置：创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取：创建人
     */
    public String getCreator() {
        return creator;
    }
}
