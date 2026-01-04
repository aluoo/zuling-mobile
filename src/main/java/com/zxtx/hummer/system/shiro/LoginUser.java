package com.zxtx.hummer.system.shiro;

import java.io.Serializable;

public class LoginUser implements Serializable {

    private static final long serialVersionUID = -2073723567322007761L;

    private Long userId;

    private String name;

    private String userName;

    // 密码
    private String password;
    //图片ID
    private Long picId;

    private String mobile;

    private Long employeeId;

    private String employeeName;

    private Byte employeeStatus;

    private String mobileNumber;

    private int employeeType;

    private Long deptId;

    private String deptCode;

    private Long companyId;

    private String channel;

    private String employeeAncestors;

    private Integer employeeRoleType;

    private String companyName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPicId() {
        return picId;
    }

    public void setPicId(Long picId) {
        this.picId = picId;
    }

    public Byte getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Byte employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", picId=" + picId +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeStatus=" + employeeStatus +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", employeeType=" + employeeType +
                ", deptId=" + deptId +
                ", deptCode='" + deptCode + '\'' +
                ", companyId=" + companyId +
                ", channel='" + channel + '\'' +
                '}';
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmployeeAncestors() {
        return employeeAncestors;
    }

    public void setEmployeeAncestors(String employeeAncestors) {
        this.employeeAncestors = employeeAncestors;
    }

    public Integer getEmployeeRoleType() {
        return employeeRoleType;
    }

    public void setEmployeeRoleType(Integer employeeRoleType) {
        this.employeeRoleType = employeeRoleType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}