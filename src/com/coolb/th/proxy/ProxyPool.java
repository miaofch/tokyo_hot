package com.coolb.th.proxy;

import java.util.List;

public class ProxyPool {
	

	/**
	 * 
	 */
	private List<List<Proxy>> proxyDepository;
	
	private static volatile ProxyPool proxyPool;
	
	private static Object lock;
	
	public static ProxyPool getInstance() {
		if (proxyPool == null) {
			synchronized (lock) {
				if (proxyPool == null) {
					proxyPool = new ProxyPool();
				}
			}
		}
		return proxyPool;
	}
	
	public Proxy getProxy() {
		return null;
	}

}
