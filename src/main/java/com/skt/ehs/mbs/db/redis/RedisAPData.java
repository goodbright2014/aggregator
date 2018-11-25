package com.skt.ehs.mbs.db.redis;

/**
 * Created by user on 2016-11-16.
 */
public class RedisAPData {
    private String tag_id;
    private int sequence_num;
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
        return "RedisAPData{" +
                "tag_id='" + tag_id + '\'' +
                ", sequence_num=" + sequence_num +
                ", ap_id='" + ap_id + '\'' +
                ", rssi=" + rssi +
                '}';
    }
}
