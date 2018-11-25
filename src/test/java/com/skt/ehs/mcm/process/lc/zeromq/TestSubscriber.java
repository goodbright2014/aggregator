package com.skt.ehs.mcm.process.lc.zeromq;


import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;


/**
 * Created by user on 2016-11-16.
 */
public class TestSubscriber {
    public static void main(String[] args) {

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://52.78.46.226:5563");

        // connection fail 에 대해 처리해야...
        // connection fail 의 경우 : 1. client 처음 연결시 - publisher 작동 않할때,
        //                            2. 운영중 publisher 다운 되었을 시
        // 1 과 2의 경우를 각각 detect하여 대응해야...

        subscriber.subscribe("lc.locating".getBytes());
        while (!Thread.currentThread().isInterrupted()) {

            String topic = subscriber.recvStr(0);
            System.out.printf(" Topic: "+topic);
            String message = subscriber.recvStr(0);
            System.out.println(" message: "+message);

        }
        subscriber.close();
        context.term();

    }
}
