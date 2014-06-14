package com.lamchop.alcolist.server;

public class Parser {
	
	public String[] parseLine(String line) {
		// Too simple parsing that will not work for with commas within values.
		// TODO write a proper parser.
		// TODO write tests for parser.
		return line.split(",", -1); // -1 so trailing commas produce empty strings instead of being discarded.
	}
}
