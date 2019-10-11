package it.reply.challenge.fantabosco.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
	
	private FileUtils() {
	}
	
	public static List<String> readFile(String fileName) {
		List<String> output = new ArrayList<>();
		BufferedReader br = null;
		InputStream is = null;
		try {
			is = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
			br = new BufferedReader(new InputStreamReader(is));

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				output.add(sCurrentLine);
			}
		} catch (NullPointerException e) {
			System.err.println("File not found in classpath: " + fileName);
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return output;
	}

}
