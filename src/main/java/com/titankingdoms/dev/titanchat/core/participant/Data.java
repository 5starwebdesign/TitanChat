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

package com.titankingdoms.dev.titanchat.core.participant;

public final class Data {
	
	private final Object value;
	
	public Data(Object value) {
		this.value = value;
	}
	
	public boolean asBoolean() {
		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue();
		
		if (value instanceof Number)
			return asInteger() != 0;
		
		if (value instanceof String)
			return Boolean.valueOf(asString()).booleanValue();
		
		return value != null;
	}
	
	public byte asByte() {
		if (value instanceof Number)
			return ((Number) value).byteValue();
		
		if (value instanceof String)
			try { return Byte.valueOf(asString()).byteValue(); } catch (Exception e) {}
		
		return 0;
	}
	
	public double asDouble() {
		if (value instanceof Number)
			return ((Number) value).doubleValue();
		
		if (value instanceof String)
			try { return Double.valueOf(asString()).doubleValue(); } catch (Exception e) {}
		
		return 0.0D;
	}
	
	public float asFloat() {
		if (value instanceof Number)
			return ((Number) value).floatValue();
		
		if (value instanceof String)
			try { return Float.valueOf(asString()).floatValue(); } catch (Exception e) {}
		
		return 0.0F;
	}
	
	public int asInteger() {
		if (value instanceof Number)
			return ((Number) value).intValue();
		
		if (value instanceof String)
			try { return Integer.valueOf(asString()).intValue(); } catch (Exception e) {}
		
		return 0;
	}
	
	public long asLong() {
		if (value instanceof Number)
			return ((Number) value).longValue();
		
		if (value instanceof String)
			try { return Long.valueOf(asString()).longValue(); } catch (Exception e) {}
		
		return 0L;
	}
	
	public short asShort() {
		if (value instanceof Number)
			return ((Number) value).shortValue();
		
		if (value instanceof String)
			try { return Short.valueOf(asString()).shortValue(); } catch (Exception e) {}
		
		return 0;
	}
	
	public String asString() {
		return (value != null) ? value.toString() : "";
	}
	
	public Object value() {
		return value;
	}
}