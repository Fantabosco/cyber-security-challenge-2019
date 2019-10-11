package it.reply.challenge.fantabosco.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
	
	private HashUtils() {
	}
	
	public static String generateFlag(String solution) {
		String flag = "{FLG:" + it.reply.challenge.fantabosco.utils.HashUtils.getSHA256(solution).toLowerCase() + "}";
		System.out.println("Soluzione: " + flag);
		return flag;
	}
	
	public static String getSHA256(String input) {
		return getHash(input, "SHA-256");
	}
		
	public static String getSHA1(String input) {
		return getHash(input, "SHA-1");
	}
	
	public static String getMD5(String input) {
		return getHash(input, "MD5");
	}
	
	private static String getHash(String input, String algorithm) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
		return bytesToHex(hash);
	}
	
	public static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder();
	    for (int i = 0; i < hash.length; i++) {
		    String hex = Integer.toHexString(0xff & hash[i]);
		    if(hex.length() == 1) {
		    	hexString.append('0');
		    }
		    hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
