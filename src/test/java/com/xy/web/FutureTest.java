package com.xy.web;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.MDC;

public class FutureTest {

	public static void main(String[] args) throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("xxx");
				
			}
		});
		
		executor.execute(new FutureTask<Integer>(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				System.out.println("call");
				return 1;
			}
		}));
		
		Future<Integer> future = executor.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				System.out.println("call");
				return 1;
			}
		});
		
		
		FutureTask<String> futureTask = new FutureTask<String>(new UserCallable("熊焱"));
		
		executor.execute(futureTask);
		executor.submit(futureTask);
		
		System.out.println(futureTask.get());
		
	}

}
