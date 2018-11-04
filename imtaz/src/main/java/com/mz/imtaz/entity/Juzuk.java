package com.mz.imtaz.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
public class Juzuk implements Serializable {

	private Integer juz;
	private Integer page;
	private Integer line;

	public Juzuk() {}

	public Juzuk(Integer juz, Integer page, Integer line) {
		this.juz = juz;
		this.page = page;
		this.line = line;
	}

	@Override
	public String toString() {
		return "Juz " + (juz != null ? juz : 0) + " Muka " + (page != null ? page : 0) + " Baris " + (line != null ? line : 0);
	}
}
