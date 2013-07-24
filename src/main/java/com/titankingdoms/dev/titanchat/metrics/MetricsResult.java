package com.titankingdoms.dev.titanchat.metrics;

public enum MetricsResult {
	DISABLED("Metrics have been disabled"),
	FAILURE("Metrics cannot be set up"),
	SUCCESS("Metrics is now set up");
	
	private String message;
	
	private MetricsResult(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}