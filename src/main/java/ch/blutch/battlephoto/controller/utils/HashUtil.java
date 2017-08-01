package ch.blutch.battlephoto.controller.utils;

import java.math.BigInteger;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashUtil {

	public static String getHash(String text) {
		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
		String digest = bcpe.encode(text);
		
		return digest;
	}
	
}
