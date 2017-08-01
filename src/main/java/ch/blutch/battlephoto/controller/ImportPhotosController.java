package ch.blutch.battlephoto.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import ch.blutch.battlephoto.controller.utils.FileUtil;
import ch.blutch.battlephoto.controller.utils.ImageUtil;
import ch.blutch.battlephoto.controller.utils.UserUtil;
import ch.blutch.battlephoto.controller.utils.ViewParameters;

@ManagedBean
@RequestScoped
@Controller
@Scope("request")
public class ImportPhotosController {
	
	@PostConstruct
	public void postConstruct() {
		loadParameters();
	}
	
	private void loadParameters() {
		Map<String, String> requestParameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		String destinationFilename = event.getFile().getFileName().replaceAll(" ", "");
		String destinationPath = ViewParameters.uploadDestinationPath + "/" + UserUtil.getUsername() + "/" + destinationFilename;
		File destinationFile = new File(destinationPath);
		
		// Création de tous les dossiers nécessaires
		if (!destinationFile.exists()) {
			destinationFile.getParentFile().mkdirs();
		}
		
		try (BufferedInputStream bis = new BufferedInputStream(event.getFile().getInputstream());
				FileOutputStream bos = new FileOutputStream(destinationFile);) {
			
			// Upload de l'image
			byte buffer[] = new byte[512 * 1024];
			int len;

			while ((len = bis.read(buffer)) != -1){
				bos.write(buffer, 0, len);
			}
			
			// Affichage du message			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Les fichiers ont été uploadés.");
	        FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (IOException e) {
			e.printStackTrace();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Echec", "Les fichiers n'ont pas pu être uploadés");
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
    }
}
