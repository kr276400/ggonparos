package org.parosproxy.paros.common;


public class ThreadPool {

	private Thread[]	pool		= null;
	
	public ThreadPool(int maxThreadCount) {
		pool = new Thread[maxThreadCount];
	}
	/*
	 * pool이라는 thread배열 안에 null 값을 주어주고, threading할 수있는 한도를 최대한으로 잡은 값만큼 배열칸을 쥐어줘
	 */

	/*
	 * pool이라는 배열 안에서 free thread 값을 얻어오는데, 여기서 free thread 값이 없으면 null값을 리턴한다고 보면되 
	 */
	public synchronized Thread getFreeThreadAndRun(Runnable runnable) {
		/*
		 * synchronized의 뜻을 이해해보자고.
		 * 둘 이상의 쓰레드가 공동의 파일을 공유할 때, 순서를 잘 맞춰서 한 쓰레드가 파일 사용하고 있으면, 다른 쓰레드가 동시에 같은 파일 즉, 
		 * 레코드를 수정하면 문제 발생하니까, 이부분을 없애기 위해 처리하는 방법인데, 간단하게 동기화라고도 불러요.
		 * 
		 * 위에 있는거랑 붙여서 설명하면 실행할 수 있는 free thread를 얻어오고 실행하는 과정이 담긴 함수를 정의하는건데 Thread라는 
		 * 클래스 안에 포함되어 있는 메소드여.
		 */
	    
		for (int i=0; i<pool.length; i++) {
			if (pool[i] == null || !pool[i].isAlive()) {
				// 쓰레드들이 끝날 때 까지 기다리는 나머지 쓰레드들이 있는데, 이 애들을 체크하는 함수가 isAlive()라고 생각하면 됨
				pool[i] = new Thread(runnable);
				pool[i].setDaemon(true);
				// 데몬 쓰레드는 다른 일반 쓰레드( 데몬 쓰레드가 아닌 놈들)의 작업을 돕는 보조 역할을 수행하는 쓰레드라고 생각하면 된다잉.
				pool[i].start();
				return pool[i];
			}
			/*
			 * 만약에 pool안에 값들이 null이거나 pool안에 값들을 체크 해봤는데, 뒤져있어.
			 * 그 때, pool안에 값들안에 실행 가능한 값을 이용한 새로운 thread를 넣어주고,
			 * 작업을 도울 수 있게 보조역할을 하는 쓰레드로 만들어준다음,
			 * 그 작업들을 시작하게 해서
			 * 그 과정이 끝난 pool값들을 리턴한다고 생각혀
			 */
		}
		return null;
	}

	/*
	 * 각각 쓰레드가 지네들 각각 대부분의 시간에 맞춰서 모든 쓰레드가 작업을 끝낼 때까지 대기하는 부분이여.
	 * 만약에 안 끝난다면, 기다리지도 않어. 그리고 각 쓰레드들은 지네끼리 자살해야되.(쓰레드를 알아서 종료한다고 생각혀) 
	 */
	public void waitAllThreadComplete(int waitInMillis) {
		for (int i=0; i<pool.length; i++) {
			if (pool[i] != null && pool[i].isAlive()) {
				try {
					pool[i].join(waitInMillis);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	public boolean isAllThreadComplete() {
		for (int i=0; i<pool.length; i++) {
			if (pool[i] != null && pool[i].isAlive()) {
			    return false;
			}
		}
		return true;
	}
}
