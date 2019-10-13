package it.reply.challenge.fantabosco.challenge.puzzle;

import java.io.File;

public class Puzzle {

	public static void main(String[] args) {
		StringBuilder path = new StringBuilder();

		File folder = new File("scr\\main\\resources\\puzzle");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				path.append(listOfFiles[i].getName() + "\\");
				listOfFiles = listOfFiles[i].listFiles();
				i = -1;
			}
		}
		System.out.println(path);
	}
}
