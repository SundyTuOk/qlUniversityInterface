package com.sf.qlinterface.dao;

import com.sf.db.domain.ApSaleInfo;

public interface ApSaleInfoDAO {
	public ApSaleInfo getLastApSaleInfo();
	
	public int addApSaleInfo(ApSaleInfo apSaleInfo);
	
	public ApSaleInfo getApSaleInfoByOrderNo(String orderNo);
}
