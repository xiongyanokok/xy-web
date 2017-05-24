package com.xy.web.proxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

/**
 * 异常处理抽象类
 * 
 * @author xiongyan
 * @date 2017年2月13日 下午2:58:57
 */
public abstract class ThrowHandler extends AbstractHandler {

	public abstract void throwInvoke(Object proxy, Method method, Object[] args);
	
	public abstract void throwInvoke(Object proxy, Method method, Object[] args, MethodProxy methodProxy);
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			return method.invoke(getTargetObject(), args);
		} catch (Exception e) {
			throwInvoke(proxy, method, args);
			throw e;
		}
	}
	
	@Override
	public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		try {
			return methodProxy.invoke(getTargetObject(), args);
		} catch (Exception e) {
			throwInvoke(proxy, method, args);
			throw e;
		}
	}

}
