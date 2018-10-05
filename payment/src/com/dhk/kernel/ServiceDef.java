package com.dhk.kernel;

import java.util.HashSet;
import java.util.Set;

import com.dhk.kernel.exception.BaseException;

public class ServiceDef extends ServerDeployableDef{
	private String name;
    private String text;
    private String sService;
    private Class service;
    private String sImpl;
    private Class impl;
    private String sInterceptor;
    private Object instance;
    private Object proxy;
    private Set protocols;
    
    private String className;
    private Class serviceClass;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public ServiceDef(){
	  protocols = new HashSet(0);
	}
	public Class getService(){
	if(service == null)
	    try
	    {
	        service = Thread.currentThread().getContextClassLoader().loadClass(sService);
	    }
	    catch(ClassNotFoundException cnfe)
	    {
	        throw new BaseException((new StringBuffer("服务")).append(name).append("的'class'装载失败").toString(), cnfe);
	    }
	return service;
	}
	
	public Class getServiceClass(){
		if(serviceClass == null)
		    try
		    {
		    	serviceClass = Thread.currentThread().getContextClassLoader().loadClass(className);
		    }
		    catch(ClassNotFoundException cnfe)
		    {
		        throw new BaseException((new StringBuffer("服务")).append(name).append("的'class'装载失败").toString(), cnfe);
		    }
		return serviceClass;
		}
	
	public Class getImpl(){
	if(impl == null)
	    if(sImpl != null)
	        try{
	            impl = Thread.currentThread().getContextClassLoader().loadClass(sImpl);
	        }catch(ClassNotFoundException cnfe){
	            throw new BaseException((new StringBuffer("服务")).append(name).append("的'impl'装载失败").toString(), cnfe);
	        }
	    else
	        impl = getService();
	    return impl;
	}
	
	
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getSImpl(){
		return sImpl;
	}
	
	public void setSImpl(String impl){
		sImpl = impl;
	}
	
	public String getSInterceptor(){
		return sInterceptor;
	}
	
	public void setSInterceptor(String interceptor){
		sInterceptor = interceptor;
	}
	
	public String getSService(){
		return sService;
	}
	
	public void setSService(String service){
		sService = service;
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public void setImpl(Class impl)
	{
		this.impl = impl;
	}
	
	public void setInstance(Object instance){
		this.instance = instance;
	}
	
	public void setProxy(Object proxy){
		this.proxy = proxy;
	}
	
	public void setService(Class service){
		this.service = service;
	}
	
	public Set getProtocols(){
		return protocols;
	}
	
	public void setProtocols(Set protocols){
		this.protocols = protocols;
	}

}
