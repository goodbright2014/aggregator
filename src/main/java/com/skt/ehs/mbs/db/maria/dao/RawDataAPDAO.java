package com.skt.ehs.mbs.db.maria.dao;



import com.skt.ehs.mbs.db.maria.vo.RawDataAP;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-22.
 */
public interface RawDataAPDAO {

    void insertRawDataAP(RawDataAP rawdata);
    ArrayList<RawDataAP> getRawDataAP();
    //void deleteRawDataAP(String c_cell_id);
}
