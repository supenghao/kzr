package com.sunnada.kernel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ServerDef {

	private String name;
	private String text;
    private String ip;
    private int rmiPort;
    private boolean current;
    private boolean joinCluster;
    private InetAddress inetAddress;
    
    private Properties properties;
    
    private String className;
    private Class serverClass;

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getRmiPort() {
		return rmiPort;
	}
	public void setRmiPort(int rmiPort) {
		this.rmiPort = rmiPort;
	}
	public boolean isCurrent() {
		return current;
	}
	public void setCurrent(boolean current) {
		this.current = current;
	}
	public boolean isJoinCluster() {
		return joinCluster;
	}
	public void setJoinCluster(boolean joinCluster) {
		this.joinCluster = joinCluster;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}
	public InetAddress getInetAddress(){
        if(inetAddress == null)
            try
            {
                inetAddress = InetAddress.getByName(ip);
            }
            catch(UnknownHostException uhe)
            {
                throw new com.sunnada.kernel.exception.BaseException(uhe);
            }
        return inetAddress;
    }
    public Class getServer(){
    	if(serverClass == null)
    	    try
    	    {
    	    	serverClass = Thread.currentThread().getContextClassLoader().loadClass(className);
    	    }
    	    catch(ClassNotFoundException cnfe)
    	    {
    	        throw new com.sunnada.kernel.exception.BaseException((new StringBuffer("服务")).append(name).append("的'class'装载失败").toString(), cnfe);
    	    }
    	return serverClass;
    	}
}
