package com.titankingdoms.dev.titanchat.data;

public class DataValue {
	
	private final String value;
	
	public DataValue(String value) {
		this.value = value;
	}
	
	public boolean asBoolean() {
		return Boolean.valueOf(value).booleanValue();
	}
	
	public byte asByte() {
		try { return Byte.valueOf(value).byteValue(); } catch (Exception e) { return 0; }
	}
	
	public double asDouble() {
		try { return Double.valueOf(value).doubleValue(); } catch (Exception e) { return 0.0D; }
	}
	
	public float asFloat() {
		try { return Float.valueOf(value).floatValue(); } catch (Exception e) { return 0.0f; }
	}
	
	public int asInt() {
		try { return Integer.valueOf(value).intValue(); } catch (Exception e) { return 0; }
	}
	
	public long asLong() {
		try { return Long.valueOf(value).longValue(); } catch (Exception e) { return 0L; }
	}
	
	public short asShort() {
		try { return Short.valueOf(value).shortValue(); } catch (Exception e) { return 0; }
	}
	
	public String asString() {
		return value;
	}
}