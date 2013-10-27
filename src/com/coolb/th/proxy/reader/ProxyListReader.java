package com.coolb.th.proxy.reader;

import java.util.List;

import com.coolb.th.proxy.Proxy;

public interface ProxyListReader {
	
	public List<Proxy> readProxyList(String file) throws Exception;

}
