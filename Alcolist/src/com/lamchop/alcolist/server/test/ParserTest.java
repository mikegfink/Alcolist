package com.lamchop.alcolist.server.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.lamchop.alcolist.server.Parser;

/** Test the Parser class */
public class ParserTest {

	private Parser parser;
	
	/**
	 * Initialize parser for the tests. 
	 */
	@Before
	public void setup() {
		parser = new Parser();
	}
	
	/**
	 * Test we can parse a simple comma-separated line of text.
	 */
	@Test
	public void testParseSimpleLine() {
		String line = "first,second,third,fourth,fifth";
		String[] tokens = parser.parseLine(line);
		
		assertEquals(5, tokens.length);
		assertEquals("first", tokens[0]);
		assertEquals("fifth", tokens[4]);		
	}
	
	@Test
	/**
	 * Test we can parse a line with missing fields.
	 */
	public void testParseMissingFields() {
		String line = "0,1,,,4,,6";
		String[] tokens = parser.parseLine(line);
		
		assertEquals(7, tokens.length);
		assertEquals("", tokens[2]);
		assertEquals("", tokens[3]);
		assertEquals("4", tokens[4]);
		assertEquals("", tokens[5]);
	}
	
	/**
	 * Test we can parse a line ending with commas and still get the right # tokens.
	 */
	@Test
	public void testParseTrailingCommas() {
		String line = "zero,one,,";
		String[] tokens = parser.parseLine(line);
		
		assertEquals(4, tokens.length);
		assertEquals("one", tokens[1]);
		assertEquals("", tokens[2]);
		assertEquals("", tokens[3]);
		
	}

	/**
	 * Test we can properly parse a quoted field with internal commas.
	 */
	@Test
	public void testParseQuotedCommas() {
		String line = "Austin House Fish & Chips,\"# 1, 32650 Logan Avenue\",Food Primary,94";
		String[] tokens = parser.parseLine(line);
		
		assertEquals("Austin House Fish & Chips", tokens[0]);
		assertEquals("# 1, 32650 Logan Avenue", tokens[1]);
		assertEquals("Food Primary", tokens[2]);
		assertEquals("94", tokens[3]);
		assertEquals(4, tokens.length);
	}
	
	/**
	 * Test parsing an example line from the CSV we are using.
	 */
	@Test
	public void testParseExampleLine() {
		String line = "The View Winery ,#1 - 2287 Ward Road,,KELOWNA,V1W4R5,1-2287 WARD RD,,KELOWNA,BC,V1W4R5,250 2151331,Winery,0";
		String[] tokens = parser.parseLine(line);
		
		assertEquals("The View Winery", tokens[0]);
		assertEquals("#1 - 2287 Ward Road", tokens[1]);
		assertEquals("", tokens[2]);
		assertEquals("0", tokens[12]);
		assertEquals(13, tokens.length);
	}
	
}