package org.bhu.commons.lang.analyzer.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 加载词典用的类
 * 
 * @author Jackie
 */
public class DicReader {
	public static BufferedReader getReader(String name) {
		// maven工程修改词典加载方式
		InputStream in = DicReader.class.getResourceAsStream("/" + name);
		try {
			return new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static BufferedReader getReader(InputStream in) {
		// maven工程修改词典加载方式
		try {
			return new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedReader getZipFileBufferedReader(String name){
		try {
			return new BufferedReader(new InputStreamReader(getZipFileInputStream(name), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedReader getZipFileBufferedReader(InputStream in){
		try {
			return new BufferedReader(new InputStreamReader(in,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getInputStream(String name) {
		// maven工程修改词典加载方式
		InputStream in = DicReader.class.getResourceAsStream("/" + name);
		return in;
	}
	
	public static InputStream getZipFileInputStream(String name) {
		// maven工程修改词典加载方式
		InputStream in = zipFileRead(DicReader.class.getResource(name));
		return in;
	}
	
	private static InputStream zipFileRead(URL url) {
		InputStream is = null;
			try {
				// 获得zip信息
//				System.out.println(url.getPath());
				@SuppressWarnings("resource")
				ZipFile zipFile = new ZipFile(url.getPath());
				@SuppressWarnings("unchecked")
				Enumeration<ZipEntry> enu = (Enumeration<ZipEntry>) zipFile
						.entries();
				while (enu.hasMoreElements()) {
					ZipEntry zipElement = (ZipEntry) enu.nextElement();
					is = zipFile.getInputStream(zipElement);
				}
			} catch (IOException e) {
				e.printStackTrace();
				StaticDictionaryLoad.LIBRARYLOG.info(url.getPath());
			}
			return is;
	}
}
