package com.sf.db.domain;

import java.util.Date;

public class ApCardDZ {

	  private  Integer apCardDZID;
	  private  String  apCardDZTime;
	  private  float   cardMoney;
	  private  float   apMoney;
	  private  float   cMoney;
	  private  Integer cardCountTimes;
	  private  Integer cardTimes;
	  private  Integer aPTimes;
	  private  Integer eroorTimes;
	  private  Integer execTimes;
	  private  Integer usersID;
	  private  Date    insertTime;
	  
	  
	public Integer getaPCardDZID() {
		return apCardDZID;
	}
	public void setaPCardDZID(Integer apCardDZID) {
		this.apCardDZID = apCardDZID;
	}
	public String getaPCardDZTime() {
		return apCardDZTime;
	}
	public void setaPCardDZTime(String apCardDZTime) {
		this.apCardDZTime = apCardDZTime;
	}
	public float getCardMoney() {
		return cardMoney;
	}
	public void setCardMoney(float cardMoney) {
		this.cardMoney = cardMoney;
	}
	public float getApMoney() {
		return apMoney;
	}
	public void setApMoney(float apMoney) {
		this.apMoney = apMoney;
	}
	public float getcMoney() {
		return cMoney;
	}
	public void setcMoney(float cMoney) {
		this.cMoney = cMoney;
	}
	public Integer getCardCountTimes() {
		return cardCountTimes;
	}
	public void setCardCountTimes(Integer cardCountTimes) {
		this.cardCountTimes = cardCountTimes;
	}
	public Integer getCardTimes() {
		return cardTimes;
	}
	public void setCardTimes(Integer cardTimes) {
		this.cardTimes = cardTimes;
	}
	public Integer getaPTimes() {
		return aPTimes;
	}
	public void setaPTimes(Integer aPTimes) {
		this.aPTimes = aPTimes;
	}
	public Integer getEroorTimes() {
		return eroorTimes;
	}
	public void setEroorTimes(Integer eroorTimes) {
		this.eroorTimes = eroorTimes;
	}
	public Integer getExecTimes() {
		return execTimes;
	}
	public void setExecTimes(Integer execTimes) {
		this.execTimes = execTimes;
	}
	public Integer getUsersID() {
		return usersID;
	}
	public void setUsersID(Integer usersID) {
		this.usersID = usersID;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	
	@Override
	public String toString() {
		return "ApCardDZ [apCardDZID=" + apCardDZID + ", apCardDZTime="
				+ apCardDZTime + ", cardMoney=" + cardMoney + ", apMoney="
				+ apMoney + ", cMoney=" + cMoney + ", cardCountTimes="
				+ cardCountTimes + ", cardTimes=" + cardTimes + ", aPTimes="
				+ aPTimes + ", eroorTimes=" + eroorTimes + ", execTimes="
				+ execTimes + ", usersID=" + usersID + ", insertTime="
				+ insertTime + "]";
	}
	  
}
