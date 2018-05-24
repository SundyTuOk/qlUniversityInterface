package com.sf.qlinterface.dao;

import org.apache.ibatis.annotations.Param;

import com.sf.db.domain.ApCardSaleInfo;

public interface ApCardSaleInfoDAO {
	public Integer addApCardSaleInfo(ApCardSaleInfo apCardSaleInfo);
	
	public Integer updateState(@Param("state") String state,@Param("apCardSaleInfoID") Integer apCardSaleInfoID);
}
