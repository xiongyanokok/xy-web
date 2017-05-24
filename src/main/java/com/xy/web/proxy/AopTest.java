package com.xy.web.proxy;

import java.util.ArrayList;
import java.util.List;

public class AopTest {

	public static void main(String[] args) {
		List<AbstractHandler> handlers = new ArrayList<AbstractHandler>();
		BeforeHandler beforeHandler = new BeforeHandlerImpl();
		AfterHandler afterHandler = new AfterHandlerImpl();
		ThrowHandler throwHandler = new ThrowHandlerImpl();
		handlers.add(beforeHandler);
		handlers.add(afterHandler);
		handlers.add(throwHandler);
		
		UserServiceImpl userService = new UserServiceImpl();
		UserService proxy = (UserService) ProxyFactory.getProxy(userService, false, handlers);
		System.out.println(proxy.add(1, 2));
	}

}
