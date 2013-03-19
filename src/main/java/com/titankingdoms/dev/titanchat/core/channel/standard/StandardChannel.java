package com.titankingdoms.dev.titanchat.core.channel.standard;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.info.Range;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.format.Format;

public class StandardChannel extends Channel {
	
	public StandardChannel(String name, Status status) {
		super(name, "Standard", status);
	}
	
	@Override
	public String[] getAliases() {
		return getConfig().getStringList("aliases").toArray(new String[0]);
	}
	
	@Override
	public String getFormat() {
		return getConfig().getString("format", Format.getFormat());
	}
	
	@Override
	public String getPassword() {
		return getConfig().getString("password", "");
	}
	
	@Override
	public Range getRange() {
		return Range.fromName(getConfig().getString("range", "channel"));
	}
	
	@Override
	public void reload() {}
}