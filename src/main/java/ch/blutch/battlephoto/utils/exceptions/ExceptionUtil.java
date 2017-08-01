package ch.blutch.battlephoto.utils.exceptions;

import java.io.IOException;

import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class ExceptionUtil {

	public static void handleGlobalException(Throwable t) {
		// Redirige l'utilisateur sur la page myphotos
//		try {
//			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//			ec.redirect("/" + ec.getContextName().toLowerCase() + "/exception");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		 final FacesContext fc = FacesContext.getCurrentInstance();
         final NavigationHandler nav = fc.getApplication().getNavigationHandler();
         
         nav.handleNavigation(fc, null, "/web-pages/exception.xhtml");
	}
	
}
