package com.sf.db.domain;

import java.util.Date;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User{

	private Integer userID;
	private String userNickName;
	private Integer areaId;
	private String  userNum;
	private String usersLoginName;
	private String userPassword;
	private Integer userGender;
	private String userBirth;
	private String userPhoto;
	private Integer userDepartment;
	private Integer userPhone;
	private Integer userPhone1;
	private Integer userPhone2;
	private String userHomeAddress;
	private Date userLasttime;
	private String userRegTime;
	private Integer userStateId;
	private String userRemark;
	private Integer onlineRE;
	private String IP;
	private Integer isAlarm;
	private String urlHost;
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getUserNum() {
		return userNum;
	}
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
	public String getUsersLoginName() {
		return usersLoginName;
	}
	public void setUsersLoginName(String usersLoginName) {
		this.usersLoginName = usersLoginName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public Integer getUserGender() {
		return userGender;
	}
	public void setUserGender(Integer userGender) {
		this.userGender = userGender;
	}
	public String getUserBirth() {
		return userBirth;
	}
	public void setUserBirth(String userBirth) {
		this.userBirth = userBirth;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public Integer getUserDepartment() {
		return userDepartment;
	}
	public void setUserDepartment(Integer userDepartment) {
		this.userDepartment = userDepartment;
	}
	public Integer getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(Integer userPhone) {
		this.userPhone = userPhone;
	}
	public Integer getUserPhone1() {
		return userPhone1;
	}
	public void setUserPhone1(Integer userPhone1) {
		this.userPhone1 = userPhone1;
	}
	public Integer getUserPhone2() {
		return userPhone2;
	}
	public void setUserPhone2(Integer userPhone2) {
		this.userPhone2 = userPhone2;
	}
	public String getUserHomeAddress() {
		return userHomeAddress;
	}
	public void setUserHomeAddress(String userHomeAddress) {
		this.userHomeAddress = userHomeAddress;
	}
	public Date getUserLasttime() {
		return userLasttime;
	}
	public void setUserLasttime(Date userLasttime) {
		this.userLasttime = userLasttime;
	}
	public String getUserRegTime() {
		return userRegTime;
	}
	public void setUserRegTime(String userRegTime) {
		this.userRegTime = userRegTime;
	}
	public Integer getUserStateId() {
		return userStateId;
	}
	public void setUserStateId(Integer userStateId) {
		this.userStateId = userStateId;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public Integer getOnlineRE() {
		return onlineRE;
	}
	public void setOnlineRE(Integer onlineRE) {
		this.onlineRE = onlineRE;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public Integer getIsAlarm() {
		return isAlarm;
	}
	public void setIsAlarm(Integer isAlarm) {
		this.isAlarm = isAlarm;
	}
	public String getUrlHost() {
		return urlHost;
	}
	public void setUrlHost(String urlHost) {
		this.urlHost = urlHost;
	}
}