package org.bhu.commons.lang.analyzer.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.bhu.commons.lang.analyzer.dictionary.StaticDictionaryLoad;

public class EasternAsianName {

	public static HashMap<String, NameParameter> asianNameMap = new HashMap<String, NameParameter>();
	
	public static HashMap<String, NameParameter> loadNameMap(){
		BufferedReader reader = StaticDictionaryLoad.getAsianNameReader();
		String line;
		String[] items;
		try {
			for(;(line=reader.readLine())!=null;){
				if(line.trim().startsWith("//"))continue;
				items = line.split("\t");
				if(!asianNameMap.containsKey(items[0])){
					asianNameMap.put(items[0], new NameParameter(
							Double.parseDouble(items[1]),
							Double.parseDouble(items[2]),
							Double.parseDouble(items[3])));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return asianNameMap;
	}
}
