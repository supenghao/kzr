package com.dhk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtil {

	public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  //newFixedThreadPool(50)
}
