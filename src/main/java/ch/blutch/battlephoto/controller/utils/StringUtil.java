package ch.blutch.battlephoto.controller.utils;

public class StringUtil {
	
	public static String generateRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890"; 
        StringBuffer pass = new StringBuffer();
        
        for(int x=0; x < length; x++)   {
           int i = (int)Math.floor(Math.random() * (chars.length() -1));
           pass.append(chars.charAt(i));
        }
        
        return pass.toString();
	}
}
