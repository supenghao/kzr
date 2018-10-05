package com.dhk;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public interface NetworkInterface {

	public InputStream getInputstream(NetworkParams p) throws Exception;

	public String getResultStr(NetworkParams p)  throws  Exception ;

	public class NetworkParams {

		private Map<String, String> map = new LinkedHashMap<String, String>();

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
