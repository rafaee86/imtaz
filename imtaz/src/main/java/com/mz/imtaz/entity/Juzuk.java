package com.mz.imtaz.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

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
	
	public static Juzuk substract(Juzuk source, Juzuk other) {
		Juzuk juzuk = new Juzuk();
		if(source.juz != null &&  other.getJuz() != null && source.juz > other.getJuz()) {
			juzuk.setJuz(source.juz - other.getJuz());
		}else {
			juzuk.setJuz(0);
		}
		
		if(source.page != null &&  other.getPage() != null && source.page > other.getPage()) {
			juzuk.setPage(source.page - other.getPage());
		}else {
			juzuk.setPage(0);
		}
		
		if(source.line != null &&  other.getLine() != null && source.line > other.getLine()) {
			juzuk.setLine(source.line - other.getLine());
		}else {
			juzuk.setLine(0);
		}
		
		return juzuk;
	}
}
