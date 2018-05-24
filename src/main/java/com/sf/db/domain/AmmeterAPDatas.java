package com.sf.db.domain;

import java.util.Date;

public class AmmeterAPDatas {
           
	private Integer ammeterID;
	private Date    valueTime;
	private Float syMoney;
	private Float tzMoney;
	private Float syValue;
	private Float tzValue;
	private Integer thisCircleVol;
	private Integer lastCircleVol;
	
	
	public Integer getAmmeterID() {
		return ammeterID;
	}
	public void setAmmeterID(Integer ammeterID) {
		this.ammeterID = ammeterID;
	}
	public Date getValueTime() {
		return valueTime;
	}
	public void setValueTime(Date valueTime) {
		this.valueTime = valueTime;
	}
	public Float getSyMoney() {
		return syMoney;
	}
	public void setSyMoney(Float syMoney) {
		this.syMoney = syMoney;
	}
	public Float getTzMoney() {
		return tzMoney;
	}
	public void setTzMoney(Float tzMoney) {
		this.tzMoney = tzMoney;
	}
	public Float getSyValue() {
		return syValue;
	}
	public void setSyValue(Float syValue) {
		this.syValue = syValue;
	}
	public Float getTzValue() {
		return tzValue;
	}
	public void setTzValue(Float tzValue) {
		this.tzValue = tzValue;
	}
	public Integer getThisCircleVol() {
		return thisCircleVol;
	}
	public void setThisCircleVol(Integer thisCircleVol) {
		this.thisCircleVol = thisCircleVol;
	}
	public Integer getLastCircleVol() {
		return lastCircleVol;
	}
	public void setLastCircleVol(Integer lastCircleVol) {
		this.lastCircleVol = lastCircleVol;
	}
	
    
}
