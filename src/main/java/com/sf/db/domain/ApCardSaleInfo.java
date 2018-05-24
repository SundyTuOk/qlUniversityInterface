package com.sf.db.domain;

import java.util.Date;

public class ApCardSaleInfo {
   
	  private Integer apCardSaleInfoID;
	  private Integer apCardDZID;
	  private Date    buyTime;
	  private String  posNum;
	  private String  cardManNum;
	  private String  cardManName;
	  private String  studentNum;
	  private float   theMoney;
	  private float   yMoney;
	  private Integer userTimes;
	  private String  state;
	  private String  remark;
	  private String  saleInfoNum;
	  
	  
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
	public Date getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}
	public String getPosNum() {
		return posNum;
	}
	public void setPosNum(String posNum) {
		this.posNum = posNum;
	}
	public String getCardManNum() {
		return cardManNum;
	}
	public void setCardManNum(String cardManNum) {
		this.cardManNum = cardManNum;
	}
	public String getCardManName() {
		return cardManName;
	}
	public void setCardManName(String cardManName) {
		this.cardManName = cardManName;
	}
	public String getStudentNum() {
		return studentNum;
	}
	public void setStudentNum(String studentNum) {
		this.studentNum = studentNum;
	}
	public float getTheMoney() {
		return theMoney;
	}
	public void setTheMoney(float theMoney) {
		this.theMoney = theMoney;
	}
	public float getyMoney() {
		return yMoney;
	}
	public void setyMoney(float yMoney) {
		this.yMoney = yMoney;
	}
	public Integer getUserTimes() {
		return userTimes;
	}
	public void setUserTimes(Integer userTimes) {
		this.userTimes = userTimes;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSaleInfoNum() {
		return saleInfoNum;
	}
	public void setSaleInfoNum(String saleInfoNum) {
		this.saleInfoNum = saleInfoNum;
	}
	@Override
	public String toString() {
		return "ApCardSaleInfo [apCardSaleInfoID=" + apCardSaleInfoID
				+ ", apCardDZID=" + apCardDZID + ", buyTime=" + buyTime
				+ ", posNum=" + posNum + ", cardManNum=" + cardManNum
				+ ", cardManName=" + cardManName + ", studentNum=" + studentNum
				+ ", theMoney=" + theMoney + ", yMoney=" + yMoney
				+ ", userTimes=" + userTimes + ", state=" + state + ", remark="
				+ remark + ", saleInfoNum=" + saleInfoNum + "]";
	}
	  
}
