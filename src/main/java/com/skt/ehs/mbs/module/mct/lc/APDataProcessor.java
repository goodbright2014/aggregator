package com.skt.ehs.mbs.module.mct.lc;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.ehs.mbs.db.redis.RedisDBManager;
import com.skt.ehs.mbs.module.mct.lc.zmq.ZMQSubscriber;


/**
 * LocationCore에서 수신받은 데이터를 Parsing하고 저장하는 역할을 수행
 * @author YiSuHwan
 *
 */
public class APDataProcessor implements ZMQSubscriber.OnRecvDataProcessor{
	final Logger logger = LoggerFactory.getLogger(APDataProcessor.class);
	
	public static int MAX_THREAD_NUMBER = 3;
	
	// 데이터가 얼마나 들어올지 모르기 때문에 ThreadPool을 사용해야 할 것같다.
	private ThreadPoolExecutor mThreadPool;
	/**
	 * Redis 연동시 필요한 API를 모아두는 Class
	 */
	private RedisDBManager mRedisDBManager;

	/**
	 * Worker에서 데이터를 처리해주기 위해 필요한 Queue
	 */
	final BlockingQueue<byte[]> mRecvApInfoQueue = new LinkedBlockingQueue<byte[]>();

	public APDataProcessor (RedisDBManager redisManager) {
		mRedisDBManager = redisManager;
		// SynchronousQueue 를 사용하면 execute를 할때 바로 worker를 추가로 생성한다로 적혀있음
		//mThreadPool = new ThreadPoolExecutor( 1, MAX_THREAD_NUMBER, 10, TimeUnit.SECONDS, new SynchronousQueue <Runnable>()  );
		// we needed to fine tune of these factors . core_size , max_size ...as testing being performed...
		// also we can change the value by using either setCorePoolSize(int) or setMaximumPoolSize(int).
		mThreadPool = new ThreadPoolExecutor( 1, 100, 10, TimeUnit.SECONDS, new SynchronousQueue <Runnable>()  );
		// core 는 pool 안에 대기하는 숫자 , core 가 작으면 thread instance 를 위한 context switching 이 자주 발생하는 것으로 관찰 됨
		// 괜찮다면 core 를 충분히 큰값으로 유지하는게 좋을듯...
	}
	
	/**
	 * void processData(byte[] data)
	 * ZMQSubscriber Thread에서 호출해주는 함수
	 * 쓰레드간 메세지 큐에 LC로부터의 메세지를 그대로 적재
	 * worker 쓰레드 없으면 하나 instantiation .. ThreadPoolExecutor 를 통해....
	*/

	public void processData(byte[] data) {
		try {
			mRecvApInfoQueue.put(data);
		} catch (InterruptedException e) {
			logger.error("mRecvApInfoQueue put error : " + e.getMessage());
		}
		int num_of_worker = mThreadPool.getPoolSize();
		if (mThreadPool.getPoolSize() == 0)
			mThreadPool.execute(new Worker());
	}

	/**
	 * 수신 받은 데이터를 처리하기 위한  worker
	 * @author YiSuHwan
	 */
	class Worker implements Runnable {
		
		public void run() {
			int preQueueCnt = 0;
			int endQueueCnt = 0;
			byte[] data;
			int val=0;

			while (true) {
				preQueueCnt = mRecvApInfoQueue.size();
				try {
					//2.. 3. Redis I/F 통해 저장 요청 ... IF 쪽 핸들러가 직접 byte[] 타입의 테이터 파싱하여
					// Redis 적재시 필요한 데이터구조 만들어서 바로적재
					mRedisDBManager.setAPInfo(mRecvApInfoQueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// thread pool management
				// 데이터를 처리했는데 처리 이후에 쌓인 데이터가 5개 이상이라면 worker를 추가해 준다.
				endQueueCnt = mRecvApInfoQueue.size();

				if (endQueueCnt - preQueueCnt > 30) {
					try {
						mThreadPool.execute(new Worker());
					}catch (RejectedExecutionException e) {
						logger.error("Not enough Worker !!" + e.getMessage());
					}catch (NullPointerException e) {
						logger.error("Null Point Error? " + e.getMessage());
					}
				}
			}
		}
	}
	
}

