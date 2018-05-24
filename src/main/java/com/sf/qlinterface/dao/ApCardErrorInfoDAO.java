package com.sf.qlinterface.dao;

import org.apache.ibatis.annotations.Param;

import com.sf.db.domain.ApCardErrorInfo;

public interface ApCardErrorInfoDAO {
	public int addApCardErrorInfo(ApCardErrorInfo apCardErrorInfo);
	
	public int updateExecState(@Param(value = "execState")Integer execState,@Param(value = "apCardErrorInfoID")Integer apCardErrorInfoID);
}
