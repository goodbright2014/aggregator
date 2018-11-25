package com.skt.ehs.mbs.db.maria.vo;

import java.util.Date;

/**
 * Created by user on 2016-11-22.
 */
public class RawDataAP {
    private String tag_id;
    private int sequence_num;
    private Date collected_time;
    private String ap_id;
    private int rssi;

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public int getSequence_num() {
        return sequence_num;
    }

    public void setSequence_num(int sequence_num) {
        this.sequence_num = sequence_num;
    }

    public Date getCollected_time() {
        return collected_time;
    }

    public void setCollected_time(Date collected_time) {
        this.collected_time = collected_time;
    }

    public String getAp_id() {
        return ap_id;
    }

    public void setAp_id(String ap_id) {
        this.ap_id = ap_id;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "RawDataAP{" +
                "tag_id='" + tag_id + '\'' +
                ", sequence_num=" + sequence_num +
                ", collected_time=" + collected_time +
                ", ap_id='" + ap_id + '\'' +
                ", rssi=" + rssi +
                '}';
    }
}
