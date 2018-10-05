package com.dhk.kernel.cache;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

public class MemClient implements CacheClient{
	private MemcachedClient memcachedClient;
	private int expireTime;


	public MemClient(MemcachedClient memcachedClient){
		this.memcachedClient = memcachedClient;
		expireTime = CacheClient.defaultExpireTime;
	}
	public int getExpireTime(){
		return expireTime;
	}
	public void setExpireTime(int expireTime){
		this.expireTime = expireTime;
	}
	public <T> T get(String key){
		return (T) memcachedClient.get(key);
	}
	public boolean delete(String key) throws Exception{
		OperationFuture<Boolean> result = memcachedClient.delete(key);
		return result.get();
	}
	public boolean flushAll() throws Exception{
		OperationFuture<Boolean> result = memcachedClient.flush();
		return result.get();
	}
	public boolean flushAll(int delay) throws Exception{
		OperationFuture<Boolean> result = memcachedClient.flush(delay);
		return result.get();
	}
	public <T> boolean add(String key, T value) throws Exception{
		OperationFuture<Boolean> result = memcachedClient.add(key, expireTime, value);
		return result.get();
	}
	public <T> boolean add(String key, T value, int expiry) throws Exception{
		OperationFuture<Boolean> result = memcachedClient.add(key, expiry, value);
		return result.get();
	}
	public <T> boolean set(String key, T value) throws Exception{
		OperationFuture<Boolean> result = memcachedClient.set(key, expireTime, value);
		return result.get();
	}
	public <T> boolean set(String key, T value, int expiry) throws Exception{
		OperationFuture<Boolean> result = memcachedClient.set(key, expiry, value);
		return result.get();
	}
	public MemcachedClient getClient() {
		return memcachedClient;
	}
	public static void main(String[] args) throws Exception {
		MemcachedClient cache = null;
		try {
			cache = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
//		String key = "xx";
//		String value = System.currentTimeMillis()+"";
//		int maxTryCount = 10;
//		int res = 0;
//		
//		while(maxTryCount-->0&&res==0){
//			CASValue<Object> x = null;
//			try {
//				x = cache.asyncGets(key).get();
//			} catch (InterruptedException | ExecutionException e) {
//				e.printStackTrace();
//			}
//			if(x!=null){
//				long version = x.getCas();
//				CASResponse resp = cache.cas(key,version, value);
//				res = resp.compareTo(CASResponse.EXISTS);
////				if(0==resp.compareTo(CASResponse.EXISTS)){
////					throw new VersionExpiredException();
////				}
//			}else{
//				cache.set(key,0 ,value);
//				res=1;
//			}
//		}
//		System.out.println("set success!");
//		return;
		
//		CASValue<Object> x = null;
//		try {
//			x = cache.asyncGets("xx").get();
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}
//		if(x!=null){
//			long version = x.getCas();
//			CASResponse resp = cache.cas("xx",version, ((Integer)x.getValue())+2);
//			if(0==resp.compareTo(CASResponse.EXISTS)){
//				throw new VersionExpiredException();
//			}
//		}else{
//			cache.set("xx",0 ,2);
//		}
		Object y = cache.get("xx");
		System.out.println("===:"+y);
		cache.delete("xx");
	}
}

