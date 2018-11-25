package com.skt.ehs.mbs.module.mct.lc;

import org.zeromq.ZMQ;

/**
 * ZeroMQ를 이용하여 LocationCore에서 처리한 데이터를 수신하는 부분 로직을 생성하고 이를 APDataProcessor에 넘겨줌
 * @author YiSuHwan
 * added jaehyu 2016-11-16
 * APDataProcessor 는 REST API 를 통해 비동기적으로 수행
 * LC interface 는 다음을 수행한다
 * - read lc_messages through zmq_subscriber (ZMQSubscriber)
 * - transformation (LCRecvDataParser)
 * - store to redis
 *
 * 이 클래스는 사용하지 않음
 */
public class LocationCoreInterface {

    public static void receive () {

    }

	public static void main(String[] args) {



		ZMQ.Context context = ZMQ.context(1);
        //  Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");

        while (!Thread.currentThread().isInterrupted()) {
            // Wait for next request from the client
            byte[] request = responder.recv(0);
            System.out.println("Received Hello");

            // Do some 'work'
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Send reply back to client
            String reply = "World";
            responder.send(reply.getBytes(), 0);
        }
        responder.close();
        context.term();
	}
}
