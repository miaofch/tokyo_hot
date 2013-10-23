package com.coolb.th.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class GreatCustomer {

	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		// 读取配置文件
		Properties config = new Properties();
		config.load(new FileInputStream(GreatCustomer.class.getResource("/").getFile() + "config.properties"));
		// 读取代理列表文件地址
		String proxyListFile = config.getProperty("proxylist");
		// 读取最高延迟时间
		int maxdelay = Integer.parseInt(config.getProperty("maxdelay", "60"));

		// 读取代理列表
		System.out.println("开始读取代理文件");
		BufferedReader reader = new BufferedReader(new FileReader(GreatCustomer.class.getResource("/").getFile() + proxyListFile));
		String nextLine = null;
		List<ProxyConfig> proxyList = new ArrayList<ProxyConfig>();
		while ((nextLine = reader.readLine()) != null) {
			try {
				if (!"".equals(nextLine.trim())) {

					ProxyConfig proxyConfig = new ProxyConfig();
//					proxyConfig.setHost(nextLine.split("\\s")[0]);
//					proxyConfig.setPort(Integer.parseInt(nextLine.split("\\s")[1]));
					proxyConfig.setHost(nextLine.split("\\s")[0].split(":")[0]);
					proxyConfig.setPort(Integer.parseInt(nextLine.split("\\s")[0].split(":")[1]));
					proxyList.add(proxyConfig);
				}
			} catch (Exception e) {
				// 若处理中出现问题则跳过此行
				continue;
			}
		}
		reader.close();
		System.out.println("代理文件获取完毕，共获取代理[" + proxyList.size() + "]个");

		// 连接流程
		System.out.println("开始组装链接");
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
		
		// 设置投票地址
		String host = "http://www.hotpepper.jp/strJ001038791/";
		HttpGet method = new HttpGet(host);
		System.out.println("目标[" + host + "]");

		// 伪装头信息
		method.setHeader("Accept", "*/*");
		method.setHeader("Accept-Encoding", "gzip, deflate");
		method.setHeader("Cache-Control", "no-cache");
		method.setHeader("Connection", "keep-alive");
		// method.setHeader("Content-Length", "18");
		method.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		method.setHeader("Pragma", "no-cache");
		method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:19.0) Gecko/20100101 Firefox/19.0");
		method.setHeader("X-Requested-With", "XMLHttpRequest");

		// 遍历代理列表进行发送
		System.out.println("开始遍历请求过程");
		int count = 0;
		for (int i = 0; i < proxyList.size(); i++) {
			// 设置代理
			ProxyConfig proxyConfig = proxyList.get(i);
			System.out.print((i + 1) + ".当前代理[" + proxyConfig + "]");
			HttpHost proxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			// 测试代理
			boolean enable = false;
			try {
				HttpGet testMethod = new HttpGet("http://www.baidu.com");
				HttpResponse testResponse = client.execute(testMethod);
				if (testResponse.getStatusLine().getStatusCode() == 200) {
					enable = true;
				} else {
					System.out.print("\t代理测试不可用");
				}
				testMethod.releaseConnection();
			} catch (Exception e) {
				System.out.print("\t代理测试不可用");
			}
			// 测试通过后，开始发送
			if (enable) {
				try {
					HttpResponse response = client.execute(method);
					// 打印输出
					// System.out.println(response.getStatusLine().getStatusCode());
					// 获取结果
					if (response.getStatusLine().getStatusCode() == 200) {
						System.out.print("\t投票成功");
						count++;
					} else {
						System.out.print("\t投票失败:" + response.getStatusLine().getStatusCode());
						if (response.getStatusLine().getStatusCode() == 500) {
							System.out.println(response.getEntity().getContentType().getValue());
							System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
						}
					}
					method.releaseConnection();
				} catch (Exception e) {
					System.out.print("\t代理异常");
				}
				// 计算伪装延迟
				int delay = (int) (Math.random() * (maxdelay + 1));
				System.out.print("\t[" + delay + "]秒后进行下一次请求");
				Thread.sleep(delay * 1000);
			}
			System.out.println();
		}

		// 处理结束，输出汇总信息
		System.out.println("遍历请求完成，成功次数[" + count + "]，实际增加可能小于此值");

	}
}

class ProxyConfig {

	private String host;
	private int port;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}

}
