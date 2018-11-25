package com.skt.ehs.mbs.module.mct.lc.zmq;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.ehs.mbs.conf.Configure;

/**
 * ZeroMQ에서 무한정 데이터를 읽어오는 기능을 제공 
 * 해당 데이터는 다른 곳으로 넘겨 DB에 담도록 처리된다. 
 * @author YiSuHwan
 *
 */
public class ZMQSubscriber extends Thread {
	final Logger logger = LoggerFactory.getLogger(ZMQSubscriber.class);

	public interface OnRecvDataProcessor {
		void processData(byte[] data);

	}
	
	/**
	 * ZeroMQ Subscriber`s Topic.
	 * Location Core에서 전달된 데이터를 받는다.
	 */
	//private static final String TOPIC_COLLECT = "lc.locating";
	private static final String TOPIC_TRACKING = "lc.locating";

	/**
	 * 데이터를 읽고나서 전달을 해주기 위한 litener
	 */
	private OnRecvDataProcessor mProcessor;
	
	/**
	 * 해당 Thread를 종료하기 위한 함수
	 */
	private boolean mStopSubscribe;
	
	public ZMQSubscriber (OnRecvDataProcessor worker) {
		mProcessor = worker;
	}
	
	/**
	 * 전체 프로세서를 죽이거나 해당 기능만을 제거할 때를 제외하고는 해당 함수를 호출하지 안는다.
	 * Usually, you should not call this function except you try to finish server. 
	 */
	public void stopSubscriber() {
		mStopSubscribe = true;
	}
	
	@Override
	public void run() {
		
        // Prepare our context and subscriber
        Context context = ZMQ.context(1);
        Socket subscriber = context.socket(ZMQ.SUB);

        // To make server connection ip.
        String server = "tcp://" + Configure.MC_LC_ZEROMQ_IP 
        					+":" + Configure.MC_LC_ZEROMQ_PORT;
        
        //subscriber.connect(server);
        //subscriber.subscribe(TOPIC_TRACKING.getBytes());
		subscriber.connect("tcp://52.78.46.226:5563");
		subscriber.subscribe("lc.locating".getBytes());

        while (!mStopSubscribe) {
			String topic = subscriber.recvStr();   // read topic String
			//logger.info("Topic: "+topic);
        	byte[] recvData = subscriber.recv();  // receive byte [] data
			//logger.info("LC_Message: "+recvData.toString());

			// TODO : YI SUHWAN -> data가 들어오지 않을 경우에 recv에서 전달되는 값이 무엇인지를 확인해야한다.
        	//if (recvData == null) {
            	// TODO : NULL이 오면 socket에 문제가 생긴것으로 간주해야 할것으로 판단됨. 
        		// 에러처리 방안 고려 필요.
            //} else if (recvData.length == 0) {
            	//System.out.println("");
            //}

        	if (mProcessor != null) {
            	mProcessor.processData(recvData);
            }
        }
        
        subscriber.close ();
        context.term ();		
		
		//super.run();
	}
}
