package com.titankingdoms.dev.titanchat.update;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.math.NumberUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class UpdateVerify {
	
	private final String rss;
	private final String currentVersion;
	private String newVersion;
	
	public UpdateVerify(String rss, String currentVersion) {
		this.rss = rss;
		this.currentVersion = currentVersion;
		verify();
	}
	
	public String getCurrentVersion() {
		return currentVersion;
	}
	
	public String getNewVersion() {
		return newVersion;
	}
	
	public boolean verify() {
		try {
			URL url = new URL(rss);
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openStream());
			doc.getDocumentElement().normalize();
			
			Node node = doc.getElementsByTagName("item").item(0);
			
			if (node.getNodeType() == 1) {
				Node name = ((Element) node).getElementsByTagName("title").item(0);
				Node version = name.getChildNodes().item(0);
				newVersion = version.getNodeValue().split(" ")[1].trim().substring(1);
				
			} else { newVersion = currentVersion; }
			
		} catch (Exception e) { newVersion = currentVersion; }
		
		boolean update = false;
		
		String[] currentDigits = currentVersion.split("\\.");
		String[] newDigits = newVersion.split("\\.");
		
		for (int digit = 0; digit < Math.max(currentDigits.length, newDigits.length); digit++) {
			if (currentDigits.length < digit + 1 || newDigits.length < digit + 1)
				break;
			
			String currentDigitString = currentDigits[digit];
			String newDigitString = newDigits[digit];
			
			if (!NumberUtils.isNumber(currentDigitString) || !NumberUtils.isNumber(newDigitString))
				continue;
			
			int currentDigit = NumberUtils.toInt(currentDigitString);
			int newDigit = NumberUtils.toInt(newDigitString);
			
			if (newDigit == currentDigit)
				continue;
			
			update = newDigit > currentDigit;
			break;
		}
		
		return update;
	}
}