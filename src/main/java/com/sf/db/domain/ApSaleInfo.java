package com.sf.db.domain;

import java.util.Date;

public class ApSaleInfo {
     
	private  String  saleInfoNum;
	private  Integer ammeterID;
	private  String  ammeterName;
	private  String  ammeter_485Address;
	private  Integer times;
	private  Integer kind;
	private  Float qSYValue;
	private  Float hSYValue;
	private  Integer usersId;
	private  Date    buyTime;
	private  Date    sendTime;
	private  Integer status;
	private  Float price;
	private  Float thegross;
	private  Float themoney; 
	private  String  studentID;
	private  String  orderNo;
	private  String  sign;
	private  String  usersName;
	private  Integer repeatTime;
	private  Integer saleType;
	private  Integer existInvoice;
	public String getSaleInfoNum() {
		return saleInfoNum;
	}
	public void setSaleInfoNum(String saleInfoNum) {
		this.saleInfoNum = saleInfoNum;
	}
	public Integer getAmmeterID() {
		return ammeterID;
	}
	public void setAmmeterID(Integer ammeterID) {
		this.ammeterID = ammeterID;
	}
	public String getAmmeterName() {
		return ammeterName;
	}
	public void setAmmeterName(String ammeterName) {
		this.ammeterName = ammeterName;
	}
	public String getAmmeter_485Address() {
		return ammeter_485Address;
	}
	public void setAmmeter_485Address(String ammeter_485Address) {
		this.ammeter_485Address = ammeter_485Address;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Integer getKind() {
		return kind;
	}
	public void setKind(Integer kind) {
		this.kind = kind;
	}
	public Float getqSYValue() {
		return qSYValue;
	}
	public void setqSYValue(Float qSYValue) {
		this.qSYValue = qSYValue;
	}
	public Float gethSYValue() {
		return hSYValue;
	}
	public void sethSYValue(Float hSYValue) {
		this.hSYValue = hSYValue;
	}
	public Integer getUsersId() {
		return usersId;
	}
	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}
	public Date getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getThegross() {
		return thegross;
	}
	public void setThegross(Float thegross) {
		this.thegross = thegross;
	}
	public Float getThemoney() {
		return themoney;
	}
	public void setThemoney(Float themoney) {
		this.themoney = themoney;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getUsersName() {
		return usersName;
	}
	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}
	public Integer getRepeatTime() {
		return repeatTime;
	}
	public void setRepeatTime(Integer repeatTime) {
		this.repeatTime = repeatTime;
	}
	public Integer getSaleType() {
		return saleType;
	}
	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}
	public Integer getExistInvoice() {
		return existInvoice;
	}
	public void setExistInvoice(Integer existInvoice) {
		this.existInvoice = existInvoice;
	}
	@Override
	public String toString() {
		return "ApSaleInfo [saleInfoNum=" + saleInfoNum + ", ammeterID="
				+ ammeterID + ", ammeterName=" + ammeterName
				+ ", ammeter_485Address=" + ammeter_485Address + ", times="
				+ times + ", kind=" + kind + ", qSYValue=" + qSYValue
				+ ", hSYValue=" + hSYValue + ", usersId=" + usersId
				+ ", buyTime=" + buyTime + ", sendTime=" + sendTime
				+ ", status=" + status + ", price=" + price + ", thegross="
				+ thegross + ", themoney=" + themoney + ", studentID="
				+ studentID + ", orderNo=" + orderNo + ", sign=" + sign
				+ ", usersName=" + usersName + ", repeatTime=" + repeatTime
				+ ", saleType=" + saleType + ", existInvoice=" + existInvoice
				+ "]";
	}
	
	
}
