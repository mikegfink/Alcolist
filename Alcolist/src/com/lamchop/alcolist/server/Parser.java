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
			String nextToken;
			// Start the accumulator
			accumulator.setLength(0);
			boolean inQuotes = false;
			
			for (int i = 0; i < line.length(); i++) {
				char next = line.charAt(i);
				if (next == QUOTE) {
					inQuotes = !inQuotes;
				} else if (next == COMMA && !inQuotes) {
					// Only commas outside of quotes signal the end of a token.
					// Trim whitespace off either end before adding
					nextToken = accumulator.toString();
					tokens.add(trimWhitespace(nextToken));
					// Reset accumulator for next token.
					accumulator.setLength(0);
				} else {
					// Mid-token
					accumulator.append(next);
				}
			}
			// Add the last token, trimming whitespace off either end.
			nextToken = accumulator.toString();
			tokens.add(trimWhitespace(nextToken));
			
			String[] tokenArray = tokens.toArray(new String[tokens.size()]);
			return tokenArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Still return a String array if parsing fails.
		String[] noResult = {};
		return noResult;	
	}

	// Trim the whitespace around the given string, or all the whitespace if all characters are spaces/tabs.
	// Needed because built-in trim function will not trim whitespace from a string containing only 
	// whitespace.
	private String trimWhitespace(String string) {
		// Regular expression to match strings containing only whitespace
		String regex = "[ \t]*";
		if (string.matches(regex)) {
			string = "";
		} else {
			string = string.trim();
		}
		return string;
	}
}

