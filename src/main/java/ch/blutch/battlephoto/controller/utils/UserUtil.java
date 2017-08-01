package ch.blutch.battlephoto.controller.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {

	public static String getUsername() {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
		
		return username;
	}
	
	public static boolean isLogged() {
		return getUsername().equals("anonymousUser") ? false : true;
	}
	
	public static boolean isValidEmail(String email) {
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}
	
}
