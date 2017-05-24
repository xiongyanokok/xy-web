package com.xy.web.proxy;

import java.lang.reflect.Proxy;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;

public class ProxyFactory {

	public static Object getProxy(Object targetObject, Boolean cglib, List<AbstractHandler> handlers) {
		if (null != handlers && !handlers.isEmpty()) {
			Object proxyObject = targetObject;
			for (AbstractHandler handler : handlers) {
				handler.setTargetObject(proxyObject);
				if (cglib) {
					Enhancer enhancer = new Enhancer();
					enhancer.setSuperclass(targetObject.getClass());
					enhancer.setCallback(handler);
					proxyObject = enhancer.create();
				} else {
					proxyObject = Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), handler);
				}
			}
			return proxyObject;
		}
		return targetObject;
	}
}
