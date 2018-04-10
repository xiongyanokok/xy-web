package com.xy.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/asyncServlet", asyncSupported = true)
public class AsyncServlet extends HttpServlet {
	
	ExecutorService executor = Executors.newFixedThreadPool(20); 

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AsyncContext asyncContext = request.startAsync(); 
		//asyncContext.addListener(new MyAsyncListener());

		ServletInputStream inputStream = request.getInputStream();
		inputStream.setReadListener(new MyReadListener(asyncContext));
		
		//asyncContext.setTimeout(2000);
		//executor.execute(new AsyncRequestProcessor(asyncContext));
	}
	
	class MyAsyncListener implements AsyncListener {
	    @Override
	    public void onComplete(AsyncEvent asyncEvent) throws IOException {
	        System.out.println("AppAsyncListener onComplete");
	    }

	    @Override
	    public void onError(AsyncEvent asyncEvent) throws IOException {
	        System.out.println("AppAsyncListener onError");
	    }

	    @Override
	    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
	        System.out.println("AppAsyncListener onStartAsync");
	    }

	    @Override
	    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
	        System.out.println("AppAsyncListener onTimeout");
	        ServletResponse response = asyncEvent.getAsyncContext().getResponse();
	    	response.setContentType("application/json"); // 指定内容类型为 JSON 格式
	    	response.setCharacterEncoding("UTF-8"); // 防止中文乱码
			response.getWriter().write("{\"error\":\"timeout\"}");
	    }
	    
	}
	
	class MyReadListener implements ReadListener {
	    private AsyncContext asyncContext;
	    
	    public MyReadListener(AsyncContext context){
	        this.asyncContext = context;
	    }
	    
	    //数据可用时触发执行
	    @Override
	    public void onDataAvailable() throws IOException {
	        System.out.println("数据可用时触发执行");
	    }

	    //数据读完时触发调用
	    @Override
	    public void onAllDataRead() throws IOException {
	    	System.out.println("数据读完时触发调用");
	    	executor.execute(new AsyncRequestProcessor(asyncContext));
	    }

	    //数据出错触发调用
	    @Override
	    public void onError(Throwable t){
	        System.out.println("数据 出错");
	        t.printStackTrace();
	    }
	}
	
	class AsyncRequestProcessor implements Runnable {
	    private AsyncContext asyncContext;

	    public AsyncRequestProcessor() {
	    }

	    public AsyncRequestProcessor(AsyncContext asyncContext) {
	        this.asyncContext = asyncContext;
	    }

	    @Override
	    public void run() {
    		String name = asyncContext.getRequest().getParameter("name");
    		write("{\"name\":\""+name+"\"}");
			asyncContext.complete();
	    }
	    
	    private void write(String content) {
	    	ServletResponse response = asyncContext.getResponse();
	    	response.setContentType("application/json"); // 指定内容类型为 JSON 格式
	    	response.setCharacterEncoding("UTF-8"); // 防止中文乱码
			try {
				Thread.sleep(3000);
				response.getWriter().write(content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
}
