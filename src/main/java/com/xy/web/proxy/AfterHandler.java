package com.xy.web.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

/**
 * 后置处理抽象类
 * 
 * @author xiongyan
 * @date 2017年2月13日 下午2:58:17
 */
public abstract class AfterHandler extends AbstractHandler {

	public abstract void afterInvoke(Object proxy, Method method, Object[] args);
	
	public abstract void afterInvoke(Object proxy, Method method, Object[] args, MethodProxy methodProxy);
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = method.invoke(getTargetObject(), args);
		afterInvoke(proxy, method, args);
		return result;
	}
	
	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Object result = methodProxy.invoke(getTargetObject(), args);
		afterInvoke(proxy, method, args);
		return result;
	}

}
