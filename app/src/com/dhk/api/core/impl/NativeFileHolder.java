package com.dhk.api.core.impl;

import java.io.InputStream;

import com.dhk.api.core.FileHolder;

public class NativeFileHolder implements FileHolder {

	public InputStream getFile(String uri) {
		if (uri == null || uri.isEmpty()) {
			return null;
		}
		StringBuilder builder = new StringBuilder(BASE_URI.length()
				+ uri.length());
		builder.append(BASE_URI);
		builder.append(uri);
		String url = builder.toString();
		//FileInputStream file = new FileInputStream(url);
		return null;
	}
}
