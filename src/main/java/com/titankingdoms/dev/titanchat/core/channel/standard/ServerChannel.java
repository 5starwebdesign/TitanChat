package com.titankingdoms.dev.titanchat.core.channel.standard;

import com.titankingdoms.dev.titanchat.core.channel.info.Range;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;

public final class ServerChannel extends StandardChannel {
	
	public ServerChannel() {
		super("Server", Status.DEFAULT);
	}
	
	@Override
	public Range getRange() {
		return Range.GLOBAL;
	}
}