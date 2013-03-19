package com.titankingdoms.dev.titanchat.format;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public final class Censor {
	
	public static String filter(String text, List<String> phrases, String censor) {
		if (text == null)
			return "";
		
		if (phrases == null)
			return text;
		
		if (censor == null)
			censor = "";
		
		StringBuffer filtered = new StringBuffer();
		
		String regex = "(" + StringUtils.join(phrases.toArray(new String[0])) + ")";
		
		Pattern phrasePattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher match = phrasePattern.matcher(text);
		
		while (match.find())
			match.appendReplacement(filtered, Pattern.compile(".").matcher(match.group()).replaceAll(censor));
		
		return match.appendTail(filtered).toString();
	}
}