package com.dhk.api.core;

import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public interface NetworkInterface {

	public InputStream getInputstream(NetworkParams p);

	public String getResultStr(NetworkParams p) throws SocketTimeoutException;

	public class NetworkParams {

		private Map<String, String> map = new HashMap<String, String>();

		public NetworkParams addParam(String key, Object value) {
			map.put(key, value.toString());
			return this;
		}

		public NetworkParams removeParam(String key) {
			map.remove(key);
			return this;
		}

		public NetworkParams clear() {
			map.clear();
			return this;
		}

		public Map<String, String> getMap() {
			return map;
		}
	}
}
