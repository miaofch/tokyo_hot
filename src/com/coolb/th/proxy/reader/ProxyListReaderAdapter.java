package com.coolb.th.proxy.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.coolb.th.proxy.Proxy;

public abstract class ProxyListReaderAdapter implements ProxyListReader {

	@Override
	public List<Proxy> readProxyList(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String nextLine = null;
		List<Proxy> proxyList = new ArrayList<Proxy>();
		while ((nextLine = reader.readLine()) != null) {
			try {
				if ("".equals(nextLine.trim())) {
					continue;
				}
				Proxy proxyConfig = processLine(nextLine);
				if (proxyConfig != null) {
					proxyList.add(proxyConfig);
				}
			} catch (Exception e) {
				// 若处理中出现问题则跳过此行
				continue;
			}
		}
		reader.close();
		return null;
	}
	
	abstract protected Proxy processLine(String line);

}
