package com.xy.web.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

public class BeforeHandlerImpl extends BeforeHandler {

	public void beforeInvoke(Object proxy, Method method, Object[] args) {
		System.out.println("JDK before");
	}
	
	public void beforeInvoke(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
		System.out.println("CGLIB before");
	}

}
