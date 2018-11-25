package com.skt.ehs.mbs.db.maria.vo;

public class EHSFloorInfo {
	
	
	private int floorID;
	
	private String floorName;
	
	private String imageName;
	
	private double dist_pixel;

	public int getFloorID() {
		return floorID;
	}

	public void setFloorID(int floorID) {
		this.floorID = floorID;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public double getDist_pixel() {
		return dist_pixel;
	}

	public void setDist_pixel(double dist_pixel) {
		this.dist_pixel = dist_pixel;
	}
	
	public String toString() {
		return new String ("FloorID: " + floorID + ", FloorName : " + floorName + ", ImageName : " + imageName);
	}
}
