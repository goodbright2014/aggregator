package com.skt.ehs.mbs.data;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Tag에서 올라오는 정보를 담아준다.
 * @author YiSuHwan
 *
 */
public class DataTag implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1623778544931961228L;
	
	// Tag ID
	private String tagID;
	// Tag의 sequenceNumber
	
	private int sequenceNumber;
	// battery level	(0~7)
	// 0 : low battery, 1 : 10%, 2 : 30%, 3 : 50%, 4 : 70%, 5 : 90%, 6 : NoMovement, 7 : Emergency
	//private int battery;
	// 지자기 최대 3개
	private ArrayList<DataMagnetic> magnetic;
	// 가속도 정보를 담아준다.
	private ArrayList<DataAcceleration> accelerations;
	// 각도 값 0~71 (* 5를 할 경우 355도까지 커버됨)
	private int northDirection;
	// TAG를 감지한 AP들의 정보를 넣어준다.
	private ArrayList<DataAP> aps;
	
	/*
	 *  LC Core에서 넘겨주는 extention 값, 
	 *  참조 (Location Core -> Magnetic Core간 데이터포맷_v1.0)
	 *  https://tde.sktelecom.com/wiki/pages/viewpage.action?pageId=87199582& 참조
	 */
	// 	1 : Zone이 변경 되었음 , 2 : Zone은 변경되지 않았으나 AP가 변경 됨.
	private int resultType;
	// 측위된 Zone의 ID
	private int zoneID;
	
	private int tagXfromLC;
	
	private int tagYfromLC;
	// 배터리 값  - Rule Engine에 전송시 필요.
	private String battery;
	// 긴급호출 발생   - Rule Engine에 전송시 필요.
	private byte alertOccurrence;
	
	// Distribution Node에서 확인한 층 정보를 저장 "campus_id:building_id:floor_id" 형식
	private String locationFloorInfo;
	
	public String getTagID() {
		return tagID;
	}
	public void setTagID(String tagID) {
		this.tagID = tagID;
	}
	public String getBattery() {
		return battery;
	}
	public void setBattery(String battery) {
		this.battery = battery;
	}
	public ArrayList<DataMagnetic> getMagnetic() {
		return magnetic;
	}
	public void setMagnetic(ArrayList<DataMagnetic> magnetic) {
		this.magnetic = magnetic;
	}

	public ArrayList<DataAcceleration> getAccelerations() {
		return accelerations;
	}
	public void setAccelerations(ArrayList<DataAcceleration> accelerations) {
		this.accelerations = accelerations;
	}
	public int getNorthDirection() {
		return northDirection;
	}
	public void setNorthDirection(int northDirection) {
		this.northDirection = northDirection;
	}
	public ArrayList<DataAP> getAps() {
		return aps;
	}
	public void setAps(ArrayList<DataAP> aps) {
		this.aps = aps;
	}
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public int getResultType() {
		return resultType;
	}
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}
	public int getZoneID() {
		return zoneID;
	}
	public void setZoneID(int zoneID) {
		this.zoneID = zoneID;
	}
	public int getTagXfromLC() {
		return tagXfromLC;
	}
	public void setTagXfromLC(int tagXfromLC) {
		this.tagXfromLC = tagXfromLC;
	}
	public int getTagYfromLC() {
		return tagYfromLC;
	}
	public void setTagYfromLC(int tagYfromLC) {
		this.tagYfromLC = tagYfromLC;
	}
	public byte getAlertOccurrence() {
		return alertOccurrence;
	}
	public void setAlertOccurrence(byte alertOccurrence) {
		this.alertOccurrence = alertOccurrence;
	}
	public String getLocationFloorInfo() {
		return locationFloorInfo;
	}
	public void setLocationFloorInfo(String locationFloorInfo) {
		this.locationFloorInfo = locationFloorInfo;
	}
}
