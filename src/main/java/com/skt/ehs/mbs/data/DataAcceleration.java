package com.skt.ehs.mbs.data;

import java.io.Serializable;

/**
 * Tag에서 올라오는 가속도의 정보를 저장
 * @author YiSuHwan
 *
 */
public class DataAcceleration implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4962110279550706229L;
	
	private double x;
	private double y;
	private double z;
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
}
