package com.skt.ehs.mbs.db.maria.vo;

public class EHSUser {
	
	/**
	 * User의 ID
	 */
	private String USER_ID;
	
	/**
	 * UserGroup이 ID
	 */
	private String USER_GROUP_ID;
	
	/**
	 * User의 Password
	 */
	private String PASSWORD;

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getUSER_GROUP_ID() {
		return USER_GROUP_ID;
	}

	public void setUSER_GROUP_ID(String uSER_GROUP_ID) {
		USER_GROUP_ID = uSER_GROUP_ID;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

}
