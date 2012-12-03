package com.titankingdoms.nodinchan.titanchat.format;

import java.util.HashMap;
import java.util.Map;

public final class VariableManager {
	
	private final Map<String, FormatVariable> variables;
	
	public VariableManager() {
		this.variables = new HashMap<String, FormatVariable>();
	}
	
	public FormatVariable getVariable(String tag) {
		return variables.get(tag.toLowerCase());
	}
	
	public boolean hasVariable(String tag) {
		return variables.containsKey(tag.toLowerCase());
	}
	
	public boolean hasVariable(FormatVariable variable) {
		return hasVariable(variable.getFormatTag());
	}
	
	public void register(FormatVariable... variables) {
		for (FormatVariable variable : variables) {
			if (hasVariable(variable))
				continue;
			
			this.variables.put(variable.getFormatTag().toLowerCase(), variable);
		}
	}
}