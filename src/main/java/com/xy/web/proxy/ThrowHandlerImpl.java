package com.xy.web.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

/**
 * 异常处理抽象类
 * 
 * @author xiongyan
 * @date 2017年2月13日 下午2:58:57
 */
public class ThrowHandlerImpl extends ThrowHandler {

	public void throwInvoke(Object proxy, Method method, Object[] args) {
		System.out.println("JDK throw");
	}

	public void throwInvoke(Object proxy, Method method, Object[] args, MethodProxy methodProxy) {
		System.out.println("CGLIB throw");
	}
	
}
