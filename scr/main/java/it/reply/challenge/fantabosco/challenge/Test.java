package it.reply.challenge.fantabosco.challenge;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Test {

	public static void main(String[] args) {
		String hex = "8a53e0a87320cb0c1c723971b6cab1c9";

		System.out.println(convertHexToCharset(hex, StandardCharsets.ISO_8859_1));
		System.out.println(convertHexToCharset(hex, StandardCharsets.US_ASCII));
		System.out.println(convertHexToCharset(hex, StandardCharsets.UTF_16));
		System.out.println(convertHexToCharset(hex, StandardCharsets.UTF_16BE));
		System.out.println(convertHexToCharset(hex, StandardCharsets.UTF_8));
	}
	
	private static String convertHexToCharset(String hex, Charset charset) {
		ByteBuffer buff = ByteBuffer.allocate(hex.length()/2);
		for (int i = 0; i < hex.length(); i+=2) {
		    buff.put((byte)Integer.parseInt(hex.substring(i, i+2), 16));
		}
		buff.rewind();
		return Charset.forName(charset.name()).decode(buff).toString();
	}

}
