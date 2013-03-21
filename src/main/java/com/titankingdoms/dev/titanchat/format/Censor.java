/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
 *     
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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