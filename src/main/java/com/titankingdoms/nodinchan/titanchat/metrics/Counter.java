package com.titankingdoms.nodinchan.titanchat.metrics;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.event.Listener;

public class Counter implements Listener {
	
	private final AtomicInteger characters = new AtomicInteger(0);
	private final AtomicInteger words = new AtomicInteger(0);
	private final AtomicInteger lines = new AtomicInteger(0);
}