package com.skt.ehs.mbs.data;

import java.io.Serializable;

/**
 * 지자기 성분 분해 값을 저장해둔다.
 * @author YiSuHwan
 *
 */
public class DataMagnetic implements Serializable {

	// vetical의 값
	private Integer bv;
	// horizental의 값 (vertical 외 2가지의 힘의 함)
	private Integer bh;
	
	public Integer getBv() {
		return bv;
	}
	public void setBv(Integer bv) {
		this.bv = bv;
	}
	public Integer getBh() {
		return bh;
	}
	public void setBh(Integer bh) {
		this.bh = bh;
	}
}
