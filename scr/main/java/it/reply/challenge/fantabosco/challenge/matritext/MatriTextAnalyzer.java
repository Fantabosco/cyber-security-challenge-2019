package it.reply.challenge.fantabosco.challenge.matritext;

import java.util.ArrayList;
import java.util.List;

import it.reply.challenge.fantabosco.utils.FileUtils;

public class MatriTextAnalyzer {
	/*
APPUNTI
 - In almeno un file sembra ci siano dei dati corrotti.
 - Trovare i file simili e estrarre i caratteri differenti?

FILE SIMILI
- 2600-0.txt / 2600-0 (1).txt		 Leo Tolstoy		 War and Peace
- pg2383.txt / pg2383 (1).txt [U]		Geoffrey Chaucer		The Canterbury Tales and Other Poems
- pg30452.txt / pg30452 (1).txt		Various		 Astounding Stories,  April, 1931
- pg13951.txt / pg13951 (1).txt		Alexanere Dumas		Les trois mouhquetaires
	 */
	

	private static int index;
	private static List<Doc> docs = new ArrayList<>();
	
	private enum RowType {
		AUTHOR,
		TITLE
	}

	public static void main(String[] args) {		
		// Leo Tolstoy, War and Peace
//		compareBinaryFiles("2600-0.txt","2600-0 (1).txt");
//		 
		// Geoffrey Chaucer, The Canterbury Tales and Other Poems
//		compareBinaryFiles("pg2383.txt","pg2383 (1).txt");
		
		// Various, Astounding Stories,  April, 1931
		compareBinaryFiles("pg30452.txt","pg30452 (1).txt");
		
		//Alexanere Dumas, Les trois mouhquetaires
		compareBinaryFiles("pg13951.txt","pg13951 (1).txt");
	}
	
	private static void compareBinaryFiles(String fileName1, String fileName2) {
		System.out.println();
		System.out.println("_________COMPARING: " + fileName1 + "/" + fileName2 + "_________");
		
		byte[] file1 = FileUtils.readBinaryFile("matritext\\workarea\\" + fileName1);
		byte[] file2 = FileUtils.readBinaryFile("matritext\\workarea\\" + fileName2);
		
		StringBuilder delta1 = new StringBuilder();
		StringBuilder delta2 = new StringBuilder();
		StringBuilder delta1_skipped = new StringBuilder();
		StringBuilder delta2_skipped = new StringBuilder();
		try {
			int delta = 0;
			boolean isPreviousDifferent = false;
			for(int i = 0; (i + delta)<Math.min(file1.length, file2.length); i++) {
				if(file1[i + delta] != file2[i]) {
					// Check if one of them is a space
//					if(file1[i + delta] == (byte) ' ') {
//						delta--;
//					} else if(file2[i] == (byte) ' ') {
//						delta++;
//					} else if(file1[i + delta] == file2[i + 1]) {
//						// Can apply a delta + 1
//						delta2_skipped.append((char) file2[i]);
//						delta++;
//					} else if(file1[i + delta + 1] == file2[i]) {
//						// Can apply a delta - 1
//						delta1_skipped.append((char) file1[i + delta]);
//						delta--;
//					} else {
						if(isPreviousDifferent) {
							System.out.println("aaa");
						}
						// Cannot fix this discrepancy
						delta1.append((char) file1[i + delta]);
						delta2.append((char) file2[i]);
						isPreviousDifferent = true;
//					}
				} else {
					isPreviousDifferent = false;
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		} finally {
			if(delta1.length() > 0 || delta2.length() > 0) {
				System.out.println("DELTA1: " + delta1);
				System.out.println("DELTA2: " + delta2);
				
				if(delta1.toString().contains("{FLG:")) {
					System.err.println("\n\n\n!!!!!!!!!!!\n\n\n");
				}
				if(delta2.toString().contains("{FLG:")) {
					System.err.println("\n\n\n!!!!!!!!!!!\n\n\n");
				}
			}
				
			if(delta1_skipped.length() > 0 || delta2_skipped.length() > 0) {
				System.out.println("DELTA1_SKIPPED: " + delta1_skipped);
				System.out.println("DELTA2_SKIPPED: " + delta2_skipped);
				
				if(delta1_skipped.toString().contains("{FLG:")) {
					System.err.println("\n\n\n!!!!!!!!!!!\n\n\n");
				}
				if(delta2_skipped.toString().contains("{FLG:")) {
					System.err.println("\n\n\n!!!!!!!!!!!\n\n\n");
				}
			}
		}
	}
	
	
	
	
	private static void compareFiles(String fileName1, String fileName2) {
		System.out.println();
		System.out.println("_________COMPARING: " + fileName1 + "/" + fileName2 + "_________");
		
		StringBuilder delta1 = new StringBuilder();
		StringBuilder delta2 = new StringBuilder();
		List<String> file1 = FileUtils.readFile("matritext\\workarea\\" + fileName1);
		List<String> file2 = FileUtils.readFile("matritext\\workarea\\" + fileName2);

		if(file1.size() != file2.size()) {
			System.err.println("Difference in file size");
		}
		
		try {
			for(int i = 0; i<Math.min(file1.size(),file2.size()); i++) {
				String[] line1 = file1.get(i).trim().split(" ");
				String[] line2 = file2.get(i).trim().split(" ");
				if(line1.length != line2.length) {
					System.err.println("Difference in line words: " + Math.abs(line1.length - line2.length));
				}
				
				for(int j = 0; j<Math.min(line1.length, line2.length); j++) {
					String word1 = line1[j];
					String word2 = line2[j];
	
					if(Math.abs(word1.length() - word2.length()) > 2) {
						System.err.println("Compare out of sync: " + word1 + "\t" + word2);
						return;
					}
					
					if(!word1.equals(word2)) {
						for(int k=0; k<Math.min(word1.length(), word2.length()); k++) {
							if(word1.charAt(k) != word2.charAt(k)) {
								delta1.append(word1.charAt(k));
								delta2.append(word2.charAt(k));
							}
						}
					}
				}
			}
		} finally {
			if(delta1.length() > 0 || delta2.length() > 0) {
				System.out.println("DELTA1: " + delta1);
				System.out.println("DELTA2: " + delta2);
			}
		}
	}
}
