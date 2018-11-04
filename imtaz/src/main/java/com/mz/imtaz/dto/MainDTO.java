package com.mz.imtaz.dto;

public interface MainDTO<ORIG> {

	void pull(ORIG t);
	
	ORIG push(ORIG orig);
	
}
