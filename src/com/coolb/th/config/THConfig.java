package com.coolb.th.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class THConfig {
	
	private static Properties CONFIG;
	
	private static String CONFIG_FILE = "config.properties";
	
	static {
		try {
			loadConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadConfig() throws FileNotFoundException, IOException {
		CONFIG = new Properties();
		CONFIG.load(new FileInputStream(THConfig.class.getResource("/") + CONFIG_FILE));
	}

}
