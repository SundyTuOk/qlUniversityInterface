package com.sf.qlinterface.dao;
import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.sf.db.domain.ZAmDatas;

public interface ZAmDatasDAO {
	public ZAmDatas getZAmDatasByValueTime(@Param("zAMTableName") String zAMTableName,@Param("date")Date date);
	
	public ZAmDatas getLastZamDatas(@Param("zAMTableName") String zAMTableName);
}
