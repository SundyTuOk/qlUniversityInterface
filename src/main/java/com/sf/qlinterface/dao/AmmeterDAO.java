package com.sf.qlinterface.dao;

import com.sf.db.domain.Ammeter;

public interface AmmeterDAO {
	public Ammeter getAmmeterById(int ammeterID);
	
	public Ammeter getAmmeterByConsumerNum(String consumerNum);
}
