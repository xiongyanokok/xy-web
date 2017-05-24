package com.xy.web.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

/**
 * 前置处理抽象类
 * 
 * @author xiongyan
 * @date 2017年2月13日 下午2:58:45
 */
public abstract class BeforeHandler extends AbstractHandler {

	public abstract void beforeInvoke(Object proxy, Method method, Object[] args);
	
	public abstract void beforeInvoke(Object proxy, Method method, Object[] args, MethodProxy methodProxy);
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		beforeInvoke(proxy, method, args);
		return method.invoke(getTargetObject(), args);
	}
	
	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		beforeInvoke(proxy, method, args);
		return method.invoke(getTargetObject(), args);
	}

}
