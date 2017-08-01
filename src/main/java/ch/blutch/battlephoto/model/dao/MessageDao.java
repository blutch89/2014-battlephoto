package ch.blutch.battlephoto.model.dao;

import java.util.List;

import ch.blutch.battlephoto.model.entity.Category;
import ch.blutch.battlephoto.model.entity.Message;
import ch.blutch.battlephoto.model.entity.Photo;

public interface MessageDao {

	// Mettre les méthodes spécifiques dans cette partie
	public Message findMessageById(String messageId);
	
}
