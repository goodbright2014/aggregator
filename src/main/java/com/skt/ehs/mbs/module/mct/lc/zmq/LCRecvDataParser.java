package com.skt.ehs.mbs.module.mct.lc.zmq;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.InputMismatchException;

import com.skt.ehs.mbs.data.DataAP;
import com.skt.ehs.mbs.data.DataAcceleration;
import com.skt.ehs.mbs.data.DataMagnetic;
import com.skt.ehs.mbs.data.DataTag;

/**
 * Location Core에서 전달되는 데이터를 Parsing할 수 있도록 지원하는 Class
 * @author YiSuHwan
 *
 */
public class LCRecvDataParser {

	private static final int TAG_ID_DATA_SIZE = 16;
	private static final int MAGNETIC_DATA_SIZE = 21;
	private static final int LC_EXTENTION_DATA_SIZE = 20;
	private static final int LC_AP_INFO_SIZE = 20;

	/**
	 * Extention에서 사용하는 값들
	 */
	private static final int LC_RESULT_TYPE_SIZE = 4;
	private static final int LC_ZONE_ID_SIZE = 4;
	private static final int LC_POS_X_SIZE = 4;
	private static final int LC_POS_Y_SIZE = 4;
	private static final int LC_BATTERY_SIZE = 3;
	private static final int LC_ALERT_SIZE = 1;

	/**
	 *  AP-INFO 에서 사용하는 값들
	 */
	private static final int LC_AP_ID_SIZE = 16;
	private static final int LC_AP_RSSI_SIZE = 4;
	
	
	/**
	 * LC에서 전달된 데이터를 전체 Parsing하여 DataTag Object를 생성해서 전달한다.
	 * @param data	LC데이터에서 전달되는 Byte 데이터
	 * @return	DataTag의 Object
	 * @throws Throwable	데이터가 문제가 있을 경우 생기면 발생 (데이터 Parsing에 문제가 있을 수도 있음)
	 */
	public static DataTag parseLCData(final byte[] data) throws Throwable {
		DataTag tagdata = new DataTag();
		
		tagdata.setTagID(getTagID(data));
	
		byte[] magneticData = new byte[MAGNETIC_DATA_SIZE];
		
		System.arraycopy(data, TAG_ID_DATA_SIZE, magneticData, 0, MAGNETIC_DATA_SIZE);
		magDataParser(magneticData, tagdata);
		
		byte[] lcExtentionData = new byte[LC_EXTENTION_DATA_SIZE];
		System.arraycopy(data, TAG_ID_DATA_SIZE + MAGNETIC_DATA_SIZE, lcExtentionData, 0, LC_EXTENTION_DATA_SIZE);
		parseLCExtentionData(lcExtentionData, tagdata);
		
		int apCnt = 0;

		try {
			byte apCntByte = data[TAG_ID_DATA_SIZE + MAGNETIC_DATA_SIZE + LC_EXTENTION_DATA_SIZE];
			apCnt = ((int)apCntByte & 0xff);
		} catch (ArrayIndexOutOfBoundsException e) {
			// AP Data 가 아예 없을 경우에 대해서 예외처리를 위함
			// data[TAG_ID_DATA_SIZE + MAGNETIC_DATA_SIZE + LC_EXTENTION_DATA_SIZE] 의 예외처리
		}
					
		if (apCnt > 0) {
			byte[] apDatas = new byte[LC_AP_INFO_SIZE * apCnt];
			System.arraycopy(data, TAG_ID_DATA_SIZE + MAGNETIC_DATA_SIZE + LC_EXTENTION_DATA_SIZE + 1
							, apDatas, 0, LC_AP_INFO_SIZE * apCnt);
			parseAPData(apDatas, tagdata, apCnt);
		}

		return tagdata;
	}
	
	
	/**
	 * LC Core에서 받은 데이터에서 Tag의 고유 ID를 획득한다.
	 * @param data
	 * @return
	 */
	public static String getTagID (byte[] data) throws Throwable {

		byte[] tagID = new byte[TAG_ID_DATA_SIZE]; 
		System.arraycopy(data, 0, tagID, 0, TAG_ID_DATA_SIZE);

		return getStringToCustomByte (tagID);
	}

	/**
	 * 고정된 byte수에 0x00 의 데이터가 체워지다가 0x00이 달라지는 순간부터 데이터로 인식하는 기능을 수행한다. 
	 * 
	 * @param data
	 * @return
	 * @throws Throwable
	 */
	public static String getStringToCustomByte(byte[] data) throws Throwable {
		
		char[] retData = null;
		int length = 0;
		
		for (int i = data.length -1 ; i > 0 ; --i) {
			if (data[i] != 0){
				length = i + 1;
				break;
			}
		}

		retData = new char[length];
		for (int i = 0 ; i < length; ++i) {
			retData[i] = (char)data[i];
		}
		
		return String.valueOf(retData);
	}
	
	
	/**
	 * LC Core에서 넘겨받은 데이터 중 Ap 데이터 영역을 Parsing하는 부분
	 * (AP 개수 부분은 해당 함수를 호출하기 전에 사용하고 해당 데이터는 여기에서 처리하지 않는다.)
	 * @param data
	 * @param tagdata
	 * @throws Throwable
	 */
	public static void parseAPData(final byte[] data, DataTag tagdata, int apCnt) throws Throwable {
		if (tagdata == null)
			throw new NullPointerException("arg DataTag is null");		
		if (data.length != LC_EXTENTION_DATA_SIZE * apCnt || apCnt == 0)
			throw new InputMismatchException("data Size is not right");
		
		ArrayList<DataAP> aps = new ArrayList<>();
		byte[] tempRssi = new byte[LC_AP_RSSI_SIZE];
		byte[] tempAPID = new byte[LC_AP_ID_SIZE];
		
		for (int i = 0; i < apCnt; ++i) {
			DataAP tempAP = new DataAP();	
			System.arraycopy(data, LC_AP_INFO_SIZE * i, tempAPID, 0, LC_AP_ID_SIZE);
			tempAP.setApID( getStringToCustomByte (tempAPID));
			System.arraycopy(data, LC_AP_INFO_SIZE * i + LC_AP_ID_SIZE, tempRssi, 0, LC_AP_RSSI_SIZE);
			tempAP.setRssi(byteArrayToInt(tempRssi));
			aps.add(tempAP);
		}
		tagdata.setAps(aps);
	}
	
	/**
	 * LC Core에서 넘겨받는 데이터 중 Tag에서 전달되는 값 이외에 LC에서 추가되어서 전달되는 값을 Parsing한다.
	 * Result Type	4	Integer
	 * Zone ID		4	String
	 * X 좌표			4	Integer
	 * Y 좌표			4	Integer
	 * Battery		3	String
	 * 비상호출 발생 여부	1	byte
	 * 
	 * @param data
	 * @param tagdata
	 * @throws Throwable
	 */
	public static void parseLCExtentionData(final byte[] data, DataTag tagdata) throws Throwable {

		if (tagdata == null)
			throw new NullPointerException("arg DataTag is null");		
		if (data.length != LC_EXTENTION_DATA_SIZE)
			throw new InputMismatchException("data Size is not right");

		byte[] tempData = new byte[LC_RESULT_TYPE_SIZE];
		int pos = 0;
		System.arraycopy(data, pos, tempData, 0, LC_RESULT_TYPE_SIZE);
		tagdata.setResultType(byteArrayToInt(tempData));
		pos += LC_RESULT_TYPE_SIZE;
		
		System.arraycopy(data, pos, tempData, 0, LC_ZONE_ID_SIZE);
		tagdata.setZoneID(byteArrayToInt(tempData));
		pos += LC_ZONE_ID_SIZE;
		
		tempData = new byte[LC_POS_X_SIZE];
		System.arraycopy(data, pos, tempData, 0, LC_POS_X_SIZE);
		tagdata.setTagXfromLC(byteArrayToInt(tempData));
		pos += LC_POS_X_SIZE;
		
		tempData = new byte[LC_POS_Y_SIZE];
		System.arraycopy(data, pos, tempData, 0, LC_POS_Y_SIZE);
		tagdata.setTagYfromLC(byteArrayToInt(tempData));
		pos += LC_POS_Y_SIZE;
		
		tagdata.setBattery(new String (data, pos, LC_BATTERY_SIZE));
		pos += LC_BATTERY_SIZE;
		
		tagdata.setAlertOccurrence(data[pos]); // 		LC_ALERT_SIZE
	}
	
	/**
	 * LC에서 넘겨준 데이터 중 실제 지자기 21 byte 데이터에 대해서 Parsing하는 부분
	 * @param data
	 * @return
	 */
	public static void magDataParser (final byte[] data, DataTag tagdata) throws Throwable {
		
		if (tagdata == null)
			throw new NullPointerException("arg DataTag is null");
		if (data.length != MAGNETIC_DATA_SIZE)
			throw new InputMismatchException("data Size is not right");
		
		ArrayList<DataMagnetic> magnetics = new ArrayList<>();
		
		//byteArrayToInt2(data[0], data[1]);
		
		// 2byte의 지자기 정보 bv, bh 값 * 3개 저장
		DataMagnetic magnetic = new DataMagnetic();
		byte[] twoByteData = new byte[2];
		int pos = 2; System.arraycopy(data, pos, twoByteData, 0, 2);
		magnetic.setBv((int)byteArrayToShort(twoByteData));
		pos += 2; System.arraycopy(data, pos, twoByteData, 0, 2);
		magnetic.setBh((int)byteArrayToShort(twoByteData));
		magnetics.add(magnetic);
		magnetic = new DataMagnetic();
		pos += 2; System.arraycopy(data, pos, twoByteData, 0, 2);
		magnetic.setBv((int)byteArrayToShort(twoByteData));
		pos += 2; System.arraycopy(data, pos, twoByteData, 0, 2);
		magnetic.setBh((int)byteArrayToShort(twoByteData));
		magnetics.add(magnetic);
		magnetic = new DataMagnetic();
		pos += 2; System.arraycopy(data, pos, twoByteData, 0, 2);
		magnetic.setBv((int)byteArrayToShort(twoByteData));
		pos += 2; System.arraycopy(data, pos, twoByteData, 0, 2);
		magnetic.setBh((int)byteArrayToShort(twoByteData));
		magnetics.add(magnetic);
		tagdata.setMagnetic(magnetics);
		
		ArrayList<DataAcceleration> acceldatas = new ArrayList<>();
		DataAcceleration acceldata = new DataAcceleration();
		acceldata.setX((int)data[14] & 0xff);
		acceldata.setY((int)data[15] & 0xff);
		acceldata.setZ((int)data[16] & 0xff);
		acceldatas.add(acceldata);
		acceldata = new DataAcceleration();
		acceldata.setX((int)data[17] & 0xff);
		acceldata.setY((int)data[18] & 0xff);
		acceldata.setZ((int)data[19] & 0xff);
		acceldatas.add(acceldata);
		tagdata.setAccelerations(acceldatas);
		
		tagdata.setNorthDirection((int)data[20] & 0xff);
	}
	
	
//	private static int byteArrayToInt2(byte bytes[]) throws Throwable {
//		return  ((int)bytes[0] & 0xff) << 8 | 
//				((int)bytes[1] & 0xff);
//	}

	private static int byteArrayToInt2(byte bytes0, byte bytes1)  throws Throwable{
		return  ((int)bytes0 & 0xff) << 8 | 
				((int)bytes1 & 0xff);
	}

	public static short byteArrayToShort(byte[] bytes) {
		final int size = Short.SIZE / 8;
		ByteBuffer buff = ByteBuffer.allocate(size);
		final byte[] newBytes = new byte[size];
		for (int i = 0; i < size; i++) {
			if (i + bytes.length < size) {
				newBytes[i] = (byte) 0x00;
			} else {
				newBytes[i] = bytes[i + bytes.length - size];
			}
		}
		buff = ByteBuffer.wrap(newBytes);
		buff.order(ByteOrder.BIG_ENDIAN); // Endian에 맞게 세팅
		return buff.getShort();
	}
	
	public static byte[] shortToByteArray(final short data) {
		ByteBuffer buff = ByteBuffer.allocate(Short.SIZE / 8);
		buff.putShort(data);
		buff.order(ByteOrder.BIG_ENDIAN);
		//buff.order(ByteOrder.LITTLE_ENDIAN);
		return buff.array();
	}
	
	public static byte[] intToByteArray(final int integer) {
		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE / 8);
		buff.putInt(integer);
		buff.order(ByteOrder.BIG_ENDIAN);
		//buff.order(ByteOrder.LITTLE_ENDIAN);
		return buff.array();
	}
	
	public static int byteArrayToInt(byte[] bytes) {
		final int size = Integer.SIZE / 8;
		ByteBuffer buff = ByteBuffer.allocate(size);
		final byte[] newBytes = new byte[size];
		for (int i = 0; i < size; i++) {
			if (i + bytes.length < size) {
				newBytes[i] = (byte) 0x00;
			} else {
				newBytes[i] = bytes[i + bytes.length - size];
			}
		}
		buff = ByteBuffer.wrap(newBytes);
		buff.order(ByteOrder.BIG_ENDIAN); // Endian에 맞게 세팅
		return buff.getInt();
	}

}
