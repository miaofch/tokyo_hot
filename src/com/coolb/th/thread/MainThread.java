package com.coolb.th.thread;

import org.apache.log4j.Logger;

public class MainThread {
	
	private static final Logger log = Logger.getLogger(MainThread.class); 

	public static void main(String[] args) {
		try {
			Class.forName("com.coolb.th.config.THConfig");
		} catch (ClassNotFoundException e) {
			log.error("加载配置失败", e);
		}
	}
	
}
