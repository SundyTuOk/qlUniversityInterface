package com.sf.qlinterface.dao;

import org.apache.ibatis.annotations.Param;

import com.sf.db.domain.ApCardDZ;


public interface ApCardDZDAO {

	public Integer addApCardDZ(ApCardDZ apCardDZ);
	
//	public Integer updateErrorTimes(@Param("eroorTimes") Integer eroorTimes,@Param("apCardDZID") Integer apCardDZID);
	
	public Integer updateApMoney(@Param("apMoney") Float apMoney,@Param("apCardDZID") Integer apCardDZID);
	
	public Integer updateCMoney(@Param("apCardDZID") Integer apCardDZID);

	public Integer updateApTimes(@Param("apTimes") Integer apTimes,@Param("apCardDZID") Integer apCardDZID);
	
	public Integer updateErrorTimes(@Param("apCardDZID") Integer apCardDZID);
}
