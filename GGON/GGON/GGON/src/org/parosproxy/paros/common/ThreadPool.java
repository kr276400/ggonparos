package org.parosproxy.paros.common;


public class ThreadPool {

	private Thread[]	pool		= null;
	
	public ThreadPool(int maxThreadCount) {
		pool = new Thread[maxThreadCount];
	}
	/*
	 * pool�̶�� thread�迭 �ȿ� null ���� �־��ְ�, threading�� ���ִ� �ѵ��� �ִ������� ���� ����ŭ �迭ĭ�� �����
	 */

	/*
	 * pool�̶�� �迭 �ȿ��� free thread ���� �����µ�, ���⼭ free thread ���� ������ null���� �����Ѵٰ� ����� 
	 */
	public synchronized Thread getFreeThreadAndRun(Runnable runnable) {
		/*
		 * synchronized�� ���� �����غ��ڰ�.
		 * �� �̻��� �����尡 ������ ������ ������ ��, ������ �� ���缭 �� �����尡 ���� ����ϰ� ������, �ٸ� �����尡 ���ÿ� ���� ���� ��, 
		 * ���ڵ带 �����ϸ� ���� �߻��ϴϱ�, �̺κ��� ���ֱ� ���� ó���ϴ� ����ε�, �����ϰ� ����ȭ��� �ҷ���.
		 * 
		 * ���� �ִ°Ŷ� �ٿ��� �����ϸ� ������ �� �ִ� free thread�� ������ �����ϴ� ������ ��� �Լ��� �����ϴ°ǵ� Thread��� 
		 * Ŭ���� �ȿ� ���ԵǾ� �ִ� �޼ҵ忩.
		 */
	    
		for (int i=0; i<pool.length; i++) {
			if (pool[i] == null || !pool[i].isAlive()) {
				// ��������� ���� �� ���� ��ٸ��� ������ ��������� �ִµ�, �� �ֵ��� üũ�ϴ� �Լ��� isAlive()��� �����ϸ� ��
				pool[i] = new Thread(runnable);
				pool[i].setDaemon(true);
				// ���� ������� �ٸ� �Ϲ� ������( ���� �����尡 �ƴ� ���)�� �۾��� ���� ���� ������ �����ϴ� �������� �����ϸ� �ȴ���.
				pool[i].start();
				return pool[i];
			}
			/*
			 * ���࿡ pool�ȿ� ������ null�̰ų� pool�ȿ� ������ üũ �غôµ�, �����־�.
			 * �� ��, pool�ȿ� ����ȿ� ���� ������ ���� �̿��� ���ο� thread�� �־��ְ�,
			 * �۾��� ���� �� �ְ� ���������� �ϴ� ������� ������ش���,
			 * �� �۾����� �����ϰ� �ؼ�
			 * �� ������ ���� pool������ �����Ѵٰ� ������
			 */
		}
		return null;
	}

	/*
	 * ���� �����尡 ���׵� ���� ��κ��� �ð��� ���缭 ��� �����尡 �۾��� ���� ������ ����ϴ� �κ��̿�.
	 * ���࿡ �� �����ٸ�, ��ٸ����� �ʾ�. �׸��� �� ��������� ���׳��� �ڻ��ؾߵ�.(�����带 �˾Ƽ� �����Ѵٰ� ������) 
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
