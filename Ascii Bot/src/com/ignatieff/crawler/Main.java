package com.ignatieff.crawler;

import java.io.IOException;

import com.ignatieff.converter.GIFConverter;

public class Main {

	public static void main(String[] args) throws IOException {
		if(args.length!=4){
			System.err.println("Invalid number of arguments, expected 4.");
			return;
		}
         Crawl c = new Crawl(args[0], args[1], args[2], args[3]);
	}

}
