package it.reply.challenge.fantabosco.challenge.matritext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.reply.challenge.fantabosco.utils.FileUtils;

public class MatriTextExtractor {
	
	private static int index;
	private static List<Doc> docs = new ArrayList<>();
	
	private enum RowType {
		AUTHOR,
		TITLE
	}

	public static void main(String[] args) {
		List<String> files = FileUtils.listDirectory("scr\\main\\resources\\matritext", true);
		for(String fileName : files) {
			Doc d = null;
			try {
				d = parseDocument("matritext\\" + fileName);
			} catch(StackOverflowError e) {
				System.err.println("Cannot parse file: " + fileName);
			}
			if(d != null) {
				docs.add(d);
			}
		}
		
		System.out.println("Loaded " + docs.size() + " docs");
		Collections.sort(docs, new Doc.DocComparator());
		for(Doc d : docs) {
			System.out.println(d.file + "\t\t" + d.author + "\t\t" + d.title);
		}
	}

	private static Doc parseDocument(String fileName) {
		Doc d = new Doc();
		index = 0;
		List<String> file = FileUtils.readFile(fileName);
		d.file = fileName.replace("matritext\\", "");
		
//		Project Gutenberg's La Divina Commedia di Dante, by Dante Alighieri
		d.intro = getWholeLine(file, null);

//		This eBook is for the use of anyone anywhere in the United States and most
//		other parts of the world at no cost and with almost no restrictions
//		whatsoever.  You may copy it, give it away or re-use it under the terms of
//		the Project Gutenberg License included with this eBook or online at
//		www.gutenberg.org.  If you are not located in the United States, you'll have
//		to check the laws of the country where you are located before using this ebook.
		d.license = getWholeLine(file, null);
		
//		Title: La Divina Commedia di Dante
		d.title = getWholeLine(file, RowType.TITLE).replace("Title:", "").replace("eitle:", "");

//		Author: Dante Alighieri
		d.author = getWholeLine(file, RowType.AUTHOR).replace("Author:", "");

//		Posting Date: November 7, 2015 [EBook #1012]
//		Release Date: August, 1997
//		First Posted: September 4, 1997
//		Last Updated: December 8, 2014
//
//		Language: Italian
//
//		Character set encoding: UTF-8
//
//		*** START OF THIS PROJECT GUTENBERG EBOOK LA DIVINA COMMEDIA DI DANTE ***
		return d;
	}
	
	private static String getWholeLine(List<String> file, RowType type) {
		// Go to first not empty line
		for(; index<file.size(); index++) {
			if(!file.get(index).trim().isEmpty()) {
				break;
			}
		}
			
		// Find whole line
		StringBuilder line = new StringBuilder();
		for(; index<file.size(); index++) {
			if(file.get(index).trim().isEmpty()) {
				break;
			}
			line.append(file.get(index).trim() + " ");
		}
		
		if(type != null) {
			boolean confirmed = false;
			switch(type) {
			case AUTHOR:
				confirmed = line.toString().contains("Author:");
				break;
			case TITLE:
				confirmed = line.toString().contains("Title:") || line.toString().contains("eitle");
				break;
			}
			if(confirmed) {
				// Found correct line
				index++;
				return line.toString().trim();
			} else {
				// Try again
//				System.out.println("This is not a " + type.name() + ":\t\t" + line);
				return getWholeLine(file, type).trim();
			}
		} else {
			// If no type is specified, return first whole line
			return line.toString().trim();
		}
	}

}
