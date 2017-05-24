package com.xy.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xy.web.generator.IdWorker;

public class IdWorkerTest {
	
    public static void main(String[] args) {
        IdWorker worker = new IdWorker();
        Set<Long> set = Collections.synchronizedSet(new HashSet<Long>(1000000));
        List<Long> list = Collections.synchronizedList(new ArrayList<Long>(1000000));
		for (int i=0;i<10000;i++) {
			new Thread(() -> {
				for (int j=0; j<1000; j++) {
					long id = worker.nextId();
					System.out.println(id);
					set.add(id);
					list.add(id);
				}
			}).start();
		}
		try {
			Thread.sleep(200000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("set长度："+set.size());
		System.out.println("list长度："+list.size());
    }

}
