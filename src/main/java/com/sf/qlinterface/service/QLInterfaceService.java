package com.sf.qlinterface.service;

import com.sf.db.domain.Ammeter;

public interface  QLInterfaceService {
	
	public Ammeter getAmmeterByConsumerNum(String comsumerNum);
}
