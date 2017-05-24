package com.xy.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pc {
	static Set<String> phones = new HashSet<String>();
	static Pattern pattern = Pattern.compile(">1[3|4|5|7|8]\\d{9}</");
	static Pattern patternHttp = Pattern.compile("href=\"(.+?)\"");

	public static void main(String args[]) {
		String s = "abcd";
		char c = "a".charAt(0);
		System.out.println(s.indexOf(c) != -1);
		
		/*getUrl("http://xzpeixian.58.com/shangpucs/0/?PGTID=0d306b36-02c5-5bbc-1906-8551620657a1&ClickID=1&qq-pf-to=pcqq.c2c");

		for (String phone : phones) {
			System.out.println(phone);
		}*/
	}

	private static void getUrl(String url_) {
		URL url;
		int responsecode;
		HttpURLConnection urlConnection;
		BufferedReader reader;
		String line;
		try {
			// 生成一个URL对象，要获取源代码的网页地址为：http://www.sina.com.cn
			url = new URL(url_);
			// 打开URL
			urlConnection = (HttpURLConnection) url.openConnection();
			// 获取服务器响应代码
			responsecode = urlConnection.getResponseCode();
			if (responsecode == 200) {
				// 得到输入流，即获得了网页的内容
				reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
				while ((line = reader.readLine()) != null) {
					Matcher matcher = pattern.matcher(line);
					while (matcher.find()) {
						phones.add(matcher.group(1));
					}
					Matcher matcherHttp = patternHttp.matcher(line);
					while (matcherHttp.find()) {
						String s = matcherHttp.group(1);
						if (s.startsWith("http://") && s.length() > 80) {
							System.out.println(s);
							//getUrl(s);
						}
					}
				}
			} else {
				System.out.println("获取不到网页的源码，服务器响应代码为：" + responsecode);
			}
		} catch (Exception e) {
			System.out.println("获取不到网页的源码,出现异常：" + e);
		}
	}
}
