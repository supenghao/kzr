package com.sunnada.kernel;

import org.apache.log4j.Logger;

public class DreamHelper {
	static Logger logger = Logger.getLogger(DreamHelper.class);
	
	static ServerHelper serverHelper = new ServerHelper();
	static BackendHelper backendHelper = new BackendHelper();
	
	public static void startServer() {
		serverHelper.startServers();
		backendHelper.startBackends();
	}
	public static void stopServer(){
		serverHelper.stopServers();
		backendHelper.stopBackends();
	}
}
