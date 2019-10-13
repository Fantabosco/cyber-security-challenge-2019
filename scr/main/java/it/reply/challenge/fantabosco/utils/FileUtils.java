package it.reply.challenge.fantabosco.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtils {
	
	private FileUtils() {
	}
	
	public static List<String> listDirectory(String directory, boolean ignoreDirs) {
		List<String> items = new ArrayList<>();
		File folder = new File(directory);
		if(!folder.exists()) {
			System.err.println("Does not exists: " + directory);
			return items;
		}
		if(!folder.isDirectory()) {
			System.err.println("Not a directory: " + directory);
			return items;
		}
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() || (!ignoreDirs && listOfFiles[i].isDirectory())) {
				items.add(listOfFiles[i].getName());
			}
		}
		Collections.sort(items);
		return items;
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
	
	public static byte[] readBinaryFile(String fileName) {
		DataInputStream dis = null;
		InputStream is = null;
		byte[] data = null;
		try {
			is = FileUtils.class.getClassLoader().getResourceAsStream(fileName);
			dis = new DataInputStream(new BufferedInputStream(is));

			byte[] dataBuffer = new byte[1000];
		    ByteArrayOutputStream streamBuffer = new ByteArrayOutputStream();
			while(dis.available() > 0) {
				int read = dis.read(dataBuffer);
				if(read < 1000) {
					dataBuffer = Arrays.copyOfRange(dataBuffer, 0, read - 1);
				}
				streamBuffer.write(dataBuffer);
			}
			data = streamBuffer.toByteArray(); 
		} catch (NullPointerException e) {
			System.err.println("File not found in classpath: " + fileName);
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				if (dis != null) {
					dis.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return data;
	}

}
