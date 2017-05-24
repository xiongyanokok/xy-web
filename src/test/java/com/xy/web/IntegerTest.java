package com.xy.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IntegerTest {

	public static void main(String[] args) {
		Long s1 = 128L;
		Long s2 = 128L;
		System.out.println(s1 == s2); 
		System.out.println(s1.equals(s2));
		
		List<String> list = new ArrayList<String>();
		list.subList(1, 1);

		
	}
	
	static class ThreadTest extends Thread {
		public ThreadTest()  {
			super.setName("xxxxx");
		}
		
	}
	
	
}
