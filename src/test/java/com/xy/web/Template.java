package com.xy.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

public class Template {
	
	Charset uft8 = Charset.forName("utf-8");
	
	static Map<String, List<String>> map = new HashMap<String, List<String>>();
	static Map<String, String> fileMap = new HashMap<String, String>();
	
	public void compile(String basePath, String fromPath, String toPath) throws Exception {
		Collection<File> files = FileUtils.listFiles(new File(basePath + fromPath), new SuffixFileFilter(".html"), DirectoryFileFilter.DIRECTORY);
		for (File f : files) {
			String content = getContext(basePath, fromPath, toPath, f);
			fileMap.put(f.getName(), content);
			File newPath = new File(basePath + toPath);
			if (!newPath.exists()) {
				newPath.mkdirs();
			}
			FileUtils.write(new File(newPath, f.getName()), content, uft8);
		}
	}
	
	public String getContext(String basePath, String fromPath, String toPath, File f) throws IOException {
		if (fileMap.containsKey(f.getName())) {
			return fileMap.get(f.getName());
		}
		// 读取文件内容
		String line = FileUtils.readFileToString(f, uft8);

		String str = "<!--@include\\s+file=\"(.*)\"\\s+-->";
		Pattern pattern = Pattern.compile(str);
		Matcher matcher = pattern.matcher(line);
		List<Record> records = new ArrayList<Record>();
		while (matcher.find()) {
			List<String> list = map.get(f.getName());
			if (null == list) {
				list = new ArrayList<String>();
				map.put(f.getName(), list);
			}
			String filename = matcher.group(1);
			String fileName = filename;
			if (filename.lastIndexOf("/") != -1) {
				fileName = fileName.substring(fileName.lastIndexOf("/")+1);
			}
			List<String> subList = map.get(fileName);
			if (null != subList && !subList.isEmpty()) {
				for (String sub : subList) {
					if (map.containsKey(sub)) {
						String msg = String.format("%s include file %s", f.getAbsolutePath(), basePath + filename);
						System.out.println(msg);
						throw new RuntimeException("你已陷入死循环");
					}
				}
			}
			list.add(fileName);
			File includeFile = new File(basePath + fromPath, filename);
			if (!includeFile.exists()) {
				throw new FileNotFoundException();
			}
			String context = getContext(basePath, fromPath, toPath, includeFile);
			records.add(new Record(matcher.start(), matcher.end(), context));
		}
		
		String newContent = line;
		for (Record record : records) {
			newContent = newContent.replace(line.substring(record.start, record.end), record.content);
		}
		return newContent;
	}

	public class Record {
		public int start;
		public int end;
		public String content;

		public Record(int start, int end, String content) {
			this.start = start;
			this.end = end;
			this.content = content;
		}
	}
	
	public static void main(String[] args) throws Exception {
		Template template = new Template();
		String basePath = "C:/Users/xiongyan/Desktop/testapp/web/";
		String fromPath = "htmlTemplate";
		String toPath = "pages";
		template.compile(basePath, fromPath, toPath);
		System.out.println(fileMap);
	}

}
