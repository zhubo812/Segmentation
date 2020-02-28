package org.bhu.commons.lang.analyzer.enamex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bhu.commons.lang.analyzer.bean.Entity;

public class EntityHelper {
	protected static Pattern patterns = null;
	protected static Matcher match;
	
	protected static List<Entity> getEntity(String src) {
		int startline = -1, endline = -1;
		List<Entity> entitylist = new ArrayList<Entity>();
		try {
			match = patterns.matcher(src);
			while (match.find()) {
				startline = match.start();
				endline = match.end();
				String str = match.group();
				entitylist.add(new Entity(str, startline, endline));
			}

		} catch (Exception e) {
			System.err.println(src);
			System.err.println(e.toString());
		}

		return entitylist;
	}
}
