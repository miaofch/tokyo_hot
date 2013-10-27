package com.coolb.th.proxy.reader.impl;

import com.coolb.th.proxy.Proxy;
import com.coolb.th.proxy.reader.ProxyListReaderAdapter;

public class ProxyListReaderImpl1 extends ProxyListReaderAdapter {

	@Override
	protected Proxy processLine(String line) {
		Proxy proxyConfig = new Proxy();
		String[] strs = line.split("\\s")[0].split(":");
		proxyConfig.setHost(strs[0]);
		proxyConfig.setPort(Integer.parseInt(strs[1]));
		return proxyConfig;
	}


}
