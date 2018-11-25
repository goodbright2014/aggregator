package com.skt.ehs.mbs.data;

import java.io.Serializable;

/**
 * AP의 MAC과 RSSI의 값을 저장하는 DataClass
 * AP에서 수집된 Tag의 정보 값을 담게 된다. 
 */
public class DataAP implements Serializable{

	private String apID;
	private int rssi;
	
	
	public String getApID() {
		return apID;
	}
	public void setApID(String apMac) {
		this.apID = apMac;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	
}
