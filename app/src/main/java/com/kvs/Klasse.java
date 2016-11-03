package com.kvs;

/**
 * Modellierung der Klasse
 */

public class Klasse {

	private int kid, fid;
	private String klassename, fachname;
	
	public Klasse(int kid, String klassename, int fid, String fachname) {
		this.kid = kid;
		this.klassename = klassename;
		this.fid = fid;
		this.fachname = fachname;
	}

	
	public String getKlasseame() {
		return klassename;
	}
	
	public int getKid() {
		return kid;
	}

	public String getFachname() {
		return fachname;
	}

	public int getFid() {
		return fid;
	}
	
	@Override
	public String toString() {
		return klassename + " - " + fachname;
	}
		
}
