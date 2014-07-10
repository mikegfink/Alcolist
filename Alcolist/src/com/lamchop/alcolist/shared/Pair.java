package com.lamchop.alcolist.shared;

import java.io.Serializable;

public class Pair implements Serializable {
	private int total;
	private int batch;
	
	public Pair() {
		total = 0;
		batch = 0;
	}
	
	public Pair(int total, int batch){
		this.total = total;
		this.batch = batch;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getBatch() {
		return batch;
	}
}
