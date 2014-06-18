package com.lamchop.alcolist.server;

import java.util.ArrayList;
import java.util.List;

public class Parser {
	
	private static final char QUOTE = '"';
	private static final char COMMA = ',';
	
	public String[] parseLine(String line) {
		try {
			List<String> tokens = new ArrayList<String>();
			StringBuffer accumulator = new StringBuffer();
			// Start the accumulator
			accumulator.setLength(0);
			boolean inQuotes = false;
			
			for (int i = 0; i < line.length(); i++) {
				char next = line.charAt(i);
				if (next == QUOTE) {
					inQuotes = !inQuotes;
				} else if (next == COMMA && !inQuotes) {
					// Only commas outside of quotes signal the end of a token.
					tokens.add(accumulator.toString());
					// Reset accumulator for next token.
					accumulator.setLength(0);
				} else {
					// Mid-token
					accumulator.append(next);
				}
			}
			// Add the last token
			tokens.add(accumulator.toString());
			
			String[] tokenArray = tokens.toArray(new String[tokens.size()]);
			return tokenArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Still return a String array if parsing fails.
		String[] noResult = {""};
		return noResult;	
	}
}

