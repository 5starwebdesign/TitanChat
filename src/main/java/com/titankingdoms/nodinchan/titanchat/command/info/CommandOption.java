package com.titankingdoms.nodinchan.titanchat.command.info;

public @interface CommandOption {
	
	boolean requireChannel() default false;
	
	boolean allowConsoleUsage() default true;
}