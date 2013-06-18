package com.titankingdoms.dev.titanchat.format.tag;

import com.titankingdoms.dev.titanchat.core.EndPoint;

public final class StaticTag extends Tag {
	
	private final String variable;
	
	public StaticTag(String tag, String variable) {
		super(tag);
		this.variable = (variable != null) ? variable : "";
	}
	
	@Override
	public String getVariable(EndPoint sender, EndPoint recipient) {
		return variable;
	}
}