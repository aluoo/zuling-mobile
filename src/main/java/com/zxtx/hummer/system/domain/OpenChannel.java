package com.zxtx.hummer.system.domain;

import java.io.Serializable;
import java.util.Date;

public class OpenChannel implements Serializable {
    private Long id;

    private String operatorId;

    private String operatorName;

    private Long empId;

    private String md5Key;

    private String aesKey;

    private String relatedAppId;

    private String relatedLocation;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key == null ? null : md5Key.trim();
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey == null ? null : aesKey.trim();
    }

    public String getRelatedAppId() {
        return relatedAppId;
    }

    public void setRelatedAppId(String relatedAppId) {
        this.relatedAppId = relatedAppId == null ? null : relatedAppId.trim();
    }

    public String getRelatedLocation() {
        return relatedLocation;
    }

    public void setRelatedLocation(String relatedLocation) {
        this.relatedLocation = relatedLocation == null ? null : relatedLocation.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", empId=").append(empId);
        sb.append(", md5Key=").append(md5Key);
        sb.append(", aesKey=").append(aesKey);
        sb.append(", relatedAppId=").append(relatedAppId);
        sb.append(", relatedLocation=").append(relatedLocation);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OpenChannel other = (OpenChannel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
                && (this.getOperatorName() == null ? other.getOperatorName() == null : this.getOperatorName().equals(other.getOperatorName()))
                && (this.getEmpId() == null ? other.getEmpId() == null : this.getEmpId().equals(other.getEmpId()))
                && (this.getMd5Key() == null ? other.getMd5Key() == null : this.getMd5Key().equals(other.getMd5Key()))
                && (this.getAesKey() == null ? other.getAesKey() == null : this.getAesKey().equals(other.getAesKey()))
                && (this.getRelatedAppId() == null ? other.getRelatedAppId() == null : this.getRelatedAppId().equals(other.getRelatedAppId()))
                && (this.getRelatedLocation() == null ? other.getRelatedLocation() == null : this.getRelatedLocation().equals(other.getRelatedLocation()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getOperatorName() == null) ? 0 : getOperatorName().hashCode());
        result = prime * result + ((getEmpId() == null) ? 0 : getEmpId().hashCode());
        result = prime * result + ((getMd5Key() == null) ? 0 : getMd5Key().hashCode());
        result = prime * result + ((getAesKey() == null) ? 0 : getAesKey().hashCode());
        result = prime * result + ((getRelatedAppId() == null) ? 0 : getRelatedAppId().hashCode());
        result = prime * result + ((getRelatedLocation() == null) ? 0 : getRelatedLocation().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}