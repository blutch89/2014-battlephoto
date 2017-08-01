package ch.blutch.battlephoto.controller.utils;

import java.io.File;

import javax.faces.context.FacesContext;

public class ViewParameters {
	// !!!! A mettre dans un fichier de message pour internationalisation
	public static final String noCategoryMessage = "Toutes les catégories";
	
	// Général
	public static final String appPath = FacesContext.getCurrentInstance().getExternalContext().getContextName();
	
	// Upload
	public static final String uploadDestinationPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/upload");
	
	// Photo
	public static final int photoNbCharInFileName = 20;
	public static final String photoDestinationPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/photos");
	public static final int photoLargeFormat = 1200;
	public static final int photoSmallFormat = 220;
	public static final String photoExtension = "jpg";
}
