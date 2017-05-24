package com.xy.web;

import java.util.concurrent.Callable;

public class UserCallable implements Callable<String>, Runnable {

	private String name;
	
	public UserCallable(String name) {
		this.name = name;
	}
	
	@Override
	public String call() throws Exception {
		System.out.println("call...");
		return name;
	}

	@Override
	public void run() {
		System.out.println("run...");
	}

}
