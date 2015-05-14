package com.ciandt.dcoder.lab.util;

public abstract class UniqueID {
	static long current = System.currentTimeMillis();

	public static synchronized long get() {
		return current++;
	}
}
