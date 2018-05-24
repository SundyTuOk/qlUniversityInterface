package com.sf.qlinterface.dao;

import com.sf.db.domain.AmmeterAPDatas;

public interface AmmeterAPDatasDAO {
	/**
	 * 获取最新的apdatas 可以得到剩余金额
	 * @param ammeterId
	 * @return
	 */
	public AmmeterAPDatas getLastAmmeterAPDatasByAmmeterId(Integer ammeterId);
}
