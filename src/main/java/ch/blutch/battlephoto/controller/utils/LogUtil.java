package ch.blutch.battlephoto.controller.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.faces.context.FacesContext;

public class LogUtil {

	public static void write(String text) {
		try {
			FileWriter fw = new FileWriter(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "test.txt", true);
			fw.write(text + "\r\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
