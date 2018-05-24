package com.sf.db.domain;

import java.util.Date;

public class ApCardErrorInfo {
      
	private Integer apCardErrorInfoID;
	private Integer apCardSaleInfoID;
	private Integer apCardDZID;
	private String  saleInfoNum;
	private String  errorType;
	private Integer execState;
	private Date    buyTime;
	
	
	public Integer getApCardErrorInfoID() {
		return apCardErrorInfoID;
	}
	public void setApCardErrorInfoID(Integer apCardErrorInfoID) {
		this.apCardErrorInfoID = apCardErrorInfoID;
	}
	public Integer getApCardSaleInfoID() {
		return apCardSaleInfoID;
	}
	public void setApCardSaleInfoID(Integer apCardSaleInfoID) {
		this.apCardSaleInfoID = apCardSaleInfoID;
	}
	public Integer getApCardDZID() {
		return apCardDZID;
	}
	public void setApCardDZID(Integer apCardDZID) {
		this.apCardDZID = apCardDZID;
	}
	public String getSaleInfoNum() {
		return saleInfoNum;
	}
	public void setSaleInfoNum(String saleInfoNum) {
		this.saleInfoNum = saleInfoNum;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public Integer getExecState() {
		return execState;
	}
	public void setExecState(Integer execState) {
		this.execState = execState;
	}
	public Date getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	public String getAmMeterName() {
		return amMeterName;
	}
	public void setAmMeterName(String amMeterName) {
		this.amMeterName = amMeterName;
	}
	private String  amMeterName;


	@Override
	public String toString() {
		return "ApCardErrorInfo [apCardErrorInfoID=" + apCardErrorInfoID
				+ ", apCardSaleInfoID=" + apCardSaleInfoID + ", apCardDZID="
				+ apCardDZID + ", saleInfoNum=" + saleInfoNum + ", errorType="
				+ errorType + ", execState=" + execState + ", buyTime="
				+ buyTime + ", amMeterName=" + amMeterName + "]";
	}
	
}
