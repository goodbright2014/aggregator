package com.skt.ehs.mbs.db.maria.vo;

public class EHSBuildingInfo {
	
	private int	campusID;
	
	private String campusName;
	
	private int buildID;
	
	private String buildName;

	public int getCampusID() {
		return campusID;
	}

	public void setCampusID(int campusID) {
		this.campusID = campusID;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public int getBuildID() {
		return buildID;
	}

	public void setBuildID(int buildID) {
		this.buildID = buildID;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}
	
	public String toString() {
		return new String ("campusID: " + campusID + ", campusName : " + campusName + ", buildID : " + buildID + ", buildName : " + buildName);
	}
}
